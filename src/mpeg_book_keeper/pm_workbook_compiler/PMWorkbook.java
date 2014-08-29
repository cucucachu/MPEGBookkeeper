package mpeg_book_keeper.pm_workbook_compiler;

import jxl.*;
import jxl.write.*;
import java.io.File;
import java.io.IOException;
import jxl.read.biff.BiffException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Calendar;

public class PMWorkbook {

   private static final String newline = System.lineSeparator();

   private String projectManager;
   private String pmFolder;
   private WritableWorkbook workbook;
   private ArrayList<String> sheets;
   
   public PMWorkbook(String projectManager, String pmFolder) {
      this.projectManager = projectManager;
      this.pmFolder = pmFolder;
      sheets = new ArrayList<String>();
      workbook = null;
   }
   
   public String getProjectManager() {
      return this.projectManager;
   }
   
   public ArrayList<String> getSheets() {
      return sheets;
   }
   
   public void addSheet(String sheet) {
      sheets.add(sheet);
   }
   
   public ArrayList<String> write() throws IOException, WriteException, BiffException, Exception {
      int index;
      Workbook jcaWorkbook;
      Sheet jcaSheet;
      WritableSheet copySheet;
      File workbookFile;
      String jobNo;
      ArrayList<String> failed;
      String date;
      Calendar calendar;
      
      index = 0;
      
      failed = new ArrayList<String>();
      
      calendar = Calendar.getInstance();
      date = String.format("%d_%d_%d", calendar.get(Calendar.MONTH) + 1, 
         calendar.get(Calendar.DATE), calendar.get(Calendar.YEAR));
         
      if (sheets.isEmpty() == false) {
         workbookFile = new File(pmFolder + "/" + projectManager + "_" + date + ".xls");
         workbook = Workbook.createWorkbook(workbookFile);
         
         for (String sheet : sheets) {
            jobNo = new File(sheet).getName();
            output("   copying sheet from " + jobNo);
            
            if (JCAFormat.badFormat(sheet)) {
               failed.add(jobNo);
               output("      " + jobNo + " failed. Incorect format.");
            }
            else {
               try {                              
                  jcaWorkbook = Workbook.getWorkbook(new File(sheet));
                  jcaSheet = jcaWorkbook.getSheet(0);
                  
                  copySheet = workbook.createSheet(jcaSheet.getName(), index);
                  
                  copyJCA(jcaSheet, copySheet);
                  JCAFormat.formatJCA(copySheet);
                  index++;
                  jcaWorkbook.close();
                  jcaWorkbook = null;
               }
               catch (Exception ex) {
                  output("      failed to copy " + jobNo);
                  output("         "+ ex);
                  failed.add(jobNo);
               }
               finally {
                  jcaWorkbook = null;
               }
            }
         }
         
         try {
            workbook.write(); //IOB Exception?
         }
         catch (ArrayIndexOutOfBoundsException ex) {
            workbook.close();
            workbookFile.delete();
            throw ex;
         }
         
         workbook.close();
      }
      
      return failed;
   }
   
   private void copyJCA(Sheet oldSheet, WritableSheet sheet) throws 
      Exception {
      int curRow;
      int curCol;
      
      Cell oldCell;
      WritableCell newCell;
      
      CellFeatures oldFeatures;
      WritableCellFeatures newFeatures;
      
      for (curRow = 0; curRow < oldSheet.getRows(); curRow++) {
         for (curCol = 0; curCol < oldSheet.getColumns(); curCol++) {            
            oldCell = oldSheet.getCell(curCol, curRow);
            oldFeatures = oldCell.getCellFeatures();
            
            if (oldCell.getType().equals(CellType.NUMBER))
               newCell = new jxl.write.Number(curCol, curRow, ((NumberCell)oldCell).getValue());
            else if (oldCell.getType().equals(CellType.NUMBER_FORMULA))
               newCell = new jxl.write.Formula(curCol, curRow, ((FormulaCell)oldCell).getFormula());
            else
               newCell = new jxl.write.Label(curCol, curRow, oldCell.getContents());
            
            if (oldFeatures != null) {
               newFeatures = new WritableCellFeatures(oldFeatures);
               newCell.setCellFeatures(newFeatures);
            }
            
            sheet.addCell(newCell);
         }
      }   
      
      sheet.setColumnView(0, 17);
   }
   
   private void output(String msg) {
      PMWorkbookCompiler.output(msg);
   }
   
   public String toString() {
      String name;
      String sheetNames;
      String temp;
      
      name = projectManager + ":" + newline;
      sheetNames = "";
      for (String sheet : sheets) {
         temp = (new File(sheet)).getName();
         
         sheetNames = sheetNames + "   " + temp + newline; 
      }
      
      return name + sheetNames;
   }
}
