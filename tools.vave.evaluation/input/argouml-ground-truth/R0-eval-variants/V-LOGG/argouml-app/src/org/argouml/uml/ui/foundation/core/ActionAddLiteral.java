package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.Icon;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionAddLiteral extends AbstractActionNewModelElement {
	public ActionAddLiteral() {
		super("button.new-enumeration-literal");
		putValue(Action.NAME,Translator.localize("button.new-enumeration-literal"));
		Icon icon = ResourceLoaderWrapper.lookupIcon("EnumerationLiteral");
		putValue(Action.SMALL_ICON,icon);
	}
	public void actionPerformed(ActionEvent e) {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isAEnumerationLiteral(target)) {
			target = Model.getFacade().getEnumeration(target);
		}
		if (Model.getFacade().isAClassifier(target)) {
			Object el = Model.getCoreFactory().buildEnumerationLiteral("",target);
			TargetManager.getInstance().setTarget(el);
			super.actionPerformed(e);
		}
	}
}



