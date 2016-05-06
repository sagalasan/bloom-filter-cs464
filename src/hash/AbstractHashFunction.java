package hash;

/**
 * Created by christiaan on 4/25/16.
 */
public abstract class AbstractHashFunction
{
  private int k;

  public AbstractHashFunction(int k)
  {
    this.k = k;
  }

  public abstract long hash(String string);

  public int getK()
  {
    return k;
  }
}
