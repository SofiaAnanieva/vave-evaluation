package org.argouml.model.euml;

import java.util.List;
import org.argouml.model.AbstractModelFactory;
import org.argouml.model.DataTypesFactory;
import org.argouml.model.NotImplementedException;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.UMLFactory;
import org.argouml.model.euml.EUMLModelImplementation;


class DataTypesFactoryEUMLImpl implements DataTypesFactory,AbstractModelFactory {
	private EUMLModelImplementation modelImpl;
	public DataTypesFactoryEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public Object createActionExpression(String language,String body) {
		return createExpression(language,body);
	}
	public Object createArgListsExpression(String language,String body) {
		return createExpression(language,body);
	}
	public Object createBooleanExpression(String language,String body) {
		return createExpression(language,body);
	}
	public OpaqueExpression createExpression(String language,String body) {
		OpaqueExpression expression = UMLFactory.eINSTANCE.createOpaqueExpression();
		expression.getLanguages().add(language);
		expression.getBodies().add(body);
		return expression;
	}
	public Object createIterationExpression(String language,String body) {
		return createExpression(language,body);
	}
	public Object createMappingExpression(String language,String body) {
		return createExpression(language,body);
	}
	public Object createMultiplicity(int lower,int upper) {
		throw new NotImplementedException();
	}
	public Object createMultiplicity(List range) {
		throw new NotImplementedException();
	}
	public Object createMultiplicity(String str) {
		return str;
	}
	public Object createMultiplicityRange(String str) {
		throw new NotImplementedException();
	}
	public Object createMultiplicityRange(int lower,int upper) {
		throw new NotImplementedException();
	}
	public Object createObjectSetExpression(String language,String body) {
		return createExpression(language,body);
	}
	public Object createProcedureExpression(String language,String body) {
		return createExpression(language,body);
	}
	public Object createTimeExpression(String language,String body) {
		return createExpression(language,body);
	}
	public Object createTypeExpression(String language,String body) {
		return createExpression(language,body);
	}
	static String boundToString(int i) {
		if (i == -1) {
			return"*";
		}else {
			return Integer.toString(i);
		}
	}
	private static int stringToBound(String b) {
		try {
			if (b.equals("n")||b.equals("*")) {
				return-1;
			}else {
				return Integer.parseInt(b);
			}
		}catch (Exception ex) {
			throw new IllegalArgumentException("illegal range bound : " + b);
		}
	}
}



