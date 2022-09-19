package org.argouml.model.mdr;

import java.util.Collection;
import java.util.Iterator;
import org.argouml.model.CoreHelper;
import org.argouml.model.ExtensionMechanismsFactory;
import org.argouml.model.ExtensionMechanismsHelper;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.datatypes.Multiplicity;


class ExtensionMechanismsFactoryMDRImpl extends AbstractUmlModelFactoryMDR implements ExtensionMechanismsFactory {
	private MDRModelImplementation modelImpl;
	private ExtensionMechanismsHelper extensionHelper;
	ExtensionMechanismsFactoryMDRImpl(MDRModelImplementation implementation) {
			modelImpl = implementation;
			extensionHelper = implementation.getExtensionMechanismsHelper();
		}
	public TaggedValue createTaggedValue() {
		TaggedValue tv = modelImpl.getUmlPackage().getCore().getTaggedValue().createTaggedValue();
		super.initialize(tv);
		return tv;
	}
	TagDefinition getTagDefinition(String tagName) {
		if (tagName == null) {
			throw new IllegalArgumentException("Argument may not be null");
		}
		for (Iterator i = modelImpl.getUmlPackage().getCore().getTagDefinition().refAllOfClass().iterator();i.hasNext();) {
			TagDefinition td = (TagDefinition) i.next();
			if (tagName.equals(td.getName())) {
				return td;
			}
		}
		Object rootModel = modelImpl.getModelManagementFactory().getRootModel();
		TagDefinition td = buildTagDefinition(tagName,null,rootModel);
		super.initialize(td);
		return td;
	}
	public Stereotype buildStereotype(Object theModelElementObject,Object theName,Object theNamespaceObject) {
		if (theModelElementObject == null||theName == null||theNamespaceObject == null) {
			throw new IllegalArgumentException("one of the arguments is null: modelElement=" + theModelElementObject + " name=" + theName + " namespace=" + theNamespaceObject);
		}
		ModelElement me = (ModelElement) theModelElementObject;
		String text = (String) theName;
		Namespace ns = (Namespace) theNamespaceObject;
		Stereotype stereo = buildStereotype(text);
		stereo.getBaseClass().add(modelImpl.getMetaTypes().getName(me));
		Stereotype stereo2 = (Stereotype) extensionHelper.getStereotype(ns,stereo);
		if (stereo2 != null) {
			me.getStereotype().add(stereo2);
			modelImpl.getUmlFactory().delete(stereo);
			return stereo2;
		}
		stereo.setNamespace(ns);
		me.getStereotype().add(stereo);
		return stereo;
	}
	public Stereotype buildStereotype(Object theModelElementObject,String theName,Object model,Collection models) {
		ModelElement me = (ModelElement) theModelElementObject;
		Stereotype stereo = buildStereotype(theName);
		stereo.getBaseClass().add(modelImpl.getMetaTypes().getName(me));
		Stereotype stereo2 = (Stereotype) extensionHelper.getStereotype(models,stereo);
		if (stereo2 != null) {
			me.getStereotype().add(stereo2);
			modelImpl.getUmlFactory().delete(stereo);
			return stereo2;
		}
		stereo.setNamespace((org.omg.uml.modelmanagement.Model) model);
		if (me != null) {
			me.getStereotype().add(stereo);
		}
		return stereo;
	}
	private Stereotype buildStereotype(String text) {
		Stereotype stereotype = modelImpl.getUmlPackage().getCore().getStereotype().createStereotype();
		super.initialize(stereotype);
		stereotype.setName(text);
		return stereotype;
	}
	public Stereotype buildStereotype(String text,Object namespace) {
		if (!(namespace instanceof Namespace)) {
			throw new IllegalArgumentException("Namespace is wrong type - text:" + text + ",ns:" + namespace);
		}
		Namespace ns = (Namespace) namespace;
		org.omg.uml.
				UmlPackage umlPkg = ((org.omg.uml.UmlPackage) ns.refOutermostPackage());
		Stereotype stereotype = umlPkg.getCore().getStereotype().createStereotype();
		super.initialize(stereotype);
		stereotype.setName(text);
		stereotype.setNamespace(ns);
		return stereotype;
	}
	@Deprecated public TaggedValue buildTaggedValue(String tag,String value) {
		TaggedValue tv = buildTaggedValue(getTagDefinition(tag));
		tv.getDataValue().add(value);
		return tv;
	}
	private TaggedValue buildTaggedValue(TagDefinition type) {
		TaggedValue tv = createTaggedValue();
		tv.setType(type);
		return tv;
	}
	public TaggedValue buildTaggedValue(Object type,String[]values) {
		if (!(type instanceof TagDefinition)) {
			throw new IllegalArgumentException("TagDefinition required, received - " + type);
		}
		TaggedValue tv = buildTaggedValue((TagDefinition) type);
		for (String value:values) {
			tv.getDataValue().add(value);
		}
		return tv;
	}
	public void copyTaggedValues(Object source,Object target) {
		if (!(source instanceof ModelElement)||!(target instanceof ModelElement)) {
			throw new IllegalArgumentException();
		}
		Iterator it = ((ModelElement) source).getTaggedValue().iterator();
		Collection taggedValues = ((ModelElement) target).getTaggedValue();
		taggedValues.clear();
		while (it.hasNext()) {
			taggedValues.add(copyTaggedValue((TaggedValue) it.next()));
		}
	}
	private Object copyTaggedValue(TaggedValue source) {
		TaggedValue tv = createTaggedValue();
		tv.setType(source.getType());
		tv.getDataValue().addAll(source.getDataValue());
		tv.getReferenceValue().addAll(source.getReferenceValue());
		return tv;
	}
	void deleteStereotype(Object elem) {
		if (!(elem instanceof Stereotype)) {
			throw new IllegalArgumentException();
		}
		modelImpl.getUmlHelper().deleteCollection(((Stereotype) elem).getDefinedTag());
		modelImpl.getUmlHelper().deleteCollection(((Stereotype) elem).getStereotypeConstraint());
	}
	void deleteTaggedValue(Object elem) {
		if (!(elem instanceof TaggedValue)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteTagDefinition(Object elem) {
		if (!(elem instanceof TagDefinition)) {
			throw new IllegalArgumentException();
		}
		TagDefinition td = (TagDefinition) elem;
		modelImpl.getUmlHelper().deleteCollection(((org.omg.uml.UmlPackage) td.refOutermostPackage()).getCore().getATypeTypedValue().getTypedValue(td));
	}
	public Object copyStereotype(Object source,Object ns) {
		if (!(source instanceof Stereotype)) {
			throw new IllegalArgumentException("source");
		}
		if (!(ns instanceof Namespace)) {
			throw new IllegalArgumentException("namespace");
		}
		Stereotype st = buildStereotype(null,ns);
		doCopyStereotype((Stereotype) source,st);
		return st;
	}
	private void doCopyStereotype(Stereotype source,Stereotype target) {
		((CoreFactoryMDRImpl) modelImpl.getCoreFactory()).doCopyGeneralizableElement(source,target);
		target.getBaseClass().clear();
		target.getBaseClass().addAll(source.getBaseClass());
		target.setIcon(source.getIcon());
	}
	public TagDefinition buildTagDefinition(String name,Object owner,Object namespace) {
		return buildTagDefinition(name,owner,namespace,null);
	}
	public TagDefinition buildTagDefinition(String name,Object owner,Object ns,String tagType) {
		if (owner != null) {
			if (!(owner instanceof Stereotype)) {
				throw new IllegalArgumentException("owner: " + owner);
			}
			if (ns != null) {
				throw new IllegalArgumentException("only one of owner & namespace may be specified");
			}
		}else if (!(ns instanceof Namespace)) {
			throw new IllegalArgumentException("namespace: " + ns);
		}
		TagDefinition td = (TagDefinition) createTagDefinition();
		CoreHelper coreHelper = org.argouml.model.Model.getCoreHelper();
		if (owner != null) {
			coreHelper.setOwner(td,owner);
		}else {
			coreHelper.setNamespace(td,ns);
		}
		coreHelper.setName(td,name);
		coreHelper.setMultiplicity(td,org.argouml.model.Model.getDataTypesFactory().createMultiplicity(0,1));
		td.setTagType(tagType);
		return td;
	}
	public Object createTagDefinition() {
		TagDefinition td = modelImpl.getUmlPackage().getCore().getTagDefinition().createTagDefinition();
		super.initialize(td);
		return td;
	}
	public Object createStereotype() {
		Stereotype st = modelImpl.getUmlPackage().getCore().getStereotype().createStereotype();
		super.initialize(st);
		return st;
	}
	public Object copyTagDefinition(Object anElement,Object aNs) {
		if (!(anElement instanceof TagDefinition)) {
			throw new IllegalArgumentException("source: " + anElement);
		}
		if (!(aNs instanceof Namespace||aNs instanceof Stereotype)) {
			throw new IllegalArgumentException("namespace: " + aNs);
		}
		TagDefinition source = (TagDefinition) anElement;
		TagDefinition td = (TagDefinition) createTagDefinition();
		if (aNs instanceof Namespace) {
			td.setNamespace((Namespace) aNs);
		}else {
			td.setOwner((Stereotype) aNs);
		}
		doCopyTagDefinition(source,td);
		return td;
	}
	private void doCopyTagDefinition(TagDefinition source,TagDefinition target) {
		((CoreFactoryMDRImpl) modelImpl.getCoreFactory()).doCopyModelElement(source,target);
		target.setTagType(source.getTagType());
		String srcMult = org.argouml.model.Model.getFacade().toString(source.getMultiplicity());
		target.setMultiplicity((Multiplicity) org.argouml.model.Model.getDataTypesFactory().createMultiplicity(srcMult));
	}
}



