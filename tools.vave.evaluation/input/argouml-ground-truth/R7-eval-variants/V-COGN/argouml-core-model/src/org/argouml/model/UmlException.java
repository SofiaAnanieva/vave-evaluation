package org.argouml.model;


public class UmlException extends Exception {
	public UmlException(String message) {
		super(message);
	}
	public UmlException(String message,Throwable c) {
		super(message,c);
	}
	public UmlException(Throwable c) {
		super(c);
	}
	private static final long serialVersionUID = -1029321716390822627l;
}



