package org.argouml.model.mdr;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jmi.model.Association;
import javax.jmi.model.AssociationEnd;
import javax.jmi.model.Attribute;
import javax.jmi.model.GeneralizableElement;
import javax.jmi.model.ModelElement;
import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofClass;
import javax.jmi.model.NameNotFoundException;
import javax.jmi.model.Reference;
import javax.jmi.reflect.InvalidObjectException;
import javax.jmi.reflect.RefAssociation;
import javax.jmi.reflect.RefBaseObject;
import javax.jmi.reflect.RefObject;
import org.argouml.model.AbstractModelEventPump;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.mdr.events.AssociationEvent;
import org.netbeans.api.mdr.events.AttributeEvent;
import org.netbeans.api.mdr.events.InstanceEvent;
import org.netbeans.api.mdr.events.MDRChangeEvent;
import org.netbeans.api.mdr.events.MDRPreChangeListener;
import org.netbeans.api.mdr.events.TransactionEvent;
import org.netbeans.api.mdr.events.VetoChangeException;


class ModelEventPumpMDRImpl extends AbstractModelEventPump implements MDRPreChangeListener {
	private static final boolean VETO_READONLY_CHANGES = true;
	private MDRModelImplementation modelImpl;
	private Object registrationMutex = new Byte[0];
	private MDRepository repository;
	private Boolean eventCountMutex = new Boolean(false);
	private int pendingEvents = 0;
	private Thread eventThread;
	private Registry<PropertyChangeListener>elements = new Registry<PropertyChangeListener>();
	private Registry<PropertyChangeListener>listenedClasses = new Registry<PropertyChangeListener>();
	private Map<String,Collection<String>>subtypeMap;
	private Map<String,Collection<String>>propertyNameMap;
	public ModelEventPumpMDRImpl(MDRModelImplementation implementation) {
		this(implementation,MDRManager.getDefault().getDefaultRepository());
	}
	public ModelEventPumpMDRImpl(MDRModelImplementation implementation,MDRepository repo) {
		super();
		modelImpl = implementation;
		repository = repo;
		subtypeMap = buildTypeMap(modelImpl.getModelPackage());
		propertyNameMap = buildPropertyNameMap(modelImpl.getModelPackage());
	}
	public void addModelEventListener(PropertyChangeListener listener,Object modelElement,String[]propertyNames) {
		if (listener == null) {
			throw new IllegalArgumentException("A listener must be supplied");
		}
		if (modelElement == null) {
			throw new IllegalArgumentException("A model element must be supplied");
		}
		registerModelEvent(listener,modelElement,propertyNames);
	}
	public void addModelEventListener(PropertyChangeListener listener,Object modelElement) {
		if (listener == null) {
			throw new IllegalArgumentException("A listener must be supplied");
		}
		if (modelElement == null) {
			throw new IllegalArgumentException("A model element must be supplied");
		}
		registerModelEvent(listener,modelElement,null);
	}
	public void removeModelEventListener(PropertyChangeListener listener,Object modelelement,String[]propertyNames) {
		unregisterModelEvent(listener,modelelement,propertyNames);
	}
	public void removeModelEventListener(PropertyChangeListener listener,Object modelelement) {
		unregisterModelEvent(listener,modelelement,null);
	}
	public void addClassModelEventListener(PropertyChangeListener listener,Object modelClass,String[]propertyNames) {
		registerClassEvent(listener,modelClass,propertyNames);
	}
	public void removeClassModelEventListener(PropertyChangeListener listener,Object modelClass,String[]propertyNames) {
		unregisterClassEvent(listener,modelClass,propertyNames);
	}
	public void change(MDRChangeEvent mdrEvent) {
		if (eventThread == null) {
			eventThread = Thread.currentThread();
		}
		decrementEvents();
		if (mdrEvent instanceof TransactionEvent) {
			return;
		}
		List<UmlChangeEvent>events = new ArrayList<UmlChangeEvent>();
		if (mdrEvent instanceof AttributeEvent) {
			AttributeEvent ae = (AttributeEvent) mdrEvent;
			events.add(new AttributeChangeEvent(ae.getSource(),ae.getAttributeName(),ae.getOldElement(),ae.getNewElement(),mdrEvent));
		}else if (mdrEvent instanceof InstanceEvent&&mdrEvent.isOfType(InstanceEvent.EVENT_INSTANCE_DELETE)) {
			InstanceEvent ie = (InstanceEvent) mdrEvent;
			events.add(new DeleteInstanceEvent(ie.getSource(),"remove",null,null,mdrEvent));
		}else if (mdrEvent instanceof AssociationEvent) {
			AssociationEvent ae = (AssociationEvent) mdrEvent;
			if (ae.isOfType(AssociationEvent.EVENT_ASSOCIATION_ADD)) {
				events.add(new AddAssociationEvent(ae.getNewElement(),mapPropertyName(ae.getEndName()),ae.getOldElement(),ae.getFixedElement(),ae.getFixedElement(),mdrEvent));
				events.add(new AttributeChangeEvent(ae.getNewElement(),mapPropertyName(ae.getEndName()),ae.getOldElement(),ae.getFixedElement(),mdrEvent));
				events.add(new AddAssociationEvent(ae.getFixedElement(),otherAssocEnd(ae),ae.getOldElement(),ae.getNewElement(),ae.getNewElement(),mdrEvent));
				events.add(new AttributeChangeEvent(ae.getFixedElement(),otherAssocEnd(ae),ae.getOldElement(),ae.getNewElement(),mdrEvent));
			}else if (ae.isOfType(AssociationEvent.EVENT_ASSOCIATION_REMOVE)) {
				events.add(new RemoveAssociationEvent(ae.getOldElement(),mapPropertyName(ae.getEndName()),ae.getFixedElement(),ae.getNewElement(),ae.getFixedElement(),mdrEvent));
				events.add(new AttributeChangeEvent(ae.getOldElement(),mapPropertyName(ae.getEndName()),ae.getFixedElement(),ae.getNewElement(),mdrEvent));
				events.add(new RemoveAssociationEvent(ae.getFixedElement(),otherAssocEnd(ae),ae.getOldElement(),ae.getNewElement(),ae.getOldElement(),mdrEvent));
				events.add(new AttributeChangeEvent(ae.getFixedElement(),otherAssocEnd(ae),ae.getOldElement(),ae.getNewElement(),mdrEvent));
			}
		}
		for (UmlChangeEvent event:events) {
			fire(event);
			if (event instanceof DeleteInstanceEvent) {
				elements.unregister(null,((RefBaseObject) event.getSource()).refMofId(),null);
			}
		}
	}
	private boolean isReadOnly(RefBaseObject object) {
		return modelImpl.isReadOnly(object.refOutermostPackage());
	}
	public void plannedChange(MDRChangeEvent e) {
		if (VETO_READONLY_CHANGES) {
			if (e instanceof InstanceEvent) {
				if (e.isOfType(InstanceEvent.EVENT_INSTANCE_CREATE)) {
					RefBaseObject element = (RefBaseObject) ((InstanceEvent) e).getSource();
					if (isReadOnly(element)) {
						throw new VetoChangeException(e.getSource(),null);
					}
				}else {
					RefObject element = ((InstanceEvent) e).getInstance();
					if (isReadOnly(element)) {
						throw new VetoChangeException(e.getSource(),element);
					}
				}
			}else if (e instanceof AssociationEvent) {
			}else if (e instanceof AttributeEvent) {
				RefObject element = (RefObject) ((AttributeEvent) e).getSource();
				if (isReadOnly(element)) {
					throw new VetoChangeException(element,element);
				}
			}
		}
		synchronized (eventCountMutex) {
			pendingEvents++;
		}
	}
	public void changeCancelled(MDRChangeEvent e) {
		decrementEvents();
	}
	private void decrementEvents() {
		synchronized (eventCountMutex) {
			pendingEvents--;
			if (pendingEvents <= 0) {
				eventCountMutex.notifyAll();
			}
		}
	}
	private void fire(UmlChangeEvent event) {
		String mofId = ((RefBaseObject) event.getSource()).refMofId();
		String className = getClassName(event.getSource());
		Set<PropertyChangeListener>listeners = new HashSet<PropertyChangeListener>();
		synchronized (registrationMutex) {
			listeners.addAll(elements.getMatches(mofId,event.getPropertyName()));
			listeners.addAll(listenedClasses.getMatches(className,event.getPropertyName()));
		}
		if (!listeners.isEmpty()) {
			for (PropertyChangeListener pcl:listeners) {
				pcl.propertyChange(event);
			}
		}else {
		}
	}
	private void registerModelEvent(PropertyChangeListener listener,Object modelElement,String[]propertyNames) {
		if (listener == null||modelElement == null) {
			throw new IllegalArgumentException("Neither listener (" + listener + ") or modelElement (" + modelElement + ") can be null! [Property names: " + propertyNames + "]");
		}
		String mofId = ((RefBaseObject) modelElement).refMofId();
		try {
			verifyAttributeNames(((RefBaseObject) modelElement).refMetaObject(),propertyNames);
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		synchronized (registrationMutex) {
			elements.register(listener,mofId,propertyNames);
		}
	}
	private void unregisterModelEvent(PropertyChangeListener listener,Object modelElement,String[]propertyNames) {
		if (listener == null||modelElement == null) {
			return;
		}
		if (!(modelElement instanceof RefBaseObject)) {
			return;
		}
		String mofId = ((RefBaseObject) modelElement).refMofId();
		synchronized (registrationMutex) {
			elements.unregister(listener,mofId,propertyNames);
		}
	}
	private void registerClassEvent(PropertyChangeListener listener,Object modelClass,String[]propertyNames) {
		if (modelClass instanceof Class) {
			String className = getClassName(modelClass);
			Collection<String>subtypes = subtypeMap.get(className);
			verifyAttributeNames(className,propertyNames);
			synchronized (registrationMutex) {
				listenedClasses.register(listener,className,propertyNames);
				for (String subtype:subtypes) {
					listenedClasses.register(listener,subtype,propertyNames);
				}
			}
			return;
		}
		throw new IllegalArgumentException("Don\'t know how to register class event for object " + modelClass);
	}
	private void unregisterClassEvent(PropertyChangeListener listener,Object modelClass,String[]propertyNames) {
		if (modelClass instanceof Class) {
			String className = getClassName(modelClass);
			Collection<String>subtypes = subtypeMap.get(className);
			synchronized (registrationMutex) {
				listenedClasses.unregister(listener,className,propertyNames);
				for (String subtype:subtypes) {
					listenedClasses.unregister(listener,subtype,propertyNames);
				}
			}
			return;
		}
		throw new IllegalArgumentException("Don\'t know how to unregister class event for object " + modelClass);
	}
	private String getClassName(Object elementOrClass) {
		return modelImpl.getMetaTypes().getName(elementOrClass);
	}
	public void startPumpingEvents() {
		repository.addListener(this);
	}
	public void stopPumpingEvents() {
		repository.removeListener(this);
	}
	public void flushModelEvents() {
		while (true) {
			synchronized (eventCountMutex) {
				if (pendingEvents <= 0||Thread.currentThread().equals(eventThread)) {
					return;
				}
				try {
					eventCountMutex.wait();
				}catch (InterruptedException e) {
				}
			}
		}
	}
	private String otherAssocEnd(AssociationEvent ae) {
		RefAssociation ra = (RefAssociation) ae.getSource();
		Association a = (Association) ra.refMetaObject();
		AssociationEnd aend = null;
		try {
			aend = (AssociationEnd) a.lookupElementExtended(ae.getEndName());
		}catch (NameNotFoundException e) {
			return null;
		}
		return aend.otherEnd().getName();
	}
	private static String mapPropertyName(String name) {
		if ("typedParameter".equals(name)) {
			return"parameter";
		}
		if ("typedFeature".equals(name)) {
			return"feature";
		}
		return name;
	}
	private String formatArray(String[]array) {
		if (array == null) {
			return null;
		}
		String result = "[";
		for (int i = 0;i < array.;i++) {
			result = result + array[i] + ", ";
		}
		return result.substring(0,result.length() - 2) + "]";
	}
	private String formatElement(Object element) {
		try {
			if (element instanceof RefBaseObject) {
				return modelImpl.getMetaTypes().getName(element) + "<" + ((RefBaseObject) element).refMofId() + ">";
			}else if (element != null) {
				return element.toString();
			}
		}catch (InvalidObjectException e) {
			return modelImpl.getMetaTypes().getName(element) + "<deleted>";
		}
		return null;
	}
	private Map<String,Collection<String>>buildTypeMap(ModelPackage extent) {
		Map<String,Collection<String>>names = new HashMap<String,Collection<String>>();
		for (Object metaclass:extent.getMofClass().refAllOfClass()) {
			ModelElement element = (ModelElement) metaclass;
			String name = element.getName();
			if (!names.containsKey(name)) {
				names.put(name,getSubtypes(extent,element));
			}
		}
		return names;
	}
	private Collection<String>getSubtypes(ModelPackage extent,ModelElement me) {
		Collection<String>allSubtypes = new HashSet<String>();
		if (me instanceof GeneralizableElement) {
			GeneralizableElement ge = (GeneralizableElement) me;
			Collection<ModelElement>subtypes = extent.getGeneralizes().getSubtype(ge);
			for (ModelElement st:subtypes) {
				allSubtypes.add(st.getName());
				allSubtypes.addAll(getSubtypes(extent,st));
			}
		}
		return allSubtypes;
	}
	private Map<String,Collection<String>>buildPropertyNameMap(ModelPackage extent) {
		Map<String,Collection<String>>names = new HashMap<String,Collection<String>>();
		for (Reference reference:(Collection<Reference>) extent.getReference().refAllOfClass()) {
			mapAssociationEnd(names,reference.getExposedEnd());
			mapAssociationEnd(names,reference.getReferencedEnd());
		}
		for (Attribute attribute:(Collection<Attribute>) extent.getAttribute().refAllOfClass()) {
			mapPropertyName(names,attribute.getContainer(),attribute.getName());
		}
		return names;
	}
	private void mapAssociationEnd(Map<String,Collection<String>>names,AssociationEnd end) {
		ModelElement type = end.otherEnd().getType();
		mapPropertyName(names,type,end.getName());
	}
	private boolean mapPropertyName(Map<String,Collection<String>>names,ModelElement type,String propertyName) {
		String typeName = type.getName();
		boolean added = mapPropertyName(names,typeName,propertyName);
		Collection<String>subtypes = subtypeMap.get(typeName);
		if (subtypes != null) {
			for (String subtype:subtypes) {
				added &= mapPropertyName(names,subtype,propertyName);
			}
		}
		return added;
	}
	private boolean mapPropertyName(Map<String,Collection<String>>names,String typeName,String propertyName) {
		if (!names.containsKey(typeName)) {
			names.put(typeName,new HashSet<String>());
		}
		Collection<String>tempcoll = names.get(typeName);
		boolean added = tempcoll.add(propertyName);
		return added;
	}
	private void verifyAttributeNames(String className,String[]attributes) {
		RefObject ro = null;
		verifyAttributeNames(ro,attributes);
	}
	private void verifyAttributeNames(RefObject metaobject,String[]attributes) {
	}
	@SuppressWarnings("unchecked")public List getDebugInfo() {
		List info = new ArrayList();
		info.add("Event Listeners");
		for (Iterator it = elements.registry.entrySet().iterator();it.hasNext();) {
			Map.
				Entry entry = (Map.Entry) it.next();
			String item = entry.getKey().toString();
			List modelElementNode = newDebugNode(getDebugDescription(item));
			info.add(modelElementNode);
			Map propertyMap = (Map) entry.getValue();
			for (Iterator propertyIterator = propertyMap.entrySet().iterator();propertyIterator.hasNext();) {
				Map.
					Entry propertyEntry = (Map.Entry) propertyIterator.next();
				List propertyNode = newDebugNode(propertyEntry.getKey().toString());
				modelElementNode.add(propertyNode);
				List listenerList = (List) propertyEntry.getValue();
				for (Iterator listIt = listenerList.iterator();listIt.hasNext();) {
					Object listener = listIt.next();
					List listenerNode = newDebugNode(listener.getClass().getName());
					propertyNode.add(listenerNode);
				}
			}
		}
		return info;
	}
	private List<String>newDebugNode(String name) {
		List<String>list = new ArrayList<String>();
		list.add(name);
		return list;
	}
	private String getDebugDescription(String mofId) {
		Object modelElement = repository.getByMofId(mofId);
		String name = Model.getFacade().getName(modelElement);
		if (name != null&&name.trim().length() != 0) {
			return"\"" + name + "\" - " + modelElement.toString();
		}else {
			return modelElement.toString();
		}
	}
}

class Registry<T> {
	Map<String,Map<String,List<T>>>registry;
	Registry() {
			registry = Collections.synchronizedMap(new HashMap<String,Map<String,List<T>>>());
		}
	void register(T item,String key,String[]subkeys) {
		Map<String,List<T>>entry = registry.get(key);
		if (entry == null) {
			entry = new HashMap<String,List<T>>();
			registry.put(key,entry);
		}
		if (subkeys == null||subkeys. < 1) {
			subkeys = new String[] {""};
		}
		for (int i = 0;i < subkeys.;i++) {
			List<T>list = entry.get(subkeys[i]);
			if (list == null) {
				list = new ArrayList<T>();
				entry.put(subkeys[i],list);
			}
			if (!list.contains(item)) {
				list.add(item);
			}
		}
	}
	void unregister(T item,String key,String[]subkeys) {
		Map<String,List<T>>entry = registry.get(key);
		if (entry == null) {
			return;
		}
		if (subkeys != null&&subkeys. > 0) {
			for (int i = 0;i < subkeys.;i++) {
				lookupRemoveItem(entry,subkeys[i],item);
			}
		}else {
			if (item == null) {
				registry.remove(key);
			}else {
				lookupRemoveItem(entry,"",item);
			}
		}
	}
	private void lookupRemoveItem(Map<String,List<T>>map,String key,T item) {
		List<T>list = map.get(key);
		if (list == null) {
			return;
		}
		if (item == null) {
			map.remove(key);
			return;
		}
		while (list.contains(item)) {
			list.remove(item);
		}
		if (list.isEmpty()) {
			map.remove(key);
		}
	}
	Collection<T>getMatches(String key,String subkey) {
		List<T>results = new ArrayList<T>();
		Map<String,List<T>>entry = registry.get(key);
		if (entry != null) {
			if (entry.containsKey(subkey)) {
			}
			if (entry.containsKey("")) {
			}
		}
		return results;
	}
}



