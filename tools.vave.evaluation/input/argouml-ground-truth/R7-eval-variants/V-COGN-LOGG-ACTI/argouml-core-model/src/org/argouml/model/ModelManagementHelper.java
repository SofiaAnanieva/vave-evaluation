package org.argouml.model;

import java.util.Collection;
import java.util.List;
import java.util.Vector;


public interface ModelManagementHelper {
	String FRIEND_STEREOTYPE = "friend";
	String IMPORT_STEREOTYPE = "import";
	String ACCESS_STEREOTYPE = "access";
	Collection getAllSubSystems(Object ns);
	Collection getAllNamespaces(Object ns);
	Collection getAllModelElementsOfKindWithModel(Object model,Object type);
	Collection getAllModelElementsOfKind(Object namespace,Object type);
	Collection getAllModelElementsOfKind(Object namespace,String kind);
	Collection getAllSurroundingNamespaces(Object ns);
	Collection getAllBehavioralFeatures(Object ns);
	Collection getAllPossibleImports(Object pack);
	Object getElement(List<String>path);
	Object getElement(List<String>path,Object theRootNamespace);
	List<String>getPathList(Object element);
	boolean isCyclicOwnership(Object parent,Object child);
	void removeImportedElement(Object handle,Object me);
	void setImportedElements(Object pack,Collection imports);
	void setAlias(Object handle,String alias);
	void setSpecification(Object handle,boolean isSpecification);
	Collection getContents(Object namespace);
	Collection getAllImportedElements(Object pack);
	Collection getAllContents(Object namespace);
	boolean isReadOnly(Object element);
}



