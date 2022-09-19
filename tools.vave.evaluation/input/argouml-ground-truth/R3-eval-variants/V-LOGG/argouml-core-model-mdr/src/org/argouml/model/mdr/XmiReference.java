package org.argouml.model.mdr;


class XmiReference {
	private final String systemId;
	private final String xmiId;
	XmiReference(String system,String xmi) {
			systemId = system;
			xmiId = xmi;
		}
	String getSystemId() {
		return systemId;
	}
	String getXmiId() {
		return xmiId;
	}
}



