package org.argouml.cognitive.ui;

import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.ToDoItem;
import org.argouml.i18n.Translator;
import org.argouml.ui.UndoableAction;


public abstract class ToDoItemAction extends UndoableAction {
	private Object rememberedTarget = null;
	public ToDoItemAction(String name,boolean hasIcon) {
		super(Translator.localize(name),hasIcon?ResourceLoaderWrapper.lookupIcon(name):null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize(name));
	}
	protected Object getRememberedTarget() {
		return rememberedTarget;
	}
	public void updateEnabled(Object target) {
		if (target == null) {
			setEnabled(false);
			return;
		}
		rememberedTarget = target;
		setEnabled(isEnabled(target));
	}
	public boolean isEnabled(Object target) {
		return target instanceof ToDoItem;
	}
}



