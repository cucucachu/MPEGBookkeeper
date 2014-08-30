package mpeg_book_keeper;

import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

public class TextPanel extends JPanel {
	private JTextArea labelArea;
   private JTextArea textArea;
   
   public TextPanel(String label, String text) {
      this.setBackground(mpeg_book_keeper.MPEGBookKeeperGui.BG_COLOR);
      this.setMaximumSize(new Dimension(370, 30));
      this.setAlignmentX(Component.RIGHT_ALIGNMENT);
      
      textArea = new JTextArea(text, 1, 15);
      textArea.setMaximumSize(new Dimension(80, 30));
      textArea.setAlignmentX(Component.RIGHT_ALIGNMENT);
      textArea.setBackground(mpeg_book_keeper.MPEGBookKeeperGui.BANNER_COLOR);
      textArea.setForeground(mpeg_book_keeper.MPEGBookKeeperGui.TEXT_COLOR);
      
      labelArea = new JTextArea(label, 1, label.length());
      //labelArea.setAlignmentX(Component.RIGHT_ALIGNMENT);
      labelArea.setMaximumSize(new Dimension(80, 30));
      labelArea.setBackground(mpeg_book_keeper.MPEGBookKeeperGui.BG_COLOR);
      //labelArea.setForeground(mpeg_book_keeper.MPEGBookKeeperGui.TEXT_COLOR);
      
      this.add(labelArea);
      this.add(textArea);
   }
   
   public String getText() {
   	return textArea.getText();
   }
}
