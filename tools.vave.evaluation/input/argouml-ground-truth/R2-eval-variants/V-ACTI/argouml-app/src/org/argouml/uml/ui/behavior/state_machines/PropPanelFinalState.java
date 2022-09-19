package org.argouml.uml.ui.behavior.state_machines;


public class PropPanelFinalState extends AbstractPropPanelState {
	private static final long serialVersionUID = 4111793068615402073l;
	public PropPanelFinalState() {
		super("label.final.state",lookupIcon("FinalState"));
		addField("label.name",getNameTextField());
		addField("label.container",getContainerScroll());
		addField("label.entry",getEntryScroll());
		addField("label.do-activity",getDoScroll());
		addSeparator();
		addField("label.incoming",getIncomingScroll());
		addField("label.internal-transitions",getInternalTransitionsScroll());
	}
}



