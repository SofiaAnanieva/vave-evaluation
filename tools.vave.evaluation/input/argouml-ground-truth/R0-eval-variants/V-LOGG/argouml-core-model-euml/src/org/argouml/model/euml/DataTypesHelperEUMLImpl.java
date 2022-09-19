package org.argouml.model.euml;

import org.argouml.model.DataTypesHelper;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.argouml.model.euml.EUMLModelImplementation;


class DataTypesHelperEUMLImpl implements DataTypesHelper {
	private EUMLModelImplementation modelImpl;
	public DataTypesHelperEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public boolean equalsCHOICEKind(Object kind) {
		return false;
	}
	public boolean equalsDeepHistoryKind(Object kind) {
		return false;
	}
	public boolean equalsFORKKind(Object kind) {
		return false;
	}
	public boolean equalsINITIALKind(Object kind) {
		return false;
	}
	public boolean equalsJOINKind(Object kind) {
		return false;
	}
	public boolean equalsJUNCTIONKind(Object kind) {
		return false;
	}
	public boolean equalsShallowHistoryKind(Object kind) {
		return false;
	}
	public String getBody(Object handle) {
		return null;
	}
	public String getLanguage(Object handle) {
		return null;
	}
	public String multiplicityToString(Object multiplicity) {
		if (!(multiplicity instanceof MultiplicityElement)) {
			throw new IllegalArgumentException("multiplicity must be instance of MultiplicityElement");
		}
		MultiplicityElement mult = (MultiplicityElement) multiplicity;
		if (mult.getLower() == mult.getUpper()) {
			return DataTypesFactoryEUMLImpl.boundToString(mult.getLower());
		}else {
			return DataTypesFactoryEUMLImpl.boundToString(mult.getLower()) + ".." + DataTypesFactoryEUMLImpl.boundToString(mult.getUpper());
		}
	}
	public Object setBody(Object handle,String body) {
		return null;
	}
	public Object setLanguage(Object handle,String language) {
		return null;
	}
}



