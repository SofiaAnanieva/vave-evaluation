package org.argouml.profile;

import java.util.Iterator;


public interface FormatingStrategy {
	String formatElement(Object element,Object namespace);
	String formatCollection(Iterator iter,Object namespace);
}



