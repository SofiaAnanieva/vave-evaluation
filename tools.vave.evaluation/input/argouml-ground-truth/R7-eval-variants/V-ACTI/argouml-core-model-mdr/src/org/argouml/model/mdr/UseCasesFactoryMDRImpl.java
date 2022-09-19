package org.argouml.model.mdr;

import java.util.Collection;
import java.util.Iterator;
import org.argouml.model.UseCasesFactory;
import org.omg.uml.behavioralelements.usecases.Actor;
import org.omg.uml.behavioralelements.usecases.Extend;
import org.omg.uml.behavioralelements.usecases.ExtensionPoint;
import org.omg.uml.behavioralelements.usecases.Include;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.behavioralelements.usecases.UseCaseInstance;
import org.omg.uml.foundation.core.Namespace;


class UseCasesFactoryMDRImpl extends AbstractUmlModelFactoryMDR implements UseCasesFactory {
	private MDRModelImplementation modelImpl;
	UseCasesFactoryMDRImpl(MDRModelImplementation implementation) {
			modelImpl = implementation;
		}
	public Extend createExtend() {
		Extend myExtend = modelImpl.getUmlPackage().getUseCases().getExtend().createExtend();
		super.initialize(myExtend);
		return myExtend;
	}
	public ExtensionPoint createExtensionPoint() {
		ExtensionPoint myExtensionPoint = modelImpl.getUmlPackage().getUseCases().getExtensionPoint().createExtensionPoint();
		super.initialize(myExtensionPoint);
		return myExtensionPoint;
	}
	public Actor createActor() {
		Actor myActor = modelImpl.getUmlPackage().getUseCases().getActor().createActor();
		super.initialize(myActor);
		return myActor;
	}
	public Include createInclude() {
		Include myInclude = modelImpl.getUmlPackage().getUseCases().getInclude().createInclude();
		super.initialize(myInclude);
		return myInclude;
	}
	public UseCase createUseCase() {
		UseCase myUseCase = modelImpl.getUmlPackage().getUseCases().getUseCase().createUseCase();
		super.initialize(myUseCase);
		return myUseCase;
	}
	public Extend buildExtend(Object abase,Object anextension) {
		return buildExtend(abase,anextension,null);
	}
	public Extend buildExtend(Object abase,Object anextension,Object apoint) {
		UseCase base = (UseCase) abase;
		UseCase extension = (UseCase) anextension;
		ExtensionPoint point = (ExtensionPoint) apoint;
		if (base == null||extension == null) {
			throw new IllegalArgumentException("Either the base usecase or " + "the extension usecase is " + "null");
		}
		if (base.equals(extension)) {
			throw new IllegalArgumentException("The base usecase and " + "the extension usecase must be different");
		}
		if (point != null) {
			if (!base.getExtensionPoint().contains(point)) {
				throw new IllegalArgumentException("The extensionpoint is not " + "part of the base " + "usecase");
			}
		}else {
			point = buildExtensionPoint(base);
		}
		Extend extend = createExtend();
		extend.setBase(base);
		extend.setExtension(extension);
		extend.getExtensionPoint().add(point);
		return extend;
	}
	public ExtensionPoint buildExtensionPoint(Object modelElement) {
		if (!(modelElement instanceof UseCase)) {
			throw new IllegalArgumentException("An extension point can only " + "be built on a use case");
		}
		UseCase useCase = (UseCase) modelElement;
		ExtensionPoint extensionPoint = createExtensionPoint();
		extensionPoint.setUseCase(useCase);
		extensionPoint.setName("newEP");
		extensionPoint.setLocation("loc");
		return extensionPoint;
	}
	public Include buildInclude(Object abase,Object anaddition) {
		UseCase base = (UseCase) abase;
		UseCase addition = (UseCase) anaddition;
		Include include = createInclude();
		include.setAddition(addition);
		include.setBase(base);
		if (base.getNamespace() != null) {
			include.setNamespace(base.getNamespace());
		}else if (addition.getNamespace() != null) {
			include.setNamespace(addition.getNamespace());
		}
		return include;
	}
	private Actor buildActor(Namespace ns,Object model) {
		if (ns == null) {
			ns = (Namespace) model;
		}
		Actor actor = createActor();
		actor.setNamespace(ns);
		actor.setLeaf(false);
		actor.setRoot(false);
		return actor;
	}
	public Actor buildActor(Object actor,Object model) {
		if (actor instanceof Actor) {
			return buildActor(((Actor) actor).getNamespace(),model);
		}
		throw new IllegalArgumentException();
	}
	void deleteActor(Object elem) {
		if (!(elem instanceof Actor)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteExtend(Object elem) {
		if (!(elem instanceof Extend)) {
			throw new IllegalArgumentException();
		}
		modelImpl.getUmlHelper().deleteCollection(((Extend) elem).getExtensionPoint());
	}
	void deleteExtensionPoint(Object elem) {
		if (!(elem instanceof ExtensionPoint)) {
			throw new IllegalArgumentException();
		}
		ExtensionPoint ep = (ExtensionPoint) elem;
		Collection xtends = ((org.omg.uml.UmlPackage) ep.refOutermostPackage()).getUseCases().getAExtensionPointExtend().getExtend(ep);
		for (Iterator it = xtends.iterator();it.hasNext();) {
			Extend extend = (Extend) it.next();
			Collection eps = extend.getExtensionPoint();
			if (eps.size() == 1&&eps.contains(elem)) {
				modelImpl.getUmlFactory().delete(extend);
			}
		}
	}
	void deleteInclude(Object elem) {
		if (!(elem instanceof Include)) {
			throw new IllegalArgumentException();
		}
	}
	void deleteUseCase(Object elem) {
		if (!(elem instanceof UseCase)) {
			throw new IllegalArgumentException();
		}
		UseCase useCase = ((UseCase) elem);
		modelImpl.getUmlHelper().deleteCollection(useCase.getExtend());
		modelImpl.getUmlHelper().deleteCollection(useCase.getInclude());
		modelImpl.getUmlHelper().deleteCollection(((org.omg.uml.UmlPackage) useCase.refOutermostPackage()).getUseCases().getABaseExtender().getExtender(useCase));
		modelImpl.getUmlHelper().deleteCollection(((org.omg.uml.UmlPackage) useCase.refOutermostPackage()).getUseCases().getAIncluderAddition().getIncluder(useCase));
	}
	void deleteUseCaseInstance(Object elem) {
		if (!(elem instanceof UseCaseInstance)) {
			throw new IllegalArgumentException();
		}
	}
}



