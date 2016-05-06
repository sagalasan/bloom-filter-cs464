package hash;

/**
 * Created by christiaan on 4/25/16.
 */
public class Sdbm extends AbstractHashFunction
{
  public Sdbm(int k, int m)
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
      hash = c + (hash << 6) + (hash << 16) - hash;
    }
    return (hash % getM());
  }
}
