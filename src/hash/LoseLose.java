package hash;

/**
 * Created by christiaan on 4/25/16.
 */
public class LoseLose extends AbstractHashFunction
{
  public LoseLose(int k, int m)
  {
    super(k, m);
  }

  @Override
  public long hash(String string)
  {
    long hash = 0;
    char[] array = string.toCharArray();

    for(int i = 0; i < array.length; i++)
    {
      int c = (int) array[i];
      hash += c;
    }
    return (hash % getM());
  }
}
