import hash.AbstractHashFunction;
import hash.BloomFilter;

import java.util.HashSet;
import java.util.List;

/**
 * Created by christiaan on 5/6/16.
 */
public class BloomChecker<T extends AbstractHashFunction>
{
  private List<String> loadedStrings;
  private HashSet<String> insertedStrings;
  private BloomFilter<T> bloomFilter;

  public BloomChecker(List<String> loadedStrings, HashSet<String> insertedStrings, BloomFilter<T> bloomFilter)
  {
    this.loadedStrings = loadedStrings;
    this.insertedStrings = insertedStrings;
    this.bloomFilter = bloomFilter;
  }

  public int getNumberFalsePositives()
  {
    int falsePositives = 0;
    for(String string : loadedStrings)
    {
      int[] locations = bloomFilter.getLocations(string);
      boolean isPositive = bloomFilter.isPositive(locations);
      if(isPositive)
      {
        if(!isInInsertedString(string))
        {
          falsePositives++;
        }
      }
    }
    return falsePositives;
  }

  private boolean isInInsertedString(String string)
  {
    return insertedStrings.contains(string);
  }
}
