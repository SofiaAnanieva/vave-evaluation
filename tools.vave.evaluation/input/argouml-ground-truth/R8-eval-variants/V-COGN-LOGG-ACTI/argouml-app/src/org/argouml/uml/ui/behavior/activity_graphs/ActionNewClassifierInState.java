package org.argouml.uml.ui.behavior.activity_graphs;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.undo.UndoableAction;


class ActionNewClassifierInState extends UndoableAction {
	private Object choiceClass = Model.getMetaTypes().getState();
	public ActionNewClassifierInState() {
		super();
	}
	public void actionPerformed(ActionEvent e) {
		Object ofs = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isAObjectFlowState(ofs)) {
			Object type = Model.getFacade().getType(ofs);
			if (Model.getFacade().isAClassifierInState(type)) {
				type = Model.getFacade().getType(type);
			}
			if (Model.getFacade().isAClassifier(type)) {
				Collection c = Model.getModelManagementHelper().getAllModelElementsOfKindWithModel(type,choiceClass);
				Collection states = new ArrayList(c);
				PropPanelObjectFlowState.removeTopStateFrom(states);
				if (states.size() < 1)return;
				Object state = pickNicestStateFrom(states);
				if (state != null) {
					states.clear();
					states.add(state);
				}
				super.actionPerformed(e);
				Object cis = Model.getActivityGraphsFactory().buildClassifierInState(type,states);
				Model.getCoreHelper().setType(ofs,cis);
				TargetManager.getInstance().setTarget(cis);
			}
		}
	}
	public boolean isEnabled() {
		boolean isEnabled = false;
		Object t = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isAObjectFlowState(t)) {
			Object type = Model.getFacade().getType(t);
			if (Model.getFacade().isAClassifier(type)) {
				if (!Model.getFacade().isAClassifierInState(type)) {
					Collection states = Model.getModelManagementHelper().getAllModelElementsOfKindWithModel(type,choiceClass);
					if (states.size() > 0) {
						isEnabled = true;
					}
				}
			}
		}
		return isEnabled;
	}
	private Object pickNicestStateFrom(Collection states) {
		if (states.size() < 2)return states.iterator().next();
		Collection simples = new ArrayList();
		Collection composites = new ArrayList();
		Iterator i;
		i = states.iterator();
		while (i.hasNext()) {
			Object st = i.next();
			String name = Model.getFacade().getName(st);
			if (Model.getFacade().isASimpleState(st)&&!Model.getFacade().isAObjectFlowState(st)) {
				simples.add(st);
				if (name != null&&(name.length() > 0)) {
					return st;
				}
			}
		}
		i = states.iterator();
		while (i.hasNext()) {
			Object st = i.next();
			String name = Model.getFacade().getName(st);
			if (Model.getFacade().isACompositeState(st)&&!Model.getFacade().isASubmachineState(st)) {
				composites.add(st);
				if (name != null&&(name.length() > 0)) {
					return st;
				}
			}
		}
		if (simples.size() > 0) {
			return simples.iterator().next();
		}
		if (composites.size() > 0) {
			return composites.iterator().next();
		}
		return states.iterator().next();
	}
}



