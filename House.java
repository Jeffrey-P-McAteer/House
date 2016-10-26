/**
 * Useful resources:
 *   http://www.mke-skywarn.org/hail_wind.htm
 *     Look at wind "VISUAL CLUES AND DAMAGE EFFECTS" section
 * ToDo:
 *   Inventory
 *   Fix chat input interruptions (maybe only print out at end of parseInput() ? )
 *   
 */

import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.time.LocalDateTime;

public class House {
  
  public static HashMap<String, Guest> rememberedGuests = new HashMap<String, Guest>();
  
  public static ArrayList<Guest> guests = new ArrayList<Guest>();
  
  // Thread to check weather & kick off people who aren't active
  public static Thread watchThread = null;
  
  public static void main(String... args) throws Exception {
    ServerSocket server = new ServerSocket(Data.PORT);
    if (new File(Data.MEMORY).exists()) {
      try {
        FileInputStream fin = new FileInputStream(Data.MEMORY);
        ObjectInputStream ois = new ObjectInputStream(fin);
        @SuppressWarnings("unchecked")
        HashMap<String, Guest> rem = (HashMap<String, Guest>) ois.readObject();
        rememberedGuests = rem;
        fin.close();
      } catch (Exception e) {
        e.printStackTrace();
        saveToFile();
      }
    }
    E.fetch();
    launchWatchThread();
    
    System.out.println("House has been built, memory stored in "+ Data.MEMORY +" for OS type "+ System.getProperty("os.name"));
    System.out.printf("Today is %s.\nThe %s wind is %d mph.\nThe temperature is %d\u00B0F.\nThe sun rises at %.2f and sets at %.2f.\n",
                      E.sky, E.windDir, E.windSpeed, E.temp, E.sunriseHour, E.sunsetHour);
    System.out.println("Listening for connections on port " + Data.PORT);
    while (true) {
      try {
        handleConnection(server.accept());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  public static void launchWatchThread() {
    if (watchThread != null) return;
    
    watchThread = new Thread() {
      public void run() {
        while (true) {
          try {
            Thread.sleep(Data.TIMEOUT_MS);
            E.fetch();
            long now = System.currentTimeMillis();
            for (Guest g : guests) {
              if (now - g.lastInputMS > Data.TIMEOUT_MS) {
                g.printf(Util.rand(
                  "You are suddenly swept away by a giant gust of wind!",
                  "You hear a funny noise....\n*you have been bludgeoned by a troll*",
                  "Without any warning whatsoever, you burst into flames. Darn wizard kids."
                ));
                g.interrupt();
                g.in.close();
                g.socket.close();
                guests.remove(g);
              }
            }
            //saveToFile();
          } catch (Exception e) {
            //e.printStackTrace();
          }
        }
      }
    };
    watchThread.start();
  }
  
  public static void log(String message) {
    try {
      FileOutputStream fout = new FileOutputStream(Data.LOG);
      fout.write(message.getBytes());
      fout.write("\n".getBytes());
    } catch (Exception e) { e.printStackTrace(); }
  }
  
  public static void saveToFile() {
    try {
      FileOutputStream fout = new FileOutputStream(Data.MEMORY);
      ObjectOutputStream oos = new ObjectOutputStream(fout);
      oos.writeObject(rememberedGuests);
      fout.close();
    } catch (Exception e) { e.printStackTrace(); }
  }
  
  public static void handleConnection(Socket s) throws Exception {
    Guest newcomer = new Guest(s);
    newcomer.start();
    guests.add(newcomer);
  }
  
  
}
