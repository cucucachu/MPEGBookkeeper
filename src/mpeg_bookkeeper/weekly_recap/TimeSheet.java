package mpeg_bookkeeper.weekly_recap;

import jxl.*;
import java.io.File;
import java.io.IOException;
import jxl.read.biff.BiffException;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Point;

/**
 * Reads and stores information from a time sheet.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class TimeSheet {
  
   private final Point ClassCodeLocation = new Point(8, 2);
  
   private static final String NameColumnLabel = "Project Name";
   private static final String JobNoColumnLabel = "Project No.";
   private static final String InitialsColumnLabel = "Initials";
   private static final String InitialVColumnLabel = "Initial-V";
   private static final String HoursColumnLabel = "Total";
   private static final String PrevWageColumnLabel = "P3=FDT";
   
   
   //private static final String VehicleStartLabel = "OTHER CHARGES - PLEASE INDICATE TYPE"
      //+ " -Inclinometer I/2 or Full Day (Incl 1/2 or F), Water Level (WL), Laser Level(LL),"
      //+ " Sampler 1/2 or Full Day (S 1/2 or F)";
   private static final String PrevWageVColumnLabel = "\"P\" ?";
   private static final String FDTColumnLabel = "FDT";
   private static final String OtherColumnLabel = "OTHER";
   private static final String TypeColumnLabel = "TYPE";
   private static final String TerminatorOne = "Total Chargeable Hours";
   private static final String TerminatorTwo = "Total for Expense Report";
   
   private static final String OTFlag = "possible o/t";

   private String employee;
   private String initials;
   private ArrayList<Job> jobs;
   // private ArrayList<Comment> comments;
   private Workbook wb;
   private Sheet timeSheet;
   private String fileName;
   
   public TimeSheet(String filePath) 
      throws IOException, BiffException, TimeSheetFormatException {
      jobs = new ArrayList<Job>();
      // comments = new ArrayList<Comment>();
      fileName = filePath;
      wb = Workbook.getWorkbook(new File(filePath));
      timeSheet = wb.getSheet(0);
      
      setJobs();
   }
   
   private void setJobs() throws TimeSheetFormatException {
      int nameCol;
      int jobNoCol;
      int initialsCol;
      int classCodeCol;
      int hoursCol;
      int milesCol;
      int fdtCol;
      int otherCol;
      int typeCol;
      int prevWageCol;
      int jobRow;
      int dateRow;
      int curRow;
      double dayHours;
      double overTime;
      
      Cell nameCell;
      Cell jobCell;
      Cell initialsCell;
      Cell classCodeCell;
      Cell hoursCell;
      Cell milesCell;
      Cell fdtCell;
      Cell otherCell;
      Cell typeCell;
      Cell prevWageCell;
      Cell dayHoursCell;
      Cell curDateCell;
      
      String jobName;
      String jobNoStr;
      String initials; 
      String classCode; 
      String hours;
      String miles;
      String fdt;
      String other;
      String type;
      String prevWage;
      String dayHoursStr;

      String commentText;
      Comment comment;
      ArrayList<Comment> comments;
      
      classCodeCell = timeSheet.getCell((int)ClassCodeLocation.getX(),
         (int)ClassCodeLocation.getY());
      if (classCodeCell.getType().equals(CellType.EMPTY))
         throw new TimeSheetFormatException("Could not find employees class code.");
      classCode = classCodeCell.getContents();
      
      nameCell = timeSheet.findCell(NameColumnLabel);
      jobCell = timeSheet.findCell(JobNoColumnLabel);
      initialsCell = timeSheet.findCell(InitialsColumnLabel);
      hoursCell = timeSheet.findCell(HoursColumnLabel);
      prevWageCell = timeSheet.findCell(PrevWageColumnLabel);
      //curRow = 0;
      
      if (jobCell == null) {
         throw new TimeSheetFormatException("Could not find Job No. Column.");
      }
      else if (hoursCell == null) {
         throw new TimeSheetFormatException("Could not find the hours column.");
      }
      else if (nameCell == null) {
         throw new TimeSheetFormatException("Could not find the job name column.");
      }
      else if (initialsCell == null) {
         throw new TimeSheetFormatException("Could not find the employee initals column.");
      }
      else if (prevWageCell == null) {
         throw new TimeSheetFormatException("Could not find the prevailing wage column.");
      }
      else if (timeSheet.findCell(TerminatorOne) == null) {
         throw new TimeSheetFormatException("Could not find the billable hours "
            + "terminator " + TerminatorOne + "."); 
      }
      else {
      
         nameCol = nameCell.getColumn();
         jobNoCol = jobCell.getColumn();
         initialsCol = initialsCell.getColumn();
         hoursCol = hoursCell.getColumn();
         prevWageCol = prevWageCell.getColumn();
         jobRow = jobCell.getRow();
         dateRow = jobRow + 1;
         curRow = jobRow + 2;
         
         nameCell = timeSheet.getCell(nameCol, curRow);
         jobCell = timeSheet.getCell(jobNoCol, curRow);
         initialsCell = timeSheet.getCell(initialsCol, curRow);
         hoursCell = timeSheet.getCell(hoursCol, curRow);
         prevWageCell = timeSheet.getCell(prevWageCol, curRow);
         
         //Loop over Hours Rows, to get hours per job. Stop Loop once we reach Total Chargeable Hours row.        
         while (timeSheet.getCell(0, curRow).getContents().compareTo(TerminatorOne) != 0) {
            if (hoursCell.getType().equals(CellType.NUMBER_FORMULA) == false)
               throw new TimeSheetFormatException("Missing formula in first totals cell.");
            else {
               jobName = null;
               jobNoStr = null;
               initials = null;
               hours = null;
               miles = null;
               fdt = null;
               other = null;
               type = null;
               prevWage = null;
               overTime = 0;
               comments = new ArrayList<Comment>();
               
               jobName = nameCell.getContents();
               jobNoStr = jobCell.getContents();
               initials = initialsCell.getContents();
               if (initials != null)
                  this.initials = initials;
               hours = hoursCell.getContents();
               prevWage = prevWageCell.getContents();
               
               // Loop over each day in the row to get hours, OT, and comments.
               for (int dayCol = hoursCol + 1; dayCol < hoursCol + 15; dayCol++) {
                  dayHoursCell = timeSheet.getCell(dayCol, curRow);
                  curDateCell = timeSheet.getCell(dayCol, dateRow);
                  if (dayHoursCell.getType() != CellType.EMPTY) {
                     dayHoursStr = dayHoursCell.getContents();
                     commentText = getCellComment(dayHoursCell);

                    
                     if (commentText != null){

                        comment = new Comment(this.initials, curDateCell.getContents(), commentText);
                        comments.add(comment);
                     }

                        
                     try {
                        if (dayHoursStr.trim().compareTo("") == 0)
                           dayHours = 0;
                        else
                           dayHours = Double.parseDouble(dayHoursStr);
                        
                        if (dayHours > 8) {
                           overTime += dayHours - 8;
                           other = OTFlag;
                        }
                     }
                     catch (Exception ex) {
                        throw new TimeSheetFormatException("Found a non-number in "
                           + "a single day's hours column. " + ex);
                     }
                  }
               }

               
               try {
                  if (Double.parseDouble(hoursCell.getContents()) != 0)
                     jobs.add(new Job(jobName, jobNoStr, initials, classCode, hours,
                        miles, fdt, other, type, prevWage, comments));
                  
                  curRow++;
                  nameCell = timeSheet.getCell(nameCol, curRow);
                  jobCell = timeSheet.getCell(jobNoCol, curRow);
                  initialsCell = timeSheet.getCell(initialsCol, curRow);
                  hoursCell = timeSheet.getCell(hoursCol, curRow);
                  prevWageCell = timeSheet.getCell(prevWageCol, curRow);
                  curDateCell = null;               
               }
               catch (NumberFormatException ex) {
                  throw new TimeSheetFormatException("An entry in the Row " + (curRow + 1)
                     + " is missing a formula.");
               }
            }
         }
      }
      
      nameCell = timeSheet.findCell(NameColumnLabel, 0, curRow, 10,
         curRow + 10, false);
      jobCell = timeSheet.findCell(JobNoColumnLabel, 0, curRow, 10,
         curRow + 10, false);
      initialsCell = timeSheet.findCell(InitialVColumnLabel, 0, curRow, 10,
         curRow + 10, false);
      prevWageCell = timeSheet.findCell(PrevWageVColumnLabel, 0, curRow, 10,
         curRow + 10, false);
      hoursCell = timeSheet.findCell("H", 0, curRow, 10, curRow + 10, false);
      milesCell = timeSheet.findCell("M", 0, curRow, 10, curRow + 10, false);
      fdtCell = timeSheet.findCell(FDTColumnLabel, 0, curRow, 25,
         curRow + 10, false);
      otherCell = timeSheet.findCell(OtherColumnLabel, 0, curRow, 25,
         curRow + 10, false);
      typeCell = timeSheet.findCell(TypeColumnLabel, 0, curRow, 25,
         curRow + 10, false);
      
      if (jobCell == null) {
         throw new TimeSheetFormatException("Could not find other charges second Job No. Column.");
      }
      else if (hoursCell == null) {
         throw new TimeSheetFormatException("Could not find the other charges hours column.");
      }
      else if (nameCell == null) {
         throw new TimeSheetFormatException("Could not find the other charges job name column.");
      }
      else if (initialsCell == null) {
         throw new TimeSheetFormatException("Could not find the other charges employee initals column.");
      }
      else if (milesCell == null) {
         throw new TimeSheetFormatException("Could not find the other charges miles column.");
      }
      else if (fdtCell == null) {
         throw new TimeSheetFormatException("Could not find the other charges fdt column.");
      }
      else if (otherCell == null) {
         throw new TimeSheetFormatException("Could not find the other charges OTHER column.");
      }
      else if (typeCell == null) {
         throw new TimeSheetFormatException("Could not find the other charges Type column.");
      }
      else if (prevWageCell == null) {
         throw new TimeSheetFormatException("Could not find the other charges prevailing wage column.");
      }
      else if (timeSheet.findCell(TerminatorTwo) == null) {
         throw new TimeSheetFormatException("Could not find the other charges "
            + "terminator " + TerminatorTwo + "."); 
      }
      else {
         nameCol = nameCell.getColumn();
         jobNoCol = jobCell.getColumn();
         initialsCol = initialsCell.getColumn();
         prevWageCol = prevWageCell.getColumn();
         hoursCol = hoursCell.getColumn();
         milesCol = milesCell.getColumn();
         fdtCol = fdtCell.getColumn();
         otherCol = otherCell.getColumn();
         typeCol = typeCell.getColumn();
         curRow = jobCell.getRow() + 1;
         
         nameCell = timeSheet.getCell(nameCol, curRow);
         jobCell = timeSheet.getCell(jobNoCol, curRow);
         hoursCell = timeSheet.getCell(hoursCol, curRow);
         initialsCell = timeSheet.getCell(initialsCol, curRow);
         prevWageCell = timeSheet.getCell(prevWageCol, curRow);
         milesCell = timeSheet.getCell(milesCol, curRow);
         fdtCell = timeSheet.getCell(fdtCol, curRow);
         otherCell = timeSheet.getCell(otherCol, curRow);
         typeCell = timeSheet.getCell(typeCol, curRow);
         
         while (timeSheet.getCell(0, curRow).getContents().compareTo(TerminatorTwo) != 0) {
            
            if (hoursCell.getType().equals(CellType.NUMBER_FORMULA) == false)
               throw new TimeSheetFormatException("Missing formula in other charges hours cell");
            else if (milesCell.getType().equals(CellType.NUMBER_FORMULA) == false)
               throw new TimeSheetFormatException("Missing formula in other charges miles cell");
            else {
               jobName = null;
               jobNoStr = null;
               initials = null;
               hours = null;
               miles = null;
               fdt = null;
               other = null;
               type = null;
               prevWage = null;
               
               jobName = nameCell.getContents().trim();
               jobNoStr = jobCell.getContents().trim();
               initials = initialsCell.getContents().trim();
               hours = hoursCell.getContents();
               miles = milesCell.getContents();
               fdt = fdtCell.getContents().trim();
               other = otherCell.getContents().trim();
               type = typeCell.getContents().trim();
               prevWage = prevWageCell.getContents().trim();
               
               try {
               	if (fdt.compareTo("") != 0)
							new Double(fdt);
					}
					catch (NumberFormatException ex) {
						throw new TimeSheetFormatException("Found a non-number in the fdt column, row " + (curRow + 1));
					}
               
               if (Double.parseDouble(hoursCell.getContents()) != 0
                  || Double.parseDouble(milesCell.getContents()) != 0
                  || fdtCell.getContents().compareTo("") != 0
                  || otherCell.getContents().compareTo("") != 0
                  || typeCell.getContents().compareTo("") != 0 ) {
                  jobs.add(new Job(jobName, jobNoStr, initials, classCode, hours,
                     miles, fdt, other, type, prevWage, null));
               }
               
               curRow++;
               nameCell = timeSheet.getCell(nameCol, curRow);
               jobCell = timeSheet.getCell(jobNoCol, curRow);
               prevWageCell = timeSheet.getCell(prevWageCol, curRow);
               hoursCell = timeSheet.getCell(hoursCol, curRow);
               initialsCell = timeSheet.getCell(initialsCol, curRow);
               milesCell = timeSheet.getCell(milesCol, curRow);
               fdtCell = timeSheet.getCell(fdtCol, curRow);
               otherCell = timeSheet.getCell(otherCol, curRow);
               typeCell = timeSheet.getCell(typeCol, curRow);
            }
         }
      }

   }
   
   private String getCellComment(Cell cell) {
      String comment = null;
      CellFeatures features;
      String col;
      int row;
      
      if (cell != null) {
         col = CellReferenceHelper.getColumnReference(cell.getColumn());
         row = cell.getRow();
         features = cell.getCellFeatures();
         
         if (features != null)
            comment = features.getComment();
         
         // if (comment != null)
         //    comment = this.initials + " Cell: " + col + row + ": \"" + comment + "\"";
      }
      
      return comment;
   }
   
   public ArrayList<Job> getJobs() {
      return jobs;
   }
   
   // public ArrayList<Comment> getComments() {
   //    return comments;
   // }
   
   void printJobs() {
      for (Job job : jobs)
         System.out.println(job);
   }
   
   void close() {
      wb.close();
   }
}
