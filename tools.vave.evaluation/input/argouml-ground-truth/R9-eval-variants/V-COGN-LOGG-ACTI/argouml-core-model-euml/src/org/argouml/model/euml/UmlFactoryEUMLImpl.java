package org.argouml.model.euml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.argouml.model.AbstractModelFactory;
import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.InvalidElementException;
import org.argouml.model.MetaTypes;
import org.argouml.model.UmlFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.UseCase;
import org.argouml.model.euml.EUMLModelImplementation;


class UmlFactoryEUMLImpl implements UmlFactory,AbstractModelFactory {
	private static final Logger LOG = Logger.getLogger(UmlFactoryEUMLImpl.class);
	private EUMLModelImplementation modelImpl;
	private MetaTypes metaTypes;
	private Map validConnectionMap = new HashMap();
	private static final Object[][]VALID_CONNECTIONS =  { {Generalization.class,Classifier.class}, {Dependency.class,Element.class}, {Usage.class,NamedElement.class}, {PackageImport.class,NamedElement.class}, {Abstraction.class,org.eclipse.uml2.uml.Class.class,Interface.class,null}, {Association.class,Type.class}, {Extend.class,UseCase.class}, {Include.class,UseCase.class}, {Transition.class,State.class}, {AssociationClass.class,Type.class}};
	public UmlFactoryEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
		metaTypes = modelImpl.getMetaTypes();
		buildValidConnectionMap();
	}
	public Object buildConnection(Object elementType,Object fromElement,Object fromStyle,Object toElement,Object toStyle,Object unidirectional,Object namespace)throws IllegalModelElementConnectionException {
		IllegalModelElementConnectionException exception = new IllegalModelElementConnectionException("Cannot make a " + elementType.getClass().getName() + " between a " + fromElement.getClass().getName() + " and a " + toElement.getClass().getName());
		if (!isConnectionValid(elementType,fromElement,toElement,true)) {
			throw exception;
		}
		Object connection = null;
		if (elementType == metaTypes.getAssociation()) {
			connection = modelImpl.getCoreFactory().buildAssociation((Classifier) fromElement,(AggregationKind) fromStyle,(Classifier) toElement,(AggregationKind) toStyle,(Boolean) unidirectional);
		}else if (elementType == metaTypes.getAssociationEnd()) {
			if (fromElement instanceof Association) {
				connection = modelImpl.getCoreFactory().buildAssociationEnd(toElement,fromElement);
			}else if (fromElement instanceof Classifier) {
				connection = modelImpl.getCoreFactory().buildAssociationEnd(fromElement,toElement);
			}
		}else if (elementType == metaTypes.getAssociationClass()) {
			connection = modelImpl.getCoreFactory().buildAssociationClass(fromElement,toElement);
		}else if (elementType == metaTypes.getAssociationRole()) {
			connection = modelImpl.getCollaborationsFactory().buildAssociationRole(fromElement,fromStyle,toElement,toStyle,(Boolean) unidirectional);
		}else if (elementType == metaTypes.getGeneralization()) {
			connection = modelImpl.getCoreFactory().buildGeneralization(fromElement,toElement);
		}else if (elementType == metaTypes.getPackageImport()) {
			connection = modelImpl.getCoreFactory().buildPackageImport(fromElement,toElement);
		}else if (elementType == metaTypes.getUsage()) {
			connection = modelImpl.getCoreFactory().buildUsage(fromElement,toElement);
		}else if (elementType == metaTypes.getDependency()) {
			connection = modelImpl.getCoreFactory().buildDependency(fromElement,toElement);
		}else if (elementType == metaTypes.getAbstraction()) {
			connection = modelImpl.getCoreFactory().buildRealization(fromElement,toElement,namespace);
		}else if (elementType == metaTypes.getLink()) {
			connection = modelImpl.getCommonBehaviorFactory().buildLink(fromElement,toElement);
		}else if (elementType == metaTypes.getExtend()) {
			connection = modelImpl.getUseCasesFactory().buildExtend(toElement,fromElement);
		}else if (elementType == metaTypes.getInclude()) {
			connection = modelImpl.getUseCasesFactory().buildInclude(fromElement,toElement);
		}else if (elementType == metaTypes.getTransition()) {
			connection = modelImpl.getStateMachinesFactory().buildTransition(fromElement,toElement);
		}
		if (connection == null) {
			throw exception;
		}
		return connection;
	}
	public Object buildNode(Object elementType,Object container) {
		Object element = buildNode(elementType);
		modelImpl.getCoreHelper().addOwnedElement(container,element);
		return element;
	}
	public Object buildNode(Object elementType) {
		Object o = null;
		if (elementType == metaTypes.getActor()) {
			o = modelImpl.getUseCasesFactory().createActor();
		}else if (elementType == metaTypes.getUseCase()) {
			o = modelImpl.getUseCasesFactory().createUseCase();
		}else if (elementType == metaTypes.getUMLClass()) {
			o = modelImpl.getCoreFactory().buildClass();
		}else if (elementType == metaTypes.getInterface()) {
			o = modelImpl.getCoreFactory().buildInterface();
		}else if (elementType == metaTypes.getDataType()) {
			o = modelImpl.getCoreFactory().createDataType();
		}else if (elementType == metaTypes.getPackage()) {
			o = modelImpl.getModelManagementFactory().createPackage();
		}else if (elementType == metaTypes.getModel()) {
			o = modelImpl.getModelManagementFactory().createModel();
		}else if (elementType == metaTypes.getInstance()) {
			throw new IllegalArgumentException("Attempt to instantiate abstract type");
		}else if (elementType == metaTypes.getSubsystem()) {
			o = modelImpl.getModelManagementFactory().createSubsystem();
		}else if (elementType == metaTypes.getCallState()) {
			o = modelImpl.getActivityGraphsFactory().createCallState();
		}else if (elementType == metaTypes.getSimpleState()) {
			o = modelImpl.getStateMachinesFactory().createSimpleState();
		}else if (elementType == metaTypes.getFinalState()) {
			o = modelImpl.getStateMachinesFactory().createFinalState();
		}else if (elementType == metaTypes.getPseudostate()) {
			o = modelImpl.getStateMachinesFactory().createPseudostate();
		}else if (elementType == metaTypes.getObjectFlowState()) {
			o = modelImpl.getActivityGraphsFactory().createObjectFlowState();
		}else if (elementType == metaTypes.getActionState()) {
			o = modelImpl.getActivityGraphsFactory().createActionState();
		}else if (elementType == metaTypes.getSubactivityState()) {
			o = modelImpl.getActivityGraphsFactory().createSubactivityState();
		}else if (elementType == metaTypes.getPartition()) {
			o = modelImpl.getActivityGraphsFactory().createPartition();
		}else if (elementType == metaTypes.getStubState()) {
			o = modelImpl.getStateMachinesFactory().createStubState();
		}else if (elementType == metaTypes.getSubmachineState()) {
			o = modelImpl.getStateMachinesFactory().createSubmachineState();
		}else if (elementType == metaTypes.getCompositeState()) {
			o = modelImpl.getStateMachinesFactory().createCompositeState();
		}else if (elementType == metaTypes.getSynchState()) {
			o = modelImpl.getStateMachinesFactory().createSynchState();
		}else if (elementType == metaTypes.getState()) {
			throw new IllegalArgumentException("Attempt to instantiate abstract type");
		}else if (elementType == modelImpl.getMetaTypes().getSimpleState()) {
			o = modelImpl.getStateMachinesFactory().createSimpleState();
		}else if (elementType == metaTypes.getClassifierRole()) {
			o = modelImpl.getCollaborationsFactory().createClassifierRole();
		}else if (elementType == metaTypes.getComponent()) {
			o = modelImpl.getCoreFactory().createComponent();
		}else if (elementType == metaTypes.getComponentInstance()) {
			o = modelImpl.getCommonBehaviorFactory().createComponentInstance();
		}else if (elementType == metaTypes.getNode()) {
			o = modelImpl.getCoreFactory().createNode();
		}else if (elementType == metaTypes.getNodeInstance()) {
			o = modelImpl.getCommonBehaviorFactory().createNodeInstance();
		}else if (elementType == metaTypes.getObject()) {
			o = modelImpl.getCommonBehaviorFactory().createObject();
		}else if (elementType == metaTypes.getComment()) {
			o = modelImpl.getCoreFactory().createComment();
		}else if (elementType == metaTypes.getNamespace()) {
			throw new IllegalArgumentException("Attempt to instantiate abstract type");
		}else if (elementType == metaTypes.getOperation()) {
			o = modelImpl.getCoreFactory().createOperation();
		}else if (elementType == metaTypes.getEnumeration()) {
			o = modelImpl.getCoreFactory().createEnumeration();
		}else if (elementType == metaTypes.getStereotype()) {
			o = modelImpl.getExtensionMechanismsFactory().createStereotype();
		}else if (elementType == metaTypes.getAttribute()) {
			o = modelImpl.getCoreFactory().createAttribute();
		}else if (elementType == metaTypes.getSignal()) {
			o = modelImpl.getCommonBehaviorFactory().createSignal();
		}else if (elementType == metaTypes.getException()) {
			o = modelImpl.getCommonBehaviorFactory().createException();
		}else if (elementType == metaTypes.getTransition()) {
			o = modelImpl.getStateMachinesFactory().createTransition();
		}
		if (!(o instanceof EObject)) {
			throw new IllegalArgumentException("Attempted to create unsupported model element type: " + elementType);
		}
		return o;
	}
	public void delete(final Object elem) {
		if (!(elem instanceof EObject)) {
			throw new IllegalArgumentException("elem must be instance of EObject");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		EcoreUtil.delete((EObject) elem);
	}
};
		modelImpl.getEditingDomain().getCommandStack().execute(new ChangeCommand(modelImpl,run,"Remove from the model the element #",elem));
	}
	public boolean isRemoved(Object o) {
		if (o instanceof Element) {
			return((Element) o).eResource() == null;
		}
		throw new IllegalArgumentException("Not an Element : " + o);
	}
	public boolean isConnectionType(Object connectionType) {
		throw new NotYetImplementedException();
	}
	public boolean isConnectionValid(Object connectionType,Object fromElement,Object toElement,boolean checkWFR) {
		List validItems = (ArrayList) validConnectionMap.get(connectionType);
		if (validItems == null) {
			return false;
		}
		Iterator it = validItems.iterator();
		while (it.hasNext()) {
			Class[]modeElementPair = (Class[]) it.next();
			if (modeElementPair[0].isInstance(fromElement)&&modeElementPair[1].isInstance(toElement)) {
				if (checkWFR) {
					return isConnectionWellformed((Class) connectionType,(Element) fromElement,(Element) toElement);
				}else {
					return true;
				}
			}
		}
		return false;
	}
	public boolean isContainmentValid(Object metaType,Object container) {
		return true;
	}
	private boolean isConnectionWellformed(Class connectionType,Element fromElement,Element toElement) {
		if (fromElement == null||toElement == null) {
			return false;
		}
		if (connectionType == Generalization.class) {
			if (fromElement.getClass() != toElement.getClass()) {
				return false;
			}
		}
		return true;
	}
	private void buildValidConnectionMap() {
		Object connection = null;
		for (int i = 0;i < VALID_CONNECTIONS.;++i) {
			connection = VALID_CONNECTIONS[i][0];
			List validItems = (ArrayList) validConnectionMap.get(connection);
			if (validItems == null) {
				validItems = new ArrayList();
				validConnectionMap.put(connection,validItems);
			}
			if (VALID_CONNECTIONS[i]. < 3) {
				Object[]modeElementPair = new Class[2];
				modeElementPair[0] = VALID_CONNECTIONS[i][1];
				modeElementPair[1] = VALID_CONNECTIONS[i][1];
				validItems.add(modeElementPair);
			}else {
				Object[]modeElementPair = new Class[2];
				modeElementPair[0] = VALID_CONNECTIONS[i][1];
				modeElementPair[1] = VALID_CONNECTIONS[i][2];
				validItems.add(modeElementPair);
				if (VALID_CONNECTIONS[i]. < 4&&VALID_CONNECTIONS[i][1] != VALID_CONNECTIONS[i][2]) {
					Object[]reversedModeElementPair = new Class[2];
					reversedModeElementPair[0] = VALID_CONNECTIONS[i][2];
					reversedModeElementPair[1] = VALID_CONNECTIONS[i][1];
					validItems.add(reversedModeElementPair);
				}
			}
		}
	}
	public void deleteExtent(Object element) {
		Resource resource = ((EObject) element).eResource();
		if (resource != null) {
			modelImpl.unloadResource(resource);
		}else {
			LOG.warn("Tried to delete null resource");
			throw new InvalidElementException(element != null?element.toString():"Null");
		}
	}
}



