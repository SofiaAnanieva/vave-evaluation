package org.argouml.model.euml;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.CommonBehaviorFactory;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.UMLFactory;
import org.argouml.model.euml.EUMLModelImplementation;


class CommonBehaviorFactoryEUMLImpl implements CommonBehaviorFactory,AbstractModelFactory {
	private EUMLModelImplementation modelImpl;
	public CommonBehaviorFactoryEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public Object buildAction(Object message) {
		return null;
	}
	public Object buildCallAction(Object oper,String name) {
		return null;
	}
	public Object buildLink(Object fromInstance,Object toInstance) {
		return null;
	}
	public Object buildReception(Object aClassifier) {
		return null;
	}
	public Object buildSignal(Object element) {
		return null;
	}
	public Object buildStimulus(Object link) {
		return null;
	}
	public Object buildUninterpretedAction(Object actionState) {
		return null;
	}
	public Object createActionSequence() {
		return null;
	}
	public Object createArgument() {
		return null;
	}
	public Object createAttributeLink() {
		return null;
	}
	public Object createCallAction() {
		return null;
	}
	public Object createComponentInstance() {
		return null;
	}
	public Object createCreateAction() {
		return UMLFactory.eINSTANCE.createCreateObjectAction();
	}
	public Object createDataValue() {
		return null;
	}
	public Object createDestroyAction() {
		return UMLFactory.eINSTANCE.createDestroyObjectAction();
	}
	public Object createException() {
		return null;
	}
	public Object createLink() {
		return null;
	}
	public Object createLinkEnd() {
		return null;
	}
	public Object createLinkObject() {
		return null;
	}
	public Object createNodeInstance() {
		return null;
	}
	public Object createObject() {
		return null;
	}
	public Object createReception() {
		return UMLFactory.eINSTANCE.createReception();
	}
	public Object createReturnAction() {
		return null;
	}
	public Object createSendAction() {
		return UMLFactory.eINSTANCE.createSendObjectAction();
	}
	public Signal createSignal() {
		return UMLFactory.eINSTANCE.createSignal();
	}
	public Object createStimulus() {
		return null;
	}
	public Object createSubsystemInstance() {
		return null;
	}
	public Object createTerminateAction() {
		return null;
	}
	public OpaqueAction createUninterpretedAction() {
		return null;
	}
}



