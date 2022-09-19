package org.argouml.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.argouml.i18n.Translator;
import org.argouml.taskmgmt.ProgressEvent;
import org.argouml.util.ArgoFrame;


public class ProgressMonitorWindow implements org.argouml.taskmgmt.ProgressMonitor {
	private ProgressMonitor pbar;
	public ProgressMonitorWindow(JFrame parent,String title) {
		pbar = new ProgressMonitor(parent,title,null,0,100);
		pbar.setMillisToDecideToPopup(250);
		pbar.setMillisToPopup(500);
		parent.repaint();
		updateProgress(5);
	}
	public void progress(final ProgressEvent event) {
		final int progress = (int) event.getPosition();
		if (pbar != null) {
			if (!SwingUtilities.isEventDispatchThread()) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						updateProgress(progress);
					}
				});
			}else {
				updateProgress(progress);
			}
		}
	}
	public void updateProgress(final int progress) {
		if (pbar != null) {
			pbar.setProgress(progress);
			Object[]args = new Object[] {String.valueOf(progress)};
			pbar.setNote(Translator.localize("dialog.progress.note",args));
		}
	}
	public boolean isCanceled() {
		return(pbar != null)&&pbar.isCanceled();
	}
	public void close() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pbar.close();
				pbar = null;
			}
		});
	}
	static {
	UIManager.put("ProgressBar.repaintInterval",Integer.valueOf(150));
	UIManager.put("ProgressBar.cycleTime",Integer.valueOf(1050));
}
	public void notifyMessage(final String title,final String introduction,final String message) {
		final String messageString = introduction + " : " + message;
		pbar.setNote(messageString);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JDialog dialog = new ExceptionDialog(ArgoFrame.getInstance(),title,introduction,message);
				dialog.setVisible(true);
			}
		});
	}
	public void notifyNullAction() {
	}
	public void setMaximumProgress(int max) {
		pbar.setMaximum(max);
	}
	public void updateSubTask(String action) {
		pbar.setNote(action);
	}
	public void updateMainTask(String name) {
		pbar.setNote(name);
	}
}



