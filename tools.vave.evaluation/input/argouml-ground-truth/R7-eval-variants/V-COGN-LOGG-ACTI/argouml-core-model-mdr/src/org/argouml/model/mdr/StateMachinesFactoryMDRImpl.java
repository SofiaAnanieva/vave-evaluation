package org.argouml.model.mdr;

import java.util.Collection;
import org.apache.log4j.Logger;
import org.argouml.model.StateMachinesFactory;
import org.omg.uml.behavioralelements.statemachines.CallEvent;
import org.omg.uml.behavioralelements.statemachines.ChangeEvent;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.Guard;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.SignalEvent;
import org.omg.uml.behavioralelements.statemachines.SimpleState;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.StateMachinesPackage;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.statemachines.StubState;
import org.omg.uml.behavioralelements.statemachines.SubmachineState;
import org.omg.uml.behavioralelements.statemachines.SynchState;
import org.omg.uml.behavioralelements.statemachines.TimeEvent;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.foundation.core.BehavioralFeature;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.datatypes.BooleanExpression;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;
import org.omg.uml.foundation.datatypes.TimeExpression;


class StateMachinesFactoryMDRImpl extends AbstractUmlModelFactoryMDR implements StateMachinesFactory {
	private Logger LOG = Logger.getLogger(StateMachinesFactoryMDRImpl.class);
	private MDRModelImplementation modelImpl;
	StateMachinesFactoryMDRImpl(MDRModelImplementation implementation) {
			modelImpl = implementation;
		}
	private StateMachinesPackage getSmPackage() {
		return modelImpl.getUmlPackage().getStateMachines();
	}
	public CallEvent createCallEvent() {
		CallEvent myCallEvent = getSmPackage().getCallEvent().createCallEvent();
		super.initialize(myCallEvent);
		return myCallEvent;
	}
	public ChangeEvent createChangeEvent() {
		ChangeEvent myChangeEvent = getSmPackage().getChangeEvent().createChangeEvent();
		super.initialize(myChangeEvent);
		return myChangeEvent;
	}
	public CompositeState createCompositeState() {
		CompositeState myCompositeState = getSmPackage().getCompositeState().createCompositeState();
		super.initialize(myCompositeState);
		return myCompositeState;
	}
	public FinalState createFinalState() {
		FinalState myFinalState = getSmPackage().getFinalState().createFinalState();
		super.initialize(myFinalState);
		return myFinalState;
	}
	public Guard createGuard() {
		Guard myGuard = getSmPackage().getGuard().createGuard();
		super.initialize(myGuard);
		return myGuard;
	}
	public Pseudostate createPseudostate() {
		Pseudostate myPseudostate = getSmPackage().getPseudostate().createPseudostate();
		super.initialize(myPseudostate);
		return myPseudostate;
	}
	public SignalEvent createSignalEvent() {
		SignalEvent mySignalEvent = getSmPackage().getSignalEvent().createSignalEvent();
		super.initialize(mySignalEvent);
		return mySignalEvent;
	}
	public SimpleState createSimpleState() {
		SimpleState mySimpleState = getSmPackage().getSimpleState().createSimpleState();
		super.initialize(mySimpleState);
		return mySimpleState;
	}
	public StateMachine createStateMachine() {
		StateMachine myStateMachine = getSmPackage().getStateMachine().createStateMachine();
		super.initialize(myStateMachine);
		return myStateMachine;
	}
	public StubState createStubState() {
		StubState myStubState = getSmPackage().getStubState().createStubState();
		super.initialize(myStubState);
		return myStubState;
	}
	public SubmachineState createSubmachineState() {
		SubmachineState mySubmachineState = getSmPackage().getSubmachineState().createSubmachineState();
		super.initialize(mySubmachineState);
		return mySubmachineState;
	}
	public SynchState createSynchState() {
		SynchState mySynchState = getSmPackage().getSynchState().createSynchState();
		super.initialize(mySynchState);
		return mySynchState;
	}
	public TimeEvent createTimeEvent() {
		TimeEvent myTimeEvent = getSmPackage().getTimeEvent().createTimeEvent();
		super.initialize(myTimeEvent);
		return myTimeEvent;
	}
	public Transition createTransition() {
		Transition myTransition = getSmPackage().getTransition().createTransition();
		super.initialize(myTransition);
		return myTransition;
	}
	public CompositeState buildCompositeStateOnStateMachine(Object statemachine) {
		if (statemachine instanceof StateMachine) {
			StateMachine sm = (StateMachine) statemachine;
			CompositeState top = createCompositeState();
			top.setStateMachine(sm);
			top.setName("top");
			sm.setTop(top);
			assert top.equals(sm.getTop());
			return top;
		}
		throw new IllegalArgumentException("statemachine");
	}
	public StateMachine buildStateMachine(Object oContext) {
		if (oContext != null&&(modelImpl.getStateMachinesHelper().isAddingStatemachineAllowed(oContext))) {
			StateMachine machine = createStateMachine();
			ModelElement context = (ModelElement) oContext;
			machine.setContext(context);
			if (context instanceof Classifier) {
				machine.setNamespace((Classifier) context);
			}else if (context instanceof BehavioralFeature) {
				BehavioralFeature feature = (BehavioralFeature) context;
				machine.setNamespace(feature.getOwner());
			}
			State top = buildCompositeStateOnStateMachine(machine);
			assert top.equals(machine.getTop());
			return machine;
		}
		throw new IllegalArgumentException("In buildStateMachine: " + "context null or not legal");
	}
	public Transition buildTransition(Object owningState,Object source,Object dest) {
		if (!(owningState instanceof CompositeState)) {
			throw new IllegalArgumentException("owningState");
		}
		if (!(source instanceof StateVertex)) {
			throw new IllegalArgumentException("source");
		}
		if (!(dest instanceof StateVertex)) {
			throw new IllegalArgumentException("dest");
		}
		CompositeState compositeState = (CompositeState) owningState;
		if (compositeState.getSubvertex().contains(source)&&compositeState.getSubvertex().contains(dest)) {
			Transition trans = createTransition();
			compositeState.getInternalTransition().add(trans);
			trans.setSource((StateVertex) source);
			trans.setTarget((StateVertex) dest);
			return trans;
		}
		throw new IllegalArgumentException("In buildTransition: " + "arguments not legal");
	}
	public Pseudostate buildPseudoState(Object compositeState) {
		if (compositeState instanceof CompositeState) {
			Pseudostate state = createPseudostate();
			state.setKind(PseudostateKindEnum.PK_CHOICE);
			state.setContainer((CompositeState) compositeState);
			((CompositeState) compositeState).getSubvertex().add(state);
			return state;
		}
		throw new IllegalArgumentException("Argument must be a CompositeState");
	}
	public SynchState buildSynchState(Object compositeState) {
		if (compositeState instanceof CompositeState) {
			SynchState state = createSynchState();
			state.setBound(0);
			state.setContainer((CompositeState) compositeState);
			return state;
		}
		throw new IllegalArgumentException("Argument must be a CompositeState");
	}
	public StubState buildStubState(Object compositeState) {
		if (compositeState instanceof CompositeState) {
			StubState state = createStubState();
			state.setReferenceState("");
			state.setContainer((CompositeState) compositeState);
			return state;
		}
		throw new IllegalArgumentException("Argument must be a CompositeState");
	}
	public CompositeState buildCompositeState(Object compositeState) {
		if (compositeState instanceof CompositeState) {
			CompositeState state = createCompositeState();
			state.setConcurrent(false);
			state.setContainer((CompositeState) compositeState);
			return state;
		}
		throw new IllegalArgumentException("Argument must be a CompositeState");
	}
	public SimpleState buildSimpleState(Object compositeState) {
		if (compositeState instanceof CompositeState) {
			SimpleState state = createSimpleState();
			state.setContainer((CompositeState) compositeState);
			return state;
		}
		throw new IllegalArgumentException("Argument must be a CompositeState");
	}
	public FinalState buildFinalState(Object compositeState) {
		if (compositeState instanceof CompositeState) {
			FinalState state = createFinalState();
			state.setContainer((CompositeState) compositeState);
			return state;
		}
		throw new IllegalArgumentException("Argument must be a CompositeState");
	}
	public SubmachineState buildSubmachineState(Object compositeState) {
		if (compositeState instanceof CompositeState) {
			SubmachineState state = createSubmachineState();
			state.setContainer((CompositeState) compositeState);
			return state;
		}
		throw new IllegalArgumentException("Argument must be a CompositeState");
	}
	public Transition buildInternalTransition(Object state) {
		if (state instanceof State) {
			Transition trans = createTransition();
			((State) state).getInternalTransition().add(trans);
			trans.setSource((State) state);
			trans.setTarget((State) state);
			return trans;
		}
		throw new IllegalArgumentException("Argument must be a State");
	}
	public Transition buildTransition(Object source,Object target) {
		if (source instanceof StateVertex&&target instanceof StateVertex) {
			Transition trans = createTransition();
			trans.setSource((StateVertex) source);
			trans.setTarget((StateVertex) target);
			trans.setStateMachine((StateMachine) modelImpl.getStateMachinesHelper().getStateMachine(source));
			return trans;
		}
		throw new IllegalArgumentException();
	}
	public CallEvent buildCallEvent(Object ns) {
		CallEvent event = createCallEvent();
		event.setNamespace((Namespace) ns);
		event.setName("");
		return event;
	}
	public CallEvent buildCallEvent(Object trans,String name,Object ns) {
		if (!(trans instanceof Transition)) {
			throw new IllegalArgumentException();
		}
		CallEvent evt = createCallEvent();
		evt.setNamespace((Namespace) ns);
		String operationName = (name.indexOf("(") > 0)?name.substring(0,name.indexOf("(")).trim():name.trim();
		evt.setName(operationName);
		Object op = modelImpl.getStateMachinesHelper().findOperationByName(trans,operationName);
		if (op != null) {
			evt.setOperation((Operation) op);
		}
		return evt;
	}
	public SignalEvent buildSignalEvent(Object ns) {
		SignalEvent event = createSignalEvent();
		event.setNamespace((Namespace) ns);
		event.setName("");
		return event;
	}
	public SignalEvent buildSignalEvent(String name,Object ns) {
		SignalEvent event = createSignalEvent();
		event.setNamespace((Namespace) ns);
		event.setName(name);
		return event;
	}
	public TimeEvent buildTimeEvent(Object ns) {
		TimeEvent event = createTimeEvent();
		event.setNamespace((Namespace) ns);
		event.setName("");
		return event;
	}
	public TimeEvent buildTimeEvent(String s,Object ns) {
		TimeEvent event = createTimeEvent();
		event.setNamespace((Namespace) ns);
		event.setName("");
		Object te = modelImpl.getDataTypesFactory().createTimeExpression("",s);
		event.setWhen((TimeExpression) te);
		return event;
	}
	public ChangeEvent buildChangeEvent(Object ns) {
		ChangeEvent event = createChangeEvent();
		event.setNamespace((Namespace) ns);
		event.setName("");
		return event;
	}
	public ChangeEvent buildChangeEvent(String expression,Object ns) {
		ChangeEvent event = buildChangeEvent(ns);
		Object ce = modelImpl.getDataTypesFactory().createBooleanExpression("",expression);
		event.setChangeExpression((BooleanExpression) ce);
		return event;
	}
	public Guard buildGuard(Object transition) {
		if (transition instanceof Transition) {
			Transition t = (Transition) transition;
			if (t.getGuard() != null) {
				LOG.warn("Replacing Guard " + t.getGuard().getName() + " on Transition " + t.getName());
			}
			Guard guard = createGuard();
			guard.setTransition((Transition) transition);
			return guard;
		}
		throw new IllegalArgumentException("transition: " + transition);
	}
	void deleteCallEvent(Object elem) {
		if (!(elem instanceof CallEvent)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteChangeEvent(Object elem) {
		if (!(elem instanceof ChangeEvent)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteCompositeState(Object elem) {
		if (!(elem instanceof CompositeState)) {
			throw new IllegalArgumentException();
		}
		final CompositeState compositeState = (CompositeState) elem;
		for (StateVertex vertex:compositeState.getSubvertex()) {
			modelImpl.getUmlFactory().delete(vertex);
		}
		final CompositeState containingCompositeState = compositeState.getContainer();
		if (containingCompositeState != null&&containingCompositeState.isConcurrent()) {
			final Collection<StateVertex>siblings = containingCompositeState.getSubvertex();
			final int substatesRemaining = siblings.size();
			if (substatesRemaining == 2) {
				for (StateVertex sibling:siblings) {
					if (sibling != compositeState) {
						modelImpl.getUmlFactory().delete(sibling);
					}
				}
			}
		}
	}
	void deleteEvent(Object elem) {
		if (!(elem instanceof Event)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteFinalState(Object elem) {
		if (!(elem instanceof FinalState)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteGuard(Object elem) {
		if (!(elem instanceof Guard)) {
			throw new IllegalArgumentException();
		}
	}
	void deletePseudostate(Object elem) {
		if (!(elem instanceof Pseudostate)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteSignalEvent(Object elem) {
		if (!(elem instanceof SignalEvent)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteState(Object elem) {
		if (!(elem instanceof State)) {
			throw new IllegalArgumentException();
		}
		State state = (State) elem;
		deleteNonNull(state.getDoActivity());
		deleteNonNull(state.getEntry());
		deleteNonNull(state.getExit());
		modelImpl.getUmlHelper().deleteCollection(state.getInternalTransition());
	}
	private void deleteNonNull(ModelElement action) {
		if (action != null) {
			modelImpl.getUmlFactory().delete(action);
		}
	}
	void deleteSimpleState(Object elem) {
		if (!(elem instanceof SimpleState)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteStateMachine(Object elem) {
		if (!(elem instanceof StateMachine)) {
			throw new IllegalArgumentException();
		}
		StateMachine stateMachine = (StateMachine) elem;
		State top = stateMachine.getTop();
		if (top != null) {
			modelImpl.getUmlFactory().delete(top);
		}
		modelImpl.getUmlHelper().deleteCollection(stateMachine.getSubmachineState());
	}
	void deleteStateVertex(Object elem) {
		if (!(elem instanceof StateVertex)) {
			throw new IllegalArgumentException();
		}
		modelImpl.getUmlHelper().deleteCollection(((StateVertex) elem).getIncoming());
		modelImpl.getUmlHelper().deleteCollection(((StateVertex) elem).getOutgoing());
	}
	void deleteStubState(Object elem) {
		if (!(elem instanceof StubState)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteSubmachineState(Object elem) {
		if (!(elem instanceof SubmachineState)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteSynchState(Object elem) {
		if (!(elem instanceof SynchState)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteTimeEvent(Object elem) {
		if (!(elem instanceof TimeEvent)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteTransition(Object elem) {
		if (!(elem instanceof Transition)) {
			throw new IllegalArgumentException();
		}
		final Transition transition = (Transition) elem;
		final Guard guard = transition.getGuard();
		if (guard != null) {
			modelImpl.getUmlFactory().delete(guard);
		}
	}
}



