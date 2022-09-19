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


@UmlModelMutator public class ActionAddAttribute extends UndoableAction {
	private static ActionAddAttribute targetFollower;
	public ActionAddAttribute() {
		super(Translator.localize("button.new-attribute"),ResourceLoaderWrapper.lookupIcon("button.new-attribute"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("button.new-attribute"));
	}
	public static ActionAddAttribute getTargetFollower() {
		if (targetFollower == null) {
			targetFollower = new ActionAddAttribute();
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
		Object target = TargetManager.getInstance().getSingleModelTarget();
		Object classifier = null;
		if (Model.getFacade().isAClassifier(target)||Model.getFacade().isAAssociationEnd(target)) {
			classifier = target;
		}else if (Model.getFacade().isAFeature(target)) {
			classifier = Model.getFacade().getOwner(target);
		}else {
			return;
		}
		Project project = ProjectManager.getManager().getCurrentProject();
		Object attrType = project.getDefaultAttributeType();
		Object attr = Model.getCoreFactory().buildAttribute2(classifier,attrType);
		TargetManager.getInstance().setTarget(attr);
	}
	public boolean shouldBeEnabled() {
		Object target = TargetManager.getInstance().getSingleModelTarget();
		if (target == null) {
			return false;
		}
		return Model.getFacade().isAClassifier(target)||Model.getFacade().isAFeature(target)||Model.getFacade().isAAssociationEnd(target);
	}
	private static final long serialVersionUID = -111785878370086329l;
}



