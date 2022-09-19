package org.argouml.model;


public class InvalidElementException extends RuntimeException {
	private static final long serialVersionUID = -5831736942969641257l;
	public InvalidElementException(String message) {
		super(message);
	}
	public InvalidElementException(String message,Throwable c) {
		super(message,c);
	}
	public InvalidElementException(Throwable c) {
		super(c);
	}
}



