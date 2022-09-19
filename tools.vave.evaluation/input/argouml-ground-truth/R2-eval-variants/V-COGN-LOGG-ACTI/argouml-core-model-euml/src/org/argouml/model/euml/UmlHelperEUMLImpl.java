package org.argouml.model.euml;

import java.util.Collection;
import java.util.Iterator;
import org.argouml.model.UmlHelper;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Transition;
import org.argouml.model.euml.EUMLModelImplementation;


class UmlHelperEUMLImpl implements UmlHelper {
	private EUMLModelImplementation modelImpl;
	public UmlHelperEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public void addListenersToModel(Object model) {
	}
	public void deleteCollection(Collection col) {
		Iterator it = col.iterator();
		while (it.hasNext()) {
			modelImpl.getUmlFactory().delete(it.next());
		}
	}
	public Object getSource(Object relationship) {
		if (relationship instanceof Relationship) {
			return modelImpl.getCoreHelper().getSource(relationship);
		}else if (relationship instanceof Transition) {
			return modelImpl.getStateMachinesHelper().getSource(relationship);
		}else if (relationship instanceof Property) {
			return modelImpl.getCoreHelper().getSource(relationship);
		}
		throw new IllegalArgumentException();
	}
	public Object getDestination(Object relationShip) {
		if (relationShip instanceof Relationship) {
			return modelImpl.getCoreHelper().getDestination(relationShip);
		}else if (relationShip instanceof Transition) {
			return modelImpl.getStateMachinesHelper().getDestination(relationShip);
		}else if (relationShip instanceof Property) {
			return modelImpl.getCoreHelper().getDestination(relationShip);
		}
		throw new IllegalArgumentException();
	}
}



