package org.argouml.uml.ui.foundation.core;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.core.UMLFeatureOwnerScopeCheckBox;


public abstract class PropPanelFeature extends PropPanelModelElement {
	private UMLFeatureOwnerScopeCheckBox ownerScopeCheckbox;
	private JPanel ownerScroll;
	private static UMLFeatureOwnerListModel ownerListModel;
	private JPanel visibilityPanel;
	protected PropPanelFeature(String name,ImageIcon icon) {
		super(name,icon);
	}
	public JPanel getOwnerScroll() {
		if (ownerScroll == null) {
			if (ownerListModel == null) {
				ownerListModel = new UMLFeatureOwnerListModel();
			}
			ownerScroll = getSingleRowScroll(ownerListModel);
		}
		return ownerScroll;
	}
	public UMLFeatureOwnerScopeCheckBox getOwnerScopeCheckbox() {
		if (ownerScopeCheckbox == null) {
			ownerScopeCheckbox = new UMLFeatureOwnerScopeCheckBox();
		}
		return ownerScopeCheckbox;
	}
	protected JPanel getVisibilityPanel() {
		if (visibilityPanel == null) {
			visibilityPanel = new UMLModelElementVisibilityRadioButtonPanel(Translator.localize("label.visibility"),true);
		}
		return visibilityPanel;
	}
}



