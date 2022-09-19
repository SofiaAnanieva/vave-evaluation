package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelStateMachine extends PropPanelModelElement {
	private static final long serialVersionUID = -2157218581140487530l;
	public PropPanelStateMachine() {
		this("label.statemachine",lookupIcon("StateMachine"));
	}
	public PropPanelStateMachine(String name,ImageIcon icon) {
		super(name,icon);
		initialize();
	}
	protected void initialize() {
		addField("label.name",getNameTextField());
		addField("label.namespace",getNamespaceSelector());
		UMLComboBox2 contextComboBox = new UMLComboBox2(getContextComboBoxModel(),ActionSetContextStateMachine.getInstance());
		addField("label.context",new UMLComboBoxNavigator(Translator.localize("label.context.navigate.tooltip"),contextComboBox));
		JList topList = new UMLLinkedList(new UMLStateMachineTopListModel());
		addField("label.top-state",new JScrollPane(topList));
		addSeparator();
		JList transitionList = new UMLLinkedList(new UMLStateMachineTransitionListModel());
		addField("label.transition",new JScrollPane(transitionList));
		JList submachineStateList = new UMLLinkedList(new UMLStateMachineSubmachineStateListModel());
		addField("label.submachinestate",new JScrollPane(submachineStateList));
		addAction(new ActionNavigateNamespace());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
	protected UMLComboBoxModel2 getContextComboBoxModel() {
		return new UMLStateMachineContextComboBoxModel();
	}
}



