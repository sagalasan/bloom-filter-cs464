package bloom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by christiaan on 4/25/16.
 */
public class StringGenerator
{
  private Random random;
  private int offset = 'a';
  private int bound = 26;

  public StringGenerator()
  {
    random = new Random();
  }

  public List<String> generateStrings(int length, int numStrings)
  {
    List<String> strings = new ArrayList<>();

    for(int i = 0; i < numStrings; i++)
    {
      strings.add(randomString(length));
    }

    return strings;
  }

  public String randomString(int length)
  {
    char[] chars = new char[length];

    for(int i = 0; i < length; i++)
    {
      char c = (char) (random.nextInt(bound) + offset);
      chars[i] = c;
    }

    return new String(chars);
  }
}
