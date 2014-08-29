package mpeg_book_keeper.weekly_recap;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
import mpeg_book_keeper.GuiTab;
import mpeg_book_keeper.SelectionPanel;

public class WeeklyRecapGui extends GuiTab {
   
   private WeeklyRecap weeklyRecap;
   private SelectionPanel timeSheetsFolderSelect;
   private SelectionPanel recapsFolderSelect;
   private String timeSheetsFolderSetting;
   private String recapsFolderSetting;
   
   
   public WeeklyRecapGui() {
   	gui = this;
      timeSheetsFolderSetting = mpeg_book_keeper.MPEGBookKeeper.settings.getTimeSheetsFolder();
      recapsFolderSetting = mpeg_book_keeper.MPEGBookKeeper.settings.getRecapsFolder();
      
      timeSheetsFolderSelect = new SelectionPanel(timeSheetsFolderSetting,
      	"Choose TimeSheets Folder");
      timeSheetsFolderSelect.foldersOnly();
      
      recapsFolderSelect = new SelectionPanel(recapsFolderSetting,
      	"Choose TimeSheets Folder");
      recapsFolderSelect.foldersOnly();
      
      selectionPanels.add(timeSheetsFolderSelect);
      selectionPanels.add(recapsFolderSelect);
   }
   
   public void runProgram() {
      try {
         if (running == false) {
            running = true;
            Thread recapThread = new Thread(new Runnable() {
               public void run() {
                  WeeklyRecap.recapWeek((WeeklyRecapGui)gui,
                  	timeSheetsFolderSelect.getSelection(),
                  	recapsFolderSelect.getSelection());
               }
            });
            
            recapThread.start();
         }
      }
      catch (Exception ex) {
         output("Please select the timesheet folder first.");
      }
   }
}
