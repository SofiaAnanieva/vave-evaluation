package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.targetmanager.TargetManager;


class NavigateTargetBackAction extends AbstractAction {
	public NavigateTargetBackAction() {
		super(Translator.localize("action.navigate-back"),ResourceLoaderWrapper.lookupIcon("action.navigate-back"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.navigate-back"));
	}
	public void actionPerformed(ActionEvent e) {
		TargetManager.getInstance().navigateBackward();
	}
	public boolean isEnabled() {
		return TargetManager.getInstance().navigateBackPossible();
	}
	private static final long serialVersionUID = 33340548502483040l;
}



