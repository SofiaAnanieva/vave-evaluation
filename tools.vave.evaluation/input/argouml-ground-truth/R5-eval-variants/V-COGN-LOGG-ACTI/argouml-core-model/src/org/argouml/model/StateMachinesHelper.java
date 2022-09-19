package org.argouml.model;

import java.util.Collection;


public interface StateMachinesHelper {
	Object getSource(Object trans);
	Object getDestination(Object trans);
	Object getStateMachine(Object handle);
	void setEventAsTrigger(Object transition,Object event);
	boolean isAddingStatemachineAllowed(Object context);
	boolean isTopState(Object o);
	Collection getAllPossibleStatemachines(Object model,Object oSubmachineState);
	Collection getAllPossibleSubvertices(Object oState);
	void setStatemachineAsSubmachine(Object oSubmachineState,Object oStatemachine);
	Object getTop(Object sm);
	Collection getOutgoingStates(Object ostatevertex);
	Object findOperationByName(Object trans,String opname);
	Collection getAllSubStates(Object compState);
	void removeSubvertex(Object handle,Object subvertex);
	void addSubvertex(Object handle,Object subvertex);
	void setBound(Object handle,int bound);
	void setConcurrent(Object handle,boolean concurrent);
	void setContainer(Object handle,Object compositeState);
	void setDoActivity(Object handle,Object value);
	void setEffect(Object handle,Object value);
	void setEntry(Object handle,Object value);
	void setExit(Object handle,Object value);
	void setExpression(Object handle,Object value);
	void setGuard(Object handle,Object guard);
	void setInternalTransitions(Object handle,Collection intTrans);
	void setSource(Object handle,Object state);
	void setState(Object handle,Object element);
	void setStateMachine(Object handle,Object stm);
	void setSubvertices(Object handle,Collection subvertices);
	void setTrigger(Object handle,Object event);
	void setWhen(Object handle,Object value);
	void setChangeExpression(Object handle,Object value);
	String getPath(Object o);
	Object getStatebyName(String path,Object container);
	void setReferenceState(Object o,String referenced);
	Object findNamespaceForEvent(Object trans,Object model);
	void setContext(Object statemachine,Object modelElement);
	void addDeferrableEvent(Object state,Object deferrableEvent);
	void removeDeferrableEvent(Object state,Object deferrableEvent);
}



