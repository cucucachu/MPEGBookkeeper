package mpeg_bookkeeper.jca_builder;

import jxl.*;
import jxl.write.*;
import java.io.File;
import java.io.IOException;
import jxl.read.biff.BiffException;
import java.util.ArrayList;
import java.util.Iterator;

import mpeg_bookkeeper.GuiTab;

/**
 * Copies and sorts job information into a new column of a JCA
 *
 * @author Cody Jones
 * @version 1.0
 */
public class JCA {
   public static final int JobRowNotFound = -1;
   public static final int CurrentWeekColumn = 4;
   private static final String IChargesLabel = "INSIDE CHGS";
   private static final String RChargesLabel = "REIMB CHGS";
   private static final String VHoursLabel = "VEHICLE (HRS)";
   private static final String MilesLabel = "MILES";
   private static final String FDTLabel = "FDT TEST";
   private static final String WeekLabel = "WEEK ENDING DATE:";
   private static final String WPAssistantLabel = "WP/Assistant";
   private static final String ProjectManagerLabel = "PROJ MGR :";
   private static final String FormatCheckString = "JCA Upload Format";
   private static final int ColumnLimit = 60;

	private GuiTab gui;

   private String jobNoStr;
   private String week;
   private ArrayList<Job> jobs;
   private ArrayList<Job> failedJobs;
   private String jcaFolder;
   private String jcaOutFile;
   private File jcaFile;
   private WritableWorkbook jcaWorkbook;
   private WritableSheet sheet;
   private String projectManager;
   
   private double weeklyMiles;
   private double weeklyVehicleHours;
   private double weeklyFdtTotal;
   
   public JCA (String jobNoStr, String jcaFolder, String week, GuiTab gui) {
      this.jobNoStr = jobNoStr;
      this.jcaFolder = jcaFolder;
      this.week = week;
      this.projectManager = null;
      this.gui = gui;
      jobs = new ArrayList<Job>();
      failedJobs = new ArrayList<Job>();
      jcaOutFile = jcaFolder + "/JCAs_" + week.replace("/", "_") + "/" + jobNoStr + ".xls";
      findFile();
   }
   
   public void loadWorkbook() throws JCANotFoundException, JCAException, Exception {
      //String jcaOutFile = jcaFolder + "/JCAs_" + week.replace("/", "_") + "/" + jobNoStr + ".xls";
      if (jcaFile == null) {
         throw new JCANotFoundException("Could not find JCA for job", jobNoStr);
      }
      else {
         try {
            Workbook originalWorkbook = Workbook.getWorkbook(jcaFile);
            jcaWorkbook = Workbook.createWorkbook(new File(jcaOutFile), originalWorkbook);
            sheet = jcaWorkbook.getSheet(0);
            if (sheet.findCell(FormatCheckString) == null) {
               throw new JCAException("This JCA is not in the updated format. Could not find " 
                  + "\"JCA Upload Format\" Label.");
            }
            if (sheet.getColumns() >= ColumnLimit) {
               output("      Warning!!!   " + this + " is approaching the column limit. Please "
                  + "start a new sheet before next week.");
            }
            setProjectManager();
         }
         catch (JCAException ex) {
            throw ex;
         }
         catch (Exception ex) {
            output("Failed to load Workbook! " + ex);
            throw ex;
            //System.out.println("Failed to load Workbook!!!!!!!!!!!! " + ex);
         }
      }
   }
   
   private void findFile() {
      File file = new File(jcaFolder+"/"+jobNoStr+".xls");
      //System.out.println("Searching for " + jcaFolder+"/"+jobNoStr+".xls");
      if (file.exists()) 
         jcaFile = file;
      else jcaFile = null;
   }
   
   public void addJob(Job newJob) {
      jobs.add(newJob);
      
      if (jcaFile == null) {
         newJob.setRejection(new Rejection(newJob.toString(), 
            "Cannot find JCA file."));
         failedJobs.add(newJob);
      }
   }
   
   private int findJobRow(Job job) {
      String toFind;
      Cell searchCell;
      
      if (job.classCode != null && job.classCode.trim().toUpperCase().compareTo("WP") == 0) {
         searchCell = sheet.findCell(WPAssistantLabel);
         if (searchCell == null)
            return -1;
         else
            return searchCell.getRow();
      }
      
      if (job.prevWage != null) 
         toFind = job.initials.trim() + " " + job.prevWage;
      else
         toFind = job.initials.trim() + " " + job.classCode.trim();
      
      searchCell = sheet.findCell(toFind);
      
      if (searchCell == null)
         return JobRowNotFound;
      else
         return searchCell.getRow();
   }
   
   private void calculateInsideCharges() {
      weeklyMiles = weeklyVehicleHours = weeklyFdtTotal = 0;
      
      for (Job job: jobs) {
         if (job.isInsideCharge()) {
            if (job.miles != null) {
               try {
                  weeklyMiles += Double.parseDouble(job.miles);
                  job.miles = null;
               }
               catch (NumberFormatException ex) {
                  System.out.println("Vehicle miles in job " + job + "is not "
                     + "a number.");
                  output("Vehicle miles in job " + job + "is not "
                     + "a number.");
                  job.setRejection(new Rejection(job.toString(), "Vehicle miles in job " 
                     + job + "is not " + "a number."));
                  failedJobs.add(job);
               }
            }
            
            weeklyVehicleHours += job.hours;
            job.hours = 0;
            
            if (job.fdt != null) {
               try {
                  weeklyFdtTotal += Double.parseDouble(job.fdt);
                  job.fdt = null;
               }
               catch (NumberFormatException ex) {
                  System.out.println("FDT in job " + job + "is not "
                     + "a number.");
                  output("FDT in job " + job + "is not "
                     + "a number.");
                  job.setRejection(new Rejection(job.toString(), 
                     "FDT in job " + job + "is not " + "a number."));
                  failedJobs.add(job);
               }
            }
         }
      }
   }
   
   public void prepareJCA() throws JCAException, WriteException {
      JCAColumn.prepareJCA(sheet);
   }
   
   public void fillJCA() throws JCAException {
      int jobRow;
      jxl.write.Number hours;
      jxl.write.Number miles;
      jxl.write.Number vHours;
      jxl.write.Number fdt;
      jxl.write.Label date;
      
      Cell searchCell;
      int searchRow;
      int searchCol;
      int vHoursRow;
      int milesRow;
      int fdtRow;
      int weekRow;
      


      jxl.format.CellFormat format;
      WritableCellFeatures features;
      
      for (Job job : jobs) {
         if (job.isNormal()) {
            jobRow = findJobRow(job);
            
            if (jobRow == -1) {
               job.setRejection(new Rejection(job.toString(), 
                  "Could not find the right row in JCA."));
               failedJobs.add(job);
            }
            else {
               try {
                  output("   Putting " + job + " at row " + jobRow + " in " + this);
                  format = sheet.getCell(CurrentWeekColumn, jobRow).getCellFormat();
                  hours = new jxl.write.Number(CurrentWeekColumn, jobRow, job.hours, format);

                  if (job.comment != null) {
                     System.out.println("Writing Comment \"" + job.comment + "\"");
                     features = new WritableCellFeatures();
                     features.setComment(job.comment);
                     hours.setCellFeatures(features);
                  }
                  sheet.addCell(hours);
               }
               catch (Exception ex) {
                  System.out.println(job + " failed to write to JCA");
                  output("      Failed to write " + job + " to JCA " + this);
                  job.setRejection(new Rejection(job.toString(), ex.getMessage()));
                  failedJobs.add(job);
               }
            }
         }
      }
      
      calculateInsideCharges();
      
      for (Job job : jobs) {
         if (job.isReimbursableCharge()) {
            job.setRejection(new Rejection(job.toString(), 
               "This job is a reimbursable charge."));
            failedJobs.add(job);
         }
      }
      
      searchCell = sheet.findCell(IChargesLabel);
      if (searchCell == null)
         throw new JCAException("Could not find \"INSIDE CHGS\" label.");      
      searchRow = searchCell.getRow();
      searchCol = searchCell.getColumn();
      
      searchCell = sheet.findCell(VHoursLabel, 0, searchRow, 10, searchRow + 10, false);
      if (searchCell == null)
         throw new JCAException("Could not find \"VEHICLE (HRS)\" label.");
      vHoursRow = searchCell.getRow();
      
      searchCell = sheet.findCell(MilesLabel, 0, searchRow, 10, searchRow + 10, false);
      if (searchCell == null)
         throw new JCAException("Could not find \"MILES\" label.");
      milesRow = searchCell.getRow();
      
      searchCell = sheet.findCell(FDTLabel, 0, searchRow, 10, searchRow + 10, false);
      if (searchCell == null)
         throw new JCAException("Could not find \"FDT TEST\" label.");
      fdtRow = searchCell.getRow();
      
      searchCell = sheet.findCell(WeekLabel);
      if (searchCell == null)
         throw new JCAException("Could not find \"WEEK ENDING DATE:\" label.");      
      weekRow = searchCell.getRow();
      
      try {
         if (weeklyVehicleHours != 0) {
            format = sheet.getCell(CurrentWeekColumn, vHoursRow).getCellFormat();
            vHours = new jxl.write.Number(CurrentWeekColumn, vHoursRow, weeklyVehicleHours, format);
            sheet.addCell(vHours);
         }
         
         if (weeklyMiles != 0) {
            format = sheet.getCell(CurrentWeekColumn, milesRow).getCellFormat();
            miles = new jxl.write.Number(CurrentWeekColumn, milesRow, weeklyMiles, format);
            sheet.addCell(miles);
         }
         
         if (weeklyFdtTotal != 0) {
            format = sheet.getCell(CurrentWeekColumn, fdtRow).getCellFormat();
            fdt = new jxl.write.Number(CurrentWeekColumn, fdtRow, weeklyFdtTotal, format);
            sheet.addCell(fdt);
         }
         
         format = sheet.getCell(CurrentWeekColumn, weekRow).getCellFormat();
         date = new Label(CurrentWeekColumn, weekRow, this.week, format);
         sheet.addCell(date);
      }
      catch (Exception ex) {
         throw new JCAException("Failed to write inside charges to JCA.");
      }
      
      try {
         JCAColumn.updateColumnFormulas(sheet);
      }
      catch (Exception ex) {
         throw new JCAException("Failed to write inside charges to JCA.");
      }
   }
   
   public void writeJCA() {      
      try {
         jcaWorkbook.write();
         jcaWorkbook.close();
      }
      catch (Exception ex) {
         System.out.println("Failed to write JCA, caught " + ex);
      }
   }
   
   public void setProjectManager() {
      Cell searchCell;
      Cell projectManagerCell;
      
      searchCell = sheet.findCell(ProjectManagerLabel);
      
      if (searchCell != null) {
            
         projectManagerCell = sheet.getCell(searchCell.getColumn() + 1,
            searchCell.getRow());
            
         projectManager = projectManagerCell.getContents();
      }
   }
   
   public String getProjectManager() {
      return projectManager;
   }
   
   public Sheet getSheet() throws IOException, BiffException {
      Sheet toReturn;
      Workbook wb = Workbook.getWorkbook(new File(jcaOutFile));
      
      toReturn = wb.getSheet(0);
      wb.close();
      return toReturn;
   }
   
   public String getJobNoStr(){
      return jobNoStr;
   }
   
   public ArrayList<Job> getJobs() {
      return jobs;
   }
   
   public ArrayList<Job> getFailedJobs() {
      return failedJobs;
   }
   
   public String toString() {
      return "JCA: " + jobNoStr;
   }

   public void close() {
      try {
         jcaWorkbook.close();
      }
      catch (Exception ex) {
         System.out.println("Could not close " + this);
      }
   }
   
   public String getJCAFile() {
      return jcaOutFile;
   }
   
   public void delete() {
      File toDelete = new File(jcaOutFile);
      
      if (jcaWorkbook != null) {
      	close();
      }
      toDelete.delete();
   }
   
   private void output(String msg) {
      gui.output(msg);
   }

}
