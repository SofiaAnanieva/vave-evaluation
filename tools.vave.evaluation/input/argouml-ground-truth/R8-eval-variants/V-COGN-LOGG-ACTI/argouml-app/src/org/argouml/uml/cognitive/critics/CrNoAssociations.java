package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrNoAssociations extends CrUML {
	public CrNoAssociations() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.RELATIONSHIPS);
		setKnowledgeTypes(Critic.KT_COMPLETENESS);
		addTrigger("associationEnd");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAClassifier(dm)))return NO_PROBLEM;
		if (!(Model.getFacade().isPrimaryObject(dm)))return NO_PROBLEM;
		if ((Model.getFacade().getName(dm) == null)||("".equals(Model.getFacade().getName(dm)))) {
			return NO_PROBLEM;
		}
		if (Model.getFacade().isAGeneralizableElement(dm)&&Model.getFacade().isAbstract(dm)) {
			return NO_PROBLEM;
		}
		if (Model.getFacade().isType(dm))return NO_PROBLEM;
		if (Model.getFacade().isUtility(dm))return NO_PROBLEM;
		if (Model.getFacade().getClientDependencies(dm).size() > 0)return NO_PROBLEM;
		if (Model.getFacade().getSupplierDependencies(dm).size() > 0)return NO_PROBLEM;
		if (Model.getFacade().isAUseCase(dm)) {
			Object usecase = dm;
			Collection includes = Model.getFacade().getIncludes(usecase);
			if (includes != null&&includes.size() >= 1) {
				return NO_PROBLEM;
			}
			Collection extend = Model.getFacade().getExtends(usecase);
			if (extend != null&&extend.size() >= 1) {
				return NO_PROBLEM;
			}
		}
		if (findAssociation(dm,0))return NO_PROBLEM;
		return PROBLEM_FOUND;
	}
	private boolean findAssociation(Object dm,int depth) {
		if (Model.getFacade().getAssociationEnds(dm).iterator().hasNext())return true;
		if (depth > 50)return false;
		Iterator iter = Model.getFacade().getGeneralizations(dm).iterator();
		while (iter.hasNext()) {
			Object parent = Model.getFacade().getGeneral(iter.next());
			if (parent == dm)continue;
			if (Model.getFacade().isAClassifier(parent))if (findAssociation(parent,depth + 1))return true;
		}
		if (Model.getFacade().isAUseCase(dm)) {
			Iterator iter2 = Model.getFacade().getExtends(dm).iterator();
			while (iter2.hasNext()) {
				Object parent = Model.getFacade().getExtension(iter2.next());
				if (parent == dm)continue;
				if (Model.getFacade().isAClassifier(parent))if (findAssociation(parent,depth + 1))return true;
			}
			Iterator iter3 = Model.getFacade().getIncludes(dm).iterator();
			while (iter3.hasNext()) {
				Object parent = Model.getFacade().getBase(iter3.next());
				if (parent == dm)continue;
				if (Model.getFacade().isAClassifier(parent))if (findAssociation(parent,depth + 1))return true;
			}
		}
		return false;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getUMLClass());
		ret.add(Model.getMetaTypes().getActor());
		ret.add(Model.getMetaTypes().getUseCase());
		return ret;
	}
}



