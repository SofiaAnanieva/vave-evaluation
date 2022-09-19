package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public abstract class PropPanelStateVertex extends PropPanelModelElement {
	private JScrollPane incomingScroll;
	private JScrollPane outgoingScroll;
	private JPanel containerScroll;
	public PropPanelStateVertex(String name,ImageIcon icon) {
		super(name,icon);
		JList incomingList = new UMLLinkedList(new UMLStateVertexIncomingListModel());
		incomingScroll = new JScrollPane(incomingList);
		JList outgoingList = new UMLLinkedList(new UMLStateVertexOutgoingListModel());
		outgoingScroll = new JScrollPane(outgoingList);
		containerScroll = getSingleRowScroll(new UMLStateVertexContainerListModel());
		addAction(new ActionNavigateContainerElement());
		addExtraButtons();
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
	protected void addExtraButtons() {
	}
	protected JScrollPane getIncomingScroll() {
		return incomingScroll;
	}
	protected JScrollPane getOutgoingScroll() {
		return outgoingScroll;
	}
	protected JPanel getContainerScroll() {
		return containerScroll;
	}
}



