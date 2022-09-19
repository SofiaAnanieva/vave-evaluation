package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.tigris.gef.undo.UndoableAction;


public class PropPanelTaggedValue extends PropPanelModelElement {
	private JComponent modelElementSelector;
	private JComponent typeSelector;
	private JScrollPane referenceValuesScroll;
	private JScrollPane dataValuesScroll;
	public PropPanelTaggedValue() {
	}
	protected JScrollPane getReferenceValuesScroll() {
		return null;
	}
	protected JScrollPane getDataValuesScroll() {
		return null;
	}
	protected JComponent getModelElementSelector() {
		return null;
	}
	protected JComponent getTypeSelector() {
		return null;
	}
	static class ActionSetTaggedValueModelElement extends UndoableAction {
	public ActionSetTaggedValueModelElement() {
		super();
	}
	public void actionPerformed(ActionEvent e) {
	}
}
	static class UMLTaggedValueModelElementComboBoxModel extends UMLComboBoxModel2 {
	public UMLTaggedValueModelElementComboBoxModel() {
		super("modelelement",true);
	}
	protected void buildModelList() {
	}
	protected Object getSelectedModelElement() {
		return null;
	}
	protected boolean isValidElement(Object element) {
		return false;
	}
}
	static class ActionSetTaggedValueType extends UndoableAction {
	public ActionSetTaggedValueType() {
		super();
	}
	public void actionPerformed(ActionEvent e) {
	}
}
	static class UMLTaggedValueTypeComboBoxModel extends UMLComboBoxModel2 {
	public UMLTaggedValueTypeComboBoxModel() {
		super("type",true);
	}
	protected void buildModelList() {
	}
	protected Object getSelectedModelElement() {
		return null;
	}
	protected boolean isValidElement(Object element) {
		return false;
	}
}
	static class UMLReferenceValueListModel extends UMLModelElementListModel2 {
	public UMLReferenceValueListModel() {
		super("referenceValue");
	}
	protected void buildModelList() {
	}
	protected boolean isValidElement(Object element) {
		return false;
	}
}
	static class UMLDataValueListModel extends UMLModelElementListModel2 {
	public UMLDataValueListModel() {
		super("dataValue");
	}
	protected void buildModelList() {
	}
	protected boolean isValidElement(Object element) {
		return false;
	}
}
}



