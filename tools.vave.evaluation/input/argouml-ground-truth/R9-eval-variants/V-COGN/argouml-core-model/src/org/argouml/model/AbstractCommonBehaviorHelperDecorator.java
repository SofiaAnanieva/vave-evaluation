package org.argouml.model;

import java.util.Collection;
import java.util.List;


public abstract class AbstractCommonBehaviorHelperDecorator implements CommonBehaviorHelper {
	private CommonBehaviorHelper impl;
	protected AbstractCommonBehaviorHelperDecorator(CommonBehaviorHelper component) {
		impl = component;
	}
	protected CommonBehaviorHelper getComponent() {
		return impl;
	}
	public Object getSource(Object link) {
		return impl.getSource(link);
	}
	public Object getDestination(Object link) {
		return impl.getDestination(link);
	}
	public void removeActualArgument(Object handle,Object argument) {
		impl.removeActualArgument(handle,argument);
	}
	public void setActualArguments(Object action,List arguments) {
		impl.setActualArguments(action,arguments);
	}
	public void removeClassifier(Object handle,Object classifier) {
		impl.removeClassifier(handle,classifier);
	}
	public void removeContext(Object handle,Object context) {
		impl.removeContext(handle,context);
	}
	public void removeReception(Object handle,Object reception) {
		impl.removeReception(handle,reception);
	}
	public void addActualArgument(Object handle,Object argument) {
		impl.addActualArgument(handle,argument);
	}
	public void addActualArgument(Object handle,int position,Object argument) {
		impl.addActualArgument(handle,position,argument);
	}
	public void addClassifier(Object handle,Object classifier) {
		impl.addClassifier(handle,classifier);
	}
	public void addStimulus(Object handle,Object stimulus) {
		impl.addStimulus(handle,stimulus);
	}
	public void setAsynchronous(Object handle,boolean value) {
		impl.setAsynchronous(handle,value);
	}
	public void setOperation(Object handle,Object operation) {
		impl.setOperation(handle,operation);
	}
	public void setClassifiers(Object handle,Collection classifiers) {
		impl.setClassifiers(handle,classifiers);
	}
	public void setCommunicationLink(Object handle,Object c) {
		impl.setCommunicationLink(handle,c);
	}
	public void setComponentInstance(Object handle,Object c) {
		impl.setComponentInstance(handle,c);
	}
	public void setContexts(Object handle,Collection c) {
		impl.setContexts(handle,c);
	}
	public void setDispatchAction(Object handle,Object value) {
		impl.setDispatchAction(handle,value);
	}
	public void setInstance(Object handle,Object inst) {
		impl.setInstance(handle,inst);
	}
	public void setNodeInstance(Object handle,Object nodeInstance) {
		impl.setNodeInstance(handle,nodeInstance);
	}
	public void setReceiver(Object handle,Object receiver) {
		impl.setReceiver(handle,receiver);
	}
	public void setReception(Object handle,Collection c) {
		impl.setReception(handle,c);
	}
	public void setRecurrence(Object handle,Object expr) {
		impl.setRecurrence(handle,expr);
	}
	public void setScript(Object handle,Object expr) {
		impl.setScript(handle,expr);
	}
	public void setSender(Object handle,Object sender) {
		impl.setSender(handle,sender);
	}
	public void setSignal(Object handle,Object signal) {
		impl.setSignal(handle,signal);
	}
	public void setSpecification(Object handle,String specification) {
		impl.setSpecification(handle,specification);
	}
	public void setTarget(Object handle,Object element) {
		impl.setTarget(handle,element);
	}
	public void setTransition(Object handle,Object trans) {
		impl.setTransition(handle,trans);
	}
	public void setValue(Object handle,Object value) {
		impl.setValue(handle,value);
	}
	public Object getInstantiation(Object createaction) {
		return impl.getInstantiation(createaction);
	}
	public void setInstantiation(Object createaction,Object instantiation) {
		impl.setInstantiation(createaction,instantiation);
	}
	public Object getActionOwner(Object handle) {
		return impl.getActionOwner(handle);
	}
	public void addAction(Object handle,Object action) {
		impl.addAction(handle,action);
	}
	public void addAction(Object handle,int position,Object action) {
		impl.addAction(handle,position,action);
	}
	public void removeAction(Object handle,Object action) {
		impl.removeAction(handle,action);
	}
}



