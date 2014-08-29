package mpeg_book_keeper.pm_workbook_compiler;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

import mpeg_book_keeper.GuiTab;
import mpeg_book_keeper.SelectionPanel;
import mpeg_book_keeper.TextPanel;

public class PMGui extends GuiTab {
  
	private PMWorkbookCompiler compiler;
   private SelectionPanel jcaSelector;
   private SelectionPanel workbookFolderSelector;
   private TextPanel datePanel;
   
   private String jcaFolderSetting;
   private String workbookFolderSetting;
   
   public PMGui() {
      jcaFolderSetting = mpeg_book_keeper.MPEGBookKeeper.settings.getJCAFolder();
      workbookFolderSetting = mpeg_book_keeper.MPEGBookKeeper.settings.getPMWorkbooksFolder();
      
      jcaSelector = new SelectionPanel(jcaFolderSetting, "Choose JCA Folder");
      jcaSelector.foldersOnly();
      
      workbookFolderSelector = new SelectionPanel(workbookFolderSetting, "Choose Workbooks Folder");
      workbookFolderSelector.foldersOnly();
      datePanel = new TextPanel("MM/DD/YYYY");
      
      selectionPanels.add(jcaSelector);
      selectionPanels.add(workbookFolderSelector);
      
      textBoxPanels.add(datePanel);
   }
   
   public void runProgram() {
      if (running == false) {
         running = true;
         
         Thread pmWorkbookCompilerThread = new Thread(new Runnable() {
            public void run() {
               PMWorkbookCompiler.compilePMWorkbooks((PMGui)gui,
               	jcaSelector.getSelection(),
               	workbookFolderSelector.getSelection(),
               	datePanel.getText());
            }
         });
         
         pmWorkbookCompilerThread.start();
      }
   }
   
}
