package org.argouml.profile.internal.ocl.uml14;

import java.util.Collection;


public interface Bag<E>extends Collection<E> {
	int count(E element);
}



