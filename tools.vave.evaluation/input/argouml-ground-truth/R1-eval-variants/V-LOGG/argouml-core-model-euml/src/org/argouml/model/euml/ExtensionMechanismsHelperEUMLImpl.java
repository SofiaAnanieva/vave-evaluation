package org.argouml.model.euml;

import java.util.Collection;
import java.util.Collections;
import org.argouml.model.ExtensionMechanismsHelper;
import org.eclipse.uml2.uml.Element;
import org.argouml.model.euml.EUMLModelImplementation;


class ExtensionMechanismsHelperEUMLImpl implements ExtensionMechanismsHelper {
	private EUMLModelImplementation modelImpl;
	public ExtensionMechanismsHelperEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public void addBaseClass(Object handle,Object baseClass) {
	}
	public void addCopyStereotype(Object modelElement,Object stereotype) {
	}
	public void addExtendedElement(Object handle,Object extendedElement) {
	}
	public void addTaggedValue(Object handle,Object taggedValue) {
	}
	public Collection getAllPossibleStereotypes(Collection models,Object modelElement) {
		return Collections.emptySet();
	}
	public String getMetaModelName(Object m) {
		if (m instanceof Element) {
			return getMetaModelName(m.getClass());
		}
		throw new IllegalArgumentException("Not an Element");
	}
	protected String getMetaModelName(Class clazz) {
		return modelImpl.getMetaTypes().getName(clazz);
	}
	public Object getStereotype(Object ns,Object stereo) {
		return null;
	}
	public Object getStereotype(Collection models,Object stereo) {
		return null;
	}
	public Collection getStereotypes(Object ns) {
		return Collections.emptySet();
	}
	public Collection getStereotypes(Collection models) {
		return Collections.emptySet();
	}
	public boolean hasStereotype(Object handle,String name) {
		return false;
	}
	public boolean isStereotype(Object object,String name,String base) {
		return false;
	}
	public boolean isStereotypeInh(Object object,String name,String base) {
		return false;
	}
	public boolean isValidStereotype(Object theModelElement,Object theStereotype) {
		return false;
	}
	public void removeBaseClass(Object handle,Object baseClass) {
	}
	public void removeTaggedValue(Object handle,Object taggedValue) {
	}
	public void setIcon(Object handle,Object icon) {
	}
	public void setTaggedValue(Object handle,Collection taggedValues) {
	}
	public void setTagType(Object handle,String tagType) {
	}
	public void setType(Object handle,Object type) {
	}
	public void setValueOfTag(Object handle,String value) {
	}
	public void setDataValues(Object handle,String[]value) {
	}
}



