import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christiaan on 4/25/16.
 */
public class BloomFilter
{
  public static final int STRING_LENGTH = 10;
  public static final int[] NUM_STRINGS = {100, 500, 1000, 5000, 10000, 50000, 100000, 500000};

  private static final String fileName = "./strings/A.txt";

  private List<List<String>> stringFamily;
  private StringGenerator stringGenerator;
  private List<String> loadedStrings;


  public BloomFilter()
  {
    stringGenerator = new StringGenerator();
    stringFamily = new ArrayList<>();
    loadStrings();
  }

  public void run()
  {
    for(int i = 0; i < NUM_STRINGS.length; i++)
    {
      int numStrings = NUM_STRINGS[i];
      List<String> strings = stringGenerator.generateStrings(STRING_LENGTH, numStrings);
      stringFamily.add(strings);
    }
  }

  public List<List<String>> getStringFamily()
  {
    return stringFamily;
  }

  public void loadStrings()
  {
    loadedStrings = new ArrayList<>();
    try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName)))
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


  public static void main(String[] args)
  {
    BloomFilter bloomFilter = new BloomFilter();
    bloomFilter.run();

    List<List<String>> stringFamily = bloomFilter.getStringFamily();

    for(int i = 0; i < stringFamily.size(); i++)
    {
      List<String> strings = stringFamily.get(i);
      System.out.println(strings.size());
    }
  }
}
