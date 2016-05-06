package hash;

/**
 * This class simulates a bit array.
 * We are not allowed to use any libraries so I am simulating a bit array with
 * a byte array for simplicity.
 */
public class BloomFilter
{
  private int arraySize;
  private byte[] array;

  public BloomFilter(int arraySize)
  {
    this.arraySize = arraySize;
    initArray();
  }

  private void initArray()
  {
    array = new byte[arraySize];
    for(int i = 0; i < array.length; i++)
    {
      array[i] = 0;
    }
  }

  public void setBit(int position)
  {
    if(position < 0 || position >= array.length)
    {
      throw new IllegalArgumentException("Position is out of bounds");
    }
    else
    {
      array[position] = 1;
    }
  }
}
