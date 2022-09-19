package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.uml.ui.foundation.core.ActionNewParameter;


public class PropPanelSignalEvent extends PropPanelEvent {
	public PropPanelSignalEvent() {
		super("label.signal.event",lookupIcon("SignalEvent"));
	}
	@Override public void initialize() {
		super.initialize();
		JList signalList = new UMLSignalEventSignalList(new UMLSignalEventSignalListModel());
		signalList.setVisibleRowCount(2);
		addField("label.signal",new JScrollPane(signalList));
		addAction(new ActionNewParameter());
		addAction(getDeleteAction());
	}
}



