package org.argouml.model.mdr;

import java.util.Collection;
import java.util.Iterator;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Feature;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.modelmanagement.UmlPackage;


final class CopyHelper implements org.argouml.model.CopyHelper {
	private MDRModelImplementation modelImpl;
	CopyHelper(MDRModelImplementation implementation) {
			modelImpl = implementation;
		}
	public Object copy(Object anelement,Object ans) {
		ModelElement element = (ModelElement) anelement;
		Namespace ns = (Namespace) ans;
		if (element instanceof UmlPackage) {
			return modelImpl.getModelManagementFactory().copyPackage(element,ns);
		}
		if (element instanceof UmlClass) {
			return modelImpl.getCoreFactory().copyClass(element,ns);
		}
		if (element instanceof DataType) {
			return modelImpl.getCoreFactory().copyDataType(element,ns);
		}
		if (element instanceof Interface) {
			return modelImpl.getCoreFactory().copyInterface(element,ns);
		}
		if (element instanceof Feature) {
			return modelImpl.getCoreFactory().copyFeature(element,ns);
		}
		if (element instanceof Stereotype) {
			return modelImpl.getExtensionMechanismsFactory().copyStereotype(element,ns);
		}
		if (element instanceof TagDefinition) {
			return modelImpl.getExtensionMechanismsFactory().copyTagDefinition(element,ns);
		}
		throw new IllegalArgumentException("anelement:" + anelement + ", ans: " + ans);
	}
	Object fullCopy(Object anelement,Object ans) {
		ModelElement copy = (ModelElement) copy(anelement,ans);
		if (anelement instanceof Namespace) {
			Collection children = ((Namespace) anelement).getOwnedElement();
			if (!children.isEmpty()) {
				Iterator it = children.iterator();
				while (it.hasNext()) {
					Object childToCopy = it.next();
					fullCopy(childToCopy,copy);
				}
			}
		}
		return copy;
	}
}



