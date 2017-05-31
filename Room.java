//1 "Room.c"
//1 "<built-in>"
//1 "<command-line>"
//31 "<command-line>"
//1 "/usr/include/stdc-predef.h" 1 3 4
//32 "<command-line>" 2
//1 "Room.c"
import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.time.LocalDateTime;




public enum Room {






//1 "foyer.room" 1
Foyer ("Foyer",
            "You are in a well-lit foyer in which you see:%s"+
            "It all looks very inviting.",
new Item[] {
  new Item("Dining Room Door",
    new String[]{"dining", "east", "east door", "dining room"},
    "A pair of double doors to the east, leading to the dining hall.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("A pair of heavy oak doors.");
        case Go:
        case Open:
          guest.enterRoom(DiningHall);
          return "";
        default:
          return "You can't do that to a door!";
      }
    }
  },
  new Item("Deck Door",
    new String[]{"pool", "deck", "west", "west door", "outside"},
    "A pair of double doors to the west, leading to the pool deck.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return "A pair of light blue metal and glass doors." +
                 (E.isSunset()? " The "+E.skyVisualAdjective()+" sky is visible beyond." : "") +
                 (!E.sunIsHidden()? " The "+Util.rand("","golden ","bright ","fiery ")+"sun shines through the glass"+
                    (E.sunIsLowInSky() && Util.now().getHour() > 12? " and "+Util.rand("bounces","jumps")+" across the floor." : ".") : "");
        case Go:
        case Open:
          guest.enterRoom(PoolDeck,
            guest.name+" walks onto the "+PoolDeck+".",
            "You "+Util.rand("walk","stride")+" onto the "+PoolDeck+".",
            guest.name+" walks in from the "+guest.location+".");
          return "";
        default:
          return "You can't do that to a door!";
      }
    }
  },
  new Item("West Stairs",
    new String[]{"northwest", "northwest stair", "west stair", "northwest stairs", "west stairs", "northwest staircase", "west staircase"},
    "A spiral staircase to the northwest.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("A staircase.");
        case GoUp:
          guest.enterRoom(Library,
            guest.name+" walks upstairs to the "+Library+".",
            "You "+Util.rand("walk","go")+" upstairs to the "+Library+".",
            guest.name+" walks in from the "+guest.location+" downstairs.");
          return "";
        case GoDown:
          guest.enterRoom(Basement,
            guest.name+" walks downstairs to the "+Basement+".",
            "You "+Util.rand("walk","go")+" downstairs to the "+Basement+".",
            guest.name+" walks in from the "+guest.location+" upstairs.");
          return "";
        case Go:
          guest.printf(Util.rand("Would you like to go up or down?"));
          String dir;
          while (true) {
            dir = guest.getInput();
            if (Data.UP.indexOf(dir) > -1) {
              this.action(guest, Action.GoUp);
              break;
            } else if (Data.DOWN.indexOf(dir) > -1) {
              this.action(guest, Action.GoDown);
              break;
            } else if (Data.NO.indexOf(dir) > -1) {
              return Util.rand("You don't do anything.");
            }
            guest.printf(Util.rand("Up or down?"));
          }
          return "";
        default:
          return "You can't do that to a staircase!";
      }
    }
  },
  new Item("East Stairs",
    new String[]{"stair", "stairs", "northeast", "northeast stair", "east stair", "northeast stairs", "east stairs", "northeast staircase", "east staircase"},
    "A spiral staircase to the northeast.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("A staircase.");
        case GoUp:
          guest.enterRoom(Library,
            guest.name+" walks upstairs to the "+Library+".",
            "You "+Util.rand("walk","go")+" upstairs to the "+Library+".",
            guest.name+" walks in from the "+guest.location+" downstairs.");
          return "";
        case GoDown:
          guest.enterRoom(Basement,
            guest.name+" walks downstairs to the "+Basement+".",
            "You "+Util.rand("walk","go")+" downstairs to the "+Basement+".",
            guest.name+" walks in from the "+guest.location+" upstairs.");
          return "";
        case Go:
          guest.printf(Util.rand("Would you like to go up or down?"));
          String dir;
          while (true) {
            dir = guest.getInput();
            if (Data.UP.indexOf(dir) > -1) {
              this.action(guest, Action.GoUp);
              break;
            } else if (Data.DOWN.indexOf(dir) > -1) {
              this.action(guest, Action.GoDown);
              break;
            } else if (Data.NO.indexOf(dir) > -1) {
              return Util.rand("You don't do anything.");
            }
            guest.printf(Util.rand("Up or down?"));
          }
          return "";
        default:
          return "You can't do that to a staircase!";
      }
    }
  },
  new Item("Fireplace",
    new String[]{"fireplace", "fire", "north"},
    "A warm, cozy fire.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand(description, description, "*crackle* *pop* *hisss*", "Is that a bit of used floo powder?");
        default:
          return "You can't do that to a fireplace!";
      }
    }
  },
}),
//18 "Room.c" 2
//1 "basement.room" 1
Basement ("Basement",
            "You are in a basement. You see: %s",
new Item[] {
  new Item("West Stairs",
    new String[]{"northwest", "northwest stair", "west stair", "northwest stairs", "west stairs", "northwest staircase", "west staircase"},
    "A spiral staircase to the northwest.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("A staircase.");
        case GoUp:
          guest.enterRoom(Room.Foyer,
            guest.name+" walks upstairs to the "+Room.Foyer+".",
            "You "+Util.rand("walk","go")+" upstairs to the "+Room.Foyer+".",
            guest.name+" walks in from the "+guest.location+" downstairs.");
          return "";
        case GoDown:




          return "There is nothing downstairs!";
        case Go:
          guest.printf(Util.rand("Would you like to go up or down?"));
          String dir;
          while (true) {
            dir = guest.getInput();
            if (Data.UP.indexOf(dir) > -1) {
              this.action(guest, Action.GoUp);
              break;
            } else if (Data.DOWN.indexOf(dir) > -1) {
              this.action(guest, Action.GoDown);
              break;
            } else if (Data.NO.indexOf(dir) > -1) {
              return Util.rand("You don't do anything.");
            }
            guest.printf(Util.rand("Up or down?"));
          }
          return "";
        default:
          return "You can't do that to a staircase!";
      }
    }
  },
  new Item("East Stairs",
    new String[]{"stair", "stairs", "northeast", "northeast stair", "east stair", "northeast stairs", "east stairs", "northeast staircase", "east staircase"},
    "A spiral staircase to the northeast.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("A staircase.");
        case GoUp:
          guest.enterRoom(Room.Foyer,
            guest.name+" walks upstairs to the "+Room.Foyer+".",
            "You "+Util.rand("walk","go")+" upstairs to the "+Room.Foyer+".",
            guest.name+" walks in from the "+guest.location+" downstairs.");
          return "";
        case GoDown:




          return "There is nothing downstairs!";
        case Go:
          guest.printf(Util.rand("Would you like to go up or down?"));
          String dir;
          while (true) {
            dir = guest.getInput();
            if (Data.UP.indexOf(dir) > -1) {
              this.action(guest, Action.GoUp);
              break;
            } else if (Data.DOWN.indexOf(dir) > -1) {
              this.action(guest, Action.GoDown);
              break;
            } else if (Data.NO.indexOf(dir) > -1) {
              return Util.rand("You don't do anything.");
            }
            guest.printf(Util.rand("Up or down?"));
          }
          return "";
        default:
          return "You can't do that to a staircase!";
      }
    }
  },
  new Item("Fireplace",
    new String[]{"fire", "fireplace", "north"},
    "A fireplace.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand(description, description, "*crackle* *pop* *hisss*", "Is that a bit of used floo powder?");
        default:
          return "You can't do that to a fire!";
      }
    }
  },
}),
//19 "Room.c" 2
//1 "library.room" 1
Library ("Library",
            "A library. It smells of knowledge and: %s",
new Item[] {
  new Item("West Stairs",
    new String[]{"northwest", "northwest stair", "west stair", "northwest stairs", "west stairs", "northwest staircase", "west staircase"},
    "A spiral staircase to the northwest.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("A staircase.");
        case GoUp:




          return "There is nothing upstairs! (yet, it's being programmed)";
        case GoDown:
          guest.enterRoom(Room.Foyer,
            guest.name+" walks downstairs to the "+Room.Foyer+".",
            "You "+Util.rand("walk","go")+" downstairs to the "+Room.Foyer+".",
            guest.name+" walks in from the "+guest.location+" upstairs.");
          return "";
        case Go:
          guest.printf(Util.rand("Would you like to go up or down?"));
          String dir;
          while (true) {
            dir = guest.getInput();
            if (Data.UP.indexOf(dir) > -1) {
              this.action(guest, Action.GoUp);
              break;
            } else if (Data.DOWN.indexOf(dir) > -1) {
              this.action(guest, Action.GoDown);
              break;
            } else if (Data.NO.indexOf(dir) > -1) {
              return Util.rand("You don't do anything.");
            }
            guest.printf(Util.rand("Up or down?"));
          }
          return "";
        default:
          return "You can't do that to a staircase!";
      }
    }
  },
  new Item("East Stairs",
    new String[]{"stair", "stairs", "northeast", "northeast stair", "east stair", "northeast stairs", "east stairs", "northeast staircase", "east staircase"},
    "A spiral staircase to the northeast.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("A staircase.");
        case GoUp:




          return "There is nothing upstairs! (yet, it's being programmed)";
        case GoDown:
          guest.enterRoom(Room.Foyer,
            guest.name+" walks downstairs to the "+Room.Foyer+".",
            "You "+Util.rand("walk","go")+" downstairs to the "+Room.Foyer+".",
            guest.name+" walks in from the "+guest.location+" upstairs.");
          return "";
        case Go:
          guest.printf(Util.rand("Would you like to go up or down?"));
          String dir;
          while (true) {
            dir = guest.getInput();
            if (Data.UP.indexOf(dir) > -1) {
              this.action(guest, Action.GoUp);
              break;
            } else if (Data.DOWN.indexOf(dir) > -1) {
              this.action(guest, Action.GoDown);
              break;
            } else if (Data.NO.indexOf(dir) > -1) {
              return Util.rand("You don't do anything.");
            }
            guest.printf(Util.rand("Up or down?"));
          }
          return "";
        default:
          return "You can't do that to a staircase!";
      }
    }
  },
  new Item("Fireplace",
    new String[]{"fire", "fireplace", "north"},
    "A fireplace.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand(description, description, "*crackle* *pop* *hisss*", "Is that a bit of used floo powder?");
        default:
          return "You can't do that to a fire!";
      }
    }
  },
  new Item("4 Bookcases",
    new String[]{"books", "bookcase", "bookcases", "bookshelves", "shelves"},
    "4 Bookcases jutting out from the south wall.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("Ah, knowledge.", "There are many books.");
        default:
          return "You can't do that to a bookcase!";
      }
    }
  },
}),
//20 "Room.c" 2
//1 "dining_room.room" 1
DiningHall ("Dining Hall",
            "A large dining room with a steepled roof high above you.\nFar below the roof you observe:%s",
new Item[] {
  new Item("Foyer Door",
    new String[]{"foyer", "south west", "south west door", "southwest", "southwest door"},
    "A pair of double doors to the southwest, leading to the foyer.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("A pair of heavy oak doors.");
        case Go:
        case Open:
          guest.enterRoom(Room.Foyer);
          return "";
        default:
          return "You can't do that to a door!";
      }
    }
  },
  new Item("Kitchen Door",
    new String[]{"kitchen", "kitchen door", "northwest", "northwest door", "north west", "north west door"},
    "A door to the northwest, leading to the kitchen.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("It's a door. Behind it you smell something absolutely delicious.");
        case Go:
        case Open:
          guest.enterRoom(Room.Kitchen);
          return "";
        default:
          return "You can't do that to a door!";
      }
    }
  },
  new Item("Tables",
    new String[]{"table", "tables"},
    "A pair of long tables running the length of the room.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("Two oak tables where guests may sit, eat, and make merry. (merriness still in beta)",
                      "The tables are made of the same rich oak as the doors.");
        case Sit:
          if (guest.currentAction.equals("sitting")) {
            return "You are already sitting down!";
          }
          guest.currentAction = "sitting";
          return Util.rand("You take a seat.", "You sit at one of the tables.");
        case Go:
        case Open:
          guest.enterRoom(Room.Kitchen);
          return "";
        default:
          return "You can't do that to a table!";
      }
    }
  },
  new Item("Dance Floor",
    new String[]{"dance", "floor", "dance floor"},
    "A dance floor to the east of the room.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("A dance floor",
                      "Tell jeff to write better descriptions. (It's midnight, conscience). Aww boo hoo. Grab some coffee and get back to line 412. Also, write in a DJ booth.");
        case Go:
        case Dance:
          if (guest.currentAction.equals("dancing")) {
            return "You are already dancing!";
          }
          guest.currentAction = "dancing";
          return Util.rand("You waltz onto the dance floor...");
        default:
          return "You can't do that!";
      }
    }
  },
}),
//21 "Room.c" 2
//1 "pool_deck.room" 1
PoolDeck ("Pool Deck",
            "A deck overlooking a cliff face. You see: %s",
new Item[] {
  new Item("Foyer Door",
    new String[]{"foyer", "south east", "south east door", "southeast", "southeast door"},
    "A pair of double doors to the southeast, leading to the foyer.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("A pair of light blue metal and glass doors.");
        case Go:
        case Open:
          guest.enterRoom(Room.Foyer);
          return "";
        default:
          return "You can't do that to a door!";
      }
    }
  },
  new Item("Kitchen Door",
    new String[]{"kitchen", "north east", "north east door", "northeast", "northeast door"},
    "A door to the northeast, leading to the kitchen.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("A single door through which you can smell something delicious.");
        case Go:
        case Open:
          guest.enterRoom(Room.Kitchen);
          return "";
        default:
          return "You can't do that to a door!";
      }
    }
  },
  new Item("Pool",
    new String[]{"pool", "water", "west"},
    "A pool to the west overlooking the cliff.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand(description);
        case Go:
        case Swim:
          if (guest.currentAction != null && guest.currentAction.toLowerCase().indexOf("swimming") > -1) {
            return "You are already " + guest.currentAction + "!";
          }
          boolean dressedToSwim = false;
          for (String suit : Data.SWIMMING_APPAREL) {
            if (guest.clothing.indexOf(suit) > -1) {
              dressedToSwim = true;
              break;
            }
          }
          if (dressedToSwim) {
            guest.currentAction = "swimming";
            return "You "+Util.rand("step","dive","jump")+" into the pool.";
          }
          return String.format(Util.rand("You can't swim in %s!",
                                    "%s were not designed for swimming."), guest.clothing);
        default:
          return "You can't do that to a pool!";
      }
    }
  },
  new Item("View",
    new String[]{"view", "landscape", "lagoon", "cliff", "cliffs"},
    "A spectacular view of a lagoon surrounded on all but one side by tall cliffs.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return "Cerulean waters are wrapped up in a gigantic circular cliff protecting their beaches. "+
                 "In the center you see a lagoon with some sort of pier connecting it to the beach. "+
                 (E.sunIsHidden()? "A shame the sun isn't out now." : "The sun sparkles over the water like sugar-coated gumdrops.")+" "+
                 "The "+E.skyVisualAdjective()+" sky above curls about itself, as if excited and restless.";
        default:
          return "You can't do that to thin air!"+Util.rand("", " (or photons, for that matter)");
      }
    }
  },
}),
//22 "Room.c" 2
//1 "kitchen.room" 1
Kitchen ("Kitchen",
            "A kitchen filled with deliciousness.\nInside, you see: %s",
new Item[] {
  new Item("Dining Hall Door",
    new String[]{"dining", "hall", "dining hall", "east", "east door"},
    "A door to the east leading to the dining hall.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("This is where you go with your food.");
        case Go:
        case Open:
          guest.enterRoom(Room.DiningHall);
          return "";
        default:
          return "You can't do that to a door!";
      }
    }
  },
  new Item("Pool Deck Door",
    new String[]{"pool", "deck", "pool deck", "west", "west door"},
    "A door to the west leading to the pool deck.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand(description);
        case Go:
        case Open:
          guest.enterRoom(Room.PoolDeck);
          return "";
        default:
          return "You can't do that to a door!";
      }
    }
  },
  new Item("Garden Door",
    new String[]{"garden", "garden door", "north", "north door"},
    "A door to the north leading to the garden.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand(description);
        case Go:
        case Open:
          guest.enterRoom(Room.Garden,
            guest.name+" walks out to the "+Room.Garden+".",
            "You "+Util.rand("walk","stride")+" out to the "+Room.Garden+".",
            guest.name+" walks out from the "+guest.location+".");
          return "";
        default:
          return "You can't do that to a door!";
      }
    }
  },
}),
//23 "Room.c" 2
//1 "garden.room" 1
Garden ("Garden",
            "A garden where all the food is grown. You look about and see: %s",
new Item[] {
  new Item("Kitchen Door",
    new String[]{"garden", "garden door", "south", "south door"},
    "A door to the south leading to the kitchen.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand(description);
        case Go:
        case Open:
          guest.enterRoom(Room.Kitchen);
          return "";
        default:
          return "You can't do that to a door!";
      }
    }
  },
  new Item("Forest Gate",
    new String[]{"forest", "gate", "north", "north"},
    "A door to the north leading to the forest.") {
    public String action(Guest guest, Action action) {
      switch (action) {
        case Look:
          return Util.rand("An iron gate.")+
                  (E.windSpeed >= 15? " It swings softly in the wind." : "");
        case Go:
        case Open:
          guest.enterRoom(Room.Forest);
          return "";
        default:
          return "You can't do that to a gate!";
      }
    }
  },
}),
//24 "Room.c" 2

  Forest ("Forest",
              "A forest which is supremely underprogrammed. Yell at jeff and tell him the only things here are: %s",
  new Item[] {
    new Item("Garden Gate",
      new String[]{"gate", "garden", "garden gate", "south", "south gate"},
      "A gate to the south leading to the garden.") {
      public String action(Guest guest, Action action) {
        switch (action) {
          case Look:
            return Util.rand("An iron gate.")+
                    (E.windSpeed >= 15? " It swings softly in the wind." : "");
          case Go:
          case Open:
            guest.enterRoom(Garden);
            return "";
          default:
            return "You can't do that to a gate!";
        }
      }
    },
  });


  String name;
  String description;
  ArrayList<Guest> roomPeople;
  ArrayList<Item> roomItems;

  Room(String name, String description, Item[] items) {
    this.name = name;
    roomPeople = new ArrayList<Guest>();
    roomItems = new ArrayList<Item>();
    StringBuilder itemDescriptions = new StringBuilder();
    itemDescriptions.append("\n");
    for (Item item : items) {
      roomItems.add(item);
      itemDescriptions.append(" * ");
      itemDescriptions.append(item.description);
      itemDescriptions.append("\n");
    }
    this.description = String.format(description, itemDescriptions.toString());
  }

  void addGuest(Guest g, String arrivalmessage, Object... args) {
    roomPeople.forEach((guest) -> guest.printf(arrivalmessage, args));
    roomPeople.add(g);
  }

  void removeGuest(Guest g, String leavingMessage, Object... args) {
    roomPeople.remove(g);
    roomPeople.forEach((guest) -> guest.printf(leavingMessage, args));
  }

  Item getItem(String name) {
    name = name.toLowerCase();
    for (Item item : roomItems) {
      for (String invocation : item.invocations) {
        if (name.equals(invocation)) {
          return item;
        }
      }
    }
    return null;
  }

  Guest getGuest(String name) {
    for (Guest g : roomPeople) {
      String lowName = g.name.toLowerCase();
      if (name.endsWith(lowName) || name.startsWith(lowName)) {
        return g;
      }
    }
    return null;
  }

  public String toString() {
    return this.name;
  }

}
