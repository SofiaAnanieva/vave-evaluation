package org.argouml.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public interface Facade {
	String GENERATED_TAG = "GeneratedFromImport";
	String DERIVED_TAG = "derived";
	String getUmlVersion();
	boolean isAAbstraction(Object handle);
	boolean isAAction(Object handle);
	boolean isAActionSequence(Object handle);
	boolean isAActionState(Object handle);
	boolean isACallState(Object handle);
	boolean isAObjectFlowState(Object handle);
	boolean isASubactivityState(Object handle);
	boolean isAActor(Object handle);
	boolean isAAggregationKind(Object handle);
	boolean isAArtifact(Object handle);
	boolean isAAssociation(Object handle);
	boolean isAAssociationEnd(Object handle);
	boolean isAAssociationRole(Object handle);
	boolean isAAssociationEndRole(Object handle);
	boolean isAAttribute(Object handle);
	boolean isAAttributeLink(Object handle);
	boolean isAsynchronous(Object handle);
	boolean isAbstract(Object handle);
	boolean isAActivityGraph(Object handle);
	boolean isABehavioralFeature(Object handle);
	boolean isABinding(Object handle);
	boolean isACallAction(Object handle);
	boolean isACallEvent(Object handle);
	boolean isAChangeEvent(Object handle);
	boolean isAClass(Object handle);
	boolean isAAssociationClass(Object handle);
	boolean isAClassifier(Object handle);
	boolean isAClassifierInState(Object handle);
	boolean isAClassifierRole(Object handle);
	boolean isAComment(Object handle);
	boolean isACollaboration(Object handle);
	boolean isACollaborationInstanceSet(Object handle);
	boolean isAComponent(Object handle);
	boolean isAComponentInstance(Object handle);
	boolean isAConstraint(Object handle);
	boolean isACreateAction(Object handle);
	boolean isADataType(Object handle);
	boolean isADataValue(Object handle);
	boolean isADependency(Object handle);
	boolean isADestroyAction(Object handle);
	boolean isACompositeState(Object handle);
	boolean isAElement(Object handle);
	boolean isAElementImport(Object handle);
	boolean isAElementResidence(Object handle);
	boolean isAEnumeration(Object handle);
	boolean isAEnumerationLiteral(Object handle);
	boolean isAEvent(Object handle);
	boolean isAException(Object handle);
	boolean isAExpression(Object handle);
	boolean isAExtend(Object handle);
	boolean isAExtensionPoint(Object handle);
	boolean isAFeature(Object handle);
	boolean isAFinalState(Object handle);
	boolean isAFlow(Object handle);
	boolean isAGuard(Object handle);
	boolean isAGeneralizableElement(Object handle);
	boolean isAGeneralization(Object handle);
	boolean isAInclude(Object handle);
	boolean isAInstance(Object handle);
	boolean isAInteraction(Object handle);
	boolean isAInteractionInstanceSet(Object handle);
	boolean isAInterface(Object handle);
	boolean isALink(Object handle);
	boolean isALinkEnd(Object handle);
	boolean isALinkObject(Object handle);
	boolean isAMessage(Object handle);
	boolean isAMethod(Object handle);
	boolean isAModel(Object handle);
	boolean isAModelElement(Object handle);
	boolean isANamedElement(Object handle);
	boolean isAMultiplicity(Object handle);
	boolean isAMultiplicityRange(Object handle);
	boolean isANamespace(Object handle);
	boolean isANaryAssociation(Object handle);
	boolean isANode(Object handle);
	boolean isANodeInstance(Object handle);
	boolean isAOperation(Object handle);
	boolean isAObject(Object handle);
	boolean isAParameter(Object handle);
	boolean isAPartition(Object handle);
	boolean isAPackageImport(Object handle);
	boolean isAPackage(Object handle);
	boolean isAPrimitiveType(Object handle);
	boolean isAPseudostate(Object handle);
	boolean isAPseudostateKind(Object handle);
	Object getKind(Object handle);
	Object getReceiver(Object handle);
	Object getLink(Object handle);
	boolean equalsPseudostateKind(Object ps1,Object ps2);
	boolean isAReception(Object handle);
	boolean isAReturnAction(Object handle);
	boolean isARelationship(Object handle);
	boolean isASendAction(Object handle);
	boolean isASignal(Object handle);
	boolean isASignalEvent(Object handle);
	boolean isASimpleState(Object handle);
	boolean isAStateMachine(Object handle);
	boolean isAStimulus(Object handle);
	boolean isAStateVertex(Object handle);
	boolean isAStereotype(Object handle);
	boolean isAStructuralFeature(Object handle);
	boolean isAState(Object handle);
	boolean isAStubState(Object handle);
	boolean isASubmachineState(Object handle);
	boolean isASubsystem(Object handle);
	boolean isASubsystemInstance(Object handle);
	boolean isASynchState(Object handle);
	boolean isATaggedValue(Object handle);
	boolean isATemplateArgument(Object handle);
	boolean isATemplateParameter(Object handle);
	boolean isATerminateAction(Object handle);
	boolean isATransition(Object handle);
	boolean isATimeEvent(Object handle);
	public boolean isAUMLElement(Object handle);
	boolean isAUninterpretedAction(Object handle);
	boolean isAUsage(Object handle);
	boolean isAUseCase(Object handle);
	boolean isAVisibilityKind(Object handle);
	boolean isActive(Object handle);
	boolean isConcurrent(Object handle);
	boolean isAConcurrentRegion(Object handle);
	boolean isConstructor(Object handle);
	boolean isFrozen(Object handle);
	boolean isComposite(Object handle);
	boolean isAggregate(Object handle);
	boolean isInitialized(Object handle);
	boolean isInternal(Object handle);
	boolean isLeaf(Object handle);
	boolean isRoot(Object handle);
	boolean isSpecification(Object handle);
	boolean isNavigable(Object handle);
	boolean isPrimaryObject(Object handle);
	boolean isPackage(Object handle);
	boolean isPrivate(Object handle);
	boolean isPublic(Object handle);
	boolean isQuery(Object handle);
	boolean isProtected(Object handle);
	boolean isRealize(Object handle);
	boolean isReturn(Object handle);
	boolean isSingleton(Object handle);
	boolean isStereotype(Object handle,String stereotypename);
	boolean isTop(Object handle);
	boolean isType(Object handle);
	boolean isUtility(Object handle);
	Object getAssociation(Object handle);
	Object getAssociationEnd(Object classifier,Object association);
	Collection getAssociationEnds(Object handle);
	Collection getAssociationRoles(Object handle);
	List getAttributes(Object handle);
	Collection<String>getBaseClasses(Object handle);
	Object getBase(Object handle);
	Collection getBases(Object handle);
	Collection getBehaviors(Object handle);
	Object getBehavioralFeature(Object handle);
	Object getBody(Object handle);
	int getBound(Object handle);
	@Deprecated Object getChangeability(Object handle);
	Object getSpecific(Object handle);
	Collection getChildren(Object handle);
	Collection getClassifierRoles(Object handle);
	Object getClassifier(Object handle);
	Collection getClassifiers(Object handle);
	Collection getClassifiersInState(Object handle);
	Collection getClients(Object handle);
	Collection getClientDependencies(Object handle);
	Object getCondition(Object handle);
	Object getConcurrency(Object handle);
	Collection getConnections(Object handle);
	Object getEffect(Object handle);
	Collection getElementResidences(Object handle);
	Collection getElementImports2(Object handle);
	Collection getElementImports(Object handle);
	Object getEntry(Object handle);
	Object getEnumeration(Object handle);
	List getEnumerationLiterals(Object handle);
	Object getExit(Object handle);
	Object getExpression(Object handle);
	public Collection getExtendedElements(Object handle);
	Collection getExtends(Object handle);
	Collection getExtenders(Object handle);
	Object getExtension(Object handle);
	Object getExtensionPoint(Object handle,int index);
	Collection getExtensionPoints(Object handle);
	List getFeatures(Object handle);
	Object getGeneralization(Object handle,Object parent);
	Collection getGeneralizations(Object handle);
	Object getGuard(Object handle);
	Object getIcon(Object handle);
	Collection getIncludes(Object handle);
	Collection getIncluders(Object handle);
	Collection getIncomings(Object handle);
	Object getInitialValue(Object handle);
	Object getInstance(Object handle);
	Collection getInstances(Object handle);
	Collection getInStates(Object handle);
	Object getInteraction(Object handle);
	Collection getInteractions(Object handle);
	Collection getInternalTransitions(Object handle);
	Collection getMessages(Object handle);
	Collection getSuccessors(Object handle);
	Collection getActivatedMessages(Object handle);
	Collection getReceivedMessages(Object handle);
	Collection getSentMessages(Object handle);
	@Deprecated Object getModel(Object handle);
	Object getRoot(Object handle);
	Collection getRootElements();
	Object getModelElement(Object handle);
	Object getMultiplicity(Object handle);
	Collection getComments(Object handle);
	Collection getAnnotatedElements(Object handle);
	Object getCommunicationConnection(Object handle);
	Object getCommunicationLink(Object handle);
	Collection getCollaborations(Object handle);
	Object getComponentInstance(Object handle);
	Collection getConstrainingElements(Object handle);
	List getConstrainedElements(Object handle);
	Collection getConstraints(Object handle);
	Object getModelElementContainer(Object handle);
	List getModelElementContents(Object handle);
	List getModelElementAssociated(Object handle);
	Object getContainer(Object handle);
	Collection getContents(Object handle);
	Object getContext(Object handle);
	Collection getContexts(Object handle);
	Collection getCreateActions(Object handle);
	Object getDefaultValue(Object handle);
	Collection getDeferrableEvents(Object handle);
	Collection getDeployedComponents(Object handle);
	Collection getDeploymentLocations(Object handle);
	@Deprecated Object getDiscriminator(Object handle);
	Object getDispatchAction(Object handle);
	Object getDoActivity(Object handle);
	Collection getImportedElements(Object pack);
	Object getImportedElement(Object elementImport);
	Collection getLinks(Object handle);
	Collection getLinkEnds(Object handle);
	String getLocation(Object handle);
	Collection getMethods(Object handle);
	Object getNamespace(Object handle);
	Object getNodeInstance(Object handle);
	Collection getObjectFlowStates(Object handle);
	Object getOperation(Object handle);
	Collection getOccurrences(Object handle);
	List getOperations(Object handle);
	List getOperationsAndReceptions(Object handle);
	Object getNextEnd(Object handle);
	Object getOrdering(Object handle);
	Collection getOutgoings(Object handle);
	Collection getOtherAssociationEnds(Object handle);
	Collection getOtherLinkEnds(Object handle);
	Collection getOwnedElements(Object handle);
	Object getPowertype(Object handle);
	Collection getPowertypeRanges(Object handle);
	Collection getPredecessors(Object handle);
	List getQualifiers(Object handle);
	boolean hasReturnParameterDirectionKind(Object handle);
	Object getPackage(Object handle);
	Object getParameter(Object handle);
	Object getParameter(Object handle,int n);
	Collection getParameters(Object handle);
	List getParametersList(Object handle);
	Object getGeneral(Object handle);
	Collection getRaisedSignals(Object handle);
	Collection getReceptions(Object handle);
	Object getRecurrence(Object handle);
	Object getRepresentedClassifier(Object handle);
	Object getRepresentedOperation(Object handle);
	Object getScript(Object handle);
	Object getSender(Object handle);
	Object getSignal(Object handle);
	Object getResident(Object handle);
	Collection getResidentElements(Object handle);
	Collection getResidents(Object handle);
	Object getSource(Object handle);
	Collection getSources(Object handle);
	Collection getSourceFlows(Object handle);
	Collection getSpecializations(Object handle);
	Object getStateMachine(Object handle);
	Object getState(Object handle);
	Collection getStates(Object handle);
	Collection getStereotypes(Object handle);
	Collection getStimuli(Object handle);
	Collection getReceivedStimuli(Object handle);
	Collection getSentStimuli(Object handle);
	Collection getSubvertices(Object handle);
	Object getSubmachine(Object handle);
	Collection getSubmachineStates(Object handle);
	Collection getSupplierDependencies(Object handle);
	Object getTop(Object handle);
	Object getTransition(Object handle);
	Object getTrigger(Object handle);
	Object getType(Object handle);
	Collection getTypedValues(Object handle);
	Object getTarget(Object handle);
	@Deprecated Object getTargetScope(Object handle);
	Collection getTargetFlows(Object handle);
	List getTemplateParameters(Object handle);
	Object getDefaultElement(Object handle);
	Object getTemplate(Object handle);
	Object getBinding(Object handle);
	List getArguments(Object handle);
	int getUpper(Object handle);
	Object getUseCase(Object handle);
	int getLower(Object handle);
	Collection getTransitions(Object handle);
	List getStructuralFeatures(Object handle);
	String getSpecification(Object handle);
	Collection getSpecifications(Object handle);
	Collection getSuppliers(Object handle);
	Object getAction(Object handle);
	List getActions(Object handle);
	Object getActionSequence(Object handle);
	Object getActivator(Object handle);
	Object getActivityGraph(Object handle);
	List getActualArguments(Object handle);
	Object getAddition(Object handle);
	Object getAggregation(Object handle);
	String getAlias(Object handle);
	Collection getAssociatedClasses(Object handle);
	String getName(Object handle);
	Object getOwner(Object handle);
	String getTag(Object handle);
	Iterator getTaggedValues(Object handle);
	Collection getTaggedValuesCollection(Object handle);
	Object getTaggedValue(Object handle,String name);
	String getTaggedValueValue(Object handle,String name);
	String getTagOfTag(Object handle);
	Object getValue(Object handle);
	String getValueOfTag(Object handle);
	Collection getReferenceValue(Object taggedValue);
	Collection getDataValue(Object taggedValue);
	String getUUID(Object element);
	Object getVisibility(Object handle);
	Collection getPartitions(Object container);
	String getReferenceState(Object o);
	Object lookupIn(Object handle,String name);
	String getUMLClassName(Object handle);
	boolean isAArgument(Object modelElement);
	String getTipString(Object modelElement);
	String toString(Object modelElement);
	Object getWhen(Object target);
	Object getChangeExpression(Object target);
	boolean isATagDefinition(Object handle);
	Collection getTagDefinitions(Object handle);
	Object getTagDefinition(Object handle);
	boolean isSynch(Object handle);
	boolean isStatic(Object handle);
	boolean isReadOnly(Object handle);
	String[]getMetatypeNames();
	boolean isA(String metatypeName,Object element);
}



