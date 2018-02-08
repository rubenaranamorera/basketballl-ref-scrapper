import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class OddsScrapper {

  private static final String BASE_URL = "https://www.sportsbookreview.com/betting-odds/nba-basketball/totals/?date=";
  private static final String ODDS_FILE = "odds2016.csv";
  private static final String GAMES_FILE = "games2016.csv";
  private static final String[] ODDS_HEADERS = {
      "YEAR",
      "MONTH",
      "DAY",
      "VISITOR_TEAM",
      "HOME_TEAM",
      "MIN_LINE",
      "MAX_LINE",
      "AVERAGE_LINE"
  };

  private static final int FIRST_SEASON_WITH_DATA = 2007;


  public void loadOdds() {

    long pretime = System.currentTimeMillis();

    new File(ODDS_FILE);

    try (
        Reader reader = new FileReader(GAMES_FILE);
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(ODDS_FILE));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(ODDS_HEADERS))) {

      Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

      String previousYear = "";
      String previousDay = "";
      String previousMonth = "";

      for (CSVRecord record : records) {
        String season = record.get("SEASON");
        String year = record.get("YEAR");
        String day = record.get("DAY");
        String month = record.get("MONTH");

        if (Integer.parseInt(season) < FIRST_SEASON_WITH_DATA) {
          continue;
        }

        if (previousYear.equals(year) && previousDay.equals(day) && previousMonth.equals(month)) {
          continue;
        } else {
          previousYear = year;
          previousDay = day;
          previousMonth = month;
        }

        parseDay(year, day, month, csvPrinter);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void parseDay(String year, String day, String month, CSVPrinter csvPrinter) {
    try {
      Jsoup.connect(getUrl(year, month, day))
          .timeout(15000)
          .validateTLSCertificates(false)
          .get()
          .getElementsByClass("event-holder")
          .stream()
          .map(this::parseGame)
          .forEach(csvStrings -> {
            try {
              csvStrings.add(0, year);
              csvStrings.add(1, Utils.normalize(month));
              csvStrings.add(2, Utils.normalize(day));
              System.out.println(csvStrings);
              csvPrinter.printRecord(csvStrings);
              System.out.println("----------");
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private List<String> parseGame(Element element) {

    System.out.println("-----------------------------------");
    System.out.println("PARSE GAME");

    List<String> list = element.getElementsByClass("team-name")
        .stream()
        .map(Element::text)
        .collect(Collectors.toList());

    System.out.println(list);

    double min = element.getElementsByClass("eventLine-book")
        .stream()
        .flatMap(eventLineBookElement -> eventLineBookElement.getElementsByClass("eventLine-book-value").stream())
        .skip(3)
        .map(Element::text)
        .filter(str -> str.length() >= 3)
        .map(str -> str.substring(0, 3).replaceAll("[^0-9]", " ").trim())
        .peek(str -> System.out.println(str))
        .map(str -> str + ".5")
        .mapToDouble(Double::parseDouble)
        .filter(value -> value > 150 && value < 250)
        .min()
        .getAsDouble();

    double max = element.getElementsByClass("eventLine-book")
        .stream()
        .flatMap(eventLineBookElement -> eventLineBookElement.getElementsByClass("eventLine-book-value").stream())
        .skip(3)
        .map(Element::text)
        .filter(str -> str.length() >= 3)
        .map(str -> str.substring(0, 3).replaceAll("[^0-9]", " ").trim())
        .map(str -> str + ".5")
        .mapToDouble(Double::parseDouble)
        .filter(value -> value > 150 && value < 250)
        .max()
        .getAsDouble();

    double avg = element.getElementsByClass("eventLine-book")
        .stream()
        .flatMap(eventLineBookElement -> eventLineBookElement.getElementsByClass("eventLine-book-value").stream())
        .skip(4)
        .map(Element::text)
        .filter(str -> str.length() >= 3)
        .map(str -> str.substring(0, 3).replaceAll("[^0-9]", " ").trim())
        .map(str -> str + ".5")
        .mapToDouble(Double::parseDouble)
        .filter(value -> value > 150 && value < 250)
        .average()
        .getAsDouble();

    list.add(String.valueOf(min));
    list.add(String.valueOf(max));
    list.add(String.valueOf(avg));

    System.out.println("PARSED GAME");


    return list;

  }

  private String getUrl(String year, String month, String day) {
    String url = BASE_URL + year + Utils.normalize(month) + Utils.normalize(day);
    System.out.println(url);
    return url;
  }



}
