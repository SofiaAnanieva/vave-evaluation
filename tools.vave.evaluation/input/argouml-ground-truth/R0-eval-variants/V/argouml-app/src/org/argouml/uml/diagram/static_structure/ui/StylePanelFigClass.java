package org.argouml.uml.diagram.static_structure.ui;

import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.JCheckBox;
import org.argouml.i18n.Translator;
import org.argouml.ui.StylePanelFigNodeModelElement;
import org.argouml.uml.diagram.AttributesCompartmentContainer;
import org.argouml.uml.diagram.OperationsCompartmentContainer;


public class StylePanelFigClass extends StylePanelFigNodeModelElement {
	private JCheckBox attrCheckBox = new JCheckBox(Translator.localize("checkbox.attributes"));
	private JCheckBox operCheckBox = new JCheckBox(Translator.localize("checkbox.operations"));
	private boolean refreshTransaction;
	public StylePanelFigClass() {
		super();
		addToDisplayPane(attrCheckBox);
		addToDisplayPane(operCheckBox);
		attrCheckBox.setSelected(false);
		operCheckBox.setSelected(false);
		attrCheckBox.addItemListener(this);
		operCheckBox.addItemListener(this);
	}
	public void refresh(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();
		if (propertyName.equals("bounds")) {
			refresh();
		}
	}
	public void refresh() {
		refreshTransaction = true;
		super.refresh();
		AttributesCompartmentContainer ac = (AttributesCompartmentContainer) getPanelTarget();
		attrCheckBox.setSelected(ac.isAttributesVisible());
		OperationsCompartmentContainer oc = (OperationsCompartmentContainer) getPanelTarget();
		operCheckBox.setSelected(oc.isOperationsVisible());
		refreshTransaction = false;
	}
	public void itemStateChanged(ItemEvent e) {
		if (!refreshTransaction) {
			Object src = e.getSource();
			if (src == attrCheckBox) {
				((AttributesCompartmentContainer) getPanelTarget()).setAttributesVisible(attrCheckBox.isSelected());
			}else if (src == operCheckBox) {
				((OperationsCompartmentContainer) getPanelTarget()).setOperationsVisible(operCheckBox.isSelected());
			}else {
				super.itemStateChanged(e);
			}
		}
	}
	private static final long serialVersionUID = 4587367369055254943l;
}



