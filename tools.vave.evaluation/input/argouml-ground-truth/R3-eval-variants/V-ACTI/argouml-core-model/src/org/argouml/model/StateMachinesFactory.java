package org.argouml.model;


public interface StateMachinesFactory extends Factory {
	Object createCallEvent();
	Object createChangeEvent();
	Object createCompositeState();
	Object createFinalState();
	Object createGuard();
	Object createPseudostate();
	Object createSignalEvent();
	Object createSimpleState();
	Object createStateMachine();
	Object createStubState();
	Object createSubmachineState();
	Object createSynchState();
	Object createTimeEvent();
	Object createTransition();
	Object buildCompositeStateOnStateMachine(Object statemachine);
	Object buildStateMachine(Object oContext);
	Object buildTransition(Object owningState,Object source,Object dest);
	Object buildPseudoState(Object compositeState);
	Object buildSynchState(Object compositeState);
	Object buildStubState(Object compositeState);
	Object buildCompositeState(Object compositeState);
	Object buildSimpleState(Object compositeState);
	Object buildFinalState(Object compositeState);
	Object buildSubmachineState(Object compositeState);
	Object buildInternalTransition(Object state);
	Object buildTransition(Object source,Object target);
	Object buildCallEvent(Object ns);
	Object buildCallEvent(Object trans,String name,Object ns);
	Object buildSignalEvent(Object ns);
	Object buildSignalEvent(String name,Object ns);
	Object buildTimeEvent(Object ns);
	Object buildTimeEvent(String s,Object ns);
	Object buildChangeEvent(Object ns);
	Object buildChangeEvent(String s,Object ns);
	Object buildGuard(Object transition);
}



