package org.argouml.model.mdr;

import java.util.Collection;
import org.argouml.model.ActivityGraphsFactory;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.activitygraphs.CallState;
import org.omg.uml.behavioralelements.activitygraphs.ClassifierInState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.activitygraphs.Partition;
import org.omg.uml.behavioralelements.activitygraphs.SubactivityState;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.foundation.core.BehavioralFeature;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;


class ActivityGraphsFactoryMDRImpl extends AbstractUmlModelFactoryMDR implements ActivityGraphsFactory {
	private MDRModelImplementation modelImpl;
	ActivityGraphsFactoryMDRImpl(MDRModelImplementation implementation) {
			modelImpl = implementation;
		}
	public ActionState createActionState() {
		ActionState myActionState = modelImpl.getUmlPackage().getActivityGraphs().getActionState().createActionState();
		super.initialize(myActionState);
		return myActionState;
	}
	public ActivityGraph createActivityGraph() {
		ActivityGraph myActivityGraph = modelImpl.getUmlPackage().getActivityGraphs().getActivityGraph().createActivityGraph();
		super.initialize(myActivityGraph);
		return myActivityGraph;
	}
	public CallState createCallState() {
		CallState myCallState = modelImpl.getUmlPackage().getActivityGraphs().getCallState().createCallState();
		super.initialize(myCallState);
		return myCallState;
	}
	public ClassifierInState createClassifierInState() {
		ClassifierInState myClassifierInState = modelImpl.getUmlPackage().getActivityGraphs().getClassifierInState().createClassifierInState();
		super.initialize(myClassifierInState);
		return myClassifierInState;
	}
	public ObjectFlowState createObjectFlowState() {
		ObjectFlowState myObjectFlowState = modelImpl.getUmlPackage().getActivityGraphs().getObjectFlowState().createObjectFlowState();
		super.initialize(myObjectFlowState);
		return myObjectFlowState;
	}
	public Partition createPartition() {
		Partition myPartition = modelImpl.getUmlPackage().getActivityGraphs().getPartition().createPartition();
		super.initialize(myPartition);
		return myPartition;
	}
	public SubactivityState createSubactivityState() {
		SubactivityState mySubactivityState = modelImpl.getUmlPackage().getActivityGraphs().getSubactivityState().createSubactivityState();
		super.initialize(mySubactivityState);
		return mySubactivityState;
	}
	public ActivityGraph buildActivityGraph(Object theContext) {
		if (theContext instanceof ModelElement) {
			ActivityGraph myActivityGraph = createActivityGraph();
			myActivityGraph.setContext((ModelElement) theContext);
			if (theContext instanceof Namespace) {
				myActivityGraph.setNamespace((Namespace) theContext);
			}else if (theContext instanceof BehavioralFeature) {
				myActivityGraph.setNamespace(((BehavioralFeature) theContext).getOwner());
			}
			State top = (CompositeState) modelImpl.getStateMachinesFactory().buildCompositeStateOnStateMachine(myActivityGraph);
			myActivityGraph.setTop(top);
			return myActivityGraph;
		}
		throw new IllegalArgumentException("Cannot create an ActivityGraph with context " + theContext);
	}
	public ObjectFlowState buildObjectFlowState(Object compositeState) {
		if (!(compositeState instanceof CompositeState)) {
			throw new IllegalArgumentException();
		}
		ObjectFlowState state = createObjectFlowState();
		state.setContainer((CompositeState) compositeState);
		return state;
	}
	public ClassifierInState buildClassifierInState(Object classifier,Collection state) {
		if (!(classifier instanceof Classifier)) {
			throw new IllegalArgumentException();
		}
		if (state.size() < 1) {
			throw new IllegalArgumentException("Collection of states must have at least one element");
		}
		ClassifierInState c = createClassifierInState();
		c.setType((Classifier) classifier);
		c.getInState().addAll(state);
		c.setNamespace(((Classifier) classifier).getNamespace());
		c.setName(((Classifier) classifier).getName() + "inState[" + ((State) state.iterator().next()).getName() + "]");
		return c;
	}
	void deleteActionState(Object elem) {
		if (!(elem instanceof ActionState)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteActivityGraph(Object elem) {
		if (!(elem instanceof ActivityGraph)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteCallState(Object elem) {
		if (!(elem instanceof CallState)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteClassifierInState(Object elem) {
		if (!(elem instanceof ClassifierInState)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteObjectFlowState(Object elem) {
		if (!(elem instanceof ObjectFlowState)) {
			throw new IllegalArgumentException();
		}
	}
	void deletePartition(Object elem) {
		if (!(elem instanceof Partition)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteSubactivityState(Object elem) {
		if (!(elem instanceof SubactivityState)) {
			throw new IllegalArgumentException();
		}
	}
}



