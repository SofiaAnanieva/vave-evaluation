package org.argouml.model;

import java.util.Collection;


public interface ExtensionMechanismsFactory extends Factory {
	Object createTaggedValue();
	Object buildStereotype(Object theModelElementObject,Object theName,Object theNamespaceObject);
	Object buildStereotype(Object theModelElementObject,String theName,Object model,Collection models);
	Object buildStereotype(String text,Object ns);
	@Deprecated Object buildTaggedValue(String tag,String value);
	Object buildTaggedValue(Object type,String[]values);
	public void copyTaggedValues(Object source,Object target);
	Object copyStereotype(Object source,Object ns);
	Object buildTagDefinition(String name,Object stereotype,Object namespace);
	Object buildTagDefinition(String name,Object stereotype,Object namespace,String tagType);
	Object createTagDefinition();
	Object createStereotype();
	Object copyTagDefinition(Object aTd,Object aNs);
}



