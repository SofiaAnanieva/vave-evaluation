package org.argouml.model.euml;

import org.argouml.model.DataTypesHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.PseudostateKind;
import org.argouml.model.euml.EUMLModelImplementation;


class DataTypesHelperEUMLImpl implements DataTypesHelper {
	private EUMLModelImplementation modelImpl;
	public DataTypesHelperEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public boolean equalsCHOICEKind(Object kind) {
		return PseudostateKind.CHOICE_LITERAL.equals(kind);
	}
	public boolean equalsDeepHistoryKind(Object kind) {
		return PseudostateKind.DEEP_HISTORY_LITERAL.equals(kind);
	}
	public boolean equalsFORKKind(Object kind) {
		return PseudostateKind.FORK_LITERAL.equals(kind);
	}
	public boolean equalsINITIALKind(Object kind) {
		return PseudostateKind.INITIAL_LITERAL.equals(kind);
	}
	public boolean equalsJOINKind(Object kind) {
		return PseudostateKind.JOIN_LITERAL.equals(kind);
	}
	public boolean equalsJUNCTIONKind(Object kind) {
		return PseudostateKind.JUNCTION_LITERAL.equals(kind);
	}
	public boolean equalsShallowHistoryKind(Object kind) {
		return PseudostateKind.SHALLOW_HISTORY_LITERAL.equals(kind);
	}
	public String getBody(Object handle) {
		EList<String>bodies = ((OpaqueExpression) handle).getBodies();
		if (bodies.size() < 1) {
			return null;
		}
		return bodies.get(0);
	}
	public String getLanguage(Object handle) {
		EList<String>languages = ((OpaqueExpression) handle).getLanguages();
		if (languages.size() < 1) {
			return null;
		}
		return languages.get(0);
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
		EList<String>bodies = ((OpaqueExpression) handle).getBodies();
		if (bodies.size() > 1) {
			throw new IllegalStateException("Only one body/lang supported");
		}
		bodies.clear();
		bodies.add(body);
		return handle;
	}
	public Object setLanguage(Object handle,String language) {
		EList<String>langs = ((OpaqueExpression) handle).getLanguages();
		if (langs.size() > 1) {
			throw new IllegalStateException("Only one body/lang supported");
		}
		langs.clear();
		langs.add(language);
		return handle;
	}
}



