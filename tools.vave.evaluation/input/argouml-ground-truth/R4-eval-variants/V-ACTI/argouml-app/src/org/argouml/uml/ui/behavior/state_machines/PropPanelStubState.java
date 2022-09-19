package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JComboBox;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxNavigator;


public class PropPanelStubState extends PropPanelStateVertex {
	private static final long serialVersionUID = 5934039619236682498l;
	public PropPanelStubState() {
		super("label.stub.state",lookupIcon("StubState"));
		addField("label.name",getNameTextField());
		addField("label.container",getContainerScroll());
		JComboBox referencestateBox = new UMLComboBox2(new UMLStubStateComboBoxModel(),ActionSetStubStateReferenceState.getInstance());
		addField("label.referencestate",new UMLComboBoxNavigator(Translator.localize("tooltip.nav-stubstate"),referencestateBox));
		addSeparator();
		addField("label.incoming",getIncomingScroll());
		addField("label.outgoing",getOutgoingScroll());
	}
}



