package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.undo.UndoableAction;


@UmlModelMutator public class ActionAddEnumerationLiteral extends UndoableAction {
	private static final long serialVersionUID = -1206083856173080229l;
	public ActionAddEnumerationLiteral() {
		super(Translator.localize("button.new-enumeration-literal"),ResourceLoaderWrapper.lookupIcon("button.new-enumeration-literal"));
	}
	public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		Object target = TargetManager.getInstance().getModelTarget();
		Object enumeration;
		if (Model.getFacade().isAEnumeration(target)) {
			enumeration = target;
		}else if (Model.getFacade().isAEnumerationLiteral(target)) {
			enumeration = Model.getFacade().getEnumeration(target);
		}else {
			return;
		}
		Object oper = Model.getCoreFactory().buildEnumerationLiteral("anon",enumeration);
		TargetManager.getInstance().setTarget(oper);
	}
}



