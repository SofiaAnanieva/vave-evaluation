package org.argouml.persistence;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.xml.sax.SAXException;


public class OpenException extends PersistenceException {
	public OpenException(String message) {
		super(message);
	}
	public OpenException(String message,Throwable cause) {
		super(message,cause);
	}
	public OpenException(Throwable cause) {
		super(cause);
	}
	public void printStackTrace() {
		super.printStackTrace();
		if (getCause()instanceof SAXException&&((SAXException) getCause()).getException() != null) {
			((SAXException) getCause()).getException().printStackTrace();
		}
	}
	public void printStackTrace(PrintStream ps) {
		super.printStackTrace(ps);
		if (getCause()instanceof SAXException&&((SAXException) getCause()).getException() != null) {
			((SAXException) getCause()).getException().printStackTrace(ps);
		}
	}
	public void printStackTrace(PrintWriter pw) {
		super.printStackTrace(pw);
		if (getCause()instanceof SAXException&&((SAXException) getCause()).getException() != null) {
			((SAXException) getCause()).getException().printStackTrace(pw);
		}
	}
	private static final long serialVersionUID = -4787911270548948677l;
}



