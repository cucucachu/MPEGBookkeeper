package mpeg_book_keeper;

import java.lang.InterruptedException;
import java.util.concurrent.atomic.AtomicBoolean;

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
