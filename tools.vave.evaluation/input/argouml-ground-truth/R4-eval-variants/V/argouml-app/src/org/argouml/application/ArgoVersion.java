package org.argouml.application;

import org.argouml.application.helpers.ApplicationVersion;


final class ArgoVersion {
	private static final String VERSION = "0.28.1";
	private static final String STABLE_VERSION = "0.26";
	static void init() {
		ApplicationVersion.init(VERSION,STABLE_VERSION);
	}
	private ArgoVersion() {
	}
}



