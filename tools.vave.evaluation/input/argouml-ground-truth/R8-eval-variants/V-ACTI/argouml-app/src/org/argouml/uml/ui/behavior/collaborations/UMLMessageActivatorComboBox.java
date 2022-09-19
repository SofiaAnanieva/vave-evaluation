package org.argouml.uml.ui.behavior.collaborations;

import java.awt.event.ActionEvent;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLListCellRenderer2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;


public class UMLMessageActivatorComboBox extends UMLComboBox2 {
	public UMLMessageActivatorComboBox(UMLUserInterfaceContainer container,UMLComboBoxModel2 arg0) {
		super(arg0);
		setRenderer(new UMLListCellRenderer2(true));
	}
	protected void doIt(ActionEvent event) {
	}
}



