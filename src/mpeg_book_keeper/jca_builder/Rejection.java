package mpeg_book_keeper.jca_builder;

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
