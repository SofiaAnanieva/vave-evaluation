package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.uml.ui.UMLTextField2;


public class PropPanelSynchState extends PropPanelStateVertex {
	private static final long serialVersionUID = -6671890304679263593l;
	public PropPanelSynchState() {
		super("label.synch-state",lookupIcon("SynchState"));
		addField("label.name",getNameTextField());
		addField("label.container",getContainerScroll());
		addField("label.bound",new UMLTextField2(new UMLSynchStateBoundDocument()));
		addSeparator();
		addField("label.incoming",getIncomingScroll());
		addField("label.outgoing",getOutgoingScroll());
	}
}



