package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.Icon;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewSignal extends AbstractActionNewModelElement {
	public ActionNewSignal() {
		super("button.new-signal");
		putValue(Action.NAME,Translator.localize("button.new-signal"));
		Icon icon = ResourceLoaderWrapper.lookupIcon("SignalSending");
		putValue(Action.SMALL_ICON,icon);
	}
	public void actionPerformed(ActionEvent e) {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isASignalEvent(target)||Model.getFacade().isASendAction(target)||Model.getFacade().isAReception(target)||Model.getFacade().isABehavioralFeature(target)) {
			Object newSig = Model.getCommonBehaviorFactory().buildSignal(target);
			TargetManager.getInstance().setTarget(newSig);
		}else {
			Object ns = null;
			if (Model.getFacade().isANamespace(target)) {
				ns = target;
			}else {
				ns = Model.getFacade().getNamespace(target);
			}
			Object newElement = Model.getCommonBehaviorFactory().createSignal();
			TargetManager.getInstance().setTarget(newElement);
			Model.getCoreHelper().setNamespace(newElement,ns);
		}
		super.actionPerformed(e);
	}
}



