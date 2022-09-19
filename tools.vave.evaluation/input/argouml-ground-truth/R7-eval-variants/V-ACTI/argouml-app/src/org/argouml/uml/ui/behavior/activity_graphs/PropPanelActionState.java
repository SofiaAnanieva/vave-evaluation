package org.argouml.uml.ui.behavior.activity_graphs;

import javax.swing.ImageIcon;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.behavior.state_machines.AbstractPropPanelState;


public class PropPanelActionState extends AbstractPropPanelState {
	private static final long serialVersionUID = 4936258091606712050l;
	public PropPanelActionState() {
		this("label.action-state",lookupIcon("ActionState"));
	}
	public PropPanelActionState(String name,ImageIcon icon) {
		super(name,icon);
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.container"),getContainerScroll());
		addField(Translator.localize("label.entry"),getEntryScroll());
		addField(Translator.localize("label.deferrable"),getDeferrableEventsScroll());
		addSeparator();
		addField(Translator.localize("label.incoming"),getIncomingScroll());
		addField(Translator.localize("label.outgoing"),getOutgoingScroll());
	}
}



