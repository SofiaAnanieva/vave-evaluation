package org.argouml.util;


public class ThreadUtils {
	public static void checkIfInterrupted()throws InterruptedException {
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
	}
}



