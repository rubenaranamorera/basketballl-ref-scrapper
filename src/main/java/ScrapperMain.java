public class ScrapperMain {

  private static final int FROM_SEASON = 2007;
  private static final int TO_SEASON = 2007;

  public static void main(String[] args ) {

    GamesScrapper gamesScrapper = new GamesScrapper();
    //gamesScrapper.loadGames(FROM_SEASON, TO_SEASON);

    BoxScoreScrapper boxScoreScrapper = new BoxScoreScrapper();
    //boxScoreScrapper.loadBoxScores();

    OddsScrapper oddsScrapper = new OddsScrapper();
    //oddsScrapper.loadOdds();
  }

}
