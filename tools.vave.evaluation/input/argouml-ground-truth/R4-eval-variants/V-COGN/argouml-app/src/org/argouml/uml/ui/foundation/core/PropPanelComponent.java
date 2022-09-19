package org.argouml.uml.ui.foundation.core;

import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelComponent extends PropPanelClassifier {
	private static final long serialVersionUID = 1551050121647608478l;
	public PropPanelComponent() {
		super("label.component",lookupIcon("Component"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		add(getModifiersPanel());
		addSeparator();
		addField(Translator.localize("label.generalizations"),getGeneralizationScroll());
		addField(Translator.localize("label.specializations"),getSpecializationScroll());
		addSeparator();
		addField(Translator.localize("label.client-dependencies"),getClientDependencyScroll());
		addField(Translator.localize("label.supplier-dependencies"),getSupplierDependencyScroll());
		JList resList = new UMLLinkedList(new UMLComponentResidentListModel());
		addField(Translator.localize("label.residents"),new JScrollPane(resList));
		addAction(new ActionNavigateNamespace());
		addAction(getActionNewReception());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}



