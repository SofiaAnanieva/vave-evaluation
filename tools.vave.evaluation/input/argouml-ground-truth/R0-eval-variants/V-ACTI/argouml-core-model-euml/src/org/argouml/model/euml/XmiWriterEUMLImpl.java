package org.argouml.model.euml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.argouml.model.UmlException;
import org.argouml.model.XmiExtensionWriter;
import org.argouml.model.XmiWriter;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.argouml.model.euml.EUMLModelImplementation;
import org.argouml.model.euml.ModelEventPumpEUMLImpl;


class XmiWriterEUMLImpl implements XmiWriter {
	private EUMLModelImplementation modelImpl;
	private OutputStream oStream;
	private org.eclipse.uml2.uml.Package model;
	public XmiWriterEUMLImpl(EUMLModelImplementation implementation,Object theModel,OutputStream stream,String version) {
		if (stream == null) {
			throw new IllegalArgumentException("An OutputStream must be provided");
		}
		if (!(theModel instanceof org.eclipse.uml2.uml.Package)) {
			throw new IllegalArgumentException("A container must be provided" + " and it must be a UML 2 Package");
		}
		if (implementation == null) {
			throw new IllegalArgumentException("A parent must be provided");
		}
		modelImpl = implementation;
		model = (org.eclipse.uml2.uml.Package) theModel;
		oStream = stream;
	}
	public void write()throws UmlException {
		if (model.eResource() == null) {
			throw new UmlException("Root container is not affiliated with any resource!");
		}
		Map<String,Integer>options = new HashMap<String,Integer>();
		options.put(XMLResource.OPTION_LINE_WIDTH,100);
		try {
			modelImpl.getModelEventPump().stopPumpingEvents();
			model.eResource().save(oStream,options);
		}catch (IOException ioe) {
			throw new UmlException(ioe);
		}finally {
			modelImpl.getModelEventPump().startPumpingEvents();
		}
	}
	public void setXmiExtensionWriter(XmiExtensionWriter xmiExtensionWriter) {
		throw new NotYetImplementedException();
	}
}



