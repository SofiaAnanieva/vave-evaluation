package org.argouml.model;

import java.util.Collection;


public abstract class AbstractUseCasesHelperDecorator implements UseCasesHelper {
	private UseCasesHelper impl;
	protected AbstractUseCasesHelperDecorator(UseCasesHelper component) {
		impl = component;
	}
	protected UseCasesHelper getComponent() {
		return impl;
	}
	public Collection getExtensionPoints(Object useCase) {
		return Model.getFacade().getExtensionPoints(useCase);
	}
	public Collection getAllUseCases(Object ns) {
		return impl.getAllUseCases(ns);
	}
	public Collection getAllActors(Object ns) {
		return impl.getAllActors(ns);
	}
	public Collection getExtendedUseCases(Object ausecase) {
		return impl.getExtendedUseCases(ausecase);
	}
	public Object getExtends(Object abase,Object anextension) {
		return impl.getExtends(abase,anextension);
	}
	public Collection getIncludedUseCases(Object ausecase) {
		return impl.getIncludedUseCases(ausecase);
	}
	public Object getIncludes(Object abase,Object aninclusion) {
		return impl.getIncludes(abase,aninclusion);
	}
	public Collection getSpecificationPath(Object ausecase) {
		return impl.getSpecificationPath(ausecase);
	}
	public void setBase(Object extend,Object base) {
		impl.setBase(extend,base);
	}
	public void removeExtend(Object elem,Object extend) {
		impl.removeExtend(elem,extend);
	}
	public void removeExtensionPoint(Object elem,Object ep) {
		impl.removeExtensionPoint(elem,ep);
	}
	public void removeInclude(Object usecase,Object include) {
		impl.removeInclude(usecase,include);
	}
	public void addExtend(Object elem,Object extend) {
		impl.addExtend(elem,extend);
	}
	public void addExtensionPoint(Object handle,Object extensionPoint) {
		impl.addExtensionPoint(handle,extensionPoint);
	}
	public void addExtensionPoint(Object handle,int position,Object extensionPoint) {
		impl.addExtensionPoint(handle,position,extensionPoint);
	}
	public void addInclude(Object usecase,Object include) {
		impl.addInclude(usecase,include);
	}
	public void setAddition(Object handle,Object useCase) {
		impl.setAddition(handle,useCase);
	}
	public void setCondition(Object handle,Object booleanExpression) {
		impl.setCondition(handle,booleanExpression);
	}
	public void setExtension(Object handle,Object ext) {
		impl.setExtension(handle,ext);
	}
	public void setExtensionPoints(Object handle,Collection extensionPoints) {
		impl.setExtensionPoints(handle,extensionPoints);
	}
	public void setIncludes(Object handle,Collection includes) {
		impl.setIncludes(handle,includes);
	}
	public void setLocation(Object handle,String loc) {
		impl.setLocation(handle,loc);
	}
	public void setUseCase(Object elem,Object usecase) {
		impl.setUseCase(elem,usecase);
	}
}



