package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;
import org.tigris.gef.undo.UndoableAction;


@Deprecated public class ActionSetStructuralFeatureTargetScope extends UndoableAction {
	private static final ActionSetStructuralFeatureTargetScope SINGLETON = new ActionSetStructuralFeatureTargetScope();
	protected ActionSetStructuralFeatureTargetScope() {
		super(Translator.localize("Set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof UMLCheckBox2) {
			UMLCheckBox2 source = (UMLCheckBox2) e.getSource();
			Object target = source.getTarget();
			if (Model.getFacade().isAStructuralFeature(target)) {
				Object m = target;
				Model.getCoreHelper().setTargetScope(m,source.isSelected()?Model.getScopeKind().getClassifier():Model.getScopeKind().getInstance());
			}
		}
	}
	public static ActionSetStructuralFeatureTargetScope getInstance() {
		return SINGLETON;
	}
}



