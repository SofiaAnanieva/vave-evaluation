package org.argouml.kernel;

import java.net.URI;


public class ProjectFactory {
	private ProjectFactory() {
		super();
	}
	private static final ProjectFactory INSTANCE = new ProjectFactory();
	public Project createProject() {
		return new ProjectImpl();
	}
	public Project createProject(URI uri) {
		return new ProjectImpl(uri);
	}
	public static ProjectFactory getInstance() {
		return INSTANCE;
	}
}



