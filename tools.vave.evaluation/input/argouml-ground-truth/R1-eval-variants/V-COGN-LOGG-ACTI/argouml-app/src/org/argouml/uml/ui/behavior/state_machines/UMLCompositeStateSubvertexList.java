package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionRemoveModelElement;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;


public class UMLCompositeStateSubvertexList extends UMLMutableLinkedList {
	private class PopupMenu extends JPopupMenu {
	public PopupMenu() {
		super();
		JMenu pMenu = new JMenu();
		pMenu.setText(Translator.localize("button.new-pseudostate"));
		pMenu.add(new ActionNewPseudoState(Model.getPseudostateKind().getChoice(),"label.pseudostate.choice"));
		pMenu.add(new ActionNewPseudoState(Model.getPseudostateKind().getInitial(),"label.pseudostate.initial"));
		pMenu.add(new ActionNewPseudoState(Model.getPseudostateKind().getDeepHistory(),"label.pseudostate.deephistory"));
		pMenu.add(new ActionNewPseudoState(Model.getPseudostateKind().getJunction(),"label.pseudostate.junction"));
		pMenu.add(new ActionNewPseudoState(Model.getPseudostateKind().getShallowHistory(),"label.pseudostate.shallowhistory"));
		pMenu.add(new ActionNewPseudoState(Model.getPseudostateKind().getFork(),"label.pseudostate.fork"));
		pMenu.add(new ActionNewPseudoState(Model.getPseudostateKind().getJoin(),"label.pseudostate.join"));
		JMenu newMenu = new JMenu();
		newMenu.setText(Translator.localize("action.new"));
		newMenu.add(pMenu);
		newMenu.add(ActionNewSynchState.getInstance());
		ActionNewSynchState.getInstance().setTarget(getTarget());
		newMenu.add(ActionNewStubState.getInstance());
		ActionNewStubState.getInstance().setTarget(getTarget());
		newMenu.add(ActionNewCompositeState.getSingleton());
		ActionNewCompositeState.getSingleton().setTarget(getTarget());
		newMenu.add(ActionNewSimpleState.getSingleton());
		ActionNewSimpleState.getSingleton().setTarget(getTarget());
		newMenu.add(ActionNewFinalState.getSingleton());
		ActionNewFinalState.getSingleton().setTarget(getTarget());
		newMenu.add(ActionNewSubmachineState.getInstance());
		ActionNewSubmachineState.getInstance().setTarget(getTarget());
		add(newMenu);
		addSeparator();
		ActionRemoveModelElement.SINGLETON.setTarget(getSelectedValue());
		ActionRemoveModelElement.SINGLETON.setObjectToRemove(getSelectedValue());
		add(ActionRemoveModelElement.SINGLETON);
	}
}
	public UMLCompositeStateSubvertexList(UMLModelElementListModel2 dataModel) {
		super(dataModel);
	}
	public JPopupMenu getPopupMenu() {
		return new PopupMenu();
	}
}



