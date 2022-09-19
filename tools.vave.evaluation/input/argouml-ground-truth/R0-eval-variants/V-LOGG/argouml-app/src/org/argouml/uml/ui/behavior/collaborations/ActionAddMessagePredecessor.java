package org.argouml.uml.ui.behavior.collaborations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;


public class ActionAddMessagePredecessor extends AbstractActionAddModelElement2 {
	private static final ActionAddMessagePredecessor SINGLETON = new ActionAddMessagePredecessor();
	protected ActionAddMessagePredecessor() {
		super();
	}
	protected List getChoices() {
		if (getTarget() == null) {
			return Collections.EMPTY_LIST;
		}
		List vec = new ArrayList();
		return vec;
	}
	protected List getSelected() {
		if (getTarget() == null) {
			throw new IllegalStateException("getSelected may not be called with null target");
		}
		List vec = new ArrayList();
		vec.addAll(Model.getFacade().getPredecessors(getTarget()));
		return vec;
	}
	protected String getDialogTitle() {
		return Translator.localize("dialog.add-predecessors");
	}
	protected void doIt(Collection selected) {
		if (getTarget() == null)throw new IllegalStateException("doIt may not be called with null target");
	}
	public static ActionAddMessagePredecessor getInstance() {
		return SINGLETON;
	}
}



