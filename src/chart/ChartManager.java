package chart;

import bloom.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christiaan on 5/6/16.
 */
public class ChartManager
{
  List<File> files;
  public ChartManager()
  {
    files = new ArrayList<>();
  }

  public void run()
  {
    System.out.println("Starting chart generation...");
    System.out.println("Reading statistics files...");

    try
    {
      File chartDir = new File(Constants.CHART_DIR);
      FileUtils.deleteDirectory(chartDir);
      chartDir.mkdir();

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    readFiles();
    generateCharts();
  }

  private void generateCharts()
  {
    for(File file : files)
    {
      ChartGenerator chartGenerator = new ChartGenerator();
      List<String> values = new ArrayList<>();
      try
      {
        values = FileUtils.readLines(file, Charset.defaultCharset());
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      String name = Constants.CHART_DIR + FilenameUtils.removeExtension(file.getName()) + ".png";
      for(int i = 0; i < Constants.NUM_STRINGS.length; i++)
      {
        chartGenerator.addXName(Integer.toString(Constants.NUM_STRINGS[i]), Integer.parseInt(values.get(i)));
      }
      chartGenerator.generateAndSave(new File(name));
    }
  }

  private void readFiles()
  {
    try
    {
      Files.walk(Paths.get(Constants.STAT_DIR)).forEach(filePath -> {
        if (Files.isRegularFile(filePath))
        {
          System.out.println("\t" + filePath);
          files.add(filePath.toFile());
        }
      });
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
