package org.argouml.uml.ui.foundation.core;

import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelNode extends PropPanelClassifier {
	private static final long serialVersionUID = 2681345252220104772l;
	public PropPanelNode() {
		super("label.node",lookupIcon("Node"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		add(getModifiersPanel());
		addSeparator();
		addField("Generalizations:",getGeneralizationScroll());
		addField("Specializations:",getSpecializationScroll());
		addSeparator();
		JList resList = new UMLLinkedList(new UMLNodeDeployedComponentListModel());
		addField(Translator.localize("label.deployedcomponents"),new JScrollPane(resList));
		addAction(new ActionNavigateContainerElement());
		addAction(getActionNewReception());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}

class UMLNodeDeployedComponentListModel extends UMLModelElementListModel2 {
	private static final long serialVersionUID = -7137518645846584922l;
	public UMLNodeDeployedComponentListModel() {
		super("deployedComponent");
	}
	protected void buildModelList() {
		if (Model.getFacade().isANode(getTarget())) {
			setAllElements(Model.getFacade().getDeployedComponents(getTarget()));
		}
	}
	protected boolean isValidElement(Object o) {
		return(Model.getFacade().isAComponent(o));
	}
}



