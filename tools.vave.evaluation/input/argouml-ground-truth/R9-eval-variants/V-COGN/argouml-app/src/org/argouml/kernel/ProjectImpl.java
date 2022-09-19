package org.argouml.kernel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeSupport;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileFacade;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.ProjectMemberModel;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.tigris.gef.presentation.Fig;


public class ProjectImpl implements java.io.Serializable,Project {
	private static final String UNTITLED_FILE = Translator.localize("label.projectbrowser-title");
	static final long serialVersionUID = 1399111233978692444l;
	private URI uri;
	private String authorname;
	private String authoremail;
	private String description;
	private String version;
	private ProjectSettings projectSettings;
	private final List<String>searchpath = new ArrayList<String>();
	private final List<ProjectMember>members = new MemberList();
	private String historyFile;
	private int persistenceVersion;
	private final List models = new ArrayList();
	private Object root;
	private final Collection roots = new HashSet();
	private final List<ArgoDiagram>diagrams = new ArrayList<ArgoDiagram>();
	private Object currentNamespace;
	private Map<String,Object>uuidRefs;
	private transient VetoableChangeSupport vetoSupport;
	private ProfileConfiguration profileConfiguration;
	private ArgoDiagram activeDiagram;
	private String savedDiagramName;
	private HashMap<String,Object>defaultModelTypeCache;
	private final Collection trashcan = new ArrayList();
	private UndoManager undoManager = DefaultUndoManager.getInstance();
	private boolean dirty = false;
	public ProjectImpl(URI theProjectUri) {
		this();
		uri = theProjectUri;
	}
	public ProjectImpl() {
		setProfileConfiguration(new ProfileConfiguration(this));
		projectSettings = new ProjectSettings();
		Model.getModelManagementFactory().setRootModel(null);
		authorname = Configuration.getString(Argo.KEY_USER_FULLNAME);
		authoremail = Configuration.getString(Argo.KEY_USER_EMAIL);
		description = "";
		version = ApplicationVersion.getVersion();
		historyFile = "";
		defaultModelTypeCache = new HashMap<String,Object>();
		addSearchPath("PROJECT_DIR");
	}
	public String getName() {
		if (uri == null) {
			return UNTITLED_FILE;
		}
		return new File(uri).getName();
	}
	public URI getUri() {
		return uri;
	}
	public URI getURI() {
		return uri;
	}
	public void setUri(URI theUri) {
		uri = theUri;
	}
	public void setFile(final File file) {
		URI theProjectUri = file.toURI();
		uri = theProjectUri;
	}
	public List<String>getSearchPathList() {
		return Collections.unmodifiableList(searchpath);
	}
	public void addSearchPath(final String searchPathElement) {
		if (!searchpath.contains(searchPathElement)) {
			searchpath.add(searchPathElement);
		}
	}
	public List<ProjectMember>getMembers() {
		return members;
	}
	private void addDiagramMember(ArgoDiagram d) {
		int serial = getDiagramCount();
		while (!isValidDiagramName(d.getName())) {
			try {
				d.setName(d.getName() + " " + serial);
			}catch (PropertyVetoException e) {
				serial++;
			}
		}
		ProjectMember pm = new ProjectMemberDiagram(d,this);
		addDiagram(d);
		members.add(pm);
	}
	private void addTodoMember(ProjectMemberTodoList pm) {
		members.add(pm);
	}
	public void addMember(Object m) {
		if (m == null) {
			throw new IllegalArgumentException("A model member must be suppleid");
		}else if (m instanceof ArgoDiagram) {
			addDiagramMember((ArgoDiagram) m);
		}else if (m instanceof ProjectMemberTodoList) {
			addTodoMember((ProjectMemberTodoList) m);
		}else if (Model.getFacade().isAModel(m)) {
			addModelMember(m);
		}else {
			throw new IllegalArgumentException("The member must be a UML model todo member or diagram." + "It is " + m.getClass().getName());
		}
	}
	private void addModelMember(final Object m) {
		boolean memberFound = false;
		Object currentMember = members.get(0);
		if (currentMember instanceof ProjectMemberModel) {
			Object currentModel = ((ProjectMemberModel) currentMember).getModel();
			if (currentModel == m) {
				memberFound = true;
			}
		}
		if (!memberFound) {
			if (!models.contains(m)) {
				addModel(m);
			}
			ProjectMember pm = new ProjectMemberModel(m,this);
			members.add(pm);
		}else {
			throw new IllegalArgumentException("Attempted to load 2 models");
		}
	}
	public void addModel(final Object model) {
		if (!Model.getFacade().isAModel(model)) {
			throw new IllegalArgumentException();
		}
		if (!models.contains(model)) {
			setRoot(model);
		}
	}
	private void addModelInternal(final Object model) {
		models.add(model);
		roots.add(model);
		setCurrentNamespace(model);
		setSaveEnabled(true);
		if (models.size() > 1||roots.size() > 1) {
		}
	}
	protected void removeProjectMemberDiagram(ArgoDiagram d) {
		if (activeDiagram == d) {
			ArgoDiagram defaultDiagram = null;
			if (diagrams.size() == 1) {
				Object projectRoot = getRoot();
				if (!Model.getUmlFactory().isRemoved(projectRoot)) {
					defaultDiagram = DiagramFactory.getInstance().createDefaultDiagram(projectRoot);
					addMember(defaultDiagram);
				}
			}else {
				defaultDiagram = diagrams.get(0);
				if (defaultDiagram == d) {
					defaultDiagram = diagrams.get(1);
				}
			}
			activeDiagram = defaultDiagram;
			TargetManager.getInstance().setTarget(activeDiagram);
		}
		removeDiagram(d);
		members.remove(d);
		d.remove();
		setSaveEnabled(true);
	}
	private void setSaveEnabled(boolean enable) {
		ProjectManager pm = ProjectManager.getManager();
		if (pm.getCurrentProject() == this) {
			pm.setSaveEnabled(enable);
		}
	}
	public String getAuthorname() {
		return authorname;
	}
	public void setAuthorname(final String s) {
		final String oldAuthorName = authorname;
		AbstractCommand command = new AbstractCommand() {
	public Object execute() {
		authorname = s;
		return null;
	}
	public void undo() {
		authorname = oldAuthorName;
	}
};
		undoManager.execute(command);
	}
	public String getAuthoremail() {
		return authoremail;
	}
	public void setAuthoremail(final String s) {
		final String oldAuthorEmail = authoremail;
		AbstractCommand command = new AbstractCommand() {
	public Object execute() {
		authoremail = s;
		return null;
	}
	public void undo() {
		authoremail = oldAuthorEmail;
	}
};
		undoManager.execute(command);
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String s) {
		version = s;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(final String s) {
		final String oldDescription = description;
		AbstractCommand command = new AbstractCommand() {
	public Object execute() {
		description = s;
		return null;
	}
	public void undo() {
		description = oldDescription;
	}
};
		undoManager.execute(command);
	}
	public String getHistoryFile() {
		return historyFile;
	}
	public void setHistoryFile(final String s) {
		historyFile = s;
	}
	public List getUserDefinedModelList() {
		return models;
	}
	public Collection getModels() {
		Set result = new HashSet();
		result.addAll(models);
		for (Profile profile:getProfileConfiguration().getProfiles()) {
			try {
				result.addAll(profile.getProfilePackages());
			}catch (org.argouml.profile.ProfileException e) {
			}
		}
		return Collections.unmodifiableCollection(result);
	}
	@SuppressWarnings("deprecation")@Deprecated public Object getModel() {
		if (models.size() != 1) {
			return null;
		}
		return models.iterator().next();
	}
	public Object findType(String s) {
		return findType(s,true);
	}
	public Object getDefaultAttributeType() {
		if (profileConfiguration.getDefaultTypeStrategy() != null) {
			return profileConfiguration.getDefaultTypeStrategy().getDefaultAttributeType();
		}
		return null;
	}
	public Object getDefaultParameterType() {
		if (profileConfiguration.getDefaultTypeStrategy() != null) {
			return profileConfiguration.getDefaultTypeStrategy().getDefaultParameterType();
		}
		return null;
	}
	public Object getDefaultReturnType() {
		if (profileConfiguration.getDefaultTypeStrategy() != null) {
			return profileConfiguration.getDefaultTypeStrategy().getDefaultReturnType();
		}
		return null;
	}
	public Object findType(String s,boolean defineNew) {
		if (s != null) {
			s = s.trim();
		}
		if (s == null||s.length() == 0) {
			return null;
		}
		Object cls = null;
		for (Object model:models) {
			cls = findTypeInModel(s,model);
			if (cls != null) {
				return cls;
			}
		}
		cls = findTypeInDefaultModel(s);
		if (cls == null&&defineNew) {
			cls = Model.getCoreFactory().buildClass(getCurrentNamespace());
			Model.getCoreHelper().setName(cls,s);
		}
		return cls;
	}
	public Collection<Fig>findFigsForMember(Object member) {
		Collection<Fig>figs = new ArrayList<Fig>();
		for (ArgoDiagram diagram:diagrams) {
			Fig fig = diagram.getContainingFig(member);
			if (fig != null) {
				figs.add(fig);
			}
		}
		return figs;
	}
	public Collection findAllPresentationsFor(Object obj) {
		Collection<Fig>figs = new ArrayList<Fig>();
		for (ArgoDiagram diagram:diagrams) {
			Fig aFig = diagram.presentationFor(obj);
			if (aFig != null) {
				figs.add(aFig);
			}
		}
		return figs;
	}
	public Object findTypeInModel(String typeName,Object namespace) {
		if (typeName == null) {
			throw new IllegalArgumentException("typeName must be non-null");
		}
		if (!Model.getFacade().isANamespace(namespace)) {
			throw new IllegalArgumentException("Looking for the classifier " + typeName + " in a non-namespace object of " + namespace + ". A namespace was expected.");
		}
		Collection allClassifiers = Model.getModelManagementHelper().getAllModelElementsOfKind(namespace,Model.getMetaTypes().getClassifier());
		for (Object classifier:allClassifiers) {
			if (typeName.equals(Model.getFacade().getName(classifier))) {
				return classifier;
			}
		}
		return null;
	}
	@SuppressWarnings("deprecation")@Deprecated public void setCurrentNamespace(final Object m) {
		if (m != null&&!Model.getFacade().isANamespace(m)) {
			throw new IllegalArgumentException();
		}
		currentNamespace = m;
	}
	@SuppressWarnings("deprecation")@Deprecated public Object getCurrentNamespace() {
		return currentNamespace;
	}
	public List<ArgoDiagram>getDiagramList() {
		return Collections.unmodifiableList(diagrams);
	}
	public int getDiagramCount() {
		return diagrams.size();
	}
	public ArgoDiagram getDiagram(String name) {
		for (ArgoDiagram ad:diagrams) {
			if (ad.getName() != null&&ad.getName().equals(name)) {
				return ad;
			}
			if (ad.getItemUID() != null&&ad.getItemUID().toString().equals(name)) {
				return ad;
			}
		}
		return null;
	}
	public void addDiagram(final ArgoDiagram d) {
		d.setProject(this);
		diagrams.add(d);
		d.addPropertyChangeListener("name",new NamePCL());
		setSaveEnabled(true);
	}
	private class NamePCL implements PropertyChangeListener {
	public void propertyChange(PropertyChangeEvent evt) {
		setSaveEnabled(true);
	}
}
	protected void removeDiagram(ArgoDiagram d) {
		diagrams.remove(d);
		Object o = d.getDependentElement();
		if (o != null) {
			moveToTrash(o);
		}
	}
	public int getPresentationCountFor(Object me) {
		if (!Model.getFacade().isAUMLElement(me)) {
			throw new IllegalArgumentException();
		}
		int presentations = 0;
		for (ArgoDiagram d:diagrams) {
			presentations += d.getLayer().presentationCountFor(me);
		}
		return presentations;
	}
	public Object getInitialTarget() {
		if (savedDiagramName != null) {
			return getDiagram(savedDiagramName);
		}
		if (diagrams.size() > 0) {
			return diagrams.get(0);
		}
		if (models.size() > 0) {
			return models.iterator().next();
		}
		return null;
	}
	@SuppressWarnings("deprecation")@Deprecated public VetoableChangeSupport getVetoSupport() {
		if (vetoSupport == null) {
			vetoSupport = new VetoableChangeSupport(this);
		}
		return vetoSupport;
	}
	public void preSave() {
		for (ArgoDiagram diagram:diagrams) {
			diagram.preSave();
		}
	}
	public void postSave() {
		for (ArgoDiagram diagram:diagrams) {
			diagram.postSave();
		}
		setSaveEnabled(true);
	}
	public void postLoad() {
		long startTime = System.currentTimeMillis();
		for (ArgoDiagram diagram:diagrams) {
			diagram.postLoad();
		}
		long endTime = System.currentTimeMillis();
		Object model = getModel();
		setRoot(model);
		setSaveEnabled(true);
		uuidRefs = null;
	}
	private void emptyTrashCan() {
		trashcan.clear();
	}
	public void moveToTrash(Object obj) {
		if (obj instanceof Collection) {
			Iterator i = ((Collection) obj).iterator();
			while (i.hasNext()) {
				Object trash = i.next();
				if (!trashcan.contains(trash)) {
					trashInternal(trash);
				}
			}
		}else {
			if (!trashcan.contains(obj)) {
				trashInternal(obj);
			}
		}
	}
	protected void trashInternal(Object obj) {
		if (Model.getFacade().isAModel(obj)) {
			return;
		}
		if (obj != null) {
			trashcan.add(obj);
		}
		if (Model.getFacade().isAUMLElement(obj)) {
			Model.getUmlFactory().delete(obj);
			if (models.contains(obj)) {
				models.remove(obj);
			}
		}else if (obj instanceof ArgoDiagram) {
			removeProjectMemberDiagram((ArgoDiagram) obj);
			ProjectManager.getManager().firePropertyChanged("remove",obj,null);
		}else if (obj instanceof Fig) {
			((Fig) obj).deleteFromModel();
		}else if (obj instanceof CommentEdge) {
			CommentEdge ce = (CommentEdge) obj;
			ce.delete();
		}
	}
	@SuppressWarnings("deprecation")@Deprecated public boolean isInTrash(Object obj) {
		return trashcan.contains(obj);
	}
	public Object findTypeInDefaultModel(String name) {
		if (defaultModelTypeCache.containsKey(name)) {
			return defaultModelTypeCache.get(name);
		}
		Object result = profileConfiguration.findType(name);
		defaultModelTypeCache.put(name,result);
		return result;
	}
	@SuppressWarnings("deprecation")@Deprecated public final Object getRoot() {
		return root;
	}
	@SuppressWarnings("deprecation")@Deprecated public void setRoot(final Object theRoot) {
		if (theRoot == null) {
			throw new IllegalArgumentException("A root model element is required");
		}
		if (!Model.getFacade().isAModel(theRoot)) {
			throw new IllegalArgumentException("The root model element must be a model - got " + theRoot.getClass().getName());
		}
		Object treeRoot = Model.getModelManagementFactory().getRootModel();
		if (treeRoot != null) {
			models.remove(treeRoot);
		}
		root = theRoot;
		Model.getModelManagementFactory().setRootModel(theRoot);
		addModelInternal(theRoot);
		roots.clear();
		roots.add(theRoot);
	}
	public final Collection getRoots() {
		return Collections.unmodifiableCollection(roots);
	}
	public void setRoots(final Collection elements) {
		boolean modelFound = false;
		for (Object element:elements) {
			if (Model.getFacade().isAModel(element)) {
				addModel(element);
				if (!modelFound) {
					setRoot(element);
					modelFound = true;
				}
			}
		}
		roots.clear();
		roots.addAll(elements);
	}
	public boolean isValidDiagramName(String name) {
		boolean rv = true;
		for (ArgoDiagram diagram:diagrams) {
			if (diagram.getName().equals(name)) {
				rv = false;
				break;
			}
		}
		return rv;
	}
	public Map<String,Object>getUUIDRefs() {
		return uuidRefs;
	}
	public void setSearchPath(final List<String>theSearchpath) {
		searchpath.clear();
		searchpath.addAll(theSearchpath);
	}
	public void setUUIDRefs(Map<String,Object>uUIDRefs) {
		uuidRefs = uUIDRefs;
	}
	@SuppressWarnings("deprecation")@Deprecated public void setVetoSupport(VetoableChangeSupport theVetoSupport) {
		vetoSupport = theVetoSupport;
	}
	@SuppressWarnings("deprecation")@Deprecated public ArgoDiagram getActiveDiagram() {
		return activeDiagram;
	}
	@SuppressWarnings("deprecation")@Deprecated public void setActiveDiagram(final ArgoDiagram theDiagram) {
		activeDiagram = theDiagram;
	}
	public void setSavedDiagramName(String diagramName) {
		savedDiagramName = diagramName;
	}
	public void remove() {
		for (ArgoDiagram diagram:diagrams) {
			diagram.remove();
		}
		members.clear();
		if (!roots.isEmpty()) {
			try {
				Model.getUmlFactory().deleteExtent(roots.iterator().next());
			}catch (InvalidElementException e) {
			}
			roots.clear();
		}
		models.clear();
		diagrams.clear();
		searchpath.clear();
		if (uuidRefs != null) {
			uuidRefs.clear();
		}
		if (defaultModelTypeCache != null) {
			defaultModelTypeCache.clear();
		}
		uuidRefs = null;
		defaultModelTypeCache = null;
		uri = null;
		authorname = null;
		authoremail = null;
		description = null;
		version = null;
		historyFile = null;
		currentNamespace = null;
		vetoSupport = null;
		activeDiagram = null;
		savedDiagramName = null;
		emptyTrashCan();
	}
	public int getPersistenceVersion() {
		return persistenceVersion;
	}
	public void setPersistenceVersion(int pv) {
		persistenceVersion = pv;
	}
	public String repair() {
		StringBuilder report = new StringBuilder();
		Iterator it = members.iterator();
		while (it.hasNext()) {
			ProjectMember member = (ProjectMember) it.next();
			report.append(member.repair());
		}
		return report.toString();
	}
	public ProjectSettings getProjectSettings() {
		return projectSettings;
	}
	public UndoManager getUndoManager() {
		return undoManager;
	}
	public ProfileConfiguration getProfileConfiguration() {
		return profileConfiguration;
	}
	public void setProfileConfiguration(ProfileConfiguration pc) {
		if (this.profileConfiguration != pc) {
			if (this.profileConfiguration != null) {
				this.members.remove(this.profileConfiguration);
			}
			this.profileConfiguration = pc;
			members.add(pc);
		}
		ProfileFacade.applyConfiguration(pc);
	}
	public boolean isDirty() {
		return ProjectManager.getManager().isSaveActionEnabled();
	}
	public void setDirty(boolean isDirty) {
		dirty = isDirty;
		ProjectManager.getManager().setSaveEnabled(isDirty);
	}
}



