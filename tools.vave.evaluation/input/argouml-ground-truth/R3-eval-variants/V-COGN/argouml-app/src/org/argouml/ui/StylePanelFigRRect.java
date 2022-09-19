package org.argouml.ui;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import org.argouml.i18n.Translator;
import org.tigris.gef.presentation.FigRRect;


public class StylePanelFigRRect extends StylePanelFig {
	private JLabel roundingLabel = new JLabel(Translator.localize("label.stylepane.rounding") + ": ");
	private JTextField roundingField = new JTextField();
	public StylePanelFigRRect() {
		super();
		Document roundingDoc = roundingField.getDocument();
		roundingDoc.addDocumentListener(this);
		roundingLabel.setLabelFor(roundingField);
		add(roundingLabel);
		add(roundingField);
	}
	public void refresh() {
		super.refresh();
		String roundingStr = ((FigRRect) getPanelTarget()).getCornerRadius() + "";
		roundingField.setText(roundingStr);
	}
	protected void setTargetRounding() {
		if (getPanelTarget() == null)return;
		String roundingStr = roundingField.getText();
		if (roundingStr.length() == 0)return;
		int r = Integer.parseInt(roundingStr);
		((FigRRect) getPanelTarget()).setCornerRadius(r);
		getPanelTarget().endTrans();
	}
	public void insertUpdate(DocumentEvent e) {
		Document roundingDoc = roundingField.getDocument();
		if (e.getDocument() == roundingDoc)setTargetRounding();
		super.insertUpdate(e);
	}
}



