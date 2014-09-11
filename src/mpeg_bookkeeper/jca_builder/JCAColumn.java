package mpeg_bookkeeper.jca_builder;

import jxl.*;
import jxl.write.*;
import java.io.File;
import java.io.IOException;
import jxl.read.biff.BiffException;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Point;

/**
 * Provides necessary methods to prepare a JCA so that new information
 * may be added to it.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class JCAColumn {
   public static final String PersonnelChargesStart = "PERSONNEL";
   public static final String InsideChargesStart = "INSIDE CHGS";
   public static final String ReimbursableChargesStart = "REIMB CHGS";
   public static final String lastRowTerminator = "Actual Budget Remaining";
   public static final String WeekEndDate = "WEEK ENDING DATE:";
   public static final String CopyStartLabel = "PERSONNEL";
   public static final String CopyEndLabel = "Actual Budget Remaining";
   public static final String TotalsStartLabel = "TOTAL LABOR";
   public static final String BillingsLabel = "Billings\\Retainer";
   public static final String WriteOffLabel = "Write Off's (On's are negative #)";
   public static final String BudgetStart = "BUDGET";
   public static final String BudgetEnd = "Actual Budget Remaining";
   public static final int StartOfDataColumn = 4;
   public static final int SumColumn = 2;
   
   public static void prepareJCA(WritableSheet sheet) throws JCAException, WriteException {
      shiftAllCharges(sheet);
      clearNewWeekColumn(sheet);
      updateFormulas(sheet);
   }

   public static void shiftAllCharges(WritableSheet sheet) throws JCAException {
      int endCol;
      int startCol;
      Cell searchCell;
      
      searchCell = sheet.findCell(WeekEndDate);
      
      if (searchCell == null)
         throw new JCAException("Could not locate the \"Week Ending Date:\" heading.");
      startCol = searchCell.getColumn() + 1;
      
      //endCol = sheet.getColumns() - 1;
      
      try {
         endCol = lastColumn(sheet);
         for (int curCol = endCol; curCol >= startCol; curCol--) {
            shiftChargesColumn(sheet, curCol);
         }
      }
      catch (Exception ex) {
         System.out.println("Caught " + ex + " when shifting charges.");
      }
   }

   public static void shiftChargesColumn(WritableSheet sheet, int col)
      throws WriteException, JCAException {
      WritableCell toCopy;
      WritableCell copy;
      String cellContents;
      Cell searchCell;
      
      int destination = col + 1;
      int curRow;
      int copyStart;
      int copyEnd;

      searchCell = sheet.findCell(CopyStartLabel);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"PERSONNEL\" heading.");
      copyStart = searchCell.getRow();
      
      searchCell = sheet.findCell(CopyEndLabel);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"Actual Budget Remaining\" heading.");
      copyEnd = searchCell.getRow();
            
      for (curRow = copyStart - 1; curRow <= copyEnd; curRow++) {
         toCopy = sheet.getWritableCell(col, curRow);
         copy = toCopy.copyTo(destination, curRow);
         sheet.addCell(copy);
      } 
   }
   
   public static void clearNewWeekColumn(WritableSheet sheet)
      throws WriteException, JCAException {
      jxl.write.Blank blankCell;
      WritableCell cell;
      Cell searchCell;
      
      int curRow;
      int pChargesStart;
      int pChargesEnd;
      int iChargesStart;
      int iChargesEnd;
      int rChargesStart;
      int rChargesEnd;
      int totalsStart;
      int totalsEnd;
      int budgetStart;
      int budgetEnd;
      
      jxl.format.CellFormat format;
            
      searchCell = sheet.findCell(PersonnelChargesStart);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"PERSONNEL\" heading.");
      pChargesStart = searchCell.getRow();
      
      searchCell = sheet.findCell(InsideChargesStart);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"INSIDE CHGS\" heading.");
      pChargesEnd = iChargesStart = searchCell.getRow();
      
      searchCell = sheet.findCell(ReimbursableChargesStart);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"REIMB CHGS\" heading.");
      iChargesEnd = rChargesStart = searchCell.getRow();
      
      searchCell = sheet.findCell(WeekEndDate);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"Week Ending Date:\" heading.");
      rChargesEnd = totalsStart = searchCell.getRow();
      
      searchCell = sheet.findCell(BudgetStart);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"BUDGET\" heading.");
      totalsEnd = budgetStart = searchCell.getRow();
      
      searchCell = sheet.findCell(BudgetEnd);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"Actual Budget Remaining\" heading.");
      budgetEnd = searchCell.getRow();
      
      for (curRow = pChargesStart + 1; curRow < pChargesEnd; curRow++) {
         format = sheet.getCell(StartOfDataColumn, curRow).getCellFormat();
         blankCell = new jxl.write.Blank(StartOfDataColumn, curRow, format);
         sheet.addCell(blankCell);
      }  
      
      for (curRow = iChargesStart + 1; curRow < iChargesEnd; curRow++) {
         format = sheet.getCell(StartOfDataColumn, curRow).getCellFormat();
         blankCell = new jxl.write.Blank(StartOfDataColumn, curRow, format);
         sheet.addCell(blankCell);
      }  
      
      for (curRow = rChargesStart + 1; curRow <= rChargesEnd; curRow++) {
         format = sheet.getCell(StartOfDataColumn, curRow).getCellFormat();
         blankCell = new jxl.write.Blank(StartOfDataColumn, curRow, format);
         sheet.addCell(blankCell);
      }  
      
      for (curRow = totalsStart + 1; curRow < totalsEnd; curRow++) {
         format = sheet.getCell(StartOfDataColumn, curRow).getCellFormat();
         blankCell = new jxl.write.Blank(StartOfDataColumn, curRow, format);
         sheet.addCell(blankCell);
      }  
      
      for (curRow = budgetStart; curRow <= budgetEnd; curRow++) {
         format = sheet.getCell(StartOfDataColumn, curRow).getCellFormat();
         blankCell = new jxl.write.Blank(StartOfDataColumn, curRow, format);
         sheet.addCell(blankCell);
      }  
   }
   
   public static void updateFormulas(WritableSheet sheet) throws JCAException {
      jxl.write.Formula newFormulaCell;
      String oldFormula;
      String newFormula;
      jxl.format.CellFormat standardFormat;
      
      Cell searchCell;
      
      int curRow;
      int curCol;
      int lastCol;
      String lastColLetter;
      int pChargesStart;
      int pChargesEnd;
      int iChargesStart;
      int iChargesEnd;
      int rChargesStart;
      int rChargesEnd;
      int totalsStart;
      int totalsEnd;
      int billingsRow;
      int writeOffRow;
      
      searchCell = sheet.findCell(PersonnelChargesStart);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"PERSONNEL\" heading.");
      pChargesStart = searchCell.getRow();
      
      searchCell = sheet.findCell(InsideChargesStart);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"INSIDE CHGS\" heading.");
      pChargesEnd = iChargesStart = searchCell.getRow();
      
      searchCell = sheet.findCell(ReimbursableChargesStart);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"REIMB CHGS\" heading.");
      iChargesEnd = rChargesStart = searchCell.getRow();
      
      searchCell = sheet.findCell(WeekEndDate);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"Week Ending Date:\" heading.");
      rChargesEnd = searchCell.getRow();
      
      searchCell = sheet.findCell(TotalsStartLabel);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"TOTAL LABOR\" heading.");
      totalsStart = searchCell.getRow();
      totalsEnd = totalsStart + 3;
      
      searchCell = sheet.findCell(BillingsLabel);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"Billings\\Retainer\" heading.");
      billingsRow = searchCell.getRow();
      
      searchCell = sheet.findCell(WriteOffLabel);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"Write Off's (On's are negative #)\" heading.");
      writeOffRow = searchCell.getRow();
      
      
      try {
         lastCol = lastColumn(sheet);
         lastColLetter = CellReferenceHelper.getColumnReference(lastCol);
         
         for (curRow = pChargesStart + 1; curRow < pChargesEnd; curRow++) {
            int row = curRow + 1;
            standardFormat = sheet.getCell(SumColumn,curRow).getCellFormat();
            newFormulaCell = new jxl.write.Formula(SumColumn, curRow, 
               "SUM(E"+row+":"+lastColLetter+row+")", standardFormat);
            sheet.addCell(newFormulaCell);
         }  
               
         for (curRow = iChargesStart + 1; curRow < iChargesEnd; curRow++) {
            int row = curRow + 1;
            standardFormat = sheet.getCell(SumColumn,curRow).getCellFormat();
            newFormulaCell = new jxl.write.Formula(SumColumn, curRow,
               "SUM(E"+row+":"+lastColLetter+row+")", standardFormat);
            sheet.addCell(newFormulaCell);
         }  
         
         for (curRow = rChargesStart + 1; curRow < rChargesEnd; curRow++) {
            int row = curRow + 1;
            standardFormat = sheet.getCell(SumColumn,curRow).getCellFormat();
            newFormulaCell = new jxl.write.Formula(SumColumn, curRow,
               "SUM(E"+row+":"+lastColLetter+row+")", standardFormat);
            sheet.addCell(newFormulaCell);
         }  
         
         for (curRow = totalsStart; curRow < totalsEnd; curRow++) {
            int row = curRow + 1;
            standardFormat = sheet.getCell(SumColumn + 1, curRow).getCellFormat();
            newFormulaCell = new jxl.write.Formula(SumColumn + 1, curRow,
               "SUM(E"+row+":"+lastColLetter+row+")", standardFormat);
            sheet.addCell(newFormulaCell);
         }  
         
         int row = billingsRow + 1;
         standardFormat = sheet.getCell(SumColumn + 1, billingsRow).getCellFormat();
         newFormulaCell = new jxl.write.Formula(SumColumn + 1, billingsRow,
            "SUM(E"+row+":"+lastColLetter+row+")", standardFormat);
         sheet.addCell(newFormulaCell);
         
         row = writeOffRow + 1;
         standardFormat = sheet.getCell(SumColumn + 1, writeOffRow).getCellFormat();
         newFormulaCell = new jxl.write.Formula(SumColumn + 1, writeOffRow,
            "SUM(E"+row+":"+lastColLetter+row+")", standardFormat);
         sheet.addCell(newFormulaCell);
         
      }
      catch (Exception ex) {
         System.out.println("Formula Update failed.");
      }
   }
   
   public static void updateColumnFormulas(WritableSheet sheet) 
      throws WriteException, JCAException, IOException, BiffException {
      int curCol;
      int curRow;
      int pStart;
      int pEnd;
      int iStart;
      int iEnd;
      int rStart;
      int rEnd;
      int lastCol;
      
      Cell searchCell;
      String columnLetter;
      jxl.write.Formula newFormulaCell;
      jxl.format.CellFormat standardFormat;
      
      searchCell = sheet.findCell(PersonnelChargesStart);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"PERSONNEL\" heading.");
      pStart = searchCell.getRow() + 2;
      
      searchCell = sheet.findCell(InsideChargesStart);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"INSIDE CHGS\" heading.");
      pEnd = searchCell.getRow();
      iStart = pEnd + 2;
      
      searchCell = sheet.findCell(ReimbursableChargesStart);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"REIMB CHGS\" heading.");
      iEnd = searchCell.getRow();
      rStart = iEnd + 2;
      
      searchCell = sheet.findCell(WeekEndDate);
      if (searchCell == null)
         throw new JCAException("Could not locate the \"Week Ending Date:\" heading.");
      rEnd = searchCell.getRow();
         
      lastCol = lastColumn(sheet);
      
      for (curCol = StartOfDataColumn; curCol < sheet.getColumns()
         && curCol <= lastCol; curCol++) {
         if (isWeekColumn(sheet, curCol)) {
            searchCell = sheet.findCell(TotalsStartLabel);
            if (searchCell == null)
               throw new JCAException("Could not locate the \"TOTAL LABOR\" heading.");
            curRow = searchCell.getRow();
            
            columnLetter = CellReferenceHelper.getColumnReference(curCol);
            
            standardFormat = sheet.getCell(curCol, curRow).getCellFormat();
            newFormulaCell = new jxl.write.Formula(curCol, curRow, 
               "SUMPRODUCT($B$"+pStart+":$B$"+pEnd+", "+columnLetter+pStart
               +":"+columnLetter+pEnd+")", standardFormat);
            copyFeatures(sheet, newFormulaCell, curCol, curRow);
            sheet.addCell(newFormulaCell);
         
            curRow++;
            
            standardFormat = sheet.getCell(curCol, curRow).getCellFormat();
            newFormulaCell = new jxl.write.Formula(curCol, curRow, 
               "SUMPRODUCT($B$"+iStart+":$B$"+iEnd+", "+columnLetter+iStart
               +":"+columnLetter+iEnd+")", standardFormat);
            copyFeatures(sheet, newFormulaCell, curCol, curRow);
            sheet.addCell(newFormulaCell);
         
            curRow++;
         
            standardFormat = sheet.getCell(curCol, curRow).getCellFormat();
            newFormulaCell = new jxl.write.Formula(curCol, curRow, 
               "SUMPRODUCT($B$"+rStart+":$B$"+rEnd+", "+columnLetter+rStart
               +":"+columnLetter+rEnd+")", standardFormat);
            copyFeatures(sheet, newFormulaCell, curCol, curRow);
            sheet.addCell(newFormulaCell);
         
            curRow++;
         
            standardFormat = sheet.getCell(curCol, curRow).getCellFormat();
            newFormulaCell = new jxl.write.Formula(curCol, curRow, 
               "SUM("+columnLetter+(curRow-2)+":"+columnLetter+curRow+")"
               , standardFormat);
            copyFeatures(sheet, newFormulaCell, curCol, curRow);
            sheet.addCell(newFormulaCell);
         }
         else {
            searchCell = sheet.findCell(TotalsStartLabel);
            if (searchCell == null)
               throw new JCAException("Could not locate the \"TOTAL LABOR\" heading.");
            curRow = searchCell.getRow() + 3;
            
            columnLetter = CellReferenceHelper.getColumnReference(curCol);
            standardFormat = sheet.getCell(curCol, curRow).getCellFormat();
            newFormulaCell = new jxl.write.Formula(curCol, curRow, 
               "SUM("+columnLetter+(curRow-2)+":"+columnLetter+curRow+")"
               , standardFormat);
            copyFeatures(sheet, newFormulaCell, curCol, curRow);
            sheet.addCell(newFormulaCell);
         }
      }
   }
   
   private static void copyFeatures(WritableSheet sheet, WritableCell cell, int col, int row) {
      Cell ogCell;
      CellFeatures ogFeatures;
      WritableCellFeatures features;
      
      
      if (sheet == null || cell == null) 
         return;
         
      ogCell = sheet.getCell(col, row);
      
      if (ogCell == null)
         return;
         
      ogFeatures = ogCell.getCellFeatures();
      
      if (ogFeatures == null)
         return;
      
      features = new WritableCellFeatures(ogFeatures);

      cell.setCellFeatures(features);
   }
   
   private static boolean isWeekColumn(WritableSheet sheet, int col) throws JCAException, IOException, BiffException {
      boolean needFormulas;
      Cell searchCell;
      int row;
      int endRow;
      
      needFormulas = false;
      
      searchCell = sheet.findCell(CopyStartLabel);
      if (searchCell == null)
         throw new JCAException("Could not find label \"PERSONNEL\"");
      
      row = searchCell.getRow() + 1;
      
      searchCell = sheet.findCell(InsideChargesStart);
      if (searchCell == null)
         throw new JCAException("Could not find label \"INSIDE CHGS\"");
         
      endRow = searchCell.getRow();
      
      for (; row < endRow; row++)
         if (sheet.getCell(col, row).getContents().compareTo("") != 0)
            needFormulas = true;
      
      row = endRow + 1;
      
      searchCell = sheet.findCell(ReimbursableChargesStart);
      
      if (searchCell == null)
         throw new JCAException("Could not find label \"REIMB CHGS\"");
         
      endRow = searchCell.getRow();
      
      for (; row < endRow; row++)
         if (sheet.getCell(col, row).getContents().compareTo("") != 0)
            needFormulas = true;
      
      row = endRow + 1;
      
      searchCell = sheet.findCell(WeekEndDate);
      
      if (searchCell == null)
         throw new JCAException("Could not find label \"WEEK ENDING DATE:\"");
         
      endRow = searchCell.getRow();
      
      for (; row < endRow; row++)
         if (sheet.getCell(col, row).getContents().compareTo("") != 0)
            needFormulas = true;
   
            
      return needFormulas;
   }

   private static int lastColumn(WritableSheet sheet) 
      throws JCAException, IOException, BiffException {
      int col;
      int row;
      Cell searchCell;
      
      searchCell = sheet.findCell("HRS");
      
      if (searchCell == null)
         throw new JCAException("Could not find label \"HRS\"");
      
      row = searchCell.getRow();
      col = StartOfDataColumn;
      
      while (searchCell.getContents().compareTo("HRS") == 0) {
         col++;
         if (col < sheet.getColumns())
            searchCell = sheet.getCell(col, row);
         else
            break;
      }
      
      return col - 1;
   }
}
