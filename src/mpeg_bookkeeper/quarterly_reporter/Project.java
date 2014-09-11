package mpeg_bookkeeper.quarterly_reporter;

import jxl.*;
import jxl.write.*;
import java.io.File;
import java.io.IOException;
import jxl.read.biff.BiffException;
import java.util.ArrayList;

/**
 * Stores the information of a single project to be written to a single line
 * of a QuarterlyReport.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class Project implements Comparable<Project> {
   
   private String projectNumber;
   private String name;
   private String manager;
   private ArrayList<WriteOff> writeOffs;
   private Double unbilledCharges;
   private Double remainingBudget;
   private String status;
   private String soc;
   
   public Project(String projectNumber, String name, String manager, 
      Double unbilledCharges, Double remainingBudget, String soc) {
      this.projectNumber = projectNumber;
      this.name = name;
      this.manager = manager;
      this.writeOffs = new ArrayList<WriteOff>();
      this.unbilledCharges = unbilledCharges;
      this.remainingBudget = remainingBudget;
      this.status = " ";
      this.soc = soc;
   }
   
   public void setWriteOffs(ArrayList<WriteOff> writeOffs) {
      this.writeOffs = writeOffs;
   }
   
   public String getProjectNumber() {
      return projectNumber;
   }
   
   public String getProjectName() {
      return name;
   }
   
   public String getManager() {
      return manager.toUpperCase().trim();
   }
   
   public ArrayList<WriteOff> getWriteOffs() {
      return writeOffs;
   }
   
   public Double getUnbilledCharges() {
      return unbilledCharges;
   }
   
   public Double getRemainingBudget() {
      return remainingBudget;
   }
   
   public String getStatus() {
      return status;
   }
   
   public String getSoc() {
      return soc;
   }
   
   public void setStatus(String status) {
      this.status = status;
   }
   
   public String toString() {
      return projectNumber + " " + name;
   }
   
   public int compareTo(Project other) {
      boolean nanThis = false;
      boolean nanOther = false;
      Double numberThis;
      Double numberOther;
      
      try {
         numberThis = new Double(this.projectNumber);
      }
      catch (NumberFormatException ex) {
         numberThis = Double.NaN;
         nanThis = true;
      }
      
      try {
         numberOther = new Double(other.getProjectNumber());
      }
      catch (NumberFormatException ex) {
         numberOther = Double.NaN; 
         nanOther = true;
      }
      
      if (nanThis == false && nanOther == false) {
         if (numberThis > numberOther)
            return 1;
         else if (numberThis < numberOther)
            return -1;
         else 
            return 0;
      }
      else if (nanThis == true && nanOther == false) {
         return -1;
      }
      else if (nanThis == false && nanOther == true) {
         return 1;
      }
      else {
         return this.projectNumber.compareTo(other.getProjectNumber());
      }
   }
}
