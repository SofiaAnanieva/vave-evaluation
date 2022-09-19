package org.argouml.uml.diagram.ui;

import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;


@UmlModelMutator class ActionVisibilityProtected extends AbstractActionRadioMenuItem {
	private static final long serialVersionUID = -8808296945094744255l;
	public ActionVisibilityProtected(Object o) {
		super("checkbox.visibility.protected-uc",false);
		putValue("SELECTED",Boolean.valueOf(Model.getVisibilityKind().getProtected().equals(valueOfTarget(o))));
	}
	void toggleValueOfTarget(Object t) {
		Model.getCoreHelper().setVisibility(t,Model.getVisibilityKind().getProtected());
	}
	Object valueOfTarget(Object t) {
		Object v = Model.getFacade().getVisibility(t);
		return v == null?Model.getVisibilityKind().getPublic():v;
	}
}



