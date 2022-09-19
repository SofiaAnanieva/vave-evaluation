package org.argouml.uml.ui.behavior.collaborations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;


public class ActionAddClassifierRoleBase extends AbstractActionAddModelElement2 {
	public static final ActionAddClassifierRoleBase SINGLETON = new ActionAddClassifierRoleBase();
	protected ActionAddClassifierRoleBase() {
		super();
	}
	protected List getChoices() {
		List vec = new ArrayList();
		return vec;
	}
	protected List getSelected() {
		List vec = new ArrayList();
		vec.addAll(Model.getFacade().getBases(getTarget()));
		return vec;
	}
	protected String getDialogTitle() {
		return Translator.localize("dialog.title.add-bases");
	}
	protected void doIt(Collection selected) {
	}
}



