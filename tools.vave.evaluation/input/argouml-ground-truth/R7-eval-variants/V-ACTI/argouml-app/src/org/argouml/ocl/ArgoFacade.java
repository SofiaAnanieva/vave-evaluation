package org.argouml.ocl;

import java.util.Collection;
import java.util.Iterator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import tudresden.ocl.check.OclTypeException;
import tudresden.ocl.check.types.Any;
import tudresden.ocl.check.types.Basic;
import tudresden.ocl.check.types.Type;
import tudresden.ocl.check.types.Type2;


public class ArgoFacade implements tudresden.ocl.check.types.ModelFacade {
	private Object target;
	public ArgoFacade(Object t) {
		if (Model.getFacade().isAClassifier(t)) {
			target = t;
		}
	}
	public Any getClassifier(String name) {
		Project p = ProjectManager.getManager().getCurrentProject();
		if (target != null&&Model.getFacade().getName(target).equals(name)) {
			return new ArgoAny(target);
		}
		Object classifier = p.findTypeInModel(name,p.getModel());
		if (classifier == null) {
			classifier = p.findType(name,false);
			if (classifier == null) {
				throw new OclTypeException("cannot find classifier: " + name);
			}
		}
		return new ArgoAny(classifier);
	}
}

class ArgoAny implements Any,Type2 {
	private Object classifier;
	ArgoAny(Object cl) {
			classifier = cl;
		}
	public Type navigateQualified(String name,Type[]qualifiers)throws OclTypeException {
		if (classifier == null) {
			throw new OclTypeException("attempting to access features of Void");
		}
		if (qualifiers != null) {
			throw new OclTypeException("qualified associations " + "not supported yet!");
		}
		Type type = Basic.navigateAnyQualified(name,this,qualifiers);
		if (type != null) {
			return type;
		}
		Object foundAssocType = null,foundAttribType = null;
		boolean isSet = false,isSequence = false;
		Collection attributes = Model.getCoreHelper().getAttributesInh(classifier);
		Iterator iter = attributes.iterator();
		while (iter.hasNext()&&foundAttribType == null) {
			Object attr = iter.next();
			if (Model.getFacade().getName(attr).equals(name)) {
				foundAttribType = Model.getFacade().getType(attr);
			}
		}
		Collection associationEnds = Model.getCoreHelper().getAssociateEndsInh(classifier);
		Iterator asciter = associationEnds.iterator();
		while (asciter.hasNext()&&foundAssocType == null) {
			Object ae = asciter.next();
			if (Model.getFacade().getName(ae) != null&&name.equals(Model.getFacade().getName(ae))) {
				foundAssocType = Model.getFacade().getType(ae);
			}else if (Model.getFacade().getName(ae) == null||Model.getFacade().getName(ae).equals("")) {
				String oppositeName = Model.getFacade().getName(Model.getFacade().getType(ae));
				if (oppositeName != null) {
					String lowerOppositeName = oppositeName.substring(0,1).toLowerCase();
					lowerOppositeName += oppositeName.substring(1);
					if (lowerOppositeName.equals(name)) {
						foundAssocType = Model.getFacade().getType(ae);
					}
				}
			}
			if (foundAssocType != null) {
				Object multiplicity = Model.getFacade().getMultiplicity(ae);
				if (multiplicity != null&&(Model.getFacade().getUpper(multiplicity) > 1||Model.getFacade().getUpper(multiplicity) == -1)) {
					if (Model.getExtensionMechanismsHelper().hasStereotype(ae,"ordered")) {
						isSequence = true;
					}else {
						isSet = true;
					}
				}
			}
		}
		if (foundAssocType != null&&foundAttribType != null) {
			throw new OclTypeException("cannot access feature " + name + " of classifier " + toString() + " because both an attribute and " + "an association end of this name " + "where found");
		}
		Object foundType;
		if (foundAssocType == null) {
			foundType = foundAttribType;
		}else {
			foundType = foundAssocType;
		}
		if (foundType == null) {
			throw new OclTypeException("attribute " + name + " not found in classifier " + toString());
		}
		Type result = getOclRepresentation(foundType);
		if (isSet) {
			result = new tudresden.ocl.check.types.Collection(tudresden.ocl.check.types.Collection.SET,result);
		}
		if (isSequence) {
			result = new tudresden.ocl.check.types.Collection(tudresden.ocl.check.types.Collection.SEQUENCE,result);
		}
		return result;
	}
	public Type navigateParameterizedQuery(String name,Type[]qualifiers)throws OclTypeException {
		return internalNavigateParameterized(name,qualifiers,true);
	}
	public Type navigateParameterized(String name,Type[]qualifiers)throws OclTypeException {
		return internalNavigateParameterized(name,qualifiers,false);
	}
	private Type internalNavigateParameterized(final String name,final Type[]params,boolean fCheckIsQuery)throws OclTypeException {
		if (classifier == null) {
			throw new OclTypeException("attempting to access features of Void");
		}
		Type type = Basic.navigateAnyParameterized(name,params);
		if (type != null) {
			return type;
		}
		Object foundOp = null;
		java.util.
				Collection operations = Model.getFacade().getOperations(classifier);
		Iterator iter = operations.iterator();
		while (iter.hasNext()&&foundOp == null) {
			Object op = iter.next();
			if (operationMatchesCall(op,name,params)) {
				foundOp = op;
			}
		}
		if (foundOp == null) {
			throw new OclTypeException("operation " + name + " not found in classifier " + toString());
		}
		if (fCheckIsQuery) {
			if (!Model.getFacade().isQuery(foundOp)) {
				throw new OclTypeException("Non-query operations cannot " + "be used in OCL expressions. (" + name + ")");
			}
		}
		Collection returnParams = Model.getCoreHelper().getReturnParameters(foundOp);
		Object rp;
		if (returnParams.size() == 0) {
			rp = null;
		}else {
			rp = returnParams.iterator().next();
		}
		if (rp == null||Model.getFacade().getType(rp) == null) {
			return new ArgoAny(null);
		}
		Object returnType = Model.getFacade().getType(rp);
		return getOclRepresentation(returnType);
	}
	public boolean conformsTo(Type type) {
		if (type instanceof ArgoAny) {
			ArgoAny other = (ArgoAny) type;
			return equals(type)||Model.getCoreHelper().getAllSupertypes(classifier).contains(other.classifier);
		}
		return false;
	}
	public boolean equals(Object o) {
		ArgoAny any = null;
		if (o instanceof ArgoAny) {
			any = (ArgoAny) o;
			return(any.classifier == classifier);
		}
		return false;
	}
	public int hashCode() {
		if (classifier == null) {
			return 0;
		}
		return classifier.hashCode();
	}
	public String toString() {
		if (classifier == null) {
			return"Void";
		}
		return Model.getFacade().getName(classifier);
	}
	public boolean hasState(String name) {
		return false;
	}
	protected Type getOclRepresentation(Object foundType) {
		Type result = null;
		if (Model.getFacade().getName(foundType).equals("int")||Model.getFacade().getName(foundType).equals("Integer")) {
			result = Basic.INTEGER;
		}
		if (Model.getFacade().getName(foundType).equals("float")||Model.getFacade().getName(foundType).equals("double")) {
			result = Basic.REAL;
		}
		if (Model.getFacade().getName(foundType).equals("bool")||Model.getFacade().getName(foundType).equals("Boolean")||Model.getFacade().getName(foundType).equals("boolean")) {
			result = Basic.BOOLEAN;
		}
		if (Model.getFacade().getName(foundType).equals("String")) {
			result = Basic.STRING;
		}
		if (result == null) {
			result = new ArgoAny(foundType);
		}
		return result;
	}
	protected boolean operationMatchesCall(Object operation,String callName,Type[]callParams) {
		if (!callName.equals(Model.getFacade().getName(operation))) {
			return false;
		}
		Collection operationParameters = Model.getFacade().getParameters(operation);
		if (!(Model.getFacade().isReturn(operationParameters.iterator().next())&&operationParameters.size() == (callParams. + 1))) {
			return false;
		}
		Iterator paramIter = operationParameters.iterator();
		paramIter.next();
		int index = 0;
		while (paramIter.hasNext()) {
			Object nextParam = paramIter.next();
			Object paramType = Model.getFacade().getType(nextParam);
			Type operationParam = getOclRepresentation(paramType);
			if (!callParams[index].conformsTo(operationParam)) {
				return false;
			}
			index++;
		}
		return true;
	}
}



