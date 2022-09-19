package org.argouml.model;


public interface PseudostateKind {
	Object getChoice();
	Object getDeepHistory();
	Object getFork();
	Object getInitial();
	Object getJoin();
	Object getJunction();
	Object getShallowHistory();
	public Object getEntryPoint();
	public Object getExitPoint();
	public Object getTerminate();
}



