package org.argouml.cognitive.ui;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.cognitive.Designer;
import org.argouml.i18n.Translator;
import org.argouml.ui.UndoableAction;


public class ActionAutoCritique extends UndoableAction {
	public ActionAutoCritique() {
		super(Translator.localize("action.toggle-auto-critique"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.toggle-auto-critique"));
		putValue("SELECTED",Boolean.valueOf(Designer.theDesigner().getAutoCritique()));
	}
	public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		Designer d = Designer.theDesigner();
		boolean b = d.getAutoCritique();
		d.setAutoCritique(!b);
		Designer.theDesigner().getToDoList().setPaused(!Designer.theDesigner().getToDoList().isPaused());
	}
	private static final long serialVersionUID = 9057306108717070004l;
}



