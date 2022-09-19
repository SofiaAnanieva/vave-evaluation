package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;


public class ActionNewActionSequence extends ActionNewAction {
	private static final ActionNewActionSequence SINGLETON = new ActionNewActionSequence();
	protected ActionNewActionSequence() {
		super();
		putValue(Action.NAME,Translator.localize("button.new-actionsequence"));
	}
	protected Object createAction() {
		return Model.getCommonBehaviorFactory().createActionSequence();
	}
	public static ActionNewActionSequence getInstance() {
		return SINGLETON;
	}
	public static ActionNewAction getButtonInstance() {
		ActionNewAction a = new ActionNewActionSequence() {
	public void actionPerformed(ActionEvent e) {
		Object target = TargetManager.getInstance().getModelTarget();
		if (!Model.getFacade().isATransition(target))return;
		setTarget(target);
		super.actionPerformed(e);
	}
};
		a.putValue(SHORT_DESCRIPTION,a.getValue(Action.NAME));
		Object icon = ResourceLoaderWrapper.lookupIconResource("ActionSequence");
		a.putValue(SMALL_ICON,icon);
		a.putValue(ROLE,Roles.EFFECT);
		return a;
	}
}



