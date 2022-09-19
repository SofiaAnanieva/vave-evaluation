package org.argouml.model.euml;

import java.util.Collection;
import org.argouml.model.StateMachinesHelper;
import org.eclipse.uml2.uml.State;
import org.argouml.model.euml.EUMLModelImplementation;


class StateMachinesHelperEUMLImpl implements StateMachinesHelper {
	private EUMLModelImplementation modelImpl;
	public StateMachinesHelperEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public void addDeferrableEvent(Object state,Object deferrableEvent) {
		throw new NotYetImplementedException();
	}
	public void addSubvertex(Object handle,Object subvertex) {
		throw new NotYetImplementedException();
	}
	public Object findNamespaceForEvent(Object trans,Object model) {
		throw new NotYetImplementedException();
	}
	public Object findOperationByName(Object trans,String opname) {
		throw new NotYetImplementedException();
	}
	public Collection getAllPossibleStatemachines(Object model,Object oSubmachineState) {
		throw new NotYetImplementedException();
	}
	public Collection getAllPossibleSubvertices(Object oState) {
		throw new NotYetImplementedException();
	}
	public Collection getAllSubStates(Object compState) {
		throw new NotYetImplementedException();
	}
	public Object getDestination(Object trans) {
		throw new NotYetImplementedException();
	}
	public Collection getOutgoingStates(Object ostatevertex) {
		throw new NotYetImplementedException();
	}
	public String getPath(Object o) {
		throw new NotYetImplementedException();
	}
	public Object getSource(Object trans) {
		throw new NotYetImplementedException();
	}
	public Object getStateMachine(Object handle) {
		throw new NotYetImplementedException();
	}
	public Object getStatebyName(String path,Object container) {
		throw new NotYetImplementedException();
	}
	public Object getTop(Object sm) {
		throw new NotYetImplementedException();
	}
	public boolean isAddingStatemachineAllowed(Object context) {
		throw new NotYetImplementedException();
	}
	public boolean isTopState(Object o) {
		return o instanceof State&&((State) o).getOwner() == null;
	}
	public void removeDeferrableEvent(Object state,Object deferrableEvent) {
		throw new NotYetImplementedException();
	}
	public void removeSubvertex(Object handle,Object subvertex) {
		throw new NotYetImplementedException();
	}
	public void setBound(Object handle,int bound) {
		throw new NotYetImplementedException();
	}
	public void setChangeExpression(Object handle,Object value) {
		throw new NotYetImplementedException();
	}
	public void setConcurrent(Object handle,boolean concurrent) {
		throw new NotYetImplementedException();
	}
	public void setContainer(Object handle,Object compositeState) {
		throw new NotYetImplementedException();
	}
	public void setContext(Object statemachine,Object modelElement) {
		throw new NotYetImplementedException();
	}
	public void setDoActivity(Object handle,Object value) {
		throw new NotYetImplementedException();
	}
	public void setEffect(Object handle,Object value) {
		throw new NotYetImplementedException();
	}
	public void setEntry(Object handle,Object value) {
		throw new NotYetImplementedException();
	}
	public void setEventAsTrigger(Object transition,Object event) {
		throw new NotYetImplementedException();
	}
	public void setExit(Object handle,Object value) {
		throw new NotYetImplementedException();
	}
	public void setExpression(Object handle,Object value) {
		throw new NotYetImplementedException();
	}
	public void setGuard(Object handle,Object guard) {
		throw new NotYetImplementedException();
	}
	public void setInternalTransitions(Object handle,Collection intTrans) {
		throw new NotYetImplementedException();
	}
	public void setReferenceState(Object o,String referenced) {
		throw new NotYetImplementedException();
	}
	public void setSource(Object handle,Object state) {
		throw new NotYetImplementedException();
	}
	public void setState(Object handle,Object element) {
		throw new NotYetImplementedException();
	}
	public void setStateMachine(Object handle,Object stm) {
		throw new NotYetImplementedException();
	}
	public void setStatemachineAsSubmachine(Object oSubmachineState,Object oStatemachine) {
		throw new NotYetImplementedException();
	}
	public void setSubvertices(Object handle,Collection subvertices) {
		throw new NotYetImplementedException();
	}
	public void setTrigger(Object handle,Object event) {
		throw new NotYetImplementedException();
	}
	public void setWhen(Object handle,Object value) {
		throw new NotYetImplementedException();
	}
}



