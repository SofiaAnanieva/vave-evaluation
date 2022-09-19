package org.argouml.ui;

import java.text.MessageFormat;
import javax.swing.SwingUtilities;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.application.events.ArgoHelpEventListener;
import org.argouml.application.events.ArgoStatusEvent;
import org.argouml.application.events.ArgoStatusEventListener;
import org.argouml.i18n.Translator;
import org.tigris.gef.ui.IStatusBar;


public class ArgoStatusBar extends StatusBar implements IStatusBar,ArgoStatusEventListener,ArgoHelpEventListener {
	public ArgoStatusBar() {
		super();
		ArgoEventPump.addListener(ArgoEventTypes.ANY_HELP_EVENT,this);
		ArgoEventPump.addListener(ArgoEventTypes.ANY_STATUS_EVENT,this);
	}
	public void projectLoaded(ArgoStatusEvent e) {
		String status = MessageFormat.format(Translator.localize("statusmsg.bar.open-project-status-read"),new Object[] {e.getText()});
		showStatusOnSwingThread(status);
	}
	public void projectModified(ArgoStatusEvent e) {
		String status = MessageFormat.format(Translator.localize("statusmsg.bar.project-modified"),new Object[] {e.getText()});
		showStatusOnSwingThread(status);
	}
	public void projectSaved(ArgoStatusEvent e) {
		String status = MessageFormat.format(Translator.localize("statusmsg.bar.save-project-status-wrote"),new Object[] {e.getText()});
		showStatusOnSwingThread(status);
	}
	public void statusCleared(ArgoStatusEvent e) {
		showStatusOnSwingThread("");
	}
	public void statusText(ArgoStatusEvent e) {
		showStatusOnSwingThread(e.getText());
	}
	public void helpChanged(ArgoHelpEvent e) {
		showStatusOnSwingThread(e.getHelpText());
	}
	public void helpRemoved(ArgoHelpEvent e) {
		showStatusOnSwingThread("");
	}
	private void showStatusOnSwingThread(final String status) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showStatus(status);
			}
		});
	}
}



