package org.argouml.model.euml;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.argouml.model.CoreHelper;
import org.argouml.model.NotImplementedException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.BehavioralFeature;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Feature;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.RedefinableElement;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.VisibilityKind;
import org.argouml.model.euml.EUMLModelImplementation;


class CoreHelperEUMLImpl implements CoreHelper {
	private EUMLModelImplementation modelImpl;
	private EditingDomain editingDomain;
	public CoreHelperEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
		editingDomain = implementation.getEditingDomain();
	}
	public void addAllStereotypes(final Object modelElement,final Collection stereos) {
		if (!(modelElement instanceof Element)) {
			throw new IllegalArgumentException("modelElement must be instance of Element");
		}
		if (stereos == null) {
			throw new NullPointerException("stereos must be non-null");
		}
		for (Object o:stereos) {
			if (!(o instanceof Stereotype)) {
				throw new IllegalArgumentException("The stereotypes from stereo collection" + " must be instances of Stereotype");
			}
			if (!((Element) modelElement).isStereotypeApplicable((Stereotype) o)) {
				throw new UnsupportedOperationException("The stereotype " + o + " cannot be applied to " + modelElement);
			}
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		for (Object o:stereos) {
			((Element) modelElement).applyStereotype((Stereotype) o);
		}
	}
};
		ChangeCommand cmd;
		if (stereos.size() == 1) {
			cmd = new ChangeCommand(modelImpl,run,"Apply the stereotype # to the element #",stereos.iterator().next(),modelElement);
		}else {
			cmd = new ChangeCommand(modelImpl,run,"Apply # stereotypes to the element #",stereos.size(),modelElement);
		}
		editingDomain.getCommandStack().execute(cmd);
	}
	public void addAnnotatedElement(final Object comment,final Object annotatedElement) {
		if (!(annotatedElement instanceof Element)) {
			throw new IllegalArgumentException("annotatedElement must be instance of Element");
		}
		if (!(comment instanceof Comment)) {
			throw new IllegalArgumentException("comment must be instance of Comment");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((Comment) comment).getAnnotatedElements().add((Element) annotatedElement);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Add the comment # to the element #",comment,annotatedElement));
	}
	public void addClient(final Object dependency,final Object element) {
		if (!(dependency instanceof Dependency)) {
			throw new IllegalArgumentException("The dependency must be instance of Dependency");
		}
		if (!(element instanceof NamedElement)) {
			throw new IllegalArgumentException("The element must be instance of NamedElement");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((Dependency) dependency).getClients().add((NamedElement) element);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Add the client # to the dependency #",element,dependency));
	}
	public void addClientDependency(Object handle,Object dependency) {
		addClient(dependency,handle);
	}
	public void addComment(Object element,Object comment) {
		addAnnotatedElement(comment,element);
	}
	public void addConnection(Object handle,Object connection) {
		addConnection(handle,CommandParameter.NO_INDEX,connection);
	}
	public void addConnection(Object handle,int position,Object connection) {
		if (!(handle instanceof Association)) {
			throw new IllegalArgumentException("The handle must be instance of Association");
		}
		if (!(connection instanceof Property)) {
			throw new IllegalArgumentException("The connection must be instance of Property");
		}
		RunnableClass run = getRunnableClassForAddCommand((Association) handle,position,(Property) connection);
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Add the AssociationEnd (Property) # to the Association #",connection,handle));
	}
	public void addConstraint(final Object handle,final Object mc) {
		if (!(handle instanceof Element)) {
			throw new IllegalArgumentException("The handle must be instance of Element");
		}
		if (!(mc instanceof Constraint)) {
			throw new IllegalArgumentException("mc must be instance of Constraint");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((Constraint) mc).getConstrainedElements().add((Element) handle);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Add the constraint # to the element #",mc,handle));
	}
	public void addDeploymentLocation(Object handle,Object node) {
		throw new NotYetImplementedException();
	}
	public void addElementResidence(Object handle,Object residence) {
		throw new NotYetImplementedException();
	}
	private RunnableClass getRunnableClassForAddCommand(Element owner,Element element) {
		return getRunnableClassForAddCommand(owner,CommandParameter.NO_INDEX,element);
	}
	private RunnableClass getRunnableClassForAddCommand(Element owner,int index,Element element) {
		final Command cmd = AddCommand.create(editingDomain,owner,null,element,index);
		if (cmd == null||!cmd.canExecute()) {
			throw new UnsupportedOperationException("The element " + element + " cannot be added to the element " + owner);
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		cmd.execute();
	}
};
		return run;
	}
	private RunnableClass getRunnableClassForRemoveCommand(Element element) {
		final Command cmd = RemoveCommand.create(editingDomain,element);
		if (cmd == null||!cmd.canExecute()) {
			String s = "The element " + element;
			if (element.getOwner() != null) {
				s += ", owned by " + element.getOwner() + ", ";
			}
			s += " cannot be removed";
			throw new UnsupportedOperationException(s);
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		cmd.execute();
	}
};
		return run;
	}
	public void addFeature(Object handle,int index,Object f) {
		if (!(handle instanceof Classifier)) {
			throw new IllegalArgumentException("The handle must be instance of Classifier");
		}
		if (!(f instanceof Feature)) {
			throw new IllegalArgumentException("f must be instance of Feature");
		}
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,getRunnableClassForAddCommand((Classifier) handle,index,(Feature) f),"Add the feature # to the classifier #",f,handle));
	}
	public void addFeature(Object handle,Object f) {
		addFeature(handle,CommandParameter.NO_INDEX,f);
	}
	public void addLink(Object handle,Object link) {
		throw new NotYetImplementedException();
	}
	public void addLiteral(Object handle,int index,Object literal) {
		if (!(handle instanceof Enumeration)) {
			throw new IllegalArgumentException("The handle must be instance of Enumeration");
		}
		if (!(literal instanceof EnumerationLiteral)) {
			throw new IllegalArgumentException("literal must be instance of EnumerationLiteral");
		}
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,getRunnableClassForAddCommand((Enumeration) handle,index,(EnumerationLiteral) literal),"Add the EnumerationLiteral # to the Enumeration #",literal,handle));
	}
	public void addMethod(final Object handle,final Object method) {
		if (!(handle instanceof BehavioralFeature)) {
			throw new IllegalArgumentException("The handle must be instance of BehavioralFeature");
		}
		if (!(method instanceof Behavior)) {
			throw new IllegalArgumentException("method must be instance of Behavior");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((BehavioralFeature) handle).getMethods().add((Behavior) method);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Add the Behavior (method) # to the BehavioralFeature (operation) #",method,handle));
	}
	public void addOwnedElement(Object handle,Object me,String msg,Object...objects) {
		if (!(handle instanceof Namespace)) {
			throw new IllegalArgumentException("The handle must be instance of Namespace");
		}
		if (!(me instanceof Element)) {
			throw new IllegalArgumentException("\'me\' must be instance of Element");
		}
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,getRunnableClassForAddCommand((Namespace) handle,(Element) me),msg,objects));
	}
	public void addOwnedElement(Object handle,Object me) {
		addOwnedElement(handle,me,"Add the owned element # to the owner #",me,handle);
	}
	public void addParameter(Object handle,int index,Object parameter) {
		if (!(handle instanceof BehavioralFeature)) {
			throw new IllegalArgumentException("handle must be instance of BehavioralFeature");
		}
		if (!(parameter instanceof Parameter)) {
			throw new IllegalArgumentException("parameter must be instance of Parameter");
		}
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,getRunnableClassForAddCommand((BehavioralFeature) handle,index,(Parameter) parameter),"Add the owned element # to the owner #",parameter,handle));
	}
	public void addParameter(Object handle,Object parameter) {
		addParameter(handle,CommandParameter.NO_INDEX,parameter);
	}
	public void addQualifier(Object handle,int position,Object qualifier) {
		if (!(handle instanceof Property)||!(qualifier instanceof Property)) {
			throw new IllegalArgumentException("handle and qualifier must be instances of Property");
		}
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,getRunnableClassForAddCommand((Property) handle,position,(Property) qualifier),"Add the qualifier # to the property #",qualifier,handle));
	}
	public void addRaisedSignal(Object handle,Object sig) {
		throw new NotYetImplementedException();
	}
	public void addSourceFlow(Object handle,Object flow) {
		throw new NotYetImplementedException();
	}
	public void addStereotype(Object modelElement,Object stereo) {
		addAllStereotypes(modelElement,Collections.singleton(stereo));
	}
	public void addSupplier(final Object dependency,final Object element) {
		if (!(dependency instanceof Dependency)) {
			throw new IllegalArgumentException("The dependency must be instance of Dependency");
		}
		if (!(element instanceof NamedElement)) {
			throw new IllegalArgumentException("The element must be instance of NamedElement");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((Dependency) dependency).getSuppliers().add((NamedElement) element);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Add the supplier # to the dependency #",element,dependency));
	}
	public void addSupplierDependency(Object supplier,Object dependency) {
		addSupplier(dependency,supplier);
	}
	public void addTargetFlow(Object handle,Object flow) {
		throw new NotYetImplementedException();
	}
	public void addTemplateArgument(Object handle,int index,Object argument) {
		throw new NotYetImplementedException();
	}
	public void addTemplateArgument(Object handle,Object argument) {
		throw new NotYetImplementedException();
	}
	public void addTemplateParameter(Object handle,int index,Object parameter) {
		throw new NotYetImplementedException();
	}
	public void addTemplateParameter(Object handle,Object parameter) {
		throw new NotYetImplementedException();
	}
	public void clearStereotypes(Object handle) {
		throw new NotYetImplementedException();
	}
	public boolean equalsAggregationKind(Object associationEnd,String kindType) {
		if (!(associationEnd instanceof Property)) {
			throw new IllegalArgumentException("associationEnd must be instance of Property");
		}
		return((Property) associationEnd).getAggregation().getLiteral().equals(kindType);
	}
	public Collection getAllAttributes(Object classifier) {
		if (!(classifier instanceof Classifier)) {
			throw new IllegalArgumentException("classifier must be instance of Classifier");
		}
		Collection result = new HashSet();
		result.addAll(((Classifier) classifier).getAttributes());
		for (Classifier c:((Classifier) classifier).allParents()) {
			result.addAll(c.getAttributes());
		}
		return result;
	}
	public Collection getAllBehavioralFeatures(Object element) {
		return modelImpl.getModelManagementHelper().getAllModelElementsOfKind(element,BehavioralFeature.class);
	}
	public Collection getAllClasses(Object ns) {
		return modelImpl.getModelManagementHelper().getAllModelElementsOfKind(ns,org.eclipse.uml2.uml.Class.class);
	}
	public Collection getAllClassifiers(Object namespace) {
		return modelImpl.getModelManagementHelper().getAllModelElementsOfKind(namespace,Classifier.class);
	}
	public Collection getAllComponents(Object ns) {
		return modelImpl.getModelManagementHelper().getAllModelElementsOfKind(ns,org.eclipse.uml2.uml.Component.class);
	}
	public Collection getAllDataTypes(Object ns) {
		return modelImpl.getModelManagementHelper().getAllModelElementsOfKind(ns,DataType.class);
	}
	public Collection getAllInterfaces(Object ns) {
		return modelImpl.getModelManagementHelper().getAllModelElementsOfKind(ns,Interface.class);
	}
	public Collection getAllMetaDatatypeNames() {
		return Collections.emptySet();
	}
	public Collection getAllMetatypeNames() {
		Collection result = new ArrayList();
		for (Field f:UMLPackage.Literals.class.getDeclaredFields()) {
			Object o;
			try {
				o = f.get(null);
			}catch (IllegalArgumentException e) {
				throw e;
			}catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			if (o instanceof EClass) {
				result.add(((EClass) o).getName());
			}
		}
		return result;
	}
	public Collection getAllNodes(Object ns) {
		return modelImpl.getModelManagementHelper().getAllModelElementsOfKind(ns,Node.class);
	}
	public Collection getAllPossibleNamespaces(Object modelElement,Object model) {
		if (!(model instanceof Element)||!(modelElement instanceof Element)) {
			throw new IllegalArgumentException("modelElement and model must be instances of Element");
		}
		Collection result = new ArrayList();
		if (isValidNamespace(modelElement,model)) {
			result.add((Namespace) model);
		}
		for (Object o:modelImpl.getModelManagementHelper().getAllModelElementsOfKind(model,Namespace.class)) {
			if (isValidNamespace(modelElement,o)) {
				result.add((Namespace) o);
			}
		}
		return result;
	}
	public Collection getAllRealizedInterfaces(Object element) {
		if (!(element instanceof org.eclipse.uml2.uml.Class)) {
			throw new IllegalArgumentException("element must be instance of UML2 Class");
		}
		return((org.eclipse.uml2.uml.Class) element).getAllImplementedInterfaces();
	}
	public Collection getAllSupertypes(Object classifier) {
		if (!(classifier instanceof Classifier)) {
			throw new IllegalArgumentException("classifier must be instance of Classifier");
		}
		return((Classifier) classifier).allParents();
	}
	public Collection getAllVisibleElements(Object ns) {
		if (!(ns instanceof Namespace)) {
			throw new IllegalArgumentException("ns must be instance of Namespace");
		}
		Collection result = new ArrayList();
		for (NamedElement e:((Namespace) ns).getOwnedMembers()) {
			if (e.getVisibility() == VisibilityKind.PUBLIC_LITERAL) {
				result.add(e);
			}
		}
		return result;
	}
	public Collection getAssociateEndsInh(Object classifier) {
		if (!(classifier instanceof Classifier)) {
			throw new IllegalArgumentException("classifier must be instance of Classifier");
		}
		Collection result = new ArrayList();
		result.addAll(modelImpl.getFacade().getAssociationEnds(classifier));
		for (Classifier o:((Classifier) classifier).allParents()) {
			result.addAll(modelImpl.getFacade().getAssociationEnds(o));
		}
		return result;
	}
	public Collection getAssociatedClassifiers(Object aclassifier) {
		if (!(aclassifier instanceof Classifier)) {
			throw new IllegalArgumentException("aclassifier must be instance of Classifier");
		}
		Collection result = new ArrayList();
		for (Association a:((Classifier) aclassifier).getAssociations()) {
			for (Type t:a.getEndTypes()) {
				if (t != aclassifier&&t instanceof Classifier) {
					result.add((Classifier) t);
				}
			}
		}
		return result;
	}
	public Object getAssociationEnd(Object type,Object assoc) {
		if (!(type instanceof Classifier)) {
			throw new IllegalArgumentException("type must be instance of Classifier");
		}
		if (!(assoc instanceof Association)) {
			throw new IllegalArgumentException("assoc must be instance of Association");
		}
		return((Association) assoc).getMemberEnd(null,(Classifier) type);
	}
	public Collection getAssociations(Object from,Object to) {
		if (from == null||to == null) {
			return Collections.EMPTY_LIST;
		}
		if (!(from instanceof Classifier)||!(to instanceof Classifier)) {
			throw new IllegalArgumentException("\'from\' and \'to\' must be instances of Classifier");
		}
		Collection result = new ArrayList();
		for (Association a:((Classifier) from).getAssociations()) {
			if (((Classifier) to).getAssociations().contains(a)) {
				result.add(a);
			}
		}
		return result;
	}
	public Collection getAssociations(Object classifier) {
		if (!(classifier instanceof Classifier)) {
			throw new IllegalArgumentException("\'classifier\' must be instance of Classifier");
		}
		return((Classifier) classifier).getAssociations();
	}
	public Collection getAttributesInh(Object classifier) {
		if (!(classifier instanceof Classifier)) {
			throw new IllegalArgumentException("\'classifier\' must be instance of Classifier");
		}
		return((Classifier) classifier).getAllAttributes();
	}
	public List<BehavioralFeature>getBehavioralFeatures(Object classifier) {
		if (!(classifier instanceof Classifier)) {
			throw new IllegalArgumentException("\'classifier\' must be instance of Classifier");
		}
		List<BehavioralFeature>result = new ArrayList<BehavioralFeature>();
		for (Feature feature:((Classifier) classifier).getFeatures()) {
			if (feature instanceof BehavioralFeature) {
				result.add((BehavioralFeature) feature);
			}
		}
		return result;
	}
	public String getBody(Object comment) {
		if (!(comment instanceof Comment)) {
			throw new IllegalArgumentException("\'comment\' must be instance of Comment");
		}
		return((Comment) comment).getBody();
	}
	public Collection<Classifier>getChildren(Object element) {
		if (!(element instanceof Classifier)) {
			throw new IllegalArgumentException("\'element\' must be instance of Classifier");
		}
		Collection<Classifier>results = new HashSet<Classifier>();
		LinkedList<Classifier>classifiers = new LinkedList<Classifier>();
		classifiers.add((Classifier) element);
		while (!classifiers.isEmpty()) {
			Classifier c = classifiers.removeFirst();
			if (results.contains(c)) {
				break;
			}
			results.add(c);
			for (DirectedRelationship d:c.getTargetDirectedRelationships(UMLPackage.Literals.GENERALIZATION)) {
				for (Element e:d.getSources()) {
					if (e instanceof Classifier&&!results.contains(e)) {
						classifiers.add((Classifier) e);
					}
				}
			}
		}
		results.remove(element);
		return results;
	}
	public Collection getDependencies(Object supplierObj,Object clientObj) {
		if (!(supplierObj instanceof NamedElement)||!(clientObj instanceof NamedElement)) {
			throw new IllegalArgumentException("supplierObj and clientObj must be instances of NamedElement");
		}
		Collection result = new ArrayList();
		for (Dependency d:((NamedElement) clientObj).getClientDependencies()) {
			if (d.getSuppliers().contains(supplierObj)) {
				result.add(d);
			}
		}
		return result;
	}
	public Collection getExtendedClassifiers(Object element) {
		if (!(element instanceof Classifier)) {
			throw new IllegalArgumentException("\'element\' must be instance of Classifier");
		}
		return((Classifier) element).getGenerals();
	}
	public Collection<Element>getExtendingClassifiers(Object classifier) {
		if (!(classifier instanceof Classifier)) {
			throw new IllegalArgumentException("\'classifier\' must be instance of Classifier");
		}
		Collection<Element>result = new HashSet<Element>();
		for (Element e:getExtendingElements(classifier)) {
			if (e instanceof Classifier) {
				result.add(e);
			}
		}
		return result;
	}
	public Collection<Element>getExtendingElements(Object element) {
		if (!(element instanceof Classifier)) {
			throw new IllegalArgumentException("\'element\' must be instance of Classifier");
		}
		Collection<Element>result = new HashSet<Element>();
		for (DirectedRelationship d:((Classifier) element).getTargetDirectedRelationships(UMLPackage.Literals.GENERALIZATION)) {
			for (Element e:d.getSources()) {
				result.add(e);
			}
		}
		return result;
	}
	public Object getFirstSharedNamespace(Object ns1,Object ns2) {
		if (!(ns1 instanceof Namespace)||!(ns2 instanceof Namespace)) {
			throw new IllegalArgumentException("ns1 and ns2 must be instances of Namespace");
		}
		Namespace result = null;
		List<Namespace>l1 = new ArrayList<Namespace>();
		l1.add((Namespace) ns1);
		l1.addAll(((Namespace) ns1).allNamespaces());
		List<Namespace>l2 = new ArrayList<Namespace>();
		l2.add((Namespace) ns2);
		l2.addAll(((Namespace) ns2).allNamespaces());
		int i = l1.size() - 1;
		int j = l2.size() - 1;
		while (i >= 0&&j >= 0) {
			if (l1.get(i) == l2.get(j)) {
				result = l1.get(i);
				i--;
				j--;
			}else {
				break;
			}
		}
		return result;
	}
	public Collection getFlows(Object source,Object target) {
		throw new NotYetImplementedException();
	}
	public Object getGeneralization(Object achild,Object aparent) {
		if (!(achild instanceof Classifier)||!(aparent instanceof Classifier)) {
			throw new IllegalArgumentException("\'achild\' and \'aparent\' must " + "be instances of Classifier");
		}
		return((Classifier) achild).getGeneralization((Classifier) aparent);
	}
	public Collection getOperationsInh(Object classifier) {
		if (!(classifier instanceof Classifier)) {
			throw new IllegalArgumentException("\'classifier\' must be instance of Classifier");
		}
		return((Classifier) classifier).getAllOperations();
	}
	public Collection getRealizedInterfaces(Object cls) {
		if (!(cls instanceof org.eclipse.uml2.uml.Class)) {
			throw new IllegalArgumentException("\'cls\' must be instance of UML2 Class");
		}
		return((org.eclipse.uml2.uml.Class) cls).getImplementedInterfaces();
	}
	public Collection<DirectedRelationship>getRelationships(Object source,Object dest) {
		if (!(source instanceof Element)||!(dest instanceof Element)) {
			throw new IllegalArgumentException("\'source\' and \'dest\' must be instances of Element");
		}
		Collection<DirectedRelationship>result = new ArrayList<DirectedRelationship>();
		for (DirectedRelationship d:((Element) source).getSourceDirectedRelationships()) {
			if (d.getTargets().contains(dest)) {
				result.add(d);
			}
		}
		for (DirectedRelationship d:((Element) source).getTargetDirectedRelationships()) {
			if (d.getSources().contains(dest)) {
				result.add(d);
			}
		}
		return result;
	}
	public List<Parameter>getReturnParameters(Object operation) {
		if (!(operation instanceof Operation)) {
			throw new IllegalArgumentException("\'operation\' must be instance of Operation");
		}
		List<Parameter>result = new ArrayList<Parameter>();
		for (Parameter p:((Operation) operation).getOwnedParameters()) {
			if (p.getDirection() == ParameterDirectionKind.RETURN_LITERAL) {
				result.add(p);
			}
		}
		return result;
	}
	public Object getSource(Object relationship) {
		if (!(relationship instanceof Relationship)&&!(relationship instanceof Property)) {
			throw new IllegalArgumentException("\'relationship\' must be instance of Relationship or Property");
		}
		if (relationship instanceof Association) {
			List<Property>conns = ((Association) relationship).getMemberEnds();
			if (conns.size() < 2) {
				return null;
			}
			Property prop = conns.get(1);
			return prop.getType();
		}
		if (relationship instanceof DirectedRelationship) {
			List<Element>sources = ((DirectedRelationship) relationship).getSources();
			if (sources.isEmpty()) {
				return null;
			}
			return sources.get(0);
		}
		if (relationship instanceof Property) {
			return((Property) relationship).getAssociation();
		}
		return null;
	}
	public Object getDestination(Object relationship) {
		if (!(relationship instanceof Relationship)&&!(relationship instanceof Property)) {
			throw new IllegalArgumentException("\'relationship\' must be instance of Relationship or Property");
		}
		if (relationship instanceof Association) {
			List<Property>conns = ((Association) relationship).getMemberEnds();
			if (conns.isEmpty()) {
				return null;
			}
			Property prop = conns.get(0);
			return prop.getType();
		}
		if (relationship instanceof DirectedRelationship) {
			List<Element>targets = ((DirectedRelationship) relationship).getTargets();
			if (targets.isEmpty()) {
				return null;
			}
			return targets.get(0);
		}
		if (relationship instanceof Property) {
			return((Property) relationship).getAssociation();
		}
		return null;
	}
	public Object getSpecification(Object object) {
		throw new NotYetImplementedException();
	}
	public Collection<Element>getSubtypes(Object cls) {
		if (!(cls instanceof Classifier)) {
			throw new IllegalArgumentException("\'cls\' must be instance of Classifier");
		}
		Collection<Element>results = new HashSet<Element>();
		for (DirectedRelationship d:((Classifier) cls).getTargetDirectedRelationships(UMLPackage.Literals.GENERALIZATION)) {
			results.addAll(d.getSources());
		}
		return results;
	}
	public Collection getSupertypes(Object generalizableElement) {
		if (!(generalizableElement instanceof Classifier)) {
			throw new IllegalArgumentException("\'generalizableElement\' must be instance of Classifier");
		}
		return((Classifier) generalizableElement).getGenerals();
	}
	public boolean hasCompositeEnd(Object association) {
		if (!(association instanceof Association)) {
			throw new IllegalArgumentException("\'association\' must be instance of Association");
		}
		for (Property p:((Association) association).getMemberEnds()) {
			if (p.getAggregation() == AggregationKind.COMPOSITE_LITERAL) {
				return true;
			}
		}
		return false;
	}
	public boolean isSubType(Object type,Object subType) {
		if (!(type instanceof Class)||!(subType instanceof Class)) {
			throw new IllegalArgumentException("type and subType must be instances of java.lang.Class");
		}
		return((Class) type).isAssignableFrom((Class) subType);
	}
	public boolean isValidNamespace(Object element,Object namespace) {
		if (!(element instanceof NamedElement)||!(namespace instanceof Namespace)) {
			return false;
		}
		if (((NamedElement) element).getNamespace() == namespace) {
			return true;
		}
		try {
			RunnableClass run = getRunnableClassForAddCommand((Namespace) namespace,(NamedElement) element);
		}catch (UnsupportedOperationException e) {
			return false;
		}
		return true;
	}
	public void removeAnnotatedElement(final Object comment,final Object annotatedElement) {
		if (!(annotatedElement instanceof Element)) {
			throw new IllegalArgumentException("annotatedElement must be instance of Element");
		}
		if (!(comment instanceof Comment)) {
			throw new IllegalArgumentException("comment must be instance of Comment");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((Comment) comment).getAnnotatedElements().remove((Element) annotatedElement);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Remove the link between the comment # and the element #",comment,annotatedElement));
	}
	public void removeClientDependency(final Object handle,final Object dep) {
		if (!(handle instanceof NamedElement)) {
			throw new IllegalArgumentException("handle must be instance of NamedElement");
		}
		if (!(dep instanceof Dependency)) {
			throw new IllegalArgumentException("dep must be instance of Dependency");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((NamedElement) handle).getClientDependencies().remove(dep);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Remove the client dependency # from the element #",handle,dep));
	}
	public void removeConnection(final Object handle,final Object connection) {
		if (!(handle instanceof Association)) {
			throw new IllegalArgumentException("handle must be instance of Association");
		}
		if (!(connection instanceof Property)) {
			throw new IllegalArgumentException("connection must be instance of Property");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		if (((Association) handle).getOwnedEnds().contains(connection)) {
			((Association) handle).getOwnedEnds().remove(connection);
		}
		if (((Property) connection).getAssociation() == handle) {
			((Property) connection).setAssociation(null);
		}
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Remove the association end # from the association #",connection,handle));
	}
	public void removeConstraint(Object handle,Object cons) {
		throw new NotYetImplementedException();
	}
	public void removeDeploymentLocation(Object handle,Object node) {
		throw new NotYetImplementedException();
	}
	public void removeElementResidence(Object handle,Object residence) {
		throw new NotYetImplementedException();
	}
	public void removeFeature(Object cls,Object feature) {
		removeOwnedElement(cls,feature);
	}
	public void removeLiteral(Object enumeration,Object literal) {
		removeOwnedElement(enumeration,literal);
	}
	public void removeOwnedElement(Object handle,Object value) {
		if (!(handle instanceof Element)) {
			throw new IllegalArgumentException("handle must be instance of Element");
		}
		if (!(value instanceof Element)) {
			throw new IllegalArgumentException("value must be instance of Element");
		}
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,getRunnableClassForRemoveCommand((Element) value),"Remove the element # from the owner #",value,handle));
	}
	public void removeParameter(Object handle,Object parameter) {
		removeOwnedElement(handle,parameter);
	}
	public void removeQualifier(Object handle,Object qualifier) {
		removeOwnedElement(handle,qualifier);
	}
	public void removeSourceFlow(Object handle,Object flow) {
		throw new NotYetImplementedException();
	}
	public void removeStereotype(Object handle,Object stereo) {
		throw new NotYetImplementedException();
	}
	public void removeSupplierDependency(final Object supplier,final Object dependency) {
		if (!(supplier instanceof NamedElement)) {
			throw new IllegalArgumentException("supplier must be instance of NamedElement");
		}
		if (!(dependency instanceof Dependency)) {
			throw new IllegalArgumentException("dependency must be instance of Dependency");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((Dependency) dependency).getSuppliers().remove(supplier);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Remove the supplier # from the dependency #",supplier,dependency));
	}
	public void removeTargetFlow(Object handle,Object flow) {
		throw new NotYetImplementedException();
	}
	public void removeTemplateArgument(Object binding,Object argument) {
		throw new NotYetImplementedException();
	}
	public void removeTemplateParameter(Object handle,Object parameter) {
		throw new NotYetImplementedException();
	}
	public void setAbstract(final Object handle,final boolean isAbstract) {
		if (!(handle instanceof Classifier)&&!(handle instanceof BehavioralFeature)) {
			throw new IllegalArgumentException("handle must be instance of Classifier or BehavioralFeature");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		if (handle instanceof Classifier) {
			((Classifier) handle).setIsAbstract(isAbstract);
		}else if (handle instanceof BehavioralFeature) {
			((BehavioralFeature) handle).setIsAbstract(isAbstract);
		}
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set isAbstract to # for #",isAbstract,handle));
	}
	public void setActive(final Object handle,final boolean isActive) {
		if (!(handle instanceof org.eclipse.uml2.uml.Class)) {
			throw new IllegalArgumentException("handle must be instance of UML2 Class");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((org.eclipse.uml2.uml.Class) handle).setIsActive(isActive);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set isActive to # for #",isActive,handle));
	}
	public void setAggregation(final Object handle,final Object aggregationKind) {
		if (!(handle instanceof Property)) {
			throw new IllegalArgumentException("handle must be instance of Property");
		}
		if (!(aggregationKind instanceof AggregationKind)) {
			throw new IllegalArgumentException("aggregationKind must be instance of AggregationKind");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((Property) handle).setAggregation((AggregationKind) aggregationKind);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set the aggregation # to the association end #",aggregationKind,handle));
	}
	public void setAnnotatedElements(final Object handle,final Collection elems) {
		if (!(handle instanceof Comment)) {
			throw new IllegalArgumentException("handle must be instance of Comment");
		}
		if (elems == null) {
			throw new NullPointerException("elems must be non-null");
		}
		for (Object o:elems) {
			if (!(o instanceof Element)) {
				throw new IllegalArgumentException("the collection must contain only instances of Element");
			}
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((Comment) handle).getAnnotatedElements().clear();
		for (Object o:elems) {
			((Comment) handle).getAnnotatedElements().add((Element) o);
		}
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set # annotated alements for the comment #",elems.size(),handle));
	}
	public void setAssociation(Object handle,Object association) {
		throw new NotYetImplementedException();
	}
	public void setAttributes(Object classifier,List attributes) {
		throw new NotYetImplementedException();
	}
	public void setBody(Object handle,Object expr) {
		throw new NotYetImplementedException();
	}
	public void setBody(Object handle,String body) {
		if (!(handle instanceof Comment)) {
			throw new IllegalArgumentException();
		}
		((Comment) handle).setBody(body);
	}
	@SuppressWarnings("deprecation")public void setChangeability(Object handle,Object ck) {
		throw new NotImplementedException();
	}
	public void setChild(final Object handle,final Object child) {
		if (!(handle instanceof Generalization)) {
			throw new IllegalArgumentException("handle must be instance of Generalization");
		}
		if (!(child instanceof Classifier)) {
			throw new IllegalArgumentException("child must be instance of Classifier");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((Generalization) handle).setSpecific((Classifier) child);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set the # as the specific classifier of the generalization #",child,handle));
	}
	public void setConcurrency(Object handle,Object concurrencyKind) {
		throw new NotYetImplementedException();
	}
	public void setConnections(Object handle,Collection ends) {
		throw new NotYetImplementedException();
	}
	public void setContainer(Object handle,Object component) {
		throw new NotYetImplementedException();
	}
	public void setDefaultElement(Object handle,Object element) {
		throw new NotYetImplementedException();
	}
	public void setDefaultValue(Object handle,Object expression) {
		throw new NotYetImplementedException();
	}
	public void setDiscriminator(Object handle,String discriminator) {
		throw new NotImplementedException();
	}
	public void setEnumerationLiterals(Object enumeration,List literals) {
		throw new NotYetImplementedException();
	}
	public void setFeature(Object classifier,int index,Object feature) {
		throw new NotYetImplementedException();
	}
	public void setFeatures(Object classifier,Collection features) {
		throw new NotYetImplementedException();
	}
	public void setInitialValue(Object attribute,Object expression) {
		throw new NotYetImplementedException();
	}
	public void setKind(Object handle,Object kind) {
		throw new NotYetImplementedException();
	}
	public void setLeaf(final Object handle,final boolean isLeaf) {
		if (!(handle instanceof RedefinableElement)) {
			throw new IllegalArgumentException("handle must be instance of RedefinableElement");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((RedefinableElement) handle).setIsLeaf(isLeaf);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set isLeaf to # for #",isLeaf,handle));
	}
	public void setModelElementContainer(Object handle,Object container) {
		addOwnedElement(container,handle);
	}
	public void setMultiplicity(final Object handle,Object arg) {
		if (arg == null) {
			return;
		}
		if (!(handle instanceof MultiplicityElement)) {
			throw new IllegalArgumentException();
		}
		if (arg instanceof String) {
			int lower = 1,upper = 1;
			try {
				int i = Integer.parseInt((String) arg);
				lower = i;
				upper = i;
			}catch (NumberFormatException e) {
				throw new NotYetImplementedException();
			}
			final int lower_ = lower,upper_ = upper;
			RunnableClass run = new RunnableClass() {
			public void run() {
				((MultiplicityElement) handle).setLower(lower_);
				((MultiplicityElement) handle).setUpper(upper_);
			}
		};
			editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set the multiplicity # to the element #",arg,handle));
		}else {
			throw new NotYetImplementedException();
		}
	}
	public void setName(final Object handle,final String name) {
		if (!(handle instanceof NamedElement)) {
			throw new IllegalArgumentException("handle must be instance of NamedElement");
		}
		if (name == null) {
			throw new NullPointerException("name must be non-null");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((NamedElement) handle).setName(name);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set the name \"#\" to the named element #",name,handle));
	}
	public void setNamespace(Object handle,Object ns) {
		addOwnedElement(ns,handle);
	}
	public void setNavigable(final Object handle,final boolean flag) {
		if (!(handle instanceof Property)) {
			throw new IllegalArgumentException("handle must be instance of Property");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((Property) handle).setIsNavigable(flag);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set isNavigable to # for the association end #",flag,handle));
	}
	public void setOperations(Object classifier,List operations) {
		throw new NotYetImplementedException();
	}
	public void setOrdering(Object handle,Object ordering) {
		throw new NotYetImplementedException();
	}
	public void setOwner(Object handle,Object owner) {
		throw new NotYetImplementedException();
	}
	public void setParameter(Object handle,Object parameter) {
		throw new NotYetImplementedException();
	}
	public void setParameters(Object handle,Collection parameters) {
		throw new NotYetImplementedException();
	}
	public void setParent(final Object handle,final Object parent) {
		if (!(handle instanceof Generalization)) {
			throw new IllegalArgumentException("handle must be instance of Generalization");
		}
		if (!(parent instanceof Classifier)) {
			throw new IllegalArgumentException("parent must be instance of Classifier");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((Generalization) handle).setGeneral((Classifier) parent);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set the # as the general classifier of the generalization #",parent,handle));
	}
	public void setPowertype(Object handle,Object powerType) {
		throw new NotYetImplementedException();
	}
	public void setQualifiers(Object handle,List qualifiers) {
		throw new NotYetImplementedException();
	}
	public void setQuery(final Object handle,final boolean isQuery) {
		if (!(handle instanceof Operation)) {
			throw new IllegalArgumentException("handle must be instance of Operation");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((Operation) handle).setIsQuery(isQuery);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set isQuery to # for the operation #",isQuery,handle));
	}
	public void setRaisedSignals(Object handle,Collection raisedSignals) {
		throw new NotYetImplementedException();
	}
	public void setReadOnly(final Object handle,final boolean isReadOnly) {
		if (!(handle instanceof StructuralFeature)) {
			throw new IllegalArgumentException("handle must be instance of StructuralFeature");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((StructuralFeature) handle).setIsReadOnly(isReadOnly);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set isReadOnly to # for the structural feature #",isReadOnly,handle));
	}
	public void setResident(Object handle,Object resident) {
		throw new NotYetImplementedException();
	}
	public void setResidents(Object handle,Collection residents) {
		throw new NotYetImplementedException();
	}
	public void setRoot(Object handle,boolean isRoot) {
		throw new NotImplementedException();
	}
	public void setSources(Object handle,Collection specifications) {
		throw new NotYetImplementedException();
	}
	public void setSpecification(Object handle,boolean isSpecification) {
		throw new NotYetImplementedException();
	}
	public void setSpecification(Object method,Object specification) {
		throw new NotYetImplementedException();
	}
	public void setSpecification(Object operation,String specification) {
		throw new NotYetImplementedException();
	}
	public void setSpecifications(Object handle,Collection specifications) {
		throw new NotYetImplementedException();
	}
	public void setStatic(final Object feature,final boolean isStatic) {
		if (!(feature instanceof Feature)) {
			throw new IllegalArgumentException("feature must be instance of Feature");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((Feature) feature).setIsStatic(isStatic);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set isStatic to # for the feature #",isStatic,feature));
	}
	public void setTaggedValue(Object handle,String tag,String value) {
		throw new NotYetImplementedException();
	}
	@SuppressWarnings("deprecation")public void setTargetScope(Object handle,Object targetScope) {
		throw new NotImplementedException();
	}
	public void setType(final Object handle,final Object type) {
		if (!(handle instanceof TypedElement)) {
			throw new IllegalArgumentException("handle must be instance of TypedElement");
		}
		if (!(type instanceof Type)) {
			throw new IllegalArgumentException("type must be instance of Type");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((TypedElement) handle).setType((Type) type);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set the type # for the typed element #",type,handle));
	}
	public void setVisibility(final Object handle,final Object visibility) {
		if (!(handle instanceof NamedElement)) {
			throw new IllegalArgumentException("handle must be instance of NamedElement");
		}
		if (!(visibility instanceof VisibilityKind)) {
			throw new IllegalArgumentException("visibility must be instance of VisibilityKind");
		}
		RunnableClass run = new RunnableClass() {
	public void run() {
		((NamedElement) handle).setVisibility((VisibilityKind) visibility);
	}
};
		editingDomain.getCommandStack().execute(new ChangeCommand(modelImpl,run,"Set the visibility # to the named element #",visibility,handle));
	}
	public Collection getParents(Object generalizableElement) {
		throw new NotYetImplementedException();
	}
	public Object getPackageImport(Object supplier,Object client) {
		throw new NotYetImplementedException();
	}
	public Collection getPackageImports(Object client) {
		throw new NotYetImplementedException();
	}
}



