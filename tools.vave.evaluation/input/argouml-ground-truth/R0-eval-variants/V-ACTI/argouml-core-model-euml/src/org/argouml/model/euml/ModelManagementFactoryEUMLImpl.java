package org.argouml.model.euml;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.ModelManagementFactory;
import org.argouml.model.NotImplementedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.uml2.common.edit.command.ChangeCommand;
import org.eclipse.uml2.uml.ElementImport;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.UMLFactory;
import org.argouml.model.euml.UMLUtil;
import org.argouml.model.euml.EUMLModelImplementation;
import org.argouml.model.euml.ModelEventPumpEUMLImpl;


class ModelManagementFactoryEUMLImpl implements ModelManagementFactory,AbstractModelFactory {
	private EUMLModelImplementation modelImpl;
	private EditingDomain editingDomain;
	private org.eclipse.uml2.uml.Package theRootModel;
	public ModelManagementFactoryEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
		editingDomain = implementation.getEditingDomain();
	}
	public ElementImport buildElementImport(final Object pack,final Object me) {
		if (!(pack instanceof Namespace)) {
			throw new IllegalArgumentException("pack must be instance of Namespace");
		}
		if (!(me instanceof PackageableElement)) {
			throw new IllegalArgumentException("me must be instance of PackageableElement");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		ElementImport elementImport = createElementImport();
		elementImport.setImportingNamespace((Namespace) pack);
		elementImport.setImportedElement((PackageableElement) me);
		getParams().add(elementImport);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(editingDomain,run));
		return(ElementImport) run.getParams().get(0);
	}
	public org.eclipse.uml2.uml.Package buildPackage(String name) {
		org.eclipse.uml2.uml.
				Package pkg = (org.eclipse.uml2.uml.Package) createPackage();
		if (name != null) {
			pkg.setName(name);
		}
		return pkg;
	}
	public Object copyPackage(Object source,Object ns) {
		return null;
	}
	public ElementImport createElementImport() {
		return UMLFactory.eINSTANCE.createElementImport();
	}
	public Model createModel() {
		return UMLFactory.eINSTANCE.createModel();
	}
	public org.eclipse.uml2.uml.Package createPackage() {
		return UMLFactory.eINSTANCE.createPackage();
	}
	@Deprecated public Object createSubsystem() {
		throw new NotImplementedException();
	}
	public void setRootModel(Object rootModel) {
		if (rootModel != null&&!(rootModel instanceof org.eclipse.uml2.uml.Package)) {
			throw new IllegalArgumentException("The rootModel supplied must be a Package. Got a " + rootModel.getClass().getName());
		}
		if (theRootModel != null&&theRootModel.eResource() != null) {
			EcoreUtil.remove(theRootModel);
		}
		theRootModel = (org.eclipse.uml2.uml.Package) rootModel;
		if (rootModel != null) {
			Resource r = UMLUtil.getResource(modelImpl,UMLUtil.DEFAULT_URI,Boolean.FALSE);
			r.getContents().add(theRootModel);
		}
		modelImpl.getModelEventPump().setRootContainer(theRootModel);
	}
	public org.eclipse.uml2.uml.Package getRootModel() {
		return theRootModel;
	}
}



