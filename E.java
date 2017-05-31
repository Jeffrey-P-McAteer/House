import java.util.function.*;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.net.URLEncoder;

/**
 * An Enviroment class (Name shortened due to laziness)
 * which reads multiple (That's a lie, I've only writte one) sources
 * for weather data given a location stored in final variables.
 */
public class E {
  
  // For debugging weatehr APIs
  public static void main(String... args) {
    E.fetch();
    System.out.printf("Today is %s.\nThe %s wind is %d mph.\nThe temperature is %d\u00B0F.\nThe sun rises at %.2f and sets at %.2f.\n",
                      E.sky, E.windDir, E.windSpeed, E.temp, E.sunriseHour, E.sunsetHour);
  }
  
  public static final String STATE = "virginia";
  public static final String ST = "va";
  public static final String CITY = "fredericksburg";
  public static final String ZIP = "22407";
  
  public static double hourFetched = -1;
  // "Partly sunny", "Mostly cloudy in the morning then becoming partly sunny", "Mostly clear"
  public static String sky = null;
  // north, south, northeast....
  public static String windDir = null;
  // in miles per hour
  public static int windSpeed = -1;
  public static int temp = -1000;
  public static double sunriseHour = -1.0;
  public static double sunsetHour = -1.0;
  
  public static void fetch() {
    double now = Util.nowHour();
    // if fetched < 15 min ago, skip
    if (now - hourFetched < 15.0 / 60.0) {
      return;
    }
    hourFetched = now;
    
    BooleanSupplier[] fetchers = new BooleanSupplier[] {
      E::fetchYahooWeather,
      E::fetchOpenWeatherMapWeather,
      //E::fetchBingWeather,
      E::fetchDefaultData,
    };
    for (BooleanSupplier fetcher : fetchers) {
      if (fetcher.getAsBoolean()) {
        break;
      }
    }
  }
  
  /** Public interface everyone uses */
  
  public static boolean isSunrise() { return Math.abs(Util.nowHour() - sunriseHour) < 0.1; }
  
  public static boolean isSunset() { return Math.abs(Util.nowHour() - sunsetHour) < 0.1; }
  
  public static boolean isDark() { return Util.nowHour() < sunriseHour - 0.5 || Util.nowHour() > sunsetHour + 0.5; }
  
  public static boolean sunIsHidden() {
    String s = skyAdjective();
    return s.indexOf("cloudy") > -1 || s.indexOf("rainy") > -1;
  }
  
  public static boolean sunIsLowInSky() {
    if (sunIsHidden()) return false;
    return Math.abs(Util.nowHour() - sunsetHour)  < 2.0 ||
           Math.abs(Util.nowHour() - sunriseHour) < 2.0;
  }
  
  public static String skyAdjective() {
    String[] adjectives = new String[] {
      "sunny", "cloudy", "clear", "rainy"
    };
    for (String adjective : adjectives) {
      if (sky.contains(adjective)) return adjective;
    }
    
    if (sky.contains("rain") || sky.contains("shower")) return "rainy";
    
    new Exception("Unknown sky description: "+ sky).printStackTrace();
    
    return "bland";
  }
  
  public static String skyVisualAdjective() {
    String desc = skyAdjective();
    if      (desc.equals("cloudy")) return "grey";
    else if (desc.equals("rainy"))  return "grey";
    
    if (isDark()) return Util.rand("black", "deep, dark blue");
    
    if (isSunset())  return Util.rand("orange", "amber");
    if (isSunrise()) return Util.rand("orange", "golden");
    
    return Util.rand("blue", "azure"); // it's always blue, right?
  }
  
  public static String tempAdjective() {
    if      (temp <= 0)   return Util.rand("freezing", "icy");
    else if (temp <= 30)  return Util.rand("freezing", "icy");
    else if (temp <= 50)  return Util.rand("cold",     "chilly");
    else if (temp <= 70)  return Util.rand("brisk",    "cool");
    else if (temp <= 80)  return Util.rand("warm");
    else if (temp <= 90)  return Util.rand("hot");
    else if (temp <= 100) return Util.rand("burning");
    else                  return Util.rand("burning");
  }
  
  /** The ugly business of parsing web requests */
  
  private static boolean fetchYahooWeather() {
    try {
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setNamespaceAware(true);
      SAXParser parser = spf.newSAXParser();
      
      String yql_format = String.format("select %%s from %%s where woeid in (select woeid from geo.places(1) where text=\"%s, %s\")", CITY, ST);
      
      /* Fetch Wind Data (temp, windDir, and windSpeed) */
      String yql_wind = String.format(yql_format, "wind", "weather.forecast");
      String url_wind = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=xml&u=f", URLEncoder.encode(yql_wind, "UTF-8"));
      
      DefaultHandler wind_handler = new DefaultHandler() {
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
          //System.out.printf("uri=%s\nlocalName=%s\nqName=%s\nattributes=%s\n\n", uri, localName, qName, attributes);
          if (!qName.equals("yweather:wind")) return;
          
          temp = Integer.parseInt(attributes.getValue("chill"));
          
          int dir = Integer.parseInt(attributes.getValue("direction")); // number from 0-359 indicating direction
          // I began writing an if tree, then remembered I was lazy.
          String[] dir_words = new String[] {
            "east", "northeast", "north", "northwest", "west", "southwest", "south", "southeast"
          };
          windDir = dir_words[dir/45];
          
          windSpeed = Integer.parseInt(attributes.getValue("speed")); // speed in mph
        }
      };
      parser.parse(url_wind, wind_handler);
      
      /* Fetch Atronomy Data (sunriseHour and sunsetHour) */
      String yql_astro = String.format(yql_format, "astronomy", "weather.forecast");
      String url_astro = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=xml&u=f", URLEncoder.encode(yql_astro, "UTF-8"));
      
      DefaultHandler astro_handler = new DefaultHandler() {
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
          //System.out.printf("uri=%s\nlocalName=%s\nqName=%s\nattributes=%s\n\n", uri, localName, qName, attributes);
          if (!qName.equals("yweather:astronomy")) return;
          
          sunriseHour = Util.parseTime(attributes.getValue("sunrise"));
          sunsetHour = Util.parseTime(attributes.getValue("sunset"));
          
        }
      };
      parser.parse(url_astro, astro_handler);
      
      /* Fetch Description Data (sky) */
      String yql_sky = String.format(yql_format, "item.condition.text", "weather.forecast");
      String url_sky = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=xml&u=f", URLEncoder.encode(yql_sky, "UTF-8"));
      
      DefaultHandler sky_handler = new DefaultHandler() {
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
          //System.out.printf("uri=%s\nlocalName=%s\nqName=%s\nattributes=%s\n\n", uri, localName, qName, attributes);
          if (!qName.equals("yweather:condition")) return;
          
          sky = attributes.getValue("text").toLowerCase();
          
        }
      };
      parser.parse(url_sky, sky_handler);
      
      return E.sky != null;
      
    } catch (java.net.UnknownHostException uhe) {
      if (Data.DEBUG) System.err.println("You are offline!");
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  
  private static boolean fetchOpenWeatherMapWeather() {
    // Key: &APPID=0dd3b4d82fad93deaab50d094e7094f1
    
    return false;
  }
  
  // Feeds fake data in as a last resort
  private static boolean fetchDefaultData() {
    if (!Data.DEBUG) return false;
    System.err.println("We cannot fetch from any online sources, so default weather data has been injected.");
    sky = Util.rand("partly sunny", "mostly cloudy", "rainy", "mostly sunny");
    windDir = Util.rand("north", "south", "east", "west");
    windSpeed = Util.rand(0, 5, 10, 15);
    temp = Util.rand(40, 50, 60, 70, 80);
    sunriseHour = 6.5;
    sunsetHour = 20.0;
    return true;
  }
  
}