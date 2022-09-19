package org.argouml.uml.ui.foundation.core;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.UMLCheckBox2;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLMultiplicityPanel;
import org.argouml.uml.ui.UMLRadioButtonPanel;
import org.argouml.uml.ui.foundation.core.PropPanelFeature;


public class PropPanelStructuralFeature extends PropPanelFeature {
	private JPanel multiplicityComboBox;
	private UMLComboBox2 typeComboBox;
	private UMLRadioButtonPanel changeabilityRadioButtonPanel;
	private UMLCheckBox2 targetScopeCheckBox;
	private static UMLStructuralFeatureTypeComboBoxModel typeComboBoxModel;
	protected PropPanelStructuralFeature(String name,ImageIcon icon) {
		super(name,icon);
	}
	public JPanel getMultiplicityComboBox() {
		if (multiplicityComboBox == null) {
			multiplicityComboBox = new UMLMultiplicityPanel();
		}
		return multiplicityComboBox;
	}
	public UMLComboBox2 getTypeComboBox() {
		if (typeComboBox == null) {
			if (typeComboBoxModel == null) {
				typeComboBoxModel = new UMLStructuralFeatureTypeComboBoxModel();
			}
			typeComboBox = new UMLComboBox2(typeComboBoxModel,ActionSetStructuralFeatureType.getInstance());
		}
		return typeComboBox;
	}
	public UMLRadioButtonPanel getChangeabilityRadioButtonPanel() {
		if (changeabilityRadioButtonPanel == null) {
			changeabilityRadioButtonPanel = new UMLStructuralFeatureChangeabilityRadioButtonPanel(Translator.localize("label.changeability"),true);
		}
		return changeabilityRadioButtonPanel;
	}
	@Deprecated public UMLCheckBox2 getTargetScopeCheckBox() {
		if (targetScopeCheckBox == null) {
			targetScopeCheckBox = new UMLStructuralFeatureTargetScopeCheckBox();
		}
		return targetScopeCheckBox;
	}
}



