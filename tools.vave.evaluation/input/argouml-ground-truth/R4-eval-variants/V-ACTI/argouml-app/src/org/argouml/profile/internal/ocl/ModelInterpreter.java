package org.argouml.profile.internal.ocl;

import java.util.Map;


public interface ModelInterpreter {
	Object invokeFeature(Map<String,Object>vt,Object subject,String feature,String type,Object[]parameters);
	Object getBuiltInSymbol(String sym);
}



