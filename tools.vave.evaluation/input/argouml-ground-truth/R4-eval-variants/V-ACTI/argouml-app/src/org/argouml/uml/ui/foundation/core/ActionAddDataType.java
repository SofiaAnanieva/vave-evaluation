package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionAddDataType extends AbstractActionNewModelElement {
	public ActionAddDataType() {
		super("button.new-datatype");
		putValue(Action.NAME,Translator.localize("button.new-datatype"));
	}
	public void actionPerformed(ActionEvent e) {
		Object target = TargetManager.getInstance().getModelTarget();
		Object ns = null;
		if (Model.getFacade().isANamespace(target))ns = target;
		if (Model.getFacade().isAParameter(target))if (Model.getFacade().getBehavioralFeature(target) != null)target = Model.getFacade().getBehavioralFeature(target);
		if (Model.getFacade().isAFeature(target))if (Model.getFacade().getOwner(target) != null)target = Model.getFacade().getOwner(target);
		if (Model.getFacade().isAEvent(target))ns = Model.getFacade().getNamespace(target);
		if (Model.getFacade().isAClassifier(target))ns = Model.getFacade().getNamespace(target);
		if (Model.getFacade().isAAssociationEnd(target)) {
			target = Model.getFacade().getAssociation(target);
			ns = Model.getFacade().getNamespace(target);
		}
		Object newDt = Model.getCoreFactory().buildDataType("",ns);
		TargetManager.getInstance().setTarget(newDt);
		super.actionPerformed(e);
	}
}



