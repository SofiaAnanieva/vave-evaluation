package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLListCellRenderer2;
import org.argouml.uml.ui.UMLComboBoxModel2;


public abstract class UMLEditableComboBox extends UMLComboBox2 implements FocusListener {
	protected class UMLComboBoxEditor extends BasicComboBoxEditor {
	private class UMLImagePanel extends JPanel {
	private JLabel imageIconLabel = new JLabel();
	private JTextField theTextField;
	public UMLImagePanel(JTextField textField,boolean showIcon) {
		setLayout(new BorderLayout());
		theTextField = textField;
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		if (showIcon) {
			imageIconLabel.setOpaque(true);
			imageIconLabel.setBackground(theTextField.getBackground());
			add(imageIconLabel,BorderLayout.WEST);
		}
		add(theTextField,BorderLayout.CENTER);
		theTextField.addFocusListener(UMLEditableComboBox.this);
	}
	public void setText(String text) {
		theTextField.setText(text);
	}
	public String getText() {
		return theTextField.getText();
	}
	public void setIcon(Icon i) {
		if (i != null) {
			imageIconLabel.setIcon(i);
			imageIconLabel.setBorder(BorderFactory.createEmptyBorder(0,2,0,2));
		}else {
			imageIconLabel.setIcon(null);
			imageIconLabel.setBorder(null);
		}
		imageIconLabel.invalidate();
		validate();
		repaint();
	}
	public void selectAll() {
		theTextField.selectAll();
	}
	public void addActionListener(ActionListener l) {
		theTextField.addActionListener(l);
	}
	public void removeActionListener(ActionListener l) {
		theTextField.removeActionListener(l);
	}
}
	private UMLImagePanel panel;
	private boolean theShowIcon;
	public UMLComboBoxEditor(boolean showIcon) {
		super();
		panel = new UMLImagePanel(editor,showIcon);
		setShowIcon(showIcon);
	}
	public void setItem(Object anObject) {
		if (((UMLComboBoxModel2) getModel()).contains(anObject)) {
			editor.setText(((UMLListCellRenderer2) getRenderer()).makeText(anObject));
			if (theShowIcon&&(anObject != null))panel.setIcon(ResourceLoaderWrapper.getInstance().lookupIcon(anObject));
		}else super.setItem(anObject);
	}
	public boolean isShowIcon() {
		return theShowIcon;
	}
	public void setShowIcon(boolean showIcon) {
		theShowIcon = showIcon;
	}
	public Component getEditorComponent() {
		return panel;
	}
	public void addActionListener(ActionListener l) {
		panel.addActionListener(l);
	}
	public void removeActionListener(ActionListener l) {
		panel.removeActionListener(l);
	}
	public void selectAll() {
		super.selectAll();
	}
	public Object getItem() {
		return panel.getText();
	}
}
	public UMLEditableComboBox(UMLComboBoxModel2 model,Action selectAction,boolean showIcon) {
		super(model,selectAction,showIcon);
		setEditable(true);
		setEditor(new UMLComboBoxEditor(showIcon));
		getEditor().addActionListener(this);
	}
	public UMLEditableComboBox(UMLComboBoxModel2 arg0,Action selectAction) {
		this(arg0,selectAction,true);
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof JTextField) {
			Object oldValue = getSelectedItem();
			ComboBoxEditor editor = getEditor();
			Object item = editor.getItem();
			doOnEdit(item);
			if (oldValue == getSelectedItem())getEditor().setItem(getSelectedItem());
		}
	}
	protected abstract void doOnEdit(Object item);
	public final void focusGained(FocusEvent arg0) {
	}
	public final void focusLost(FocusEvent arg0) {
		doOnEdit(getEditor().getItem());
	}
}



