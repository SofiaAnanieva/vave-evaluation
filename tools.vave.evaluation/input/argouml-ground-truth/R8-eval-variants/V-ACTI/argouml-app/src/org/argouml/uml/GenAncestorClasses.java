package org.argouml.uml;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import org.argouml.model.Model;
import org.tigris.gef.util.ChildGenerator;


public class GenAncestorClasses implements ChildGenerator {
	public Enumeration gen(Object cls) {
		Set res = new HashSet();
		if (Model.getFacade().isAGeneralizableElement(cls)) {
			accumulateAncestors(cls,res);
		}
		return Collections.enumeration(res);
	}
	public void accumulateAncestors(Object cls,Collection accum) {
		Collection gens = Model.getFacade().getGeneralizations(cls);
		if (gens == null) {
			return;
		}
		for (Object g:gens) {
			Object ge = Model.getFacade().getGeneral(g);
			if (!accum.contains(ge)) {
				accum.add(ge);
				accumulateAncestors(cls,accum);
			}
		}
	}
}



