package org.argouml.profile.internal.ocl.uml14;

import java.util.Collection;
import java.util.Map;
import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.profile.internal.ocl.CompositeModelInterpreter;


public class Uml14ModelInterpreter extends CompositeModelInterpreter {
	private static final Logger LOG = Logger.getLogger(Uml14ModelInterpreter.class);
	public Uml14ModelInterpreter() {
		addModelInterpreter(new ModelAccessModelInterpreter());
		addModelInterpreter(new OclAPIModelInterpreter());
		addModelInterpreter(new CollectionsModelInterpreter());
	}
	private String toString(Object obj) {
		if (Model.getFacade().isAModelElement(obj)) {
			return Model.getFacade().getName(obj);
		}else if (obj instanceof Collection) {
			return colToString((Collection) obj);
		}else {
			return"" + obj;
		}
	}
	private String colToString(Collection collection) {
		String ret = "[";
		for (Object object:collection) {
			ret += toString(object) + ",";
		}
		return ret + "]";
	}
}



