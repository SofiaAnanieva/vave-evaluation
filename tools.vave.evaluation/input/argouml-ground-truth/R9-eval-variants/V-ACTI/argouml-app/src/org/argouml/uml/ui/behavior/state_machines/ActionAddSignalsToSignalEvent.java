package org.argouml.uml.ui.behavior.state_machines;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.behavior.state_machines.ActionAddSignalsToSignalEvent;


class ActionAddSignalsToSignalEvent extends AbstractActionAddModelElement2 {
	public static final ActionAddSignalsToSignalEvent SINGLETON = new ActionAddSignalsToSignalEvent();
	protected ActionAddSignalsToSignalEvent() {
		super();
		setMultiSelect(false);
	}
	protected List getChoices() {
		List vec = new ArrayList();
		vec.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(Model.getFacade().getModel(getTarget()),Model.getMetaTypes().getSignal()));
		return vec;
	}
	protected List getSelected() {
		List vec = new ArrayList();
		Object signal = Model.getFacade().getSignal(getTarget());
		if (signal != null) {
			vec.add(signal);
		}
		return vec;
	}
	protected String getDialogTitle() {
		return Translator.localize("dialog.title.add-signal");
	}
	@Override protected void doIt(Collection selected) {
		Object event = getTarget();
		if (selected == null||selected.size() == 0) {
			Model.getCommonBehaviorHelper().setSignal(event,null);
		}else {
			Model.getCommonBehaviorHelper().setSignal(event,selected.iterator().next());
		}
	}
	private static final long serialVersionUID = 6890869588365483936l;
}



