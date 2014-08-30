package mpeg_book_keeper.jca_builder;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

import mpeg_book_keeper.GuiTab;
import mpeg_book_keeper.SelectionPanel;
import mpeg_book_keeper.TextPanel;

public class JCAGui extends GuiTab {

   private SelectionPanel jcaSelector;
   private SelectionPanel recapSelector;
   private TextPanel datePanel;
   
   private String jcaFolderSetting;
   private String recapFolderSetting;
   
   public JCAGui() {
      jcaFolderSetting = mpeg_book_keeper.MPEGBookKeeper.settings.getJCAFolder();
      recapFolderSetting = mpeg_book_keeper.MPEGBookKeeper.settings.getRecapsFolder();
      
      jcaSelector = new SelectionPanel(jcaFolderSetting, "Choose JCA Folder");
      jcaSelector.foldersOnly();
      
      recapSelector = new SelectionPanel(recapFolderSetting, "Choose Recap File");
      recapSelector.filesOnly();
      
      datePanel = new TextPanel("Week Ending Date:", "MM/DD/YYYY");
      
      selectionPanels.add(jcaSelector);
      selectionPanels.add(recapSelector);
      
      textBoxPanels.add(datePanel);
   }
   
   public void runProgram() {
   	try {
		   if (running.compareAndSet(false, true)) {
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
}
