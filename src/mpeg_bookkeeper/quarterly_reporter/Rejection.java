package mpeg_bookkeeper.quarterly_reporter;

/**
 * Stores a project or JCA which could not be read or written, 
 * and the reason it could not be read or written.
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
   
   public String toString() {
      return name + ": " + reason;
   }

}
