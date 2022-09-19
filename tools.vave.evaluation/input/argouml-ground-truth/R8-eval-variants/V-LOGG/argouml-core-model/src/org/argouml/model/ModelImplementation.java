package org.argouml.model;

import java.io.OutputStream;
import java.io.Writer;


public interface ModelImplementation {
	Facade getFacade();
	DiagramInterchangeModel getDiagramInterchangeModel();
	ModelEventPump getModelEventPump();
	ActivityGraphsFactory getActivityGraphsFactory();
	ActivityGraphsHelper getActivityGraphsHelper();
	CollaborationsFactory getCollaborationsFactory();
	CollaborationsHelper getCollaborationsHelper();
	CommonBehaviorFactory getCommonBehaviorFactory();
	CommonBehaviorHelper getCommonBehaviorHelper();
	CoreFactory getCoreFactory();
	CoreHelper getCoreHelper();
	DataTypesFactory getDataTypesFactory();
	DataTypesHelper getDataTypesHelper();
	ExtensionMechanismsFactory getExtensionMechanismsFactory();
	ExtensionMechanismsHelper getExtensionMechanismsHelper();
	ModelManagementFactory getModelManagementFactory();
	ModelManagementHelper getModelManagementHelper();
	StateMachinesFactory getStateMachinesFactory();
	StateMachinesHelper getStateMachinesHelper();
	UmlFactory getUmlFactory();
	UmlHelper getUmlHelper();
	UseCasesFactory getUseCasesFactory();
	UseCasesHelper getUseCasesHelper();
	MetaTypes getMetaTypes();
	@Deprecated ChangeableKind getChangeableKind();
	AggregationKind getAggregationKind();
	PseudostateKind getPseudostateKind();
	@Deprecated ScopeKind getScopeKind();
	ConcurrencyKind getConcurrencyKind();
	DirectionKind getDirectionKind();
	OrderingKind getOrderingKind();
	VisibilityKind getVisibilityKind();
	XmiReader getXmiReader()throws UmlException;
	XmiWriter getXmiWriter(Object model,OutputStream stream,String version)throws UmlException;
	CopyHelper getCopyHelper();
}



