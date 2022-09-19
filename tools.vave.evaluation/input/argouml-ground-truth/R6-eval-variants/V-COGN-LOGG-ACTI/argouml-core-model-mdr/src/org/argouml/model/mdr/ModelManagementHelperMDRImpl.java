package org.argouml.model.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.jmi.reflect.InvalidObjectException;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;
import org.apache.log4j.Logger;
import org.argouml.model.InvalidElementException;
import org.argouml.model.ModelManagementHelper;
import org.omg.uml.behavioralelements.collaborations.Collaboration;
import org.omg.uml.behavioralelements.commonbehavior.Instance;
import org.omg.uml.foundation.core.BehavioralFeature;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Permission;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.ElementImport;
import org.omg.uml.modelmanagement.Subsystem;
import org.omg.uml.modelmanagement.UmlPackage;


class ModelManagementHelperMDRImpl implements ModelManagementHelper {
	private static final Logger LOG = Logger.getLogger(ModelManagementHelperMDRImpl.class);
	private MDRModelImplementation modelImpl;
	ModelManagementHelperMDRImpl(MDRModelImplementation implementation) {
			modelImpl = implementation;
		}
	public Collection getAllSubSystems(Object ns) {
		if (ns == null) {
			return new ArrayList();
		}
		if (!(ns instanceof Namespace)) {
			throw new IllegalArgumentException();
		}
		Iterator it = ((Namespace) ns).getOwnedElement().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof Namespace) {
				list.addAll(getAllSubSystems(o));
			}
			if (o instanceof Subsystem) {
				list.add(o);
			}
		}
		return list;
	}
	public Collection getAllNamespaces(Object ns) {
		if (ns == null||!(ns instanceof Namespace)) {
			return Collections.EMPTY_LIST;
		}
		Collection namespaces = ((Namespace) ns).getOwnedElement();
		List list = Collections.EMPTY_LIST;
		if (namespaces == Collections.EMPTY_LIST||namespaces.size() == 0) {
			return Collections.EMPTY_LIST;
		}
		for (Iterator it = namespaces.iterator();it.hasNext();) {
			Object o = it.next();
			if (o instanceof Namespace) {
				if (list == Collections.EMPTY_LIST) {
					list = new ArrayList(namespaces.size());
				}
				list.add(o);
				Collection namespaces1 = getAllNamespaces(o);
				if (namespaces1 != Collections.EMPTY_LIST&&namespaces1.size() > 0) {
					list.addAll(namespaces1);
				}
			}
		}
		return list;
	}
	public Collection getAllModelElementsOfKindWithModel(Object model,Object type) {
		if (model == null) {
			throw new IllegalArgumentException("A model must be supplied");
		}
		Class kind = (Class) type;
		Collection ret = getAllModelElementsOfKind(model,kind);
		if (kind.isAssignableFrom(model.getClass())&&!ret.contains(model)) {
			ret = new ArrayList(ret);
			ret.add(model);
		}
		return ret;
	}
	public Collection getAllModelElementsOfKind(Object nsa,Object type) {
		long startTime = System.currentTimeMillis();
		if (nsa == null||type == null) {
			return Collections.EMPTY_LIST;
		}
		if (type instanceof String) {
			return getAllModelElementsOfKind(nsa,(String) type);
		}
		if (!(nsa instanceof Namespace)||!(type instanceof Class)) {
			throw new IllegalArgumentException("illegal argument - namespace: " + nsa + " type: " + type);
		}
		String name = ((Class) type).getName();
		name = name.substring(name.lastIndexOf(".") + 1);
		if (name.startsWith("Uml")) {
			name = name.substring(3);
		}
		Collection allOfType = Collections.emptySet();
		try {
			RefPackage extent = ((RefObject) nsa).refOutermostPackage();
			RefClass classProxy = ((FacadeMDRImpl) modelImpl.getFacade()).getProxy(name,extent);
			allOfType = classProxy.refAllOfType();
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		Collection returnElements = new ArrayList();
		for (Iterator i = allOfType.iterator();i.hasNext();) {
			Object me = i.next();
			if (contained(nsa,me)) {
				returnElements.add(me);
			}
		}
		long duration = System.currentTimeMillis() - startTime;
		LOG.debug("Get allOfKind took " + duration + " msec.");
		return returnElements;
	}
	private boolean contained(Object container,Object candidate) {
		Object current = ((RefObject) candidate).refImmediateComposite();
		while (current != null) {
			if (container.equals(current)) {
				return true;
			}
			current = ((RefObject) current).refImmediateComposite();
		}
		return false;
	}
	public Collection getAllModelElementsOfKind(Object nsa,String kind) {
		if (nsa == null||kind == null) {
			return Collections.EMPTY_LIST;
		}
		if (!(nsa instanceof Namespace)) {
			throw new IllegalArgumentException("given argument " + nsa + " is not a namespace");
		}
		Collection col = null;
		try {
			col = getAllModelElementsOfKind(nsa,Class.forName(kind));
		}catch (ClassNotFoundException cnfe) {
			throw new IllegalArgumentException("Can\'t derive a class name from " + kind);
		}
		return col;
	}
	public Collection getAllSurroundingNamespaces(Object ns) {
		if (!(ns instanceof Namespace)) {
			throw new IllegalArgumentException();
		}
		Set set = new HashSet();
		set.add(ns);
		Namespace namespace = ((Namespace) ns);
		if (namespace.getNamespace() != null) {
			set.addAll(getAllSurroundingNamespaces(namespace.getNamespace()));
		}
		return set;
	}
	public Collection getAllBehavioralFeatures(Object ns) {
		ArrayList features = new ArrayList();
		try {
			Collection classifiers = getAllModelElementsOfKind(ns,modelImpl.getMetaTypes().getClassifier());
			Iterator i = classifiers.iterator();
			while (i.hasNext()) {
				features.addAll(modelImpl.getFacade().getFeatures(i.next()));
			}
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		ArrayList behavioralfeatures = new ArrayList();
		Iterator ii = features.iterator();
		while (ii.hasNext()) {
			Object f = ii.next();
			if (f instanceof BehavioralFeature) {
				behavioralfeatures.add(f);
			}
		}
		return behavioralfeatures;
	}
	public Collection getAllPossibleImports(Object pack) {
		Object container = pack;
		Object cc = modelImpl.getFacade().getModelElementContainer(pack);
		while (cc != null) {
			container = cc;
			cc = modelImpl.getFacade().getModelElementContainer(cc);
		}
		Collection mes = getAllModelElementsOfKind(container,modelImpl.getMetaTypes().getModelElement());
		Collection vmes = new ArrayList();
		Iterator i = mes.iterator();
		while (i.hasNext()) {
			Object me = i.next();
			if (modelImpl.getCoreHelper().isValidNamespace(me,pack)) {
				vmes.add(me);
			}
		}
		return vmes;
	}
	public Object getElement(List<String>path,Object theRootNamespace) {
		ModelElement root = (ModelElement) theRootNamespace;
		if (root == null) {
			return getElement(path);
		}else {
			for (int i = 0;i < path.size();i++) {
				if (root == null||!(root instanceof Namespace)) {
					return null;
				}
				String name = path.get(i);
				boolean found = false;
				for (ModelElement me:((Namespace) root).getOwnedElement()) {
					if (i < path.size() - 1&&!(me instanceof Namespace)) {
						continue;
					}
					if (name.equals(me.getName())) {
						root = me;
						found = true;
						break;
					}
				}
				if (!found) {
					return null;
				}
			}
			return root;
		}
	}
	public Object getElement(List<String>fullPath) {
		if (fullPath == null||fullPath.isEmpty()) {
			return null;
		}
		Object element = null;
		for (Object root:modelImpl.getFacade().getRootElements()) {
			if (((ModelElement) root).getName().equals(fullPath.get(0))) {
				element = root;
				if (root instanceof Namespace&&fullPath.size() > 1) {
					element = modelImpl.getModelManagementHelper().getElement(fullPath.subList(1,fullPath.size()),root);
				}
				if (element != null) {
					break;
				}
			}
		}
		return element;
	}
	public List<String>getPathList(Object element) {
		if (element == null) {
			return new ArrayList<String>();
		}
		if (!(element instanceof RefObject)) {
			throw new IllegalArgumentException();
		}
		List<String>path = getPathList(((RefObject) element).refImmediateComposite());
		path.add(modelImpl.getFacade().getName(element));
		return path;
	}
	public boolean isCyclicOwnership(Object parent,Object child) {
		return(getOwnerShipPath(parent).contains(child)||parent == child);
	}
	private List getOwnerShipPath(Object elem) {
		if (elem instanceof ModelElement) {
			List ownershipPath = new ArrayList();
			Object parent = modelImpl.getFacade().getModelElementContainer(elem);
			while (parent != null) {
				ownershipPath.add(parent);
				parent = modelImpl.getFacade().getModelElementContainer(parent);
			}
			return ownershipPath;
		}
		throw new IllegalArgumentException("Not a base");
	}
	public void removeImportedElement(Object pack,Object me) {
		try {
			if (pack instanceof UmlPackage&&me instanceof ModelElement) {
				Collection c = ((UmlPackage) pack).getElementImport();
				ElementImport match = null;
				Iterator it = c.iterator();
				while (it.hasNext()) {
					ElementImport ei = (ElementImport) it.next();
					if (ei.getImportedElement() == me) {
						match = ei;
						break;
					}
				}
				if (match != null)c.remove(match);
				return;
			}
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		throw new IllegalArgumentException("There must be a Package and a ModelElement");
	}
	public void setImportedElements(Object pack,Collection imports) {
		if (pack instanceof UmlPackage) {
			Collection eis = ((UmlPackage) pack).getElementImport();
			Collection toRemove = new ArrayList();
			Collection toAdd = new ArrayList(imports);
			Iterator i = eis.iterator();
			while (i.hasNext()) {
				ElementImport ei = (ElementImport) i.next();
				if (imports.contains(ei.getImportedElement())) {
					toAdd.remove(ei);
				}else {
					toRemove.add(ei);
				}
			}
			eis.removeAll(toRemove);
			Collection toAddEIs = new ArrayList();
			i = toAdd.iterator();
			while (i.hasNext()) {
				ModelElement me = (ModelElement) i.next();
				toAddEIs.add(modelImpl.getModelManagementFactory().buildElementImport(pack,me));
			}
			eis.addAll(toAddEIs);
			return;
		}
		throw new IllegalArgumentException("There must be a Package and a ModelElement");
	}
	public void setAlias(Object handle,String alias) {
		if ((handle instanceof ElementImport)&&(alias != null)) {
			((ElementImport) handle).setAlias(alias);
			return;
		}
		throw new IllegalArgumentException("handle: " + handle + " or alias: " + alias);
	}
	public void setSpecification(Object handle,boolean specification) {
		if (handle instanceof ElementImport) {
			((ElementImport) handle).setSpecification(specification);
			return;
		}
		throw new IllegalArgumentException("handle: " + handle);
	}
	public Collection<ModelElement>getContents(Object modelelement) {
		if (modelelement instanceof UmlPackage) {
			return getContents((UmlPackage) modelelement);
		}else if (modelelement instanceof Namespace) {
			return getContents((Namespace) modelelement);
		}else if (modelelement instanceof Instance) {
			return getContents((Instance) modelelement);
		}else if (modelelement == null) {
			return Collections.emptySet();
		}
		throw new IllegalArgumentException("Unsupported element type " + modelelement);
	}
	private void getContents(final Collection<ModelElement>results,final Object modelelement) {
		if (modelelement instanceof UmlPackage) {
			getContents(results,(UmlPackage) modelelement);
			return;
		}else if (modelelement instanceof Namespace) {
			getContents(results,(Namespace) modelelement);
			return;
		}else if (modelelement instanceof Instance) {
			getContents(results,(Instance) modelelement);
			return;
		}else if (modelelement == null) {
			return;
		}
		throw new IllegalArgumentException("Unsupported element type " + modelelement);
	}
	static Collection<ModelElement>getContents(UmlPackage pkg) {
		Collection<ModelElement>results = new ArrayList<ModelElement>();
		Collection<ElementImport>c = pkg.getElementImport();
		for (ElementImport ei:c) {
			results.add(ei.getImportedElement());
		}
		results.addAll(getContents((Namespace) pkg));
		return results;
	}
	private static void getContents(final Collection<ModelElement>results,final UmlPackage pkg) {
		Collection<ElementImport>c = pkg.getElementImport();
		for (ElementImport ei:c) {
			results.add(ei.getImportedElement());
		}
		getContents(results,(Namespace) pkg);
	}
	static Collection<ModelElement>getContents(Namespace namespace) {
		Collection<ModelElement>results = new ArrayList<ModelElement>();
		results.addAll(namespace.getOwnedElement());
		Namespace owner = namespace.getNamespace();
		if (owner != null) {
			results.addAll(getContents(owner));
		}
		return results;
	}
	private static void getContents(final Collection<ModelElement>results,final Namespace namespace) {
		results.addAll(namespace.getOwnedElement());
		Namespace owner = namespace.getNamespace();
		if (owner != null) {
			getContents(results,owner);
		}
	}
	static Collection<ModelElement>getContents(Instance instance) {
		Collection<ModelElement>results = new ArrayList<ModelElement>();
		results.addAll(instance.getOwnedInstance());
		results.addAll(instance.getOwnedLink());
		return results;
	}
	private static void getContents(final Collection<ModelElement>results,final Instance instance) {
		results.addAll(instance.getOwnedInstance());
		results.addAll(instance.getOwnedLink());
	}
	public Collection<ModelElement>getAllImportedElements(Object pack) {
		if (!(pack instanceof Namespace)) {
			return Collections.emptyList();
		}
		Collection<ModelElement>ret = new ArrayList<ModelElement>();
		getAllImportedElements(ret,pack);
		return ret;
	}
	private void getAllImportedElements(final Collection<ModelElement>results,final Object pack) {
		if (!(pack instanceof Namespace)) {
			return;
		}
		Namespace ns = ((Namespace) pack);
		try {
			Collection<Dependency>deps = ns.getClientDependency();
			for (Dependency dep:deps) {
				if (dep instanceof Permission) {
					if (modelImpl.getExtensionMechanismsHelper().hasStereotype(dep,FRIEND_STEREOTYPE)) {
						for (ModelElement o:dep.getSupplier()) {
							if (o instanceof Namespace) {
								results.addAll(((Namespace) o).getOwnedElement());
							}
						}
					}else if (modelImpl.getExtensionMechanismsHelper().hasStereotype(dep,IMPORT_STEREOTYPE)||modelImpl.getExtensionMechanismsHelper().hasStereotype(dep,ACCESS_STEREOTYPE)) {
						for (ModelElement o:dep.getSupplier()) {
							if (o instanceof Namespace) {
								results.addAll(CoreHelperMDRImpl.getAllVisibleElements((Namespace) o));
							}
						}
					}
				}
			}
			Collection imports = modelImpl.getFacade().getImportedElements(ns);
			results.addAll(imports);
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
	}
	public Collection<ModelElement>getAllContents(Object pack) {
		Set<ModelElement>results = new HashSet<ModelElement>(2000);
		Set<ModelElement>dupCheck = new HashSet<ModelElement>(2000);
		getAllContents(results,(ModelElement) pack,dupCheck);
		return results;
	}
	void getAllContents(final Collection<ModelElement>results,final ModelElement pack,final Collection<ModelElement>dupCheck) {
		if (pack == null||dupCheck.contains(pack)) {
			return;
		}
		dupCheck.add(pack);
		try {
			if (pack instanceof Namespace) {
				getContents(results,pack);
			}
			if (pack instanceof Classifier||pack instanceof UmlPackage) {
				Collection<GeneralizableElement>parents = CoreHelperMDRImpl.getParents((GeneralizableElement) pack);
				Set<ModelElement>allContents = new HashSet<ModelElement>(2000);
				for (GeneralizableElement parent:parents) {
					getAllContents(allContents,parent,dupCheck);
				}
				if (pack instanceof UmlPackage) {
					getAllImportedElements(allContents,pack);
					for (GeneralizableElement parent:parents) {
						getAllImportedElements(allContents,parent);
					}
				}
				for (ModelElement element:allContents) {
					if (VisibilityKindEnum.VK_PUBLIC.equals(element.getVisibility())||VisibilityKindEnum.VK_PROTECTED.equals(element.getVisibility())) {
						results.add(element);
					}
				}
			}
			if (pack instanceof Collaboration) {
				LOG.debug("Not implemented - getAllContents for: " + pack);
			}
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
	}
	public boolean isReadOnly(Object element) {
		try {
			RefPackage extent = ((RefObject) element).refOutermostPackage();
			return modelImpl.isReadOnly(extent);
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
	}
}



