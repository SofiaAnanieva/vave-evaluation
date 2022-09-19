package org.argouml.uml.ui.behavior.common_behavior;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLTextArea2;
import org.argouml.uml.ui.foundation.core.PropPanelFeature;
import org.argouml.uml.ui.foundation.core.UMLBehavioralFeatureQueryCheckBox;
import org.argouml.uml.ui.foundation.core.UMLFeatureOwnerScopeCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementAbstractCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementLeafCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementRootCheckBox;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelReception extends PropPanelFeature {
	private static final long serialVersionUID = -8572743081899344540l;
	private JPanel modifiersPanel;
	public PropPanelReception() {
		super("label.reception",lookupIcon("Reception"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.owner"),getOwnerScroll());
		add(getVisibilityPanel());
		modifiersPanel = createBorderPanel(Translator.localize("label.modifiers"));
		modifiersPanel.add(new UMLGeneralizableElementAbstractCheckBox());
		modifiersPanel.add(new UMLGeneralizableElementLeafCheckBox());
		modifiersPanel.add(new UMLGeneralizableElementRootCheckBox());
		modifiersPanel.add(new UMLBehavioralFeatureQueryCheckBox());
		modifiersPanel.add(new UMLFeatureOwnerScopeCheckBox());
		add(modifiersPanel);
		addSeparator();
		addField(Translator.localize("label.signal"),new UMLReceptionSignalComboBox(this,new UMLReceptionSignalComboBoxModel()));
		UMLTextArea2 specText = new UMLTextArea2(new UMLReceptionSpecificationDocument());
		specText.setLineWrap(true);
		specText.setRows(5);
		specText.setFont(LookAndFeelMgr.getInstance().getStandardFont());
		JScrollPane specificationScroll = new JScrollPane(specText);
		addField(Translator.localize("label.specification"),specificationScroll);
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}



