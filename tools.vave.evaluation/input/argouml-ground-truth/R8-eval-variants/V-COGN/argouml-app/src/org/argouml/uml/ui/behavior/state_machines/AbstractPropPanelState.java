package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ScrollList;
import org.argouml.uml.ui.UMLMutableLinkedList;


public abstract class AbstractPropPanelState extends PropPanelStateVertex {
	private JScrollPane entryScroll;
	private JScrollPane exitScroll;
	private JScrollPane doScroll;
	private JScrollPane internalTransitionsScroll;
	private ScrollList deferrableEventsScroll;
	public AbstractPropPanelState(String name,ImageIcon icon) {
		super(name,icon);
		deferrableEventsScroll = new ScrollList(new UMLStateDeferrableEventListModel());
		JList entryList = new UMLStateEntryList(new UMLStateEntryListModel());
		entryList.setVisibleRowCount(2);
		entryScroll = new JScrollPane(entryList);
		JList exitList = new UMLStateExitList(new UMLStateExitListModel());
		exitList.setVisibleRowCount(2);
		exitScroll = new JScrollPane(exitList);
		JList internalTransitionList = new UMLMutableLinkedList(new UMLStateInternalTransition(),null,new ActionNewTransition());
		internalTransitionsScroll = new JScrollPane(internalTransitionList);
		JList doList = new UMLStateDoActivityList(new UMLStateDoActivityListModel());
		doList.setVisibleRowCount(2);
		doScroll = new JScrollPane(doList);
	}
	@Override protected void addExtraButtons() {
		super.addExtraButtons();
		Action a = new ActionNewTransition();
		a.putValue(Action.SHORT_DESCRIPTION,Translator.localize("button.new-internal-transition"));
		Icon icon = ResourceLoaderWrapper.lookupIcon("Transition");
		a.putValue(Action.SMALL_ICON,icon);
		addAction(a);
	}
	protected JScrollPane getEntryScroll() {
		return entryScroll;
	}
	protected JScrollPane getExitScroll() {
		return exitScroll;
	}
	protected JScrollPane getDoScroll() {
		return doScroll;
	}
	protected JScrollPane getInternalTransitionsScroll() {
		return internalTransitionsScroll;
	}
	protected JScrollPane getDeferrableEventsScroll() {
		return deferrableEventsScroll;
	}
}



