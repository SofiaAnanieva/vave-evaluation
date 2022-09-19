package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.Icon;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetManager;


public class PropPanelPseudostate extends PropPanelStateVertex {
	private static final long serialVersionUID = 5822284822242536007l;
	public PropPanelPseudostate() {
		super("label.pseudostate",lookupIcon("State"));
		addField("label.name",getNameTextField());
		addField("label.container",getContainerScroll());
		addSeparator();
		addField("label.incoming",getIncomingScroll());
		addField("label.outgoing",getOutgoingScroll());
		TargetManager.getInstance().addTargetListener(this);
	}
	public void refreshTarget() {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isAPseudostate(target)) {
			Object kind = Model.getFacade().getKind(target);
			if (Model.getFacade().equalsPseudostateKind(kind,Model.getPseudostateKind().getFork())) {
				getTitleLabel().setText(Translator.localize("label.pseudostate.fork"));
			}
			if (Model.getFacade().equalsPseudostateKind(kind,Model.getPseudostateKind().getJoin())) {
				getTitleLabel().setText(Translator.localize("label.pseudostate.join"));
			}
			if (Model.getFacade().equalsPseudostateKind(kind,Model.getPseudostateKind().getChoice())) {
				getTitleLabel().setText(Translator.localize("label.pseudostate.choice"));
			}
			if (Model.getFacade().equalsPseudostateKind(kind,Model.getPseudostateKind().getDeepHistory())) {
				getTitleLabel().setText(Translator.localize("label.pseudostate.deephistory"));
			}
			if (Model.getFacade().equalsPseudostateKind(kind,Model.getPseudostateKind().getShallowHistory())) {
				getTitleLabel().setText(Translator.localize("label.pseudostate.shallowhistory"));
			}
			if (Model.getFacade().equalsPseudostateKind(kind,Model.getPseudostateKind().getInitial())) {
				getTitleLabel().setText(Translator.localize("label.pseudostate.initial"));
			}
			if (Model.getFacade().equalsPseudostateKind(kind,Model.getPseudostateKind().getJunction())) {
				getTitleLabel().setText(Translator.localize("label.pseudostate.junction"));
			}
			Icon icon = ResourceLoaderWrapper.getInstance().lookupIcon(target);
			if (icon != null) {
				getTitleLabel().setIcon(icon);
			}
		}
	}
	@Override public void targetAdded(TargetEvent e) {
		if (Model.getFacade().isAPseudostate(e.getNewTarget())) {
			refreshTarget();
			super.targetAdded(e);
		}
	}
	@Override public void targetRemoved(TargetEvent e) {
		if (Model.getFacade().isAPseudostate(e.getNewTarget())) {
			refreshTarget();
			super.targetRemoved(e);
		}
	}
	@Override public void targetSet(TargetEvent e) {
		if (Model.getFacade().isAPseudostate(e.getNewTarget())) {
			refreshTarget();
			super.targetSet(e);
		}
	}
}



