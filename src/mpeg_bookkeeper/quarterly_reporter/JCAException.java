package mpeg_bookkeeper.quarterly_reporter;

/**
 * An exception to report a badly formatted or missing JCA.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class JCAException extends Exception {
   public JCAException(String msg) {
      super(msg);
   }
}
