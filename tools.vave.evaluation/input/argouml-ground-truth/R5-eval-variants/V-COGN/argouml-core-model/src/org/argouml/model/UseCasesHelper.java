package org.argouml.model;

import java.util.Collection;


public interface UseCasesHelper {
	Collection getAllUseCases(Object ns);
	Collection getAllActors(Object ns);
	Collection getExtendedUseCases(Object ausecase);
	Object getExtends(Object abase,Object anextension);
	Collection getIncludedUseCases(Object ausecase);
	Object getIncludes(Object abase,Object aninclusion);
	Collection getSpecificationPath(Object ausecase);
	void setBase(Object extend,Object base);
	void removeExtend(Object elem,Object extend);
	void removeExtensionPoint(Object elem,Object ep);
	void removeInclude(Object usecase,Object include);
	void addExtend(Object elem,Object extend);
	void addExtensionPoint(Object handle,Object extensionPoint);
	void addExtensionPoint(Object handle,int position,Object extensionPoint);
	void addInclude(Object usecase,Object include);
	void setAddition(Object handle,Object useCase);
	void setCondition(Object handle,Object booleanExpression);
	void setExtension(Object handle,Object ext);
	void setExtensionPoints(Object handle,Collection extensionPoints);
	void setIncludes(Object handle,Collection includes);
	void setLocation(Object handle,String loc);
	void setUseCase(Object elem,Object usecase);
}



