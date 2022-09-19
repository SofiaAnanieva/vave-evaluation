package org.argouml.model.euml;

import java.util.Collection;
import org.argouml.model.AbstractModelFactory;
import org.argouml.model.ExtensionMechanismsFactory;
import org.eclipse.uml2.uml.UMLFactory;
import org.argouml.model.euml.EUMLModelImplementation;


class ExtensionMechanismsFactoryEUMLImpl implements ExtensionMechanismsFactory,AbstractModelFactory {
	private EUMLModelImplementation modelImpl;
	public ExtensionMechanismsFactoryEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public Object buildStereotype(Object theModelElementObject,Object theName,Object theNamespaceObject) {
		return null;
	}
	public Object buildStereotype(Object theModelElementObject,String theName,Object model,Collection models) {
		return null;
	}
	public Object buildStereotype(String text,Object ns) {
		return null;
	}
	public Object buildTagDefinition(String name,Object stereotype,Object ns) {
		return null;
	}
	public Object buildTagDefinition(String name,Object stereotype,Object namespace,String tagType) {
		return null;
	}
	public Object buildTaggedValue(String tag,String value) {
		return null;
	}
	public Object buildTaggedValue(Object type,String[]value) {
		return null;
	}
	public Object copyStereotype(Object source,Object ns) {
		return null;
	}
	public Object copyTagDefinition(Object aTd,Object aNs) {
		return null;
	}
	public void copyTaggedValues(Object source,Object target) {
	}
	public Object createStereotype() {
		return UMLFactory.eINSTANCE.createStereotype();
	}
	public Object createTagDefinition() {
		return null;
	}
	public Object createTaggedValue() {
		return null;
	}
}



