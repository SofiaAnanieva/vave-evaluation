package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.tigris.toolbar.toolbutton.ModalAction;


public abstract class ActionNewAction extends AbstractActionNewModelElement implements ModalAction {
	public static final String ROLE = "role";
	public static interface Roles {
	String ENTRY = "entry";
	String EXIT = "exit";
	String DO = "do";
	String ACTION = "action";
	String EFFECT = "effect";
	String MEMBER = "member";
}
	protected ActionNewAction() {
		super();
	}
	protected abstract Object createAction();
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object action = createAction();
		if (getValue(ROLE).equals(Roles.MEMBER)) {
			Model.getCommonBehaviorHelper().addAction(getTarget(),action);
		}
		TargetManager.getInstance().setTarget(action);
	}
	public static Object getAction(String role,Object t) {
		if (role.equals(Roles.EXIT)) {
			return Model.getFacade().getExit(t);
		}else if (role.equals(Roles.ENTRY)) {
			return Model.getFacade().getEntry(t);
		}else if (role.equals(Roles.DO)) {
			return Model.getFacade().getDoActivity(t);
		}else if (role.equals(Roles.ACTION)) {
			return Model.getFacade().getAction(t);
		}else if (role.equals(Roles.EFFECT)) {
			return Model.getFacade().getEffect(t);
		}else if (role.equals(Roles.MEMBER)) {
			return Model.getFacade().getActions(t);
		}
		return null;
	}
}



