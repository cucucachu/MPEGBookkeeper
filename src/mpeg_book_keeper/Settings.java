package mpeg_book_keeper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Settings {
	private final String settingsFilePath = "../resources/";
	private final String settingsFileName = "MPEGBookKeeper.dat";
	private final String MPEGBookKeeperFolderKey = "MPEGBookKeeperFolder";
	private final String TimeSheetsFolderKey = "TimeSheetsFolder";
	private final String RecapsFolderKey = "RecapsFolder";
	private final String JCAFolderKey = "JCAFolder";
	private final String PMWorkbooksFolderKey = "PMWorkbooksFolder";
	private final String QuarterlyReportsFolderKey = "QuarterlyReportsFolder";

	private String MPEGBookKeeperFolder;
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
			saveSettings();
		}
	}
	
	private void loadSettings() throws SettingsException {
		Scanner in;
		
		try {
			in = new java.util.Scanner(settingsFile);
		}
		catch (FileNotFoundException ex) {
			throw new SettingsException("Settings file MPEGBookKeeper.dat does not exist.");
		}
		
		MPEGBookKeeperFolder = getSetting(in, MPEGBookKeeperFolderKey);
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
					str = in.next();
					if (str.charAt(0) == '=') {
						setting = str;
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
			settingsFile.createNewFile();
		
			writer = new FileWriter(settingsFile);
			
			writer.write(MPEGBookKeeperFolderKey + " =" + MPEGBookKeeperFolder + "\n");
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
		MPEGBookKeeperFolder = "";
		TimeSheetsFolder = "";
		RecapsFolder = "";
		JCAFolder = "";
		PMWorkbooksFolder = "";
		QuarterlyReportsFolder = "";
	}

	public String getMPEGBookKeeperFolder() {
		return MPEGBookKeeperFolder;
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
	
	public void setMPEGBookKeeperFolder(String path) {
		MPEGBookKeeperFolder = path;
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
			+ MPEGBookKeeperFolder + "\n   "
			+ TimeSheetsFolder + "\n   "
			+ RecapsFolder + "\n   "
			+ JCAFolder + "\n   "
			+ PMWorkbooksFolder + "\n   "
			+ QuarterlyReportsFolder + "\n";
	}
}
