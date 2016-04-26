package hash;

/**
 * Created by christiaan on 4/25/16.
 */
public class LoseLose extends HashFunction
{
  @Override
  public long hash(String string)
  {
    return 0;
  }

  @Override
  public int[] getkArray()
  {
    return new int[0];
  }
}
