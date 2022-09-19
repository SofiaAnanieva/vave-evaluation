package org.argouml.model.mdr;

import java.util.Collection;
import java.util.Iterator;
import org.argouml.model.UmlHelper;
import org.omg.uml.behavioralelements.collaborations.Message;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Relationship;


class UmlHelperMDRImpl implements UmlHelper {
	private MDRModelImplementation modelImpl;
	UmlHelperMDRImpl(MDRModelImplementation implementation) {
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
		if (relationship instanceof Message) {
			Message message = (Message) relationship;
			return message.getSender();
		}
		if (relationship instanceof Relationship) {
			return modelImpl.getCoreHelper().getSource(relationship);
		}
		if (relationship instanceof Transition) {
			return modelImpl.getStateMachinesHelper().getSource(relationship);
		}
		if (relationship instanceof AssociationEnd) {
			return modelImpl.getCoreHelper().getSource(relationship);
		}
		throw new IllegalArgumentException();
	}
	public Object getDestination(Object relationship) {
		if (relationship instanceof Message) {
			Message message = (Message) relationship;
			return message.getSender();
		}
		if (relationship instanceof Relationship) {
			return modelImpl.getCoreHelper().getDestination(relationship);
		}
		if (relationship instanceof Transition) {
			return modelImpl.getStateMachinesHelper().getDestination(relationship);
		}
		if (relationship instanceof AssociationEnd) {
			return modelImpl.getCoreHelper().getDestination(relationship);
		}
		throw new IllegalArgumentException();
	}
}



