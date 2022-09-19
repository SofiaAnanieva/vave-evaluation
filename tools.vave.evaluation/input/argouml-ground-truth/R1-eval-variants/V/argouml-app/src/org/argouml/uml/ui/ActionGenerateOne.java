package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.generator.ui.ClassGenerationDialog;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.UndoableAction;


public class ActionGenerateOne extends UndoableAction {
	public ActionGenerateOne() {
		super(Translator.localize("action.generate-selected-classes"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.generate-selected-classes"));
	}
	@Override public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		List classes = getCandidates();
		ClassGenerationDialog cgd = new ClassGenerationDialog(classes);
		cgd.setVisible(true);
	}
	@Override public boolean isEnabled() {
		return true;
	}
	private List getCandidates() {
		List classes = new ArrayList();
		Collection targets = TargetManager.getInstance().getTargets();
		for (Object target:targets) {
			if (target instanceof Fig) {
				target = ((Fig) target).getOwner();
			}
			if (Model.getFacade().isAClass(target)||Model.getFacade().isAInterface(target)) {
				classes.add(target);
			}
		}
		return classes;
	}
}



