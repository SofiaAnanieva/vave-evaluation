package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.uml.ui.AbstractActionRemoveElement;


public class ActionRemoveArgument extends AbstractActionRemoveElement {
	protected ActionRemoveArgument() {
		super(Translator.localize("menu.popup.delete"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (getObjectToRemove() != null) {
			Project p = ProjectManager.getManager().getCurrentProject();
			Object o = getObjectToRemove();
			setObjectToRemove(null);
			p.moveToTrash(o);
		}
	}
}



