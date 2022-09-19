package org.argouml.uml.diagram.ui;

import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;


@UmlModelMutator class ActionVisibilityPublic extends AbstractActionRadioMenuItem {
	private static final long serialVersionUID = -4288749276325868991l;
	public ActionVisibilityPublic(Object o) {
		super("checkbox.visibility.public-uc",false);
		putValue("SELECTED",Boolean.valueOf(Model.getVisibilityKind().getPublic().equals(valueOfTarget(o))));
	}
	void toggleValueOfTarget(Object t) {
		Model.getCoreHelper().setVisibility(t,Model.getVisibilityKind().getPublic());
	}
	Object valueOfTarget(Object t) {
		Object v = Model.getFacade().getVisibility(t);
		return v == null?Model.getVisibilityKind().getPublic():v;
	}
}



