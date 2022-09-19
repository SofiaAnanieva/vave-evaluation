package org.argouml.util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;


public class UIUtils {
	private static final String ACTION_KEY_ESCAPE = "escapeAction";
	public static void loadCommonKeyMap(final JDialog dialog) {
		JRootPane rootPane = dialog.getRootPane();
		rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),ACTION_KEY_ESCAPE);
		rootPane.getActionMap().put(ACTION_KEY_ESCAPE,new AbstractAction() {
			private static final long serialVersionUID = 0;
			public void actionPerformed(ActionEvent evt) {
				dialog.dispose();
			}
		});
	}
}



