package mpeg_book_keeper;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

public class SettingsGui extends JFrame implements ActionListener {
	private final int COLS = 55;
	private final int buttonWidth = 220;
	private final int buttonHeight = 25;
	private final int frameWidth = 900;
	private final int frameHeight = 280;
	
	private JPanel mainPanel;
	
   private JPanel mpegBookKeeperPanel;
   private JTextArea mpegBookKeeperText;
   private JButton mpegBookKeeperButton;
   
   private JPanel timeSheetPanel;
   private JTextArea timeSheetText;
   private JButton timeSheetButton;
   
   private JPanel recapPanel;
   private JTextArea recapText;
   private JButton recapButton;
   
   private JPanel jcaPanel;
   private JTextArea jcaText;
   private JButton jcaButton;
   
   private JPanel pmWorkbookPanel;
   private JTextArea pmWorkbookText;
   private JButton pmWorkbookButton;
   
   private JPanel quarterlyReportPanel;
   private JTextArea quarterlyReportText;
   private JButton quarterlyReportButton;
   
   private JButton saveButton;
   
   private Settings settings;
   
   public SettingsGui (Settings settings){
   	super("Settings");
   	
   	
   	Dimension buttonSize;
   	this.settings = settings;
      this.setSize(frameWidth, frameHeight);
      this.setResizable(false);
      
      mainPanel = new JPanel();
      
      buttonSize = new Dimension(buttonWidth, buttonHeight);
      mpegBookKeeperPanel = new JPanel();
      mpegBookKeeperPanel.setSize(100, 100);
      mpegBookKeeperText = new JTextArea(settings.getMPEGBookKeeperFolder());
      mpegBookKeeperText.setEditable(false);
      mpegBookKeeperText.setMargin(new Insets(5,5,5,5));
      mpegBookKeeperText.setColumns(COLS);
      mpegBookKeeperButton = new JButton("MPEGBookKeeper Folder");
      mpegBookKeeperButton.addActionListener(this);
      mpegBookKeeperButton.setPreferredSize(buttonSize);
      mpegBookKeeperPanel.add(mpegBookKeeperText);
      mpegBookKeeperPanel.add(mpegBookKeeperButton);
      
      timeSheetPanel = new JPanel();
      timeSheetPanel.setSize(100, 100);
      timeSheetText = new JTextArea(settings.getTimeSheetsFolder());
      timeSheetText.setEditable(false);
      timeSheetText.setMargin(new Insets(5,5,5,5));
      timeSheetText.setColumns(COLS);
      timeSheetButton = new JButton("TimeSheets Folder");
      timeSheetButton.addActionListener(this);
      timeSheetButton.setPreferredSize(buttonSize);
      timeSheetPanel.add(timeSheetText);
      timeSheetPanel.add(timeSheetButton);
      
      recapPanel = new JPanel();
      recapPanel.setSize(100, 100);
      recapText = new JTextArea(settings.getRecapsFolder());
      recapText.setEditable(false);
      recapText.setMargin(new Insets(5,5,5,5));
      recapText.setColumns(COLS);
      recapButton = new JButton("Recaps Folder");
      recapButton.addActionListener(this);
      recapButton.setPreferredSize(buttonSize);
      recapPanel.add(recapText);
      recapPanel.add(recapButton);
      
      jcaPanel = new JPanel();
      jcaPanel.setSize(100, 100);
      jcaText = new JTextArea(settings.getJCAFolder());
      jcaText.setEditable(false);
      jcaText.setMargin(new Insets(5,5,5,5));
      jcaText.setColumns(COLS);
      jcaButton = new JButton("JCAs Folder");
      jcaButton.addActionListener(this);
      jcaButton.setPreferredSize(buttonSize);
      jcaPanel.add(jcaText);
      jcaPanel.add(jcaButton);
      
      pmWorkbookPanel = new JPanel();
      pmWorkbookPanel.setSize(100, 100);
      pmWorkbookText = new JTextArea(settings.getPMWorkbooksFolder());
      pmWorkbookText.setEditable(false);
      pmWorkbookText.setMargin(new Insets(5,5,5,5));
      pmWorkbookText.setColumns(COLS);
      pmWorkbookButton = new JButton("PM Workbooks Folder");
      pmWorkbookButton.addActionListener(this);
      pmWorkbookButton.setPreferredSize(buttonSize);
      pmWorkbookPanel.add(pmWorkbookText);
      pmWorkbookPanel.add(pmWorkbookButton);
      
      quarterlyReportPanel = new JPanel();
      quarterlyReportPanel.setSize(100, 100);
      quarterlyReportText = new JTextArea(settings.getQuarterlyReportsFolder());
      quarterlyReportText.setEditable(false);
      quarterlyReportText.setMargin(new Insets(5,5,5,5));
      quarterlyReportText.setColumns(COLS);
      quarterlyReportButton = new JButton("Quarterly Reports Folder");
      quarterlyReportButton.addActionListener(this);
      quarterlyReportButton.setPreferredSize(buttonSize);
      quarterlyReportPanel.add(quarterlyReportText);
      quarterlyReportPanel.add(quarterlyReportButton);
      
      saveButton = new JButton("Save");
      saveButton.addActionListener(this);
      
      //mainPanel.add(mpegBookKeeperPanel);
      mainPanel.add(timeSheetPanel);
      mainPanel.add(recapPanel);
      mainPanel.add(jcaPanel);
      mainPanel.add(pmWorkbookPanel);
      mainPanel.add(quarterlyReportPanel);
      mainPanel.add(saveButton);
      
      this.add(mainPanel);
      this.setVisible(true);
      
   }
   
   public void actionPerformed(ActionEvent e) {
      String source;
      String choice;      
      
      source = e.getActionCommand();
      choice = null;
      
      try {
		   if (source.compareTo("Save") == 0) {
		   	try {
		   		settings.saveSettings();
		   	}
		   	catch (SettingsException ex) {
		   		System.out.println("Failed to save settings.");
		   	}
		   }
		   else if (source.compareTo("MPEGBookKeeper Folder") == 0) {
		      choice = chooseFolder(mpegBookKeeperText, settings.getMPEGBookKeeperFolder());
		      if (choice != null) 
		      	settings.setMPEGBookKeeperFolder(choice);
		   }
		   else if (source.compareTo("TimeSheets Folder") == 0) {
		      choice = chooseFolder(timeSheetText, settings.getTimeSheetsFolder());
		      if (choice != null) 
		      	settings.setTimeSheetsFolder(choice);
		   }
		   else if (source.compareTo("Recaps Folder") == 0) {
		      choice = chooseFolder(recapText, settings.getRecapsFolder());
		      if (choice != null) 
		      	settings.setRecapsFolder(choice);
		   }
		   else if (source.compareTo("JCAs Folder") == 0) {
		      choice = chooseFolder(jcaText, settings.getJCAFolder());
		      if (choice != null) 
		      	settings.setJCAFolder(choice);
		   }
		   else if (source.compareTo("PM Workbooks Folder") == 0) {
		      choice = chooseFolder(pmWorkbookText, settings.getPMWorkbooksFolder());
		      if (choice != null) 
		      	settings.setPMWorkbooksFolder(choice);
		   }
		   else if (source.compareTo("Quarterly Reports Folder") == 0) {
		      choice = chooseFolder(quarterlyReportText, 
		      	settings.getQuarterlyReportsFolder());
		      if (choice != null) 
		      	settings.setQuarterlyReportsFolder(choice);
		   }
		}
		catch (Exception ex) {
			System.out.println(source + " failed. " + ex);
		}
   }
   
   private String chooseFolder(JTextArea toSet, String setting) {
		File dir;
		int userAction;
		String newSetting;
		JFileChooser chooser;
		
		newSetting = null;
		
		dir = new File(setting);
		if (dir.isDirectory())
			chooser = new JFileChooser(dir);
		else
			chooser = new JFileChooser();
			
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		userAction = chooser.showOpenDialog(this);
		
		if (userAction == JFileChooser.APPROVE_OPTION) {
			newSetting = chooser.getSelectedFile().getAbsolutePath();
			toSet.setText(newSetting);
		}
		
		return newSetting;
   }
}
