package org.argouml.persistence;


class PersistenceException extends Exception {
	public PersistenceException() {
		super();
	}
	public PersistenceException(String message) {
		super(message);
	}
	public PersistenceException(String message,Throwable c) {
		super(message,c);
	}
	public PersistenceException(Throwable c) {
		super(c);
	}
	private static final long serialVersionUID = 4626477344515962964l;
}



