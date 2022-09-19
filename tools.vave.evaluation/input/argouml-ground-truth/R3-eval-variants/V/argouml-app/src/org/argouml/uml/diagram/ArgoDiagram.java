package org.argouml.uml.diagram;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.argouml.application.events.ArgoDiagramAppearanceEventListener;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.kernel.Project;
import org.argouml.util.ItemUID;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.argouml.uml.diagram.DiagramSettings;


public interface ArgoDiagram extends ArgoNotationEventListener,ArgoDiagramAppearanceEventListener {
	public void setDiagramSettings(DiagramSettings settings);
	public DiagramSettings getDiagramSettings();
	public void setName(String n)throws PropertyVetoException;
	public void setItemUID(ItemUID i);
	public ItemUID getItemUID();
	public static final String NAMESPACE_KEY = "namespace";
	public static final String NAME_KEY = "name";
	public String getVetoMessage(String propertyName);
	public Fig getContainingFig(Object obj);
	public void damage();
	public List getEdges();
	public List getNodes();
	public String repair();
	public List presentationsFor(Object obj);
	public void remove();
	public void setProject(Project p);
	public Project getProject();
	public void encloserChanged(FigNode enclosed,FigNode oldEncloser,FigNode newEncloser);
	public Object getDependentElement();
	public Object getNamespace();
	public void setNamespace(Object ns);
	public void setModelElementNamespace(Object modelElement,Object ns);
	public void propertyChange(PropertyChangeEvent evt);
	public Object getOwner();
	public Iterator<Fig>getFigIterator();
	public void addVetoableChangeListener(VetoableChangeListener listener);
	public void removeVetoableChangeListener(VetoableChangeListener listener);
	public void addPropertyChangeListener(String property,PropertyChangeListener listener);
	public void removePropertyChangeListener(String property,PropertyChangeListener listener);
	public GraphModel getGraphModel();
	public LayerPerspective getLayer();
	public int countContained(List figures);
	public Fig presentationFor(Object o);
	public void add(Fig f);
	public String getName();
	public void preSave();
	public void postSave();
	public void postLoad();
}



