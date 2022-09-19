package org.argouml.uml.ui.foundation.core;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateAssociation;
import org.argouml.uml.ui.ActionNavigateOppositeAssocEnd;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMultiplicityPanel;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.UMLSingleRowSelector;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;


public class PropPanelAssociationEnd extends PropPanelModelElement {
	private static final long serialVersionUID = 9119453587506578751l;
	private JComboBox typeCombobox;
	private JPanel associationScroll;
	private UMLMultiplicityPanel multiplicityComboBox;
	private JCheckBox navigabilityCheckBox;
	private JCheckBox orderingCheckBox;
	private JCheckBox targetScopeCheckBox;
	private JPanel aggregationRadioButtonpanel;
	private JPanel changeabilityRadioButtonpanel;
	private JPanel visibilityRadioButtonPanel;
	private JScrollPane specificationScroll;
	private JScrollPane qualifiersScroll;
	private String associationLabel;
	protected PropPanelAssociationEnd(String name,ImageIcon icon) {
		super(name,icon);
	}
	public PropPanelAssociationEnd() {
		super("label.association-end",lookupIcon("AssociationEnd"));
		associationLabel = Translator.localize("label.association");
		createControls();
		positionStandardControls();
		positionControls();
	}
	protected void createControls() {
		typeCombobox = new UMLComboBox2(new UMLAssociationEndTypeComboBoxModel(),ActionSetAssociationEndType.getInstance(),true);
		associationScroll = new UMLSingleRowSelector(new UMLAssociationEndAssociationListModel());
		navigabilityCheckBox = new UMLAssociationEndNavigableCheckBox();
		orderingCheckBox = new UMLAssociationEndOrderingCheckBox();
		targetScopeCheckBox = new UMLAssociationEndTargetScopeCheckbox();
		aggregationRadioButtonpanel = new UMLAssociationEndAggregationRadioButtonPanel("label.aggregation",true);
		changeabilityRadioButtonpanel = new UMLAssociationEndChangeabilityRadioButtonPanel("label.changeability",true);
		visibilityRadioButtonPanel = new UMLModelElementVisibilityRadioButtonPanel("label.visibility",true);
		specificationScroll = new JScrollPane(new UMLMutableLinkedList(new UMLAssociationEndSpecificationListModel(),ActionAddAssociationSpecification.getInstance(),null,null,true));
		qualifiersScroll = new JScrollPane(new UMLLinkedList(new UMLAssociationEndQualifiersListModel()));
	}
	protected void positionStandardControls() {
		addField(Translator.localize("label.name"),getNameTextField());
	}
	protected void positionControls() {
		addField(associationLabel,associationScroll);
		addField("label.type",typeCombobox);
		addField("label.multiplicity",getMultiplicityComboBox());
		addSeparator();
		JPanel panel = createBorderPanel("label.modifiers");
		panel.add(navigabilityCheckBox);
		panel.add(orderingCheckBox);
		panel.add(targetScopeCheckBox);
		panel.setVisible(true);
		add(panel);
		addField("label.specification",specificationScroll);
		addField("label.qualifiers",qualifiersScroll);
		addSeparator();
		add(aggregationRadioButtonpanel);
		add(changeabilityRadioButtonpanel);
		add(visibilityRadioButtonPanel);
		addAction(new ActionNavigateAssociation());
		addAction(new ActionNavigateOppositeAssocEnd());
		addAction(new ActionAddAttribute(),Translator.localize("button.new-qualifier"));
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
	protected void setAssociationLabel(String label) {
		associationLabel = label;
	}
	protected JPanel getMultiplicityComboBox() {
		if (multiplicityComboBox == null) {
			multiplicityComboBox = new UMLMultiplicityPanel();
		}
		return multiplicityComboBox;
	}
}



