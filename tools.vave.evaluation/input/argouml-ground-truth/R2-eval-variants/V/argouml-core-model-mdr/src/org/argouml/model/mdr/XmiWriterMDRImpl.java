package org.argouml.model.mdr;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;
import org.argouml.model.UmlException;
import org.argouml.model.XmiExtensionWriter;
import org.argouml.model.XmiWriter;
import org.netbeans.api.xmi.XMIWriter;
import org.netbeans.api.xmi.XMIWriterFactory;
import org.netbeans.lib.jmi.xmi.OutputConfig;


class XmiWriterMDRImpl implements XmiWriter {
	private MDRModelImplementation modelImpl;
	private Object model;
	private OutputConfig config;
	private Writer writer;
	private OutputStream oStream;
	private static final String ENCODING = "UTF-8";
	private static final String XMI_VERSION = "1.2";
	private XmiExtensionWriter xmiExtensionWriter;
	private static final char[]TARGET = "/XMI.content".toCharArray();
	private XmiWriterMDRImpl(MDRModelImplementation theParent,Object theModel,String version) {
		if (theModel == null) {
			throw new IllegalArgumentException("A model must be provided");
		}
		if (theParent == null) {
			throw new IllegalArgumentException("A parent must be provided");
		}
		this.modelImpl = theParent;
		this.model = theModel;
		config = new OutputConfig();
		config.setEncoding(ENCODING);
		config.setReferenceProvider(new XmiReferenceProviderImpl(modelImpl.getObjectToId()));
		config.setHeaderProvider(new XmiHeaderProviderImpl(version));
	}
	public XmiWriterMDRImpl(MDRModelImplementation theParent,Object theModel,OutputStream theStream,String version) {
		this(theParent,theModel,version);
		if (theStream == null) {
			throw new IllegalArgumentException("A writer must be provided");
		}
		oStream = theStream;
	}
	public void write()throws UmlException {
		XMIWriter xmiWriter = XMIWriterFactory.getDefault().createXMIWriter(config);
		try {
			modelImpl.getRepository().beginTrans(false);
			try {
				RefPackage extent = ((RefObject) model).refOutermostPackage();
				xmiWriter.write(oStream,"file:///ThisIsADummyName.xmi",extent,XMI_VERSION);
			}finally {
				modelImpl.getRepository().endTrans();
			}
		}catch (IOException e) {
			throw new UmlException(e);
		}
	}
	public void setXmiExtensionWriter(XmiExtensionWriter theWriter) {
		xmiExtensionWriter = theWriter;
	}
}



