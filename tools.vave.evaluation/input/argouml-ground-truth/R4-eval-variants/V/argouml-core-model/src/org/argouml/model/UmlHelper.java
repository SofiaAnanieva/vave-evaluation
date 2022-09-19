package org.argouml.model;

import java.util.Collection;


public interface UmlHelper {
	void addListenersToModel(Object model);
	void deleteCollection(Collection col);
	Object getSource(Object relationShip);
	Object getDestination(Object relationShip);
}



