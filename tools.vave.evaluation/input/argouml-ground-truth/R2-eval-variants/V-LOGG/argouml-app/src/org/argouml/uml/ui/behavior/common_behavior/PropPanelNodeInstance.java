package org.argouml.uml.ui.behavior.common_behavior;

import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.UMLContainerResidentListModel;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelNodeInstance extends PropPanelInstance {
	private static final long serialVersionUID = -3391167975804021594l;
	public PropPanelNodeInstance() {
		super("label.node-instance",lookupIcon("NodeInstance"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		addSeparator();
		addField(Translator.localize("label.stimili-sent"),getStimuliSenderScroll());
		addField(Translator.localize("label.stimili-received"),getStimuliReceiverScroll());
		JList resList = new UMLLinkedList(new UMLContainerResidentListModel());
		addField(Translator.localize("label.residents"),new JScrollPane(resList));
		addSeparator();
		AbstractActionAddModelElement2 a = new ActionAddInstanceClassifier(Model.getMetaTypes().getNode());
		JScrollPane classifierScroll = new JScrollPane(new UMLMutableLinkedList(new UMLInstanceClassifierListModel(),a,null,null,true));
		addField(Translator.localize("label.classifiers"),classifierScroll);
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}



