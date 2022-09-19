package org.argouml.model.euml;

import org.argouml.model.CopyHelper;
import org.eclipse.uml2.uml.Element;
import org.argouml.model.euml.UMLUtil;
import org.argouml.model.euml.EUMLModelImplementation;


class CopyHelperEUMLImpl implements CopyHelper {
	private EUMLModelImplementation modelImpl;
	public CopyHelperEUMLImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public Element copy(Object source,Object destination) {
		if (!(source instanceof Element)||!(destination instanceof Element)) {
			throw new IllegalArgumentException("The source and destination must be instances of Element");
		}
		Element copiedElement = UMLUtil.copy(modelImpl,(Element) source,(Element) destination);
		if (copiedElement == null) {
			throw new UnsupportedOperationException("Could not copy " + source + " to destination " + destination);
		}
		return copiedElement;
	}
}



