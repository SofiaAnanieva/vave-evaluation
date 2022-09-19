package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.ActionNewParameter;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public abstract class PropPanelEvent extends PropPanelModelElement {
	private JScrollPane paramScroll;
	private UMLEventParameterListModel paramListModel;
	public PropPanelEvent(String name,ImageIcon icon) {
		super(name,icon);
		initialize();
	}
	protected void initialize() {
		paramScroll = getParameterScroll();
		addField("label.name",getNameTextField());
		addField("label.namespace",getNamespaceSelector());
		addSeparator();
		addField("label.parameters",getParameterScroll());
		JList transitionList = new UMLLinkedList(new UMLEventTransitionListModel());
		transitionList.setVisibleRowCount(2);
		addField("label.transition",new JScrollPane(transitionList));
		addSeparator();
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionNewStereotype());
	}
	protected JScrollPane getParameterScroll() {
		if (paramScroll == null) {
			paramListModel = new UMLEventParameterListModel();
			JList paramList = new UMLMutableLinkedList(paramListModel,new ActionNewParameter());
			paramList.setVisibleRowCount(3);
			paramScroll = new JScrollPane(paramList);
		}
		return paramScroll;
	}
}



