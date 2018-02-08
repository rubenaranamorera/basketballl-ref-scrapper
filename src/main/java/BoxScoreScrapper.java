import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class BoxScoreScrapper {

  private static final String BASE_URL = "https://www.basketball-reference.com/boxscores/";
  private static final String GAMES_FILE = "games2016.csv";
  private static final String BOX_SCORES_FILE = "boxscores2016.csv";
  private static final String[] BOX_SCORE_HEADERS = {
      "GAME_ID",
      "MP",
      "VISITOR_FG",
      "VISITOR_FGA",
      "VISITOR_FG%",
      "VISITOR_3P",
      "VISITOR_3PA",
      "VISITOR_3P%",
      "VISITOR_FT",
      "VISITOR_FTA",
      "VISITOR_FT%",
      "VISITOR_ORB",
      "VISITOR_DRB",
      "VISITOR_TRB",
      "VISITOR_AST",
      "VISITOR_STL",
      "VISITOR_BLK",
      "VISITOR_TOV",
      "VISITOR_PF",
      "VISITOR_PTS",
      "VISITOR_PLUS_MIN",
      "MP2",
      "VISITOR_TS%",
      "VISITOR_eFG%",
      "VISITOR_3PAr",
      "VISITOR_FTr",
      "VISITOR_ORB%",
      "VISITOR_DRB%",
      "VISITOR_TRB%",
      "VISITOR_AST%",
      "VISITOR_STL%",
      "VISITOR_BLK%",
      "VISITOR_TOV%",
      "VISITOR_USG%",
      "VISITOR_ORtg",
      "VISITOR_DRtg",
      "MP3",
      "HOME_FG",
      "HOME_FGA",
      "HOME_FG%",
      "HOME_3P",
      "HOME_3PA",
      "HOME_3P%",
      "HOME_FT",
      "HOME_FTA",
      "HOME_FT%",
      "HOME_ORB",
      "HOME_DRB",
      "HOME_TRB",
      "HOME_AST",
      "HOME_STL",
      "HOME_BLK",
      "HOME_TOV",
      "HOME_PF",
      "HOME_PTS",
      "HOME_PLUS_MIN",
      "MP4",
      "HOME_TS%",
      "HOME_eFG%",
      "HOME_3PAr",
      "HOME_FTr",
      "HOME_ORB%",
      "HOME_DRB%",
      "HOME_TRB%",
      "HOME_AST%",
      "HOME_STL%",
      "HOME_BLK%",
      "HOME_TOV%",
      "HOME_USG%",
      "HOME_ORtg",
      "HOME_DRtg"
  };

  public void loadBoxScores() {

    new File(BOX_SCORES_FILE);

    try (

        Reader reader = new FileReader(GAMES_FILE);
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(BOX_SCORES_FILE));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(BOX_SCORE_HEADERS))
    ) {

      long pretime = System.currentTimeMillis();

      Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

      for (CSVRecord record : records) {
        String id = record.get("GAME_ID");

        List<String> csvStrings = Jsoup.connect(getUrl(id))
            .timeout(15000)
            .validateTLSCertificates(false)
            .get()
            .getElementsByTag("tfoot")
            .stream()
            .flatMap(elem -> elem.getElementsByTag("td").stream())
            .map(Element::text)
            .collect(Collectors.toList());

        csvStrings.add(0, id);

        csvPrinter.printRecord(csvStrings);
        System.out.println("BoxScore with id " + id + " scrapped in " + (System.currentTimeMillis() - pretime) / 1000);
      }

      System.out.println("Scrapped boxscores in: " + (System.currentTimeMillis() - pretime) / 1000);

    } catch (Exception e) {
      e.printStackTrace();

    }

  }


  private String getUrl(String id) {
    return BASE_URL + id + ".html";
  }
}
