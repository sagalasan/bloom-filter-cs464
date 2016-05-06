package hash;

/**
 * Created by christiaan on 4/25/16.
 */
public class LoseLose extends AbstractHashFunction
{
  public LoseLose(int k)
  {
    super(k);
  }

  @Override
  public long hash(String string)
  {
    return 0;
  }

}
