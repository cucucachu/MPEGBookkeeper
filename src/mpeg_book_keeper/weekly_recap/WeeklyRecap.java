package mpeg_book_keeper.weekly_recap;

import java.io.IOException;
import jxl.read.biff.BiffException;
import java.io.File;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Collections;

public class WeeklyRecap {

   private static final String newline = System.lineSeparator();
   private static final String RecapFileName = "WeeklyRecap";
   private static WeeklyRecapGui gui;
   
   public static void recapWeek(WeeklyRecapGui inGui, String timeSheetFolderStr,
   	String recapsFolderStr) {
      TimeSheet curTimeSheet;
      String timeSheetFolderPath;
      File timeSheetFolder;
      File[] allTimeSheets;
      ArrayList<String> timeSheetFileNames;
      ArrayList<String> failedSheets;
      ArrayList<Job> allJobs;
      ArrayList<String> comments;
      Recap recap;
      Calendar calendar;
      String date;
      File recapFile;
      
      try {
      	gui = inGui;
         timeSheetFolderPath = timeSheetFolderStr + "/";
         timeSheetFolder = new File(timeSheetFolderPath);
         allTimeSheets = timeSheetFolder.listFiles();
         timeSheetFileNames = new ArrayList<String>();
         comments = new ArrayList<String>();
         failedSheets = new ArrayList<String>();
         allJobs = new ArrayList<Job>();
         
         for (File file : allTimeSheets) {
             if (file.isFile()) {
                 timeSheetFileNames.add(file.getName());
             }
         }
         
         for (String fileName : timeSheetFileNames) {
            try {
               curTimeSheet = new TimeSheet(timeSheetFolderPath + fileName);
               allJobs.addAll(curTimeSheet.getJobs());
               comments.addAll(curTimeSheet.getComments());
               curTimeSheet.close();
               gui.output(fileName + " read succesfully.");
            }
            catch (BiffException ex) {
               gui.output(fileName + " is not a timesheet or is not in the"
                  + " .xls format. Please remove this file, handle it manually or "
                  + "convert it to .xls format. The data from this file will not be "
                  + "included in the output file.");
               failedSheets.add(fileName);
            }
            catch (IndexOutOfBoundsException ex) {
               gui.output(fileName + " contains less than 5 sheets in its "
                  + "workbook. The info from this timesheet will not be included in "
                  + "the output file. Reformat " + fileName + " to the standard "
                  + "timesheet format and rerun this program or add its info to the"
                  + " recap manually.");
               failedSheets.add(fileName);
            }
            catch (TimeSheetFormatException ex) {
               gui.output(fileName + " is incorrectly formatted. "
                  + ex.getMessage());
               failedSheets.add(fileName);
            }
         }
                     
         gui.output(newline + "The following comments were find in cells on a timesheet:");
         for (String comment : comments) {
            gui.output(">>" + comment);
         }
         
         gui.output(newline + "Failed to extract info from the following files:");
         for (String fileName : failedSheets) {
            gui.output(fileName);
         }
         
         calendar = Calendar.getInstance();
         date = String.format("%d_%d_%d", calendar.get(Calendar.MONTH) + 1, 
            calendar.get(Calendar.DATE), calendar.get(Calendar.YEAR));
            
         recapFile = new File(recapsFolderStr + "/" +RecapFileName+"_"+date+".xls");
         
         if (recapFile.exists() && !recapFile.isDirectory()) {
            try {
               gui.output(newline + "Deleting old recap file " + recapFile.getPath()
                  + "." + newline);
               recapFile.delete();
            }
            catch (Exception ex) {
               gui.output(newline + "Failed to delete old recap file "
                  + recapFile.getPath() + "." + newline);
            }
         }
         
         recap = new Recap(allJobs);
         recap.makeRecap(recapFile.getAbsolutePath());
         recap.writeRecap();
         
         gui.output(newline + "Recap Succesful!" + newline);
         
         gui.output("Saving logfile to logfile.txt" + newline);
         gui.saveLogFile(timeSheetFolderPath + "logfile.txt");
      }
      catch (Exception ex) {
         gui.output("Caught exception" + ex);
      }
   }
}
