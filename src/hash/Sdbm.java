package hash;

/**
 * Created by christiaan on 4/25/16.
 */
public class Sdbm extends AbstractHashFunction
{
  private Sdbm(SdbmBuilder sdbmBuilder)
  {
    super(sdbmBuilder.k, sdbmBuilder.m);
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
    return Integer.remainderUnsigned((int) hash, getM());
  }

  public static class SdbmBuilder extends HashBuilder<Sdbm>
  {
    private int k;
    private int m;

    @Override
    public HashBuilder<Sdbm> addK(int k)
    {
      this.k = k;
      return this;
    }

    @Override
    public HashBuilder<Sdbm> addM(int m)
    {
      this.m = m;
      return this;
    }

    @Override
    public Sdbm build()
    {
      return new Sdbm(this);
    }
  }
}
