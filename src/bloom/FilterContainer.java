package bloom;

import hash.AbstractHashFunction;
import hash.BloomFilter;
import hash.LoseLose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christiaan on 5/5/16.
 */
public class FilterContainer<T extends AbstractHashFunction>
{
  private List<BloomFilter<T>> bloomFilters;
  private int numBloomFilters;
  private int[] kValues;
  private int mValue;
  private AbstractHashFunction.HashBuilder<T> hashBuilder;

  public FilterContainer(AbstractHashFunction.HashBuilder<T> hashBuilder, int numBloomFilters, int[] kValues, int mValue)
  {
    this.numBloomFilters = numBloomFilters;
    this.kValues = kValues;
    this.mValue = mValue;
    this.hashBuilder = hashBuilder;
    bloomFilters = new ArrayList<>();

    generateBloomFilters();
  }

  public void insertString(int position, String string)
  {
    bloomFilters.get(position).insertString(string);
  }

  public List<byte[]> getByteArrays()
  {
    List<byte[]> arrays = new ArrayList<>();
    for(int i = 0; i < bloomFilters.size(); i++)
    {
      arrays.add(bloomFilters.get(i).getByteArray());
    }
    return arrays;
  }

  public List<BloomFilter<T>> getBloomFilters()
  {
    return bloomFilters;
  }

  private void generateBloomFilters()
  {
    for(int i = 0; i < numBloomFilters; i++)
    {
      bloomFilters.add(new BloomFilter.BloomBuilder<>(mValue, generateHashFamily()).build());
    }
  }

  private List<T> generateHashFamily()
  {
    List<T> hashFamily = new ArrayList<>();
    for(int i = 0; i < kValues.length; i++)
    {
      hashFamily.add(hashBuilder
              .addK(kValues[i])
              .addM(mValue)
              .build());
    }
    return hashFamily;
  }

  public static void main(String[] args)
  {
    int[] stringLens = {10, 20};
    int[] kValues = {1000, 2000};

    FilterContainer<LoseLose> loseLoseContainer = new FilterContainer<>(new LoseLose.LoseLoseBuilder(), stringLens.length, kValues, 10);

    System.out.println(loseLoseContainer.bloomFilters.size());

  }
}
