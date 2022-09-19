package org.argouml.model.euml;

import org.argouml.model.MetaTypes;
import org.argouml.model.NotImplementedException;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.Action;
import org.eclipse.uml2.uml.ActivityPartition;
import org.eclipse.uml2.uml.Actor;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Artifact;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.BehavioralFeature;
import org.eclipse.uml2.uml.CallAction;
import org.eclipse.uml2.uml.CallConcurrencyKind;
import org.eclipse.uml2.uml.CallEvent;
import org.eclipse.uml2.uml.ChangeEvent;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Collaboration;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ElementImport;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Expression;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.ExtensionPoint;
import org.eclipse.uml2.uml.Feature;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionConstraint;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.ObjectNode;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Reception;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.TemplateParameterSubstitution;
import org.eclipse.uml2.uml.TimeEvent;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.UseCase;
import org.eclipse.uml2.uml.VisibilityKind;
import org.argouml.model.euml.EUMLModelImplementation;


final class MetaTypesEUMLImpl implements MetaTypes {
	private EUMLModelImplementation modelImpl;
	public MetaTypesEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public Object getAbstraction() {
		return Abstraction.class;
	}
	public Object getAction() {
		return Action.class;
	}
	public Object getActionExpression() {
		throw new NotYetImplementedException();
	}
	public Object getActionState() {
		return State.class;
	}
	public Object getActor() {
		return Actor.class;
	}
	public Object getAggregationKind() {
		return AggregationKind.class;
	}
	public Object getArtifact() {
		return Artifact.class;
	}
	public Object getAssociation() {
		return Association.class;
	}
	public Object getAssociationClass() {
		return AssociationClass.class;
	}
	public Object getAssociationEnd() {
		return Property.class;
	}
	public Object getAssociationEndRole() {
		return Classifier.class;
	}
	public Object getAssociationRole() {
		return Classifier.class;
	}
	public Object getAttribute() {
		return Property.class;
	}
	public Object getBehavioralFeature() {
		return BehavioralFeature.class;
	}
	public Object getBinding() {
		return TemplateBinding.class;
	}
	public Object getBooleanExpression() {
		throw new NotYetImplementedException();
	}
	public Object getCallAction() {
		return CallAction.class;
	}
	public Object getCallConcurrencyKind() {
		return CallConcurrencyKind.class;
	}
	public Object getCallState() {
		return State.class;
	}
	public Object getClassifier() {
		return Classifier.class;
	}
	public Object getClassifierRole() {
		return Classifier.class;
	}
	public Object getCollaboration() {
		return Collaboration.class;
	}
	public Object getComment() {
		return Comment.class;
	}
	public Object getComponent() {
		return Component.class;
	}
	public Object getComponentInstance() {
		return InstanceSpecification.class;
	}
	public Object getCompositeState() {
		return State.class;
	}
	public Object getConstraint() {
		return Constraint.class;
	}
	public Object getCreateAction() {
		throw new NotYetImplementedException();
	}
	public Object getDataType() {
		return DataType.class;
	}
	public Object getDependency() {
		return Dependency.class;
	}
	public Object getDestroyAction() {
		throw new NotYetImplementedException();
	}
	public Object getElementImport() {
		return ElementImport.class;
	}
	public Object getEnumeration() {
		return Enumeration.class;
	}
	public Object getEnumerationLiteral() {
		return EnumerationLiteral.class;
	}
	public Object getEvent() {
		throw new NotYetImplementedException();
	}
	public Object getException() {
		return Signal.class;
	}
	public Object getExtend() {
		return Extend.class;
	}
	public Object getExtensionPoint() {
		return ExtensionPoint.class;
	}
	public Object getFinalState() {
		return FinalState.class;
	}
	public Object getGeneralizableElement() {
		return Classifier.class;
	}
	public Object getGeneralization() {
		return Generalization.class;
	}
	public Object getGuard() {
		return InteractionConstraint.class;
	}
	public Object getInclude() {
		return Include.class;
	}
	public Object getInstance() {
		return InstanceSpecification.class;
	}
	public Object getInteraction() {
		return Interaction.class;
	}
	public Object getInterface() {
		return Interface.class;
	}
	public Object getLink() {
		return InstanceSpecification.class;
	}
	public Object getMessage() {
		return Message.class;
	}
	public Object getModel() {
		return Model.class;
	}
	public Object getModelElement() {
		return Element.class;
	}
	public Object getMultiplicity() {
		return MultiplicityElement.class;
	}
	public String getName(Object element) {
		Class clazz;
		if (element instanceof Class) {
			clazz = (Class) element;
		}else {
			clazz = element.getClass();
		}
		String name = clazz.getName();
		int startName = name.lastIndexOf('.') + 1;
		final String suffix = "Impl";
		int endName = name.length();
		if (name.endsWith(suffix)) {
			endName -= suffix.length();
		}
		return name.substring(startName,endName);
	}
	public Object getNamespace() {
		return Namespace.class;
	}
	public Object getNode() {
		return Node.class;
	}
	public Object getNodeInstance() {
		return InstanceSpecification.class;
	}
	public Object getObject() {
		throw new NotImplementedException();
	}
	public Object getObjectFlowState() {
		return ObjectNode.class;
	}
	public Object getOperation() {
		return Operation.class;
	}
	public Object getPackage() {
		return org.eclipse.uml2.uml.Package.class;
	}
	public Object getParameter() {
		return Parameter.class;
	}
	public Object getParameterDirectionKind() {
		return ParameterDirectionKind.class;
	}
	public Object getPartition() {
		return ActivityPartition.class;
	}
	public Object getPackageImport() {
		return PackageImport.class;
	}
	public Object getPseudostate() {
		return Pseudostate.class;
	}
	public Object getPseudostateKind() {
		return PseudostateKind.class;
	}
	public Object getReception() {
		return Reception.class;
	}
	public Object getReturnAction() {
		throw new NotYetImplementedException();
	}
	public Object getScopeKind() {
		throw new NotImplementedException();
	}
	public Object getSendAction() {
		throw new NotYetImplementedException();
	}
	public Object getSignal() {
		return Signal.class;
	}
	public Object getSimpleState() {
		return State.class;
	}
	public Object getState() {
		return State.class;
	}
	public Object getStateMachine() {
		return StateMachine.class;
	}
	public Object getStateVertex() {
		return State.class;
	}
	public Object getStereotype() {
		return Stereotype.class;
	}
	public Object getStimulus() {
		throw new NotYetImplementedException();
	}
	public Object getStubState() {
		return State.class;
	}
	public Object getSubactivityState() {
		return State.class;
	}
	public Object getSubmachineState() {
		return State.class;
	}
	public Object getSubsystem() {
		return Component.class;
	}
	public Object getSynchState() {
		return State.class;
	}
	public Object getTagDefinition() {
		return Property.class;
	}
	public Object getTaggedValue() {
		throw new NotYetImplementedException();
	}
	public Object getTemplateArgument() {
		return TemplateParameterSubstitution.class;
	}
	public Object getTemplateParameter() {
		return TemplateParameter.class;
	}
	public Object getTerminateAction() {
		throw new NotYetImplementedException();
	}
	public Object getTransition() {
		return Transition.class;
	}
	public Object getUMLClass() {
		return org.eclipse.uml2.uml.Class.class;
	}
	public Object getUsage() {
		return Usage.class;
	}
	public Object getUseCase() {
		return UseCase.class;
	}
	public Object getVisibilityKind() {
		return VisibilityKind.class;
	}
	public Object getActionSequence() {
		return null;
	}
	public Object getArgument() {
		return null;
	}
	public Object getAttributeLink() {
		return null;
	}
	public Object getCallEvent() {
		return CallEvent.class;
	}
	public Object getChangeEvent() {
		return ChangeEvent.class;
	}
	public Object getClassifierInState() {
		return null;
	}
	public Object getCollaborationInstanceSet() {
		return null;
	}
	public Object getDataValue() {
		return null;
	}
	public Object getElement() {
		return Element.class;
	}
	public Object getElementResidence() {
		return null;
	}
	public Object getExpression() {
		return Expression.class;
	}
	public Object getFeature() {
		return Feature.class;
	}
	public Object getFlow() {
		return null;
	}
	public Object getInteractionInstanceSet() {
		return null;
	}
	public Object getLinkEnd() {
		return null;
	}
	public Object getLinkObject() {
		return null;
	}
	public Object getMethod() {
		return null;
	}
	public Object getMultiplicityRange() {
		return MultiplicityElement.class;
	}
	public Object getPrimitiveType() {
		return PrimitiveType.class;
	}
	public Object getRelationship() {
		return Relationship.class;
	}
	public Object getSignalEvent() {
		return SignalEvent.class;
	}
	public Object getStructuralFeature() {
		return StructuralFeature.class;
	}
	public Object getSubsystemInstance() {
		return null;
	}
	public Object getTimeEvent() {
		return TimeEvent.class;
	}
	public Object getUninterpretedAction() {
		return null;
	}
}



