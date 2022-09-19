package org.argouml.model.euml;

import java.util.Collection;
import org.argouml.model.CollaborationsHelper;
import org.argouml.model.euml.EUMLModelImplementation;


class CollaborationsHelperEUMLImpl implements CollaborationsHelper {
	private EUMLModelImplementation modelImpl;
	public CollaborationsHelperEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public void addBase(Object arole,Object abase) {
	}
	public void addConstrainingElement(Object handle,Object constraint) {
	}
	public void addInstance(Object classifierRole,Object instance) {
	}
	public void addMessage(Object handle,Object elem) {
	}
	public void addPredecessor(Object handle,Object predecessor) {
	}
	public void addSuccessor(Object handle,Object mess) {
	}
	public Collection allAvailableContents(Object arole) {
		return null;
	}
	public Collection allAvailableFeatures(Object arole) {
		return null;
	}
	public Collection getAllClassifierRoles(Object ns) {
		return null;
	}
	public Collection getAllPossibleActivators(Object ames) {
		return null;
	}
	public Collection getAllPossibleAssociationRoles(Object role) {
		return null;
	}
	public Collection getAllPossibleBases(Object role) {
		return null;
	}
	public Collection getAllPossiblePredecessors(Object amessage) {
		return null;
	}
	public Object getAssociationRole(Object afrom,Object ato) {
		return null;
	}
	public Collection getClassifierRoles(Object role) {
		return null;
	}
	public boolean hasAsActivator(Object message,Object activator) {
		return false;
	}
	public boolean isAddingCollaborationAllowed(Object context) {
		return false;
	}
	public void removeBase(Object handle,Object c) {
	}
	public void removeConstrainingElement(Object handle,Object constraint) {
	}
	public void removeInteraction(Object collab,Object interaction) {
	}
	public void removeMessage(Object handle,Object message) {
	}
	public void removePredecessor(Object handle,Object message) {
	}
	public void removeSuccessor(Object handle,Object mess) {
	}
	public void setAction(Object handle,Object action) {
	}
	public void setActivator(Object ames,Object anactivator) {
	}
	public void setBase(Object arole,Object abase) {
	}
	public void setBases(Object role,Collection bases) {
	}
	public void setContext(Object handle,Object col) {
	}
	public void setPredecessors(Object handle,Collection predecessors) {
	}
	public void setRepresentedClassifier(Object handle,Object classifier) {
	}
	public void setRepresentedOperation(Object handle,Object operation) {
	}
	public void setSender(Object handle,Object sender) {
	}
	public void setSuccessors(Object handle,Collection messages) {
	}
}



