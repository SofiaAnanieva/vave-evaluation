package org.argouml.model.euml;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.argouml.model.AbstractModelEventPump;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.RemoveAssociationEvent;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.argouml.model.euml.EUMLModelImplementation;


class ModelEventPumpEUMLImpl extends AbstractModelEventPump {
	private class Listener {
	private PropertyChangeListener listener;
	private Set<String>props;
	Listener(PropertyChangeListener listener,String[]properties) {
			this.listener = listener;
			if (properties != null) {
				setProperties(properties);
			}
		}
	void setProperties(String[]properties) {
		if (properties == null) {
			props = null;
		}else {
			if (props == null) {
				props = new HashSet<String>();
			}
			for (String s:properties) {
				props.add(s);
			}
		}
	}
	void removeProperties(String[]properties) {
		if (props == null) {
			return;
		}
		for (String s:properties) {
			props.remove(s);
		}
	}
	PropertyChangeListener getListener() {
		return listener;
	}
	Set<String>getProperties() {
		return props;
	}
}
	private EUMLModelImplementation modelImpl;
	private RootContainerAdapter rootContainerAdapter = new RootContainerAdapter(this);
	private Map<Object,List<Listener>>registerForElements = new HashMap<Object,List<Listener>>();
	private Map<Object,List<Listener>>registerForClasses = new LinkedHashMap<Object,List<Listener>>();
	private Object mutex;
	private static final Logger LOG = Logger.getLogger(ModelEventPumpEUMLImpl.class);
	public static final int COMMAND_STACK_UPDATE = Notification.EVENT_TYPE_COUNT + 1;
	public ModelEventPumpEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
		mutex = this;
		implementation.getEditingDomain().getCommandStack().addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				notifyChanged(new NotificationImpl(COMMAND_STACK_UPDATE,false,false));
			}
		});
	}
	public void setRootContainer(Notifier container) {
		rootContainerAdapter.setRootContainer(container);
	}
	public RootContainerAdapter getRootContainer() {
		return rootContainerAdapter;
	}
	public void addClassModelEventListener(PropertyChangeListener listener,Object modelClass,String[]propertyNames) {
		if (!(modelClass instanceof Class&&EObject.class.isAssignableFrom((Class) modelClass))) {
			throw new IllegalArgumentException("The model class must be instance of " + "java.lang.Class<EObject>");
		}
		registerListener(modelClass,listener,propertyNames,registerForClasses);
	}
	public void addModelEventListener(PropertyChangeListener listener,Object modelelement,String[]propertyNames) {
		if (!(modelelement instanceof EObject)) {
			throw new IllegalArgumentException("The modelelement must be instance " + "of EObject.");
		}
		registerListener(modelelement,listener,propertyNames,registerForElements);
	}
	public void addModelEventListener(PropertyChangeListener listener,Object modelelement) {
		addModelEventListener(listener,modelelement,(String[]) null);
	}
	private void registerListener(Object notifier,PropertyChangeListener listener,String[]propertyNames,Map<Object,List<Listener>>register) {
		if (notifier == null||listener == null) {
			throw new NullPointerException("The model element/class and the " + "listener must be non-null.");
		}
		synchronized (mutex) {
			List<Listener>list = register.get(notifier);
			boolean found = false;
			if (list == null) {
				list = new ArrayList<Listener>();
			}else {
				for (Listener l:list) {
					if (l.getListener() == listener) {
						l.setProperties(propertyNames);
						found = true;
						break;
					}
				}
			}
			if (!found) {
				list.add(new Listener(listener,propertyNames));
				register.put(notifier,list);
			}
		}
	}
	public void flushModelEvents() {
	}
	public void removeClassModelEventListener(PropertyChangeListener listener,Object modelClass,String[]propertyNames) {
		if (!(modelClass instanceof Class&&EObject.class.isAssignableFrom((Class) modelClass))) {
			throw new IllegalArgumentException();
		}
		unregisterListener(modelClass,listener,propertyNames,registerForClasses);
	}
	public void removeModelEventListener(PropertyChangeListener listener,Object modelelement,String[]propertyNames) {
		if (!(modelelement instanceof EObject)) {
			throw new IllegalArgumentException();
		}
		unregisterListener(modelelement,listener,propertyNames,registerForElements);
	}
	public void removeModelEventListener(PropertyChangeListener listener,Object modelelement) {
		removeModelEventListener(listener,modelelement,(String[]) null);
	}
	private void unregisterListener(Object notifier,PropertyChangeListener listener,String[]propertyNames,Map<Object,List<Listener>>register) {
		if (notifier == null||listener == null) {
			throw new NullPointerException("The model element/class and the " + "listener must be non-null.");
		}
		synchronized (mutex) {
			List<Listener>list = register.get(notifier);
			if (list == null) {
				return;
			}
			Iterator<Listener>iter = list.iterator();
			while (iter.hasNext()) {
				Listener l = iter.next();
				if (l.getListener() == listener) {
					if (propertyNames != null) {
						l.removeProperties(propertyNames);
					}else {
						iter.remove();
					}
					break;
				}
			}
		}
	}
	public void notifyChanged(Notification notification) {
		if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
			return;
		}
		ENamedElement feature = (ENamedElement) notification.getFeature();
		String featureName = feature == null?"":feature.getName();
		String oldValue = notification.getOldValue() != null?notification.getOldValue().toString():"Null";
		String newValue = notification.getNewValue() != null?notification.getNewValue().toString():"Null";
		LOG.debug("event  - Property: " + featureName + " Old: " + oldValue + " New: " + newValue);
		class EventAndListeners {
			public EventAndListeners(PropertyChangeEvent e,List<PropertyChangeListener>l) {
				event = e;
				listeners = l;
			}
			private PropertyChangeEvent event;
			private List<PropertyChangeListener>listeners;
		}
		List<EventAndListeners>events = new ArrayList<EventAndListeners>();
		if (notification.getEventType() == Notification.SET) {
			if (notification.getFeature()instanceof ENamedElement) {
				String propName = mapPropertyName(((ENamedElement) notification.getFeature()).getName());
				events.add(new EventAndListeners(new AttributeChangeEvent(notification.getNotifier(),propName,notification.getOldValue(),notification.getNewValue(),null),getListeners(notification.getNotifier(),propName)));
			}
		}else if (notification.getEventType() == Notification.ADD||notification.getEventType() == Notification.REMOVE) {
			if (notification.getFeature()instanceof EReference) {
				EReference ref = (EReference) notification.getFeature();
				String propName = mapPropertyName(ref.getName());
				if (notification.getEventType() == Notification.ADD) {
					events.add(new EventAndListeners(new AddAssociationEvent(notification.getNotifier(),propName,null,notification.getNewValue(),notification.getNewValue(),null),getListeners(notification.getNotifier(),propName)));
					events.add(new EventAndListeners(new AttributeChangeEvent(notification.getNotifier(),propName,null,notification.getNewValue(),null),getListeners(notification.getNotifier(),propName)));
				}else {
					events.add(new EventAndListeners(new DeleteInstanceEvent(notification.getOldValue(),"remove",null,null,null),getListeners(notification.getOldValue())));
					events.add(new EventAndListeners(new RemoveAssociationEvent(notification.getNotifier(),propName,notification.getOldValue(),null,notification.getOldValue(),null),getListeners(notification.getNotifier(),propName)));
					events.add(new EventAndListeners(new AttributeChangeEvent(notification.getNotifier(),propName,notification.getOldValue(),null,null),getListeners(notification.getNotifier(),propName)));
				}
				EReference oppositeRef = ref.getEOpposite();
				if (oppositeRef != null) {
					propName = mapPropertyName(oppositeRef.getName());
					if (notification.getEventType() == Notification.ADD) {
						events.add(new EventAndListeners(new AddAssociationEvent(notification.getNewValue(),propName,null,notification.getNotifier(),notification.getNotifier(),null),getListeners(notification.getNewValue(),propName)));
						events.add(new EventAndListeners(new AttributeChangeEvent(notification.getNewValue(),propName,null,notification.getNotifier(),null),getListeners(notification.getNewValue(),propName)));
					}else {
						events.add(new EventAndListeners(new AddAssociationEvent(notification.getOldValue(),propName,notification.getNotifier(),null,notification.getNotifier(),null),getListeners(notification.getOldValue(),propName)));
						events.add(new EventAndListeners(new AttributeChangeEvent(notification.getOldValue(),propName,notification.getNotifier(),null,null),getListeners(notification.getOldValue(),propName)));
					}
				}
			}
		}
		for (EventAndListeners e:events) {
			if (e.listeners != null) {
				for (PropertyChangeListener l:e.listeners) {
					l.propertyChange(e.event);
				}
			}
		}
	}
	private List<PropertyChangeListener>getListeners(Object element) {
		return getListeners(element,null);
	}
	@SuppressWarnings("unchecked")private List<PropertyChangeListener>getListeners(Object element,String propName) {
		List<PropertyChangeListener>returnedList = new ArrayList<PropertyChangeListener>();
		synchronized (mutex) {
			addListeners(returnedList,element,propName,registerForElements);
			for (Object o:registerForClasses.keySet()) {
				if (o instanceof Class) {
					Class type = (Class) o;
					if (type.isAssignableFrom(element.getClass())) {
						addListeners(returnedList,o,propName,registerForClasses);
					}
				}
			}
		}
		return returnedList.isEmpty()?null:returnedList;
	}
	private void addListeners(List<PropertyChangeListener>listeners,Object element,String propName,Map<Object,List<Listener>>register) {
		List<Listener>list = register.get(element);
		if (list != null) {
			for (Listener l:list) {
				if (propName == null||l.getProperties() == null||l.getProperties().contains(propName)) {
					listeners.add(l.getListener());
				}
			}
		}
	}
	public void startPumpingEvents() {
		rootContainerAdapter.setDeliverEvents(true);
	}
	public void stopPumpingEvents() {
		rootContainerAdapter.setDeliverEvents(false);
	}
	private String mapPropertyName(String name) {
		if (name.equals("ownedAttribute")) {
			return"feature";
		}
		return name;
	}
	@SuppressWarnings("unchecked")public List getDebugInfo() {
		List info = new ArrayList();
		info.add("Event Listeners");
		for (Iterator it = registerForElements.entrySet().iterator();it.hasNext();) {
			Map.
				Entry entry = (Map.Entry) it.next();
			String item = entry.getKey().toString();
			List modelElementNode = newDebugNode(item);
			info.add(modelElementNode);
			List listenerList = (List) entry.getValue();
			Map<String,List<String>>map = new HashMap<String,List<String>>();
			for (Iterator listIt = listenerList.iterator();listIt.hasNext();) {
				Listener listener = (Listener) listIt.next();
				if (listener.getProperties() != null) {
					for (String eventName:listener.getProperties()) {
						if (!map.containsKey(eventName)) {
							map.put(eventName,new LinkedList<String>());
						}
						List<String>templist = map.get(eventName);
						templist.add(listener.getListener().getClass().getName());
					}
				}else {
					if (!map.containsKey("")) {
						map.put("",new LinkedList<String>());
					}
					List<String>templist = map.get("");
					templist.add(listener.getListener().getClass().getName());
				}
			}
			for (Map.Entry o:map.entrySet()) {
				modelElementNode.add((String) o.getKey());
				modelElementNode.add((List<String>) o.getValue());
			}
		}
		return info;
	}
	private List<String>newDebugNode(String name) {
		List<String>list = new ArrayList<String>();
		list.add(name);
		return list;
	}
}



