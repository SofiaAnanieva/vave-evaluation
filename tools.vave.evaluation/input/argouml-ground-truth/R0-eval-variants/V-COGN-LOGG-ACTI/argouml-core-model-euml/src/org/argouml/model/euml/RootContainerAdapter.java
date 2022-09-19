package org.argouml.model.euml;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.argouml.model.euml.ModelEventPumpEUMLImpl;


public class RootContainerAdapter extends EContentAdapter {
	private List<Notifier>notifiers = new ArrayList<Notifier>();
	private List<Notification>events = new ArrayList<Notification>();
	private Notifier rootContainer;
	private ModelEventPumpEUMLImpl pump;
	private boolean deliverEvents = true;
	private boolean holdEvents = false;
	public RootContainerAdapter(ModelEventPumpEUMLImpl pump) {
		super();
		this.pump = pump;
	}
	public void setDeliverEvents(boolean value) {
		deliverEvents = value;
	}
	public void setRootContainer(Notifier n) {
		if (n == rootContainer) {
			return;
		}
		removeAllAdapters();
		if (n != null) {
			rootContainer = n;
			rootContainer.eAdapters().add(this);
		}
	}
	@Override protected void addAdapter(Notifier notifier) {
		notifiers.add(notifier);
		super.addAdapter(notifier);
	}
	@Override protected void removeAdapter(Notifier notifier) {
		notifiers.remove(notifier);
		super.removeAdapter(notifier);
	}
	public void removeAllAdapters() {
		List<Notifier>notifiersToRemove = new ArrayList<Notifier>(notifiers);
		for (Notifier n:notifiersToRemove) {
			super.removeAdapter(n);
		}
		if (rootContainer != null) {
			super.removeAdapter(rootContainer);
			rootContainer = null;
		}
		notifiers.clear();
	}
	@Override public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		if (deliverEvents) {
			if (holdEvents) {
				events.add(notification);
			}else {
				pump.notifyChanged(notification);
			}
		}
	}
	public void clearHeldEvents() {
		events.clear();
	}
	public void setHoldEvents(boolean value) {
		if (value == false) {
			if (deliverEvents) {
				for (Notification n:events) {
					pump.notifyChanged(n);
				}
			}
			events.clear();
		}
		holdEvents = value;
	}
}



