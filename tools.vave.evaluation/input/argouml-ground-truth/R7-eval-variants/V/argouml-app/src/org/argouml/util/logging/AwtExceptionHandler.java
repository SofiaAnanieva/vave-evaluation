package org.argouml.util.logging;


public class AwtExceptionHandler {
	public void handle(Throwable t) {
	}
	public static void registerExceptionHandler() {
		System.setProperty("sun.awt.exception.handler",AwtExceptionHandler.class.getName());
	}
}



