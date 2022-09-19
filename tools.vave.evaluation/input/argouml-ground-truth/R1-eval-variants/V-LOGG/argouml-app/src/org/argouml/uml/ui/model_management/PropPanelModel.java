package org.argouml.uml.ui.model_management;

import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.ActionAddDataType;
import org.argouml.uml.ui.foundation.core.ActionAddEnumeration;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewTagDefinition;


public class PropPanelModel extends PropPanelPackage {
	public PropPanelModel() {
		super("label.model",lookupIcon("Model"));
	}
	@Override protected void placeElements() {
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		add(getVisibilityPanel());
		add(getModifiersPanel());
		addSeparator();
		addField(Translator.localize("label.generalizations"),getGeneralizationScroll());
		addField(Translator.localize("label.specializations"),getSpecializationScroll());
		addSeparator();
		addField(Translator.localize("label.owned-elements"),getOwnedElementsScroll());
		JList importList = new UMLMutableLinkedList(new UMLClassifierPackageImportsListModel(),new ActionAddPackageImport(),null,new ActionRemovePackageImport(),true);
		addField(Translator.localize("label.imported-elements"),new JScrollPane(importList));
		addAction(new ActionNavigateNamespace());
		addAction(new ActionAddPackage());
		addAction(new ActionAddDataType());
		addAction(new ActionAddEnumeration());
		addAction(new ActionNewStereotype());
		addAction(new ActionNewTagDefinition());
		addAction(getDeleteAction());
	}
}



