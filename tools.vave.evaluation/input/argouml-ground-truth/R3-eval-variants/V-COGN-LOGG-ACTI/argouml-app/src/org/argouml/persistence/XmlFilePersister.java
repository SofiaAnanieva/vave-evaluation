package org.argouml.persistence;


class XmlFilePersister extends XmiFilePersister {
	@Override public String getExtension() {
		return"xml";
	}
	@Override public boolean hasAnIcon() {
		return false;
	}
}



