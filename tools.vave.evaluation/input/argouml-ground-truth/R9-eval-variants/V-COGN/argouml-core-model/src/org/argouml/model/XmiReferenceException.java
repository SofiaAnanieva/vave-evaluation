package org.argouml.model;


public class XmiReferenceException extends XmiException {
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
	public XmiReferenceException(String message,String publicId,String systemId,int lineNumber,int columnNumber) {
		super(message,publicId,systemId,lineNumber,columnNumber);
	}
	public XmiReferenceException(String message,String publicId,String systemId,int lineNumber,int columnNumber,Exception e) {
		super(message,publicId,systemId,lineNumber,columnNumber,e);
	}
}



