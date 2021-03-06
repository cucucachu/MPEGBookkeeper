package mpeg_bookkeeper.jca_builder;

import java.io.IOException;
import jxl.read.biff.BiffException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import jxl.write.*;
import java.lang.InterruptedException;

import mpeg_bookkeeper.SubProcess;

/**
 * Collects and distributes job data from a recap file into relevant JCAs.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class JCABuilder extends SubProcess {
   
   private String jcaFolder;
   private String recapFile;
   private String week;
   
   public JCABuilder(JCAGui gui, String jcaFolder, String recapFile, String week) {
   	this.gui = gui;
   	this.jcaFolder = jcaFolder;
   	this.recapFile = recapFile;
   	this.week = week;
   }
   
   public void run() {
      ArrayList<JCA> jcas;
      ArrayList<JCA> missingJCAs;
      ArrayList<JCA> failedJCAs;
      ArrayList<Rejection> jcaRejects;
      ArrayList<JCA> toPMWorkbooks;
      
      ArrayList<Job> allJobs;
      ArrayList<Job> failedJobs;
      ArrayList<Job> tempFailedJobs;
      
      Recap recap;
      Recap outRecap;
      String newJCAFolderName;
      File newJCAFolder;
      
      try {
         newJCAFolderName = jcaFolder + "/JCAs_" + week.replace("/", "_");
         newJCAFolder = new File(newJCAFolderName);
         newJCAFolder.mkdir();
         recap = new Recap(recapFile);
         allJobs = recap.getJobs();
         
         pollStop();
         jcas = JobSorter.sortJobs(allJobs, jcaFolder, week, gui);
         
         pollStop();
         missingJCAs = new ArrayList<JCA>();
         failedJCAs = new ArrayList<JCA>();
         jcaRejects = new ArrayList<Rejection>();
         failedJobs = new ArrayList<Job>();
         failedJobs.addAll(recap.getBadJobs());
         
         for (JCA jca : jcas) {
         	pollStop();
            gui.output("Writing to " + jca);
            try {
               jca.loadWorkbook();
               jca.prepareJCA();
               jca.fillJCA();
               jca.writeJCA();
               failedJobs.addAll(jca.getFailedJobs());
            }
            catch (JCANotFoundException ex) {
               gui.output("   " + ex.toString());
               missingJCAs.add(jca);
               tempFailedJobs = jca.getJobs();
               for (Job job : tempFailedJobs)
                  job.setRejection(new Rejection(job.toString(), "Could not find JCA."));
               failedJobs.addAll(tempFailedJobs);
            }
            catch (JCAException ex) {
               gui.output("   " + ex.toString());
               jcaRejects.add(new Rejection(jca.getJobNoStr(), ex.getMessage()));
               failedJCAs.add(jca);
               tempFailedJobs = jca.getJobs();
               for (Job job : tempFailedJobs)
                  job.setRejection(new Rejection(job.toString(), ex.getMessage()));
               failedJobs.addAll(tempFailedJobs);
            }
            catch (Exception ex) {
               gui.output("   Could not write to " + jca + ". Adding those jobs to failed jobs list.");
               gui.output("      " + ex.toString());
               tempFailedJobs = jca.getJobs();
               for (Job job : tempFailedJobs)
                  job.setRejection(new Rejection(job.toString(), ex.getMessage()));
               failedJobs.addAll(tempFailedJobs);
               jcaRejects.add(new Rejection(jca.getJobNoStr(), ex.getMessage()));
               failedJCAs.add(jca);
            }
         }
         
         gui.output("");
        
         gui.output("The following JCAs were not found in the given JCA folder: ");
         for (JCA jca : missingJCAs) {
            gui.output("   " + jca);
         }
         
         gui.output("The following JCAs failed for other reasons: ");
         for (Rejection reject : jcaRejects) {
            gui.output("   " + reject);
         }
         
         for (JCA jca : failedJCAs)
            jca.delete();
         
         gui.output("The following jobs were not added to JCAs: ");
         for (Job job : failedJobs) {
            gui.output("   " + job);
         }
         
         gui.output("Writing unsorted jobs to Recap file.");
         outRecap = new Recap(failedJobs);
         
         outRecap.makeRecap(recapFile.substring(0, recapFile.length() - 4) + "_out.xls");
         outRecap.writeRecap();
         gui.output("JCA Builder Complete.");
         gui.saveLogFile(newJCAFolderName + "/logFile_" + week.replace("/", "_"));
      }
      catch (InterruptedException ex) {
      	gui.resetLog();
      	gui.output("JCA Builder Canceled.");
      	gui.output("");
      	gui.output("Cleaning up files...");
      	cleanUp();
      	gui.output("");
      	gui.output("Weekly Recap Terminated.");      
      }
      catch (JCANotFoundException ex) {
         System.out.println("Caught exception " + ex);
         gui.output(ex.getMessage());
      }
      catch (RecapFormatException ex) {
         System.out.println("Caught a Recap Format Exception.");
         gui.output("Recap is incorrectly formatted. Please check the recap and try again.");
      }
      catch (SecurityException ex) {
         gui.output("JCA Builder does not have the necessary file permissions to create the JCA folder.");
      }
      catch (IOException ex) {
         gui.output("Failed to write log to file. " + ex);
      }
      finally {
      	gui.processFinishedCallback();
      }
   }
   
   protected void cleanUp() {
   
   }
}
