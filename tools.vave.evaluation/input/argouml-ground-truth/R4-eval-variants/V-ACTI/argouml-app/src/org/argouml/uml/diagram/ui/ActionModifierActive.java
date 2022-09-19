package org.argouml.uml.diagram.ui;

import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;


@UmlModelMutator class ActionModifierActive extends AbstractActionCheckBoxMenuItem {
	private static final long serialVersionUID = -4458846555966612262l;
	public ActionModifierActive(Object o) {
		super("checkbox.active-uc");
		putValue("SELECTED",Boolean.valueOf(valueOfTarget(o)));
	}
	void toggleValueOfTarget(Object t) {
		Model.getCoreHelper().setActive(t,!Model.getFacade().isActive(t));
	}
	boolean valueOfTarget(Object t) {
		return Model.getFacade().isActive(t);
	}
}



