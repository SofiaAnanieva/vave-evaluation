package org.argouml.model;


public interface ModelManagementFactory extends Factory {
	Object createModel();
	@Deprecated void setRootModel(Object rootModel);
	@Deprecated Object getRootModel();
	Object createElementImport();
	Object buildElementImport(Object pack,Object me);
	Object createPackage();
	Object buildPackage(String name);
	Object createSubsystem();
	Object copyPackage(Object source,Object ns);
}



