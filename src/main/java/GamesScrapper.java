import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GamesScrapper {

  private static final String BASE_URL = "https://www.basketball-reference.com/leagues/NBA_";
  private static final String GAMES_FILE = "games2016.csv";
  private static final String[] GAMES_CSV_HEADERS = {"GAME_ID", "SEASON", "DAY_OF_WEEK", "MONTH", "DAY", "YEAR", "HOUR", "VISITOR_TEAM", "VISITOR_POINTS", "HOME_TEAM", "HOME_POINTS", "OT", "OTHER"};




  public void loadGames(int fromSeason, int toSeason) throws IOException {

    long pretime = System.currentTimeMillis();

    File file = new File(GAMES_FILE);
    if (!file.exists()) {
      file.createNewFile();
    }

    try (
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(GAMES_FILE));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(GAMES_CSV_HEADERS))
    ) {

      for (int season = fromSeason; season <= toSeason; season++) {
        int currentSeason = season;
        Utils.COMPLETE_MONTHS.keySet().stream()
            .forEach(month -> {
              try {
                Jsoup.connect(getUrl(currentSeason, month))
                    .timeout(5000)
                    .validateTLSCertificates(false)
                    .get()
                    .getElementById("schedule")
                    .getElementsByTag("tr")
                    .stream()
                    .skip(1)
                    .forEach(row -> parseRow(csvPrinter, row, currentSeason));
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println("Scrapped games in: " + (System.currentTimeMillis() - pretime) / 1000);
  }

  private void parseRow(CSVPrinter csvPrinter, Element row, int season) {

    try {
      List<String> csvStrings = new ArrayList();

      row.getElementsByTag("th")
          .stream()
          .forEach(date -> {
            csvStrings.add(date.attr("csk"));
            csvStrings.add(String.valueOf(season));

            Arrays.stream(date.text().split(","))
                .map(String::trim)
                .forEach(val -> {
                  String[] splitValue = val.split(" ");

                  if (splitValue.length > 1) {
                    csvStrings.add(Utils.MONTHS.get(splitValue[0].trim()));
                    csvStrings.add(splitValue[1].trim());
                  } else {
                    csvStrings.add(val);
                  }

                });
          });

      String first = row.getElementsByTag("td")
          .stream()
          .findFirst()
          .map(td -> td.text())
          .get();

      if (!first.split(" ")[1].contains("am") && !first.split(" ")[1].contains("pm")) {
        csvStrings.add("");
      }

      row.getElementsByTag("td")
          .stream()
          .map(td -> td.text())
          .filter(text -> !"Box Score".contains(text))
          .forEach(str -> {
            if (Utils.TEAMS_MAP.containsKey(str)) {
              str = Utils.TEAMS_MAP.get(str);
            }
            csvStrings.add(str);
          });

      csvPrinter.printRecord(csvStrings);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private String getUrl(int season, String month) {
    return BASE_URL + season + "_games-" + month + ".html";
  }
}
