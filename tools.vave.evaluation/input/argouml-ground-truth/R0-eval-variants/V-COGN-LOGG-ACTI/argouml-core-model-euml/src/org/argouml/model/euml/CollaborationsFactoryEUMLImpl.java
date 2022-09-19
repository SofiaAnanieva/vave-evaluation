package org.argouml.model.euml;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.CollaborationsFactory;
import org.eclipse.uml2.uml.UMLFactory;
import org.argouml.model.euml.EUMLModelImplementation;


class CollaborationsFactoryEUMLImpl implements CollaborationsFactory,AbstractModelFactory {
	private EUMLModelImplementation modelImpl;
	public CollaborationsFactoryEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public Object buildActivator(Object owner,Object interaction) {
		return null;
	}
	public Object buildAssociationEndRole(Object atype) {
		return null;
	}
	public Object buildAssociationRole(Object from,Object to) {
		return null;
	}
	@Deprecated public Object buildAssociationRole(Object from,Object agg1,Object to,Object agg2,Boolean unidirectional) {
		if (unidirectional == null) {
			return buildAssociationRole(from,agg1,to,agg2,false);
		}else {
			return buildAssociationRole(from,agg1,to,agg2,unidirectional.booleanValue());
		}
	}
	public Object buildAssociationRole(Object from,Object agg1,Object to,Object agg2,boolean unidirectional) {
		return null;
	}
	public Object buildAssociationRole(Object link) {
		return null;
	}
	public Object buildClassifierRole(Object collaboration) {
		return null;
	}
	public Object buildCollaboration(Object handle) {
		return null;
	}
	public Object buildCollaboration(Object namespace,Object representedElement) {
		return null;
	}
	public Object buildInteraction(Object handle) {
		return null;
	}
	public Object buildMessage(Object acollab,Object arole) {
		return null;
	}
	public Object createAssociationEndRole() {
		return null;
	}
	public Object createAssociationRole() {
		return null;
	}
	public Object createClassifierRole() {
		return null;
	}
	public Object createCollaboration() {
		return UMLFactory.eINSTANCE.createCollaboration();
	}
	public Object createCollaborationInstanceSet() {
		return null;
	}
	public Object createInteraction() {
		return UMLFactory.eINSTANCE.createInteraction();
	}
	public Object createInteractionInstanceSet() {
		return null;
	}
	public Object createMessage() {
		return UMLFactory.eINSTANCE.createMessage();
	}
}



