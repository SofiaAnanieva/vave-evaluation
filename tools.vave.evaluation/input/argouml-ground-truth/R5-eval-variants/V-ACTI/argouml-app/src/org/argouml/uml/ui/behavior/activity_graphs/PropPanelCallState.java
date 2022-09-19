package org.argouml.uml.ui.behavior.activity_graphs;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.behavior.state_machines.AbstractPropPanelState;
import org.argouml.uml.ui.behavior.state_machines.UMLStateEntryListModel;


public class PropPanelCallState extends AbstractPropPanelState {
	private JScrollPane callActionEntryScroll;
	private JList callActionEntryList;
	public PropPanelCallState() {
		this("label.call-state",lookupIcon("CallState"));
	}
	public PropPanelCallState(String name,ImageIcon icon) {
		super(name,icon);
		callActionEntryList = new UMLCallStateEntryList(new UMLStateEntryListModel());
		callActionEntryList.setVisibleRowCount(2);
		callActionEntryScroll = new JScrollPane(callActionEntryList);
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.container"),getContainerScroll());
		addField(Translator.localize("label.entry"),getCallActionEntryScroll());
		addField(Translator.localize("label.deferrable"),getDeferrableEventsScroll());
		addSeparator();
		addField(Translator.localize("label.incoming"),getIncomingScroll());
		addField(Translator.localize("label.outgoing"),getOutgoingScroll());
	}
	protected void addExtraButtons() {
		Action a = new ActionNewEntryCallAction();
		a.putValue(Action.SHORT_DESCRIPTION,Translator.localize("button.new-callaction"));
		Icon icon = ResourceLoaderWrapper.lookupIcon("CallAction");
		a.putValue(Action.SMALL_ICON,icon);
		addAction(a);
	}
	protected JScrollPane getCallActionEntryScroll() {
		return callActionEntryScroll;
	}
	private static final long serialVersionUID = -8830997687737785261l;
}



