package org.argouml.model.euml;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.StateMachinesFactory;
import org.eclipse.uml2.uml.UMLFactory;
import org.argouml.model.euml.EUMLModelImplementation;


class StateMachinesFactoryEUMLImpl implements StateMachinesFactory,AbstractModelFactory {
	private EUMLModelImplementation modelImpl;
	public StateMachinesFactoryEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public Object buildCallEvent(Object ns) {
		throw new NotYetImplementedException();
	}
	public Object buildCallEvent(Object trans,String name,Object ns) {
		throw new NotYetImplementedException();
	}
	public Object buildChangeEvent(Object ns) {
		throw new NotYetImplementedException();
	}
	public Object buildChangeEvent(String s,Object ns) {
		throw new NotYetImplementedException();
	}
	public Object buildCompositeState(Object compositeState) {
		throw new NotYetImplementedException();
	}
	public Object buildCompositeStateOnStateMachine(Object statemachine) {
		throw new NotYetImplementedException();
	}
	public Object buildFinalState(Object compositeState) {
		throw new NotYetImplementedException();
	}
	public Object buildGuard(Object transition) {
		throw new NotYetImplementedException();
	}
	public Object buildInternalTransition(Object state) {
		throw new NotYetImplementedException();
	}
	public Object buildPseudoState(Object compositeState) {
		throw new NotYetImplementedException();
	}
	public Object buildSignalEvent(Object ns) {
		throw new NotYetImplementedException();
	}
	public Object buildSignalEvent(String name,Object ns) {
		throw new NotYetImplementedException();
	}
	public Object buildSimpleState(Object compositeState) {
		throw new NotYetImplementedException();
	}
	public Object buildStateMachine(Object oContext) {
		throw new NotYetImplementedException();
	}
	public Object buildStubState(Object compositeState) {
		throw new NotYetImplementedException();
	}
	public Object buildSubmachineState(Object compositeState) {
		throw new NotYetImplementedException();
	}
	public Object buildSynchState(Object compositeState) {
		throw new NotYetImplementedException();
	}
	public Object buildTimeEvent(Object ns) {
		throw new NotYetImplementedException();
	}
	public Object buildTimeEvent(String s,Object ns) {
		throw new NotYetImplementedException();
	}
	public Object buildTransition(Object owningState,Object source,Object dest) {
		throw new NotYetImplementedException();
	}
	public Object buildTransition(Object source,Object target) {
		throw new NotYetImplementedException();
	}
	public Object createCallEvent() {
		return UMLFactory.eINSTANCE.createCallEvent();
	}
	public Object createChangeEvent() {
		return UMLFactory.eINSTANCE.createChangeEvent();
	}
	public Object createCompositeState() {
		throw new NotYetImplementedException();
	}
	public Object createFinalState() {
		throw new NotYetImplementedException();
	}
	public Object createGuard() {
		throw new NotYetImplementedException();
	}
	public Object createPseudostate() {
		return UMLFactory.eINSTANCE.createPseudostate();
	}
	public Object createSignalEvent() {
		return UMLFactory.eINSTANCE.createSignalEvent();
	}
	public Object createSimpleState() {
		throw new NotYetImplementedException();
	}
	public Object createStateMachine() {
		return UMLFactory.eINSTANCE.createStateMachine();
	}
	public Object createStubState() {
		throw new NotYetImplementedException();
	}
	public Object createSubmachineState() {
		throw new NotYetImplementedException();
	}
	public Object createSynchState() {
		throw new NotYetImplementedException();
	}
	public Object createTimeEvent() {
		return UMLFactory.eINSTANCE.createTimeEvent();
	}
	public Object createTransition() {
		return UMLFactory.eINSTANCE.createTransition();
	}
}



