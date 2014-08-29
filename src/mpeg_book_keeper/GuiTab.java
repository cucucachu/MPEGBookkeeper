package mpeg_book_keeper;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

public abstract class GuiTab extends JPanel implements ActionListener {
   private static final String newline = System.lineSeparator();
	
   protected Color bgColor;
   protected GuiTab gui;
   
   protected JPanel inputPanel;
   protected JPanel selectionPanels;
   protected JPanel textBoxPanels;
   protected JButton runButton;
   
   protected JTextArea log;
   protected JScrollPane logScrollPane;
   
   protected boolean running;

   public GuiTab() {
      guiSetUp();
   }
   
   public GuiTab(String title) {
      guiSetUp();
   }
   
   private void guiSetUp() {
   	running = false;
   	gui = this;
      bgColor = mpeg_book_keeper.MPEGBookKeeperGui.BG_COLOR;
      
      
      this.setBackground(bgColor);      
      this.setLayout(new BorderLayout());
      
      log = new JTextArea(15, 44);
      log.setMargin(new Insets(5,5,5,5));
      log.setEditable(false);
      log.setLineWrap(true);
      log.setWrapStyleWord(true);
      log.setBackground(mpeg_book_keeper.MPEGBookKeeperGui.PARCHMENT_COLOR);
      
      inputPanel = new JPanel();
      inputPanel.setBackground(bgColor);
      inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
        
      logScrollPane = new JScrollPane(log);
      logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      
      selectionPanels = new JPanel();
      selectionPanels.setLayout(new BoxLayout(selectionPanels, BoxLayout.PAGE_AXIS));
      selectionPanels.setAlignmentX(Component.RIGHT_ALIGNMENT);
      selectionPanels.setBackground(bgColor);
      //selectionPanels.add(Box.createRigidArea(new Dimension(30,0)));
      
      textBoxPanels = new JPanel();
      textBoxPanels.setLayout(new BoxLayout(textBoxPanels, BoxLayout.PAGE_AXIS));
      textBoxPanels.setAlignmentX(Component.RIGHT_ALIGNMENT);
      textBoxPanels.setBackground(bgColor);
      
      runButton = new JButton("Start");
      runButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
      runButton.addActionListener(this);
      runButton.setBackground(mpeg_book_keeper.MPEGBookKeeperGui.BUTTON_COLOR);
      
      inputPanel.add(selectionPanels);
      inputPanel.add(textBoxPanels);
      inputPanel.add(runButton);
      
      this.add(inputPanel, BorderLayout.EAST);
      this.add(logScrollPane, BorderLayout.CENTER);
   }
   
   public void actionPerformed(ActionEvent e) {
      String source;
      
      source = e.getActionCommand();
      
      if (source.compareTo("Start") == 0)
      	runProgram();
   }
   
   public abstract void runProgram();
   
   public void output(String text) {
      int length;
      int rows;
      int cols;
      int area;
      int cutoff;
      
      length = log.getDocument().getLength();
      rows  = log.getRows();
      cols = 10;
      area = rows * cols;
      cutoff = length - area;
      
      log.append(text + newline);
      if (log.getCaretPosition() > cutoff)
         log.setCaretPosition(log.getDocument().getLength());
   }
   
   public void saveLogFile(String fileName) throws IOException {
      BufferedWriter logFile = new BufferedWriter(new FileWriter(fileName+".txt"));
      logFile.write(log.getText()); 
      logFile.close();
   }
}
