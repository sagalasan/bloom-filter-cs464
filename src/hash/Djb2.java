package hash;

/**
 * Created by christiaan on 4/25/16.
 */
public class Djb2 extends AbstractHashFunction
{
  public Djb2(int k)
  {
    super(k);
  }

  @Override
  public long hash(String string)
  {
    long hash = getK();

    char[] array = string.toCharArray();

    for(int i = 0; i < array.length; i++)
    {
      int c = (int) array[i];
      hash = ((hash << 5) + hash) + c;
    }

    return hash;
  }
}
