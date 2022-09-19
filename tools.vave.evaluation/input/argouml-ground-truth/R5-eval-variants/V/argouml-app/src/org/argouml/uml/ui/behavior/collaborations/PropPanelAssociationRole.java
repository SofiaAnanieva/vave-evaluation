package org.argouml.uml.ui.behavior.collaborations;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.diagram.ui.ActionAddMessage;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelAssociation;


public class PropPanelAssociationRole extends PropPanelAssociation {
	private static final long serialVersionUID = 7693759162647306494l;
	public PropPanelAssociationRole() {
		super("label.association-role-title");
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		JComboBox baseComboBox = new UMLComboBox2(new UMLAssociationRoleBaseComboBoxModel(),new ActionSetAssociationRoleBase());
		addField(Translator.localize("label.base"),new UMLComboBoxNavigator(Translator.localize("label.association.navigate.tooltip"),baseComboBox));
		addSeparator();
		JList assocEndList = new UMLLinkedList(new UMLAssociationRoleAssociationEndRoleListModel());
		assocEndList.setVisibleRowCount(2);
		addField(Translator.localize("label.associationrole-ends"),new JScrollPane(assocEndList));
		JList messageList = new UMLLinkedList(new UMLAssociationRoleMessageListModel());
		addField(Translator.localize("label.messages"),new JScrollPane(messageList));
		addAction(new ActionNavigateContainerElement());
		addAction(ActionAddMessage.getTargetFollower());
		addAction(getDeleteAction());
	}
}



