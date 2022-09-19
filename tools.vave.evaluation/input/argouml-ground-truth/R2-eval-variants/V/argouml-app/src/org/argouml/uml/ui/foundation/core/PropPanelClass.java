package org.argouml.uml.ui.foundation.core;

import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelClass extends PropPanelClassifier {
	private static final long serialVersionUID = -8288739384387629966l;
	public PropPanelClass() {
		super("label.class",lookupIcon("Class"));
		addField("label.name",getNameTextField());
		addField("label.namespace",getNamespaceSelector());
		getModifiersPanel().add(new UMLClassActiveCheckBox());
		add(getModifiersPanel());
		add(getVisibilityPanel());
		addSeparator();
		addField("label.client-dependencies",getClientDependencyScroll());
		addField("label.supplier-dependencies",getSupplierDependencyScroll());
		addField("label.generalizations",getGeneralizationScroll());
		addField("label.specializations",getSpecializationScroll());
		addSeparator();
		addField("label.attributes",getAttributeScroll());
		addField("label.association-ends",getAssociationEndScroll());
		addField("label.operations",getOperationScroll());
		addField("label.owned-elements",getOwnedElementsScroll());
		addAction(new ActionNavigateNamespace());
		addAction(new ActionAddAttribute());
		addAction(new ActionAddOperation());
		addAction(getActionNewReception());
		addAction(new ActionNewInnerClass());
		addAction(new ActionNewClass());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}



