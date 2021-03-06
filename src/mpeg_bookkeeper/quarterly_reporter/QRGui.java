package mpeg_bookkeeper.quarterly_reporter;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

import mpeg_bookkeeper.GuiTab;
import mpeg_bookkeeper.SelectionPanel;
import mpeg_bookkeeper.TextPanel;

/**
 * A GuiTab which gets necessary input from the user to launch and 
 * control a new QuarterlyReporter thread.
 *
 * @author Cody Jones
 * @version 1.0
 */
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
      jcaFolderSetting = mpeg_bookkeeper.MPEGBookkeeper.settings.getJCAFolder();
      reportFolderSetting = mpeg_bookkeeper.MPEGBookkeeper.settings.getQuarterlyReportsFolder();
      statusFileSetting = "~";
            
      jcaSelector = new SelectionPanel(jcaFolderSetting, "Choose JCA Folder");
      jcaSelector.foldersOnly();
      
      reportsSelector = new SelectionPanel(reportFolderSetting, "Choose Reports Folder");
      reportsSelector.foldersOnly();
      
      statusSelector = new SelectionPanel(statusFileSetting, "Choose Status File");
      statusSelector.filesOnly();
      
      datePanel = new TextPanel("Valid Thru:      ", "MM/DD");
      yearPanel = new TextPanel("Year:            ", "YYYY");
      
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
   
   public void applySettings() {
   	jcaSelector.applySetting(mpeg_bookkeeper.MPEGBookkeeper.settings.getJCAFolder());
   	reportsSelector.applySetting(mpeg_bookkeeper.MPEGBookkeeper.settings.getQuarterlyReportsFolder());
   }
}
