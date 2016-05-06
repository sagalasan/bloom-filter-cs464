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
  public static final int STRING_LENGTH = 10;
  public static final int[] NUM_STRINGS = {100, 500, 1000, 5000, 10000, 50000, 100000, 500000};

  private static final String FILE_NAME = "./strings/A.txt";

  private List<List<String>> stringFamily;
  private StringGenerator stringGenerator;
  private List<String> loadedStrings;


  public Bloom()
  {
    stringGenerator = new StringGenerator();
    stringFamily = new ArrayList<>();
    loadStrings();
  }

  public void run()
  {
    generateStrings();
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
