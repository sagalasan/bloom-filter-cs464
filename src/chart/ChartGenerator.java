package chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christiaan on 5/6/16.
 */
public class ChartGenerator
{
  private String name;
  private List<DataHolder> xValues;
  private static final String COLUMN = "False Positives";

  private final DefaultCategoryDataset dataset;

  public ChartGenerator()
  {
    xValues = new ArrayList<>();
    dataset = new DefaultCategoryDataset();
  }

  public void generateAndSave(File file)
  {
    for(DataHolder xValue : xValues)
    {
      dataset.addValue(xValue.getValue(), COLUMN, xValue.getxName());
    }

    JFreeChart barChart = ChartFactory.createBarChart(name,
            "# Inserted Strings", "# False Positives", dataset,
            PlotOrientation.VERTICAL, true, true, false);
    int width = 1024;
    int height = 768;
    try
    {
      ChartUtilities.saveChartAsPNG(file, barChart, width, height);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void addXName(String xName, int value)
  {
    xValues.add(new DataHolder(xName, value));
  }

  private static class DataHolder
  {
    private String xName;
    private int value;

    private DataHolder(String xName, int value)
    {
      this.xName = xName;
      this.value = value;
    }

    public String getxName()
    {
      return xName;
    }

    public int getValue()
    {
      return value;
    }
  }
}
