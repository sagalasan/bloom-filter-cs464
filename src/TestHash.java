import hash.AbstractHashFunction;
import hash.Djb2;

/**
 * Created by christiaan on 5/5/16.
 */
public class TestHash
{
  public static void main(String[] args)
  {
    AbstractHashFunction djb2 = new Djb2(90, 10000);
    String string = "Tjcfwsy";

    long hashResult = djb2.hash(string);
    System.out.println(hashResult);
  }
}
