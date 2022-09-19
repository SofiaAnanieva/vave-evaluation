package org.argouml.model.mdr;

import org.apache.log4j.Logger;
import org.argouml.model.ModelManagementFactory;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.modelmanagement.ElementImport;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.ModelManagementPackage;
import org.omg.uml.modelmanagement.Subsystem;
import org.omg.uml.modelmanagement.UmlPackage;


final class ModelManagementFactoryMDRImpl extends AbstractUmlModelFactoryMDR implements ModelManagementFactory {
	private static final Logger LOG = Logger.getLogger(ModelManagementFactoryMDRImpl.class);
	private Object theRootModel;
	private MDRModelImplementation modelImpl;
	public ModelManagementFactoryMDRImpl(MDRModelImplementation mi) {
		modelImpl = mi;
	}
	public Model createModel() {
		ModelManagementPackage mmp = getModelManagementPackage();
		if (mmp == null) {
			modelImpl.createDefaultExtent();
		}
		Model myModel = getModelManagementPackage().getModel().createModel();
		super.initialize(myModel);
		return myModel;
	}
	private ModelManagementPackage getModelManagementPackage() {
		org.omg.uml.
				UmlPackage umlPackage = modelImpl.getUmlPackage();
		if (umlPackage == null) {
			return null;
		}
		return umlPackage.getModelManagement();
	}
	public void setRootModel(Object rootModel) {
		if (rootModel != null&&!(rootModel instanceof Model)) {
			throw new IllegalArgumentException("The rootModel supplied must be a Model. Got a " + rootModel.getClass().getName());
		}
		theRootModel = rootModel;
	}
	public Object getRootModel() {
		return theRootModel;
	}
	public ElementImport createElementImport() {
		ElementImport myElementImport = getModelManagementPackage().getElementImport().createElementImport();
		super.initialize(myElementImport);
		return myElementImport;
	}
	public ElementImport buildElementImport(Object pack,Object me) {
		if (pack instanceof UmlPackage&&me instanceof ModelElement) {
			ElementImport ei = createElementImport();
			ei.setImportedElement((ModelElement) me);
			ei.setUmlPackage((UmlPackage) pack);
			return ei;
		}
		throw new IllegalArgumentException("To build an ElementImport we need a " + "Package and a ModelElement.");
	}
	public UmlPackage createPackage() {
		UmlPackage myUmlPackage = getModelManagementPackage().getUmlPackage().createUmlPackage();
		super.initialize(myUmlPackage);
		return myUmlPackage;
	}
	public Object buildPackage(String name) {
		UmlPackage pkg = createPackage();
		pkg.setName(name);
		return pkg;
	}
	public Object createSubsystem() {
		Subsystem mySubsystem = getModelManagementPackage().getSubsystem().createSubsystem();
		super.initialize(mySubsystem);
		return mySubsystem;
	}
	public Object copyPackage(Object source,Object ns) {
		if (!(source instanceof UmlPackage)) {
			throw new IllegalArgumentException("source");
		}
		if (!(ns instanceof Namespace)) {
			throw new IllegalArgumentException("namespace");
		}
		UmlPackage p = createPackage();
		((Namespace) ns).getOwnedElement().add(p);
		doCopyPackage((UmlPackage) source,p);
		return p;
	}
	private void doCopyPackage(UmlPackage source,UmlPackage target) {
		((CoreFactoryMDRImpl) modelImpl.getCoreFactory()).doCopyNamespace(source,target);
	}
	void deleteElementImport(Object elem) {
		if (!(elem instanceof ElementImport)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteModel(Object elem) {
		if (!(elem instanceof Model)) {
			throw new IllegalArgumentException();
		}
	}
	void deletePackage(Object elem) {
		if (!(elem instanceof UmlPackage)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteSubsystem(Object elem) {
		if (!(elem instanceof Subsystem)) {
			throw new IllegalArgumentException();
		}
	}
}



