package org.argouml.kernel;

import java.beans.VetoableChangeSupport;
import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.argouml.uml.diagram.ArgoDiagram;
import org.tigris.gef.presentation.Fig;


public interface Project {
	public String getName();
	public URI getURI();
	public void setUri(final URI theUri);
	public void setFile(final File file);
	public List<String>getSearchPathList();
	public void addSearchPath(String searchPathElement);
	public void setSearchPath(final List<String>theSearchpath);
	public List<ProjectMember>getMembers();
	public void addMember(final Object m);
	public void addModel(final Object model);
	public String getAuthorname();
	public void setAuthorname(final String s);
	public String getAuthoremail();
	public void setAuthoremail(final String s);
	public String getVersion();
	public void setVersion(final String s);
	public String getDescription();
	public void setDescription(final String s);
	public String getHistoryFile();
	public void setHistoryFile(final String s);
	public List getUserDefinedModelList();
	public Collection getModels();
	@Deprecated public Object getModel();
	public Object getDefaultAttributeType();
	public Object getDefaultParameterType();
	public Object getDefaultReturnType();
	public Object findType(String s);
	public Object findType(String s,boolean defineNew);
	public Collection<Fig>findFigsForMember(Object member);
	public Collection findAllPresentationsFor(Object obj);
	public Object findTypeInModel(String s,Object ns);
	@Deprecated public void setCurrentNamespace(final Object m);
	@Deprecated public Object getCurrentNamespace();
	public List<ArgoDiagram>getDiagramList();
	public int getDiagramCount();
	public ArgoDiagram getDiagram(String name);
	public void addDiagram(final ArgoDiagram d);
	public int getPresentationCountFor(Object me);
	public Object getInitialTarget();
	@Deprecated public VetoableChangeSupport getVetoSupport();
	public void preSave();
	public void postSave();
	public void postLoad();
	public void moveToTrash(Object obj);
	@Deprecated public boolean isInTrash(Object obj);
	public Object findTypeInDefaultModel(String name);
	@Deprecated public Object getRoot();
	@Deprecated public void setRoot(final Object root);
	public Collection getRoots();
	public void setRoots(final Collection elements);
	public boolean isValidDiagramName(String name);
	public URI getUri();
	public Map<String,Object>getUUIDRefs();
	public void setUUIDRefs(final Map<String,Object>uUIDRefs);
	@Deprecated public void setVetoSupport(VetoableChangeSupport theVetoSupport);
	@Deprecated public ArgoDiagram getActiveDiagram();
	@Deprecated public void setActiveDiagram(final ArgoDiagram theDiagram);
	public void setSavedDiagramName(String diagramName);
	public void remove();
	public int getPersistenceVersion();
	public void setPersistenceVersion(int pv);
	public String repair();
	public ProjectSettings getProjectSettings();
	public ProfileConfiguration getProfileConfiguration();
	public void setProfileConfiguration(final ProfileConfiguration pc);
	public UndoManager getUndoManager();
	public boolean isDirty();
	public void setDirty(boolean isDirty);
}



