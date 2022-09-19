package org.argouml.ui;

import java.awt.Color;
import java.awt.event.ItemEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.argouml.i18n.Translator;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;
import org.tigris.gef.ui.ColorRenderer;


public class StylePanelFigText extends StylePanelFig {
	private static final String[]FONT_NAMES =  {"dialog","serif","sanserif","monospaced"};
	private static final Integer[]COMMON_SIZES =  {Integer.valueOf(8),Integer.valueOf(9),Integer.valueOf(10),Integer.valueOf(12),Integer.valueOf(16),Integer.valueOf(18),Integer.valueOf(24),Integer.valueOf(36),Integer.valueOf(48),Integer.valueOf(72),Integer.valueOf(96)};
	private static final String[]STYLES =  {"Plain","Bold","Italic","Bold-Italic"};
	private static final String[]JUSTIFIES =  {"Left","Right","Center"};
	private JLabel fontLabel = new JLabel(Translator.localize("label.stylepane.font") + ": ");
	private JComboBox fontField = new JComboBox(FONT_NAMES);
	private JLabel sizeLabel = new JLabel(Translator.localize("label.stylepane.size") + ": ");
	private JComboBox sizeField = new JComboBox(COMMON_SIZES);
	private JLabel styleLabel = new JLabel(Translator.localize("label.stylepane.style") + ": ");
	private JComboBox styleField = new JComboBox(STYLES);
	private JLabel justLabel = new JLabel(Translator.localize("label.stylepane.justify") + ": ");
	private JComboBox justField = new JComboBox(JUSTIFIES);
	private JLabel textColorLabel = new JLabel(Translator.localize("label.stylepane.text-color") + ": ");
	private JComboBox textColorField = new JComboBox();
	public StylePanelFigText() {
		super();
		fontField.addItemListener(this);
		sizeField.addItemListener(this);
		styleField.addItemListener(this);
		justField.addItemListener(this);
		textColorField.addItemListener(this);
		textColorField.setRenderer(new ColorRenderer());
		textColorLabel.setLabelFor(textColorField);
		add(textColorLabel);
		add(textColorField);
		addSeperator();
		fontLabel.setLabelFor(fontField);
		add(fontLabel);
		add(fontField);
		sizeLabel.setLabelFor(sizeField);
		add(sizeLabel);
		add(sizeField);
		styleLabel.setLabelFor(styleField);
		add(styleLabel);
		add(styleField);
		justLabel.setLabelFor(justField);
		add(justLabel);
		add(justField);
		initChoices2();
	}
	protected void initChoices2() {
		textColorField.addItem(Color.black);
		textColorField.addItem(Color.white);
		textColorField.addItem(Color.gray);
		textColorField.addItem(Color.lightGray);
		textColorField.addItem(Color.darkGray);
		textColorField.addItem(Color.red);
		textColorField.addItem(Color.blue);
		textColorField.addItem(Color.green);
		textColorField.addItem(Color.orange);
		textColorField.addItem(Color.pink);
		textColorField.addItem(getCustomItemName());
	}
	public void refresh() {
		super.refresh();
		FigText ft = (FigText) getPanelTarget();
		if (ft == null) {
			return;
		}
		String fontName = ft.getFontFamily().toLowerCase();
		int size = ft.getFontSize();
		String styleName = STYLES[0];
		fontField.setSelectedItem(fontName);
		sizeField.setSelectedItem(Integer.valueOf(size));
		if (ft.getBold()) {
			styleName = STYLES[1];
		}
		if (ft.getItalic()) {
			styleName = STYLES[2];
		}
		if (ft.getBold()&&ft.getItalic()) {
			styleName = STYLES[3];
		}
		styleField.setSelectedItem(styleName);
		String justName = JUSTIFIES[0];
		int justCode = ft.getJustification();
		if (justCode >= 0&&justCode <= JUSTIFIES.) {
			justName = JUSTIFIES[justCode];
		}
		justField.setSelectedItem(justName);
		Color c = ft.getTextColor();
		textColorField.setSelectedItem(c);
		if (c != null&&!textColorField.getSelectedItem().equals(c)) {
			textColorField.insertItemAt(c,textColorField.getItemCount() - 1);
			textColorField.setSelectedItem(c);
		}
		if (ft.isFilled()) {
			Color fc = ft.getFillColor();
			getFillField().setSelectedItem(fc);
			if (fc != null&&!getFillField().getSelectedItem().equals(fc)) {
				getFillField().insertItemAt(fc,getFillField().getItemCount() - 1);
				getFillField().setSelectedItem(fc);
			}
		}else {
			getFillField().setSelectedIndex(0);
		}
	}
	protected void setTargetFont() {
		if (getPanelTarget() == null) {
			return;
		}
		String fontStr = (String) fontField.getSelectedItem();
		if (fontStr.length() == 0) {
			return;
		}
		((FigText) getPanelTarget()).setFontFamily(fontStr);
		getPanelTarget().endTrans();
	}
	protected void setTargetSize() {
		if (getPanelTarget() == null) {
			return;
		}
		Integer size = (Integer) sizeField.getSelectedItem();
		((FigText) getPanelTarget()).setFontSize(size.intValue());
		getPanelTarget().endTrans();
	}
	protected void setTargetStyle() {
		if (getPanelTarget() == null) {
			return;
		}
		String styleStr = (String) styleField.getSelectedItem();
		if (styleStr == null) {
			return;
		}
		boolean bold = (styleStr.indexOf("Bold") != -1);
		boolean italic = (styleStr.indexOf("Italic") != -1);
		((FigText) getPanelTarget()).setBold(bold);
		((FigText) getPanelTarget()).setItalic(italic);
		getPanelTarget().endTrans();
	}
	protected void setTargetJustification() {
		if (getPanelTarget() == null) {
			return;
		}
		String justStr = (String) justField.getSelectedItem();
		if (justStr == null) {
			return;
		}
		((FigText) getPanelTarget()).setJustificationByName(justStr);
		getPanelTarget().endTrans();
	}
	protected void setTargetTextColor() {
		if (getPanelTarget() == null) {
			return;
		}
		Object c = textColorField.getSelectedItem();
		if (c instanceof Color) {
			((FigText) getPanelTarget()).setTextColor((Color) c);
		}
		getPanelTarget().endTrans();
	}
	public void itemStateChanged(ItemEvent e) {
		Object src = e.getSource();
		Fig target = getPanelTarget();
		if (e.getStateChange() == ItemEvent.SELECTED&&target instanceof FigText) {
			if (src == fontField) {
				setTargetFont();
			}else if (src == sizeField) {
				setTargetSize();
			}else if (src == styleField) {
				setTargetStyle();
			}else if (src == justField) {
				setTargetJustification();
			}else if (src == textColorField) {
				if (e.getItem() == getCustomItemName()) {
					handleCustomColor(textColorField,"label.stylepane.custom-text-color",((FigText) target).getTextColor());
				}
				setTargetTextColor();
			}else {
				super.itemStateChanged(e);
			}
		}
	}
	private static final long serialVersionUID = 2019248527481196634l;
}



