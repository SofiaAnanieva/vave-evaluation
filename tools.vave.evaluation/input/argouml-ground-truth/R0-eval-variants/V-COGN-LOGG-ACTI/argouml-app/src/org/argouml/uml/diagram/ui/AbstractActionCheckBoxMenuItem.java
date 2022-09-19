package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.undo.UndoableAction;


abstract class AbstractActionCheckBoxMenuItem extends UndoableAction {
	public AbstractActionCheckBoxMenuItem(String key) {
		super(Translator.localize(key),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize(key));
	}
	public boolean isEnabled() {
		boolean result = true;
		boolean commonValue = true;
		boolean first = true;
		Iterator i = TargetManager.getInstance().getTargets().iterator();
		while (i.hasNext()&&result) {
			Object t = i.next();
			try {
				boolean value = valueOfTarget(t);
				if (first) {
					commonValue = value;
					first = false;
				}
				result &= (commonValue == value);
			}catch (IllegalArgumentException e) {
				result = false;
			}
		}
		return result;
	}
	abstract boolean valueOfTarget(Object t);
	public final void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Iterator i = TargetManager.getInstance().getTargets().iterator();
		while (i.hasNext()) {
			Object t = i.next();
			toggleValueOfTarget(t);
		}
	}
	abstract void toggleValueOfTarget(Object t);
}



