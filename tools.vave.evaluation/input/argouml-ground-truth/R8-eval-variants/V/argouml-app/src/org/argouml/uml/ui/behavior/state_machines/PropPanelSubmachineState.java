package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.tigris.swidgets.Orientation;


public class PropPanelSubmachineState extends PropPanelCompositeState {
	private static final long serialVersionUID = 2384673708664550264l;
	public PropPanelSubmachineState(final String name,final ImageIcon icon) {
		super(name,icon);
		initialize();
	}
	public PropPanelSubmachineState() {
		super("label.submachine-state",lookupIcon("SubmachineState"));
		addField("label.name",getNameTextField());
		addField("label.container",getContainerScroll());
		addField("label.entry",getEntryScroll());
		addField("label.exit",getExitScroll());
		addField("label.do-activity",getDoScroll());
		addSeparator();
		addField("label.incoming",getIncomingScroll());
		addField("label.outgoing",getOutgoingScroll());
		addField("label.internal-transitions",getInternalTransitionsScroll());
		addSeparator();
		addField("label.subvertex",new JScrollPane(new UMLMutableLinkedList(new UMLCompositeStateSubvertexListModel(),null,ActionNewStubState.getInstance())));
	}
	@Override protected void addExtraButtons() {
	}
	@Override protected void updateExtraButtons() {
	}
}



