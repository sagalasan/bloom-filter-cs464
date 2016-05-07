package bloom;

/**
 * Created by christiaan on 5/6/16.
 */
public class Constants
{
  public static final int STRING_LENGTH = 10;
  public static final int M = 10000;
  public static final int[] NUM_STRINGS = {100, 500, 1000, 5000, 10000, 50000, 100000, 500000};

  public static final int[] K_ARRAY = {5381, 6151, 8353, 1097, 2243, 367, 9341, 4157}; // length 8

  public static final String FILE_NAME = "./strings/A.txt";
  public static final String STAT_DIR = "./statistics/";
  public static final String CHART_DIR = "./charts/";
  public static final String DJB2_TXT = STAT_DIR + "djb2.txt";
  public static final String SDBM_TXT = STAT_DIR + "sdbm.txt";
  public static final String LOSELOSE_TXT = STAT_DIR + "loselose.txt";
}
