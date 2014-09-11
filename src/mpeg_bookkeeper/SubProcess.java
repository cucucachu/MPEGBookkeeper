package mpeg_bookkeeper;

import java.lang.InterruptedException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An abstract class containing common methods for a process
 * to interact with a GuiTab and be safely terminated from 
 * another thread.
 *
 * @author Cody Jones
 * @version 1.0
 */
public abstract class SubProcess implements Runnable {
	protected AtomicBoolean kill = new AtomicBoolean(false);
	protected GuiTab gui;
	
   protected abstract void cleanUp();
   
   protected void pollStop() throws InterruptedException {
   	if (kill.get() == true)
   		throw new InterruptedException();
   }
   
   public void stop() {
   	kill.set(true);
   }
   
   public void output(String msg) {
   	gui.output(msg);
   }
}
