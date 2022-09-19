package org.argouml.util.logging;

import org.apache.log4j.Logger;


public class AwtExceptionHandler {
	private static final Logger LOG = Logger.getLogger(AwtExceptionHandler.class);
	public void handle(Throwable t) {
		try {
			LOG.error("Last chance error handler in AWT thread caught",t);
		}catch (Throwable t2) {
		}
	}
	public static void registerExceptionHandler() {
		System.setProperty("sun.awt.exception.handler",AwtExceptionHandler.class.getName());
	}
}



