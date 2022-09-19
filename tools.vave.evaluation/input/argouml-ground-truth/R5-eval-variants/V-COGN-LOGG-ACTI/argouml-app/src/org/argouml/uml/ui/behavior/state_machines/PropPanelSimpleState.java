package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.ImageIcon;


public class PropPanelSimpleState extends AbstractPropPanelState {
	private static final long serialVersionUID = 7072535148338954868l;
	public PropPanelSimpleState() {
		this("label.simple.state",lookupIcon("SimpleState"));
	}
	private PropPanelSimpleState(String name,ImageIcon icon) {
		super(name,icon);
		addField("label.name",getNameTextField());
		addField("label.container",getContainerScroll());
		addField("label.entry",getEntryScroll());
		addField("label.exit",getExitScroll());
		addField("label.do-activity",getDoScroll());
		addField("label.deferrable",getDeferrableEventsScroll());
		addSeparator();
		addField("label.incoming",getIncomingScroll());
		addField("label.outgoing",getOutgoingScroll());
		addField("label.internal-transitions",getInternalTransitionsScroll());
	}
}



