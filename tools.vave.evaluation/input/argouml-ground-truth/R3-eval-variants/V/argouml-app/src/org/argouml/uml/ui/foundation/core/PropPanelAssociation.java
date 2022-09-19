package org.argouml.uml.ui.foundation.core;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.uml.ui.foundation.core.PropPanelRelationship;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementRootCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementLeafCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementAbstractCheckBox;


public class PropPanelAssociation extends PropPanelRelationship {
	private static final long serialVersionUID = 4272135235664638209l;
	private JScrollPane assocEndScroll;
	private JScrollPane associationRoleScroll;
	private JScrollPane linksScroll;
	private JPanel modifiersPanel;
	public PropPanelAssociation() {
		this("label.association");
		addField("label.name",getNameTextField());
		addField("label.namespace",getNamespaceSelector());
		add(modifiersPanel);
		addSeparator();
		addField("label.connections",assocEndScroll);
		addSeparator();
		addField("label.association-roles",associationRoleScroll);
		addField("label.association-links",linksScroll);
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
	protected PropPanelAssociation(String title) {
		super(title,lookupIcon("Association"));
		initialize();
		JList assocEndList = new UMLLinkedList(new UMLAssociationConnectionListModel());
		assocEndScroll = new JScrollPane(assocEndList);
		JList baseList = new UMLLinkedList(new UMLAssociationAssociationRoleListModel());
		associationRoleScroll = new JScrollPane(baseList);
		JList linkList = new UMLLinkedList(new UMLAssociationLinkListModel());
		linksScroll = new JScrollPane(linkList);
	}
	private void initialize() {
		modifiersPanel = createBorderPanel(Translator.localize("label.modifiers"));
		modifiersPanel.add(new UMLGeneralizableElementAbstractCheckBox());
		modifiersPanel.add(new UMLGeneralizableElementLeafCheckBox());
		modifiersPanel.add(new UMLGeneralizableElementRootCheckBox());
	}
}



