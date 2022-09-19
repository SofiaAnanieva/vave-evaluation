package org.argouml.ui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.argouml.swingext.GlassPane;
import org.argouml.taskmgmt.ProgressMonitor;
import org.argouml.util.ArgoFrame;


public abstract class SwingWorker {
	private Object value;
	private GlassPane glassPane;
	private Timer timer;
	private ProgressMonitor pmw;
	private static class ThreadVar {
	private Thread thread;
	ThreadVar(Thread t) {
			thread = t;
		}
	synchronized Thread get() {
		return thread;
	}
	synchronized void clear() {
		thread = null;
	}
}
	private ThreadVar threadVar;
	protected synchronized Object getValue() {
		return value;
	}
	private synchronized void setValue(Object x) {
		value = x;
	}
	public abstract Object construct(ProgressMonitor progressMonitor);
	public abstract ProgressMonitor initProgressMonitorWindow();
	public Object doConstruct() {
		activateGlassPane();
		pmw = initProgressMonitorWindow();
		ArgoFrame.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Object retVal = null;
		timer = new Timer(25,new TimerListener());
		timer.start();
		try {
			retVal = construct(pmw);
		}catch (Exception exc) {
		}finally {
			pmw.close();
		}
		return retVal;
	}
	class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			if (pmw.isCanceled()) {
				threadVar.thread.interrupt();
				interrupt();
				timer.stop();
			}
		}
	}
	protected void activateGlassPane() {
		GlassPane aPane = GlassPane.mount(ArgoFrame.getInstance(),true);
		setGlassPane(aPane);
		if (getGlassPane() != null) {
			getGlassPane().setVisible(true);
		}
	}
	private void deactivateGlassPane() {
		if (getGlassPane() != null) {
			getGlassPane().setVisible(false);
		}
	}
	public void finished() {
		deactivateGlassPane();
		ArgoFrame.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	protected GlassPane getGlassPane() {
		return glassPane;
	}
	protected void setGlassPane(GlassPane newGlassPane) {
		glassPane = newGlassPane;
	}
	public void interrupt() {
		Thread t = threadVar.get();
		if (t != null) {
			t.interrupt();
		}
		threadVar.clear();
	}
	public Object get() {
		while (true) {
			Thread t = threadVar.get();
			if (t == null) {
				return getValue();
			}
			try {
				t.join();
			}catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return null;
			}
		}
	}
	public SwingWorker() {
		final Runnable doFinished = new Runnable() {
	public void run() {
		finished();
	}
};
		Runnable doConstruct = new Runnable() {
	public void run() {
		try {
			setValue(doConstruct());
		}finally {
			threadVar.clear();
		}
		SwingUtilities.invokeLater(doFinished);
	}
};
		Thread t = new Thread(doConstruct);
		threadVar = new ThreadVar(t);
	}
	public SwingWorker(String threadName) {
		this();
		threadVar.get().setName(threadName);
	}
	public void start() {
		Thread t = threadVar.get();
		if (t != null) {
			t.start();
		}
	}
}



