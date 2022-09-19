package org.argouml.model.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.jmi.reflect.InvalidObjectException;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.UseCasesHelper;
import org.omg.uml.behavioralelements.usecases.Actor;
import org.omg.uml.behavioralelements.usecases.Extend;
import org.omg.uml.behavioralelements.usecases.ExtensionPoint;
import org.omg.uml.behavioralelements.usecases.Include;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.BooleanExpression;
import org.omg.uml.modelmanagement.Subsystem;


class UseCasesHelperMDRImpl implements UseCasesHelper {
	private MDRModelImplementation modelImpl;
	UseCasesHelperMDRImpl(MDRModelImplementation implementation) {
			modelImpl = implementation;
		}
	public Collection<UseCase>getAllUseCases(Object ns) {
		if (!(ns instanceof Namespace)) {
			throw new IllegalArgumentException();
		}
		List<UseCase>list = new ArrayList<UseCase>();
		try {
			for (Object o:((Namespace) ns).getOwnedElement()) {
				if (o instanceof Namespace) {
					list.addAll(getAllUseCases(o));
				}
				if (o instanceof UseCase) {
					list.add((UseCase) o);
				}
			}
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		return list;
	}
	public Collection<Actor>getAllActors(Object ns) {
		if (!(ns instanceof Namespace)) {
			throw new IllegalArgumentException();
		}
		List<Actor>list = new ArrayList<Actor>();
		try {
			for (Object o:((Namespace) ns).getOwnedElement()) {
				if (o instanceof Namespace) {
					list.addAll(getAllActors(o));
				}
				if (o instanceof Actor) {
					list.add((Actor) o);
				}
			}
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		return list;
	}
	public Collection<UseCase>getExtendedUseCases(Object ausecase) {
		if (ausecase == null) {
			return new ArrayList<UseCase>();
		}
		List<UseCase>list = new ArrayList<UseCase>();
		UseCase usecase = (UseCase) ausecase;
		try {
			for (Extend extend:usecase.getExtend()) {
				list.add(extend.getBase());
			}
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		return list;
	}
	public Extend getExtends(Object abase,Object anextension) {
		if (!(abase instanceof UseCase)||!(anextension instanceof UseCase)) {
			throw new IllegalArgumentException();
		}
		UseCase base = (UseCase) abase;
		UseCase extension = (UseCase) anextension;
		try {
			for (Extend extend:extension.getExtend()) {
				if (extend.getBase() == base) {
					return extend;
				}
			}
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		return null;
	}
	public Collection<UseCase>getIncludedUseCases(Object ausecase) {
		if (!(ausecase instanceof UseCase)) {
			throw new IllegalArgumentException();
		}
		List<UseCase>result = new ArrayList<UseCase>();
		UseCase usecase = (UseCase) ausecase;
		try {
			for (Include include:usecase.getInclude()) {
				UseCase addition = include.getBase();
				result.add(addition);
			}
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		return result;
	}
	public Include getIncludes(Object abase,Object aninclusion) {
		if (!(abase instanceof UseCase)||!(aninclusion instanceof UseCase)) {
			throw new IllegalArgumentException();
		}
		UseCase base = (UseCase) abase;
		UseCase inclusion = (UseCase) aninclusion;
		try {
			for (Include include:inclusion.getInclude()) {
				if (include.getBase() == base) {
					return include;
				}
			}
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		return null;
	}
	public Collection getSpecificationPath(Object ausecase) {
		UseCase uc = (UseCase) ausecase;
		Set set = new HashSet();
		try {
			set.addAll(modelImpl.getModelManagementHelper().getAllSurroundingNamespaces(uc));
			Set set2 = new HashSet();
			for (Object o:set) {
				if (o instanceof Subsystem||o instanceof UmlClass) {
					set2.add(o);
				}
			}
			return set2;
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
	}
	public void setBase(Object extend,Object base) {
		if (base == null) {
			throw new IllegalArgumentException("The base cannot be null");
		}
		if (!(base instanceof UseCase)) {
			throw new IllegalArgumentException("The base cannot be a " + base.getClass().getName());
		}
		if (extend == null) {
			throw new IllegalArgumentException("extend");
		}
		if (extend instanceof Extend) {
			Extend theExtend = ((Extend) extend);
			if (base == theExtend.getBase()) {
				return;
			}
			for (ExtensionPoint point:theExtend.getExtensionPoint()) {
				removeExtend(point,theExtend);
			}
			ExtensionPoint point = (ExtensionPoint) modelImpl.getUseCasesFactory().buildExtensionPoint(base);
			theExtend.setBase((UseCase) base);
			addExtensionPoint(theExtend,point);
		}else if (extend instanceof Include) {
			Include theInclude = ((Include) extend);
			if (base == theInclude.getBase()) {
				return;
			}
			theInclude.setAddition((UseCase) base);
		}else {
			throw new IllegalArgumentException();
		}
	}
	public void removeExtend(Object elem,Object extend) {
		try {
			if (elem instanceof UseCase&&extend instanceof Extend) {
				((UseCase) elem).getExtend().remove(extend);
				return;
			}
			if (elem instanceof ExtensionPoint&&extend instanceof Extend) {
				((Extend) extend).getExtensionPoint().remove(elem);
				return;
			}
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		throw new IllegalArgumentException("elem: " + elem + " or extend: " + extend);
	}
	public void removeExtensionPoint(Object elem,Object ep) {
		try {
			if (ep instanceof ExtensionPoint) {
				if (elem instanceof UseCase) {
					((UseCase) elem).getExtensionPoint().remove(ep);
					return;
				}
				if (elem instanceof Extend) {
					((Extend) elem).getExtensionPoint().remove(ep);
					return;
				}
			}
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		throw new IllegalArgumentException("elem: " + elem + " or ep: " + ep);
	}
	public void removeInclude(Object usecase,Object include) {
		try {
			if (usecase instanceof UseCase&&include instanceof Include) {
				((UseCase) usecase).getInclude().remove(include);
				return;
			}
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		throw new IllegalArgumentException("usecase: " + usecase + " or include: " + include);
	}
	public void addExtend(Object elem,Object extend) {
		if (elem instanceof UseCase&&extend instanceof Extend) {
			((UseCase) elem).getExtend().add((Extend) extend);
		}else if (elem instanceof ExtensionPoint&&extend instanceof Extend) {
			((Extend) extend).getExtensionPoint().add((ExtensionPoint) elem);
		}else {
			throw new IllegalArgumentException("elem: " + elem + " or extend: " + extend);
		}
	}
	public void addExtensionPoint(Object handle,Object extensionPoint) {
		if (extensionPoint instanceof ExtensionPoint) {
			if (handle instanceof UseCase) {
				((UseCase) handle).getExtensionPoint().add((ExtensionPoint) extensionPoint);
				return;
			}
			if (handle instanceof Extend) {
				((Extend) handle).getExtensionPoint().add((ExtensionPoint) extensionPoint);
				return;
			}
		}
		throw new IllegalArgumentException("handle: " + handle + " or extensionPoint: " + extensionPoint);
	}
	public void addExtensionPoint(Object handle,int position,Object extensionPoint) {
		if (extensionPoint instanceof ExtensionPoint) {
			if (handle instanceof Extend) {
				((Extend) handle).getExtensionPoint().add(position,(ExtensionPoint) extensionPoint);
				return;
			}
		}
		throw new IllegalArgumentException("handle: " + handle + " or extensionPoint: " + extensionPoint);
	}
	public void addInclude(Object usecase,Object include) {
		if (usecase instanceof UseCase&&include instanceof Include) {
			((UseCase) usecase).getInclude().add((Include) include);
			return;
		}
		throw new IllegalArgumentException("usecase: " + usecase + " or include: " + include);
	}
	public void setAddition(Object handle,Object useCase) {
		if (!(useCase instanceof UseCase)) {
			throw new IllegalArgumentException("A UseCase was expected [" + useCase + "]");
		}
		if (handle instanceof Include) {
			try {
				((Include) handle).setAddition((UseCase) useCase);
				return;
			}catch (InvalidObjectException e) {
				throw new IllegalArgumentException("Operation on a removed object [" + handle + " or " + useCase + "]");
			}
		}
		throw new IllegalArgumentException("handle: " + handle);
	}
	public void setCondition(Object handle,Object booleanExpression) {
		if (handle instanceof Extend&&booleanExpression instanceof BooleanExpression) {
			((Extend) handle).setCondition((BooleanExpression) booleanExpression);
			return;
		}
		throw new IllegalArgumentException("handle: " + handle + " or booleanExpression: " + booleanExpression);
	}
	public void setExtension(Object handle,Object useCase) {
		if (!(useCase instanceof UseCase)) {
			throw new IllegalArgumentException("A use case must be supplied");
		}
		if (handle instanceof Extend&&(useCase instanceof UseCase)) {
			try {
				((Extend) handle).setExtension((UseCase) useCase);
				return;
			}catch (InvalidObjectException e) {
				throw new IllegalStateException("Operation on a removed object [" + handle + " or " + useCase + "]");
			}
		}
		throw new IllegalArgumentException("handle: " + handle + " or ext: " + useCase);
	}
	public void setExtensionPoints(Object handle,Collection extensionPoints) {
		if (handle instanceof UseCase||handle instanceof Extend) {
			Collection<ExtensionPoint>eps = Model.getFacade().getExtensionPoints(handle);
			if (!eps.isEmpty()) {
				Collection<ExtensionPoint>extPts = new ArrayList<ExtensionPoint>(eps);
				for (ExtensionPoint ep:extPts) {
					removeExtensionPoint(handle,ep);
				}
			}
			for (Object ep:extensionPoints) {
				addExtensionPoint(handle,ep);
			}
			return;
		}
		throw new IllegalArgumentException("handle: " + handle + " or extensionPoints: " + extensionPoints);
	}
	public void setIncludes(Object handle,Collection includes) {
		if (handle instanceof UseCase) {
			Collection<Include>inc = Model.getFacade().getIncludes(handle);
			if (!inc.isEmpty()) {
				Collection<Include>in = new ArrayList<Include>(inc);
				for (Include i:in) {
					removeInclude(handle,i);
				}
			}
			for (Include i:(Collection<Include>) includes) {
				addInclude(handle,i);
			}
			return;
		}
		throw new IllegalArgumentException("handle: " + handle + " or includes: " + includes);
	}
	public void setLocation(Object handle,String loc) {
		if (handle instanceof ExtensionPoint) {
			((ExtensionPoint) handle).setLocation(loc);
			return;
		}
		throw new IllegalArgumentException("handle: " + handle);
	}
	public void setUseCase(Object elem,Object usecase) {
		if (elem instanceof ExtensionPoint&&(usecase instanceof UseCase||usecase == null)) {
			((ExtensionPoint) elem).setUseCase((UseCase) usecase);
			return;
		}
		throw new IllegalArgumentException("elem: " + elem + " or usecase: " + usecase);
	}
}



