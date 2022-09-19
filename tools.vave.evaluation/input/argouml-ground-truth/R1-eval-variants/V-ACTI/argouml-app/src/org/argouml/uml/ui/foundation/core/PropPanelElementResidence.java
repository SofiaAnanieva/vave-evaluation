package org.argouml.uml.ui.foundation.core;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;


public class PropPanelElementResidence extends PropPanelModelElement {
	public PropPanelElementResidence() {
		super("label.element-residence",lookupIcon("ElementResidence"));
		add(getVisibilityPanel());
		addSeparator();
		addField(Translator.localize("label.container"),getSingleRowScroll(new ElementResidenceContainerListModel()));
		addField(Translator.localize("label.resident"),getSingleRowScroll(new ElementResidenceResidentListModel()));
		addAction(new ActionNavigateContainerElement());
		addAction(getDeleteAction());
	}
}

class ElementResidenceContainerListModel extends UMLModelElementListModel2 {
	public ElementResidenceContainerListModel() {
		super("container");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			removeAllElements();
			addElement(Model.getFacade().getContainer(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isAElementResidence(getTarget());
	}
}

class ElementResidenceResidentListModel extends UMLModelElementListModel2 {
	public ElementResidenceResidentListModel() {
		super("resident");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			removeAllElements();
			addElement(Model.getFacade().getResident(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isAElementResidence(getTarget());
	}
}



