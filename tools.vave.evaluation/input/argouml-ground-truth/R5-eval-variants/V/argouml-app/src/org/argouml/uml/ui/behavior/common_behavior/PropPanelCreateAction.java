package org.argouml.uml.ui.behavior.common_behavior;

import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.UMLMutableLinkedList;


public class PropPanelCreateAction extends PropPanelAction {
	public PropPanelCreateAction() {
		super("label.create-action",lookupIcon("CreateAction"));
		AbstractActionAddModelElement2 action = new ActionAddCreateActionInstantiation();
		UMLMutableLinkedList list = new UMLMutableLinkedList(new UMLCreateActionClassifierListModel(),action,null,null,true);
		list.setVisibleRowCount(2);
		JScrollPane instantiationScroll = new JScrollPane(list);
		addFieldBefore(Translator.localize("label.instantiation"),instantiationScroll,argumentsScroll);
	}
	private static final long serialVersionUID = 6909604490593418840l;
}



