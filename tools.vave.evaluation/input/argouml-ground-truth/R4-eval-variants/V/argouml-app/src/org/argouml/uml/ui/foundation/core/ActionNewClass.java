package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewClass extends AbstractActionNewModelElement {
	public ActionNewClass() {
		super("button.new-class");
		putValue(Action.NAME,Translator.localize("button.new-class"));
	}
	public void actionPerformed(ActionEvent e) {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isAClassifier(target)) {
			Object classifier = target;
			Object ns = Model.getFacade().getNamespace(classifier);
			if (ns != null) {
				Object peer = Model.getCoreFactory().buildClass(ns);
				TargetManager.getInstance().setTarget(peer);
				super.actionPerformed(e);
			}
		}
	}
}



