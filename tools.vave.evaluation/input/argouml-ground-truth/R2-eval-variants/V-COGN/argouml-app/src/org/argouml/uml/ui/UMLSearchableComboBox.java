package org.argouml.uml.ui;

import javax.swing.Action;
import javax.swing.ComboBoxModel;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLEditableComboBox;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLListCellRenderer2;


public class UMLSearchableComboBox extends UMLEditableComboBox {
	public UMLSearchableComboBox(UMLComboBoxModel2 model,Action selectAction,boolean showIcon) {
		super(model,selectAction,showIcon);
	}
	public UMLSearchableComboBox(UMLComboBoxModel2 arg0,Action selectAction) {
		this(arg0,selectAction,true);
	}
	protected void doOnEdit(Object item) {
		Object element = search(item);
		if (element != null) {
			setSelectedItem(element);
		}
	}
	protected Object search(Object item) {
		String text = (String) item;
		ComboBoxModel model = getModel();
		for (int i = 0;i < model.getSize();i++) {
			Object element = model.getElementAt(i);
			if (Model.getFacade().isAModelElement(element)) {
				if (getRenderer()instanceof UMLListCellRenderer2) {
					String labelText = ((UMLListCellRenderer2) getRenderer()).makeText(element);
					if (labelText != null&&labelText.startsWith(text)) {
						return element;
					}
				}
				if (Model.getFacade().isAModelElement(element)) {
					Object elem = element;
					String name = Model.getFacade().getName(elem);
					if (name != null&&name.startsWith(text)) {
						return element;
					}
				}
			}
		}
		return null;
	}
}



