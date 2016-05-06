import hash.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christiaan on 4/25/16.
 */
public class Bloom
{
  private static final int STRING_LENGTH = 10;
  private static final int M = 10000;
  private static final int K = 8;
  private static final int[] NUM_STRINGS = {100, 500, 1000, 5000, 10000, 50000, 100000, 500000};

  private static final int[] K_ARRAY = {5381, 6151, 8353, 1097, 2243, 367, 9341, 4157}; // length 8

  private static final String FILE_NAME = "./strings/A.txt";

  private List<List<String>> stringFamily;
  private StringGenerator stringGenerator;
  private List<String> loadedStrings;

  private List<FilterContainer> filterContainers;

  public Bloom()
  {
    stringGenerator = new StringGenerator();
    stringFamily = new ArrayList<>();
    loadStrings();
  }

  public void run()
  {
    generateStrings();
    generateBloomFilters();
    startInserts();
  }

  private void startInserts()
  {
    for(int i = 0; i < stringFamily.size(); i++)
    {

    }
  }

  private void generateBloomFilters()
  {
    FilterContainer<Djb2> djb2Container = new FilterContainer<>(new Djb2.Djb2Builder(), NUM_STRINGS.length, K_ARRAY, M);
    FilterContainer<Sdbm> sdbmContainer = new FilterContainer<>(new Sdbm.SdbmBuilder(), NUM_STRINGS.length, K_ARRAY, M);
    FilterContainer<LoseLose> loseLoseContainer = new FilterContainer<>(new LoseLose.LoseLoseBuilder(), NUM_STRINGS.length, K_ARRAY, M);

    filterContainers.add(djb2Container);
    filterContainers.add(sdbmContainer);
    filterContainers.add(loseLoseContainer);
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
