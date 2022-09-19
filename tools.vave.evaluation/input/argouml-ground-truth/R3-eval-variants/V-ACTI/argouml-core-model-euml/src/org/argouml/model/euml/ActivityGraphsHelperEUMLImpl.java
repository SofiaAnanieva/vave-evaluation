package org.argouml.model.euml;

import java.util.Collection;
import org.argouml.model.ActivityGraphsHelper;
import org.argouml.model.euml.EUMLModelImplementation;


class ActivityGraphsHelperEUMLImpl implements ActivityGraphsHelper {
	private EUMLModelImplementation modelImpl;
	public ActivityGraphsHelperEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public void addContent(Object partition,Object modeElement) {
	}
	public void addInState(Object classifierInState,Object state) {
	}
	public void addParameter(Object objectFlowState,Object parameter) {
	}
	public Object findClassifierByName(Object ofs,String s) {
		return null;
	}
	public Object findStateByName(Object c,String s) {
		return null;
	}
	public boolean isAddingActivityGraphAllowed(Object context) {
		return false;
	}
	public void removeContent(Object partition,Object modeElement) {
	}
	public void removeParameter(Object objectFlowState,Object parameter) {
	}
	public void setContents(Object partition,Collection newContents) {
	}
	public void setInStates(Object classifierInState,Collection newStates) {
	}
	public void setParameters(Object objectFlowState,Collection parameters) {
	}
	public void setSynch(Object objectFlowState,boolean isSynch) {
	}
}



