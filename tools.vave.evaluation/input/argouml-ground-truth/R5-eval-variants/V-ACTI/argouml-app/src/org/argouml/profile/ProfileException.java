package org.argouml.profile;


public class ProfileException extends Exception {
	public ProfileException(String message) {
		super(message);
	}
	public ProfileException(String message,Throwable theCause) {
		super(message,theCause);
	}
	public ProfileException(Throwable theCause) {
		super(theCause);
	}
}



