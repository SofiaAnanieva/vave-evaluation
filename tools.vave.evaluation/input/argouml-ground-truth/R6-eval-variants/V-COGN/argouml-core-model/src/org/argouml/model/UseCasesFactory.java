package org.argouml.model;


public interface UseCasesFactory extends Factory {
	Object createExtend();
	Object createExtensionPoint();
	Object createActor();
	Object createInclude();
	Object createUseCase();
	Object buildExtend(Object abase,Object anextension);
	Object buildExtend(Object abase,Object anextension,Object apoint);
	Object buildExtensionPoint(Object modelElement);
	Object buildInclude(Object abase,Object anaddition);
	Object buildActor(Object actor,Object model);
}



