package mpeg_bookkeeper.weekly_recap;

/**
 * An exception used to report a badly formatted or missing
 * time sheet.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class TimeSheetFormatException extends Exception {
   public TimeSheetFormatException(String msg) {
      super(msg);
   }
}
