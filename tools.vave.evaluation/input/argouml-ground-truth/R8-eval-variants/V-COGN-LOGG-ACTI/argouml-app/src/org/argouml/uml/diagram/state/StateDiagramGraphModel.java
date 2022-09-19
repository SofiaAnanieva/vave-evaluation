package org.argouml.uml.diagram.state;

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.tigris.gef.presentation.Fig;


public class StateDiagramGraphModel extends UMLMutableGraphSupport implements VetoableChangeListener {
	private static final Logger LOG = Logger.getLogger(StateDiagramGraphModel.class);
	private Object machine;
	public Object getMachine() {
		return machine;
	}
	public void setMachine(Object sm) {
		if (!Model.getFacade().isAStateMachine(sm)) {
			throw new IllegalArgumentException();
		}
		if (sm != null) {
			machine = sm;
		}
	}
	public List getPorts(Object nodeOrEdge) {
		List res = new ArrayList();
		if (Model.getFacade().isAState(nodeOrEdge)) {
			res.add(nodeOrEdge);
		}
		if (Model.getFacade().isAPseudostate(nodeOrEdge)) {
			res.add(nodeOrEdge);
		}
		return res;
	}
	public Object getOwner(Object port) {
		return port;
	}
	public List getInEdges(Object port) {
		if (Model.getFacade().isAStateVertex(port)) {
			return new ArrayList(Model.getFacade().getIncomings(port));
		}
		LOG.debug("TODO: getInEdges of MState");
		return Collections.EMPTY_LIST;
	}
	public List getOutEdges(Object port) {
		if (Model.getFacade().isAStateVertex(port)) {
			return new ArrayList(Model.getFacade().getOutgoings(port));
		}
		LOG.debug("TODO: getOutEdges of MState");
		return Collections.EMPTY_LIST;
	}
	public boolean canAddNode(Object node) {
		if (node == null||!Model.getFacade().isAModelElement(node)||containsNode(node)) {
			return false;
		}
		if (Model.getFacade().isAComment(node)) {
			return true;
		}
		if (Model.getFacade().isAStateVertex(node)||Model.getFacade().isAPartition(node)) {
			Object nodeMachine = Model.getStateMachinesHelper().getStateMachine(node);
			if (nodeMachine == null||nodeMachine == getMachine()) {
				return true;
			}
		}
		return false;
	}
	public boolean canAddEdge(Object edge) {
		if (super.canAddEdge(edge)) {
			return true;
		}
		if (edge == null) {
			return false;
		}
		if (containsEdge(edge)) {
			return false;
		}
		Object end0 = null;
		Object end1 = null;
		if (Model.getFacade().isATransition(edge)) {
			end0 = Model.getFacade().getSource(edge);
			end1 = Model.getFacade().getTarget(edge);
			if (Model.getFacade().isACompositeState(end0)&&Model.getStateMachinesHelper().getAllSubStates(end0).contains(end1)) {
				return false;
			}
		}else if (edge instanceof CommentEdge) {
			end0 = ((CommentEdge) edge).getSource();
			end1 = ((CommentEdge) edge).getDestination();
		}else {
			return false;
		}
		if (end0 == null||end1 == null) {
			LOG.error("Edge rejected. Its ends are not attached to anything");
			return false;
		}
		if (!containsNode(end0)&&!containsEdge(end0)) {
			LOG.error("Edge rejected. Its source end is attached to " + end0 + " but this is not in the graph model");
			return false;
		}
		if (!containsNode(end1)&&!containsEdge(end1)) {
			LOG.error("Edge rejected. Its destination end is attached to " + end1 + " but this is not in the graph model");
			return false;
		}
		return true;
	}
	public void addNode(Object node) {
		LOG.debug("adding statechart/activity diagram node: " + node);
		if (!canAddNode(node)) {
			return;
		}
		if (containsNode(node)) {
			return;
		}
		getNodes().add(node);
		if (Model.getFacade().isAStateVertex(node)) {
			Object top = Model.getStateMachinesHelper().getTop(getMachine());
			Model.getStateMachinesHelper().addSubvertex(top,node);
		}
		fireNodeAdded(node);
	}
	public void addEdge(Object edge) {
		LOG.debug("adding statechart/activity diagram edge!!!!!!");
		if (!canAddEdge(edge)) {
			return;
		}
		getEdges().add(edge);
		fireEdgeAdded(edge);
	}
	public void addNodeRelatedEdges(Object node) {
		super.addNodeRelatedEdges(node);
		if (Model.getFacade().isAStateVertex(node)) {
			Collection transen = new ArrayList(Model.getFacade().getOutgoings(node));
			transen.addAll(Model.getFacade().getIncomings(node));
			Iterator iter = transen.iterator();
			while (iter.hasNext()) {
				Object dep = iter.next();
				if (canAddEdge(dep)) {
					addEdge(dep);
				}
			}
		}
	}
	public boolean canConnect(Object fromPort,Object toPort) {
		if (!(Model.getFacade().isAStateVertex(fromPort))) {
			LOG.error("internal error not from sv");
			return false;
		}
		if (!(Model.getFacade().isAStateVertex(toPort))) {
			LOG.error("internal error not to sv");
			return false;
		}
		if (Model.getFacade().isAFinalState(fromPort)) {
			return false;
		}
		if (Model.getFacade().isAPseudostate(toPort)) {
			if ((Model.getPseudostateKind().getInitial()).equals(Model.getFacade().getKind(toPort))) {
				return false;
			}
		}
		return true;
	}
	public Object connect(Object fromPort,Object toPort,Object edgeClass) {
		if (Model.getFacade().isAFinalState(fromPort)) {
			return null;
		}
		if (Model.getFacade().isAPseudostate(toPort)&&Model.getPseudostateKind().getInitial().equals(Model.getFacade().getKind(toPort))) {
			return null;
		}
		if (Model.getMetaTypes().getTransition().equals(edgeClass)) {
			Object tr = null;
			tr = Model.getStateMachinesFactory().buildTransition(fromPort,toPort);
			if (canAddEdge(tr)) {
				addEdge(tr);
			}else {
				ProjectManager.getManager().getCurrentProject().moveToTrash(tr);
				tr = null;
			}
			return tr;
		}else if (edgeClass == CommentEdge.class) {
			try {
				Object connection = buildConnection(edgeClass,fromPort,null,toPort,null,null,ProjectManager.getManager().getCurrentProject().getModel());
				addEdge(connection);
				return connection;
			}catch (Exception ex) {
				LOG.error("buildConnection() failed",ex);
			}
			return null;
		}else {
			LOG.debug("wrong kind of edge in StateDiagram connect3 " + edgeClass);
			return null;
		}
	}
	public void vetoableChange(PropertyChangeEvent pce) {
		if ("ownedElement".equals(pce.getPropertyName())) {
			Collection oldOwned = (Collection) pce.getOldValue();
			Object eo = pce.getNewValue();
			Object me = Model.getFacade().getModelElement(eo);
			if (oldOwned.contains(eo)) {
				LOG.debug("model removed " + me);
				if (Model.getFacade().isAState(me)) {
					removeNode(me);
				}
				if (Model.getFacade().isAPseudostate(me)) {
					removeNode(me);
				}
				if (Model.getFacade().isATransition(me)) {
					removeEdge(me);
				}
			}else {
				LOG.debug("model added " + me);
			}
		}
	}
	static final long serialVersionUID = -8056507319026044174l;
	public boolean canChangeConnectedNode(Object newNode,Object oldNode,Object edge) {
		if (newNode == oldNode) {
			return false;
		}
		if (!(Model.getFacade().isAState(newNode)||Model.getFacade().isAState(oldNode)||Model.getFacade().isATransition(edge))) {
			return false;
		}
		Object otherSideNode = Model.getFacade().getSource(edge);
		if (otherSideNode == oldNode) {
			otherSideNode = Model.getFacade().getTarget(edge);
		}
		if (Model.getFacade().isACompositeState(newNode)&&Model.getStateMachinesHelper().getAllSubStates(newNode).contains(otherSideNode)) {
			return false;
		}
		return true;
	}
	public void changeConnectedNode(Object newNode,Object oldNode,Object edge,boolean isSource) {
		if (isSource) {
			Model.getStateMachinesHelper().setSource(edge,newNode);
		}else {
			Model.getCommonBehaviorHelper().setTarget(edge,newNode);
		}
	}
	public boolean isRemoveFromDiagramAllowed(Collection figs) {
		if (figs.isEmpty()) {
			return false;
		}
		Iterator i = figs.iterator();
		while (i.hasNext()) {
			Object obj = i.next();
			if (!(obj instanceof Fig)) {
				return false;
			}
			Object uml = ((Fig) obj).getOwner();
			if (uml != null) {
				return false;
			}
		}
		return true;
	}
}



