package org.argouml.uml.ui;

import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;
import org.tigris.gef.undo.UndoableAction;


@UmlModelMutator public class AbstractActionRemoveElement extends UndoableAction {
	private Object target;
	private Object objectToRemove;
	protected AbstractActionRemoveElement() {
		this(Translator.localize("menu.popup.remove"));
	}
	protected AbstractActionRemoveElement(String name) {
		super(Translator.localize(name),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize(name));
	}
	public Object getTarget() {
		return target;
	}
	public void setTarget(Object theTarget) {
		target = theTarget;
		setEnabled(isEnabled());
	}
	public Object getObjectToRemove() {
		return objectToRemove;
	}
	public void setObjectToRemove(Object theObjectToRemove) {
		objectToRemove = theObjectToRemove;
		setEnabled(isEnabled());
	}
	@Override public boolean isEnabled() {
		return getObjectToRemove() != null&&!Model.getModelManagementHelper().isReadOnly(getObjectToRemove())&&getTarget() != null&&!Model.getModelManagementHelper().isReadOnly(getTarget());
	}
}



