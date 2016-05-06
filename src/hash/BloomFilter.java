package hash;

import java.util.List;

/**
 * This class simulates a bloom filter.
 * We are not allowed to use any libraries so I am simulating a bit array with
 * a byte array for simplicity.
 */
public class BloomFilter<T extends AbstractHashFunction>
{
  private int arraySize;
  private byte[] array;

  private int stringLength;

  private T[] hashFamily;

  public BloomFilter(int arraySize, int stringLength, T[] hashFamily)
  {
    this.arraySize = arraySize;
    this.stringLength = stringLength;
    this.hashFamily = hashFamily;
    init();
  }

  public void insertString(String string)
  {
    for(T hashFunction : hashFamily)
    {
      setBit((int) hashFunction.hash(string));
    }
  }

  private void init()
  {
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

  private void setBit(int position)
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
