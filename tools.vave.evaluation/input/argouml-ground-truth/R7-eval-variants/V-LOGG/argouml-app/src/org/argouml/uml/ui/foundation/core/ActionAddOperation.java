package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.undo.UndoableAction;


@UmlModelMutator public class ActionAddOperation extends UndoableAction {
	private static ActionAddOperation targetFollower;
	public ActionAddOperation() {
		super(Translator.localize("button.new-operation"),ResourceLoaderWrapper.lookupIcon("button.new-operation"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("button.new-operation"));
	}
	public static ActionAddOperation getTargetFollower() {
		if (targetFollower == null) {
			targetFollower = new ActionAddOperation();
			TargetManager.getInstance().addTargetListener(new TargetListener() {
				public void targetAdded(TargetEvent e) {
					setTarget();
				}
				public void targetRemoved(TargetEvent e) {
					setTarget();
				}
				public void targetSet(TargetEvent e) {
					setTarget();
				}
				private void setTarget() {
					targetFollower.setEnabled(targetFollower.shouldBeEnabled());
				}
			});
			targetFollower.setEnabled(targetFollower.shouldBeEnabled());
		}
		return targetFollower;
	}
	public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		Project project = ProjectManager.getManager().getCurrentProject();
		Object target = TargetManager.getInstance().getModelTarget();
		Object classifier = null;
		if (Model.getFacade().isAClassifier(target)) {
			classifier = target;
		}else if (Model.getFacade().isAFeature(target)) {
			classifier = Model.getFacade().getOwner(target);
		}else {
			return;
		}
		Object returnType = project.getDefaultReturnType();
		Object oper = Model.getCoreFactory().buildOperation(classifier,returnType);
		TargetManager.getInstance().setTarget(oper);
	}
	public boolean shouldBeEnabled() {
		Object target = TargetManager.getInstance().getSingleModelTarget();
		if (target == null) {
			return false;
		}
		return Model.getFacade().isAClassifier(target)||Model.getFacade().isAFeature(target);
	}
	private static final long serialVersionUID = -1383845502957256177l;
}



