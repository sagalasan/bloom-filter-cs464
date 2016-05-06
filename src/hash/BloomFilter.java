package hash;

import java.util.ArrayList;
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

  private List<T> hashFamily;

  private BloomFilter(BloomBuilder<T> bloomBuilder)
  {
    this.arraySize = bloomBuilder.arraySize;
    this.hashFamily = bloomBuilder.hashFamily;
    init();
  }

  public void insertString(String string)
  {
    for(int i = 0; i < hashFamily.size(); i++)
    {
      setBit((int) hashFamily.get(i).hash(string));
    }
  }

  public int[] getLocations(String string)
  {
    int[] locations = new int[hashFamily.size()];
    for(int i = 0; i < hashFamily.size(); i++)
    {
      locations[i] = (int) hashFamily.get(i).hash(string);
    }
    return locations;
  }

  public boolean isPositive(int[] locations)
  {
    for(int i = 0; i < locations.length; i++)
    {
      int loc = locations[i];
      if(loc < 0 || loc >= array.length)
      {
        throw new IllegalArgumentException("Position is out of bounds");
      }
      if(array[loc] != 1)
      {
        return false;
      }
    }
    return true;
  }

  public byte[] getByteArray()
  {
    return array;
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
    //System.out.println(position);
    if(position < 0 || position >= array.length)
    {
      throw new IllegalArgumentException("Position is out of bounds");
    }
    else
    {
      array[position] = 1;
    }
  }

  public static class BloomBuilder<T extends AbstractHashFunction> implements Builder<BloomFilter<T>>
  {
    private int arraySize;
    private List<T> hashFamily;

    public BloomBuilder(int arraySize, List<T> hashFamily)
    {
      this.arraySize = arraySize;
      this.hashFamily = hashFamily;
    }

    @Override
    public BloomFilter<T> build()
    {
      return new BloomFilter<>(this);
    }
  }

  public static void main(String[] args)
  {
  }
}
