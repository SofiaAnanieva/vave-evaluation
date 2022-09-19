package org.argouml.model;

import java.util.List;


public interface CoreFactory extends Factory {
	public String REALIZE_STEREOTYPE = "realize";
	public Object createAbstraction();
	Object buildAbstraction(String name,Object supplier,Object client);
	public Object createArtifact();
	public Object createAssociation();
	public Object createAssociation(Object extent);
	public Object createAssociationClass();
	Object createAssociationEnd();
	Object createAttribute();
	Object createBinding();
	Object createClass();
	Object createComment();
	Object createComponent();
	Object createConstraint();
	Object createDataType();
	public Object createDependency();
	Object createElementResidence();
	public Object buildElementResidence(Object me,Object component);
	Object createEnumeration();
	Object createEnumerationLiteral();
	Object createFlow();
	Object createInterface();
	Object createMethod();
	Object createNode();
	Object createOperation();
	Object createParameter();
	@Deprecated Object createPermission();
	Object createPackageImport();
	public Object createPrimitiveType();
	public Object createTemplateArgument();
	Object createTemplateParameter();
	Object createUsage();
	@Deprecated Object buildAssociation(Object fromClassifier,Object aggregationKind1,Object toClassifier,Object aggregationKind2,Boolean unidirectional);
	Object buildAssociation(Object fromClassifier,Object aggregationKind1,Object toClassifier,Object aggregationKind2,boolean unidirectional);
	Object buildAssociation(Object classifier1,Object classifier2);
	Object buildAssociation(Object c1,boolean nav1,Object c2,boolean nav2,String name);
	Object buildAssociationClass(Object end1,Object end2);
	Object buildAssociationEnd(Object assoc,String name,Object type,Object multi,Object stereo,boolean navigable,Object order,Object aggregation,Object scope,Object changeable,Object visibility);
	Object buildAssociationEnd(Object type,Object assoc);
	Object buildAttribute(Object model,Object type);
	Object buildAttribute2(Object type);
	Object buildAttribute2(Object classifier,Object type);
	Object buildClass();
	Object buildClass(Object owner);
	Object buildClass(String name);
	Object buildClass(String name,Object owner);
	Object buildInterface();
	Object buildInterface(Object owner);
	Object buildInterface(String name);
	Object buildInterface(String name,Object owner);
	Object buildDataType(String name,Object owner);
	Object buildEnumeration(String name,Object owner);
	Object buildEnumerationLiteral(String name,Object enumeration);
	Object buildDependency(Object clientObj,Object supplierObj);
	Object buildPackageImport(Object client,Object supplier);
	Object buildPackageAccess(Object client,Object supplier);
	Object buildGeneralization(Object child,Object parent);
	Object buildMethod(String name);
	Object buildOperation(Object classifier,Object returnType);
	Object buildOperation2(Object cls,Object returnType,String name);
	Object buildParameter(Object o,Object type);
	Object buildRealization(Object client,Object supplier,Object namespace);
	Object buildTemplateArgument(Object element);
	Object buildUsage(Object client,Object supplier);
	Object buildComment(Object element,Object model);
	Object buildConstraint(Object constrElement);
	Object buildConstraint(String name,Object bexpr);
	Object buildBinding(Object client,Object supplier,List arguments);
	Object copyClass(Object source,Object ns);
	Object copyFeature(Object source,Object classifier);
	Object copyDataType(Object source,Object ns);
	Object copyInterface(Object source,Object ns);
	Object createGeneralization();
	Object createGeneralization(Object extent);
}



