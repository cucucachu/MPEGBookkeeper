package mpeg_bookkeeper;

import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

/**
 * A Panel providing a way for the user to select a file or folder.
 * Contains a text area and a button which brings up a fileChooser.
 *
 * @author Cody Jones
 * @version 1.0
 */

public class SelectionPanel extends JPanel implements ActionListener {
   private JTextArea folderSelectText;
   private JButton selectButton;
   private String dir;
   private int fileSelectionMode;
   
   public SelectionPanel(String dir, String buttonText) {
   	this.dir = dir;
      this.setAlignmentX(Component.RIGHT_ALIGNMENT);
      this.setBackground(mpeg_bookkeeper.MPEGBookkeeperGui.BG_COLOR);
      this.setMaximumSize(new Dimension(1000, 40));
      
      fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES;
      
      folderSelectText = new JTextArea(dir, 1, 40);
      folderSelectText.setEditable(false);
      folderSelectText.setBorder(BorderFactory.createEmptyBorder(5,5,0,0));
      folderSelectText.setPreferredSize(new Dimension(400, 30));
      folderSelectText.setBackground(mpeg_bookkeeper.MPEGBookkeeperGui.BANNER_COLOR);
      folderSelectText.setForeground(mpeg_bookkeeper.MPEGBookkeeperGui.TEXT_COLOR);
      
      selectButton = new JButton(buttonText);
      selectButton.addActionListener(this);
      selectButton.setPreferredSize(new Dimension(250, 30));
      selectButton.setBackground(mpeg_bookkeeper.MPEGBookkeeperGui.BUTTON_COLOR);
      //selectButton.setForeground(mpeg_bookkeeper.MPEGBookkeeperGui.TEXT_COLOR);
            
      this.add(selectButton);
      this.add(folderSelectText);
	}
	
	 public void actionPerformed(ActionEvent e) {
      JFileChooser fileChooser;
      
      fileChooser = new JFileChooser(new File(dir));
      fileChooser.setFileSelectionMode(fileSelectionMode);
      if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			folderSelectText.setText(filePath);
			dir = filePath;
	   }
   }
   
   public String getSelection() {
   	Document doc;
   	String path;
   	
   	path = "";
   	doc = folderSelectText.getDocument();
   	try {
   		path = doc.getText(0, doc.getLength());
   	}
   	catch (BadLocationException ex) {
   		System.out.println(ex);
   	}
   	return path;
   }
   
   public void applySetting(String setting) {
   	if (new File(setting).exists()) {
			folderSelectText.setText(setting);
			dir = setting;
		}
   }
   
   public void filesOnly() {
   	fileSelectionMode = JFileChooser.FILES_ONLY;
   }
   
   public void foldersOnly() {
   	fileSelectionMode = JFileChooser.DIRECTORIES_ONLY;
   }
}
