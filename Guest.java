import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.time.LocalDateTime;

/**
 * Each guest is a thread which waits for user input through a network socket.
 * It is possible to interrupt a user while they are typing, which is
 * the reason for the 'listening' action. While listening, a Guest will be notified of events
 * immediately. When not listening, we tell them what they missed when they strike the enter key.
 */
public class Guest extends Thread implements Serializable {
  transient Socket socket;
  transient PrintWriter out;
  transient BufferedReader in;
  transient long lastInputMS;
  transient CountDownLatch nextResponseLatch;
  transient String nextResponse;
  Room location;
  
  String ip;
  boolean unicode; // todo: implement a unicode check/question & use unicode chars when possible for descriptions.
  String name;
  /* if the word "with" appears in currentAction, the remainder of the string must be a valid guest name */
  String currentAction;
  String description;
  /* fuzzy description of what the person is wearing */
  String clothing;
  LinkedList<String> thingsToSay;
  ArrayList<InventoryItem> inventory;
  
  public Guest(Socket s) throws Exception {
    socket = s;
    out = new PrintWriter(socket.getOutputStream(), true);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    ip = socket.getRemoteSocketAddress().toString();
    ip = ip.substring(1, ip.lastIndexOf(":")); // trim forward slash and port
    lastInputMS = System.currentTimeMillis();
    clothing = "a pair of "+Util.rand("jeans","khakis")+" and a "+Util.rand("t-shirt","button down shirt");
    thingsToSay = new LinkedList<String>();
    inventory = new ArrayList<InventoryItem>();
  }
  
  public boolean greet() {
    // check IP, else must prompt for username & description
    Guest remembered = House.rememberedGuests.get(ip);
    
    if (remembered == null) {
      printf(Data.NEW_USER_BANNER);
    }
    
    printf(Util.rand("A butler walks up to you as you enter the house...",
                "A butler strolls your way as you enter..."));
    
    if (remembered == null) {
      location = Room.Foyer;
      
      printf("Butler: "+ Util.rand("I don't believe you have been here before, may I ask your name?",
                              "Ah, a newcomer! May I have your name?"));
      
      boolean notAName;
      do {
        name = getInput();
        if (name == null) return false; // user has left during prompt
        if (name.indexOf("is ") > -1) {
          name = name.substring(name.indexOf("is "), name.length());
        }
        notAName = name.equals("") || Data.NO.indexOf(name.toLowerCase()) > -1 || Data.YES.indexOf(name.toLowerCase()) > -1;
        if (notAName) {
          printf("Butler: "+ Util.rand("your name, please?",
                                    "we can't call you that guy from "+ ip +".",
                                    "what was that?"));
        }
      } while (notAName);
      printf("Butler: "+ Util.rand("Welcome %s!",
                              "Good to see you %s.",
                              "Good day, %s."), name);
      
      printf("Butler: "+ Util.rand("May I have a brief description of your character, should others ask?"));
      boolean notADescription;
      do {
        description = getInput();
        if (description == null) return false; // user has left
        notADescription = description.equals("") || Data.NO.indexOf(description.toLowerCase()) > -1 || Data.YES.indexOf(description.toLowerCase()) > -1;
        if (notADescription) {
          printf("Butler: "+ Util.rand("Surely you don't expect me to believe you are nothing?!"));
        }
      } while (notADescription);
      printf("Butler: Thank you. "+ Util.rand("", "Enjoy your stay."));
      
      House.rememberedGuests.put(ip, this);
      
      House.saveToFile();
    } else {
      location = remembered.location;
      name = remembered.name;
      description = remembered.description;
      
      if (Collections.frequency(House.guests, this) > 1) {
        printf("Butler: "+ Util.rand("It would appear that you are already here!",
                                     "Hey, aren't you already here?"));
        printf("Butler: "+ Util.rand("I'm going to have to ask you to leave, duplicates bring bad luck."));
        
        return false;
      } else {
        printf("Butler: "+ Util.rand("Welcome back %s!",
                              "Good to see you again %s."), name);
      }
      
    }
    
    location.addGuest(this, Util.rand("%s teleported into the room",
                             "A cloud of smoke descends, clearing to reveal %s",
                             "With a loud *"+Util.rand("POP", "CRACK")+"*, %s apparates before you"), name);
    
    if (remembered == null) {
      parseInput("look"); // todo: replace me with a direct call to handleAction()
    }
    
    return true;
  }
  
  public void run() {
    // make sure they can hear us!
    this.handleAction(null, Action.SilentListen, null, null);
    // gets user's name & description if unknown
    if (greet()) {
      this.handleAction(null, Action.Stop, null, null);
      while (parseInput(getInput()));
      this.handleAction(null, Action.SilentListen, null, null);
      location.removeGuest(this, Util.rand("You look around and notice that %s has disappeared",
                                      "%s suddenly explodes into a cloud of smoke"), this.name);
    }
    House.guests.remove(this);
    try {
      socket.close();
    } catch (Exception e) { e.printStackTrace();}
  }
  
  public String getInput() {
    while (thingsToSay.peek() != null) {
      out.println(thingsToSay.remove());
    }
    try {
      String input = in.readLine();
      lastInputMS = System.currentTimeMillis();
      return input.toLowerCase();
    } catch (Exception e) { e.printStackTrace(); }
    return null;
  }
  
  public String getNextResponse() {
    if (nextResponseLatch != null) {
      try { nextResponseLatch.await(); }
      catch (Exception e) { e.printStackTrace(); }
    }
    nextResponseLatch = new CountDownLatch(1);
    try { nextResponseLatch.await(); }
    catch (Exception e) { e.printStackTrace(); }
    nextResponseLatch = null;
    return nextResponse;
  }
  
  public boolean parseInput(String input) {
    if (input == null) return false; // user has left
    String[] words = input.trim().split(" ");
    if (words.length < 1) return true; // user just hit enter
    
    if (nextResponseLatch != null) {
      System.out.println("handling nextReponseLatch");
      nextResponse = input.trim();
      nextResponseLatch.countDown();
      return true;
    }
    
    // detect the most likely action
    Action action = null;
    {
      HashMap<Action, Integer> possibleActions = new HashMap<Action, Integer>();
      for (int i=0; i<words.length; i++) {
        Action ithAction = Action.getAction(String.join(" ", Arrays.copyOfRange(words, 0, i+1) ) );
        Integer oldValue = possibleActions.get(ithAction);
        if (oldValue != null) {
          possibleActions.put(ithAction, oldValue + 1);
        } else {
          possibleActions.put(ithAction, 1);
        }
      }
      for (Action a : possibleActions.keySet()) {
        if (a == null) continue;
        if (action == null) action = a;
        // in the event of a tie, the longer action name takes precedence.
        if (possibleActions.get(a) > possibleActions.get(action) ||
            (possibleActions.get(a) == possibleActions.get(action) && action.toString().length() < a.toString().length()) ) {
          action = a;
        }
      }
    }
    // action is now the most common action as parsed by checking the entire length of input.
    if (action == null) {
      action = Action.Look;
      /*printf(Util.rand("I'm not sure what you'd like to do...",
                  "I don't know how to do that."));
      return true;*/
    }
    
    // detect most likely object of action
    Item object = null;
    {
      HashMap<Item, Integer> possibleObjects = new HashMap<Item, Integer>();
      for (int i=words.length; i >= 0; i--) {
        Item ithItem = location.getItem(String.join(" ", Arrays.copyOfRange(words, i, words.length)));
        Integer oldValue = possibleObjects.get(ithItem);
        if (oldValue != null) {
          possibleObjects.put(ithItem, oldValue + 1);
        } else {
          possibleObjects.put(ithItem, 1);
        }
      }
      for (Item i : possibleObjects.keySet()) {
        if (object == null) object = i;
        if (possibleObjects.get(i) > possibleObjects.get(object)) {
          object = i;
        }
      }
    }
    // object is now populated (it may be null)
    Guest personObject = null;
    if (object == null) {
      String endWords = String.join(" ", Arrays.copyOfRange(words, 0, words.length));
      personObject = location.getGuest(endWords);
    }
    // if object is null, personObject may not be null.
    
    return handleAction(words, action, object, personObject);
  }
  
  public boolean handleAction(String[] words, Action action, Item object, Guest personObject) {
    /** Handle Actions *****************************************************************************/
    switch (action) {
      case Look:
        if (object != null) {
          printf(object.action(this, action));
        } else if (personObject != null) {
          printf(personObject.description);
          printf("%s is wearing %s", personObject.name, personObject.clothing);
          personObject.printf(Util.rand("%s glances your way...",
                                   "You notice %s is looking your way..."), name);
        } else {
          printf(location.description);
          int count = location.roomPeople.size();
          String people = location.roomPeople.toString();
          people = people.substring(1, people.length()-1);
          printf(count < 2? Util.rand("There is","You see")+" %d person here: %s" :
                            Util.rand("There are","You see")+" %d people here: %s", count, people);
        }
        break;
      case SilentListen:
        currentAction = "listening";
        break;
      case Listen:
        if (currentAction != null && currentAction.equals("listening")) {
          printf("You are already listening!");
        } else {
          currentAction = "listening";
          printf("You begin listening...");
        }
        break;
      case Say:
        String things = String.join(" ", Arrays.copyOfRange(words, 1, words.length) );
        if (personObject == null) {
          location.roomPeople.forEach((guest) -> guest.printf("%s: %s", name, things));
        } else {
          printf("you whisper to %s: %s", personObject.name, things);
          personObject.printf("%s whispers to you: %s", name, things);
        }
        break;
      case Open:
        if (object == null) {
          printf(Util.rand("I'm not sure what you want to open..."));
        } else {
          printf(object.action(this, action));
        }
        break;
      case Go:
      case GoUp:
      case GoDown:
        if (object == null) {
          printf(Util.rand("I'm not sure where you want to go..."));
        } else {
          printf(object.action(this, action));
        }
        break;
      case Dance:
        if (personObject != null && !personObject.equals(this)) {
          printf("You ask %s to dance...", personObject.name);
          currentAction = "asking "+personObject+" to dance";
          personObject.printf("%s asks you to dance. Do you accept?", this.name);
          String response = personObject.getNextResponse();
          printf("%s: %s", personObject.name, response);
          if (Data.YES.indexOf(response) > -1) {
            printf("You begin dancing with %s", personObject.name);
            personObject.printf("You begin dancing with %s", this.name);
            currentAction = String.format("dancing with %s", personObject.name);
            personObject.currentAction = String.format("dancing with %s", this.name);
          } else {
            currentAction = null;
          }
          
        } else if (object == null) {
          printf("You begin to dance...");
          currentAction = "dancing";
        } else {
          printf("You can't dance with that!");
        }
        break;
      /*case Write:
        if (object == null) {
          
        } else {
          
        }
        break;*/
      case Sit:
        if (object == null) {
          printf("I'm not sure where you want to sit...");
        } else {
          object.action(this, action);
        }
        break;
      case Swim:
        if (object == null) {
          printf(Util.rand("I'm not sure where you want to swim..."));
        } else {
          printf(object.action(this, action));
        }
        break;
      case Stop:
        if (currentAction != null) {
          String staleAction = currentAction;
          
          printf("You stop %s", currentAction);
          currentAction = null;
          
          int with = staleAction.indexOf("with");
          if (with > -1) {
            String whom = staleAction.substring(with+5, staleAction.length()).toLowerCase();
            Guest other = location.getGuest(whom);
            if (other != null && other.currentAction != null) {
              other.parseInput("stop");
            }
          }
        } else {
          printf("You aren't doing anything!");
        }
        break;
      case Report:
        if (words.length < 2) {
          printf("*BANG*");
          printf("A ghost wearing a police uniform appears in front of you!");
          Util.sleep(200);
          printf("Ghost: Well, have you got a complaint, or did I just leave heaven to see you gaping at me?");
          Util.sleep(200);
          printf("Ghost: No? Well leave me alone!");
          Util.sleep(200);
          printf("*GNAB*");
        } else {
          String line = String.join(" ", Arrays.copyOfRange(words, 1, words.length) );
          House.log(name+": "+line);
        }
        
        break;
      case Help:
        printf(Data.HELP_TEXT);
        break;
      case Quit:
        printf(Util.rand("You leave the house. Come back soon!"));
        return false;
      default:
        printf("Action %s exists, but that "+
               Util.rand("stupid","dumb","idiot")+
               " programmer hasn't told me how to do it.\n"+
               Util.rand("Go yell at him.", "Tell him he's lazy."), action);
        break;
    }
    
    return true;
  }
  
  /* if customMessages is used, the first is a global leaving message, then a personal entry message, then a global entry message */
  public void enterRoom(Room r, String... customMessages) {
    if (currentAction != null) {
      this.handleAction(null, Action.Stop, null, null);
    }
    if (customMessages == null || customMessages.length < 3) {
      location.removeGuest(this, "%s walks into the %s", name, r.name);
      printf("You "+Util.rand("walk","stride")+" into the "+ r.name);
      r.addGuest(this, "%s walks in from the %s", name, location.name);
    } else {
      location.removeGuest(this, customMessages[0]);
      printf(customMessages[1]);
      r.addGuest(this, customMessages[2]);
    }
    location = r;
    this.handleAction(null, Action.Look, null, null);
  }
  
  public void printf(String format, Object... args) {
    String output = String.format(format, args);
    if (output == null || output.equals("null")) {
      new Exception("printf output is null!").printStackTrace();
    }
    if (currentAction != null && currentAction.equalsIgnoreCase("listening")) {
      out.println(output);
    } else {
      thingsToSay.add(output);
    }
  }
  
  public String toString() {
    if (currentAction != null && !currentAction.equals("")) {
      return name +" ("+currentAction+")";
    }
    return name;
  }
  
  public boolean equals(Object o) {
    if (o instanceof Guest) {
      Guest g = (Guest) o;
      return g.name.equalsIgnoreCase(name);
    }
    return false;
  }

  
}