import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.time.LocalDateTime;

/**
 * Describes everything which exists using enums and anon classes
 */
public enum Room {

// I'm a lazy jerk who doesn't like 600-line classes
// precompile with 'cpp Room.c Room.java', then pipe it through "sed 's/^# /\/\//g'" to clean the new statements
// For Sublime Text users, get proper syntax coloring with View->Syntax->Open All...as->Java->Java

#include "foyer.room"
#include "basement.room"
#include "library.room"
#include "dining_room.room"
#include "pool_deck.room"
#include "kitchen.room"
#include "garden.room"

  Forest     ("Forest",
              "A forest which is supremely underprogrammed. Yell at jeff and tell him the only things here are: %s",
  new Item[]  {
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
  
  // constructors & methods for above enums
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