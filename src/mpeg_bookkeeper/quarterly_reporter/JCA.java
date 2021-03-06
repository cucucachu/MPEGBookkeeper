package mpeg_bookkeeper.quarterly_reporter;

import jxl.*;
import jxl.write.*;
import java.io.File;
import java.io.IOException;
import jxl.read.biff.BiffException;
import java.util.ArrayList;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

/**
 * Provides methods to read certain information from a JCA.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class JCA implements Comparable<JCA> {
   private static final String NewFormatLabel = "JCA Upload Format";
   private static final String NewProjectNumberLabel = "JOB:";
   private static final String OldProjectNumberLabel = "JOB NO.:";
   private static final String NewProjectNameLabel = "PROJ NAME:";
   private static final String OldProjectNameLabel = "PROJ NAME:";
   private static final String NewManagerLabel = "PROJ MGR :";
   private static final String OldManagerLabel = "PROJ MGR :";
   private static final String NewUnbilledChargesLabel = "Unbilled Charges";
   private static final String OldUnbilledChargesLabel = "Unbilled Charges";
   private static final String OldRemainingBudgetLabel = "Actual Budget Remaining";
   private static final String NewRemainingBudgetLabel = "Actual Budget Remaining";
   private static final String NewWriteOffsLabel = "Write Off's (On's are negative #)";
   private static final String OldWriteOffsLabel = "Write Off's (On's are negative #)";
   
   private static final String LaborStart = "PERSONNEL";
   private static final String InsideStart = "INSIDE CHGS";
   private static final String ReimbursableStart = "REIMB CHGS";
   private static final String ReimbursableEnd = "WEEK ENDING DATE:";
   private static final String TotalsStart = "TOTAL LABOR";
   private static final String BillingsLabel = "Billings\\Retainer";
   private static final String BudgetLabel = "BUDGET";
   
   private static final int NewSOCCol = 1;
   private static final int NewSOCRow = 1;
   private static final int StartOfDataColumn = 4;
   private static final int TotalsColumn = 3;
   private static final String OldSOCLabel = "ITEM";

   private String fileName;
   private File file;
   private Workbook workbook;
   private Sheet sheet;
   private int sheetNo;
   private int opened;
   private Double scarab;
   
   public JCA(String fileName) {
      this.fileName = fileName;
      this.file = new File(fileName);
      opened = 0;
      sheetNo = -1;
      scarab = new Double(0);      
   }
   
   public String getProjectNumber() throws JCAException, IOException, 
      BiffException {
      Cell searchCell;
      Cell projectNumberCell;
      String projectNumber;
      
      open();
      
      if (newFormat()) {
         searchCell = sheet.findCell(NewProjectNumberLabel);
         if (searchCell != null) {
            projectNumberCell = sheet.getCell(searchCell.getColumn() + 1, 
               searchCell.getRow());
            projectNumber = projectNumberCell.getContents();
         }
         else {
            throw new JCAException("Could not find Project Number Label \"JOB:\"");
         }
      }
      else {
         searchCell = sheet.findCell(OldProjectNumberLabel);
         if (searchCell != null) {
            projectNumberCell = sheet.getCell(searchCell.getColumn() + 1, 
               searchCell.getRow());
            projectNumber = projectNumberCell.getContents();
         }
         else {
            throw new JCAException("Could not find Project Number Label \"JOB NO.:\"");
         }
      }
      close();
      return projectNumber;
   }
   
   public String getProjectName() throws JCAException, IOException, 
      BiffException {
      Cell searchCell;
      Cell projectNameCell;
      String projectName;
      
      open();
      
      if (newFormat()) {
         searchCell = sheet.findCell(NewProjectNameLabel);
         if (searchCell != null) {
            projectNameCell = sheet.getCell(searchCell.getColumn() + 1, 
               searchCell.getRow());
            projectName = projectNameCell.getContents();
         }
         else {
            throw new JCAException("Could not find Project Name Label \"PROJ NAME:\"");
         }
      }
      else {
         searchCell = sheet.findCell(OldProjectNameLabel);
         if (searchCell != null) {
            projectNameCell = sheet.getCell(searchCell.getColumn() + 1, 
               searchCell.getRow());
            projectName = projectNameCell.getContents();
         }
         else {
            throw new JCAException("Could not find Project Name Label \"PROJ NAME:\"");
         }
      }
      close();
      return projectName;
   }
   
   public String getFileName() {
      return file.getName();
   }
   
   public String getManager() throws JCAException, IOException, 
      BiffException {
      Cell searchCell;
      Cell managerCell;
      String manager;
      
      open();
      
      if (newFormat()) {
         searchCell = sheet.findCell(NewManagerLabel);
         if (searchCell != null) {
            managerCell = sheet.getCell(searchCell.getColumn() + 1, 
               searchCell.getRow());
            manager = managerCell.getContents();
         }
         else {
            throw new JCAException("Could not find manager initials label \"PROJ MGR :\"");
         }
      }
      else {
         searchCell = sheet.findCell(OldManagerLabel);
         if (searchCell != null) {
            managerCell = sheet.getCell(searchCell.getColumn() + 1, 
               searchCell.getRow());
            manager = managerCell.getContents();
         }
         else {
            throw new JCAException("Could not find manager initials label \"PROJ MGR :\"");
         }
      }
      close();
      return manager;
   }
   
   public Double getUnbilledCharges() throws JCAException, IOException, 
      BiffException, NumberFormatException {
      Cell searchCell;
      Cell chargesCell;
      Double charges;
      
      open();
      
      if (newFormat()) {
         searchCell = sheet.findCell(NewUnbilledChargesLabel);
         if (searchCell != null) {
            chargesCell = sheet.getCell(searchCell.getColumn() + 3, 
               searchCell.getRow());
            charges = new Double(chargesCell.getContents());
            scarab = scarabUnbilled() - charges;
            
            if (getScarab() != 0)
               charges = scarabUnbilled();
               
         }
         else {
            throw new JCAException("Could not find unbilled charges label "
               + "\"Unbilled Charges\"");
         }
      }
      else {
         searchCell = sheet.findCell(OldUnbilledChargesLabel);
         if (searchCell != null) {
            chargesCell = sheet.getCell(searchCell.getColumn() + 3, 
               searchCell.getRow());
            charges = new Double(chargesCell.getContents());
         }
         else {
            throw new JCAException("Could not find unbilled charges label "
               + "\"Unbilled Charges\"");
         }
      }
      
      close();
      
      
      return charges;
   }
   
   public Double getScarab() {
      if (scarab < .01 && scarab > -.01)
         return 0.;
      return scarab;
   }
   
   private void printColumn(int col) throws JCAException, IOException, 
      BiffException, NumberFormatException {
      open();
      
      //output("Column " + col + ":");
      
      for (int i = 0; i < sheet.getRows(); i++)
         //output("   " + sheet.getCell(col, i).getContents());
         
      close();
   }
   
   public Double getRemainingBudget() throws JCAException, IOException, 
      BiffException, NumberFormatException {
      Cell searchCell;
      Cell budgetCell;
      Double budget;
      
      open();
      
      if (newFormat()) {
         searchCell = sheet.findCell(NewRemainingBudgetLabel);
         if (searchCell != null) {
            if (getScarab() == 0) {
               budgetCell = sheet.getCell(searchCell.getColumn() + 3, 
                  searchCell.getRow());
               budget = new Double(budgetCell.getContents());
            }
            else {
               budget = scarabBudget();
            }
         }
         else {
            throw new JCAException("Could not find remaining budget label "
               + "\"Actual Budget Remaining\"");
         }
      }
      else {
         searchCell = sheet.findCell(OldRemainingBudgetLabel);
         if (searchCell != null) {
            budgetCell = sheet.getCell(searchCell.getColumn() + 3, 
               searchCell.getRow());
            budget = new Double(budgetCell.getContents());
         }
         else {
            throw new JCAException("Could not find remaining budget label "
               + "\"Actual Budget Remaining\"");
         }
      }
      close();
      return budget;
   }
   
   private Double scarabBudget() throws JCAException, IOException, 
      BiffException {
      Double budget;
      Cell searchCell;
      Cell dataCell;
      
      budget = 0.;
      open();
      
      searchCell = sheet.findCell(BudgetLabel);
      
      if (searchCell == null)
         throw new JCAException("Could not find label \"BUDGET\"");
         
      dataCell = sheet.getCell(searchCell.getColumn() + 3, searchCell.getRow());
      budget = new Double(dataCell.getContents());
      
      budget -= rowSum(dataCell.getRow() + 1);
      
      budget -= scarabUnbilled();
      
      close();
      return budget;
   }
   
   public String getSoc() throws JCAException, IOException, 
      BiffException {
      Cell searchCell;
      Cell socCell;
      String soc;
      
      open();
      
      if (newFormat()) {
         socCell = sheet.getCell(NewSOCCol, NewSOCRow);
         soc = socCell.getContents();
      }
      else {
         searchCell = sheet.findCell(OldSOCLabel);
         if (searchCell != null) {
            socCell = sheet.getCell(searchCell.getColumn() + 1, 
               searchCell.getRow());
            soc = socCell.getContents();
         }
         else {
            throw new JCAException("Could not find SOC label \"ITEM\"");
         }
      }
      close();
      return soc;
   }
   
   public ArrayList<WriteOff> getWriteOffs(String year) throws JCAException, IOException, 
      BiffException {
      Cell searchCell;
      Cell writeOffCell;
      Cell labelCell;
      int writeOffsRow;
      Double amount;
      Double prior;
      Double[] quarters;
      String label;
      String[] quarterNames = {"Q1", "Q2", "Q3", "Nov", "Q4"};
      
      WriteOff newWriteOff;
      ArrayList<WriteOff> writeOffs;
      
      
      quarters = new Double[5];
      for (int i = 0; i < 5; i++)
         quarters[i] = new Double(0);
      
      open();
      
      if (newFormat())
         searchCell = sheet.findCell(NewWriteOffsLabel);
      else
         searchCell = sheet.findCell(OldWriteOffsLabel);
         
      if (searchCell != null) {     
         try {
                   
            writeOffsRow = searchCell.getRow();
            prior = 0.0;
            
            for (int curCol = StartOfDataColumn; curCol < sheet.getColumns(); curCol++) {
               writeOffCell = sheet.getCell(curCol, writeOffsRow);
               labelCell = sheet.getCell(curCol, writeOffsRow + 1);
               
               if (writeOffCell.getContents().compareTo("") != 0) {               
                  label = labelCell.getContents().toUpperCase();
                  
                  if (label.compareTo("") != 0) {
                     if (label.length() > 6 && label.contains(year)) {
                        if (label.charAt(0) == 'Q') {
                           if (label.charAt(1) == '1')
                              quarters[0] += new Double(writeOffCell.getContents());
                           else if (label.charAt(1) == '2')
                              quarters[1] += new Double(writeOffCell.getContents());
                           else if (label.charAt(1) == '3')
                              quarters[2] += new Double(writeOffCell.getContents());
                           else if (label.charAt(1) == '4')
                              quarters[4] += new Double(writeOffCell.getContents());
                        }
                        else if (label.charAt(0) == 'N' 
                           || label.contains("-11-")) {
                           quarters[3] += new Double(writeOffCell.getContents());
                        }
                        else
                           prior += new Double(writeOffCell.getContents());
                     }
                     else
                        prior += new Double(writeOffCell.getContents());
                  }
                  else
                     prior += new Double(writeOffCell.getContents());
               }
            }
            
            writeOffs = new ArrayList<WriteOff>();
            
            for (int i = 0; i < 5; i++) {
               if (quarters[i] != 0) {
                  newWriteOff = new WriteOff(quarterNames[i], 
                     quarters[i]);
                  writeOffs.add(newWriteOff);
               }
            }
            
            writeOffs.add(new WriteOff("Prior", prior));
         }
         catch (Exception ex) {
            throw new JCAException("      Exception thrown from JCA.getWriteOffs(): " 
               + ex.getMessage());
         }
      }
      else {
         throw new JCAException("Could not find Write offs label "
            + "\"Write Off's (On's are negative #)\"");
      }
      
      close();
      return writeOffs;
   }
   
   public boolean recentWriteOffs(String year) throws JCAException, IOException, 
      BiffException {
      Cell searchCell;
      int labelRow;
      boolean toReturn;
      
      toReturn = false;
      
      open();
      
      if (newFormat())
         searchCell = sheet.findCell(NewWriteOffsLabel);
      else
         searchCell = sheet.findCell(OldWriteOffsLabel);
         
      if (searchCell != null) {     
         labelRow = searchCell.getRow() + 1;
         
         for (int curCol = StartOfDataColumn; curCol < sheet.getColumns(); curCol++)
            if (sheet.getCell(curCol, labelRow).getContents().contains(year))
               toReturn = true;
      }
      else
         throw new JCAException("Could not find Write offs label "
            + "\"Write Off's (On's are negative #)\"");
            
     close();
     return toReturn;
   }
   
   public boolean newFormat() throws JCAException, IOException, BiffException {
      Cell searchCell;
      boolean toReturn;
      
      open();
      
      try {
         searchCell = sheet.findCell(NewFormatLabel);
         
         if (searchCell != null) {
            toReturn = true;
         }
         else 
            toReturn = false;
      }
      catch (Exception ex) {
         throw new JCAException("      Exception thrown by JCA.newFormat(): " + ex.getMessage());
      }
      close();
      return toReturn;
   }
   
   public boolean exists() {
      return file.exists();
   }
   
   private void open() throws JCAException, IOException, BiffException {
      int curSheet;
   
      if (!exists()) {
         throw new JCAException("Cannot find the file for this JCA");
      }   
      else {
         if (opened == 0) {
            workbook = Workbook.getWorkbook(file);
            
            if (sheetNo == -1) {
               sheet = workbook.getSheet(0);
               
               if (sheet.findCell(NewFormatLabel) == null) {
                  for (sheet = workbook.getSheet(curSheet = workbook.getNumberOfSheets() - 1); 
                     curSheet > 0 && sheet.getName().contains("Sheet"); 
                     sheet = workbook.getSheet(--curSheet))
                     ;
                  sheetNo = curSheet;
               }
               else {
                  sheetNo = 0;
               }
            }
            else
               sheet = workbook.getSheet(sheetNo);
         }
         opened++;
      }
   }
   
   private void close() {
      if (opened == 1) {
         workbook.close();
         workbook = null;
         sheet = null;
      }
      opened--;
   }
   
   public String toString() {
      try {
         return this.getProjectNumber();
      }
      catch (Exception ex) {
         return fileName + " could not find project number.";
      }
   }
   
   public int compareTo(JCA other) {
      String name;
      String otherName;
      int comparison;
      
      name = file.getName();
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
   
   private Double scarabUnbilled() throws JCAException, IOException, BiffException {
      Double unbilled;
      Cell searchCell;
      
      unbilled = totalLabor();
      
      searchCell = sheet.findCell(BillingsLabel);
      
      if (searchCell == null)
         throw new JCAException("Could not find label \"" + BillingsLabel + "\".");
         
      unbilled -= rowSum(searchCell.getRow());
      
      searchCell = sheet.findCell(NewWriteOffsLabel);
      
      if (searchCell == null)
         throw new JCAException("Could not find label \"" + NewWriteOffsLabel + "\".");
         
      unbilled -= rowSum(searchCell.getRow());
      
      return unbilled;
   }
   
   private Double totalLabor() throws JCAException, IOException, BiffException {
      Double total;
      Cell searchCell;
      Cell valueCell;
      int lastColumn;
      int row;
      int endRow;
      ScriptEngine evaluator;
      String evaluation;
      
      total = 0.;
      open();
      searchCell = sheet.findCell(LaborStart);
      
      if (searchCell == null)
         throw new JCAException("Could not find label \"PERSONNEL\"");
      
      row = searchCell.getRow() + 1;
      
      searchCell = sheet.findCell(InsideStart);
      if (searchCell == null)
         throw new JCAException("Could not find label \"INSIDE CHGS\"");
         
      endRow = searchCell.getRow();
      
      for (; row < endRow; row++)
         total += rowSubtotal(row);
      
      row = endRow + 1;
      
      searchCell = sheet.findCell(ReimbursableStart);
      
      if (searchCell == null)
         throw new JCAException("Could not find label \"REIMB CHGS\"");
         
      endRow = searchCell.getRow();
      
      for (; row < endRow; row++)
         total += rowSubtotal(row);
         
      row = endRow + 1;
      
      searchCell = sheet.findCell(ReimbursableEnd);
      
      if (searchCell == null)
         throw new JCAException("Could not find label \"WEEK ENDING DATE:\"");
         
      endRow = searchCell.getRow();
      
      for (; row < endRow; row++)
         total += rowSubtotal(row);
         
      searchCell = sheet.findCell(TotalsStart);
      
      if (searchCell == null)
         throw new JCAException("Could not find label \"TOTAL LABOR\"");
         
      evaluator = (new ScriptEngineManager()).getEngineByName("JavaScript");
      
      lastColumn = lastColumn();
         
      if (continuationSheet()) {     
         for (row = searchCell.getRow(); row < searchCell.getRow() + 3; row++) {
            try {
               valueCell = sheet.getCell(lastColumn, row);
               if (valueCell.getType().equals(CellType.NUMBER_FORMULA)
                  && new Double(valueCell.getContents()) == 4) {
                  try {
                     evaluation = evaluator.eval(((FormulaCell)valueCell).getFormula()).toString();
                     total += new Double(evaluation);
                  }
                  catch(Exception ex) {
                     throw new JCAException("There is a bad formula in row " + (row + 1) 
                        + " of " + file.getName());
                  }
               }
               else
                  total += new Double(valueCell.getContents());
            }
            catch (NumberFormatException ex) {
               throw new JCAException("Number format Exception col: " + lastColumn);
            }
            catch (JCAException ex) {
               throw ex;
            }
         }
      }   
      
      close();
      return total;
   }
   
   private boolean continuationSheet() throws JCAException, IOException, BiffException {
      boolean continuation;
      Cell searchCell;
      int col;
      int row;
      int endRow;
      
      open();
      
      continuation = true;
      col = lastColumn();
      
      searchCell = sheet.findCell(LaborStart);
      if (searchCell == null)
         throw new JCAException("Could not find label \"PERSONNEL\"");
      
      row = searchCell.getRow() + 1;
      
      searchCell = sheet.findCell(InsideStart);
      if (searchCell == null)
         throw new JCAException("Could not find label \"INSIDE CHGS\"");
         
      endRow = searchCell.getRow();
      
      for (; row < endRow; row++)
         if (sheet.getCell(col, row).getContents().compareTo("") != 0)
            continuation = false;
      
      row = endRow + 1;
      
      searchCell = sheet.findCell(ReimbursableStart);
      
      if (searchCell == null)
         throw new JCAException("Could not find label \"REIMB CHGS\"");
         
      endRow = searchCell.getRow();
      
      for (; row < endRow; row++)
         if (sheet.getCell(col, row).getContents().compareTo("") != 0)
            continuation = false;
      
      row = endRow + 1;
      
      searchCell = sheet.findCell(ReimbursableEnd);
      
      if (searchCell == null)
         throw new JCAException("Could not find label \"WEEK ENDING DATE:\"");
         
      endRow = searchCell.getRow();
      
      for (; row < endRow; row++)
         if (sheet.getCell(col, row).getContents().compareTo("") != 0)
            continuation = false;
            
      if (workbook.getNumberOfSheets() == 1)
         continuation = false;
   
      close();
            
      return continuation;
   }
   
   private int lastColumn() throws JCAException, IOException, BiffException {
      int col;
      int row;
      Cell searchCell;
      
      open();
      
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
            
      close();
      return col - 1;
   }
   
   private Double rowSubtotal(int row) throws JCAException, IOException, BiffException {
      Double subTotal;
      Double hours;
      Double rate;
      Cell cell;
      int endCol;
      ScriptEngine evaluator;
      String evaluation;
      
      open();
      try {
         evaluator = (new ScriptEngineManager()).getEngineByName("JavaScript");
         endCol = lastColumn();
         subTotal = new Double(0);
         cell = sheet.getCell(1, row);
         try {
            rate = new Double(sheet.getCell(1, row).getContents());
            
            for (int col = StartOfDataColumn; col <= endCol 
               && col < sheet.getColumns(); col++) {
               cell = sheet.getCell(col, row);
               if (cell.getContents().compareTo("") != 0) {
                  if (cell.getType().equals(CellType.NUMBER_FORMULA) 
                     && new Double(cell.getContents()) == 4) {
                     try {
                        evaluation = evaluator.eval(((FormulaCell)cell).getFormula()).toString();
                        subTotal += new Double(evaluation) * rate;
                     }
                     catch(Exception ex) {
                        throw new JCAException("There is a bad formula in row " + (row + 1) 
                           + " of " + file.getName());
                     }
                  }
                  else
                     subTotal += new Double(cell.getContents()) * rate;
               } 
            }
         }
         catch (NumberFormatException ex) {
            subTotal = 0.;
         }
      }
      catch (NullPointerException ex) {
         throw new JCAException(ex.getMessage());
      }
      
      close();
      return subTotal;
   }
   
   private Double rowSum(int row) throws JCAException, IOException, BiffException {
      Double sum;
      Cell cell;
      int endCol;
      ScriptEngine evaluator;
      String evaluation;
      
      sum = 0.0;
      open();
      
      endCol = lastColumn();
      evaluator = (new ScriptEngineManager()).getEngineByName("JavaScript");
      
      for (int col = StartOfDataColumn; col <= endCol 
         && col < sheet.getColumns(); col++) {
         cell = sheet.getCell(col, row);
         if (cell.getContents().compareTo("") != 0) {
            if (cell.getType().equals(CellType.NUMBER_FORMULA) 
               && new Double(cell.getContents()) == 4) {
               try {
                  evaluation = evaluator.eval(((FormulaCell)cell).getFormula()).toString();
                  sum += new Double(evaluation);
               }
               catch(Exception ex) {
                  throw new JCAException("There is a bad formula in row " + (row + 1) 
                     + " of " + file.getName());
               }
            }
            else
               sum += new Double(cell.getContents());
         }
      }
      
      close();
      return sum;
   }
   
   private void outputJCA() throws JCAException, IOException, BiffException {
      String rowStr;
      String cellStr;
      open();
      
      for (int row = 0; row < sheet.getRows(); row++) {
         rowStr = "";
         for (int col = 0; col < sheet.getColumns() && col <= lastColumn(); col++) {
            /*if (sheet.getCell(col, row).getType().equals(CellType.NUMBER_FORMULA)) {
               try {
                  cellStr = ((FormulaCell)sheet.getCell(col, row)).getFormula();
               }
               catch (Exception ex) {
                  cellStr = "Formula";
               }
            }
            else*/
               cellStr = sheet.getCell(col, row).getContents();
            if (cellStr.length() > 10)
               cellStr = cellStr.substring(0, 10);
            
            for (int chars = cellStr.length(); chars < 14; chars++) {
               cellStr += " ";
            }
            rowStr += cellStr;
            
         }
         //output(rowStr);
      }
   }
}
