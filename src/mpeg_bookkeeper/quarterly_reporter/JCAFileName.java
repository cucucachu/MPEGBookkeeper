package mpeg_bookkeeper.quarterly_reporter;

import java.io.File;

/**
 * Provides the ability to sort JCA file names by job number as a number,
 * rather than as a string.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class JCAFileName implements Comparable<JCAFileName> {
   private String filePath;
   private String fileName;
   
   public JCAFileName(String filePath) {
      this.filePath = filePath;
      this.fileName = (new File(filePath)).getName();
   }
   
   public String getFileName() {
      return fileName;
   }
   
   public String getFilePath() {
      return filePath;
   }
   
   public int compareTo(JCAFileName other) {
      String name;
      String otherName;
      int comparison;
      
      name = fileName;
      name.replace('-', '.');
      name = name.substring(0, name.length() - 4);
      
      otherName = other.getFileName();
      otherName.replace('-', '.');
      otherName = otherName.substring(0, otherName.length() - 4);
      
      if (name.contains(".")) {
         for (int digits = name.indexOf('.'); digits < 6; digits++)
            name = "0" + name;
            
         for (int digits = name.length() - 1 - name.indexOf('.');
            digits < 6; digits++)
            name = name + "0";
      }
      
      if (otherName.contains(".")) {
         for (int digits = otherName.indexOf('.'); digits < 6; digits++)
            otherName = "0" + otherName;
            
         for (int digits = otherName.length() - 1 - otherName.indexOf('.');
            digits < 6; digits++)
            otherName = otherName + "0";
                  
      }
      
      comparison = name.compareTo(otherName);
     
      return comparison;
   }
   
   public String toString() {
      return fileName;
   }
}
