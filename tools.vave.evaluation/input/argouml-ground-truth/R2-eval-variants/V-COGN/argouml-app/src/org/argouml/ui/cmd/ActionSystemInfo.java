package org.argouml.ui.cmd;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.SystemInfoDialog;
import org.argouml.util.ArgoFrame;


public class ActionSystemInfo extends AbstractAction {
	public ActionSystemInfo() {
		super(Translator.localize("action.system-information"),ResourceLoaderWrapper.lookupIcon("action.system-information"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.system-information"));
	}
	public void actionPerformed(ActionEvent ae) {
		JFrame jFrame = ArgoFrame.getInstance();
		SystemInfoDialog sysInfoDialog = new SystemInfoDialog(true);
		Dimension siDim = sysInfoDialog.getSize();
		Dimension pbDim = jFrame.getSize();
		if (siDim.width > pbDim.width / 2) {
			sysInfoDialog.setSize(pbDim.width / 2,siDim.height + 45);
		}else {
			sysInfoDialog.setSize(siDim.width,siDim.height + 45);
		}
		sysInfoDialog.setLocationRelativeTo(jFrame);
		sysInfoDialog.setVisible(true);
	}
}



