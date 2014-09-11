package mpeg_bookkeeper;

import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

/**
 * A JPanel containing a text label and an editable text
 * area for user input.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class TextPanel extends JPanel {
	private JTextArea labelArea;
   private JTextArea textArea;
   
   public TextPanel(String label, String text) {
      this.setBackground(mpeg_bookkeeper.MPEGBookkeeperGui.BG_COLOR);
      this.setMaximumSize(new Dimension(370, 30));
      this.setAlignmentX(Component.RIGHT_ALIGNMENT);
      
      textArea = new JTextArea(text, 1, 15);
      textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
      textArea.setMaximumSize(new Dimension(80, 30));
      textArea.setAlignmentX(Component.RIGHT_ALIGNMENT);
      textArea.setBackground(mpeg_bookkeeper.MPEGBookkeeperGui.BANNER_COLOR);
      textArea.setForeground(mpeg_bookkeeper.MPEGBookkeeperGui.TEXT_COLOR);
      
      labelArea = new JTextArea(label, 1, label.length());
      labelArea.setMaximumSize(new Dimension(80, 30));
      labelArea.setBackground(mpeg_bookkeeper.MPEGBookkeeperGui.BG_COLOR);
      labelArea.setEditable(false);
      
      this.add(labelArea);
      this.add(textArea);
   }
   
   public String getText() {
   	return textArea.getText();
   }
}
