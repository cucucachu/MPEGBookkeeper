package mpeg_book_keeper.quarterly_reporter;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

import mpeg_book_keeper.GuiTab;
import mpeg_book_keeper.SelectionPanel;
import mpeg_book_keeper.TextPanel;

public class QRGui extends GuiTab {
   
   private SelectionPanel jcaSelector;
   private SelectionPanel reportsSelector;
   private SelectionPanel statusSelector;
   private TextPanel datePanel;
   private TextPanel yearPanel;
   
   private String jcaFolderSetting;
   private String reportFolderSetting;
   private String statusFileSetting;
   
   public QRGui() {
      jcaFolderSetting = mpeg_book_keeper.MPEGBookKeeper.settings.getJCAFolder();
      reportFolderSetting = mpeg_book_keeper.MPEGBookKeeper.settings.getQuarterlyReportsFolder();
      statusFileSetting = "~";
            
      jcaSelector = new SelectionPanel(jcaFolderSetting, "Choose JCA Folder");
      jcaSelector.foldersOnly();
      
      reportsSelector = new SelectionPanel(reportFolderSetting, "Choose Reports Folder");
      reportsSelector.foldersOnly();
      
      statusSelector = new SelectionPanel(statusFileSetting, "Choose Status File");
      statusSelector.filesOnly();
      
      datePanel = new TextPanel("MM/DD");
      yearPanel = new TextPanel("YYYY");
      
      selectionPanels.add(jcaSelector);
      selectionPanels.add(reportsSelector);
      selectionPanels.add(statusSelector);
      
      textBoxPanels.add(datePanel);
      textBoxPanels.add(yearPanel);
   }
   
   public void runProgram() {
      if (running.compareAndSet(false, true)) {
         gui.resetLog();
         process = new QuarterlyReporter((QRGui)gui,
         	jcaSelector.getSelection(), reportsSelector.getSelection(),
         	statusSelector.getSelection(), datePanel.getText(), 
            yearPanel.getText());
         Thread QRThread = new Thread(process);
         
         QRThread.start();
      }
   }
}
