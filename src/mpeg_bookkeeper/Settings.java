package mpeg_bookkeeper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Interacts with a settings file to load and save settings
 * for individual runs of the program.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class Settings {
	private final String settingsFilePath = "./";
	private final String settingsFileName = "MPEGBookkeeper.dat";
	private final String MPEGBookkeeperFolderKey = "MPEGBookkeeperFolder";
	private final String TimeSheetsFolderKey = "TimeSheetsFolder";
	private final String RecapsFolderKey = "RecapsFolder";
	private final String JCAFolderKey = "JCAFolder";
	private final String PMWorkbooksFolderKey = "PMWorkbooksFolder";
	private final String QuarterlyReportsFolderKey = "QuarterlyReportsFolder";

	private String MPEGBookkeeperFolder;
	private String TimeSheetsFolder;
	private String RecapsFolder;
	private String JCAFolder;
	private String PMWorkbooksFolder;
	private String QuarterlyReportsFolder;
	private File settingsFile;
		
	public Settings() throws SettingsException {
		clearSettings();
		
		settingsFile = new File(settingsFilePath + settingsFileName);
		
		try {
			loadSettings();
		}
		catch (SettingsException ex) {
			System.out.println(ex);
			try {
				saveSettings();
			}
			catch (SettingsException exx) {
				System.out.println(exx);
				clearSettings();
			}
		}
	}
	
	private void loadSettings() throws SettingsException {
		Scanner in;
		
		try {
			in = new java.util.Scanner(settingsFile);
		}
		catch (FileNotFoundException ex) {
			throw new SettingsException("Settings file MPEGBookkeeper.dat does not exist.");
		}
		
		MPEGBookkeeperFolder = getSetting(in, MPEGBookkeeperFolderKey);
		TimeSheetsFolder = getSetting(in, TimeSheetsFolderKey);
		RecapsFolder = getSetting(in, RecapsFolderKey);
		JCAFolder = getSetting(in, JCAFolderKey);
		PMWorkbooksFolder = getSetting(in, PMWorkbooksFolderKey);
		QuarterlyReportsFolder = getSetting(in, QuarterlyReportsFolderKey);
	}
	
	private String getSetting(Scanner in, String key) {
		String str;
		String setting;
		
		str = null;
		setting = null;
		in.reset();
		
		while (in.hasNext() && setting == null) {
			str = in.next();
			if (str.compareTo(key) == 0) {	
				if (in.hasNext()) {
					str = in.nextLine();
					if (str.charAt(0) == ' ' && str.charAt(1) == '=') {
						setting = str.trim();
					}
				}
			}
		}
		
		if (setting == null || setting.length() == 1)
			setting = "";
		else
			setting = setting.substring(1);
		
		return setting;
	}
	
	public void saveSettings() throws SettingsException {
		FileWriter writer;
		
		if (settingsFile.exists())
			settingsFile.delete();
		
		try {
			System.out.println("Creating settings file at " + settingsFile.getPath());
			settingsFile.createNewFile();
		
			writer = new FileWriter(settingsFile);
			
			writer.write(MPEGBookkeeperFolderKey + " =" + MPEGBookkeeperFolder + "\n");
			writer.write(TimeSheetsFolderKey + " =" + TimeSheetsFolder + "\n");
			writer.write(RecapsFolderKey + " =" + RecapsFolder + "\n");
			writer.write(JCAFolderKey + " =" + JCAFolder + "\n");
			writer.write(PMWorkbooksFolderKey + " =" + PMWorkbooksFolder + "\n");
			writer.write(QuarterlyReportsFolderKey + " =" + QuarterlyReportsFolder + "\n");
			writer.close();
		}
		catch (IOException ex) {
			throw new SettingsException("Failed to create settings file at " 
				+ settingsFile.getPath() + ". " + ex.getMessage());
		}
		
	}

	private void clearSettings() {
		MPEGBookkeeperFolder = "";
		TimeSheetsFolder = "";
		RecapsFolder = "";
		JCAFolder = "";
		PMWorkbooksFolder = "";
		QuarterlyReportsFolder = "";
	}

	public String getMPEGBookkeeperFolder() {
		return MPEGBookkeeperFolder;
	}

	public String getTimeSheetsFolder() {
		return TimeSheetsFolder;
	}

	public String getRecapsFolder() {
		return RecapsFolder;
	}

	public String getJCAFolder() {
		return JCAFolder;
	}

	public String getPMWorkbooksFolder() {
		return PMWorkbooksFolder;
	}

	public String getQuarterlyReportsFolder() {
		return QuarterlyReportsFolder;
	}
	
	public void setMPEGBookkeeperFolder(String path) {
		MPEGBookkeeperFolder = path;
	}
	
	public void setTimeSheetsFolder(String path) {
		TimeSheetsFolder = path;
	}
	
	public void setRecapsFolder(String path) {
		RecapsFolder = path;
	}
	
	public void setJCAFolder(String path) {
		JCAFolder = path;
	}
	
	public void setPMWorkbooksFolder(String path) {
		PMWorkbooksFolder = path;
	}
	
	public void setQuarterlyReportsFolder(String path) {
		QuarterlyReportsFolder = path;
	}
	
	public String toString() {
		return "Settings: \n   "
			+ MPEGBookkeeperFolder + "\n   "
			+ TimeSheetsFolder + "\n   "
			+ RecapsFolder + "\n   "
			+ JCAFolder + "\n   "
			+ PMWorkbooksFolder + "\n   "
			+ QuarterlyReportsFolder + "\n";
	}
}
