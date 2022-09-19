package org.argouml.uml.diagram;

import org.argouml.kernel.Project;
import org.argouml.kernel.AbstractProjectMember;
import org.tigris.gef.util.Util;


public class ProjectMemberDiagram extends AbstractProjectMember {
	private static final String MEMBER_TYPE = "pgml";
	private static final String FILE_EXT = ".pgml";
	private ArgoDiagram diagram;
	public ProjectMemberDiagram(ArgoDiagram d,Project p) {
		super(Util.stripJunk(d.getName()),p);
		setDiagram(d);
	}
	public ArgoDiagram getDiagram() {
		return diagram;
	}
	public String getType() {
		return MEMBER_TYPE;
	}
	@Override public String getZipFileExtension() {
		return FILE_EXT;
	}
	protected void setDiagram(ArgoDiagram d) {
		diagram = d;
	}
	public String repair() {
		return diagram.repair();
	}
}



