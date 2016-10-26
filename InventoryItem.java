import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.time.LocalDateTime;

/**
 * To be honest I don't know what I was doing with this.
 */
public abstract class InventoryItem extends Item {
  public InventoryItem(String name, String[] invocations, String description) {
    super(name, invocations, description);
  }
  public abstract String useOn(Guest guest, Item item);
}