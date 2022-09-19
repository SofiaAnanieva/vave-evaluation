package org.argouml.model.euml;

import java.util.ArrayList;
import java.util.List;


public abstract class RunnableClass implements Runnable {
	private List<Object>params = new ArrayList<Object>();
	public List<Object>getParams() {
		return params;
	}
}



