package org.argouml.util;


public class JavaRuntimeUtility {
	public static boolean isJreSupported() {
		String javaVersion = System.getProperty("java.version","");
		return(!(javaVersion.startsWith("1.4")||javaVersion.startsWith("1.3")||javaVersion.startsWith("1.2")||javaVersion.startsWith("1.1")));
	}
	public static boolean isJre5() {
		String javaVersion = System.getProperty("java.version","");
		return(javaVersion.startsWith("1.5"));
	}
	public static String getJreVersion() {
		return System.getProperty("java.version","");
	}
}



