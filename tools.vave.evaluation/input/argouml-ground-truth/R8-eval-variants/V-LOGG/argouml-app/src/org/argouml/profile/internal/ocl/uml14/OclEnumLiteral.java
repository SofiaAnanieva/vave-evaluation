package org.argouml.profile.internal.ocl.uml14;

import org.argouml.model.Model;


public class OclEnumLiteral {
	private String name;
	public OclEnumLiteral(String literalName) {
		this.name = literalName;
	}
	public boolean equals(Object obj) {
		if (obj instanceof OclEnumLiteral) {
			return name.equals(((OclEnumLiteral) obj).name);
		}else if (Model.getFacade().isAEnumerationLiteral(obj)) {
			return name.equals(Model.getFacade().getName(obj));
		}else {
			return false;
		}
	}
	public int hashCode() {
		return name.hashCode();
	}
}



