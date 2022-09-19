package org.argouml.uml.diagram.ui;

import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;


@UmlModelMutator class ActionVisibilityPackage extends AbstractActionRadioMenuItem {
	private static final long serialVersionUID = 8048943592787710460l;
	public ActionVisibilityPackage(Object o) {
		super("checkbox.visibility.package-uc",false);
		putValue("SELECTED",Boolean.valueOf(Model.getVisibilityKind().getPackage().equals(valueOfTarget(o))));
	}
	void toggleValueOfTarget(Object t) {
		Model.getCoreHelper().setVisibility(t,Model.getVisibilityKind().getPackage());
	}
	Object valueOfTarget(Object t) {
		Object v = Model.getFacade().getVisibility(t);
		return v == null?Model.getVisibilityKind().getPublic():v;
	}
}



