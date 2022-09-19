package org.argouml.model.mdr;


public class XmiReferenceException extends RuntimeException {
	private String reference;
	public XmiReferenceException(String message) {
		super(message);
	}
	public XmiReferenceException(String href,Throwable cause) {
		super(href,cause);
		reference = href;
	}
	public String getReference() {
		return reference;
	}
	public XmiReferenceException(Throwable c) {
		super(c);
	}
}



