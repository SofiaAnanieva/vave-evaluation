package org.argouml.model;

import java.util.Collection;


public interface CollaborationsHelper {
	Collection getAllClassifierRoles(Object namespace);
	Collection getAllPossibleAssociationRoles(Object classifierRole);
	Collection getClassifierRoles(Object classifierRole);
	Object getAssociationRole(Object fromRole,Object toRole);
	Collection getAllPossibleActivators(Object message);
	boolean hasAsActivator(Object message,Object activator);
	void setActivator(Object message,Object activator);
	Collection getAllPossiblePredecessors(Object amessage);
	void addBase(Object classifierRole,Object baseClassifier);
	void setBases(Object classifierRole,Collection bases);
	Collection allAvailableFeatures(Object classifierRole);
	Collection allAvailableContents(Object classifierRole);
	Collection getAllPossibleBases(Object role);
	void setBase(Object classifierRole,Object baseClassifier);
	boolean isAddingCollaborationAllowed(Object context);
	void removeBase(Object classifierRole,Object baseClassifier);
	void removeConstrainingElement(Object handle,Object constraint);
	void removeMessage(Object handle,Object message);
	void removeSuccessor(Object handle,Object message);
	void removePredecessor(Object handle,Object message);
	void addConstrainingElement(Object handle,Object constraint);
	void addInstance(Object classifierRole,Object instance);
	void addMessage(Object handle,Object message);
	void addSuccessor(Object predecessor,Object message);
	void addPredecessor(Object handle,Object predecessor);
	void setAction(Object handle,Object action);
	void setContext(Object interaction,Object context);
	void setSuccessors(Object handle,Collection messages);
	void setPredecessors(Object handle,Collection predecessors);
	void setRepresentedClassifier(Object handle,Object classifier);
	void setRepresentedOperation(Object handle,Object operation);
	void setSender(Object handle,Object sender);
	void removeInteraction(Object collab,Object interaction);
}



