package hash;

/**
 * Created by christiaan on 4/25/16.
 */
public abstract class HashFunction
{
  private int[] kArray;

  public abstract long hash(String string);

  public void setkArray(int[] kArray)
  {
    this.kArray = kArray;
  }

  public int[] getkArray()
  {
    return kArray;
  }
}
