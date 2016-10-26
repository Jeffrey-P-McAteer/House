import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.time.LocalDateTime;

/**
 * Every anon instance of this is required to implement action(2)
 * This acts as a structure for every item in existence.
 */
public abstract class Item {
  String name;
  String[] invocations;
  String description;
  
  public Item(String name, String[] invocations, String description) {
    this.name = name;
    this.invocations = invocations;
    this.description = description;
  }
  
  public abstract String action(Guest guest, Action action);
}