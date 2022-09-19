package org.argouml.persistence;


public class SaveException extends PersistenceException {
	public SaveException(String message,Throwable cause) {
		super(message,cause);
	}
	public SaveException(String message) {
		super(message);
	}
	public SaveException(Throwable cause) {
		super(cause);
	}
}



