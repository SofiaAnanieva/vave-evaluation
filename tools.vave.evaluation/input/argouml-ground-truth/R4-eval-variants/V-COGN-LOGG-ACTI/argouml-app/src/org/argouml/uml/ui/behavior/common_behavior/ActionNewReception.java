package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewReception extends AbstractActionNewModelElement {
	public ActionNewReception() {
		super("button.new-reception");
		putValue(Action.NAME,Translator.localize("button.new-reception"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object classifier = TargetManager.getInstance().getModelTarget();
		if (!Model.getFacade().isAClassifier(classifier)) {
			throw new IllegalArgumentException("Argument classifier is null or not a classifier. Got: " + classifier);
		}
		Object reception = Model.getCommonBehaviorFactory().buildReception(classifier);
		TargetManager.getInstance().setTarget(reception);
	}
}



