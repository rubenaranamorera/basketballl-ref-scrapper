import java.util.HashMap;
import java.util.Map;

public class Utils {

  public static final Map<String, String> TEAMS_MAP;
  public static final Map<String, String> MONTHS;
  public static final Map<String, String> COMPLETE_MONTHS;

  static {
    MONTHS = new HashMap<>();
    MONTHS.put("Jan", "01");
    MONTHS.put("Feb", "02");
    MONTHS.put("Mar", "03");
    MONTHS.put("Apr", "04");
    MONTHS.put("May", "05");
    MONTHS.put("Jun", "06");
    MONTHS.put("Jul", "07");
    MONTHS.put("Aug", "08");
    MONTHS.put("Sep", "09");
    MONTHS.put("Oct", "10");
    MONTHS.put("Nov", "11");
    MONTHS.put("Dec", "12");

    COMPLETE_MONTHS = new HashMap<>();
    COMPLETE_MONTHS.put("january", "01");
    COMPLETE_MONTHS.put("february", "02");
    COMPLETE_MONTHS.put("march", "03");
    COMPLETE_MONTHS.put("april", "04");
    COMPLETE_MONTHS.put("may", "05");
    COMPLETE_MONTHS.put("june", "06");
    COMPLETE_MONTHS.put("july", "07");
    COMPLETE_MONTHS.put("august", "08");
    COMPLETE_MONTHS.put("september", "09");
    COMPLETE_MONTHS.put("october", "10");
    COMPLETE_MONTHS.put("november", "11");
    COMPLETE_MONTHS.put("december", "12");
  }

  static {
    TEAMS_MAP = new HashMap<>();
    TEAMS_MAP.put("Atlanta Hawks", "Atlanta");
    TEAMS_MAP.put("Boston Celtics", "Boston");
    TEAMS_MAP.put("Charlotte Bobcats", "Charlotte");
    TEAMS_MAP.put("Chicago Bulls", "Chicago");
    TEAMS_MAP.put("Cleveland Cavaliers", "Cleveland");
    TEAMS_MAP.put("Dallas Mavericks", "Dallas");
    TEAMS_MAP.put("Denver Nuggets", "Denver");
    TEAMS_MAP.put("Detroit Pistons", "Detroit");
    TEAMS_MAP.put("Golden State Warriors", "Golden State");
    TEAMS_MAP.put("Houston Rockets", "Houston");
    TEAMS_MAP.put("Indiana Pacers", "Indiana");
    TEAMS_MAP.put("Los Angeles Clippers", "L.A. Clippers");
    TEAMS_MAP.put("Los Angeles Lakers", "L.A. Lakers");
    TEAMS_MAP.put("Memphis Grizzlies", "Memphis");
    TEAMS_MAP.put("Miami Heat", "Miami");
    TEAMS_MAP.put("Milwaukee Bucks", "Milwaukee");
    TEAMS_MAP.put("Minnesota Timberwolves", "Minnesota");
    TEAMS_MAP.put("New Jersey Nets", "Brooklyn");
    TEAMS_MAP.put("New Orleans/Oklahoma City Hornets", "New Orleans");
    TEAMS_MAP.put("New York Knicks", "New York");
    TEAMS_MAP.put("Orlando Magic", "Orlando");
    TEAMS_MAP.put("Philadelphia 76ers", "Philadelphia");
    TEAMS_MAP.put("Phoenix Suns", "Phoenix");
    TEAMS_MAP.put("Portland Trail Blazers", "Portland");
    TEAMS_MAP.put("Sacramento Kings", "Sacramento");
    TEAMS_MAP.put("San Antonio Spurs", "San Antonio");
    TEAMS_MAP.put("Seattle SuperSonics", "Seattle"); // WTFFFF no games!
    TEAMS_MAP.put("Toronto Raptors", "Toronto");
    TEAMS_MAP.put("Utah Jazz", "Utah");
    TEAMS_MAP.put("Washington Wizards", "Washington");
  }

  public static String normalize(String str) {
    if (str.length() == 1) {
      str = "0" + str;
    }
    return str;
  }
}
