package mpeg_bookkeeper.jca_builder;

/**
 * Used to collect and report jobs which could not be sorted
 * to a JCA.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class Rejection {
   private String name;
   private String reason;
   
   public Rejection(String name, String reason) {
      this.name = name;
      this.reason = reason;
   }
   
   public String getReason() {
      return reason;
   }
   
   public String toString() {
      return name + ": " + reason;
   }
}
