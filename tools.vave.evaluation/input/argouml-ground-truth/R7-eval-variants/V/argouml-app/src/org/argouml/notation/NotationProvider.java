package org.argouml.notation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.argouml.model.Model;


public abstract class NotationProvider {
	private static final String LIST_SEPARATOR = ", ";
	private final Collection<Object[]>listeners = new ArrayList<Object[]>();
	public abstract String getParsingHelp();
	public static boolean isValue(final String key,final Map map) {
		if (map == null) {
			return false;
		}
		Object o = map.get(key);
		if (!(o instanceof Boolean)) {
			return false;
		}
		return((Boolean) o).booleanValue();
	}
	public abstract void parse(Object modelElement,String text);
	@Deprecated public abstract String toString(Object modelElement,Map args);
	public String toString(Object modelElement,NotationSettings settings) {
		return toString(modelElement,Collections.emptyMap());
	}
	public void initialiseListener(PropertyChangeListener listener,Object modelElement) {
		addElementListener(listener,modelElement);
	}
	public void cleanListener(final PropertyChangeListener listener,final Object modelElement) {
		removeAllElementListeners(listener);
	}
	public void updateListener(final PropertyChangeListener listener,Object modelElement,PropertyChangeEvent pce) {
		if (Model.getUmlFactory().isRemoved(modelElement)) {
			return;
		}
		cleanListener(listener,modelElement);
		initialiseListener(listener,modelElement);
	}
	protected final void addElementListener(PropertyChangeListener listener,Object element) {
		if (Model.getUmlFactory().isRemoved(element)) {
			return;
		}
		Object[]entry = new Object[] {element,null};
		if (!listeners.contains(entry)) {
			listeners.add(entry);
			Model.getPump().addModelEventListener(listener,element);
		}
	}
	protected final void addElementListener(PropertyChangeListener listener,Object element,String property) {
		if (Model.getUmlFactory().isRemoved(element)) {
			return;
		}
		Object[]entry = new Object[] {element,property};
		if (!listeners.contains(entry)) {
			listeners.add(entry);
			Model.getPump().addModelEventListener(listener,element,property);
		}
	}
	protected final void addElementListener(PropertyChangeListener listener,Object element,String[]property) {
		if (Model.getUmlFactory().isRemoved(element)) {
			return;
		}
		Object[]entry = new Object[] {element,property};
		if (!listeners.contains(entry)) {
			listeners.add(entry);
			Model.getPump().addModelEventListener(listener,element,property);
		}
	}
	protected final void removeElementListener(PropertyChangeListener listener,Object element) {
		listeners.remove(new Object[] {element,null});
		Model.getPump().removeModelEventListener(listener,element);
	}
	protected final void removeAllElementListeners(PropertyChangeListener listener) {
		for (Object[]lis:listeners) {
			Object property = lis[1];
			if (property == null) {
				Model.getPump().removeModelEventListener(listener,lis[0]);
			}else if (property instanceof String[]) {
				Model.getPump().removeModelEventListener(listener,lis[0],(String[]) property);
			}else if (property instanceof String) {
				Model.getPump().removeModelEventListener(listener,lis[0],(String) property);
			}else {
				throw new RuntimeException("Internal error in removeAllElementListeners");
			}
		}
		listeners.clear();
	}
	protected StringBuilder formatNameList(Collection modelElements) {
		return formatNameList(modelElements,LIST_SEPARATOR);
	}
	protected StringBuilder formatNameList(Collection modelElements,String separator) {
		StringBuilder result = new StringBuilder();
		for (Object element:modelElements) {
			String name = Model.getFacade().getName(element);
			result.append(name).append(separator);
		}
		if (result.length() >= separator.length()) {
			result.delete(result.length() - separator.length(),result.length());
		}
		return result;
	}
}



