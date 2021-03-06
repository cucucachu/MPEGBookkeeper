package mpeg_bookkeeper.jca_builder;

import java.util.ArrayList;

import mpeg_bookkeeper.GuiTab;

/**
 * Creates JCAs and sorts jobs to them by job number.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class JobSorter {
   
   public static ArrayList<JCA> sortJobs(ArrayList<Job> jobs, String jcaFolder,
      String week, GuiTab gui) 
      throws JCANotFoundException {
      
      ArrayList<JCA> jcas;
      boolean sorted;
      
      jcas = new ArrayList<JCA>();
      
      for (Job job : jobs) {
         sorted = false;
         for (JCA jca : jcas) {
            if (jca.getJobNoStr().compareTo(job.jobNoStr) == 0) {
               jca.addJob(job);
               sorted = true;
            }
         }
         if (sorted == false) {
            JCA newJCA = new JCA(job.jobNoStr, jcaFolder, week, gui);
            newJCA.addJob(job);
            jcas.add(newJCA);
         }
      }
      
      return jcas;
   }
}
