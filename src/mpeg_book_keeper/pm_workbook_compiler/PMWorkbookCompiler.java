package mpeg_book_keeper.pm_workbook_compiler;

import java.io.IOException;
import jxl.read.biff.BiffException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import jxl.write.*;
import jxl.*;

public class PMWorkbookCompiler {
   private static final String ProjectManagerLabel = "PROJ MGR :";
   
   public static PMGui gui;
   
   public static void compilePMWorkbooks(PMGui inGui, String jcaFolderName,
   	String workbooksFolderName, String date) {
      ArrayList<String> excluded;
      ArrayList<PMWorkbook> pmWorkbooks;
      PMWorkbook newPMWorkbook;
      String pmFolderName;
      File pmFolder;
      boolean found;
      File jcaFolder;
      File[] jcaFiles;
      ArrayList<JCAFileName> jcaFileNames;
      ArrayList<String> tempExcluded;
      String pm;
      
      gui = inGui;
      output("Using JCAs from " + jcaFolderName);
      pmFolderName = workbooksFolderName + "/Project Manager Workbooks" 
      	+ " " + date.replace('/', '_');
      pmFolder = new File(pmFolderName);
      pmFolder.mkdir();
      output("Created pmFolder at " + pmFolderName);
      pmWorkbooks = new ArrayList<PMWorkbook>();
      
      jcaFolder = new File(jcaFolderName);
      jcaFiles = jcaFolder.listFiles();
      
      jcaFileNames = new ArrayList<JCAFileName>();
      
      for (File file : jcaFiles) {
          if (file.isFile() && file.getAbsolutePath().contains(".xls")) {
              jcaFileNames.add(new JCAFileName(jcaFolderName + "/" + file.getName()));
          }
      }
      
      Collections.sort(jcaFileNames);
      
      excluded = new ArrayList<String>();
      
      output("");
      
      for (JCAFileName fileName : jcaFileNames) {
         String fileNameShort = fileName.getFileName();
         try {
            pm = getProjectManager(fileName.getFilePath());
      
            found = false;
            
            if (pm != null) {
               for (PMWorkbook pmWorkbook : pmWorkbooks) {
                  if (pm.compareTo(pmWorkbook.getProjectManager().trim()) == 0) {
                     try {
                        pmWorkbook.addSheet(fileName.getFilePath());
                        output("   " + fileNameShort + " sorted to " 
                           + pmWorkbook.getProjectManager());
                     }
                     catch (Exception ex) {
                        output("Could not get sheet for " + fileNameShort);
                        excluded.add(fileNameShort);
                     }  
                     found = true;
                  }
               }
               if (found == false) {
                  try {
                     output("   Creating workbook for " + pm);
                     newPMWorkbook = new PMWorkbook(pm, pmFolderName);
                     newPMWorkbook.addSheet(fileName.getFilePath());
                     pmWorkbooks.add(newPMWorkbook);
                  }
                  catch (Exception ex) {
                     output("Could not get sheet for " + fileNameShort);
                     excluded.add(fileNameShort);
                  }
               }
            }
            else {
               output("Could not find project manager for " + fileNameShort);
               excluded.add(fileNameShort);
            } 
         
         }
         catch (Exception ex) {
            output("Could not find project manager for " + fileNameShort);
            output("   " + ex);
         }
      }
      
      output("");
      output("List of sorted jcas:");
      output("");
      for (PMWorkbook pmWorkbook : pmWorkbooks) {
         output(pmWorkbook.toString());
      }
      
      output("");
      for (PMWorkbook pmWorkbook : pmWorkbooks) {
         try {
            output("Writing " + pmWorkbook.getProjectManager() + "'s workbook");
            tempExcluded = pmWorkbook.write();
            excluded.addAll(tempExcluded);
            tempExcluded = null;
         }
         catch (Exception ex) {
            output("      Failed to write pmWorkbook for " + pmWorkbook.getProjectManager());
            output("         " + ex);
            excluded.addAll(pmWorkbook.getSheets());
            printStackTrace(ex.getStackTrace());
         }
      }
      
      if (excluded.size() != 0) {
         output("");
         output("The following JCAs could not be sorted. Please add them manually.");
         for (String job : excluded) 
            output("   " + job);
      }
      
      output("");
      output("All done! :)");
      
      try {
         gui.saveLogFile(pmFolderName + "/PMWorkbook_logFile");
      }
      catch (Exception ex) {
         gui.output("Warning, failed to save log file");
      }
   }
   
   // Retrieves the project manager's initials from the JCA file at the path |jcaFile|.
   // Returns null if the initials aren't found for any reason.
   public static String getProjectManager(String jcaFile) throws Exception {
      Workbook workbook = Workbook.getWorkbook(new File(jcaFile));
      Sheet sheet = workbook.getSheet(0);
      Cell searchCell;
      Cell projectManagerCell;
      String projectManager;
      
      searchCell = sheet.findCell(ProjectManagerLabel);
      
      projectManager = null;
      
      if (searchCell != null) {
            
         projectManagerCell = sheet.getCell(searchCell.getColumn() + 1,
            searchCell.getRow());
            
         projectManager = projectManagerCell.getContents();
      }
      
      if (projectManager.compareTo("") == 0)
         return null;
      
      workbook.close();
      return projectManager.trim();
   }
   
   public static void output(String msg) {
      gui.output(msg);
   }
   
   public static void printStackTrace(StackTraceElement[] stack) {
      for (int i = 0; i < stack.length; i++) {
         output("      " + stack[i]);
      }
   }
}
