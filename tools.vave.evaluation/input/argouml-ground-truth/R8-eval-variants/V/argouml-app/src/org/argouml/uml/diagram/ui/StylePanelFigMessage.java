package org.argouml.uml.diagram.ui;

import java.awt.event.ItemEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.argouml.i18n.Translator;
import org.argouml.ui.StylePanelFigNodeModelElement;


public class StylePanelFigMessage extends StylePanelFigNodeModelElement {
	private JLabel arrowLabel = new JLabel(Translator.localize("label.localize"));
	private JComboBox arrowField = new JComboBox(FigMessage.getArrowDirections().toArray());
	public StylePanelFigMessage() {
		super();
		arrowField.addItemListener(this);
		arrowLabel.setLabelFor(arrowField);
		add(arrowLabel);
		add(arrowField);
		arrowField.setSelectedIndex(0);
		remove(getFillField());
		remove(getFillLabel());
	}
	@Override public void refresh() {
		super.refresh();
		int direction = ((FigMessage) getPanelTarget()).getArrow();
		arrowField.setSelectedItem(FigMessage.getArrowDirections().get(direction));
	}
	public void setTargetArrow() {
		String ad = (String) arrowField.getSelectedItem();
		int arrowDirection = FigMessage.getArrowDirections().indexOf(ad);
		if (getPanelTarget() == null||arrowDirection == -1)return;
		((FigMessage) getPanelTarget()).setArrow(arrowDirection);
		getPanelTarget().endTrans();
	}
	@Override public void itemStateChanged(ItemEvent e) {
		Object src = e.getSource();
		if (src == arrowField)setTargetArrow();else super.itemStateChanged(e);
	}
}



