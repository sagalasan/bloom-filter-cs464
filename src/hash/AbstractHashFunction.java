package hash;

/**
 * Created by christiaan on 4/25/16.
 */
public abstract class AbstractHashFunction
{
  private int k;
  private int m;

  public AbstractHashFunction(int k, int m)
  {
    this.k = k;
    this.m = m;
  }

  public abstract long hash(String string);

  public int getK()
  {
    return k;
  }

  public int getM()
  {
    return m;
  }

  public static abstract class HashBuilder<T extends AbstractHashFunction> implements Builder<T>
  {
    public abstract HashBuilder<T> addK(int k);
    public abstract HashBuilder<T> addM(int m);
  }
}
