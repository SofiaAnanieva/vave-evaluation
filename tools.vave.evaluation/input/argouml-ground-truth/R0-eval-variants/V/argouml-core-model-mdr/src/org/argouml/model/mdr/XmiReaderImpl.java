package org.argouml.model.mdr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.jmi.reflect.InvalidObjectException;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.argouml.model.UmlException;
import org.argouml.model.XmiException;
import org.argouml.model.XmiReader;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.netbeans.lib.jmi.xmi.InputConfig;
import org.netbeans.lib.jmi.xmi.UnknownElementsListener;
import org.netbeans.lib.jmi.xmi.XMIHeaderConsumer;
import org.omg.uml.UmlPackage;
import org.openide.ErrorManager;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;


class XmiReaderImpl implements XmiReader,UnknownElementsListener,XMIHeaderConsumer {
	private MDRModelImplementation modelImpl;
	private XmiReferenceResolverImpl resolver;
	private boolean unknownElement;
	private String unknownElementName;
	private boolean uml13;
	private String[]ignoredElements = new String[] {};
	private int ignoredElementCount;
	private String xmiHeader;
	XmiReaderImpl(MDRModelImplementation parentModelImplementation) {
			modelImpl = parentModelImplementation;
		}
	public Collection parse(InputSource inputSource,boolean readOnly)throws UmlException {
		Collection<RefObject>newElements = Collections.emptyList();
		String extentBase = inputSource.getSystemId();
		if (extentBase == null) {
			extentBase = inputSource.getPublicId();
		}
		if (extentBase == null) {
			extentBase = MDRModelImplementation.MODEL_EXTENT_NAME;
		}
		String extentName = extentBase;
		UmlPackage extent = (UmlPackage) modelImpl.getRepository().getExtent(extentName);
		int serial = 1;
		while (extent != null) {
			extentName = extentBase + " " + serial;
			serial++;
			extent = (UmlPackage) modelImpl.getRepository().getExtent(extentName);
		}
		extent = (UmlPackage) modelImpl.createExtent(extentName,readOnly);
		try {
			InputConfig config = new InputConfig();
			config.setUnknownElementsListener(this);
			config.setUnknownElementsIgnored(true);
			resolver = new XmiReferenceResolverImpl(new RefPackage[] {extent},config,modelImpl.getObjectToId(),modelImpl.getPublic2SystemIds(),modelImpl.getSearchPath(),readOnly,inputSource.getPublicId(),inputSource.getSystemId());
			config.setReferenceResolver(resolver);
			config.setHeaderConsumer(this);
			XMIReader xmiReader = XMIReaderFactory.getDefault().createXMIReader(config);
			InputConfig config2 = (InputConfig) xmiReader.getConfiguration();
			config2.setUnknownElementsListener(this);
			config2.setUnknownElementsIgnored(true);
			unknownElement = false;
			uml13 = false;
			ignoredElementCount = 0;
			modelImpl.getModelEventPump().stopPumpingEvents();
			try {
				String systemId = inputSource.getSystemId();
				File file = copySource(inputSource);
				systemId = file.toURI().toURL().toExternalForm();
				inputSource = new InputSource(systemId);
				MDRepository repository = modelImpl.getRepository();
				repository.beginTrans(true);
				newElements = xmiReader.read(inputSource.getByteStream(),systemId,extent);
				if (uml13) {
					repository.endTrans(true);
					repository.beginTrans(true);
					resolver.clearIdMaps();
					newElements = convertAndLoadUml13(inputSource.getSystemId(),extent,xmiReader,inputSource);
				}
				repository.endTrans();
			}catch (Throwable e) {
				try {
					modelImpl.getRepository().endTrans(true);
				}catch (Throwable e2) {
				}
				if (e instanceof MalformedXMIException) {
					throw(MalformedXMIException) e;
				}else if (e instanceof IOException) {
					throw(IOException) e;
				}else {
					e.printStackTrace();
					throw new MalformedXMIException();
				}
			}finally {
				modelImpl.getModelEventPump().startPumpingEvents();
			}
			if (unknownElement) {
				modelImpl.deleteExtent(extent);
				throw new XmiException("Unknown element in XMI file : " + unknownElementName);
			}
		}catch (MalformedXMIException e) {
			ErrorManager.
				Annotation[]annotations = ErrorManager.getDefault().findAnnotations(e);
			for (ErrorManager.Annotation annotation:annotations) {
				Throwable throwable = annotation.getStackTrace();
				if (throwable instanceof SAXParseException) {
					SAXParseException spe = (SAXParseException) throwable;
					throw new XmiException(spe.getMessage(),spe.getPublicId(),spe.getSystemId(),spe.getLineNumber(),spe.getColumnNumber(),e);
				}else if (throwable instanceof SAXException) {
					SAXException se = (SAXException) throwable;
					Exception e1 = se.getException();
					if (e1 instanceof org.argouml.model.mdr.XmiReferenceException) {
						String href = ((org.argouml.model.mdr.XmiReferenceException) e1).getReference();
						throw new org.argouml.model.XmiReferenceException(href,e);
					}
					throw new XmiException(se.getMessage(),se);
				}
			}
			modelImpl.deleteExtent(extent);
			throw new XmiException(e);
		}catch (IOException e) {
			try {
				modelImpl.deleteExtent(extent);
			}catch (InvalidObjectException e2) {
			}
			throw new XmiException(e);
		}
		return newElements;
	}
	private void deleteElements(Collection<RefObject>elements) {
		Collection<RefObject>toDelete = new ArrayList<RefObject>(elements);
		for (RefObject refObject:toDelete) {
			try {
				refObject.refDelete();
			}catch (InvalidObjectException e) {
			}
		}
	}
	private Collection<RefObject>convertAndLoadUml13(String systemId,RefPackage extent,XMIReader xmiReader,InputSource input)throws FileNotFoundException,UmlException,IOException,MalformedXMIException {
		final String[]transformFiles = new String[] {"NormalizeNSUML.xsl","uml13touml14.xsl"};
		unknownElement = false;
		InputSource xformedInput = serialTransform(transformFiles,input);
		return xmiReader.read(xformedInput.getByteStream(),xformedInput.getSystemId(),extent);
	}
	public Map<String,Object>getXMIUUIDToObjectMap() {
		if (resolver != null) {
			return resolver.getIdToObjectMap();
		}
		return null;
	}
	private static final String STYLE_PATH = "/org/argouml/model/mdr/conversions/";
	private InputSource chainedTransform(String[]styles,InputSource input)throws XmiException {
		SAXTransformerFactory stf = (SAXTransformerFactory) TransformerFactory.newInstance();
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();
			XMLReader last = parser.getXMLReader();
			for (int i = 0;i < styles.;i++) {
				String xsltFileName = STYLE_PATH + styles[i];
				URL xsltUrl = getClass().getResource(xsltFileName);
				if (xsltUrl == null) {
					throw new IOException("Error opening XSLT style sheet : " + xsltFileName);
				}
				StreamSource xsltStreamSource = new StreamSource(xsltUrl.openStream());
				xsltStreamSource.setSystemId(xsltUrl.toExternalForm());
				XMLFilter filter = stf.newXMLFilter(xsltStreamSource);
				filter.setParent(last);
				last = filter;
			}
			SAXSource transformSource = new SAXSource(last,input);
			File tmpFile = File.createTempFile("zargo_model_",".xmi");
			tmpFile.deleteOnExit();
			StreamResult result = new StreamResult(new FileOutputStream(tmpFile));
			Transformer transformer = stf.newTransformer();
			transformer.transform(transformSource,result);
			return new InputSource(new FileInputStream(tmpFile));
		}catch (SAXException e) {
			throw new XmiException(e);
		}catch (ParserConfigurationException e) {
			throw new XmiException(e);
		}catch (IOException e) {
			throw new XmiException(e);
		}catch (TransformerConfigurationException e) {
			throw new XmiException(e);
		}catch (TransformerException e) {
			throw new XmiException(e);
		}
	}
	private InputSource serialTransform(String[]styles,InputSource input)throws UmlException {
		SAXSource myInput = new SAXSource(input);
		SAXTransformerFactory stf = (SAXTransformerFactory) TransformerFactory.newInstance();
		try {
			for (int i = 0;i < styles.;i++) {
				String xsltFileName = STYLE_PATH + styles[i];
				URL xsltUrl = getClass().getResource(xsltFileName);
				if (xsltUrl == null) {
					throw new UmlException("Error opening XSLT style sheet : " + xsltFileName);
				}
				StreamSource xsltStreamSource = new StreamSource(xsltUrl.openStream());
				xsltStreamSource.setSystemId(xsltUrl.toExternalForm());
				File tmpOutFile = File.createTempFile("zargo_model_",".xmi");
				tmpOutFile.deleteOnExit();
				StreamResult result = new StreamResult(new FileOutputStream(tmpOutFile));
				Transformer transformer = stf.newTransformer(xsltStreamSource);
				transformer.transform(myInput,result);
				myInput = new SAXSource(new InputSource(new FileInputStream(tmpOutFile)));
			}
			return myInput.getInputSource();
		}catch (IOException e) {
			throw new UmlException(e);
		}catch (TransformerConfigurationException e) {
			throw new UmlException(e);
		}catch (TransformerException e) {
			throw new UmlException(e);
		}
	}
	private File copySource(InputSource input)throws IOException {
		byte[]buf = new byte[2048];
		int len;
		File tmpOutFile = File.createTempFile("zargo_model_",".xmi");
		tmpOutFile.deleteOnExit();
		FileOutputStream out = new FileOutputStream(tmpOutFile);
		String systemId = input.getSystemId();
		if (systemId != null) {
			input = new InputSource(new URL(systemId).openStream());
		}
		InputStream in = input.getByteStream();
		while ((len = in.read(buf)) >= 0) {
			out.write(buf,0,len);
		}
		out.close();
		return tmpOutFile;
	}
	private static final String UML_13_ELEMENTS[] =  {"TaggedValue.value","TaggedValue.tag","ModelElement.templateParameter2","ModelElement.templateParameter3","Classifier.structuralFeature","Classifier.parameter","AssociationEnd.type","Node.resident","ElementResidence.implementationLocation","TemplateParameter.modelElement","TemplateParameter.modelElement2","Constraint.constrainedElement2","UseCase.include2","StateMachine.subMachineState","ClassifierRole.message1","ClassifierRole.message2","Message.message3","Message.message4","ElementImport.modelElement","ModelElement.elementResidence","ModelElement.presentation","ModelElement.supplierDependency","ModelElement.templateParameter2","ModelElement.templateParameter3","ModelElement.binding","GeneralizableElement.specialization","Classifier.associationEnd","Classifier.participant","Operation.method","Stereotype.extendedElement","Stereotype.requiredTag","TaggedValue.stereotype","Signal.context","Signal.reception","Signal.sendAction","UseCase.include2","UseCase.extend2","ExtensionPoint.extend","Link.stimulus","Instance.attributeLink","Action.stimulus","Event.state","Event.transition","Transition.state","ClassifierRole.message1","ClassifierRole.message2","Message.message3","Message.message4","Action.state1","Action.state2","Action.state3","Instance.stimulus1","Instance.stimulus2","Instance.stimulus3"};
	public void elementFound(String name) {
		if (ignoredElements != null) {
			for (int i = 0;i < ignoredElements.;i++) {
				if (name.equals(ignoredElements[i])) {
					ignoredElementCount++;
					return;
				}
			}
		}
		if (name.startsWith("Foundation.")) {
			uml13 = true;
			return;
		}
		for (int i = 0;i < UML_13_ELEMENTS.;i++) {
			if (name.endsWith(UML_13_ELEMENTS[i])) {
				uml13 = true;
				return;
			}
		}
		unknownElement = true;
		if (unknownElementName == null) {
			unknownElementName = name;
		}
	}
	public boolean setIgnoredElements(String[]elementNames) {
		if (elementNames == null) {
			elementNames = new String[] {};
		}else {
			ignoredElements = elementNames;
		}
		return true;
	}
	public String[]getIgnoredElements() {
		return ignoredElements;
	}
	public int getIgnoredElementCount() {
		return ignoredElementCount;
	}
	public String getTagName() {
		return"XMI";
	}
	public void addSearchPath(String path) {
		modelImpl.addSearchPath(path);
	}
	public void removeSearchPath(String path) {
		modelImpl.removeSearchPath(path);
	}
	public List<String>getSearchPath() {
		return modelImpl.getSearchPath();
	}
	public void consumeHeader(InputStream stream) {
		try {
			int length = stream.available();
			byte[]bytes = new byte[length];
			stream.read(bytes,0,length);
			xmiHeader = new String(bytes);
		}catch (IOException e) {
		}
	}
	public String getHeader() {
		return xmiHeader;
	}
}



