package org.argouml.uml.ui;

import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.UmlModelMutator;
import org.tigris.gef.undo.UndoableAction;


@UmlModelMutator public abstract class AbstractActionNewModelElement extends UndoableAction {
	private Object target;
	protected AbstractActionNewModelElement() {
		super(Translator.localize("action.new"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.new"));
	}
	protected AbstractActionNewModelElement(String name) {
		super(Translator.localize(name),ResourceLoaderWrapper.lookupIcon(name));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize(name));
	}
	public Object getTarget() {
		return target;
	}
	public void setTarget(Object theTarget) {
		target = theTarget;
	}
}



