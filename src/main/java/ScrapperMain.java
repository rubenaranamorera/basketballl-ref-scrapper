import java.io.IOException;

public class ScrapperMain {

  private static final int FROM_SEASON = 2012;
  private static final int TO_SEASON = 2012;

  public static final String GAMES_FILE = "games2012.csv";
  public static final String ODDS_FILE = "odds2012.csv";
  public static final String BOX_SCORES_FILE = "boxscores2012.csv";


  public static void main(String[] args ) throws IOException {

    GamesScrapper gamesScrapper = new GamesScrapper();
    gamesScrapper.loadGames(FROM_SEASON, TO_SEASON);

    BoxScoreScrapper boxScoreScrapper = new BoxScoreScrapper();
    boxScoreScrapper.loadBoxScores();

    OddsScrapper oddsScrapper = new OddsScrapper();
    oddsScrapper.loadOdds();
  }

}
