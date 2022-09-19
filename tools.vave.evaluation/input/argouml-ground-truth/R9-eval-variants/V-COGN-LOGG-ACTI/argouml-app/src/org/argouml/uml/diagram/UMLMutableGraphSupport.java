package org.argouml.uml.diagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.model.DiDiagram;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.uml.CommentEdge;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.graph.MutableGraphSupport;


public abstract class UMLMutableGraphSupport extends MutableGraphSupport {
	private static final Logger LOG = Logger.getLogger(UMLMutableGraphSupport.class);
	private DiDiagram diDiagram;
	private List nodes = new ArrayList();
	private List edges = new ArrayList();
	private Object homeModel;
	private Project project;
	public UMLMutableGraphSupport() {
		super();
	}
	public List getNodes() {
		return nodes;
	}
	public List getEdges() {
		return edges;
	}
	public boolean containsNode(Object node) {
		return nodes.contains(node);
	}
	public boolean constainsEdge(Object edge) {
		return edges.contains(edge);
	}
	@Override public void removeNode(Object node) {
		if (!containsNode(node)) {
			return;
		}
		nodes.remove(node);
		fireNodeRemoved(node);
	}
	@Override public void removeEdge(Object edge) {
		if (!containsEdge(edge)) {
			return;
		}
		edges.remove(edge);
		fireEdgeRemoved(edge);
	}
	public boolean canConnect(Object fromP,Object toP) {
		return true;
	}
	public Object connect(Object fromPort,Object toPort) {
		throw new UnsupportedOperationException("The connect method is not supported");
	}
	public Object getHomeModel() {
		return homeModel;
	}
	public void setHomeModel(Object ns) {
		if (!Model.getFacade().isANamespace(ns)) {
			throw new IllegalArgumentException();
		}
		homeModel = ns;
	}
	public Object connect(Object fromPort,Object toPort,Class edgeClass) {
		return connect(fromPort,toPort,(Object) edgeClass);
	}
	public Object connect(Object fromPort,Object toPort,Object edgeType) {
		Editor curEditor = Globals.curEditor();
		ModeManager modeManager = curEditor.getModeManager();
		Mode mode = modeManager.top();
		Dictionary args = mode.getArgs();
		Object style = args.get("aggregation");
		Boolean unidirectional = (Boolean) args.get("unidirectional");
		Object model = getProject().getModel();
		Object connection = buildConnection(edgeType,fromPort,style,toPort,null,unidirectional,model);
		if (connection == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Cannot make a " + edgeType + " between a " + fromPort.getClass().getName() + " and a " + toPort.getClass().getName());
			}
			return null;
		}
		addEdge(connection);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Connection type" + edgeType + " made between a " + fromPort.getClass().getName() + " and a " + toPort.getClass().getName());
		}
		return connection;
	}
	public Object connect(Object fromPort,Object toPort,Object edgeType,Map styleAttributes) {
		return null;
	}
	public boolean canAddNode(Object node) {
		if (node == null) {
			return false;
		}
		if (Model.getFacade().isAComment(node)) {
			return true;
		}
		return false;
	}
	public Object getSourcePort(Object edge) {
		if (edge instanceof CommentEdge) {
			return((CommentEdge) edge).getSource();
		}else if (Model.getFacade().isARelationship(edge)||Model.getFacade().isATransition(edge)||Model.getFacade().isAAssociationEnd(edge)) {
			return Model.getUmlHelper().getSource(edge);
		}else if (Model.getFacade().isALink(edge)) {
			return Model.getCommonBehaviorHelper().getSource(edge);
		}
		LOG.error(this.getClass().toString() + ": getSourcePort(" + edge.toString() + ") - can\'t handle");
		return null;
	}
	public Object getDestPort(Object edge) {
		if (edge instanceof CommentEdge) {
			return((CommentEdge) edge).getDestination();
		}else if (Model.getFacade().isAAssociation(edge)) {
			List conns = new ArrayList(Model.getFacade().getConnections(edge));
			return conns.get(1);
		}else if (Model.getFacade().isARelationship(edge)||Model.getFacade().isATransition(edge)||Model.getFacade().isAAssociationEnd(edge)) {
			return Model.getUmlHelper().getDestination(edge);
		}else if (Model.getFacade().isALink(edge)) {
			return Model.getCommonBehaviorHelper().getDestination(edge);
		}
		LOG.error(this.getClass().toString() + ": getDestPort(" + edge.toString() + ") - can\'t handle");
		return null;
	}
	public boolean canAddEdge(Object edge) {
		if (edge instanceof CommentEdge) {
			CommentEdge ce = (CommentEdge) edge;
			return isConnectionValid(CommentEdge.class,ce.getSource(),ce.getDestination());
		}else if (edge != null&&Model.getUmlFactory().isConnectionType(edge)) {
			return isConnectionValid(edge.getClass(),Model.getUmlHelper().getSource(edge),Model.getUmlHelper().getDestination(edge));
		}
		return false;
	}
	public void addNodeRelatedEdges(Object node) {
		if (Model.getFacade().isAModelElement(node)) {
			List specs = new ArrayList(Model.getFacade().getClientDependencies(node));
			specs.addAll(Model.getFacade().getSupplierDependencies(node));
			Iterator iter = specs.iterator();
			while (iter.hasNext()) {
				Object dependency = iter.next();
				if (canAddEdge(dependency)) {
					addEdge(dependency);
				}
			}
		}
		Collection cmnt = new ArrayList();
		if (Model.getFacade().isAComment(node)) {
			cmnt.addAll(Model.getFacade().getAnnotatedElements(node));
		}
		if (Model.getFacade().isAModelElement(node)) {
			cmnt.addAll(Model.getFacade().getComments(node));
		}
		Iterator iter = cmnt.iterator();
		while (iter.hasNext()) {
			Object ae = iter.next();
			CommentEdge ce = new CommentEdge(node,ae);
			if (canAddEdge(ce)) {
				addEdge(ce);
			}
		}
	}
	protected Object buildConnection(Object edgeType,Object fromElement,Object fromStyle,Object toElement,Object toStyle,Object unidirectional,Object namespace) {
		Object connection = null;
		if (edgeType == CommentEdge.class) {
			connection = buildCommentConnection(fromElement,toElement);
		}else {
			try {
				connection = Model.getUmlFactory().buildConnection(edgeType,fromElement,fromStyle,toElement,toStyle,unidirectional,namespace);
				LOG.info("Created " + connection + " between " + fromElement + " and " + toElement);
			}catch (UmlException ex) {
			}catch (IllegalArgumentException iae) {
				LOG.warn("IllegalArgumentException caught",iae);
			}
		}
		return connection;
	}
	public CommentEdge buildCommentConnection(Object from,Object to) {
		if (from == null||to == null) {
			throw new IllegalArgumentException("Either fromNode == null " + "or toNode == null");
		}
		Object comment = null;
		Object annotatedElement = null;
		if (Model.getFacade().isAComment(from)) {
			comment = from;
			annotatedElement = to;
		}else {
			if (Model.getFacade().isAComment(to)) {
				comment = to;
				annotatedElement = from;
			}else {
				return null;
			}
		}
		CommentEdge connection = new CommentEdge(from,to);
		Model.getCoreHelper().addAnnotatedElement(comment,annotatedElement);
		return connection;
	}
	protected boolean isConnectionValid(Object edgeType,Object fromElement,Object toElement) {
		if (!nodes.contains(fromElement)||!nodes.contains(toElement)) {
			return false;
		}
		if (edgeType.equals(CommentEdge.class)) {
			return((Model.getFacade().isAComment(fromElement)&&Model.getFacade().isAModelElement(toElement))||(Model.getFacade().isAComment(toElement)&&Model.getFacade().isAModelElement(fromElement)));
		}
		return Model.getUmlFactory().isConnectionValid(edgeType,fromElement,toElement,true);
	}
	void setDiDiagram(DiDiagram dd) {
		diDiagram = dd;
	}
	public DiDiagram getDiDiagram() {
		return diDiagram;
	}
	public boolean isRemoveFromDiagramAllowed(Collection figs) {
		return!figs.isEmpty();
	}
	public void setProject(Project p) {
		project = p;
	}
	public Project getProject() {
		return project;
	}
}



