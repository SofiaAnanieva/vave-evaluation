package org.argouml.uml.diagram.ui;

import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;


@UmlModelMutator class ActionVisibilityPrivate extends AbstractActionRadioMenuItem {
	private static final long serialVersionUID = -1342216726253371114l;
	public ActionVisibilityPrivate(Object o) {
		super("checkbox.visibility.private-uc",false);
		putValue("SELECTED",Boolean.valueOf(Model.getVisibilityKind().getPrivate().equals(valueOfTarget(o))));
	}
	void toggleValueOfTarget(Object t) {
		Model.getCoreHelper().setVisibility(t,Model.getVisibilityKind().getPrivate());
	}
	Object valueOfTarget(Object t) {
		Object v = Model.getFacade().getVisibility(t);
		return v == null?Model.getVisibilityKind().getPublic():v;
	}
}



