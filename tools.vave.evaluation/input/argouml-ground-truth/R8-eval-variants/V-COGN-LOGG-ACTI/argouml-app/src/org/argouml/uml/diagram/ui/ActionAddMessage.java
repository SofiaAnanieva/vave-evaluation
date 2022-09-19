package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.undo.UndoableAction;


public class ActionAddMessage extends UndoableAction {
	private static ActionAddMessage targetFollower;
	public ActionAddMessage() {
		super(Translator.localize("action.add-message"),ResourceLoaderWrapper.lookupIcon("action.add-message"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.add-message"));
	}
	public static ActionAddMessage getTargetFollower() {
		if (targetFollower == null) {
			targetFollower = new ActionAddMessage();
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
		Object target = TargetManager.getInstance().getModelTarget();
		if (!(Model.getFacade().isAAssociationRole(target))&&Model.getFacade().isACollaboration(Model.getFacade().getNamespace(target))) {
			return;
		}
		this.addMessage(target);
	}
	private void addMessage(Object associationrole) {
	}
	public boolean shouldBeEnabled() {
		Object target = TargetManager.getInstance().getModelTarget();
		return Model.getFacade().isAAssociationRole(target);
	}
}



