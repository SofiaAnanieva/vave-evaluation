package org.argouml.model;

import java.io.OutputStream;


public final class Model {
	private static ActivityGraphsHelper activityGraphsHelper;
	private static CollaborationsHelper collaborationsHelper;
	private static CommonBehaviorHelper commonBehaviorHelper;
	private static CoreHelper coreHelper;
	private static DataTypesHelper dataTypesHelper;
	private static ExtensionMechanismsHelper extensionMechanismsHelper;
	private static StateMachinesHelper stateMachinesHelper;
	private static UmlHelper umlHelper;
	private static UseCasesHelper useCasesHelper;
	private static ModelCommandCreationObserver modelCommandCreationObserver;
	private Model() {
	}
	private static ModelImplementation impl;
	public static void setImplementation(ModelImplementation newImpl) {
		impl = newImpl;
		activityGraphsHelper = impl.getActivityGraphsHelper();
		collaborationsHelper = impl.getCollaborationsHelper();
		commonBehaviorHelper = impl.getCommonBehaviorHelper();
		coreHelper = impl.getCoreHelper();
		dataTypesHelper = impl.getDataTypesHelper();
		extensionMechanismsHelper = impl.getExtensionMechanismsHelper();
		stateMachinesHelper = impl.getStateMachinesHelper();
		umlHelper = impl.getUmlHelper();
		useCasesHelper = impl.getUseCasesHelper();
	}
	public static Facade getFacade() {
		return impl.getFacade();
	}
	public static ModelEventPump getPump() {
		return impl.getModelEventPump();
	}
	public static DiagramInterchangeModel getDiagramInterchangeModel() {
		return impl.getDiagramInterchangeModel();
	}
	public static CollaborationsFactory getCollaborationsFactory() {
		return impl.getCollaborationsFactory();
	}
	public static CollaborationsHelper getCollaborationsHelper() {
		return collaborationsHelper;
	}
	public static CommonBehaviorFactory getCommonBehaviorFactory() {
		return impl.getCommonBehaviorFactory();
	}
	public static CommonBehaviorHelper getCommonBehaviorHelper() {
		return commonBehaviorHelper;
	}
	public static CoreFactory getCoreFactory() {
		return impl.getCoreFactory();
	}
	public static CoreHelper getCoreHelper() {
		return coreHelper;
	}
	public static DataTypesFactory getDataTypesFactory() {
		return impl.getDataTypesFactory();
	}
	public static DataTypesHelper getDataTypesHelper() {
		return dataTypesHelper;
	}
	public static ExtensionMechanismsFactory getExtensionMechanismsFactory() {
		return impl.getExtensionMechanismsFactory();
	}
	public static ExtensionMechanismsHelper getExtensionMechanismsHelper() {
		return extensionMechanismsHelper;
	}
	public static ModelManagementFactory getModelManagementFactory() {
		return impl.getModelManagementFactory();
	}
	public static ModelManagementHelper getModelManagementHelper() {
		return impl.getModelManagementHelper();
	}
	public static StateMachinesFactory getStateMachinesFactory() {
		return impl.getStateMachinesFactory();
	}
	public static StateMachinesHelper getStateMachinesHelper() {
		return stateMachinesHelper;
	}
	public static UmlFactory getUmlFactory() {
		return impl.getUmlFactory();
	}
	public static UmlHelper getUmlHelper() {
		return umlHelper;
	}
	public static UseCasesFactory getUseCasesFactory() {
		return impl.getUseCasesFactory();
	}
	public static UseCasesHelper getUseCasesHelper() {
		return useCasesHelper;
	}
	public static MetaTypes getMetaTypes() {
		return impl.getMetaTypes();
	}
	@Deprecated public static ChangeableKind getChangeableKind() {
		return impl.getChangeableKind();
	}
	public static AggregationKind getAggregationKind() {
		return impl.getAggregationKind();
	}
	public static PseudostateKind getPseudostateKind() {
		return impl.getPseudostateKind();
	}
	@Deprecated public static ScopeKind getScopeKind() {
		return impl.getScopeKind();
	}
	public static ConcurrencyKind getConcurrencyKind() {
		return impl.getConcurrencyKind();
	}
	public static DirectionKind getDirectionKind() {
		return impl.getDirectionKind();
	}
	public static OrderingKind getOrderingKind() {
		return impl.getOrderingKind();
	}
	public static VisibilityKind getVisibilityKind() {
		return impl.getVisibilityKind();
	}
	public static XmiReader getXmiReader()throws UmlException {
		return impl.getXmiReader();
	}
	public static XmiWriter getXmiWriter(Object model,OutputStream stream,String version)throws UmlException {
		return impl.getXmiWriter(model,stream,version);
	}
	public static Throwable initialise(String modelName) {
		ModelImplementation newImplementation = null;
		try {
			Class implType = Class.forName(modelName);
			newImplementation = (ModelImplementation) implType.newInstance();
		}catch (ClassNotFoundException e) {
			return e;
		}catch (NoClassDefFoundError e) {
			return e;
		}catch (InstantiationException e) {
			return e;
		}catch (IllegalAccessException e) {
			return e;
		}
		if (newImplementation == null) {
			return new Throwable();
		}
		Model.setImplementation(newImplementation);
		return null;
	}
	public static boolean isInitiated() {
		return impl != null;
	}
	public static void setModelCommandCreationObserver(ModelCommandCreationObserver observer) {
		modelCommandCreationObserver = observer;
	}
	public static ModelCommandCreationObserver getModelCommandCreationObserver() {
		return modelCommandCreationObserver;
	}
	public static Object execute(ModelCommand command) {
		ModelCommandCreationObserver mco = getModelCommandCreationObserver();
		if (mco != null) {
			return mco.execute(command);
		}else {
			return command.execute();
		}
	}
	public static CopyHelper getCopyHelper() {
		return impl.getCopyHelper();
	}
}



