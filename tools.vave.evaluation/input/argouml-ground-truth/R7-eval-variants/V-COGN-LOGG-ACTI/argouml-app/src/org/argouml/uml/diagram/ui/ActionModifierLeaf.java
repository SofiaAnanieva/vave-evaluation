package org.argouml.uml.diagram.ui;

import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;


@UmlModelMutator class ActionModifierLeaf extends AbstractActionCheckBoxMenuItem {
	private static final long serialVersionUID = 1087245945242698348l;
	public ActionModifierLeaf(Object o) {
		super("checkbox.final-uc");
		putValue("SELECTED",Boolean.valueOf(valueOfTarget(o)));
	}
	void toggleValueOfTarget(Object t) {
		Model.getCoreHelper().setLeaf(t,!Model.getFacade().isLeaf(t));
	}
	boolean valueOfTarget(Object t) {
		return Model.getFacade().isLeaf(t);
	}
}



