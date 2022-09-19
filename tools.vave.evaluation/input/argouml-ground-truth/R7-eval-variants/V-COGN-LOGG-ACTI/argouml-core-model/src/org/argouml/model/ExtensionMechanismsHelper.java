package org.argouml.model;

import java.util.Collection;


public interface ExtensionMechanismsHelper {
	Collection getStereotypes(Object ns);
	Object getStereotype(Object ns,Object stereo);
	Object getStereotype(Collection models,Object stereo);
	@Deprecated String getMetaModelName(Object m);
	Collection getAllPossibleStereotypes(Collection models,Object modelElement);
	boolean isValidStereotype(Object theModelElement,Object theStereotype);
	Collection getStereotypes(Collection models);
	void addCopyStereotype(Object modelElement,Object stereotype);
	boolean isStereotype(Object object,String name,String base);
	boolean isStereotypeInh(Object object,String name,String base);
	void addExtendedElement(Object handle,Object extendedElement);
	void addBaseClass(Object handle,Object baseClass);
	void removeBaseClass(Object handle,Object baseClass);
	void setIcon(Object handle,Object icon);
	void setTagType(Object handle,String tagType);
	void setType(Object handle,Object type);
	@Deprecated void setValueOfTag(Object handle,String value);
	void setDataValues(Object handle,String[]values);
	void addTaggedValue(Object handle,Object taggedValue);
	void removeTaggedValue(Object handle,Object taggedValue);
	void setTaggedValue(Object handle,Collection taggedValues);
	boolean hasStereotype(Object element,String name);
}



