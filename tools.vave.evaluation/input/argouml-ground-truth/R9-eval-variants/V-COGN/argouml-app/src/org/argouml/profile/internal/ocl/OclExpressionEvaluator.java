package org.argouml.profile.internal.ocl;

import java.util.Map;


public interface OclExpressionEvaluator {
	Object evaluate(Map<String,Object>vt,ModelInterpreter mi,String ocl)throws InvalidOclException;
}



