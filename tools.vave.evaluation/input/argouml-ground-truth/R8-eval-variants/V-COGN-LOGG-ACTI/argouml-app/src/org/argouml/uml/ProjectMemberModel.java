package org.argouml.uml;

import org.argouml.kernel.AbstractProjectMember;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.persistence.PersistenceManager;


public class ProjectMemberModel extends AbstractProjectMember {
	private static final String MEMBER_TYPE = "xmi";
	private static final String FILE_EXT = "." + MEMBER_TYPE;
	private Object model;
	public ProjectMemberModel(Object m,Project p) {
		super(PersistenceManager.getInstance().getProjectBaseName(p) + FILE_EXT,p);
		if (!Model.getFacade().isAModel(m))throw new IllegalArgumentException();
		setModel(m);
	}
	public Object getModel() {
		return model;
	}
	protected void setModel(Object m) {
		model = m;
	}
	public String getType() {
		return MEMBER_TYPE;
	}
	public String getZipFileExtension() {
		return FILE_EXT;
	}
	public String repair() {
		return"";
	}
}



