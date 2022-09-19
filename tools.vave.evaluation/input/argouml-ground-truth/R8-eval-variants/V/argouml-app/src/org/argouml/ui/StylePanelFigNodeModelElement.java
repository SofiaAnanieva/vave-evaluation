package org.argouml.ui;

import java.awt.FlowLayout;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.argouml.i18n.Translator;
import org.argouml.uml.diagram.PathContainer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.ui.ColorRenderer;


public class StylePanelFigNodeModelElement extends StylePanelFig implements ItemListener,FocusListener,KeyListener,PropertyChangeListener {
	private boolean refreshTransaction;
	private JLabel displayLabel = new JLabel(Translator.localize("label.stylepane.display"));
	private JCheckBox pathCheckBox = new JCheckBox(Translator.localize("label.stylepane.path"));
	private JPanel displayPane;
	public StylePanelFigNodeModelElement() {
		super();
		getFillField().setRenderer(new ColorRenderer());
		getLineField().setRenderer(new ColorRenderer());
		displayPane = new JPanel();
		displayPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		addToDisplayPane(pathCheckBox);
		displayLabel.setLabelFor(displayPane);
		add(displayPane,0);
		add(displayLabel,0);
		pathCheckBox.addItemListener(this);
	}
	public void addToDisplayPane(JCheckBox cb) {
		displayPane.add(cb);
	}
	@Override public void setTarget(Object t) {
		Fig oldTarget = getPanelTarget();
		if (oldTarget != null) {
			oldTarget.removePropertyChangeListener(this);
		}
		super.setTarget(t);
		Fig newTarget = getPanelTarget();
		if (newTarget != null) {
			newTarget.addPropertyChangeListener(this);
		}
	}
	public void refresh() {
		refreshTransaction = true;
		super.refresh();
		Object target = getPanelTarget();
		if (target instanceof PathContainer) {
			PathContainer pc = (PathContainer) getPanelTarget();
			pathCheckBox.setSelected(pc.isPathVisible());
		}
		refreshTransaction = false;
		setTargetBBox();
	}
	public void itemStateChanged(ItemEvent e) {
		if (!refreshTransaction) {
			Object src = e.getSource();
			if (src == pathCheckBox) {
				PathContainer pc = (PathContainer) getPanelTarget();
				pc.setPathVisible(pathCheckBox.isSelected());
			}else {
				super.itemStateChanged(e);
			}
		}
	}
	public void propertyChange(PropertyChangeEvent evt) {
		if ("pathVisible".equals(evt.getPropertyName())) {
			refreshTransaction = true;
			pathCheckBox.setSelected((Boolean) evt.getNewValue());
			refreshTransaction = false;
		}
	}
}



