package org.argouml.uml.ui.behavior.collaborations;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMultiplicityPanel;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelClassifier;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelClassifierRole extends PropPanelClassifier {
	private static final long serialVersionUID = -5407549104529347513l;
	private UMLMultiplicityPanel multiplicityComboBox;
	public PropPanelClassifierRole() {
		super("label.classifier-role",lookupIcon("ClassifierRole"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		addField(Translator.localize("label.multiplicity"),getMultiplicityComboBox());
		JList baseList = new UMLMutableLinkedList(new UMLClassifierRoleBaseListModel(),ActionAddClassifierRoleBase.SINGLETON,null,ActionRemoveClassifierRoleBase.getInstance(),true);
		addField(Translator.localize("label.base"),new JScrollPane(baseList));
		addSeparator();
		addField(Translator.localize("label.generalizations"),getGeneralizationScroll());
		addField(Translator.localize("label.specializations"),getSpecializationScroll());
		addField(Translator.localize("label.associationrole-ends"),getAssociationEndScroll());
		addSeparator();
		JList availableContentsList = new UMLLinkedList(new UMLClassifierRoleAvailableContentsListModel());
		addField(Translator.localize("label.available-contents"),new JScrollPane(availableContentsList));
		JList availableFeaturesList = new UMLLinkedList(new UMLClassifierRoleAvailableFeaturesListModel());
		addField(Translator.localize("label.available-features"),new JScrollPane(availableFeaturesList));
		addAction(new ActionNavigateContainerElement());
		addAction(getActionNewReception());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
	protected JPanel getMultiplicityComboBox() {
		if (multiplicityComboBox == null) {
			multiplicityComboBox = new UMLMultiplicityPanel();
		}
		return multiplicityComboBox;
	}
}



