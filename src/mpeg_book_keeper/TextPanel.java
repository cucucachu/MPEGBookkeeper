package mpeg_book_keeper;

import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

public class TextPanel extends JPanel {
   private JTextArea textArea;
   
   public TextPanel(String text) {
      this.setBackground(mpeg_book_keeper.MPEGBookKeeperGui.BG_COLOR);
      this.setPreferredSize(new Dimension(100, 25));
      this.setAlignmentX(Component.RIGHT_ALIGNMENT);
      
      textArea = new JTextArea(text, 1, 15);
      textArea.setPreferredSize(new Dimension(100, 25));
      textArea.setAlignmentX(Component.RIGHT_ALIGNMENT);
      textArea.setBackground(mpeg_book_keeper.MPEGBookKeeperGui.BANNER_COLOR);
      textArea.setForeground(mpeg_book_keeper.MPEGBookKeeperGui.TEXT_COLOR);
      
      this.add(textArea);
   }
   
   public String getText() {
   	return textArea.getText();
   }
}
