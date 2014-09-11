package mpeg_bookkeeper.weekly_recap;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

import mpeg_bookkeeper.GuiTab;
import mpeg_bookkeeper.SelectionPanel;
import mpeg_bookkeeper.SubProcess;
import mpeg_bookkeeper.TextPanel;

/**
 * A GuiTab meeting with the necessary input fields to launch a Weekly Recap.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class WeeklyRecapGui extends GuiTab {
   
   private SelectionPanel timeSheetsFolderSelect;
   private SelectionPanel recapsFolderSelect;
   private TextPanel datePanel;
   
   private String timeSheetsFolderSetting;
   private String recapsFolderSetting;
   
   
   public WeeklyRecapGui() {
   	gui = this;
      timeSheetsFolderSetting = mpeg_bookkeeper.MPEGBookkeeper.settings.getTimeSheetsFolder();
      recapsFolderSetting = mpeg_bookkeeper.MPEGBookkeeper.settings.getRecapsFolder();
      
      timeSheetsFolderSelect = new SelectionPanel(timeSheetsFolderSetting,
      	"Choose TimeSheets Folder");
      timeSheetsFolderSelect.foldersOnly();
      
      recapsFolderSelect = new SelectionPanel(recapsFolderSetting,
      	"Choose Recaps Folder");
      recapsFolderSelect.foldersOnly();
      
      datePanel = new TextPanel("Week Ending Date:", lastSunday);
      
      selectionPanels.add(timeSheetsFolderSelect);
      selectionPanels.add(recapsFolderSelect);
      
      JTextArea filler = new JTextArea();
      filler.setBackground(bgColor);
      filler.setEditable(false);
      textBoxPanels.add(datePanel);
   }
   
   public void runProgram() {
      try {
         if (running.compareAndSet(false, true)) {
            process = new WeeklyRecap((WeeklyRecapGui)gui,
               timeSheetsFolderSelect.getSelection(),
               recapsFolderSelect.getSelection(),
               lastSunday);
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
      	mpeg_bookkeeper.MPEGBookkeeper.settings.getTimeSheetsFolder());
      recapsFolderSelect.applySetting(
      	mpeg_bookkeeper.MPEGBookkeeper.settings.getRecapsFolder());
   }
}
