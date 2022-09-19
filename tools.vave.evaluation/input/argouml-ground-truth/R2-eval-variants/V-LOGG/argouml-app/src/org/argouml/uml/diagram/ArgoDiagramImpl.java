package org.argouml.uml.diagram;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoDiagramAppearanceEvent;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.CoreHelper;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.ModelManagementHelper;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.ui.ArgoFig;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.util.EnumerationIterator;
import org.argouml.util.IItemUID;
import org.argouml.util.ItemUID;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphSupport;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.undo.UndoManager;
import org.argouml.uml.diagram.DiagramSettings;


public abstract class ArgoDiagramImpl extends Diagram implements PropertyChangeListener,VetoableChangeListener,ArgoDiagram,IItemUID {
	private ItemUID id;
	private Project project;
	protected Object namespace;
	private DiagramSettings settings;
	private static final Logger LOG = Logger.getLogger(ArgoDiagramImpl.class);
	@Deprecated public ArgoDiagramImpl() {
		super();
		getLayer().getGraphModel().removeGraphEventListener(getLayer());
		constructorInit();
	}
	@Deprecated public ArgoDiagramImpl(String diagramName) {
		super(diagramName);
		try {
			setName(diagramName);
		}catch (PropertyVetoException pve) {
		}
		constructorInit();
	}
	public ArgoDiagramImpl(String name,GraphModel graphModel,LayerPerspective layer) {
		super(name,graphModel,layer);
		try {
			setName(name);
		}catch (PropertyVetoException pve) {
		}
		constructorInit();
	}
	private void constructorInit() {
		Project project = ProjectManager.getManager().getCurrentProject();
		if (project != null) {
			settings = project.getProjectSettings().getDefaultDiagramSettings();
		}
		if (!(UndoManager.getInstance()instanceof DiagramUndoManager)) {
			UndoManager.setInstance(new DiagramUndoManager());
			LOG.info("Setting Diagram undo manager");
		}else {
			LOG.info("Diagram undo manager already set");
		}
		ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT,this);
		ArgoEventPump.addListener(ArgoEventTypes.ANY_DIAGRAM_APPEARANCE_EVENT,this);
		addVetoableChangeListener(this);
	}
	public void setName(String n)throws PropertyVetoException {
		super.setName(n);
		MutableGraphSupport.enableSaveAction();
	}
	public void setItemUID(ItemUID i) {
		id = i;
	}
	public ItemUID getItemUID() {
		return id;
	}
	static final long serialVersionUID = -401219134410459387l;
	public String getVetoMessage(String propertyName) {
		if (propertyName.equals("name")) {
			return"Name of diagram may not exist already";
		}
		return null;
	}
	public Fig getContainingFig(Object obj) {
		Fig fig = super.presentationFor(obj);
		if (fig == null&&Model.getFacade().isAUMLElement(obj)) {
			if (Model.getFacade().isAOperation(obj)||Model.getFacade().isAReception(obj)||Model.getFacade().isAAttribute(obj)) {
				return presentationFor(Model.getFacade().getOwner(obj));
			}
		}
		return fig;
	}
	public void damage() {
		if (getLayer() != null&&getLayer().getEditors() != null) {
			Iterator it = getLayer().getEditors().iterator();
			while (it.hasNext()) {
				((Editor) it.next()).damageAll();
			}
		}
	}
	public List getEdges() {
		if (getGraphModel() != null) {
			return getGraphModel().getEdges();
		}
		return super.getEdges();
	}
	public List getNodes() {
		if (getGraphModel() != null) {
			return getGraphModel().getNodes();
		}
		return super.getNodes();
	}
	public String toString() {
		return"Diagram: " + getName();
	}
	public String repair() {
		StringBuffer report = new StringBuffer(500);
		boolean faultFixed;
		do {
			faultFixed = false;
			List<Fig>figs = new ArrayList<Fig>(getLayer().getContentsNoEdges());
			for (Fig f:figs) {
				if (repairFig(f,report)) {
					faultFixed = true;
				}
			}
			figs = new ArrayList<Fig>(getLayer().getContentsEdgesOnly());
			for (Fig f:figs) {
				if (repairFig(f,report)) {
					faultFixed = true;
				}
			}
		}while (faultFixed);
		return report.toString();
	}
	private boolean repairFig(Fig f,StringBuffer report) {
		LOG.info("Checking " + figDescription(f) + f.getOwner());
		boolean faultFixed = false;
		String figDescription = null;
		if (!getLayer().equals(f.getLayer())) {
			if (figDescription == null) {
				figDescription = figDescription(f);
				report.append(figDescription);
			}
			if (f.getLayer() == null) {
				report.append("-- Fixed: layer was null\n");
			}else {
				report.append("-- Fixed: refered to wrong layer\n");
			}
			faultFixed = true;
			f.setLayer(getLayer());
		}
		if (!f.isVisible()) {
			if (figDescription == null) {
				figDescription = figDescription(f);
				report.append(figDescription);
			}
			report.append("-- Fixed: a Fig must be visible\n");
			faultFixed = true;
			f.setVisible(true);
		}
		if (f instanceof FigEdge) {
			FigEdge fe = (FigEdge) f;
			FigNode destFig = fe.getDestFigNode();
			FigNode sourceFig = fe.getSourceFigNode();
			if (destFig == null) {
				if (figDescription == null) {
					figDescription = figDescription(f);
					report.append(figDescription);
				}
				faultFixed = true;
				report.append("-- Removed: as it has no dest Fig\n");
				f.removeFromDiagram();
			}else if (sourceFig == null) {
				if (figDescription == null) {
					figDescription = figDescription(f);
					report.append(figDescription);
				}
				faultFixed = true;
				report.append("-- Removed: as it has no source Fig\n");
				f.removeFromDiagram();
			}else if (sourceFig.getOwner() == null) {
				if (figDescription == null) {
					figDescription = figDescription(f);
					report.append(figDescription);
				}
				faultFixed = true;
				report.append("-- Removed: as its source Fig has no owner\n");
				f.removeFromDiagram();
			}else if (destFig.getOwner() == null) {
				if (figDescription == null) {
					figDescription = figDescription(f);
					report.append(figDescription);
				}
				faultFixed = true;
				report.append("-- Removed: as its destination Fig has no owner\n");
				f.removeFromDiagram();
			}else if (Model.getUmlFactory().isRemoved(sourceFig.getOwner())) {
				if (figDescription == null) {
					figDescription = figDescription(f);
					report.append(figDescription);
				}
				faultFixed = true;
				report.append("-- Removed: as its source Figs owner is no " + "longer in the repository\n");
				f.removeFromDiagram();
			}else if (Model.getUmlFactory().isRemoved(destFig.getOwner())) {
				if (figDescription == null) {
					figDescription = figDescription(f);
					report.append(figDescription);
				}
				faultFixed = true;
				report.append("-- Removed: as its destination Figs owner " + "is no longer in the repository\n");
				f.removeFromDiagram();
			}
		}else if ((f instanceof FigNode||f instanceof FigEdge)&&f.getOwner() == null) {
			if (figDescription == null) {
				figDescription = figDescription(f);
				report.append(figDescription);
			}
			faultFixed = true;
			report.append("-- Removed: owner was null\n");
			f.removeFromDiagram();
		}else if ((f instanceof FigNode||f instanceof FigEdge)&&Model.getFacade().isAUMLElement(f.getOwner())&&Model.getUmlFactory().isRemoved(f.getOwner())) {
			if (figDescription == null) {
				figDescription = figDescription(f);
				report.append(figDescription);
			}
			faultFixed = true;
			report.append("-- Removed: model element no longer in the repository\n");
			f.removeFromDiagram();
		}else if (f instanceof FigGroup&&!(f instanceof FigNode)) {
			if (figDescription == null) {
				figDescription = figDescription(f);
				report.append(figDescription);
			}
			faultFixed = true;
			report.append("-- Removed: a FigGroup should not be on the diagram\n");
			f.removeFromDiagram();
		}
		return faultFixed;
	}
	private String figDescription(Fig f) {
		String description = "\n" + f.getClass().getName();
		if (f instanceof FigComment) {
			description += " \"" + ((FigComment) f).getBody() + "\"";
		}else if (f instanceof FigNodeModelElement) {
			description += " \"" + ((FigNodeModelElement) f).getName() + "\"";
		}else if (f instanceof FigEdgeModelElement) {
			FigEdgeModelElement fe = (FigEdgeModelElement) f;
			description += " \"" + fe.getName() + "\"";
			String source;
			if (fe.getSourceFigNode() == null) {
				source = "(null)";
			}else {
				source = ((FigNodeModelElement) fe.getSourceFigNode()).getName();
			}
			String dest;
			if (fe.getDestFigNode() == null) {
				dest = "(null)";
			}else {
				dest = ((FigNodeModelElement) fe.getDestFigNode()).getName();
			}
			description += " [" + source + "=>" + dest + "]";
		}
		return description + "\n";
	}
	public List presentationsFor(Object obj) {
		List<Fig>presentations = new ArrayList<Fig>();
		int figCount = getLayer().getContents().size();
		for (int figIndex = 0;figIndex < figCount;++figIndex) {
			Fig fig = (Fig) getLayer().getContents().get(figIndex);
			if (fig.getOwner() == obj) {
				presentations.add(fig);
			}
		}
		return presentations;
	}
	public void remove() {
		List<Fig>contents = new ArrayList<Fig>(getLayer().getContents());
		int size = contents.size();
		for (int i = 0;i < size;++i) {
			Fig f = contents.get(i);
			f.removeFromDiagram();
		}
		firePropertyChange("remove",null,null);
		super.remove();
	}
	public void setProject(Project p) {
		project = p;
	}
	public Project getProject() {
		return project;
	}
	public abstract void encloserChanged(FigNode enclosed,FigNode oldEncloser,FigNode newEncloser);
	public Object getDependentElement() {
		return null;
	}
	public Object getNamespace() {
		return namespace;
	}
	public void setNamespace(Object ns) {
		if (!Model.getFacade().isANamespace(ns)) {
			LOG.error("Not a namespace");
			LOG.error(ns);
			throw new IllegalArgumentException("Given object not a namespace");
		}
		if ((namespace != null)&&(namespace != ns)) {
			Model.getPump().removeModelEventListener(this,namespace);
		}
		Object oldNs = namespace;
		namespace = ns;
		firePropertyChange(NAMESPACE_KEY,oldNs,ns);
		Model.getPump().addModelEventListener(this,namespace,"remove");
	}
	public void setModelElementNamespace(Object modelElement,Object ns) {
		if (modelElement == null) {
			return;
		}
		if (ns == null) {
			if (getNamespace() != null) {
				ns = getNamespace();
			}else {
				ns = getProject().getRoot();
			}
		}
		if (ns == null) {
			return;
		}
		if (Model.getFacade().getNamespace(modelElement) == ns) {
			return;
		}
		CoreHelper coreHelper = Model.getCoreHelper();
		ModelManagementHelper modelHelper = Model.getModelManagementHelper();
		if (!modelHelper.isCyclicOwnership(ns,modelElement)&&coreHelper.isValidNamespace(modelElement,ns)) {
			coreHelper.setModelElementContainer(modelElement,ns);
		}
	}
	public void propertyChange(PropertyChangeEvent evt) {
		if ((evt.getSource() == namespace)&&(evt instanceof DeleteInstanceEvent)&&"remove".equals(evt.getPropertyName())) {
			Model.getPump().removeModelEventListener(this,namespace,"remove");
			if (getProject() != null) {
				getProject().moveToTrash(this);
			}
		}
	}
	public Object getOwner() {
		return getNamespace();
	}
	public Iterator<Fig>getFigIterator() {
		return new EnumerationIterator(elements());
	}
	public void setDiagramSettings(DiagramSettings newSettings) {
		settings = newSettings;
	}
	public DiagramSettings getDiagramSettings() {
		return settings;
	}
	public void diagramFontChanged(ArgoDiagramAppearanceEvent e) {
		renderingChanged();
	}
	public void renderingChanged() {
		for (Object fig:getLayer().getContents()) {
			try {
				if (fig instanceof ArgoFig) {
					((ArgoFig) fig).renderingChanged();
				}else {
					LOG.warn("Diagram " + getName() + " contains non-ArgoFig " + fig);
				}
			}catch (InvalidElementException e) {
				LOG.error("Tried to refresh deleted element ",e);
			}
		}
		damage();
	}
	public void notationChanged(ArgoNotationEvent e) {
		renderingChanged();
	}
	public void notationAdded(ArgoNotationEvent e) {
	}
	public void notationProviderAdded(ArgoNotationEvent e) {
	}
	public void notationProviderRemoved(ArgoNotationEvent e) {
	}
	public void notationRemoved(ArgoNotationEvent e) {
	}
	public void vetoableChange(PropertyChangeEvent evt)throws PropertyVetoException {
		if ("name".equals(evt.getPropertyName())) {
			if (project != null) {
				if (!project.isValidDiagramName((String) evt.getNewValue())) {
					throw new PropertyVetoException("Invalid name",evt);
				}
			}
		}
	}
}



