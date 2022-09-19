package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.uml.ui.behavior.collaborations.ActionSetClassifierRoleMultiplicity;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLSearchableComboBox;


public class UMLMultiplicityPanel extends JPanel implements ItemListener {
	private JComboBox multiplicityComboBox;
	private JCheckBox checkBox;
	private MultiplicityComboBoxModel multiplicityComboBoxModel;
	private static List<String>multiplicityList = new ArrayList<String>();
	static {
	multiplicityList.add("1");
	multiplicityList.add("0..1");
	multiplicityList.add("0..*");
	multiplicityList.add("1..*");
}
	public UMLMultiplicityPanel() {
		super(new BorderLayout());
	}
	@Override public Dimension getPreferredSize() {
		return null;
	}
	public void itemStateChanged(ItemEvent event) {
	}
	private void delete(Object multiplicity) {
	}
	private Object getTarget() {
		return null;
	}
	private class MultiplicityComboBox extends UMLSearchableComboBox {
	public MultiplicityComboBox(UMLComboBoxModel2 arg0,Action selectAction) {
		super(arg0,selectAction);
	}
	@Override protected void doOnEdit(Object item) {
	}
	@Override public void targetSet(TargetEvent e) {
	}
}
	private class MultiplicityComboBoxModel extends UMLComboBoxModel2 {
	public MultiplicityComboBoxModel(String propertySetName) {
		super(propertySetName,false);
	}
	protected boolean isValidElement(Object element) {
		return true;
	}
	protected void buildModelList() {
	}
	@Override public void addElement(Object o) {
	}
	@Override public void setSelectedItem(Object anItem) {
	}
	protected Object getSelectedModelElement() {
		return null;
	}
}
	private class MultiplicityCheckBox extends JCheckBox implements ItemListener {
	public MultiplicityCheckBox() {
	}
	public void itemStateChanged(ItemEvent e) {
	}
}
}



