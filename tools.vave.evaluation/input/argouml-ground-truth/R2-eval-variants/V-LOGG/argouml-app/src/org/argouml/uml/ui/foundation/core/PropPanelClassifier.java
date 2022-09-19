package org.argouml.uml.ui.foundation.core;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ScrollList;
import org.argouml.uml.ui.UMLDerivedCheckBox;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewReception;
import org.argouml.uml.ui.UMLDerivedCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementAbstractCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementLeafCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementRootCheckBox;


public abstract class PropPanelClassifier extends PropPanelNamespace {
	private JPanel modifiersPanel;
	private ActionNewReception actionNewReception = new ActionNewReception();
	private JScrollPane generalizationScroll;
	private JScrollPane specializationScroll;
	private JScrollPane featureScroll;
	private JScrollPane createActionScroll;
	private JScrollPane powerTypeRangeScroll;
	private JScrollPane associationEndScroll;
	private JScrollPane attributeScroll;
	private JScrollPane operationScroll;
	private static UMLGeneralizableElementGeneralizationListModel generalizationListModel = new UMLGeneralizableElementGeneralizationListModel();
	private static UMLGeneralizableElementSpecializationListModel specializationListModel = new UMLGeneralizableElementSpecializationListModel();
	private static UMLClassifierFeatureListModel featureListModel = new UMLClassifierFeatureListModel();
	private static UMLClassifierCreateActionListModel createActionListModel = new UMLClassifierCreateActionListModel();
	private static UMLClassifierPowertypeRangeListModel powertypeRangeListModel = new UMLClassifierPowertypeRangeListModel();
	private static UMLClassifierAssociationEndListModel associationEndListModel = new UMLClassifierAssociationEndListModel();
	private static UMLClassAttributeListModel attributeListModel = new UMLClassAttributeListModel();
	private static UMLClassOperationListModel operationListModel = new UMLClassOperationListModel();
	public PropPanelClassifier(String name,ImageIcon icon) {
		super(name,icon);
		initialize();
	}
	private void initialize() {
		modifiersPanel = createBorderPanel(Translator.localize("label.modifiers"));
		modifiersPanel.add(new UMLGeneralizableElementAbstractCheckBox());
		modifiersPanel.add(new UMLGeneralizableElementLeafCheckBox());
		modifiersPanel.add(new UMLGeneralizableElementRootCheckBox());
		modifiersPanel.add(new UMLDerivedCheckBox());
	}
	public JScrollPane getAssociationEndScroll() {
		if (associationEndScroll == null) {
			associationEndScroll = new ScrollList(associationEndListModel);
		}
		return associationEndScroll;
	}
	public JScrollPane getCreateActionScroll() {
		if (createActionScroll == null) {
			createActionScroll = new ScrollList(createActionListModel);
		}
		return createActionScroll;
	}
	public JScrollPane getFeatureScroll() {
		if (featureScroll == null) {
			featureScroll = new ScrollList(featureListModel,true,false);
		}
		return featureScroll;
	}
	public JScrollPane getGeneralizationScroll() {
		if (generalizationScroll == null) {
			generalizationScroll = new ScrollList(generalizationListModel);
		}
		return generalizationScroll;
	}
	public JScrollPane getPowerTypeRangeScroll() {
		if (powerTypeRangeScroll == null) {
			powerTypeRangeScroll = new ScrollList(powertypeRangeListModel);
		}
		return powerTypeRangeScroll;
	}
	public JScrollPane getSpecializationScroll() {
		if (specializationScroll == null) {
			specializationScroll = new ScrollList(specializationListModel);
		}
		return specializationScroll;
	}
	public JScrollPane getAttributeScroll() {
		if (attributeScroll == null) {
			attributeScroll = new ScrollList(attributeListModel,true,false);
		}
		return attributeScroll;
	}
	public JScrollPane getOperationScroll() {
		if (operationScroll == null) {
			operationScroll = new ScrollList(operationListModel,true,false);
		}
		return operationScroll;
	}
	protected ActionNewReception getActionNewReception() {
		return actionNewReception;
	}
	protected JPanel getModifiersPanel() {
		return modifiersPanel;
	}
}



