package hash;

/**
 * Created by christiaan on 4/25/16.
 */
public class LoseLose extends AbstractHashFunction
{
  private LoseLose(LoseLoseBuilder loseLoseBuilder)
  {
    super(loseLoseBuilder.k, loseLoseBuilder.m);
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

  public static class LoseLoseBuilder extends HashBuilder<LoseLose>
  {
    private int k;
    private int m;

    @Override
    public HashBuilder<LoseLose> addK(int k)
    {
      this.k = k;
      return this;
    }

    @Override
    public HashBuilder<LoseLose> addM(int m)
    {
      this.m = m;
      return this;
    }

    @Override
    public LoseLose build()
    {
      return null;
    }
  }
}
