package mpeg_bookkeeper.jca_builder;

/**
 * An exception used to report a badly formatted JCA file.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class JCAException extends Exception {
   public JCAException(String msg) {
      super(msg);
   }
}
