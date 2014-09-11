package mpeg_bookkeeper;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import mpeg_bookkeeper.weekly_recap.*;
import mpeg_bookkeeper.jca_builder.*;
import mpeg_bookkeeper.pm_workbook_compiler.*;
import mpeg_bookkeeper.quarterly_reporter.*;

import java.awt.event.*;
import javax.swing.filechooser.*;

/**
 * The main GUI for MPEGBookkeeper containing one panel for each of the 
 * four processes of the program. 
 *
 * @author Cody Jones
 * @version 1.0
 */

public class MPEGBookkeeperGui implements ActionListener {
   private static boolean busy = false;
   public static final Color BG_COLOR = new Color(228, 228, 228, 255);
   public static final Color TAB_COLOR = new Color(200, 221, 242, 255);
   public static final Color PARCHMENT_COLOR = new Color(241, 241, 211, 255);
   public static final Color BANNER_COLOR = new Color(48, 10, 36, 255);
   public static final Color BUTTON_COLOR = new Color(200, 221, 242, 255);
   public static final Color TEXT_COLOR = new Color(227, 219, 219, 255);
   public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   
   private JFrame frame;
   private JTabbedPane tabs;
   
   private JPanel panel;
   private JPanel bannerPanel;
   
   private JMenuBar menuBar;
   private JMenu helpMenu;
   private JMenu settingsMenu;
   private JMenu aboutMenu;
   private JMenuItem settingsButton;
   private JMenuItem helpButton;
   private JMenuItem aboutButton;
   private JMenuItem projectHomePageButton;
   
   private JButton weeklyRecapButton;
   private JButton jcaBuilderButton;
   private JButton pmWorkbookCompilerButton;
   private JButton quarterlyReporterButton;
   private JLabel banner;
   private ImageIcon icon;
   
   private WeeklyRecapGui wrGui;
   private JCAGui jcaGui;
   private PMGui pmGui;
   private QRGui qrGui;
   
   public MPEGBookkeeperGui() {
   	Container pane;
   	
      frame = new JFrame("MPEG Bookkeeper");
      tabs = new JTabbedPane();
      
      panel = new JPanel();
      bannerPanel = new JPanel();
      pane = frame.getContentPane();
      
      try  {
	      banner = new JLabel(new ImageIcon(getClass().getClassLoader().getResource(
	      "resources/banner.png")));
	   	icon = new ImageIcon(getClass().getClassLoader().getResource(
	   	"resources/book.png"));
	   }
	   catch(Exception ex) {
	   	System.out.println("Could not find banner");
	   }
      
      menuBar = new JMenuBar();
      //menuBar.setBackground(BANNER_COLOR);
      
      helpMenu = new JMenu("Help");
      helpButton = new JMenuItem("User Manual");
      helpButton.addActionListener(this);
      helpMenu.add(helpButton);
      menuBar.add(helpMenu);
      
      settingsMenu = new JMenu("Settings");
      settingsButton = new JMenuItem("Edit Settings");
      settingsButton.addActionListener(this);
      settingsMenu.add(settingsButton);
      menuBar.add(settingsMenu);
      
      aboutMenu = new JMenu("About");
      aboutButton = new JMenuItem("Info");
      aboutButton.addActionListener(this);
      aboutMenu.add(aboutButton);
      projectHomePageButton = new JMenuItem("Github Page");
      projectHomePageButton.addActionListener(this);
      aboutMenu.add(projectHomePageButton);
      menuBar.add(aboutMenu);
      
      weeklyRecapButton = new JButton("Weekly Recap");
      weeklyRecapButton.addActionListener(this);
      
      jcaBuilderButton = new JButton("JCA Builder");
      jcaBuilderButton.addActionListener(this);
      
      pmWorkbookCompilerButton = new JButton("PM Workbook Compiler");
      pmWorkbookCompilerButton.addActionListener(this);
      
      quarterlyReporterButton = new JButton("Quarterly Reporter");
      quarterlyReporterButton.addActionListener(this);
      
      bannerPanel.setBackground(BANNER_COLOR);
      bannerPanel.add(banner);
      
      tabs.setBackground(BG_COLOR);
      
      wrGui = new WeeklyRecapGui();
      tabs.addTab("Weekly Recap", wrGui);
      
      jcaGui = new JCAGui();
      tabs.addTab("JCA Builder", jcaGui);
      
      pmGui = new PMGui();
      tabs.addTab("PM Workbook Compiler", pmGui);
      
      qrGui = new QRGui();
      tabs.addTab("Quarterly Reporter", qrGui);
      
      panel.setBackground(BANNER_COLOR);
      panel.setLayout(new BorderLayout());
      panel.add(bannerPanel, BorderLayout.PAGE_START);
      panel.add(tabs, BorderLayout.CENTER);
      
      frame.setJMenuBar(menuBar);
      frame.add(panel);
      frame.setIconImage(icon.getImage());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setResizable(true);
      frame.setSize(1024, 700);
      frame.setVisible(true);
   }
   
   public void actionPerformed(ActionEvent e) {
      String source = e.getActionCommand();
      
      if (source.compareTo("User Manual") == 0) {
         openUserManual();
      }
      else if (source.compareTo("Edit Settings") == 0) {
         editSettings();
      }
      else if (source.compareTo("Info") == 0) {
         displayInfo();
      }
      else if (source.compareTo("Github Page") == 0) {
         openGitHubPage();
      }
   }
   
   private void openUserManual() {
   	File userManual;
   	
   	userManual = new File("MPEGBookkeeperUserManual.pdf");
   	try {
   		if (Desktop.isDesktopSupported())
   			Desktop.getDesktop().open(userManual);	
   	}
   	catch (Exception ex) {
   		System.out.println(ex);
   	}
   }
   
   private void editSettings() {
   	MPEGBookkeeper.settingsGui = new SettingsGui(this, MPEGBookkeeper.settings);
   }
   
   public void applySettings() {
   	wrGui.applySettings();
		jcaGui.applySettings();
		pmGui.applySettings();
		qrGui.applySettings();
   }
   
   private void displayInfo() {
   	JFrame infoFrame;
   	JTextArea info;
   	String programInfo;
   	String newLine;
   	
   	newLine = System.lineSeparator();
   	
   	programInfo = 
   	"MPEG Bookkeeper" + newLine +
   	"Author: Cody Jones" + newLine +
   	"Created for Miller Pacific Engineering Group" + newLine +
   	"September 2014";
   	
   	info = new JTextArea(programInfo);
   	info.setEditable(false);
   	info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
   	info.setBackground(PARCHMENT_COLOR);
   	info.setFont(new Font("Arial", Font.BOLD, 14));
   	
   	infoFrame = new JFrame("MPEG Bookkeeper Info");
   	infoFrame.add(info);
   	infoFrame.pack();
   	infoFrame.setVisible(true);
   }
   
   private void openGitHubPage() {
   	try {
   	Desktop.getDesktop().browse(
   		new java.net.URI("http://www.github.com/cucucachu/MPEGBookkeeper"));
   	}
   	catch (Exception ex) {
   	
   	}
   }
}
