package org.argouml.ui;

import java.awt.event.ActionEvent;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionCreateContainedModelElement extends AbstractActionNewModelElement {
	private Object metaType;
	public ActionCreateContainedModelElement(Object theMetaType,Object target,String menuDescr) {
		super(menuDescr);
		metaType = theMetaType;
		setTarget(target);
	}
	public void actionPerformed(ActionEvent e) {
		Object newElement = Model.getUmlFactory().buildNode(metaType,getTarget());
		TargetManager.getInstance().setTarget(newElement);
	}
}



