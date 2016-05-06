import hash.*;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by christiaan on 4/25/16.
 */
public class Bloom
{
  private static final int STRING_LENGTH = 10;
  private static final int M = 10000;
  private static final int[] NUM_STRINGS = {100, 500, 1000, 5000, 10000, 50000, 100000, 500000};

  private static final int[] K_ARRAY = {5381, 6151, 8353, 1097, 2243, 367, 9341, 4157}; // length 8

  private static final String FILE_NAME = "./strings/A.txt";
  private static final String STAT_DIR = "./statistics/";
  private static final String DJB2_TXT = STAT_DIR + "djb2.txt";
  private static final String SDBM_TXT = STAT_DIR + "sdbm.txt";
  private static final String LOSELOSE_TXT = STAT_DIR + "loselose.txt";

  private List<List<String>> stringFamily;
  private StringGenerator stringGenerator;
  private List<String> loadedStrings;

  private FilterContainer<Djb2> djb2Container;
  private FilterContainer<Sdbm> sdbmContainer;
  private FilterContainer<LoseLose> loseLoseContainer;

  public Bloom()
  {
    stringGenerator = new StringGenerator();
    stringFamily = new ArrayList<>();
    loadStrings();
  }

  public void run()
  {
    File statDir = new File(STAT_DIR);
    try
    {
      FileUtils.deleteDirectory(statDir);
      FileUtils.forceMkdir(statDir);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    System.out.println("Num loaded strings: " + loadedStrings.size());
    System.out.println("Generating strings...");
    generateStrings();

    System.out.println("Generating Bloom Filters...");
    generateBloomFilters();

    System.out.println("Starting inserts...");
    startInserts();

    System.out.println("Checking bloom filters...");
    checkBloomFilters();
  }

  private void startInserts()
  {
    for(int i = 0; i < stringFamily.size(); i++)
    {
      for(int j = 0; j < stringFamily.get(i).size(); j++)
      {
        String string = stringFamily.get(i).get(j);
        djb2Container.insertString(i, string);
        sdbmContainer.insertString(i, string);
        loseLoseContainer.insertString(i, string);
      }
    }
  }

  private void checkBloomFilters()
  {
    int[] djb2FalseArray = new int[K_ARRAY.length];
    int[] sdbmFalseArray = new int[K_ARRAY.length];
    int[] loseloseFalseArray = new int[K_ARRAY.length];

    System.out.println("\tChecking Djb2 Bloom filters...");
    List<BloomFilter<Djb2>> djb2Filters = djb2Container.getBloomFilters();
    for(int i = 0; i < djb2Filters.size(); i++)
    {
      BloomFilter<Djb2> bloomFilter = djb2Filters.get(i);
      List<String> insertedStrings = stringFamily.get(i);
      HashSet<String> loadSet = new HashSet<>();
      loadSet.addAll(insertedStrings);
      BloomChecker<Djb2> checker = new BloomChecker<>(loadedStrings, loadSet, bloomFilter);
      int falsePositives = checker.getNumberFalsePositives();
      System.out.println("\tNumStrings: " + insertedStrings.size() + " False Positives: " + falsePositives);
      djb2FalseArray[i] = falsePositives;
    }

    System.out.println("\n\tChecking Sdbm bloom filters...");
    List<BloomFilter<Sdbm>> sdbmFilters = sdbmContainer.getBloomFilters();
    for(int i = 0; i < sdbmFilters.size(); i++)
    {
      BloomFilter<Sdbm> bloomFilter = sdbmFilters.get(i);
      List<String> insertedStrings = stringFamily.get(i);
      HashSet<String> loadSet = new HashSet<>();
      loadSet.addAll(insertedStrings);
      BloomChecker<Sdbm> checker = new BloomChecker<>(loadedStrings, loadSet, bloomFilter);
      int falsePositives = checker.getNumberFalsePositives();
      System.out.println("\tNumStrings: " + insertedStrings.size() + " False Positives: " + falsePositives);
      sdbmFalseArray[i] = falsePositives;
    }

    System.out.println("\n\tChecking LoseLose bloom filters...");
    List<BloomFilter<LoseLose>> loseLoseFilters = loseLoseContainer.getBloomFilters();
    for(int i = 0; i < loseLoseFilters.size(); i++)
    {
      BloomFilter<LoseLose> bloomFilter = loseLoseFilters.get(i);
      List<String> insertedStrings = stringFamily.get(i);
      HashSet<String> loadSet = new HashSet<>();
      loadSet.addAll(insertedStrings);
      BloomChecker<LoseLose> checker = new BloomChecker<>(loadedStrings, loadSet, bloomFilter);
      int falsePositives = checker.getNumberFalsePositives();
      System.out.println("\n\tNumStrings: " + insertedStrings.size() + " False Positives: " + falsePositives);
      loseloseFalseArray[i] = falsePositives;
    }

    writeFalseFile(DJB2_TXT, djb2FalseArray);
    writeFalseFile(SDBM_TXT, sdbmFalseArray);
    writeFalseFile(LOSELOSE_TXT, loseloseFalseArray);
  }

  private void writeFalseFile(String fileName, int[] array)
  {
    File file = new File(fileName);
    String string = "";
    for(int num : array)
    {
      string += num + "\n";
    }
    try
    {
      FileUtils.writeStringToFile(file, string);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void generateBloomFilters()
  {
    djb2Container = new FilterContainer<>(new Djb2.Djb2Builder(), NUM_STRINGS.length, K_ARRAY, M);
    sdbmContainer = new FilterContainer<>(new Sdbm.SdbmBuilder(), NUM_STRINGS.length, K_ARRAY, M);
    loseLoseContainer = new FilterContainer<>(new LoseLose.LoseLoseBuilder(), NUM_STRINGS.length, K_ARRAY, M);
  }

  public List<List<String>> getStringFamily()
  {
    return stringFamily;
  }

  private void loadStrings()
  {
    loadedStrings = new ArrayList<>();
    try(BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME)))
    {
      String line;
      while((line = bufferedReader.readLine()) != null)
      {
        loadedStrings.add(line);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void generateStrings()
  {
    for(int i = 0; i < NUM_STRINGS.length; i++)
    {
      int numStrings = NUM_STRINGS[i];
      List<String> strings = stringGenerator.generateStrings(STRING_LENGTH, numStrings);
      stringFamily.add(strings);
    }
  }


  public static void main(String[] args)
  {
    Bloom bloom = new Bloom();
    bloom.run();
  }
}
