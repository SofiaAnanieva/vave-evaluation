package org.argouml.uml.diagram.static_structure.ui;

import java.awt.event.ItemEvent;
import javax.swing.JCheckBox;
import org.argouml.ui.StylePanelFigNodeModelElement;


public class StylePanelFigInterface extends StylePanelFigNodeModelElement {
	private JCheckBox operCheckBox = new JCheckBox("Operations");
	private boolean refreshTransaction;
	public StylePanelFigInterface() {
		super();
		addToDisplayPane(operCheckBox);
		operCheckBox.setSelected(false);
		operCheckBox.addItemListener(this);
	}
	public void refresh() {
		refreshTransaction = true;
		super.refresh();
		FigInterface ti = (FigInterface) getPanelTarget();
		operCheckBox.setSelected(ti.isOperationsVisible());
		refreshTransaction = false;
	}
	public void itemStateChanged(ItemEvent e) {
		if (!refreshTransaction) {
			Object src = e.getSource();
			if (src == operCheckBox) {
				((FigInterface) getPanelTarget()).setOperationsVisible(operCheckBox.isSelected());
			}else {
				super.itemStateChanged(e);
			}
		}
	}
	private static final long serialVersionUID = -5908351031706234211l;
}



