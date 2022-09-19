package org.argouml.model;

import java.util.Collection;
import java.util.List;


public interface CommonBehaviorHelper {
	Object getInstantiation(Object createaction);
	void setInstantiation(Object createaction,Object instantiation);
	Object getSource(Object link);
	Object getDestination(Object link);
	void removeActualArgument(Object handle,Object argument);
	void setActualArguments(Object action,List arguments);
	void removeClassifier(Object handle,Object classifier);
	void removeContext(Object handle,Object context);
	void removeReception(Object handle,Object reception);
	void addActualArgument(Object handle,Object argument);
	void addActualArgument(Object handle,int position,Object argument);
	void addClassifier(Object handle,Object classifier);
	void addStimulus(Object handle,Object stimulus);
	void setAsynchronous(Object handle,boolean value);
	void setOperation(Object handle,Object operation);
	void setClassifiers(Object handle,Collection classifiers);
	void setCommunicationLink(Object handle,Object c);
	void setComponentInstance(Object handle,Object c);
	void setContexts(Object handle,Collection c);
	void setDispatchAction(Object handle,Object value);
	void setInstance(Object handle,Object inst);
	void setNodeInstance(Object handle,Object nodeInstance);
	void setReceiver(Object handle,Object receiver);
	void setReception(Object handle,Collection receptions);
	void setRecurrence(Object handle,Object expr);
	void setScript(Object handle,Object expr);
	void setSender(Object handle,Object sender);
	void setSignal(Object handle,Object signal);
	void setSpecification(Object handle,String specification);
	void setTarget(Object handle,Object element);
	void setTransition(Object handle,Object trans);
	void setValue(Object handle,Object value);
	Object getActionOwner(Object handle);
	void addAction(Object handle,Object action);
	void addAction(Object handle,int position,Object action);
	void removeAction(Object handle,Object action);
}



