package mpeg_bookkeeper.jca_builder;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

import mpeg_bookkeeper.GuiTab;
import mpeg_bookkeeper.SelectionPanel;
import mpeg_bookkeeper.TextPanel;

/**
 * User Interface to get input for, launch, and control a JCABuilder thread.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class JCAGui extends GuiTab {

   private SelectionPanel jcaSelector;
   private SelectionPanel recapSelector;
   private TextPanel datePanel;
   
   private String jcaFolderSetting;
   private String recapFolderSetting;
   
   public JCAGui() {
      jcaFolderSetting = mpeg_bookkeeper.MPEGBookkeeper.settings.getJCAFolder();
      recapFolderSetting = mpeg_bookkeeper.MPEGBookkeeper.settings.getRecapsFolder();
      
      jcaSelector = new SelectionPanel(jcaFolderSetting, "Choose JCA Folder");
      jcaSelector.foldersOnly();
      
      recapSelector = new SelectionPanel(recapFolderSetting, "Choose Recap File");
      recapSelector.filesOnly();
      
      datePanel = new TextPanel("Week Ending Date:", lastSunday);
      
      selectionPanels.add(jcaSelector);
      selectionPanels.add(recapSelector);
      
      textBoxPanels.add(datePanel);
   }
   
   public void runProgram() {
   	try {
		   if (running.compareAndSet(false, true)) {
		   	resetLog();
		      process = new JCABuilder((JCAGui)gui, jcaSelector.getSelection(),
		               recapSelector.getSelection(), datePanel.getText());
		               
		      Thread jcaBuilderThread = new Thread(process);
		      
		      jcaBuilderThread.start();
		   }
		}
		catch (Exception ex) {
			output("JCABuilder failed");
			output("   " + ex);
			running.set(false);
		}
   }
   
   public void applySettings() {
   	jcaSelector.applySetting(
   		mpeg_bookkeeper.MPEGBookkeeper.settings.getJCAFolder());
   	recapSelector.applySetting(
   		mpeg_bookkeeper.MPEGBookkeeper.settings.getRecapsFolder());
   }
}
