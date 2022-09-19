package org.argouml.model;


public interface CollaborationsFactory extends Factory {
	Object createAssociationEndRole();
	Object createAssociationRole();
	Object createClassifierRole();
	Object createCollaboration();
	Object createCollaborationInstanceSet();
	Object createInteraction();
	Object createInteractionInstanceSet();
	Object createMessage();
	Object buildClassifierRole(Object collaboration);
	Object buildCollaboration(Object handle);
	Object buildCollaboration(Object namespace,Object representedElement);
	Object buildInteraction(Object handle);
	Object buildAssociationEndRole(Object atype);
	Object buildAssociationRole(Object from,Object to);
	@Deprecated Object buildAssociationRole(Object from,Object agg1,Object to,Object agg2,Boolean unidirectional);
	Object buildAssociationRole(Object from,Object agg1,Object to,Object agg2,boolean unidirectional);
	Object buildAssociationRole(Object link);
	Object buildMessage(Object acollab,Object arole);
	Object buildActivator(Object owner,Object interaction);
}



