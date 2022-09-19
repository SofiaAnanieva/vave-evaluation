package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionRemoveElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.ActionRemoveModelElement;
import org.argouml.uml.ui.UMLModelElementOrderedListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelActionSequence extends PropPanelModelElement {
	private JScrollPane actionsScroll;
	public PropPanelActionSequence() {
		this("label.action-sequence",lookupIcon("ActionSequence"));
	}
	public PropPanelActionSequence(String name,ImageIcon icon) {
		super(name,icon);
		initialize();
	}
	public void initialize() {
		addField(Translator.localize("label.name"),getNameTextField());
		JList actionsList = new UMLActionSequenceActionList();
		actionsList.setVisibleRowCount(5);
		actionsScroll = new JScrollPane(actionsList);
		addField(Translator.localize("label.actions"),actionsScroll);
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}

class UMLActionSequenceActionListModel extends UMLModelElementOrderedListModel2 {
	public UMLActionSequenceActionListModel() {
		super("action");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getActions(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isAAction(element);
	}
	protected void moveDown(int index) {
		Object target = getTarget();
		List c = Model.getFacade().getActions(target);
		if (index < c.size() - 1) {
			Object item = c.get(index);
			Model.getCommonBehaviorHelper().removeAction(target,item);
			Model.getCommonBehaviorHelper().addAction(target,index + 1,item);
		}
	}
	@Override protected void moveToBottom(int index) {
		Object target = getTarget();
		List c = Model.getFacade().getActions(target);
		if (index < c.size() - 1) {
			Object item = c.get(index);
			Model.getCommonBehaviorHelper().removeAction(target,item);
			Model.getCommonBehaviorHelper().addAction(target,c.size(),item);
		}
	}
	@Override protected void moveToTop(int index) {
		Object target = getTarget();
		List c = Model.getFacade().getActions(target);
		if (index > 0) {
			Object item = c.get(index);
			Model.getCommonBehaviorHelper().removeAction(target,item);
			Model.getCommonBehaviorHelper().addAction(target,0,item);
		}
	}
}

class ActionRemoveAction extends AbstractActionRemoveElement {
	public ActionRemoveAction() {
		super(Translator.localize("menu.popup.remove"));
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object action = getObjectToRemove();
		if (action != null) {
			Object as = getTarget();
			if (Model.getFacade().isAActionSequence(as)) {
				Model.getCommonBehaviorHelper().removeAction(as,action);
			}
		}
	}
}

class UMLActionSequenceActionList extends UMLMutableLinkedList {
	public UMLActionSequenceActionList() {
		super(new UMLActionSequenceActionListModel());
	}
	@Override public JPopupMenu getPopupMenu() {
		return new PopupMenuNewAction(ActionNewAction.Roles.MEMBER,this);
	}
}

class PopupMenuNewActionSequenceAction extends JPopupMenu {
	public PopupMenuNewActionSequenceAction(String role,UMLMutableLinkedList list) {
		super();
		JMenu newMenu = new JMenu();
		newMenu.setText(Translator.localize("action.new"));
		newMenu.add(ActionNewCallAction.getInstance());
		ActionNewCallAction.getInstance().setTarget(list.getTarget());
		ActionNewCallAction.getInstance().putValue(ActionNewAction.ROLE,role);
		add(newMenu);
		addSeparator();
		ActionRemoveModelElement.SINGLETON.setObjectToRemove(ActionNewAction.getAction(role,list.getTarget()));
		add(ActionRemoveModelElement.SINGLETON);
	}
}



