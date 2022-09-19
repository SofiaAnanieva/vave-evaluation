package org.argouml.uml.cognitive;

import java.util.List;
import java.util.Set;
import java.util.Vector;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ResolvedCritic;
import org.argouml.cognitive.ToDoItem;
import org.argouml.kernel.AbstractProjectMember;
import org.argouml.kernel.Project;
import org.argouml.persistence.ResolvedCriticXMLHelper;
import org.argouml.persistence.ToDoItemXMLHelper;


public class ProjectMemberTodoList extends AbstractProjectMember {
	private static final String TO_DO_EXT = ".todo";
	public ProjectMemberTodoList(String name,Project p) {
		super(name,p);
	}
	public String getType() {
		return"todo";
	}
	@Override public String getZipFileExtension() {
		return TO_DO_EXT;
	}
	public Vector<ToDoItemXMLHelper>getToDoList() {
		Vector<ToDoItemXMLHelper>out = new Vector<ToDoItemXMLHelper>();
		List<ToDoItem>tdiList = Designer.theDesigner().getToDoList().getToDoItemList();
		synchronized (tdiList) {
			for (ToDoItem tdi:tdiList) {
				if (tdi != null&&tdi.getPoster()instanceof Designer) {
					out.addElement(new ToDoItemXMLHelper(tdi));
				}
			}
		}
		return out;
	}
	public Vector<ResolvedCriticXMLHelper>getResolvedCriticsList() {
		Vector<ResolvedCriticXMLHelper>out = new Vector<ResolvedCriticXMLHelper>();
		Set<ResolvedCritic>resolvedSet = Designer.theDesigner().getToDoList().getResolvedItems();
		synchronized (resolvedSet) {
			for (ResolvedCritic rci:resolvedSet) {
				if (rci != null) {
					out.addElement(new ResolvedCriticXMLHelper(rci));
				}
			}
		}
		return out;
	}
	public String repair() {
		return"";
	}
}



