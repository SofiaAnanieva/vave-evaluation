package org.argouml.uml.diagram;

import java.util.Collection;


public interface Relocatable {
	boolean isRelocationAllowed(Object base);
	boolean relocate(Object base);
	@SuppressWarnings("unchecked")Collection getRelocationCandidates(Object root);
}



