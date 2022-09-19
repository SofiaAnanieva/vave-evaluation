package org.argouml.kernel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import javax.swing.event.EventListenerList;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.model.ModelCommand;
import org.argouml.model.ModelCommandCreationObserver;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.kernel.Project;


public final class ProjectManager implements ModelCommandCreationObserver {
	@Deprecated public static final String CURRENT_PROJECT_PROPERTY_NAME = "currentProject";
	public static final String OPEN_PROJECTS_PROPERTY = "openProjects";
	private static ProjectManager instance = new ProjectManager();
	private static Project currentProject;
	private static LinkedList<Project>openProjects = new LinkedList<Project>();
	private boolean creatingCurrentProject;
	private Action saveAction;
	private EventListenerList listenerList = new EventListenerList();
	private PropertyChangeEvent event;
	public static ProjectManager getManager() {
		return instance;
	}
	private ProjectManager() {
		super();
		Model.setModelCommandCreationObserver(this);
	}
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listenerList.add(PropertyChangeListener.class,listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listenerList.remove(PropertyChangeListener.class,listener);
	}
	void firePropertyChanged(String propertyName,Object oldValue,Object newValue) {
		Object[]listeners = listenerList.getListenerList();
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			if (listeners[i] == PropertyChangeListener.class) {
				if (event == null) {
					event = new PropertyChangeEvent(this,propertyName,oldValue,newValue);
				}
				((PropertyChangeListener) listeners[i + 1]).propertyChange(event);
			}
		}
		event = null;
	}
	public void setCurrentProject(Project newProject) {
		Project oldProject = currentProject;
		currentProject = newProject;
		addProject(newProject);
		if (currentProject != null&&currentProject.getActiveDiagram() == null) {
			List<ArgoDiagram>diagrams = currentProject.getDiagramList();
			if (diagrams != null&&!diagrams.isEmpty()) {
				ArgoDiagram activeDiagram = diagrams.get(0);
				currentProject.setActiveDiagram(activeDiagram);
			}
		}
		notifyProjectAdded(newProject,oldProject);
	}
	private void notifyProjectAdded(Project newProject,Project oldProject) {
		firePropertyChanged(CURRENT_PROJECT_PROPERTY_NAME,oldProject,newProject);
		firePropertyChanged(OPEN_PROJECTS_PROPERTY,new Project[] {oldProject},new Project[] {newProject});
	}
	public Project getCurrentProject() {
		if (currentProject == null&&!creatingCurrentProject) {
			makeEmptyProject();
		}
		return currentProject;
	}
	public List<Project>getOpenProjects() {
		List<Project>result = new ArrayList<Project>();
		if (currentProject != null) {
			result.add(currentProject);
		}
		return result;
	}
	public Project makeEmptyProject() {
		return makeEmptyProject(true);
	}
	public Project makeEmptyProject(final boolean addDefaultDiagrams) {
		final Command cmd = new NonUndoableCommand() {
	@Override public Object execute() {
		Model.getPump().stopPumpingEvents();
		creatingCurrentProject = true;
		Project newProject = new ProjectImpl();
		createDefaultModel(newProject);
		if (addDefaultDiagrams) {
			createDefaultDiagrams(newProject);
		}
		creatingCurrentProject = false;
		setCurrentProject(newProject);
		Model.getPump().startPumpingEvents();
		return null;
	}
};
		cmd.execute();
		currentProject.getUndoManager().addCommand(cmd);
		setSaveEnabled(false);
		return currentProject;
	}
	private void createDefaultDiagrams(Project project) {
		Object model = project.getRoots().iterator().next();
		DiagramFactory df = DiagramFactory.getInstance();
		ArgoDiagram d = df.create(DiagramFactory.DiagramType.Class,model,project.getProjectSettings().getDefaultDiagramSettings());
		project.addMember(d);
		project.setActiveDiagram(d);
	}
	private void createDefaultModel(Project project) {
		Object model = Model.getModelManagementFactory().createModel();
		Model.getCoreHelper().setName(model,Translator.localize("misc.untitled-model"));
		Collection roots = new ArrayList();
		roots.add(model);
		project.setRoots(roots);
		project.setCurrentNamespace(model);
		project.addMember(model);
	}
	public void setSaveAction(Action save) {
		this.saveAction = save;
	}
	public boolean isSaveActionEnabled() {
		return this.saveAction.isEnabled();
	}
	public void setSaveEnabled(boolean newValue) {
		if (saveAction != null) {
			saveAction.setEnabled(newValue);
		}
	}
	private void addProject(Project newProject) {
		openProjects.addLast(newProject);
	}
	public void removeProject(Project oldProject) {
		openProjects.remove(oldProject);
		if (currentProject == oldProject) {
			if (openProjects.size() > 0) {
				currentProject = openProjects.getLast();
			}else {
				currentProject = null;
			}
		}
		oldProject.remove();
	}
	public Object execute(final ModelCommand command) {
		setSaveEnabled(true);
		AbstractCommand wrappedCommand = new AbstractCommand() {
	private ModelCommand modelCommand = command;
	public void undo() {
		modelCommand.undo();
	}
	public boolean isUndoable() {
		return modelCommand.isUndoable();
	}
	public boolean isRedoable() {
		return modelCommand.isRedoable();
	}
	public Object execute() {
		return modelCommand.execute();
	}
	public String toString() {
		return modelCommand.toString();
	}
};
		Project p = getCurrentProject();
		if (p != null) {
			return getCurrentProject().getUndoManager().execute(wrappedCommand);
		}else {
			return wrappedCommand.execute();
		}
	}
}



