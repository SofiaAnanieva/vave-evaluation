package org.argouml.model;

import java.util.Collection;


public abstract class AbstractExtensionMechanismsHelperDecorator implements ExtensionMechanismsHelper {
	private ExtensionMechanismsHelper impl;
	protected AbstractExtensionMechanismsHelperDecorator(ExtensionMechanismsHelper component) {
		impl = component;
	}
	protected ExtensionMechanismsHelper getComponent() {
		return impl;
	}
	public Collection getStereotypes(Object ns) {
		return impl.getStereotypes(ns);
	}
	public Object getStereotype(Object ns,Object stereo) {
		return impl.getStereotype(ns,stereo);
	}
	public Object getStereotype(Collection models,Object stereo) {
		return impl.getStereotype(models,stereo);
	}
	public String getMetaModelName(Object m) {
		return impl.getMetaModelName(m);
	}
	public Collection getAllPossibleStereotypes(Collection models,Object modelElement) {
		return impl.getAllPossibleStereotypes(models,modelElement);
	}
	public boolean isValidStereotype(Object theModelElement,Object theStereotype) {
		return impl.isValidStereotype(theModelElement,theStereotype);
	}
	public Collection getStereotypes(Collection models) {
		return impl.getStereotypes(models);
	}
	public void addCopyStereotype(Object modelElement,Object stereotype) {
		impl.addCopyStereotype(modelElement,stereotype);
	}
	public boolean isStereotype(Object object,String name,String base) {
		return impl.isStereotype(object,name,base);
	}
	public boolean isStereotypeInh(Object object,String name,String base) {
		return impl.isStereotypeInh(object,name,base);
	}
	public void addExtendedElement(Object handle,Object extendedElement) {
		impl.addExtendedElement(handle,extendedElement);
	}
	public void addBaseClass(Object handle,Object baseClass) {
		impl.addBaseClass(handle,baseClass);
	}
	public void removeBaseClass(Object handle,Object baseClass) {
		impl.removeBaseClass(handle,baseClass);
	}
	public void setIcon(Object handle,Object icon) {
		impl.setIcon(handle,icon);
	}
	public void setTagType(Object handle,String tagType) {
		impl.setType(handle,tagType);
	}
	public void setType(Object handle,Object type) {
		impl.setType(handle,type);
	}
	@Deprecated public void setValueOfTag(Object handle,String value) {
		impl.setValueOfTag(handle,value);
	}
	public void setDataValues(Object handle,String[]values) {
		impl.setDataValues(handle,values);
	}
	public void addTaggedValue(Object handle,Object taggedValue) {
		impl.addTaggedValue(handle,taggedValue);
	}
	public void removeTaggedValue(Object handle,Object taggedValue) {
		impl.removeTaggedValue(handle,taggedValue);
	}
	public void setTaggedValue(Object handle,Collection taggedValues) {
		impl.setTaggedValue(handle,taggedValues);
	}
	public boolean hasStereotype(Object handle,String name) {
		return impl.hasStereotype(handle,name);
	}
}



