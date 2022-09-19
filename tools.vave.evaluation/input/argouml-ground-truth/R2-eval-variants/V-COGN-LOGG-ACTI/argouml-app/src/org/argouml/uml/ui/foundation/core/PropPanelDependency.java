package org.argouml.uml.ui.foundation.core;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.uml.ui.foundation.core.PropPanelRelationship;


public class PropPanelDependency extends PropPanelRelationship {
	private static final long serialVersionUID = 3665986064546532722l;
	private JScrollPane supplierScroll;
	private JScrollPane clientScroll;
	public PropPanelDependency() {
		this("label.dependency",lookupIcon("Dependency"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		addSeparator();
		addField(Translator.localize("label.suppliers"),supplierScroll);
		addField(Translator.localize("label.clients"),clientScroll);
		addAction(new ActionNavigateNamespace());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
	protected PropPanelDependency(String name,ImageIcon icon) {
		super(name,icon);
		JList supplierList = new UMLLinkedList(new UMLDependencySupplierListModel(),true);
		supplierScroll = new JScrollPane(supplierList);
		JList clientList = new UMLLinkedList(new UMLDependencyClientListModel(),true);
		clientScroll = new JScrollPane(clientList);
	}
	protected JScrollPane getSupplierScroll() {
		return supplierScroll;
	}
	protected JScrollPane getClientScroll() {
		return clientScroll;
	}
}



