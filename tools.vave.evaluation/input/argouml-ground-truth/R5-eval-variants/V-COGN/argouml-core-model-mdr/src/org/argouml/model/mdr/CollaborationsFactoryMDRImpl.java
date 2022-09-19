package org.argouml.model.mdr;

import java.util.Collection;
import java.util.Iterator;
import javax.jmi.reflect.InvalidObjectException;
import org.argouml.model.CollaborationsFactory;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.omg.uml.behavioralelements.collaborations.AssociationEndRole;
import org.omg.uml.behavioralelements.collaborations.AssociationRole;
import org.omg.uml.behavioralelements.collaborations.ClassifierRole;
import org.omg.uml.behavioralelements.collaborations.Collaboration;
import org.omg.uml.behavioralelements.collaborations.CollaborationInstanceSet;
import org.omg.uml.behavioralelements.collaborations.CollaborationsPackage;
import org.omg.uml.behavioralelements.collaborations.Interaction;
import org.omg.uml.behavioralelements.collaborations.InteractionInstanceSet;
import org.omg.uml.behavioralelements.collaborations.Message;
import org.omg.uml.behavioralelements.commonbehavior.Link;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.datatypes.AggregationKind;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;
import org.omg.uml.foundation.datatypes.Multiplicity;


class CollaborationsFactoryMDRImpl extends AbstractUmlModelFactoryMDR implements CollaborationsFactory {
	private MDRModelImplementation modelImpl;
	CollaborationsFactoryMDRImpl(MDRModelImplementation implementation) {
			modelImpl = implementation;
		}
	public AssociationEndRole createAssociationEndRole() {
		AssociationEndRole myAssociationEndRole = getCollabPkg().getAssociationEndRole().createAssociationEndRole();
		super.initialize(myAssociationEndRole);
		return myAssociationEndRole;
	}
	private CollaborationsPackage getCollabPkg() {
		return modelImpl.getUmlPackage().getCollaborations();
	}
	public AssociationRole createAssociationRole() {
		AssociationRole myAssociationRole = getCollabPkg().getAssociationRole().createAssociationRole();
		super.initialize(myAssociationRole);
		return myAssociationRole;
	}
	public ClassifierRole createClassifierRole() {
		ClassifierRole myClassifierRole = getCollabPkg().getClassifierRole().createClassifierRole();
		super.initialize(myClassifierRole);
		return myClassifierRole;
	}
	public Collaboration createCollaboration() {
		Collaboration myCollaboration = getCollabPkg().getCollaboration().createCollaboration();
		super.initialize(myCollaboration);
		return myCollaboration;
	}
	public CollaborationInstanceSet createCollaborationInstanceSet() {
		CollaborationInstanceSet obj = getCollabPkg().getCollaborationInstanceSet().createCollaborationInstanceSet();
		super.initialize(obj);
		return obj;
	}
	public Interaction createInteraction() {
		Interaction myInteraction = getCollabPkg().getInteraction().createInteraction();
		super.initialize(myInteraction);
		return myInteraction;
	}
	public InteractionInstanceSet createInteractionInstanceSet() {
		InteractionInstanceSet obj = getCollabPkg().getInteractionInstanceSet().createInteractionInstanceSet();
		super.initialize(obj);
		return obj;
	}
	public Message createMessage() {
		Message myMessage = getCollabPkg().getMessage().createMessage();
		super.initialize(myMessage);
		return myMessage;
	}
	public ClassifierRole buildClassifierRole(Object collaboration) {
		Collaboration myCollaboration = (Collaboration) collaboration;
		ClassifierRole classifierRole = createClassifierRole();
		classifierRole.setNamespace(myCollaboration);
		classifierRole.setMultiplicity((Multiplicity) Model.getDataTypesFactory().createMultiplicity("1..1"));
		return classifierRole;
	}
	public Object buildCollaboration(Object handle) {
		Namespace namespace = (Namespace) handle;
		Collaboration modelelement = createCollaboration();
		modelelement.setNamespace(namespace);
		modelelement.setName("newCollaboration");
		modelelement.setAbstract(false);
		return modelelement;
	}
	public Object buildCollaboration(Object namespace,Object representedElement) {
		if (!(namespace instanceof Namespace)) {
			throw new IllegalArgumentException("Argument is not " + "a namespace");
		}
		if (representedElement instanceof Classifier||representedElement instanceof Operation) {
			Collaboration collaboration = (Collaboration) buildCollaboration(namespace);
			if (representedElement instanceof Classifier) {
				collaboration.setRepresentedClassifier((Classifier) representedElement);
				return collaboration;
			}
			if (representedElement instanceof Operation) {
				collaboration.setRepresentedOperation((Operation) representedElement);
				return collaboration;
			}
		}
		throw new IllegalArgumentException("Represented element must be" + " Collaboration or Operation");
	}
	public Interaction buildInteraction(Object handle) {
		Collaboration collab = (Collaboration) handle;
		Interaction inter = createInteraction();
		inter.setContext(collab);
		inter.setName("newInteraction");
		return inter;
	}
	public AssociationEndRole buildAssociationEndRole(Object atype) {
		ClassifierRole type = (ClassifierRole) atype;
		AssociationEndRole end = createAssociationEndRole();
		end.setParticipant(type);
		return end;
	}
	public AssociationRole buildAssociationRole(Object from,Object to) {
		return buildAssociationRole((ClassifierRole) from,(ClassifierRole) to);
	}
	private AssociationRole buildAssociationRole(ClassifierRole from,ClassifierRole to) {
		Collaboration collaboration = (Collaboration) from.getNamespace();
		if (collaboration == null||!collaboration.equals(to.getNamespace())) {
			throw new IllegalArgumentException("ClassifierRoles must be in" + " same non-null namespace");
		}
		AssociationRole role = createAssociationRole();
		role.setNamespace(collaboration);
		role.getConnection().add(buildAssociationEndRole(from));
		role.getConnection().add(buildAssociationEndRole(to));
		return role;
	}
	@Deprecated public AssociationRole buildAssociationRole(Object from,Object agg1,Object to,Object agg2,Boolean unidirectional) {
		if (unidirectional == null) {
			return buildAssociationRole(from,agg1,to,agg2,false);
		}else {
			return buildAssociationRole(from,agg1,to,agg2,unidirectional.booleanValue());
		}
	}
	public AssociationRole buildAssociationRole(Object from,Object agg1,Object to,Object agg2,boolean unidirectional) {
		AggregationKind ak1 = checkAggregationKind(agg1);
		AggregationKind ak2 = checkAggregationKind(agg2);
		AssociationRole role = buildAssociationRole((ClassifierRole) from,(ClassifierRole) to);
		AssociationEndRole end = (AssociationEndRole) role.getConnection().get(0);
		end.setAggregation(ak1);
		end.setNavigable(!unidirectional);
		end = (AssociationEndRole) role.getConnection().get(1);
		end.setAggregation(ak2);
		end.setNavigable(true);
		return role;
	}
	private AggregationKind checkAggregationKind(Object aggregationKind) {
		if (aggregationKind == null) {
			aggregationKind = AggregationKindEnum.AK_NONE;
		}
		return(AggregationKind) aggregationKind;
	}
	public AssociationRole buildAssociationRole(Object link) {
		if (!(link instanceof Link)) {
			throw new IllegalArgumentException("Argument is not a link");
		}
		Object from = modelImpl.getCoreHelper().getSource(link);
		Object to = modelImpl.getCoreHelper().getDestination(link);
		Object classifierRoleFrom = modelImpl.getFacade().getClassifiers(from).iterator().next();
		Object classifierRoleTo = modelImpl.getFacade().getClassifiers(to).iterator().next();
		Object collaboration = modelImpl.getFacade().getNamespace(classifierRoleFrom);
		if (collaboration != modelImpl.getFacade().getNamespace(classifierRoleTo)) {
			throw new IllegalStateException("ClassifierRoles do not belong " + "to the same collaboration");
		}
		if (collaboration == null) {
			throw new IllegalStateException("Collaboration may not be " + "null");
		}
		AssociationRole associationRole = createAssociationRole();
		modelImpl.getCoreHelper().setNamespace(associationRole,collaboration);
		modelImpl.getCoreHelper().addLink(associationRole,link);
		return associationRole;
	}
	private Message buildMessageInteraction(Interaction inter,AssociationRole role) {
		assert inter != null:"An interaction must be provided";
		assert role != null:"An association role must be provided";
		Message message = createMessage();
		inter.getMessage().add(message);
		message.setCommunicationConnection(role);
		if (role.getConnection().size() == 2) {
			AssociationEnd ae = role.getConnection().get(0);
			message.setSender((ClassifierRole) ae.getParticipant());
			AssociationEnd ae2 = role.getConnection().get(1);
			message.setReceiver((ClassifierRole) ae2.getParticipant());
			Collection<Message>messages = Model.getFacade().getReceivedMessages(message.getSender());
			Message lastMsg = lastMessage(messages,message);
			if (lastMsg != null) {
				message.setActivator(lastMsg);
				messages = Model.getFacade().getActivatedMessages(lastMsg);
			}else {
				messages = Model.getFacade().getSentMessages(message.getSender());
			}
			lastMsg = lastMessage(messages,message);
			if (lastMsg != null) {
				message.getPredecessor().add(findEnd(lastMsg));
			}
		}
		return message;
	}
	private Message lastMessage(Collection<Message>c,Message m) {
		Message last = null;
		for (Message msg:c) {
			if (msg != null&&msg != m) {
				last = msg;
			}
		}
		return last;
	}
	private Message findEnd(Message m) {
		while (true) {
			Collection<Message>c = Model.getFacade().getSuccessors(m);
			Iterator<Message>it = c.iterator();
			if (!it.hasNext()) {
				return m;
			}
			m = it.next();
		}
	}
	public Message buildMessage(Object acollab,Object arole) {
		if (!(arole instanceof AssociationRole)) {
			throw new IllegalArgumentException("An association role must be supplied - got " + arole);
		}
		try {
			if (acollab instanceof Collaboration) {
				return buildMessageCollab((Collaboration) acollab,(AssociationRole) arole);
			}
			if (acollab instanceof Interaction) {
				return buildMessageInteraction((Interaction) acollab,(AssociationRole) arole);
			}
			throw new IllegalArgumentException("No valid object " + acollab);
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
	}
	private Message buildMessageCollab(Collaboration collab,AssociationRole role) {
		Interaction inter = null;
		if (collab.getInteraction().size() == 0) {
			inter = buildInteraction(collab);
		}else {
			inter = (Interaction) (collab.getInteraction().toArray())[0];
		}
		return buildMessageInteraction(inter,role);
	}
	public Message buildActivator(Object owner,Object interaction) {
		Message theOwner = (Message) owner;
		Interaction theInteraction;
		if (interaction == null) {
			theInteraction = theOwner.getInteraction();
		}else {
			theInteraction = (Interaction) interaction;
		}
		if (interaction == null) {
			throw new IllegalArgumentException();
		}
		Message activator = createMessage();
		activator.setInteraction(theInteraction);
		theOwner.setActivator(activator);
		return activator;
	}
	void deleteAssociationEndRole(Object elem) {
		if (!(elem instanceof AssociationEndRole)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteAssociationRole(Object elem) {
		AssociationRole role = (AssociationRole) elem;
		for (Message message:role.getMessage()) {
			modelImpl.getUmlFactory().delete(message);
		}
	}
	void deleteClassifierRole(Object elem) {
		ClassifierRole cr = (ClassifierRole) elem;
		CollaborationsPackage cPkg = ((org.omg.uml.UmlPackage) cr.refOutermostPackage()).getCollaborations();
		modelImpl.getUmlHelper().deleteCollection(cPkg.getAMessageSender().getMessage(cr));
		modelImpl.getUmlHelper().deleteCollection(cPkg.getAReceiverMessage().getMessage(cr));
	}
	void deleteCollaboration(Object elem) {
		if (!(elem instanceof Collaboration)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteCollaborationInstanceSet(Object elem) {
		if (!(elem instanceof CollaborationInstanceSet)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteInteraction(Object elem) {
		if (!(elem instanceof Interaction)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteInteractionInstanceSet(Object elem) {
		if (!(elem instanceof InteractionInstanceSet)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteMessage(Object elem) {
		Message message = (Message) elem;
		Interaction i = message.getInteraction();
		if (i != null&&i.getMessage().size() == 1) {
			modelImpl.getUmlFactory().delete(i);
		}
	}
}



