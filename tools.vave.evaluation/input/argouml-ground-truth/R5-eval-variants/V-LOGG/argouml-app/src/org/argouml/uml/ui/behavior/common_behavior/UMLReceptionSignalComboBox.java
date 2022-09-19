package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLListCellRenderer2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;


public class UMLReceptionSignalComboBox extends UMLComboBox2 {
	public UMLReceptionSignalComboBox(UMLUserInterfaceContainer container,UMLComboBoxModel2 arg0) {
		super(arg0);
		setRenderer(new UMLListCellRenderer2(true));
	}
	protected void doIt(ActionEvent event) {
		Object o = getModel().getElementAt(getSelectedIndex());
		Object signal = o;
		Object reception = getTarget();
		if (signal != Model.getFacade().getSignal(reception)) {
			Model.getCommonBehaviorHelper().setSignal(reception,signal);
		}
	}
}



