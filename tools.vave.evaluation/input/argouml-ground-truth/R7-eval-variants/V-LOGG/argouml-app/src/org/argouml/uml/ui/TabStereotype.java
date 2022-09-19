package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class TabStereotype extends PropPanel {
	private static final int INSET_PX = 3;
	private static String orientation = Configuration.getString(Configuration.makeKey("layout","tabstereotype"));
	private UMLModelElementListModel2 selectedListModel;
	private UMLModelElementListModel2 availableListModel;
	private JScrollPane selectedScroll;
	private JScrollPane availableScroll;
	private JPanel panel;
	private JButton addStButton;
	private JButton removeStButton;
	private JPanel xferButtons;
	private JList selectedList;
	private JList availableList;
	public TabStereotype() {
		super(Translator.localize("tab.stereotype"),(ImageIcon) null);
	}
	private JPanel makePanel() {
		return null;
	}
	public boolean shouldBeEnabled() {
		return false;
	}
	@Override public boolean shouldBeEnabled(Object target) {
		return false;
	}
	@Override public void setTarget(Object theTarget) {
	}
	private void doAddStereotype() {
	}
	private void doRemoveStereotype() {
	}
	private static class UMLModelStereotypeListModel extends UMLModelElementListModel2 {
	public UMLModelStereotypeListModel() {
		super("stereotype");
	}
	protected void buildModelList() {
	}
	protected boolean isValidElement(Object element) {
		return false;
	}
	private static final long serialVersionUID = 7247425177890724453l;
}
	private class AddRemoveListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	}
}
	private class AvailableListSelectionListener implements ListSelectionListener {
	public void valueChanged(ListSelectionEvent lse) {
	}
}
	private class SelectedListSelectionListener implements ListSelectionListener {
	public void valueChanged(ListSelectionEvent lse) {
	}
}
	private static final long serialVersionUID = -4741653225927138553l;
}



