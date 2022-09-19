package org.argouml.profile.internal.ocl.uml14;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.profile.internal.ocl.DefaultOclEvaluator;
import org.argouml.profile.internal.ocl.InvalidOclException;
import org.argouml.profile.internal.ocl.ModelInterpreter;


public class ModelAccessModelInterpreter implements ModelInterpreter {
	private static final Logger LOG = Logger.getLogger(ModelAccessModelInterpreter.class);
	private static Uml14ModelInterpreter uml14mi = new Uml14ModelInterpreter();
	@SuppressWarnings("unchecked")public Object invokeFeature(Map<String,Object>vt,Object subject,String feature,String type,Object[]parameters) {
		if (subject == null) {
			subject = vt.get("self");
		}
		if (Model.getFacade().isAAssociation(subject)) {
			if (type.equals(".")) {
				if (feature.equals("connection")) {
					return new ArrayList<Object>(Model.getFacade().getConnections(subject));
				}
				if (feature.equals("allConnections")) {
					return new HashSet<Object>(Model.getFacade().getConnections(subject));
				}
			}
		}
		if (Model.getFacade().isAAssociationEnd(subject)) {
			if (type.equals(".")) {
				if (feature.equals("aggregation")) {
					return Model.getFacade().getAggregation(subject);
				}
				if (feature.equals("changeability")) {
					return Model.getFacade().getChangeability(subject);
				}
				if (feature.equals("ordering")) {
					return Model.getFacade().getOrdering(subject);
				}
				if (feature.equals("isNavigable")) {
					return Model.getFacade().isNavigable(subject);
				}
				if (feature.equals("multiplicity")) {
					return Model.getFacade().getMultiplicity(subject);
				}
				if (feature.equals("targetScope")) {
					return Model.getFacade().getTargetScope(subject);
				}
				if (feature.equals("visibility")) {
					return Model.getFacade().getVisibility(subject);
				}
				if (feature.equals("qualifier")) {
					return Model.getFacade().getQualifiers(subject);
				}
				if (feature.equals("specification")) {
					return Model.getFacade().getSpecification(subject);
				}
				if (feature.equals("participant")) {
					return Model.getFacade().getClassifier(subject);
				}
				if (feature.equals("upperbound")) {
					return Model.getFacade().getUpper(subject);
				}
			}
		}
		if (Model.getFacade().isAAttribute(subject)) {
			if (type.equals(".")) {
				if (feature.equals("initialValue")) {
					return Model.getFacade().getInitialValue(subject);
				}
				if (feature.equals("associationEnd")) {
					return new ArrayList<Object>(Model.getFacade().getAssociationEnds(subject));
				}
			}
		}
		if (Model.getFacade().isABehavioralFeature(subject)) {
			if (type.equals(".")) {
				if (feature.equals("isQuery")) {
					return Model.getFacade().isQuery(subject);
				}
				if (feature.equals("parameter")) {
					return new ArrayList<Object>(Model.getFacade().getParameters(subject));
				}
			}
		}
		if (Model.getFacade().isABinding(subject)) {
			if (type.equals(".")) {
				if (feature.equals("argument")) {
					return Model.getFacade().getArguments(subject);
				}
			}
		}
		if (Model.getFacade().isAClass(subject)) {
			if (type.equals(".")) {
				if (feature.equals("isActive")) {
					return Model.getFacade().isActive(subject);
				}
			}
		}
		if (Model.getFacade().isAClassifier(subject)) {
			if (type.equals(".")) {
				if (feature.equals("feature")) {
					return new ArrayList<Object>(Model.getFacade().getFeatures(subject));
				}
				if (feature.equals("feature")) {
					return new ArrayList<Object>(Model.getFacade().getFeatures(subject));
				}
				if (feature.equals("association")) {
					return new ArrayList<Object>(Model.getFacade().getAssociatedClasses(subject));
				}
				if (feature.equals("powertypeRange")) {
					return new HashSet<Object>(Model.getFacade().getPowertypeRanges(subject));
				}
				if (feature.equals("feature")) {
					return new ArrayList<Object>(Model.getFacade().getFeatures(subject));
				}
				if (feature.equals("allFeatures")) {
					return internalOcl(subject,vt,"self.feature->union(" + "self.parent.oclAsType(Classifier).allFeatures)");
				}
				if (feature.equals("allOperations")) {
					return internalOcl(subject,vt,"self.allFeatures->" + "select(f | f.oclIsKindOf(Operation))");
				}
				if (feature.equals("allMethods")) {
					return internalOcl(subject,vt,"self.allFeatures->" + "select(f | f.oclIsKindOf(Method))");
				}
				if (feature.equals("allAttributes")) {
					return internalOcl(subject,vt,"self.allFeatures->" + "select(f | f.oclIsKindOf(Attribute))");
				}
				if (feature.equals("associations")) {
					return internalOcl(subject,vt,"self.association.association->asSet()");
				}
				if (feature.equals("allAssociations")) {
					return internalOcl(subject,vt,"self.associations->union(" + "self.parent.oclAsType(Classifier).allAssociations)");
				}
				if (feature.equals("oppositeAssociationEnds")) {
					return internalOcl(subject,vt,"self.associations->select ( a | a.connection->select ( ae |" + "ae.participant = self ).size = 1 )->collect ( a |" + "a.connection->" + "select ( ae | ae.participant <> self ) )->union (" + "self.associations->select ( a | a.connection->select ( ae |" + "ae.participant = self ).size > 1 )->collect ( a |" + "a.connection) )");
				}
				if (feature.equals("allOppositeAssociationEnds")) {
					return internalOcl(subject,vt,"self.oppositeAssociationEnds->" + "union(self.parent.allOppositeAssociationEnds )");
				}
				if (feature.equals("specification")) {
					return internalOcl(subject,vt,"self.clientDependency->" + "select(d |" + "d.oclIsKindOf(Abstraction)" + "and d.stereotype.name = \"realization\" " + "and d.supplier.oclIsKindOf(Classifier))" + ".supplier.oclAsType(Classifier)");
				}
				if (feature.equals("allContents")) {
					return internalOcl(subject,vt,"self.contents->union(" + "self.parent.allContents->select(e |" + "e.elementOwnership.visibility = #public or true or " + " e.elementOwnership.visibility = #protected))");
				}
				if (feature.equals("allDiscriminators")) {
					return internalOcl(subject,vt,"self.generalization.discriminator->" + "union(self.parent.oclAsType(Classifier).allDiscriminators)");
				}
			}
		}
		if (Model.getFacade().isAComment(subject)) {
			if (type.equals(".")) {
				if (feature.equals("body")) {
					return Model.getFacade().getBody(subject);
				}
				if (feature.equals("annotatedElement")) {
					return new HashSet<Object>(Model.getFacade().getAnnotatedElements(subject));
				}
			}
		}
		if (Model.getFacade().isAComponent(subject)) {
			if (type.equals(".")) {
				if (feature.equals("deploymentLocation")) {
					return new HashSet<Object>(Model.getFacade().getDeploymentLocations(subject));
				}
				if (feature.equals("resident")) {
					return new HashSet<Object>(Model.getFacade().getResidents(subject));
				}
				if (feature.equals("allResidentElements")) {
					return internalOcl(subject,vt,"self.resident->union(" + "self.parent.oclAsType(Component).allResidentElements->select( re |" + "re.elementResidence.visibility = #public or " + "re.elementResidence.visibility = #protected))");
				}
			}
		}
		if (Model.getFacade().isAConstraint(subject)) {
			if (type.equals(".")) {
				if (feature.equals("body")) {
					return Model.getFacade().getBody(subject);
				}
				if (feature.equals("constrainedElement")) {
					return Model.getFacade().getConstrainedElements(subject);
				}
			}
		}
		if (Model.getFacade().isADependency(subject)) {
			if (type.equals(".")) {
				if (feature.equals("client")) {
					return new HashSet<Object>(Model.getFacade().getClients(subject));
				}
				if (feature.equals("supplier")) {
					return new HashSet<Object>(Model.getFacade().getSuppliers(subject));
				}
			}
		}
		if (Model.getFacade().isAElementResidence(subject)) {
			if (type.equals(".")) {
				if (feature.equals("visibility")) {
					return Model.getFacade().getVisibility(subject);
				}
			}
		}
		if (Model.getFacade().isAEnumeration(subject)) {
			if (type.equals(".")) {
				if (feature.equals("literal")) {
					return Model.getFacade().getEnumerationLiterals(subject);
				}
			}
		}
		if (Model.getFacade().isAEnumerationLiteral(subject)) {
			if (type.equals(".")) {
				if (feature.equals("enumeration")) {
					return Model.getFacade().getEnumeration(subject);
				}
			}
		}
		if (Model.getFacade().isAFeature(subject)) {
			if (type.equals(".")) {
				if (feature.equals("ownerScope")) {
					return Model.getFacade().isStatic(subject);
				}
				if (feature.equals("visibility")) {
					return Model.getFacade().getVisibility(subject);
				}
				if (feature.equals("owner")) {
					return Model.getFacade().getOwner(subject);
				}
			}
		}
		if (Model.getFacade().isAGeneralizableElement(subject)) {
			if (type.equals(".")) {
				if (feature.equals("isAbstract")) {
					return Model.getFacade().isAbstract(subject);
				}
				if (feature.equals("isLeaf")) {
					return Model.getFacade().isLeaf(subject);
				}
				if (feature.equals("isRoot")) {
					return Model.getFacade().isRoot(subject);
				}
				if (feature.equals("generalization")) {
					return new HashSet<Object>(Model.getFacade().getGeneralizations(subject));
				}
				if (feature.equals("specialization")) {
					return new HashSet<Object>(Model.getFacade().getSpecializations(subject));
				}
				if (feature.equals("parent")) {
					return internalOcl(subject,vt,"self.generalization.parent");
				}
				if (feature.equals("allParents")) {
					return internalOcl(subject,vt,"self.parent->union(self.parent.allParents)");
				}
			}
		}
		if (Model.getFacade().isAGeneralization(subject)) {
			if (type.equals(".")) {
				if (feature.equals("discriminator")) {
					return Model.getFacade().getDiscriminator(subject);
				}
				if (feature.equals("child")) {
					return Model.getFacade().getSpecific(subject);
				}
				if (feature.equals("parent")) {
					return Model.getFacade().getGeneral(subject);
				}
				if (feature.equals("powertype")) {
					return Model.getFacade().getPowertype(subject);
				}
				if (feature.equals("specialization")) {
					return new HashSet<Object>(Model.getFacade().getSpecializations(subject));
				}
			}
		}
		if (Model.getFacade().isAMethod(subject)) {
			if (type.equals(".")) {
				if (feature.equals("body")) {
					return Model.getFacade().getBody(subject);
				}
				if (feature.equals("specification")) {
					return Model.getFacade().getSpecification(subject);
				}
			}
		}
		if (Model.getFacade().isAModelElement(subject)) {
			if (type.equals(".")) {
				if (feature.equals("name")) {
					String name = Model.getFacade().getName(subject);
					if (name == null) {
						name = "";
					}
					return name;
				}
				if (feature.equals("clientDependency")) {
					return new HashSet<Object>(Model.getFacade().getClientDependencies(subject));
				}
				if (feature.equals("constraint")) {
					return new HashSet<Object>(Model.getFacade().getConstraints(subject));
				}
				if (feature.equals("namespace")) {
					return Model.getFacade().getNamespace(subject);
				}
				if (feature.equals("supplierDependency")) {
					return new HashSet<Object>(Model.getFacade().getSupplierDependencies(subject));
				}
				if (feature.equals("templateParameter")) {
					return Model.getFacade().getTemplateParameters(subject);
				}
				if (feature.equals("stereotype")) {
					return Model.getFacade().getStereotypes(subject);
				}
				if (feature.equals("taggedValue")) {
					return Model.getFacade().getTaggedValuesCollection(subject);
				}
				if (feature.equals("constraint")) {
					return Model.getFacade().getConstraints(subject);
				}
				if (feature.equals("supplier")) {
					return internalOcl(subject,vt,"self.clientDependency.supplier");
				}
				if (feature.equals("allSuppliers")) {
					return internalOcl(subject,vt,"self.supplier->union(self.supplier.allSuppliers)");
				}
				if (feature.equals("model")) {
					return internalOcl(subject,vt,"self.namespace->" + "union(self.namespace.allSurroundingNamespaces)->" + "select( ns| ns.oclIsKindOf (Model))");
				}
				if (feature.equals("isTemplate")) {
					return!Model.getFacade().getTemplateParameters(subject).isEmpty();
				}
				if (feature.equals("isInstantiated")) {
					return internalOcl(subject,vt,"self.clientDependency->" + "select(oclIsKindOf(Binding))->notEmpty");
				}
				if (feature.equals("templateArgument")) {
					return internalOcl(subject,vt,"self.clientDependency->" + "select(oclIsKindOf(Binding))." + "oclAsType(Binding).argument");
				}
			}
		}
		if (Model.getFacade().isANamespace(subject)) {
			if (type.equals(".")) {
				if (feature.equals("ownedElement")) {
					return new HashSet<Object>(Model.getFacade().getOwnedElements(subject));
				}
				if (feature.equals("contents")) {
					return internalOcl(subject,vt,"self.ownedElement->" + "union(self.ownedElement->" + "select(x|x.oclIsKindOf(Namespace)).contents)");
				}
				if (feature.equals("allContents")) {
					return internalOcl(subject,vt,"self.contents");
				}
				if (feature.equals("allVisibleElements")) {
					return internalOcl(subject,vt,"self.allContents ->" + "select(e |e.elementOwnership.visibility = #public)");
				}
				if (feature.equals("allSurroundingNamespaces")) {
					return internalOcl(subject,vt,"self.namespace->" + "union(self.namespace.allSurroundingNamespaces)");
				}
			}
		}
		if (Model.getFacade().isANode(subject)) {
			if (type.equals(".")) {
				if (feature.equals("deployedComponent")) {
					return new HashSet<Object>(Model.getFacade().getDeployedComponents(subject));
				}
			}
		}
		if (Model.getFacade().isAOperation(subject)) {
			if (type.equals(".")) {
				if (feature.equals("concurrency")) {
					return Model.getFacade().getConcurrency(subject);
				}
				if (feature.equals("isAbstract")) {
					return Model.getFacade().isAbstract(subject);
				}
				if (feature.equals("isLeaf")) {
					return Model.getFacade().isLeaf(subject);
				}
				if (feature.equals("isRoot")) {
					return Model.getFacade().isRoot(subject);
				}
			}
		}
		if (Model.getFacade().isAParameter(subject)) {
			if (type.equals(".")) {
				if (feature.equals("defaultValue")) {
					return Model.getFacade().getDefaultValue(subject);
				}
				if (feature.equals("kind")) {
					return Model.getFacade().getKind(subject);
				}
			}
		}
		if (Model.getFacade().isAStructuralFeature(subject)) {
			if (type.equals(".")) {
				if (feature.equals("changeability")) {
					return Model.getFacade().getChangeability(subject);
				}
				if (feature.equals("multiplicity")) {
					return Model.getFacade().getMultiplicity(subject);
				}
				if (feature.equals("ordering")) {
					return Model.getFacade().getOrdering(subject);
				}
				if (feature.equals("targetScope")) {
					return Model.getFacade().getTargetScope(subject);
				}
				if (feature.equals("type")) {
					return Model.getFacade().getType(subject);
				}
			}
		}
		if (Model.getFacade().isATemplateArgument(subject)) {
			if (type.equals(".")) {
				if (feature.equals("binding")) {
					return Model.getFacade().getBinding(subject);
				}
				if (feature.equals("modelElement")) {
					return Model.getFacade().getModelElement(subject);
				}
			}
		}
		if (Model.getFacade().isATemplateParameter(subject)) {
			if (type.equals(".")) {
				if (feature.equals("defaultElement")) {
					return Model.getFacade().getDefaultElement(subject);
				}
			}
		}
		if (Model.getFacade().isAAssociationClass(subject)) {
			if (type.equals(".")) {
				if (feature.equals("allConnections")) {
					return internalOcl(subject,vt,"self.connection->union(self.parent->select(" + "s | s.oclIsKindOf(Association))->collect(" + "a : Association | a.allConnections))->asSet()");
				}
			}
		}
		if (Model.getFacade().isAStereotype(subject)) {
			if (type.equals(".")) {
				if (feature.equals("baseClass")) {
					return new HashSet<Object>(Model.getFacade().getBaseClasses(subject));
				}
				if (feature.equals("extendedElement")) {
					return new HashSet<Object>(Model.getFacade().getExtendedElements(subject));
				}
				if (feature.equals("definedTag")) {
					return new HashSet<Object>(Model.getFacade().getTagDefinitions(subject));
				}
			}
		}
		if (Model.getFacade().isATagDefinition(subject)) {
			if (type.equals(".")) {
				if (feature.equals("multiplicity")) {
					return Model.getFacade().getMultiplicity(subject);
				}
				if (feature.equals("tagType")) {
					return Model.getFacade().getType(subject);
				}
				if (feature.equals("typedValue")) {
					return new HashSet<Object>(Model.getFacade().getTypedValues(subject));
				}
				if (feature.equals("owner")) {
					return Model.getFacade().getOwner(subject);
				}
			}
		}
		if (Model.getFacade().isATaggedValue(subject)) {
			if (type.equals(".")) {
				if (feature.equals("dataValue")) {
					return Model.getFacade().getDataValue(subject);
				}
				if (feature.equals("type")) {
					return Model.getFacade().getType(subject);
				}
				if (feature.equals("referenceValue")) {
					return new HashSet<Object>(Model.getFacade().getReferenceValue(subject));
				}
			}
		}
		return null;
	}
	private Object internalOcl(Object subject,Map<String,Object>vt,String ocl) {
		try {
			Object oldSelf = vt.get("self");
			vt.put("self",subject);
			Object ret = DefaultOclEvaluator.getInstance().evaluate(vt,uml14mi,ocl);
			vt.put("self",oldSelf);
			return ret;
		}catch (InvalidOclException e) {
			LOG.error("Exception",e);
			return null;
		}
	}
	public Object getBuiltInSymbol(String sym) {
		for (String name:Model.getFacade().getMetatypeNames()) {
			if (name.equals(sym)) {
				return new OclType(sym);
			}
		}
		return null;
	}
}



