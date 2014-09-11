package mpeg_bookkeeper;

/**
 * A exception used to report a badly formatted or missing
 * settings file.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class SettingsException extends Exception {
	public SettingsException(String msg) {
		super(msg);
	}
}
