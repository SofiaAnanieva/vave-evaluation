package org.argouml.uml.ui.foundation.core;

import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelAssociationClass extends PropPanelClassifier {
	private static final long serialVersionUID = -7620821534700927917l;
	private JScrollPane attributeScroll;
	private JScrollPane operationScroll;
	private JScrollPane assocEndScroll;
	private static UMLClassAttributeListModel attributeListModel = new UMLClassAttributeListModel();
	private static UMLClassOperationListModel operationListModel = new UMLClassOperationListModel();
	public PropPanelAssociationClass() {
		super("label.association-class",lookupIcon("AssociationClass"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		getModifiersPanel().add(new UMLClassActiveCheckBox());
		add(getModifiersPanel());
		add(getVisibilityPanel());
		addSeparator();
		addField(Translator.localize("label.client-dependencies"),getClientDependencyScroll());
		addField(Translator.localize("label.supplier-dependencies"),getSupplierDependencyScroll());
		addField(Translator.localize("label.generalizations"),getGeneralizationScroll());
		addField(Translator.localize("label.specializations"),getSpecializationScroll());
		JList assocEndList = new UMLLinkedList(new UMLAssociationConnectionListModel());
		assocEndScroll = new JScrollPane(assocEndList);
		addField(Translator.localize("label.connections"),assocEndScroll);
		addSeparator();
		addField(Translator.localize("label.attributes"),getAttributeScroll());
		JList connections = new UMLLinkedList(new UMLClassifierAssociationEndListModel());
		JScrollPane connectionsScroll = new JScrollPane(connections);
		addField(Translator.localize("label.association-ends"),connectionsScroll);
		addField(Translator.localize("label.operations"),getOperationScroll());
		addField(Translator.localize("label.owned-elements"),getOwnedElementsScroll());
		addAction(new ActionNavigateNamespace());
		addAction(new ActionAddAttribute());
		addAction(new ActionAddOperation());
		addAction(getActionNewReception());
		addAction(new ActionNewInnerClass());
		addAction(new ActionNewClass());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
	@Override public JScrollPane getOperationScroll() {
		if (operationScroll == null) {
			JList list = new UMLLinkedList(operationListModel);
			operationScroll = new JScrollPane(list);
		}
		return operationScroll;
	}
	@Override public JScrollPane getAttributeScroll() {
		if (attributeScroll == null) {
			JList list = new UMLLinkedList(attributeListModel);
			attributeScroll = new JScrollPane(list);
		}
		return attributeScroll;
	}
}



