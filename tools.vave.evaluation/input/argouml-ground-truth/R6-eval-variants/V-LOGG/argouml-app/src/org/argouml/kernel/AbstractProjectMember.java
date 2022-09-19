package org.argouml.kernel;

import org.argouml.persistence.PersistenceManager;


public abstract class AbstractProjectMember implements ProjectMember {
	private String uniqueName;
	private Project project = null;
	public AbstractProjectMember(String theUniqueName,Project theProject) {
		project = theProject;
		makeUniqueName(theUniqueName);
	}
	public String getUniqueDiagramName() {
		String s = uniqueName;
		if (s != null) {
			if (!s.endsWith(getZipFileExtension())) {
				s += getZipFileExtension();
			}
		}
		return s;
	}
	public String getZipName() {
		if (uniqueName == null) {
			return null;
		}
		String s = PersistenceManager.getInstance().getProjectBaseName(project);
		if (uniqueName.length() > 0) {
			s += "_" + uniqueName;
		}
		if (!s.endsWith(getZipFileExtension())) {
			s += getZipFileExtension();
		}
		return s;
	}
	protected void makeUniqueName(String s) {
		uniqueName = s;
		if (uniqueName == null) {
			return;
		}
		String pbn = PersistenceManager.getInstance().getProjectBaseName(project);
		if (uniqueName.startsWith(pbn)) {
			uniqueName = uniqueName.substring(pbn.length());
			int i = 0;
			for (;i < uniqueName.length();i++) {
				if (uniqueName.charAt(i) != '_') {
					break;
				}
			}
			if (i > 0) {
				uniqueName = uniqueName.substring(i);
			}
		}
		if (uniqueName.endsWith(getZipFileExtension())) {
			uniqueName = uniqueName.substring(0,uniqueName.length() - getZipFileExtension().length());
		}
	}
	public abstract String getType();
	public String getZipFileExtension() {
		return"." + getType();
	}
	protected void remove() {
		uniqueName = null;
		project = null;
	}
	public String toString() {
		return getZipName();
	}
}



