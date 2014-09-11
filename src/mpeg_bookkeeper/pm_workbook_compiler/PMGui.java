package mpeg_bookkeeper.pm_workbook_compiler;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

import mpeg_bookkeeper.GuiTab;
import mpeg_bookkeeper.SelectionPanel;
import mpeg_bookkeeper.TextPanel;

/**
 * Provides a GuiTab with the input required to launch and control a
 * PMWorkbookCompiler SubProcess.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class PMGui extends GuiTab {
  
	private PMWorkbookCompiler compiler;
   private SelectionPanel jcaSelector;
   private SelectionPanel workbookFolderSelector;
   private TextPanel datePanel;
   
   private String jcaFolderSetting;
   private String workbookFolderSetting;
   
   public PMGui() {
      jcaFolderSetting = mpeg_bookkeeper.MPEGBookkeeper.settings.getJCAFolder();
      workbookFolderSetting = mpeg_bookkeeper.MPEGBookkeeper.settings.getPMWorkbooksFolder();
      
      jcaSelector = new SelectionPanel(jcaFolderSetting, "Choose JCA Folder");
      jcaSelector.foldersOnly();
      
      workbookFolderSelector = new SelectionPanel(workbookFolderSetting, "Choose Workbooks Folder");
      workbookFolderSelector.foldersOnly();
      datePanel = new TextPanel("Week Ending Date:", lastSunday);
      
      selectionPanels.add(jcaSelector);
      selectionPanels.add(workbookFolderSelector);
      
      textBoxPanels.add(datePanel);
   }
   
   public void runProgram() {
   	try {
		   if (running.compareAndSet(false, true)) {
		   	gui.resetLog();
		      process = new PMWorkbookCompiler((PMGui)gui,
            	jcaSelector.getSelection(),
            	workbookFolderSelector.getSelection(),
            	datePanel.getText());
		      Thread pmWorkbookCompilerThread = new Thread(process);
		      
		      pmWorkbookCompilerThread.start();
		   }
		}
		catch (Exception ex) {
			output("PM Compiler failed.");
			output("   " + ex);
		}
   }
   
   public void applySettings() {
   	jcaSelector.applySetting(
   		mpeg_bookkeeper.MPEGBookkeeper.settings.getJCAFolder());
   	workbookFolderSelector.applySetting(
   		mpeg_bookkeeper.MPEGBookkeeper.settings.getPMWorkbooksFolder());
   }
}
