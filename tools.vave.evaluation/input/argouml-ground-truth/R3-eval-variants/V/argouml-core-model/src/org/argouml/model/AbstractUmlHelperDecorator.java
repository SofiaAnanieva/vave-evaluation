package org.argouml.model;

import java.util.Collection;


public abstract class AbstractUmlHelperDecorator implements UmlHelper {
	private UmlHelper impl;
	public AbstractUmlHelperDecorator(UmlHelper component) {
		impl = component;
	}
	public void addListenersToModel(Object model) {
		impl.addListenersToModel(model);
	}
	public void deleteCollection(Collection col) {
		impl.deleteCollection(col);
	}
	public Object getSource(Object relationship) {
		return impl.getSource(relationship);
	}
	public Object getDestination(Object relationship) {
		return impl.getDestination(relationship);
	}
}



