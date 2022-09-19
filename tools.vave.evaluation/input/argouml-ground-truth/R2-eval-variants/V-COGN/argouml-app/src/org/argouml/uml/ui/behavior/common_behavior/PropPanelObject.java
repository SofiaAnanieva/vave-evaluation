package org.argouml.uml.ui.behavior.common_behavior;

import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelObject extends PropPanelInstance {
	public PropPanelObject() {
		super("label.object",lookupIcon("Object"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		addSeparator();
		addField(Translator.localize("label.stimili-sent"),getStimuliSenderScroll());
		addField(Translator.localize("label.stimili-received"),getStimuliReceiverScroll());
		addSeparator();
		AbstractActionAddModelElement2 action = new ActionAddInstanceClassifier(Model.getMetaTypes().getClassifier());
		JScrollPane classifierScroll = new JScrollPane(new UMLMutableLinkedList(new UMLInstanceClassifierListModel(),action,null,null,true));
		addField(Translator.localize("label.classifiers"),classifierScroll);
		addAction(new ActionNavigateNamespace());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
	private static final long serialVersionUID = 3594423150761388537l;
}



