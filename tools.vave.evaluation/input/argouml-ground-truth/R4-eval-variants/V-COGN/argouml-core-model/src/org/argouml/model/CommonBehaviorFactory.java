package org.argouml.model;


public interface CommonBehaviorFactory extends Factory {
	Object createActionSequence();
	Object createArgument();
	Object createAttributeLink();
	Object createCallAction();
	Object createComponentInstance();
	Object createCreateAction();
	Object createDataValue();
	Object createDestroyAction();
	Object createException();
	Object createLink();
	Object createLinkEnd();
	Object createLinkObject();
	Object createNodeInstance();
	Object createObject();
	Object createReception();
	Object createReturnAction();
	Object createSendAction();
	Object createSignal();
	Object createStimulus();
	Object createSubsystemInstance();
	Object createTerminateAction();
	Object createUninterpretedAction();
	Object buildCallAction(Object oper,String name);
	Object buildUninterpretedAction(Object actionState);
	Object buildLink(Object fromInstance,Object toInstance);
	Object buildAction(Object message);
	Object buildSignal(Object element);
	Object buildStimulus(Object link);
	Object buildReception(Object aClassifier);
}



