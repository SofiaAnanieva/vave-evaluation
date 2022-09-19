package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.foundation.core.PropPanelAssociationEnd;
import org.argouml.uml.ui.UMLSingleRowSelector;
import org.argouml.uml.ui.behavior.collaborations.UMLAssociationEndRoleBaseListModel;


public class PropPanelAssociationEndRole extends PropPanelAssociationEnd {
	public PropPanelAssociationEndRole() {
		super("label.association-end-role",lookupIcon("AssociationEndRole"));
		setAssociationLabel(Translator.localize("label.association-role"));
		createControls();
		positionStandardControls();
		positionControls();
	}
	@Override protected void positionControls() {
		addField(Translator.localize("label.base"),getSingleRowScroll(new UMLAssociationEndRoleBaseListModel()));
		super.positionControls();
	}
}



