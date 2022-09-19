package org.argouml.model;

import java.util.Collection;


public abstract class AbstractActivityGraphsHelperDecorator implements ActivityGraphsHelper {
	private ActivityGraphsHelper impl;
	protected AbstractActivityGraphsHelperDecorator(ActivityGraphsHelper component) {
		impl = component;
	}
	protected ActivityGraphsHelper getComponent() {
		return impl;
	}
	public Object findClassifierByName(Object ofs,String s) {
		return impl.findClassifierByName(ofs,s);
	}
	public Object findStateByName(Object c,String s) {
		return impl.findStateByName(c,s);
	}
	public boolean isAddingActivityGraphAllowed(Object context) {
		return impl.isAddingActivityGraphAllowed(context);
	}
	public void addInState(Object classifierInState,Object state) {
		impl.addInState(classifierInState,state);
	}
	public void setInStates(Object classifierInState,Collection newStates) {
		impl.setInStates(classifierInState,newStates);
	}
	public void setContents(Object partition,Collection newContents) {
		impl.setContents(partition,newContents);
	}
	public void addContent(Object partition,Object modelElement) {
		impl.addContent(partition,modelElement);
	}
	public void removeContent(Object partition,Object modelElement) {
		impl.removeContent(partition,modelElement);
	}
	public void setSynch(Object objectFlowState,boolean isSynch) {
		impl.setSynch(objectFlowState,isSynch);
	}
	public void addParameter(Object objectFlowState,Object parameter) {
		impl.addParameter(objectFlowState,parameter);
	}
	public void removeParameter(Object objectFlowState,Object parameter) {
		impl.removeParameter(objectFlowState,parameter);
	}
	public void setParameters(Object objectFlowState,Collection parameters) {
		impl.setParameters(objectFlowState,parameters);
	}
}



