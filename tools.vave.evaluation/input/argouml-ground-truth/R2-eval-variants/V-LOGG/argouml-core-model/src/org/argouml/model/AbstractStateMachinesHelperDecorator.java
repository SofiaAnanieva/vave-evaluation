package org.argouml.model;

import java.util.Collection;


public abstract class AbstractStateMachinesHelperDecorator implements StateMachinesHelper {
	private StateMachinesHelper impl;
	protected AbstractStateMachinesHelperDecorator(StateMachinesHelper component) {
		impl = component;
	}
	protected StateMachinesHelper getComponent() {
		return impl;
	}
	public Object getSource(Object trans) {
		return impl.getSource(trans);
	}
	public Object getDestination(Object trans) {
		return impl.getDestination(trans);
	}
	public Object getStateMachine(Object handle) {
		return impl.getStateMachine(handle);
	}
	public void setEventAsTrigger(Object transition,Object event) {
		impl.setEventAsTrigger(transition,event);
	}
	public boolean isAddingStatemachineAllowed(Object context) {
		return impl.isAddingStatemachineAllowed(context);
	}
	public boolean isTopState(Object o) {
		return impl.isTopState(o);
	}
	public Collection getAllPossibleStatemachines(Object model,Object oSubmachineState) {
		return impl.getAllPossibleStatemachines(model,oSubmachineState);
	}
	public Collection getAllPossibleSubvertices(Object oState) {
		return impl.getAllPossibleSubvertices(oState);
	}
	public void setStatemachineAsSubmachine(Object oSubmachineState,Object oStatemachine) {
		impl.setStatemachineAsSubmachine(oSubmachineState,oStatemachine);
	}
	public Object getTop(Object sm) {
		return impl.getTop(sm);
	}
	public Collection getOutgoingStates(Object ostatevertex) {
		return impl.getOutgoingStates(ostatevertex);
	}
	public Object findOperationByName(Object trans,String opname) {
		return impl.findOperationByName(trans,opname);
	}
	public Collection getAllSubStates(Object compState) {
		return impl.getAllSubStates(compState);
	}
	public void removeSubvertex(Object handle,Object subvertex) {
		impl.removeSubvertex(handle,subvertex);
	}
	public void addSubvertex(Object handle,Object subvertex) {
		impl.addSubvertex(handle,subvertex);
	}
	public void setBound(Object handle,int bound) {
		impl.setBound(handle,bound);
	}
	public void setConcurrent(Object handle,boolean concurrent) {
		impl.setConcurrent(handle,concurrent);
	}
	public void setContainer(Object handle,Object compositeState) {
		impl.setContainer(handle,compositeState);
	}
	public void setDoActivity(Object handle,Object value) {
		impl.setDoActivity(handle,value);
	}
	public void setEffect(Object handle,Object value) {
		impl.setEffect(handle,value);
	}
	public void setEntry(Object handle,Object value) {
		impl.setEntry(handle,value);
	}
	public void setExit(Object handle,Object value) {
		impl.setExit(handle,value);
	}
	public void setExpression(Object handle,Object value) {
		impl.setExpression(handle,value);
	}
	public void setGuard(Object handle,Object guard) {
		impl.setGuard(handle,guard);
	}
	public void setInternalTransitions(Object handle,Collection intTrans) {
		impl.setInternalTransitions(handle,intTrans);
	}
	public void setSource(Object handle,Object state) {
		impl.setSource(handle,state);
	}
	public void setState(Object handle,Object element) {
		impl.setState(handle,element);
	}
	public void setStateMachine(Object handle,Object stm) {
		impl.setStateMachine(handle,stm);
	}
	public void setSubvertices(Object handle,Collection subvertices) {
		impl.setSubvertices(handle,subvertices);
	}
	public void setTrigger(Object handle,Object event) {
		impl.setTrigger(handle,event);
	}
	public void setWhen(Object handle,Object value) {
		impl.setWhen(handle,value);
	}
	public void setChangeExpression(Object handle,Object value) {
		impl.setChangeExpression(handle,value);
	}
	public String getPath(Object o) {
		return impl.getPath(o);
	}
	public Object getStatebyName(String path,Object container) {
		return impl.getStatebyName(path,container);
	}
	public void setReferenceState(Object o,String referenced) {
		impl.setReferenceState(o,referenced);
	}
	public Object findNamespaceForEvent(Object trans,Object model) {
		return impl.findNamespaceForEvent(trans,model);
	}
	public void addDeferrableEvent(Object state,Object deferrableEvent) {
		impl.addDeferrableEvent(state,deferrableEvent);
	}
	public void removeDeferrableEvent(Object state,Object deferrableEvent) {
		impl.removeDeferrableEvent(state,deferrableEvent);
	}
	public void setContext(Object statemachine,Object modelElement) {
		impl.setContext(statemachine,modelElement);
	}
}



