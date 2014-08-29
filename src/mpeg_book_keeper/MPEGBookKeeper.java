package mpeg_book_keeper;

public class MPEGBookKeeper {
   static MPEGBookKeeperGui gui;
   static SettingsGui settingsGui;
   public static Settings settings;
   
   public static void main(String[] args) {
   	try {
   		settings = new Settings();
      }
      catch(SettingsException ex) {
      	System.out.println(ex);
      }
      
      gui = new MPEGBookKeeperGui();
   }
}
