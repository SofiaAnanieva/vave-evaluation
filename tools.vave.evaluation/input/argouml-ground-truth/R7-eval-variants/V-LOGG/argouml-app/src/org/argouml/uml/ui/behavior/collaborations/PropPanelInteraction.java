package org.argouml.uml.ui.behavior.collaborations;

import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateContext;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.uml.ui.UMLSingleRowSelector;


public class PropPanelInteraction extends PropPanelModelElement {
	private static final long serialVersionUID = 8965284617441796326l;
	public PropPanelInteraction() {
		super("label.interaction-title",lookupIcon("Interaction"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		addField(Translator.localize("label.context"),getSingleRowScroll(new UMLInteractionContextListModel()));
		addSeparator();
		JList messagesList = new UMLLinkedList(new UMLInteractionMessagesListModel());
		JScrollPane messagesScroll = new JScrollPane(messagesList);
		addField(Translator.localize("label.messages"),messagesScroll);
		addAction(new ActionNavigateContext());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}



