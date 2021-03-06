package bloom;

import chart.ChartManager;
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
    File statDir = new File(Constants.STAT_DIR);
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

    System.out.println("Generating bloom.Bloom Filters...");
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
    int[] djb2FalseArray = new int[Constants.K_ARRAY.length];
    int[] sdbmFalseArray = new int[Constants.K_ARRAY.length];
    int[] loseloseFalseArray = new int[Constants.K_ARRAY.length];

    System.out.println("\tChecking Djb2 bloom.Bloom filters...");
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
      System.out.println("\tNumStrings: " + insertedStrings.size() + " False Positives: " + falsePositives);
      loseloseFalseArray[i] = falsePositives;
    }

    writeFalseFile(Constants.DJB2_TXT, djb2FalseArray);
    writeFalseFile(Constants.SDBM_TXT, sdbmFalseArray);
    writeFalseFile(Constants.LOSELOSE_TXT, loseloseFalseArray);
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
    djb2Container = new FilterContainer<>(new Djb2.Djb2Builder(), Constants.NUM_STRINGS.length,
            Constants.K_ARRAY, Constants.M);
    sdbmContainer = new FilterContainer<>(new Sdbm.SdbmBuilder(), Constants.NUM_STRINGS.length,
            Constants.K_ARRAY, Constants.M);
    loseLoseContainer = new FilterContainer<>(new LoseLose.LoseLoseBuilder(), Constants.NUM_STRINGS.length,
            Constants.K_ARRAY, Constants.M);
  }

  public List<List<String>> getStringFamily()
  {
    return stringFamily;
  }

  private void loadStrings()
  {
    loadedStrings = new ArrayList<>();
    try(BufferedReader bufferedReader = new BufferedReader(new FileReader(Constants.FILE_NAME)))
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
    for(int i = 0; i < Constants.NUM_STRINGS.length; i++)
    {
      int numStrings = Constants.NUM_STRINGS[i];
      List<String> strings = stringGenerator.generateStrings(Constants.STRING_LENGTH, numStrings);
      stringFamily.add(strings);
    }
  }


  public static void main(String[] args)
  {
    Bloom bloom = new Bloom();
    bloom.run();
    ChartManager chartManager = new ChartManager();
    chartManager.run();
  }
}
