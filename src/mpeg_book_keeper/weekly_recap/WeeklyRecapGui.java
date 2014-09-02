package mpeg_book_keeper.weekly_recap;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

import mpeg_book_keeper.GuiTab;
import mpeg_book_keeper.SelectionPanel;
import mpeg_book_keeper.SubProcess;

public class WeeklyRecapGui extends GuiTab {
   
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
      	"Choose Recaps Folder");
      recapsFolderSelect.foldersOnly();
      
      selectionPanels.add(timeSheetsFolderSelect);
      selectionPanels.add(recapsFolderSelect);
      
      JTextArea filler = new JTextArea();
      filler.setBackground(bgColor);
      filler.setEditable(false);
      textBoxPanels.add(filler);
   }
   
   public void runProgram() {
      try {
         if (running.compareAndSet(false, true)) {
            process = new WeeklyRecap((WeeklyRecapGui)gui,
               timeSheetsFolderSelect.getSelection(),
               recapsFolderSelect.getSelection());
            Thread recapThread = new Thread(process);
            
            recapThread.start();
         }
      }
      catch (Exception ex) {
         output("Please select the timesheet folder first.");
         running.set(false);
      }
   }
   
   public void applySettings() {
      timeSheetsFolderSelect.applySetting(
      	mpeg_book_keeper.MPEGBookKeeper.settings.getTimeSheetsFolder());
      recapsFolderSelect.applySetting(
      	mpeg_book_keeper.MPEGBookKeeper.settings.getRecapsFolder());
   }
}
