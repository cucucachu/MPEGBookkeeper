package mpeg_bookkeeper.pm_workbook_compiler;

import jxl.*;
import jxl.write.*;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Alignment;
import java.io.File;
import java.io.IOException;
import jxl.read.biff.BiffException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Calendar;

import mpeg_bookkeeper.jca_builder.JCAException;

/**
 * Provides formatting information for JCAs to correctly format
 * coppied JCAs in a PMWorkbook.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class JCAFormat {
      
   private static final String FormatCheckString = "JCA Upload Format";
   private static final String DelimeterOne = "PERSONNEL";
   private static final String DelimeterTwo = "INSIDE CHGS";
   private static final String DelimeterThree = "REIMB CHGS";
   private static final String DelimeterFour = "WEEK ENDING DATE:";
   
   //Only to be used on sheets were badFormat(sheet) returns false.
   public static void formatJCA(WritableSheet sheet)
      throws WriteException, JCAException {
      int curRow;
      int curCol;
      WritableCell cell;
      int rowLimit;
      int colLimit;
      int[] delimeters;
      
      WritableCellFormat format;
      ArrayList<WritableCellFormat> formats;
      formats = new ArrayList<WritableCellFormat>();
      
      WritableFont[] fonts;
      NumberFormat[] numberFormats;
      boolean formatFound;
      
      
      rowLimit = sheet.getRows();
      
      try {
      	colLimit = lastColumn(sheet) + 1;
   	}
   	catch (Exception ex) {
   		throw new JCAException(ex.toString());
   	}
   	
   	
      delimeters = new int[4];
      delimeters[0] = sheet.findCell(DelimeterOne).getRow();
      delimeters[1] = sheet.findCell(DelimeterTwo).getRow();
      delimeters[2] = sheet.findCell(DelimeterThree).getRow();
      delimeters[3] = sheet.findCell(DelimeterFour).getRow();
      
      fonts = new WritableFont[3];
      fonts[0] = black8Pt();
      fonts[1] = black10Pt();
      fonts[2] = blue8Pt();
      
      numberFormats = new NumberFormat[3];
      numberFormats[0] = wholeNumber();
      numberFormats[1] = oneDecimalPlace();
      numberFormats[2] = twoDecimalPlaces();
      
      for (curRow = 0; curRow < rowLimit; curRow++) {
         for (curCol = 0; curCol < colLimit; curCol++) {
            cell = sheet.getWritableCell(curCol, curRow);
            format = getFormatForCell(curCol, curRow, delimeters,
               fonts, numberFormats);
            
            formatFound = false;
            if (format != null) {
               for (WritableCellFormat f : formats) {
                  if (f.equals(format)) {
                     formatFound = true;
                     cell.setCellFormat(f);
                  }
               }
               if (!formatFound) {
                  cell.setCellFormat(format);
                  formats.add(format);
               }
            }
         }
      }
      
      sheet.setColumnView(0, 17);
   }
   
   public static WritableCellFormat getFormatForCell(int col, int row, int[] delimeters,
      WritableFont[] fonts, NumberFormat[] numberFormats) 
      throws WriteException {
      WritableCellFormat format;
      format = null;
      
      WritableFont black8Pt;
      WritableFont black10Pt;
      WritableFont blue8Pt;
      
      NumberFormat wholeNumber;
      NumberFormat oneDecimalPlace;
      NumberFormat twoDecimalPlaces;
      
      if (fonts.length < 3 || numberFormats.length < 3)
         return null;
      
      black8Pt = fonts[0];
      black10Pt = fonts[1];
      blue8Pt = fonts[2];
      
      wholeNumber = numberFormats[0];
      oneDecimalPlace = numberFormats[1];
      twoDecimalPlaces = numberFormats[2];
      
      if (row == delimeters[0] && col == 0)
         format = new WritableCellFormat(black10Pt);
      else if (row > delimeters[0] && row < delimeters[1]) {
         if (col == 0)
            format = new WritableCellFormat(blue8Pt);
         else if (col == 1)
            format = new WritableCellFormat(blue8Pt, wholeNumber);
         else if (col == 2)
            format = new WritableCellFormat(black8Pt, oneDecimalPlace);
         else if (col == 3)
            format = new WritableCellFormat(black8Pt, twoDecimalPlaces);
         else
            format = new WritableCellFormat(blue8Pt, oneDecimalPlace);
      }
      else if (row > delimeters[1] && row < delimeters[2]) {
         if (col == 0)
            format = new WritableCellFormat(blue8Pt);
         else if (col == 1)
            format = new WritableCellFormat(blue8Pt, twoDecimalPlaces);
         else if (col == 2)
            format = new WritableCellFormat(black8Pt, oneDecimalPlace);
         else if (col == 3)
            format = new WritableCellFormat(black8Pt, twoDecimalPlaces);
         else
            format = new WritableCellFormat(blue8Pt, oneDecimalPlace);
      }
      else if (row > delimeters[2] && row < delimeters[3]) {
         if (col == 0)
            format = new WritableCellFormat(blue8Pt);
         else if (col == 1)
            format = new WritableCellFormat(blue8Pt, twoDecimalPlaces);
         else if (col == 2)
            format = new WritableCellFormat(black8Pt, oneDecimalPlace);
         else if (col == 3)
            format = new WritableCellFormat(black8Pt, twoDecimalPlaces);
         else
            format = new WritableCellFormat(blue8Pt, twoDecimalPlaces);
      }
      else if (row > delimeters[3] && row < delimeters[3] + 10) {
         if (col == 3)
            format = new WritableCellFormat(black8Pt, twoDecimalPlaces);
         else if (col > 3)
            format = new WritableCellFormat(blue8Pt, twoDecimalPlaces);
      }
      else if (row >= delimeters[3] + 10) {
         if (col == 1 || col == 3)
            format = new WritableCellFormat(blue8Pt);
      }
      
      if (format == null)
         format = new WritableCellFormat(black8Pt);
      
      if (topBorder(col, row, delimeters)) {
         format.setBorder(Border.TOP, BorderLineStyle.THIN);
      }
      if (bottomBorder(col, row, delimeters)) {
         format.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
      }
      if (leftBorder(col, row, delimeters)) {
         format.setBorder(Border.LEFT, BorderLineStyle.THIN);
      }
      if (rightBorder(col, row, delimeters)) {
         format.setBorder(Border.RIGHT, BorderLineStyle.THIN);
      }
      
      if (leftJustify(col, row, delimeters))
         format.setAlignment(Alignment.LEFT);
      if (rightJustify(col, row, delimeters))
         format.setAlignment(Alignment.RIGHT);
      if (centerJustify(col, row, delimeters))
         format.setAlignment(Alignment.CENTRE);
      
      return format;
   }
   
   private static WritableFont black8Pt() throws WriteException {
      WritableFont font;
      
      font = new WritableFont(WritableFont.ARIAL);
      font.setColour(jxl.format.Colour.BLACK);
      font.setPointSize(8);
      return font;
   }
   
   private static WritableFont black10Pt() throws WriteException {
      WritableFont font; 
      
      font = new WritableFont(WritableFont.ARIAL);
      font.setColour(jxl.format.Colour.BLACK);
      font.setPointSize(10);
      
      return font;
   }
   
   private static WritableFont blue8Pt() throws WriteException {
      WritableFont font;
     
      font = new WritableFont(WritableFont.ARIAL);
      font.setColour(jxl.format.Colour.BLUE);
      font.setPointSize(8);
      
      return font;
   }
   
   private static NumberFormat wholeNumber() {
      return new NumberFormat("0.###");
   }
   
   private static NumberFormat oneDecimalPlace() {
      return new NumberFormat("0.0");
   }
   
   private static NumberFormat twoDecimalPlaces() {
      return new NumberFormat("0.00");
   }
   
   private static boolean topBorder(int col, int row, int[] delimeters) {
      boolean border = false;
      
      if (row == delimeters[0] - 1)
         border = true;
      
      if (row == delimeters[0] || row == delimeters[1] 
         || row == delimeters[2] || row == delimeters[3])
         border = true;
      
      if (row == delimeters[3] + 1 || row == delimeters[3] + 5
         || row == delimeters[3] + 10)
         border = true;
         
      return border;
   }
   
   private static boolean bottomBorder(int col, int row, int[] delimeters) {
      boolean border = false;
      
      if (row == delimeters[3] + 9 || row >= delimeters[3] + 12 && col < 4)
         border = true;
         
      return border;
   }
   
   private static boolean leftBorder(int col, int row, int[] delimeters) {
      boolean border = false;
      
      if (row < delimeters[3] + 10)
         if (col == 2 || col > 3)
            border = true;
         
      return border;
   }
   
   private static boolean rightBorder(int col, int row, int[] delimeters) {
      boolean border = false;
      
      if (row < delimeters[3] + 10) {
         if (col == 1 || col >= 3)
            border = true;
      }
      else {
         if (col == 3)
            border = true;
      }
         
      return border;
   }
   
   private static boolean leftJustify(int col, int row, int[] delimeters) {
      boolean justify = false;
      
      if (row > delimeters[0] && row < delimeters[1] && col == 1)
         justify = true;
         
      return justify;
   }
   
   private static boolean centerJustify(int col, int row, int[] delimeters) {
      boolean justify = false;
      
      if (row == delimeters[0] || row == delimeters[1] || row == delimeters[2])
         if (col == 3)
            justify = true;
      
      if (row == delimeters[1] && col == 2)
         justify = true;
         
      if (row > delimeters[3] + 5 && row < delimeters[3] + 10)
         if (col == 2)
            justify = true;
         
      return justify;
   }
   
   private static boolean rightJustify(int col, int row, int[] delimeters) {
      boolean justify = false;
      
      if (row == delimeters[0] || row == delimeters[1]
         || row == delimeters[2] || row == delimeters[3])
         if (col > 3)
            justify = true;
      
      if (row == 0 && col == 2)
         justify = true;
         
      if (row == 1 && col > 1)
         justify = true;
         
      if (row >= delimeters[3] + 10 && row < delimeters[3] + 13
         && col == 0)
         justify = true;
         
      if (row > delimeters[3] + 10 && row < delimeters[3] + 13
         && col == 2)
         justify = true;
         
      return justify;
   }
   
   public static boolean badFormat(String path) {
      Workbook wb;
      Sheet sheet;
      Cell searchCell;
      java.lang.Boolean bad;
      
      bad = false;
      
      wb = null;
      
      try {
         wb = Workbook.getWorkbook(new File(path));
         sheet = wb.getSheet(0);
         
         if (sheet == null)
            bad = true;
         if (sheet.findCell(FormatCheckString) == null)
            bad = true;
         if (sheet.findCell(DelimeterOne) == null)
            bad = true;
         if (sheet.findCell(DelimeterTwo) == null)
            bad = true;
         if (sheet.findCell(DelimeterThree) == null)
            bad = true;
         if (sheet.findCell(DelimeterFour) == null)
            bad = true;
            
      }
      catch (Exception ex) {
         bad = true;
      }
      
      if (wb != null)
         wb.close();
      
      return bad;
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
      col = 4;
      
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
