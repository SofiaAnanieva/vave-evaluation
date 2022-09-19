package org.argouml.model;

import java.util.Collection;


public abstract class AbstractCollaborationsHelperDecorator implements CollaborationsHelper {
	private CollaborationsHelper impl;
	protected AbstractCollaborationsHelperDecorator(CollaborationsHelper component) {
		impl = component;
	}
	protected CollaborationsHelper getComponent() {
		return impl;
	}
	public Collection getAllClassifierRoles(Object ns) {
		return impl.getAllClassifierRoles(ns);
	}
	public Collection getAllPossibleAssociationRoles(Object role) {
		return impl.getAllPossibleAssociationRoles(role);
	}
	public Collection getClassifierRoles(Object role) {
		return impl.getClassifierRoles(role);
	}
	public Object getAssociationRole(Object afrom,Object ato) {
		return impl.getAssociationRole(afrom,ato);
	}
	public Collection getAllPossibleActivators(Object ames) {
		return impl.getAllPossibleActivators(ames);
	}
	public boolean hasAsActivator(Object message,Object activator) {
		return impl.hasAsActivator(message,activator);
	}
	public void setActivator(Object ames,Object anactivator) {
		impl.setActivator(ames,anactivator);
	}
	public Collection getAllPossiblePredecessors(Object amessage) {
		return impl.getAllPossiblePredecessors(amessage);
	}
	public void addBase(Object arole,Object abase) {
		impl.addBase(arole,abase);
	}
	public void setBases(Object role,Collection bases) {
		impl.setBases(role,bases);
	}
	public Collection allAvailableFeatures(Object arole) {
		return impl.allAvailableFeatures(arole);
	}
	public Collection allAvailableContents(Object arole) {
		return impl.allAvailableContents(arole);
	}
	public Collection getAllPossibleBases(Object role) {
		return impl.getAllPossibleBases(role);
	}
	public void setBase(Object arole,Object abase) {
		impl.setBase(arole,abase);
	}
	public boolean isAddingCollaborationAllowed(Object context) {
		return impl.isAddingCollaborationAllowed(context);
	}
	public void removeBase(Object handle,Object c) {
		impl.removeBase(handle,c);
	}
	public void removeConstrainingElement(Object handle,Object constraint) {
		impl.removeConstrainingElement(handle,constraint);
	}
	public void removeMessage(Object handle,Object message) {
		impl.removeMessage(handle,message);
	}
	public void removeMessage3(Object handle,Object mess) {
		impl.removeSuccessor(handle,mess);
	}
	public void removeSuccessor(Object handle,Object mess) {
		impl.removeSuccessor(handle,mess);
	}
	public void removePredecessor(Object handle,Object message) {
		impl.removePredecessor(handle,message);
	}
	public void addConstrainingElement(Object handle,Object constraint) {
		impl.addConstrainingElement(handle,constraint);
	}
	public void addInstance(Object classifierRole,Object instance) {
		impl.addInstance(classifierRole,instance);
	}
	public void addMessage(Object handle,Object elem) {
		impl.addMessage(handle,elem);
	}
	public void addSuccessor(Object handle,Object mess) {
		impl.addSuccessor(handle,mess);
	}
	public void addPredecessor(Object handle,Object predecessor) {
		impl.addPredecessor(handle,predecessor);
	}
	public void setAction(Object handle,Object action) {
		impl.setAction(handle,action);
	}
	public void setContext(Object handle,Object col) {
		impl.setContext(handle,col);
	}
	public void setSuccessors(Object handle,Collection messages) {
		impl.setSuccessors(handle,messages);
	}
	public void setPredecessors(Object handle,Collection predecessors) {
		impl.setPredecessors(handle,predecessors);
	}
	public void setRepresentedClassifier(Object handle,Object classifier) {
		impl.setRepresentedClassifier(handle,classifier);
	}
	public void setRepresentedOperation(Object handle,Object operation) {
		impl.setRepresentedOperation(handle,operation);
	}
	public void setSender(Object handle,Object sender) {
		impl.setSender(handle,sender);
	}
	public void removeInteraction(Object collab,Object interaction) {
		impl.removeInteraction(collab,interaction);
	}
}



