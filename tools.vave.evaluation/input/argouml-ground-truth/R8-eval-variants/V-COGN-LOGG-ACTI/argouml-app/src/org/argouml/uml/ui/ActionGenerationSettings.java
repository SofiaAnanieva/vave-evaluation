package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.tigris.gef.undo.UndoableAction;


public class ActionGenerationSettings extends UndoableAction {
	public ActionGenerationSettings() {
		super(Translator.localize("action.settings-for-project-code-generation"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.settings-for-project-code-generation"));
	}
	@Override public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		SourcePathDialog cgd = new SourcePathDialog();
		cgd.setVisible(true);
	}
	@Override public boolean isEnabled() {
		return true;
	}
}



