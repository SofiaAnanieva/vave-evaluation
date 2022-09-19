package org.argouml.model.euml;

import java.util.Collection;
import java.util.List;
import org.argouml.model.CommonBehaviorHelper;
import org.argouml.model.euml.EUMLModelImplementation;


class CommonBehaviorHelperEUMLImpl implements CommonBehaviorHelper {
	private EUMLModelImplementation modelImpl;
	public CommonBehaviorHelperEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public void addAction(Object handle,Object action) {
	}
	public void addAction(Object handle,int position,Object action) {
	}
	public void addActualArgument(Object handle,Object argument) {
	}
	public void addActualArgument(Object handle,int position,Object argument) {
	}
	public void addClassifier(Object handle,Object classifier) {
	}
	public void addStimulus(Object handle,Object stimulus) {
	}
	public Object getActionOwner(Object handle) {
		return null;
	}
	public Object getDestination(Object link) {
		return null;
	}
	public Object getInstantiation(Object createaction) {
		return null;
	}
	public Object getSource(Object link) {
		return null;
	}
	public void removeAction(Object handle,Object action) {
	}
	public void removeActualArgument(Object handle,Object argument) {
	}
	public void removeClassifier(Object handle,Object classifier) {
	}
	public void removeContext(Object handle,Object context) {
	}
	public void removeReception(Object handle,Object reception) {
	}
	public void setActualArguments(Object action,List arguments) {
	}
	public void setAsynchronous(Object handle,boolean value) {
	}
	public void setClassifiers(Object handle,Collection classifiers) {
	}
	public void setCommunicationLink(Object handle,Object c) {
	}
	public void setComponentInstance(Object handle,Object c) {
	}
	public void setContexts(Object handle,Collection c) {
	}
	public void setDispatchAction(Object handle,Object value) {
	}
	public void setInstance(Object handle,Object inst) {
	}
	public void setInstantiation(Object createaction,Object instantiation) {
	}
	public void setNodeInstance(Object handle,Object nodeInstance) {
	}
	public void setOperation(Object handle,Object operation) {
	}
	public void setReceiver(Object handle,Object receiver) {
	}
	public void setReception(Object handle,Collection receptions) {
	}
	public void setRecurrence(Object handle,Object expr) {
	}
	public void setScript(Object handle,Object expr) {
	}
	public void setSender(Object handle,Object sender) {
	}
	public void setSignal(Object handle,Object signal) {
	}
	public void setSpecification(Object handle,String specification) {
	}
	public void setTarget(Object handle,Object element) {
	}
	public void setTransition(Object handle,Object trans) {
	}
	public void setValue(Object handle,Object value) {
	}
}



