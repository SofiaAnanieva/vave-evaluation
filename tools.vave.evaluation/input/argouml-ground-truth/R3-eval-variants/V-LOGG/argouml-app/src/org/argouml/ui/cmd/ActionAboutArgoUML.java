package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.AboutBox;
import org.argouml.util.ArgoFrame;


public class ActionAboutArgoUML extends AbstractAction {
	public ActionAboutArgoUML() {
		super(Translator.localize("action.about-argouml"),ResourceLoaderWrapper.lookupIcon("action.about-argouml"));
	}
	public void actionPerformed(ActionEvent ae) {
		JFrame jframe = ArgoFrame.getInstance();
		AboutBox box = new AboutBox(jframe,true);
		box.setLocationRelativeTo(jframe);
		box.setVisible(true);
	}
	private static final long serialVersionUID = 7988731727182091682l;
}



