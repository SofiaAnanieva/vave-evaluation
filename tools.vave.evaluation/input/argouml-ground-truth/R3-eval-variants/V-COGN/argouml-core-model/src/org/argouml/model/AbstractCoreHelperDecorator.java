package org.argouml.model;

import java.util.Collection;
import java.util.List;


@SuppressWarnings("deprecation")public abstract class AbstractCoreHelperDecorator implements CoreHelper {
	private CoreHelper impl;
	protected AbstractCoreHelperDecorator(CoreHelper component) {
		impl = component;
	}
	protected CoreHelper getComponent() {
		return impl;
	}
	public void clearStereotypes(Object modelElement) {
		impl.clearStereotypes(modelElement);
	}
	public boolean isSubType(Object type,Object subType) {
		return impl.isSubType(type,subType);
	}
	public Collection getAllSupertypes(Object cls1) {
		return impl.getAllSupertypes(cls1);
	}
	public Collection getSupertypes(Object generalizableElement) {
		return impl.getSupertypes(generalizableElement);
	}
	public Collection getAssociateEndsInh(Object classifier1) {
		return impl.getAssociateEndsInh(classifier1);
	}
	public void removeFeature(Object cls,Object feature) {
		impl.removeFeature(cls,feature);
	}
	public void removeLiteral(Object enu,Object literal) {
		impl.removeLiteral(enu,literal);
	}
	public void setOperations(Object classifier,List operations) {
		impl.setOperations(classifier,operations);
	}
	public void setAttributes(Object classifier,List attributes) {
		impl.setAttributes(classifier,attributes);
	}
	public Collection getAttributesInh(Object classifier) {
		return impl.getAttributesInh(classifier);
	}
	public Collection getOperationsInh(Object classifier) {
		return impl.getOperationsInh(classifier);
	}
	public Collection getParents(Object generalizableElement) {
		return impl.getParents(generalizableElement);
	}
	public List getReturnParameters(Object operation) {
		return impl.getReturnParameters(operation);
	}
	public Object getSpecification(Object object) {
		return impl.getSpecification(object);
	}
	public Collection getSubtypes(Object cls) {
		return impl.getSubtypes(cls);
	}
	public Collection getAllBehavioralFeatures(Object element) {
		return impl.getAllBehavioralFeatures(element);
	}
	public List getBehavioralFeatures(Object clazz) {
		return impl.getBehavioralFeatures(clazz);
	}
	public Collection getAllInterfaces(Object ns) {
		return impl.getAllInterfaces(ns);
	}
	public Collection getAllClasses(Object ns) {
		return impl.getAllClasses(ns);
	}
	public Collection getRealizedInterfaces(Object cls) {
		return impl.getRealizedInterfaces(cls);
	}
	public Collection getExtendedClassifiers(Object clazz) {
		return impl.getExtendedClassifiers(clazz);
	}
	public Object getGeneralization(Object child,Object parent) {
		return impl.getGeneralization(child,parent);
	}
	public String getBody(Object comment) {
		return impl.getBody(comment);
	}
	public Collection getFlows(Object source,Object target) {
		return impl.getFlows(source,target);
	}
	public Collection getExtendingElements(Object clazz) {
		return impl.getExtendingElements(clazz);
	}
	public Collection getExtendingClassifiers(Object clazz) {
		return impl.getExtendingClassifiers(clazz);
	}
	public Collection getAllComponents(Object ns) {
		return impl.getAllComponents(ns);
	}
	public Collection getAllDataTypes(Object ns) {
		return impl.getAllDataTypes(ns);
	}
	public Collection getAllNodes(Object ns) {
		return impl.getAllNodes(ns);
	}
	public Collection getAssociatedClassifiers(Object classifier) {
		return impl.getAssociatedClassifiers(classifier);
	}
	public Collection getAssociations(Object from,Object to) {
		return impl.getAssociations(from,to);
	}
	public Collection getAllClassifiers(Object namespace) {
		return impl.getAllClassifiers(namespace);
	}
	public Collection getAssociations(Object classifier) {
		return impl.getAssociations(classifier);
	}
	public Object getAssociationEnd(Object type,Object assoc) {
		return impl.getAssociationEnd(type,assoc);
	}
	public Collection getAllAttributes(Object clazz) {
		return impl.getAllAttributes(clazz);
	}
	public Collection getAllVisibleElements(Object ns) {
		return impl.getAllVisibleElements(ns);
	}
	public Object getSource(Object relationship) {
		return impl.getSource(relationship);
	}
	public Object getDestination(Object relationship) {
		return impl.getDestination(relationship);
	}
	public Collection getDependencies(Object supplier,Object client) {
		return impl.getDependencies(supplier,client);
	}
	public Object getPackageImport(Object supplier,Object client) {
		return impl.getPackageImport(supplier,client);
	}
	public Collection getPackageImports(Object client) {
		return impl.getPackageImports(client);
	}
	public Collection getRelationships(Object source,Object dest) {
		return impl.getRelationships(source,dest);
	}
	public boolean isValidNamespace(Object mObj,Object nsObj) {
		return impl.isValidNamespace(mObj,nsObj);
	}
	public Object getFirstSharedNamespace(Object ns1,Object ns2) {
		return impl.getFirstSharedNamespace(ns1,ns2);
	}
	public Collection getAllPossibleNamespaces(Object modelElement,Object model) {
		return impl.getAllPossibleNamespaces(modelElement,model);
	}
	public Collection getChildren(Object o) {
		return impl.getChildren(o);
	}
	public Collection getAllRealizedInterfaces(Object o) {
		return impl.getAllRealizedInterfaces(o);
	}
	public boolean hasCompositeEnd(Object association) {
		return impl.hasCompositeEnd(association);
	}
	public boolean equalsAggregationKind(Object associationEnd,String kindType) {
		return impl.equalsAggregationKind(associationEnd,kindType);
	}
	public void removeAnnotatedElement(Object handle,Object me) {
		impl.removeAnnotatedElement(handle,me);
	}
	public void removeClientDependency(Object handle,Object dep) {
		impl.removeClientDependency(handle,dep);
	}
	public void removeConstraint(Object handle,Object cons) {
		impl.removeConstraint(handle,cons);
	}
	public void removeOwnedElement(Object handle,Object value) {
		impl.removeOwnedElement(handle,value);
	}
	public void removeParameter(Object handle,Object parameter) {
		impl.removeParameter(handle,parameter);
	}
	public void removeQualifier(Object handle,Object parameter) {
		impl.removeQualifier(handle,parameter);
	}
	public void removeSourceFlow(Object handle,Object flow) {
		impl.removeSourceFlow(handle,flow);
	}
	public void removeSupplierDependency(Object supplier,Object dependency) {
		impl.removeSupplierDependency(supplier,dependency);
	}
	public void removeStereotype(Object modelElement,Object stereotype) {
		impl.removeStereotype(modelElement,stereotype);
	}
	public void removeTargetFlow(Object handle,Object flow) {
		impl.removeTargetFlow(handle,flow);
	}
	public void removeTemplateArgument(Object handle,Object argument) {
		impl.removeTemplateArgument(handle,argument);
	}
	public void removeTemplateParameter(Object handle,Object parameter) {
		impl.removeTemplateParameter(handle,parameter);
	}
	public void addAnnotatedElement(Object comment,Object annotatedElement) {
		impl.addAnnotatedElement(comment,annotatedElement);
	}
	public void addClient(Object handle,Object element) {
		impl.addClient(handle,element);
	}
	public void addClientDependency(Object handle,Object dependency) {
		impl.addClientDependency(handle,dependency);
	}
	public void addComment(Object element,Object comment) {
		impl.addComment(element,comment);
	}
	public void addConnection(Object handle,Object connection) {
		impl.addConnection(handle,connection);
	}
	public void addConnection(Object handle,int position,Object connection) {
		impl.addConnection(handle,position,connection);
	}
	public void addConstraint(Object handle,Object mc) {
		impl.addConstraint(handle,mc);
	}
	public void addDeploymentLocation(Object handle,Object node) {
		impl.addDeploymentLocation(handle,node);
	}
	public void addFeature(Object handle,int index,Object f) {
		impl.addFeature(handle,index,f);
	}
	public void addFeature(Object handle,Object f) {
		impl.addFeature(handle,f);
	}
	public void addLiteral(Object handle,int index,Object literal) {
		impl.addLiteral(handle,index,literal);
	}
	public void addLink(Object handle,Object link) {
		impl.addLink(handle,link);
	}
	public void addMethod(Object handle,Object m) {
		impl.addMethod(handle,m);
	}
	public void addOwnedElement(Object handle,Object me) {
		impl.addOwnedElement(handle,me);
	}
	public void addParameter(Object handle,int index,Object parameter) {
		impl.addParameter(handle,index,parameter);
	}
	public void addParameter(Object handle,Object parameter) {
		impl.addParameter(handle,parameter);
	}
	public void addQualifier(Object handle,int index,Object qualifier) {
		impl.addQualifier(handle,index,qualifier);
	}
	public void addRaisedSignal(Object handle,Object sig) {
		impl.addRaisedSignal(handle,sig);
	}
	public void addSourceFlow(Object handle,Object flow) {
		impl.addSourceFlow(handle,flow);
	}
	public void addAllStereotypes(Object modelElement,Collection stereotypes) {
		impl.addStereotype(modelElement,stereotypes);
	}
	public void addStereotype(Object modelElement,Object stereotype) {
		impl.addStereotype(modelElement,stereotype);
	}
	public void addSupplier(Object handle,Object element) {
		impl.addSupplier(handle,element);
	}
	public void addSupplierDependency(Object supplier,Object dependency) {
		impl.addSupplierDependency(supplier,dependency);
	}
	public void addTargetFlow(Object handle,Object flow) {
		impl.addTargetFlow(handle,flow);
	}
	public void addTemplateArgument(Object handle,int index,Object argument) {
		impl.addTemplateArgument(handle,index,argument);
	}
	public void addTemplateArgument(Object handle,Object argument) {
		impl.addTemplateArgument(handle,argument);
	}
	public void addTemplateParameter(Object handle,int index,Object parameter) {
		impl.addTemplateParameter(handle,index,parameter);
	}
	public void addTemplateParameter(Object handle,Object parameter) {
		impl.addTemplateParameter(handle,parameter);
	}
	public void setAnnotatedElements(Object handle,Collection elems) {
		impl.setAnnotatedElements(handle,elems);
	}
	public void setAssociation(Object handle,Object association) {
		impl.setAssociation(handle,association);
	}
	public void setRaisedSignals(Object handle,Collection raisedSignals) {
		impl.setRaisedSignals(handle,raisedSignals);
	}
	public void setBody(Object handle,Object expr) {
		impl.setBody(handle,expr);
	}
	public void setChild(Object handle,Object child) {
		impl.setChild(handle,child);
	}
	public void setConnections(Object handle,Collection elems) {
		impl.setConnections(handle,elems);
	}
	public void setDefaultElement(Object handle,Object element) {
		impl.setDefaultElement(handle,element);
	}
	public void setDefaultValue(Object handle,Object expr) {
		impl.setDefaultValue(handle,expr);
	}
	public void setDiscriminator(Object handle,String discriminator) {
		impl.setDiscriminator(handle,discriminator);
	}
	public void setFeature(Object elem,int i,Object feature) {
		impl.setFeature(elem,i,feature);
	}
	public void setFeatures(Object handle,Collection features) {
		impl.setFeatures(handle,features);
	}
	public void setContainer(Object handle,Object component) {
		impl.setContainer(handle,component);
	}
	public void setInitialValue(Object at,Object expr) {
		impl.setInitialValue(at,expr);
	}
	public void setModelElementContainer(Object handle,Object container) {
		impl.setModelElementContainer(handle,container);
	}
	public void setNamespace(Object handle,Object ns) {
		impl.setNamespace(handle,ns);
	}
	public void setOwner(Object handle,Object owner) {
		impl.setOwner(handle,owner);
	}
	public void setStatic(Object handle,boolean isStatic) {
		impl.setStatic(handle,isStatic);
	}
	public void setParameter(Object handle,Object parameter) {
		impl.setParameter(handle,parameter);
	}
	public void setParameters(Object handle,Collection parameters) {
		impl.setParameters(handle,parameters);
	}
	public void setParent(Object handle,Object parent) {
		impl.setParent(handle,parent);
	}
	public void setQualifiers(Object handle,List elems) {
		impl.setQualifiers(handle,elems);
	}
	public void setResident(Object handle,Object resident) {
		impl.setResident(handle,resident);
	}
	public void setResidents(Object handle,Collection residents) {
		impl.setResidents(handle,residents);
	}
	public void setSources(Object handle,Collection specifications) {
		impl.setSources(handle,specifications);
	}
	public void setSpecifications(Object handle,Collection specifications) {
		impl.setSpecifications(handle,specifications);
	}
	@Deprecated public void setTaggedValue(Object handle,String tag,String value) {
		impl.setTaggedValue(handle,tag,value);
	}
	public void setType(Object handle,Object type) {
		impl.setType(handle,type);
	}
	public void removeDeploymentLocation(Object handle,Object node) {
		impl.removeDeploymentLocation(handle,node);
	}
	public void setAbstract(Object handle,boolean flag) {
		impl.setAbstract(handle,flag);
	}
	public void setActive(Object handle,boolean active) {
		impl.setActive(handle,active);
	}
	public void setAggregation(Object handle,Object aggregationKind) {
		impl.setAggregation(handle,aggregationKind);
	}
	public void setLeaf(Object handle,boolean flag) {
		impl.setLeaf(handle,flag);
	}
	@Deprecated public void setChangeability(Object handle,Object ck) {
		impl.setChangeability(handle,ck);
	}
	public void setReadOnly(Object handle,boolean isReadOnly) {
		impl.setReadOnly(handle,isReadOnly);
	}
	public void setConcurrency(Object handle,Object concurrencyKind) {
		impl.setConcurrency(handle,concurrencyKind);
	}
	public void setKind(Object handle,Object kind) {
		impl.setKind(handle,kind);
	}
	public void setMultiplicity(Object handle,Object arg) {
		impl.setMultiplicity(handle,arg);
	}
	public void setName(Object handle,String name) {
		impl.setName(handle,name);
	}
	public void setBody(Object handle,String body) {
		impl.setBody(handle,body);
	}
	public void setNavigable(Object handle,boolean flag) {
		impl.setNavigable(handle,flag);
	}
	public void setOrdering(Object handle,Object ok) {
		impl.setOrdering(handle,ok);
	}
	public void setPowertype(Object handle,Object pt) {
		impl.setPowertype(handle,pt);
	}
	public void setQuery(Object handle,boolean flag) {
		impl.setQuery(handle,flag);
	}
	public void setRoot(Object handle,boolean flag) {
		impl.setRoot(handle,flag);
	}
	public void setSpecification(Object handle,boolean specification) {
		impl.setSpecification(handle,specification);
	}
	@Deprecated public void setTargetScope(Object handle,Object scopeKind) {
		impl.setTargetScope(handle,scopeKind);
	}
	public void setVisibility(Object handle,Object visibility) {
		impl.setVisibility(handle,visibility);
	}
	public void removeConnection(Object handle,Object connection) {
		impl.removeConnection(handle,connection);
	}
	public void addElementResidence(Object handle,Object residence) {
		impl.addElementResidence(handle,residence);
	}
	public void removeElementResidence(Object handle,Object residence) {
		impl.removeElementResidence(handle,residence);
	}
	public void setEnumerationLiterals(Object enumeration,List literals) {
		impl.setEnumerationLiterals(enumeration,literals);
	}
	public Collection getAllMetatypeNames() {
		return impl.getAllMetatypeNames();
	}
	public Collection getAllMetaDatatypeNames() {
		return impl.getAllMetaDatatypeNames();
	}
}



