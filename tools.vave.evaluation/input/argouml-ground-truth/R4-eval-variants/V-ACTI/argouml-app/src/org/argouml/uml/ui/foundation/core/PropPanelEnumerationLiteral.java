package org.argouml.uml.ui.foundation.core;

import javax.swing.DefaultListModel;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;


public class PropPanelEnumerationLiteral extends PropPanelModelElement {
	private static final long serialVersionUID = 1486642919681744144l;
	public PropPanelEnumerationLiteral() {
		super("label.enumeration-literal",lookupIcon("EnumerationLiteral"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.enumeration"),getSingleRowScroll(new EnumerationListModel()));
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionAddLiteral());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}

class EnumerationListModel extends DefaultListModel implements TargetListener {
	public EnumerationListModel() {
		super();
		setTarget(TargetManager.getInstance().getModelTarget());
		TargetManager.getInstance().addTargetListener(this);
	}
	public void targetAdded(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetRemoved(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetSet(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	private void setTarget(Object t) {
		removeAllElements();
		if (Model.getFacade().isAEnumerationLiteral(t)) {
			addElement(Model.getFacade().getEnumeration(t));
		}
	}
}



