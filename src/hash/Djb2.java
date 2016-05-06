package hash;

/**
 * Created by christiaan on 4/25/16.
 */
public class Djb2 extends AbstractHashFunction
{
  private Djb2(Djb2Builder builder)
  {
    super(builder.k, builder.m);
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

    return Integer.remainderUnsigned((int) hash, getM());
  }

  public static class Djb2Builder extends HashBuilder<Djb2>
  {
    private int k;
    private int m;

    @Override
    public HashBuilder<Djb2> addK(int k)
    {
      this.k = k;
      return this;
    }

    @Override
    public HashBuilder<Djb2> addM(int m)
    {
      this.m = m;
      return this;
    }

    @Override
    public Djb2 build()
    {
      return new Djb2(this);
    }
  }
}
