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


public class PropPanelComponentInstance extends PropPanelInstance {
	private static final long serialVersionUID = 7178149693694151459l;
	public PropPanelComponentInstance() {
		super("label.component-instance",lookupIcon("ComponentInstance"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		addSeparator();
		addField(Translator.localize("label.stimili-sent"),getStimuliSenderScroll());
		addField(Translator.localize("label.stimili-received"),getStimuliReceiverScroll());
		JList resList = new UMLLinkedList(new UMLContainerResidentListModel());
		addField(Translator.localize("label.residents"),new JScrollPane(resList));
		addSeparator();
		AbstractActionAddModelElement2 action = new ActionAddInstanceClassifier(Model.getMetaTypes().getComponent());
		JScrollPane classifierScroll = new JScrollPane(new UMLMutableLinkedList(new UMLInstanceClassifierListModel(),action,null,null,true));
		addField(Translator.localize("label.classifiers"),classifierScroll);
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}



