package org.argouml.kernel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.argouml.uml.ProjectMemberModel;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.ProjectMemberDiagram;


class MemberList implements List<ProjectMember> {
	private AbstractProjectMember model;
	private List<ProjectMemberDiagram>diagramMembers = new ArrayList<ProjectMemberDiagram>(10);
	private AbstractProjectMember profileConfiguration;
	public MemberList() {
	}
	public synchronized boolean add(ProjectMember member) {
		if (member instanceof ProjectMemberModel) {
			model = (AbstractProjectMember) member;
			return true;
		}else if (member instanceof ProfileConfiguration) {
			profileConfiguration = (AbstractProjectMember) member;
			return true;
		}else if (member instanceof ProjectMemberDiagram) {
			return diagramMembers.add((ProjectMemberDiagram) member);
		}
		return false;
	}
	public synchronized boolean remove(Object member) {
		if (member instanceof ArgoDiagram) {
			return removeDiagram((ArgoDiagram) member);
		}
		((AbstractProjectMember) member).remove();
		if (model == member) {
			model = null;
			return true;
		}else if (profileConfiguration == member) {
			profileConfiguration = null;
			return true;
		}else {
			final boolean removed = diagramMembers.remove(member);
			return removed;
		}
	}
	public synchronized Iterator<ProjectMember>iterator() {
		return buildOrderedMemberList().iterator();
	}
	public synchronized ListIterator<ProjectMember>listIterator() {
		return buildOrderedMemberList().listIterator();
	}
	public synchronized ListIterator<ProjectMember>listIterator(int arg0) {
		return buildOrderedMemberList().listIterator(arg0);
	}
	private List<ProjectMember>buildOrderedMemberList() {
		List<ProjectMember>temp = new ArrayList<ProjectMember>(size());
		if (profileConfiguration != null) {
			temp.add(profileConfiguration);
		}
		if (model != null) {
			temp.add(model);
		}
		temp.addAll(diagramMembers);
		return temp;
	}
	private boolean removeDiagram(ArgoDiagram d) {
		for (ProjectMemberDiagram pmd:diagramMembers) {
			if (pmd.getDiagram() == d) {
				pmd.remove();
				diagramMembers.remove(pmd);
				return true;
			}
		}
		return false;
	}
	public synchronized int size() {
		int size = diagramMembers.size();
		if (model != null) {
			++
			size;
		}
		if (profileConfiguration != null) {
			++
			size;
		}
		return size;
	}
	public synchronized boolean contains(Object member) {
		if (model == member) {
			return true;
		}
		if (profileConfiguration == member) {
			return true;
		}
		return diagramMembers.contains(member);
	}
	public synchronized void clear() {
		if (model != null) {
			model.remove();
		}
		if (profileConfiguration != null) {
			profileConfiguration.remove();
		}
		Iterator membersIt = diagramMembers.iterator();
		while (membersIt.hasNext()) {
			((AbstractProjectMember) membersIt.next()).remove();
		}
		diagramMembers.clear();
	}
	public synchronized ProjectMember get(int i) {
		if (model != null) {
			if (i == 0) {
				return model;
			}
			--
			i;
		}
		if (i == diagramMembers.size()) {
			return profileConfiguration;
		}
		if (i == (diagramMembers.size() + 1)) {
			return profileConfiguration;
		}
		return diagramMembers.get(i);
	}
	public synchronized boolean isEmpty() {
		return size() == 0;
	}
	public synchronized ProjectMember[]toArray() {
		ProjectMember[]temp = new ProjectMember[size()];
		int pos = 0;
		if (model != null) {
			temp[pos++] = model;
		}
		for (ProjectMemberDiagram d:diagramMembers) {
			temp[pos++] = d;
		}
		if (profileConfiguration != null) {
			temp[pos++] = profileConfiguration;
		}
		return temp;
	}
	public<T>T[]toArray(T[]a) {
		throw new UnsupportedOperationException();
	}
	public boolean containsAll(Collection<?>arg0) {
		throw new UnsupportedOperationException();
	}
	public boolean addAll(Collection<?extends ProjectMember>arg0) {
		throw new UnsupportedOperationException();
	}
	public boolean addAll(int arg0,Collection<?extends ProjectMember>arg1) {
		throw new UnsupportedOperationException();
	}
	public boolean removeAll(Collection<?>arg0) {
		throw new UnsupportedOperationException();
	}
	public boolean retainAll(Collection<?>arg0) {
		throw new UnsupportedOperationException();
	}
	public ProjectMember set(int arg0,ProjectMember arg1) {
		throw new UnsupportedOperationException();
	}
	public void add(int arg0,ProjectMember arg1) {
		throw new UnsupportedOperationException();
	}
	public ProjectMember remove(int arg0) {
		throw new UnsupportedOperationException();
	}
	public int indexOf(Object arg0) {
		throw new UnsupportedOperationException();
	}
	public int lastIndexOf(Object arg0) {
		throw new UnsupportedOperationException();
	}
	public List<ProjectMember>subList(int arg0,int arg1) {
		throw new UnsupportedOperationException();
	}
}



