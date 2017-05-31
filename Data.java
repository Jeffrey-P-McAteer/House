import java.util.Arrays;
import java.util.List;

/**
 * A class to hold static data, strings, messages, etc...
 */
public class Data {
  
  public static final int PORT = 8888;
  public static final boolean DEBUG = false;
  
  /** Timeout for fetching real-world data. */
  public static final long TIMEOUT_MS = (DEBUG? 2 : 10) * 60 * 1000;
  public static final String MEMORY = DEBUG? "/tmp/housemem" : System.getProperty("user.home")+"/housemem";
  public static final String LOG = DEBUG? "/tmp/house.log" : System.getProperty("user.home")+"/house.log";
  
  public static final String NEW_USER_BANNER = ""+
    "This is a MUD-style simulation of a house, created and maintained by Jeffrey McAteer.\n"+
    ""+
    ""+
    "\n";
    
  public static final String HELP_TEXT = ""+
    "A crimson bird flies by, carrying a scroll tied to its leg.\n"+
    "you hastily untie the scroll (the bird, it appears, is anxious to leave) and begin reading:\n"+
    "  \n"+
    "    Greetings Traveller!\n"+
    "  I foresaw that you would need assistance, and sent my trusty familliar to your aid.\n"+
    "  This document contains the basics of moving about and interacting with this world\n"+
    "  you have stumbled upon."+
    "  This is a house in cyberspace, or the internet, or the 'cloud', or whatever the\n"+
    "  most popular misnomer for computer networks is these days.\n"+
    "  You may interact with items by typing commands, usually verb first and then an item.\n"+
    "  Items are sometimes optional.\n"+
    "  typing:\n"+
    "    look\n"+
    "  will cause you to look about the room, but it may also be used with an item:\n"+
    "    look fireplace\n"+
    "  causes you to inspect the fireplace.\n"+
    "  \n"+
    "  Moving around is just as easy:\n"+
    "    go east\n"+
    "  makes you walk in the most easterly direction, assuming there is someplace\n"+
    "  for you to walk to.\n"+
    "    go dining hall\n"+
    "  is usually just as valid if a location has a name and is nearby.\n"+
    "  \n"+
    "  Speaking to others works as you might expect:\n"+
    "    say Hello everyone!\n"+
    "  causes everyone in the room with you to get this message (if your name is Steve):\n"+
    "    Steve: Hello everyone!\n"+
    "  \n"+
    "  There are many more things which may be done here, but my poor bird would\n"+
    "  never be able to carry the volumes of books required to explain it all.\n"+
    "  \n"+
    "  Farewell,\n"+
    "    A travelling wizard\n"+
    "  \n"+
    "As soon as you finish reading, the scroll explodes in a puff of "+Util.rand("red ","grey ","")+"smoke.";
  
  /**
   * The below arrays are used as case-insensitive words which have a similar meaning.
   * This is used for fuzziness when asking questions to the user, so they may reply
   * "yes", "yea", or whatever and we still understand them.
   */
  
  public static final List<String> NO = Arrays.asList(new String[]{
    "no", "nah", "nope", "stop"
  });
  
  public static final List<String> YES = Arrays.asList(new String[]{
    "yes", "yea", "yeah", "yup"
  });
  
  public static final List<String> UP = Arrays.asList(new String[]{
    "up", "upwards", "above", "top"
  });
  
  public static final List<String> DOWN = Arrays.asList(new String[]{
    "down", "downwards", "below", "bottom"
  });
  
  public static final List<String> LEFT = Arrays.asList(new String[]{
    "left"
  });
  
  public static final List<String> RIGHT = Arrays.asList(new String[]{
    "right"
  });
  
  public static final List<String> SWIMMING_APPAREL = Arrays.asList(new String[]{
    "trunks", "swimsuit", "bikini", "bathing suit", "speedo"
  });
  
}