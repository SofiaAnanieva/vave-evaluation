package org.argouml.application.events;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.argouml.application.api.ArgoEventListener;
import org.argouml.application.events.ArgoEventTypes;


public final class ArgoEventPump {
	private static final Logger LOG = Logger.getLogger(ArgoEventPump.class);
	private List<Pair>listeners;
	static final ArgoEventPump SINGLETON = new ArgoEventPump();
	public static ArgoEventPump getInstance() {
		return SINGLETON;
	}
	private ArgoEventPump() {
	}
	protected void doAddListener(int event,ArgoEventListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<Pair>();
		}
		synchronized (listeners) {
			listeners.add(new Pair(event,listener));
		}
	}
	protected void doRemoveListener(int event,ArgoEventListener listener) {
		if (listeners == null) {
			return;
		}
		synchronized (listeners) {
			List<Pair>removeList = new ArrayList<Pair>();
			if (event == ArgoEventTypes.ANY_EVENT) {
				for (Pair p:listeners) {
					if (p.listener == listener) {
						removeList.add(p);
					}
				}
			}else {
				Pair test = new Pair(event,listener);
				for (Pair p:listeners) {
					if (p.equals(test)) {
						removeList.add(p);
					}
				}
			}
			listeners.removeAll(removeList);
		}
	}
	public static void addListener(ArgoEventListener listener) {
		SINGLETON.doAddListener(ArgoEventTypes.ANY_EVENT,listener);
	}
	public static void addListener(int event,ArgoEventListener listener) {
		SINGLETON.doAddListener(event,listener);
	}
	public static void removeListener(ArgoEventListener listener) {
		SINGLETON.doRemoveListener(ArgoEventTypes.ANY_EVENT,listener);
	}
	public static void removeListener(int event,ArgoEventListener listener) {
		SINGLETON.doRemoveListener(event,listener);
	}
	private void handleFireNotationEvent(final ArgoNotationEvent event,final ArgoNotationEventListener listener) {
		if (SwingUtilities.isEventDispatchThread()) {
			fireNotationEventInternal(event,listener);
		}else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					fireNotationEventInternal(event,listener);
				}
			});
		}
	}
	private void fireNotationEventInternal(ArgoNotationEvent event,ArgoNotationEventListener listener) {
		switch (event.getEventType()) {case ArgoEventTypes.NOTATION_CHANGED:
			listener.notationChanged(event);
			break;
		case ArgoEventTypes.NOTATION_ADDED:
			listener.notationAdded(event);
			break;
		case ArgoEventTypes.NOTATION_REMOVED:
			listener.notationRemoved(event);
			break;
		case ArgoEventTypes.NOTATION_PROVIDER_ADDED:
			listener.notationProviderAdded(event);
			break;
		case ArgoEventTypes.NOTATION_PROVIDER_REMOVED:
			listener.notationProviderRemoved(event);
			break;
		default:
			LOG.error("Invalid event:" + event.getEventType());
			break;
		}
	}
	private void handleFireDiagramAppearanceEvent(final ArgoDiagramAppearanceEvent event,final ArgoDiagramAppearanceEventListener listener) {
		if (SwingUtilities.isEventDispatchThread()) {
			fireDiagramAppearanceEventInternal(event,listener);
		}else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					fireDiagramAppearanceEventInternal(event,listener);
				}
			});
		}
	}
	private void fireDiagramAppearanceEventInternal(final ArgoDiagramAppearanceEvent event,final ArgoDiagramAppearanceEventListener listener) {
		switch (event.getEventType()) {case ArgoEventTypes.DIAGRAM_FONT_CHANGED:
			listener.diagramFontChanged(event);
			break;
		default:
			LOG.error("Invalid event:" + event.getEventType());
			break;
		}
	}
	private void handleFireHelpEvent(ArgoHelpEvent event,ArgoHelpEventListener listener) {
		switch (event.getEventType()) {case ArgoEventTypes.HELP_CHANGED:
			listener.helpChanged(event);
			break;
		case ArgoEventTypes.HELP_REMOVED:
			listener.helpRemoved(event);
			break;
		default:
			LOG.error("Invalid event:" + event.getEventType());
			break;
		}
	}
	private void handleFireStatusEvent(ArgoStatusEvent event,ArgoStatusEventListener listener) {
		switch (event.getEventType()) {case ArgoEventTypes.STATUS_TEXT:
			listener.statusText(event);
			break;
		case ArgoEventTypes.STATUS_CLEARED:
			listener.statusCleared(event);
			break;
		case ArgoEventTypes.STATUS_PROJECT_SAVED:
			listener.projectSaved(event);
			break;
		case ArgoEventTypes.STATUS_PROJECT_LOADED:
			listener.projectLoaded(event);
			break;
		case ArgoEventTypes.STATUS_PROJECT_MODIFIED:
			listener.projectModified(event);
			break;
		default:
			LOG.error("Invalid event:" + event.getEventType());
			break;
		}
	}
	private void handleFireProfileEvent(ArgoProfileEvent event,ArgoProfileEventListener listener) {
		switch (event.getEventType()) {case ArgoEventTypes.PROFILE_ADDED:
			listener.profileAdded(event);
			break;
		case ArgoEventTypes.PROFILE_REMOVED:
			listener.profileRemoved(event);
			break;
		default:
			LOG.error("Invalid event:" + event.getEventType());
			break;
		}
	}
	private void handleFireGeneratorEvent(ArgoGeneratorEvent event,ArgoGeneratorEventListener listener) {
		switch (event.getEventType()) {case ArgoEventTypes.GENERATOR_CHANGED:
			listener.generatorChanged(event);
			break;
		case ArgoEventTypes.GENERATOR_ADDED:
			listener.generatorAdded(event);
			break;
		case ArgoEventTypes.GENERATOR_REMOVED:
			listener.generatorRemoved(event);
			break;
		default:
			LOG.error("Invalid event:" + event.getEventType());
			break;
		}
	}
	private void handleFireEvent(ArgoEvent event,ArgoEventListener listener) {
		if (event.getEventType() == ArgoEventTypes.ANY_EVENT) {
			if (listener instanceof ArgoNotationEventListener) {
				handleFireNotationEvent((ArgoNotationEvent) event,(ArgoNotationEventListener) listener);
			}
			if (listener instanceof ArgoHelpEventListener) {
				handleFireHelpEvent((ArgoHelpEvent) event,(ArgoHelpEventListener) listener);
			}
			if (listener instanceof ArgoStatusEventListener) {
				handleFireStatusEvent((ArgoStatusEvent) event,(ArgoStatusEventListener) listener);
			}
		}else {
			if (event.getEventType() >= ArgoEventTypes.ANY_NOTATION_EVENT&&event.getEventType() < ArgoEventTypes.LAST_NOTATION_EVENT) {
				if (listener instanceof ArgoNotationEventListener) {
					handleFireNotationEvent((ArgoNotationEvent) event,(ArgoNotationEventListener) listener);
				}
			}
			if (event.getEventType() >= ArgoEventTypes.ANY_DIAGRAM_APPEARANCE_EVENT&&event.getEventType() < ArgoEventTypes.LAST_DIAGRAM_APPEARANCE_EVENT) {
				if (listener instanceof ArgoDiagramAppearanceEventListener) {
					handleFireDiagramAppearanceEvent((ArgoDiagramAppearanceEvent) event,(ArgoDiagramAppearanceEventListener) listener);
				}
			}
			if (event.getEventType() >= ArgoEventTypes.ANY_HELP_EVENT&&event.getEventType() < ArgoEventTypes.LAST_HELP_EVENT) {
				if (listener instanceof ArgoHelpEventListener) {
					handleFireHelpEvent((ArgoHelpEvent) event,(ArgoHelpEventListener) listener);
				}
			}
			if (event.getEventType() >= ArgoEventTypes.ANY_GENERATOR_EVENT&&event.getEventType() < ArgoEventTypes.LAST_GENERATOR_EVENT) {
				if (listener instanceof ArgoGeneratorEventListener) {
					handleFireGeneratorEvent((ArgoGeneratorEvent) event,(ArgoGeneratorEventListener) listener);
				}
			}
			if (event.getEventType() >= ArgoEventTypes.ANY_STATUS_EVENT&&event.getEventType() < ArgoEventTypes.LAST_STATUS_EVENT) {
				if (listener instanceof ArgoStatusEventListener) {
					handleFireStatusEvent((ArgoStatusEvent) event,(ArgoStatusEventListener) listener);
				}
			}
			if (event.getEventType() >= ArgoEventTypes.ANY_PROFILE_EVENT&&event.getEventType() < ArgoEventTypes.LAST_PROFILE_EVENT) {
				if (listener instanceof ArgoProfileEventListener) {
					handleFireProfileEvent((ArgoProfileEvent) event,(ArgoProfileEventListener) listener);
				}
			}
		}
	}
	public static void fireEvent(ArgoEvent event) {
		SINGLETON.doFireEvent(event);
	}
	protected void doFireEvent(ArgoEvent event) {
		if (listeners == null) {
			return;
		}
		List<Pair>readOnlyListeners;
		synchronized (listeners) {
			readOnlyListeners = new ArrayList<Pair>(listeners);
		}
		for (Pair pair:readOnlyListeners) {
			if (pair.getEventType() == ArgoEventTypes.ANY_EVENT) {
				handleFireEvent(event,pair.getListener());
			}else if (pair.getEventType() == event.getEventStartRange()||pair.getEventType() == event.getEventType()) {
				handleFireEvent(event,pair.getListener());
			}
		}
	}
	static class Pair {
	private int eventType;
	private ArgoEventListener listener;
	Pair(int myEventType,ArgoEventListener myListener) {
			eventType = myEventType;
			listener = myListener;
		}
	int getEventType() {
		return eventType;
	}
	ArgoEventListener getListener() {
		return listener;
	}
	@Override public String toString() {
		return"{Pair(" + eventType + "," + listener + ")}";
	}
	@Override public int hashCode() {
		if (listener != null) {
			return eventType + listener.hashCode();
		}
		return eventType;
	}
	@Override public boolean equals(Object o) {
		if (o instanceof Pair) {
			Pair p = (Pair) o;
			if (p.eventType == eventType&&p.listener == listener) {
				return true;
			}
		}
		return false;
	}
}
}



