package org.argouml.model;

import java.util.Collection;


public interface ActivityGraphsHelper {
	Object findClassifierByName(Object ofs,String s);
	Object findStateByName(Object c,String s);
	boolean isAddingActivityGraphAllowed(Object context);
	void addInState(Object classifierInState,Object state);
	void setInStates(Object classifierInState,Collection newStates);
	void setContents(Object partition,Collection newContents);
	void addContent(Object partition,Object modeElement);
	void removeContent(Object partition,Object modeElement);
	void setSynch(Object objectFlowState,boolean isSynch);
	void addParameter(Object objectFlowState,Object parameter);
	void removeParameter(Object objectFlowState,Object parameter);
	void setParameters(Object objectFlowState,Collection parameters);
}



