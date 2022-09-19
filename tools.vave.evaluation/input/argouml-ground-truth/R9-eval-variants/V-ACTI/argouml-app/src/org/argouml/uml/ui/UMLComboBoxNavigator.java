package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.ui.targetmanager.TargetManager;


public class UMLComboBoxNavigator extends JPanel implements ActionListener,ItemListener {
	private static ImageIcon icon = ResourceLoaderWrapper.lookupIconResource("ComboNav");
	private JComboBox theComboBox;
	private JButton theButton;
	public UMLComboBoxNavigator(String tooltip,JComboBox box) {
		super(new BorderLayout());
		theButton = new JButton(icon);
		theComboBox = box;
		theButton.setPreferredSize(new Dimension(icon.getIconWidth() + 6,icon.getIconHeight() + 6));
		theButton.setToolTipText(tooltip);
		theButton.addActionListener(this);
		box.addActionListener(this);
		box.addItemListener(this);
		add(theComboBox,BorderLayout.CENTER);
		add(theButton,BorderLayout.EAST);
		Object item = theComboBox.getSelectedItem();
		setButtonEnabled(item);
	}
	@Override public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width,getMinimumSize().height);
	}
	public void actionPerformed(final java.awt.event.ActionEvent event) {
		if (event.getSource() == theButton) {
			Object item = theComboBox.getSelectedItem();
			if (item != null) {
				TargetManager.getInstance().setTarget(item);
			}
		}
		if (event.getSource() == theComboBox) {
			Object item = theComboBox.getSelectedItem();
			setButtonEnabled(item);
		}
	}
	public void itemStateChanged(ItemEvent event) {
		if (event.getSource() == theComboBox) {
			Object item = theComboBox.getSelectedItem();
			setButtonEnabled(item);
		}
	}
	private void setButtonEnabled(Object item) {
		if (item != null) {
			theButton.setEnabled(true);
		}else {
			theButton.setEnabled(false);
		}
	}
	public void setEnabled(boolean enabled) {
		theComboBox.setEnabled(enabled);
		theComboBox.setEditable(enabled);
	}
}



