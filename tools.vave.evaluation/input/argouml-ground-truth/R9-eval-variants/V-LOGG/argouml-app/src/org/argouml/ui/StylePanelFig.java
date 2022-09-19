package org.argouml.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.Document;
import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.swingext.SpacerPanel;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.argouml.uml.diagram.ui.ArgoFig;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.StereotypeStyled;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.ui.ColorRenderer;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.DiagramSettings.StereotypeStyle;


public class StylePanelFig extends StylePanel implements ItemListener,FocusListener,KeyListener {
	private static final Logger LOG = Logger.getLogger(StylePanelFig.class);
	private static final String CUSTOM_ITEM = Translator.localize("label.stylepane.custom") + "...";
	private JLabel bboxLabel = new JLabel(Translator.localize("label.stylepane.bounds") + ": ");
	private JTextField bboxField = new JTextField();
	private JLabel fillLabel = new JLabel(Translator.localize("label.stylepane.fill") + ": ");
	private JComboBox fillField = new JComboBox();
	private JLabel lineLabel = new JLabel(Translator.localize("label.stylepane.line") + ": ");
	private JComboBox lineField = new JComboBox();
	private JLabel stereoLabel = new JLabel(Translator.localize("menu.popup.stereotype-view") + ": ");
	private JComboBox stereoField = new JComboBox();
	private SpacerPanel spacer = new SpacerPanel();
	private SpacerPanel spacer2 = new SpacerPanel();
	private SpacerPanel spacer3 = new SpacerPanel();
	public StylePanelFig(String title) {
		super(title);
	}
	public StylePanelFig() {
		super("Fig Appearance");
		initChoices();
		Document bboxDoc = bboxField.getDocument();
		bboxDoc.addDocumentListener(this);
		bboxField.addKeyListener(this);
		bboxField.addFocusListener(this);
		fillField.addItemListener(this);
		lineField.addItemListener(this);
		stereoField.addItemListener(this);
		fillField.setRenderer(new ColorRenderer());
		lineField.setRenderer(new ColorRenderer());
		bboxLabel.setLabelFor(bboxField);
		add(bboxLabel);
		add(bboxField);
		fillLabel.setLabelFor(fillField);
		add(fillLabel);
		add(fillField);
		lineLabel.setLabelFor(lineField);
		add(lineLabel);
		add(lineField);
		stereoLabel.setLabelFor(stereoField);
		add(stereoLabel);
		add(stereoField);
	}
	protected void initChoices() {
		fillField.addItem(Translator.localize("label.stylepane.no-fill"));
		fillField.addItem(Color.black);
		fillField.addItem(Color.white);
		fillField.addItem(Color.gray);
		fillField.addItem(Color.lightGray);
		fillField.addItem(Color.darkGray);
		fillField.addItem(new Color(255,255,200));
		fillField.addItem(new Color(255,200,255));
		fillField.addItem(new Color(200,255,255));
		fillField.addItem(new Color(200,200,255));
		fillField.addItem(new Color(200,255,200));
		fillField.addItem(new Color(255,200,200));
		fillField.addItem(new Color(200,200,200));
		fillField.addItem(Color.red);
		fillField.addItem(Color.blue);
		fillField.addItem(Color.cyan);
		fillField.addItem(Color.yellow);
		fillField.addItem(Color.magenta);
		fillField.addItem(Color.green);
		fillField.addItem(Color.orange);
		fillField.addItem(Color.pink);
		fillField.addItem(CUSTOM_ITEM);
		lineField.addItem(Translator.localize("label.stylepane.no-line"));
		lineField.addItem(Color.black);
		lineField.addItem(Color.white);
		lineField.addItem(Color.gray);
		lineField.addItem(Color.lightGray);
		lineField.addItem(Color.darkGray);
		lineField.addItem(new Color(60,60,200));
		lineField.addItem(new Color(60,200,60));
		lineField.addItem(new Color(200,60,60));
		lineField.addItem(Color.red);
		lineField.addItem(Color.blue);
		lineField.addItem(Color.cyan);
		lineField.addItem(Color.yellow);
		lineField.addItem(Color.magenta);
		lineField.addItem(Color.green);
		lineField.addItem(Color.orange);
		lineField.addItem(Color.pink);
		lineField.addItem(CUSTOM_ITEM);
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		stereoField.setModel(model);
		model.addElement(Translator.localize("menu.popup.stereotype-view.textual"));
		model.addElement(Translator.localize("menu.popup.stereotype-view.big-icon"));
		model.addElement(Translator.localize("menu.popup.stereotype-view.small-icon"));
	}
	protected void hasEditableBoundingBox(boolean value) {
		bboxField.setEnabled(value);
		bboxLabel.setEnabled(value);
	}
	public void refresh() {
		Fig target = getPanelTarget();
		if (target instanceof FigEdgeModelElement) {
			hasEditableBoundingBox(false);
		}else {
			hasEditableBoundingBox(true);
		}
		if (target == null) {
			return;
		}
		Rectangle figBounds = target.getBounds();
		Rectangle styleBounds = parseBBox();
		if (!(figBounds.equals(styleBounds))) {
			bboxField.setText(figBounds.x + "," + figBounds.y + "," + figBounds.width + "," + figBounds.height);
		}
		if (target.isFilled()) {
			Color c = target.getFillColor();
			fillField.setSelectedItem(c);
			if (c != null&&!fillField.getSelectedItem().equals(c)) {
				fillField.insertItemAt(c,fillField.getItemCount() - 1);
				fillField.setSelectedItem(c);
			}
		}else {
			fillField.setSelectedIndex(0);
		}
		if (target.getLineWidth() > 0) {
			Color c = target.getLineColor();
			lineField.setSelectedItem(c);
			if (c != null&&!lineField.getSelectedItem().equals(c)) {
				lineField.insertItemAt(c,lineField.getItemCount() - 1);
				lineField.setSelectedItem(c);
			}
		}else {
			lineField.setSelectedIndex(0);
		}
		stereoField.setEnabled(target instanceof StereotypeStyled);
		stereoLabel.setEnabled(target instanceof StereotypeStyled);
		if (target instanceof StereotypeStyled) {
			StereotypeStyled fig = (StereotypeStyled) target;
			stereoField.setSelectedIndex(0);
		}
	}
	protected void setTargetBBox() {
		Fig target = getPanelTarget();
		if (target == null) {
			return;
		}
		Rectangle bounds = parseBBox();
		if (bounds == null) {
			return;
		}
		if (!target.getBounds().equals(bounds)) {
			target.setBounds(bounds.x,bounds.y,bounds.width,bounds.height);
			target.endTrans();
		}
	}
	protected Rectangle parseBBox() {
		Fig target = getPanelTarget();
		String bboxStr = bboxField.getText().trim();
		if (bboxStr.length() == 0) {
			return null;
		}
		Rectangle res = new Rectangle();
		java.util.
				StringTokenizer st = new java.util.StringTokenizer(bboxStr,", ");
		try {
			boolean changed = false;
			if (!st.hasMoreTokens()) {
				return target.getBounds();
			}
			res.x = Integer.parseInt(st.nextToken());
			if (!st.hasMoreTokens()) {
				res.y = target.getBounds().y;
				res.width = target.getBounds().width;
				res.height = target.getBounds().height;
				return res;
			}
			res.y = Integer.parseInt(st.nextToken());
			if (!st.hasMoreTokens()) {
				res.width = target.getBounds().width;
				res.height = target.getBounds().height;
				return res;
			}
			res.width = Integer.parseInt(st.nextToken());
			if ((res.width + res.x) > 6000) {
				res.width = 6000 - res.x;
				changed = true;
			}
			if (!st.hasMoreTokens()) {
				res.width = target.getBounds().width;
				return res;
			}
			res.height = Integer.parseInt(st.nextToken());
			if ((res.height + res.y) > 6000) {
				res.height = 6000 - res.y;
				changed = true;
			}
			if (res.x < 0||res.y < 0) {
				LOG.warn("Part of bounding box is off screen " + res);
			}
			if (res.width < 0||res.height < 0) {
				throw new IllegalArgumentException("Bounding box has negative size " + res);
			}
			if (changed) {
				StringBuffer sb = new StringBuffer();
				sb.append(Integer.toString(res.x));
				sb.append(",");
				sb.append(Integer.toString(res.y));
				sb.append(",");
				sb.append(Integer.toString(res.width));
				sb.append(",");
				sb.append(Integer.toString(res.height));
				bboxField.setText(sb.toString());
			}
		}catch (NumberFormatException ex) {
			bboxField.setBackground(Color.RED);
			return null;
		}catch (IllegalArgumentException iae) {
			bboxField.setBackground(Color.RED);
			return null;
		}
		bboxField.setBackground(null);
		return res;
	}
	protected void handleCustomColor(JComboBox field,String title,Color targetColor) {
		Color newColor = JColorChooser.showDialog(ArgoFrame.getInstance(),Translator.localize(title),targetColor);
		if (newColor != null) {
			field.insertItemAt(newColor,field.getItemCount() - 1);
			field.setSelectedItem(newColor);
		}else if (getPanelTarget() != null) {
			field.setSelectedItem(targetColor);
		}
	}
	public void setTargetFill() {
		Fig target = getPanelTarget();
		Object c = fillField.getSelectedItem();
		if (target == null||c == null) {
			return;
		}
		Boolean isColor = (c instanceof Color);
		if (isColor) {
			target.setFillColor((Color) c);
		}
		target.setFilled(isColor);
		target.endTrans();
		ArgoDiagram activeDiagram = DiagramUtils.getActiveDiagram();
		for (Object t:TargetManager.getInstance().getTargets()) {
			Fig fig = null;
			if (t instanceof FigNodeModelElement) {
				fig = (Fig) t;
			}else {
				fig = activeDiagram.presentationFor(t);
			}
			if (fig != null&&fig != target) {
				if (isColor) {
					fig.setFillColor((Color) c);
				}
				fig.setFilled(isColor);
				fig.endTrans();
			}
		}
	}
	public void setTargetLine() {
		Fig target = getPanelTarget();
		Object c = lineField.getSelectedItem();
		if (target == null||c == null) {
			return;
		}
		Boolean isColor = (c instanceof Color);
		if (isColor) {
			target.setLineColor((Color) c);
		}
		target.setLineWidth(isColor?ArgoFig.LINE_WIDTH:0);
		target.endTrans();
		ArgoDiagram activeDiagram = DiagramUtils.getActiveDiagram();
		for (Object t:TargetManager.getInstance().getTargets()) {
			Fig fig = null;
			if (t instanceof FigNodeModelElement) {
				fig = (Fig) t;
			}else {
				fig = activeDiagram.presentationFor(t);
			}
			if (fig != null&&fig != target) {
				if (isColor) {
					fig.setLineColor((Color) c);
				}
				fig.setLineWidth(isColor?ArgoFig.LINE_WIDTH:0);
				fig.endTrans();
			}
		}
	}
	public void itemStateChanged(ItemEvent e) {
		Object src = e.getSource();
		Fig target = getPanelTarget();
		if (e.getStateChange() == ItemEvent.SELECTED&&target != null) {
			if (src == fillField) {
				if (e.getItem() == CUSTOM_ITEM) {
					handleCustomColor(fillField,"label.stylepane.custom-fill-color",target.getFillColor());
				}
				setTargetFill();
			}else if (src == lineField) {
				if (e.getItem() == CUSTOM_ITEM) {
					handleCustomColor(lineField,"label.stylepane.custom-line-color",target.getLineColor());
				}
				setTargetLine();
			}else if (src == stereoField) {
				if (target instanceof StereotypeStyled) {
					Object item = e.getItem();
					DefaultComboBoxModel model = (DefaultComboBoxModel) stereoField.getModel();
					int idx = model.getIndexOf(item);
					StereotypeStyled fig = (StereotypeStyled) target;
					fig.setStereotypeStyle(StereotypeStyle.getEnum(idx));
				}
			}
		}
	}
	public void focusGained(FocusEvent e) {
	}
	public void focusLost(FocusEvent e) {
		if (e.getSource() == bboxField) {
			setTargetBBox();
		}
	}
	public void keyPressed(KeyEvent e) {
	}
	public void keyReleased(KeyEvent e) {
	}
	public void keyTyped(KeyEvent e) {
		if (e.getSource().equals(bboxField)&&e.getKeyChar() == '\n') {
			setTargetBBox();
		}
	}
	protected JLabel getBBoxLabel() {
		return bboxLabel;
	}
	protected JTextField getBBoxField() {
		return bboxField;
	}
	protected JLabel getFillLabel() {
		return fillLabel;
	}
	protected JComboBox getFillField() {
		return fillField;
	}
	protected JLabel getLineLabel() {
		return lineLabel;
	}
	protected JComboBox getLineField() {
		return lineField;
	}
	protected SpacerPanel getSpacer() {
		return spacer;
	}
	protected SpacerPanel getSpacer2() {
		return spacer2;
	}
	protected SpacerPanel getSpacer3() {
		return spacer3;
	}
	private static final long serialVersionUID = -6232843473753751128l;
	protected static String getCustomItemName() {
		return CUSTOM_ITEM;
	}
}



