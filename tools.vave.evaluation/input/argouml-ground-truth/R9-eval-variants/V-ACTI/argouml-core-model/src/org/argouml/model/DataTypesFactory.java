package org.argouml.model;

import java.util.List;


public interface DataTypesFactory extends Factory {
	Object createActionExpression(String language,String body);
	Object createArgListsExpression(String language,String body);
	Object createBooleanExpression(String language,String body);
	Object createExpression(String language,String body);
	Object createIterationExpression(String language,String body);
	Object createMappingExpression(String language,String body);
	Object createObjectSetExpression(String language,String body);
	Object createProcedureExpression(String language,String body);
	Object createTimeExpression(String language,String body);
	Object createTypeExpression(String language,String body);
	Object createMultiplicity(int lower,int upper);
	Object createMultiplicity(List range);
	Object createMultiplicity(String str);
	public Object createMultiplicityRange(String str);
	public Object createMultiplicityRange(int lower,int upper);
}



