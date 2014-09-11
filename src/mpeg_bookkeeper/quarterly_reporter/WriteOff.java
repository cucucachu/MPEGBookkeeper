package mpeg_bookkeeper.quarterly_reporter;

import jxl.*;
import jxl.write.*;
import java.io.File;
import java.io.IOException;
import jxl.read.biff.BiffException;
import java.util.ArrayList;

/**
 * Represents a write off for a project, including the amount and quarter
 * it was instated.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class WriteOff {
   
   private String quarter;
   private double amount;
   
   public WriteOff(String quarter, double amount) {
      this.quarter = quarter;
      this.amount = amount;
   }
   
   public String getQuarter() {
      return quarter;
   }
   
   public double getAmount() {
      return amount;
   }
   
   public String toString() {
      return String.format("%s: %.2lf", quarter, amount);
   }
}
