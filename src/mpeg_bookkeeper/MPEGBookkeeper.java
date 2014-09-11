package mpeg_bookkeeper;

/**
 * Main class for MPEGBookkeeper which launches MPEGBookkeeperGUI.
 *
 * @author Cody Jones
 * @version 1.0
 */

public class MPEGBookkeeper {
   static MPEGBookkeeperGui gui;
   static SettingsGui settingsGui;
   public static Settings settings;
   
   public static void main(String[] args) {
   	try {
   		settings = new Settings();
      }
      catch(SettingsException ex) {
      	System.out.println(ex);
      }
      
      gui = new MPEGBookkeeperGui();
   }
}
