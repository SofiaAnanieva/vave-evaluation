package org.argouml.profile.internal.ocl.uml14;

import java.util.Map;
import org.argouml.model.Model;
import org.argouml.profile.internal.ocl.ModelInterpreter;


public class OclAPIModelInterpreter implements ModelInterpreter {
	public Object invokeFeature(Map<String,Object>vt,Object subject,String feature,String type,Object[]parameters) {
		if (type.equals(".")) {
			if (feature.toString().trim().equals("oclIsKindOf")||feature.toString().trim().equals("oclIsTypeOf")) {
				String typeName = ((OclType) parameters[0]).getName();
				if (typeName.equals("OclAny")) {
					return true;
				}else {
					return Model.getFacade().isA(typeName,subject);
				}
			}
			if (feature.toString().trim().equals("oclAsType")) {
				return subject;
			}
			if (subject instanceof OclType) {
				if (feature.toString().trim().equals("name")) {
					return((OclType) subject).getName();
				}
			}
			if (subject instanceof String) {
				if (feature.toString().trim().equals("size")) {
					return((String) subject).length();
				}
				if (feature.toString().trim().equals("concat")) {
					return((String) subject).concat((String) parameters[0]);
				}
				if (feature.toString().trim().equals("toLower")) {
					return((String) subject).toLowerCase();
				}
				if (feature.toString().trim().equals("toUpper")) {
					return((String) subject).toUpperCase();
				}
				if (feature.toString().trim().equals("substring")) {
					return((String) subject).substring((Integer) parameters[0],(Integer) parameters[1]);
				}
			}
		}
		return null;
	}
	public Object getBuiltInSymbol(String sym) {
		if (sym.equals("OclType")) {
			return new OclType("OclType");
		}else if (sym.equals("OclExpression")) {
			return new OclType("OclExpression");
		}
		if (sym.equals("OclAny")) {
			return new OclType("OclAny");
		}
		return null;
	}
}



