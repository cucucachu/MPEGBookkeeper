package mpeg_bookkeeper.jca_builder;

/**
 * An exception used to report that a JCA file is missing.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class JCANotFoundException extends Exception {
   private String jobNoStr;
   
   public JCANotFoundException(String msg, String jobNoStr) {
      super(msg + " " + jobNoStr + ". Please create it or remove offending job "
         + "from recap and try again.");
      this.jobNoStr = jobNoStr;
   }
   
   public String getJobNo() {
      return jobNoStr;
   }
}
