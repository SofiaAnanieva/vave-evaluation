package org.argouml.uml.ui.behavior.collaborations;

import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelNamespace;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.uml.ui.UMLSingleRowSelector;


public class PropPanelCollaboration extends PropPanelNamespace {
	private static final long serialVersionUID = 5642815840272293391l;
	public PropPanelCollaboration() {
		super("label.collaboration",lookupIcon("Collaboration"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		UMLComboBox2 representedClassifierComboBox = new UMLComboBox2(new UMLCollaborationRepresentedClassifierComboBoxModel(),new ActionSetRepresentedClassifierCollaboration());
		addField(Translator.localize("label.represented-classifier"),new UMLComboBoxNavigator(Translator.localize("label.represented-classifier." + "navigate.tooltip"),representedClassifierComboBox));
		UMLComboBox2 representedOperationComboBox = new UMLComboBox2(new UMLCollaborationRepresentedOperationComboBoxModel(),new ActionSetRepresentedOperationCollaboration());
		addField(Translator.localize("label.represented-operation"),new UMLComboBoxNavigator(Translator.localize("label.represented-operation." + "navigate.tooltip"),representedOperationComboBox));
		addSeparator();
		addField(Translator.localize("label.interaction"),getSingleRowScroll(new UMLCollaborationInteractionListModel()));
		UMLLinkedList constrainingList = new UMLLinkedList(new UMLCollaborationConstrainingElementListModel());
		addField(Translator.localize("label.constraining-elements"),new JScrollPane(constrainingList));
		addSeparator();
		addField(Translator.localize("label.owned-elements"),getOwnedElementsScroll());
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}



