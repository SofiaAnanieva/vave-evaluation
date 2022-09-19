package org.argouml.kernel;

import java.beans.PropertyChangeEvent;


public class DelayedChangeNotify implements Runnable {
	private DelayedVChangeListener listener;
	private PropertyChangeEvent pce;
	public DelayedChangeNotify(DelayedVChangeListener l,PropertyChangeEvent p) {
		listener = l;
		pce = p;
	}
	public void run() {
		listener.delayedVetoableChange(pce);
	}
}



