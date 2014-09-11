package mpeg_bookkeeper.jca_builder;

/**
 * An exception used to report a badly formatted or missing
 * recap file.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class RecapFormatException extends Exception {
   public RecapFormatException(String msg) {
      super(msg);
   }
}
