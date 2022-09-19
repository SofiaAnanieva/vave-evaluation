package org.argouml.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


abstract class SAXParserBase extends DefaultHandler {
	public SAXParserBase() {
	}
	private static XMLElement[]elements = new XMLElement[100];
	private static int nElements = 0;
	private static XMLElement[]freeElements = new XMLElement[100];
	private static int nFreeElements = 0;
	private static boolean stats = true;
	private static long parseTime = 0;
	public void setStats(boolean s) {
		stats = s;
	}
	public boolean getStats() {
		return stats;
	}
	public long getParseTime() {
		return parseTime;
	}
	public void parse(Reader reader)throws SAXException {
		parse(new InputSource(reader));
	}
	public void parse(InputSource input)throws SAXException {
		long start,end;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);
		try {
			SAXParser parser = factory.newSAXParser();
			if (input.getSystemId() == null) {
				input.setSystemId(getJarResource("org.argouml.kernel.Project"));
			}
			start = System.currentTimeMillis();
			parser.parse(input,this);
			end = System.currentTimeMillis();
			parseTime = end - start;
		}catch (IOException e) {
			throw new SAXException(e);
		}catch (ParserConfigurationException e) {
			throw new SAXException(e);
		}
	}
	protected abstract void handleStartElement(XMLElement e)throws SAXException;
	protected abstract void handleEndElement(XMLElement e)throws SAXException;
	public void startElement(String uri,String localname,String name,Attributes atts)throws SAXException {
		if (isElementOfInterest(name)) {
			XMLElement element = createXmlElement(name,atts);
			elements[nElements++] = element;
			handleStartElement(element);
		}
	}
	private XMLElement createXmlElement(String name,Attributes atts) {
		if (nFreeElements == 0) {
			return new XMLElement(name,atts);
		}
		XMLElement e = freeElements[--nFreeElements];
		e.setName(name);
		e.setAttributes(atts);
		e.resetText();
		return e;
	}
	public void endElement(String uri,String localname,String name)throws SAXException {
		if (isElementOfInterest(name)) {
			XMLElement e = elements[--nElements];
			handleEndElement(e);
		}
	}
	protected boolean isElementOfInterest(String name) {
		return true;
	}
	public void characters(char[]ch,int start,int length)throws SAXException {
		elements[nElements - 1].addText(ch,start,length);
	}
	public InputSource resolveEntity(String publicId,String systemId)throws SAXException {
		try {
			URL testIt = new URL(systemId);
			InputSource s = new InputSource(testIt.openStream());
			return s;
		}catch (Exception e) {
			String dtdName = systemId.substring(systemId.lastIndexOf('/') + 1);
			String dtdPath = "/org/argouml/persistence/" + dtdName;
			InputStream is = SAXParserBase.class.getResourceAsStream(dtdPath);
			if (is == null) {
				try {
					is = new FileInputStream(dtdPath.substring(1));
				}catch (Exception ex) {
					throw new SAXException(e);
				}
			}
			return new InputSource(is);
		}
	}
	public String getJarResource(String cls) {
		String jarFile = "";
		String fileSep = System.getProperty("file.separator");
		String classFile = cls.replace('.',fileSep.charAt(0)) + ".class";
		ClassLoader thisClassLoader = this.getClass().getClassLoader();
		URL url = thisClassLoader.getResource(classFile);
		if (url != null) {
			String urlString = url.getFile();
			int idBegin = urlString.indexOf("file:");
			int idEnd = urlString.indexOf("!");
			if (idBegin > -1&&idEnd > -1&&idEnd > idBegin) {
				jarFile = urlString.substring(idBegin + 5,idEnd);
			}
		}
		return jarFile;
	}
	public void ignoreElement(XMLElement e) {
	}
	public void notImplemented(XMLElement e) {
	}
}



