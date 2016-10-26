import java.time.LocalDateTime;

/**
 * Nifty functions used all over
 */
public class Util {
  
  public static void sleep(int ms) {
    try {
      Thread.sleep(ms);
    } catch (Exception e) {}
  }
  
  public static String rand(String... choices) {
    return choices[(int) (Math.random() * choices.length)];
  }
  
  public static int rand(int... choices) {
    return choices[(int) (Math.random() * choices.length)];
  }
  
  public static double rand(double... choices) {
    return choices[(int) (Math.random() * choices.length)];
  }
  
  public static LocalDateTime now() {
    return LocalDateTime.now();
  }
  
  public static double nowHour() {
    return now().getHour() + (now().getMinute() / 60.0);
  }
  
  // Parses string of format "5:30 AM" to 5.5 hours
  public static double parseTime(String hourMinAmPm) {
    String str_hour = hourMinAmPm.split(":")[0];
    String str_min = hourMinAmPm.split(":")[1].split(" ")[0];
    String am_pm = hourMinAmPm.split(":")[1].split(" ")[1];
    int hour = Integer.parseInt(str_hour);
    int min = Integer.parseInt(str_min);
    if (am_pm.equals("pm")) hour += 12;
    return (double) hour + (min / 60.0);
  }
  
}