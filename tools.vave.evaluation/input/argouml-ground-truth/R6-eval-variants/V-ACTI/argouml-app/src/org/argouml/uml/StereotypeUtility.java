package org.argouml.uml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.Action;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.util.PathComparator;
import org.argouml.util.MyTokenizer;


public class StereotypeUtility {
	private StereotypeUtility() {
		super();
	}
	public static Action[]getApplyStereotypeActions(Object modelElement) {
		Set availableStereotypes = getAvailableStereotypes(modelElement);
		if (!availableStereotypes.isEmpty()) {
			Action[]menuActions = new Action[availableStereotypes.size()];
			Iterator it = availableStereotypes.iterator();
			for (int i = 0;it.hasNext();++i) {
				menuActions[i] = new ActionAddStereotype(modelElement,it.next());
			}
			return menuActions;
		}
		return new Action[0];
	}
	public static Set<Object>getAvailableStereotypes(Object modelElement) {
		Set<List>paths = new HashSet<List>();
		Set<Object>availableStereotypes = new TreeSet<Object>(new PathComparator());
		Collection models = ProjectManager.getManager().getCurrentProject().getModels();
		Collection topLevelModels = ProjectManager.getManager().getCurrentProject().getModels();
		Collection topLevelStereotypes = getTopLevelStereotypes(topLevelModels);
		Collection validTopLevelStereotypes = new ArrayList();
		addAllUniqueModelElementsFrom(availableStereotypes,paths,Model.getExtensionMechanismsHelper().getAllPossibleStereotypes(models,modelElement));
		for (Object stereotype:topLevelStereotypes) {
			if (Model.getExtensionMechanismsHelper().isValidStereotype(modelElement,stereotype)) {
				validTopLevelStereotypes.add(stereotype);
			}
		}
		addAllUniqueModelElementsFrom(availableStereotypes,paths,validTopLevelStereotypes);
		Object namespace = Model.getFacade().getNamespace(modelElement);
		if (namespace != null) {
			while (true) {
				getApplicableStereotypesInNamespace(modelElement,paths,availableStereotypes,namespace);
				Object newNamespace = Model.getFacade().getNamespace(namespace);
				if (newNamespace == null) {
					break;
				}
				namespace = newNamespace;
			}
		}
		addAllUniqueModelElementsFrom(availableStereotypes,paths,ProjectManager.getManager().getCurrentProject().getProfileConfiguration().findAllStereotypesForModelElement(modelElement));
		return availableStereotypes;
	}
	private static Collection<Object>getTopLevelStereotypes(Collection<Object>topLevelModels) {
		Collection<Object>ret = new ArrayList<Object>();
		for (Object model:topLevelModels) {
			for (Object stereotype:Model.getExtensionMechanismsHelper().getStereotypes(model)) {
				Object namespace = Model.getFacade().getNamespace(stereotype);
				if (Model.getFacade().getNamespace(namespace) == null) {
					ret.add(stereotype);
				}
			}
		}
		return ret;
	}
	private static void getApplicableStereotypesInNamespace(Object modelElement,Set<List>paths,Set<Object>availableStereotypes,Object namespace) {
		Collection allProfiles = getAllProfilePackages(Model.getFacade().getModel(modelElement));
		Collection<Object>allAppliedProfiles = new ArrayList<Object>();
		for (Object profilePackage:allProfiles) {
			Collection allDependencies = Model.getCoreHelper().getDependencies(profilePackage,namespace);
			for (Object dependency:allDependencies) {
				if (Model.getExtensionMechanismsHelper().hasStereotype(dependency,"appliedProfile")) {
					allAppliedProfiles.add(profilePackage);
					break;
				}
			}
		}
		addAllUniqueModelElementsFrom(availableStereotypes,paths,getApplicableStereotypes(modelElement,allAppliedProfiles));
	}
	private static Collection<Object>getApplicableStereotypes(Object modelElement,Collection<Object>allAppliedProfiles) {
		Collection<Object>ret = new ArrayList<Object>();
		for (Object profile:allAppliedProfiles) {
			for (Object stereotype:Model.getExtensionMechanismsHelper().getStereotypes(profile)) {
				if (Model.getExtensionMechanismsHelper().isValidStereotype(modelElement,stereotype)) {
					ret.add(stereotype);
				}
			}
		}
		return ret;
	}
	private static Collection<Object>getAllProfilePackages(Object model) {
		Collection col = Model.getModelManagementHelper().getAllModelElementsOfKind(model,Model.getMetaTypes().getPackage());
		Collection<Object>ret = new ArrayList<Object>();
		for (Object element:col) {
			if (Model.getFacade().isAPackage(element)&&Model.getExtensionMechanismsHelper().hasStereotype(element,"profile")) {
				ret.add(element);
			}
		}
		return ret;
	}
	private static void addAllUniqueModelElementsFrom(Set<Object>elements,Set<List>paths,Collection<Object>source) {
		for (Object obj:source) {
			List path = Model.getModelManagementHelper().getPathList(obj);
			if (!paths.contains(path)) {
				paths.add(path);
				elements.add(obj);
			}
		}
	}
	public static void dealWithStereotypes(Object element,StringBuilder stereotype,boolean removeCurrent) {
		if (stereotype == null) {
			dealWithStereotypes(element,(String) null,removeCurrent);
		}else {
			dealWithStereotypes(element,stereotype.toString(),removeCurrent);
		}
	}
	public static void dealWithStereotypes(Object umlobject,String stereotype,boolean full) {
		String token;
		MyTokenizer mst;
		Collection<String>stereotypes = new ArrayList<String>();
		if (stereotype != null) {
			mst = new MyTokenizer(stereotype," ,\\,");
			while (mst.hasMoreTokens()) {
				token = mst.nextToken();
				if (!",".equals(token)&&!" ".equals(token)) {
					stereotypes.add(token);
				}
			}
		}
		if (full) {
			Collection<Object>toBeRemoved = new ArrayList<Object>();
			for (Object stereo:Model.getFacade().getStereotypes(umlobject)) {
				String stereotypename = Model.getFacade().getName(stereo);
				if (stereotypename != null&&!stereotypes.contains(stereotypename)) {
					toBeRemoved.add(getStereotype(umlobject,stereotypename));
				}
			}
			for (Object o:toBeRemoved) {
				Model.getCoreHelper().removeStereotype(umlobject,o);
			}
		}
		for (String stereotypename:stereotypes) {
			if (!Model.getExtensionMechanismsHelper().hasStereotype(umlobject,stereotypename)) {
				Object umlstereo = getStereotype(umlobject,stereotypename);
				if (umlstereo != null) {
					Model.getCoreHelper().addStereotype(umlobject,umlstereo);
				}
			}
		}
	}
	private static Object getStereotype(Object obj,String name) {
		Object root = Model.getFacade().getModel(obj);
		Object stereo;
		stereo = findStereotypeContained(obj,root,name);
		if (stereo != null) {
			return stereo;
		}
		Project project = ProjectManager.getManager().getCurrentProject();
		stereo = project.getProfileConfiguration().findStereotypeForObject(name,obj);
		if (stereo != null) {
			return stereo;
		}
		if (root != null&&name.length() > 0) {
			stereo = Model.getExtensionMechanismsFactory().buildStereotype(obj,name,root);
		}
		return stereo;
	}
	private static Object findStereotype(final Object obj,final Object namespace,final String name) {
		Object ns = namespace;
		if (ns == null) {
			ns = Model.getFacade().getNamespace(obj);
			if (ns == null) {
				return null;
			}
		}
		Collection ownedElements = Model.getFacade().getOwnedElements(ns);
		for (Object element:ownedElements) {
			if (Model.getFacade().isAStereotype(element)&&name.equals(Model.getFacade().getName(element))) {
				return element;
			}
		}
		ns = Model.getFacade().getNamespace(ns);
		if (namespace != null) {
			return findStereotype(obj,ns,name);
		}
		return null;
	}
	private static Object findStereotypeContained(Object obj,Object root,String name) {
		Object stereo;
		if (root == null) {
			return null;
		}
		if (Model.getFacade().isAStereotype(root)&&name.equals(Model.getFacade().getName(root))) {
			if (Model.getExtensionMechanismsHelper().isValidStereotype(obj,root)) {
				return root;
			}
		}
		if (!Model.getFacade().isANamespace(root)) {
			return null;
		}
		Collection ownedElements = Model.getFacade().getOwnedElements(root);
		for (Object ownedElement:ownedElements) {
			stereo = findStereotypeContained(obj,ownedElement,name);
			if (stereo != null) {
				return stereo;
			}
		}
		return null;
	}
}



