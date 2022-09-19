package org.argouml.uml.diagram.static_structure;

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.UMLMutableGraphSupport;


public class ClassDiagramGraphModel extends UMLMutableGraphSupport implements VetoableChangeListener {
	private static final Logger LOG = Logger.getLogger(ClassDiagramGraphModel.class);
	public List<Object>getPorts(Object nodeOrEdge) {
		List<Object>res = new ArrayList<Object>();
		if (Model.getFacade().isAClassifier(nodeOrEdge)) {
			res.add(nodeOrEdge);
		}
		if (Model.getFacade().isAInstance(nodeOrEdge)) {
			res.add(nodeOrEdge);
		}
		if (Model.getFacade().isAModel(nodeOrEdge)) {
			res.add(nodeOrEdge);
		}
		if (Model.getFacade().isAStereotype(nodeOrEdge)) {
			res.add(nodeOrEdge);
		}
		if (Model.getFacade().isASignal(nodeOrEdge)) {
			res.add(nodeOrEdge);
		}
		return res;
	}
	public Object getOwner(Object port) {
		return port;
	}
	public List getInEdges(Object port) {
		List<Object>edges = new ArrayList<Object>();
		if (Model.getFacade().isAModelElement(port)) {
			Iterator it = Model.getFacade().getSupplierDependencies(port).iterator();
			while (it.hasNext()) {
				edges.add(it.next());
			}
		}
		if (Model.getFacade().isAGeneralizableElement(port)) {
			Iterator it = Model.getFacade().getSpecializations(port).iterator();
			while (it.hasNext()) {
				edges.add(it.next());
			}
		}
		if (Model.getFacade().isAClassifier(port)||Model.getFacade().isAPackage(port)) {
			Iterator it = Model.getFacade().getAssociationEnds(port).iterator();
			while (it.hasNext()) {
				Object nextAssocEnd = it.next();
				if (Model.getFacade().isNavigable(nextAssocEnd)) {
					edges.add(nextAssocEnd);
				}
			}
		}
		if (Model.getFacade().isAInstance(port)) {
			Iterator it = Model.getFacade().getLinkEnds(port).iterator();
			while (it.hasNext()) {
				edges.add(it.next());
			}
		}
		return edges;
	}
	public List getOutEdges(Object port) {
		List<Object>edges = new ArrayList<Object>();
		if (Model.getFacade().isAModelElement(port)) {
			Iterator it = Model.getFacade().getClientDependencies(port).iterator();
			while (it.hasNext()) {
				edges.add(it.next());
			}
		}
		if (Model.getFacade().isAGeneralizableElement(port)) {
			Iterator it = Model.getFacade().getGeneralizations(port).iterator();
			while (it.hasNext()) {
				edges.add(it.next());
			}
		}
		if (Model.getFacade().isAClassifier(port)) {
			Iterator it = Model.getFacade().getAssociationEnds(port).iterator();
			while (it.hasNext()) {
				Object thisEnd = it.next();
				Object assoc = Model.getFacade().getAssociation(thisEnd);
				if (assoc != null) {
					Iterator it2 = Model.getFacade().getAssociationEnds(assoc).iterator();
					while (it2.hasNext()) {
						Object nextAssocEnd = it2.next();
						if (!thisEnd.equals(nextAssocEnd)&&Model.getFacade().isNavigable(nextAssocEnd)) {
							edges.add(nextAssocEnd);
						}
					}
				}
			}
		}
		return edges;
	}
	@Override public boolean canAddNode(Object node) {
		if (Model.getFacade().isAAssociation(node)&&!Model.getFacade().isANaryAssociation(node)) {
			LOG.debug("A binary association cannot be added as a node");
			return false;
		}
		if (super.canAddNode(node)&&!containsNode(node)) {
			return true;
		}
		if (containsNode(node)) {
			LOG.error("Addition of node of type " + node.getClass().getName() + " rejected because its already in the graph model");
			return false;
		}
		if (Model.getFacade().isAAssociation(node)) {
			Collection ends = Model.getFacade().getConnections(node);
			Iterator iter = ends.iterator();
			while (iter.hasNext()) {
				Object classifier = Model.getFacade().getClassifier(iter.next());
				if (!containsNode(classifier)) {
					LOG.error("Addition of node of type " + node.getClass().getName() + " rejected because it is connected to a " + "classifier that is not in the diagram");
					return false;
				}
			}
			return true;
		}
		if (Model.getFacade().isAModel(node)) {
			return false;
		}
		if (Model.getFacade().isAClassifierRole(node)) {
			return false;
		}
		return Model.getFacade().isAClassifier(node)||Model.getFacade().isAPackage(node)||Model.getFacade().isAStereotype(node)||Model.getFacade().isASignal(node)||Model.getFacade().isAInstance(node);
	}
	@Override public boolean canAddEdge(Object edge) {
		if (edge == null) {
			return false;
		}
		if (containsEdge(edge)) {
			return false;
		}
		Object sourceModelElement = null;
		Object destModelElement = null;
		if (Model.getFacade().isAAssociation(edge)) {
			Collection conns = Model.getFacade().getConnections(edge);
			if (conns.size() < 2) {
				LOG.error("Association rejected. Must have at least 2 ends");
				return false;
			}
			Iterator iter = conns.iterator();
			Object associationEnd0 = iter.next();
			Object associationEnd1 = iter.next();
			if (associationEnd0 == null||associationEnd1 == null) {
				LOG.error("Association rejected. An end is null");
				return false;
			}
			sourceModelElement = Model.getFacade().getType(associationEnd0);
			destModelElement = Model.getFacade().getType(associationEnd1);
		}else if (Model.getFacade().isAAssociationEnd(edge)) {
			sourceModelElement = Model.getFacade().getAssociation(edge);
			destModelElement = Model.getFacade().getType(edge);
			if (sourceModelElement == null||destModelElement == null) {
				LOG.error("Association end rejected. An end is null");
				return false;
			}
			if (!containsEdge(sourceModelElement)&&!containsNode(sourceModelElement)) {
				LOG.error("Association end rejected. The source model element (" + sourceModelElement.getClass().getName() + ") must be on the diagram");
				return false;
			}
			if (!containsNode(destModelElement)) {
				LOG.error("Association end rejected. " + "The destination model element must be " + "on the diagram.");
				return false;
			}
		}else if (Model.getFacade().isAGeneralization(edge)) {
			sourceModelElement = Model.getFacade().getSpecific(edge);
			destModelElement = Model.getFacade().getGeneral(edge);
		}else if (Model.getFacade().isADependency(edge)) {
			Collection clients = Model.getFacade().getClients(edge);
			Collection suppliers = Model.getFacade().getSuppliers(edge);
			if (clients == null||suppliers == null) {
				return false;
			}
			sourceModelElement = clients.iterator().next();
			destModelElement = suppliers.iterator().next();
		}else if (Model.getFacade().isALink(edge)) {
			Collection roles = Model.getFacade().getConnections(edge);
			if (roles.size() < 2) {
				return false;
			}
			Iterator iter = roles.iterator();
			Object linkEnd0 = iter.next();
			Object linkEnd1 = iter.next();
			if (linkEnd0 == null||linkEnd1 == null) {
				return false;
			}
			sourceModelElement = Model.getFacade().getInstance(linkEnd0);
			destModelElement = Model.getFacade().getInstance(linkEnd1);
		}else if (edge instanceof CommentEdge) {
			sourceModelElement = ((CommentEdge) edge).getSource();
			destModelElement = ((CommentEdge) edge).getDestination();
		}else {
			return false;
		}
		if (sourceModelElement == null||destModelElement == null) {
			LOG.error("Edge rejected. Its ends are not attached to anything");
			return false;
		}
		if (!containsNode(sourceModelElement)&&!containsEdge(sourceModelElement)) {
			LOG.error("Edge rejected. Its source end is attached to " + sourceModelElement + " but this is not in the graph model");
			return false;
		}
		if (!containsNode(destModelElement)&&!containsEdge(destModelElement)) {
			LOG.error("Edge rejected. Its destination end is attached to " + destModelElement + " but this is not in the graph model");
			return false;
		}
		return true;
	}
	@Override public void addNode(Object node) {
		if (!canAddNode(node)) {
			return;
		}
		getNodes().add(node);
		if (Model.getFacade().isAModelElement(node)&&Model.getFacade().getNamespace(node) == null) {
			Model.getCoreHelper().addOwnedElement(getHomeModel(),node);
		}
		fireNodeAdded(node);
	}
	@Override public void addEdge(Object edge) {
		if (edge == null) {
			throw new IllegalArgumentException("Cannot add a null edge");
		}
		if (getDestPort(edge) == null||getSourcePort(edge) == null) {
			throw new IllegalArgumentException("The source and dest port should be provided on an edge");
		}
		if (LOG.isInfoEnabled()) {
			LOG.info("Adding an edge of type " + edge.getClass().getName() + " to class diagram.");
		}
		if (!canAddEdge(edge)) {
			LOG.info("Attempt to add edge rejected");
			return;
		}
		getEdges().add(edge);
		if (Model.getFacade().isAModelElement(edge)&&Model.getFacade().getNamespace(edge) == null&&!Model.getFacade().isAAssociationEnd(edge)) {
			Model.getCoreHelper().addOwnedElement(getHomeModel(),edge);
		}
		fireEdgeAdded(edge);
	}
	@Override public void addNodeRelatedEdges(Object node) {
		super.addNodeRelatedEdges(node);
		if (Model.getFacade().isAClassifier(node)) {
			Collection ends = Model.getFacade().getAssociationEnds(node);
			Iterator iter = ends.iterator();
			while (iter.hasNext()) {
				Object association = Model.getFacade().getAssociation(iter.next());
				if (!Model.getFacade().isANaryAssociation(association)&&canAddEdge(association)) {
					addEdge(association);
				}
			}
		}
		if (Model.getFacade().isAGeneralizableElement(node)) {
			Collection generalizations = Model.getFacade().getGeneralizations(node);
			Iterator iter = generalizations.iterator();
			while (iter.hasNext()) {
				Object generalization = iter.next();
				if (canAddEdge(generalization)) {
					addEdge(generalization);
				}
			}
			Collection specializations = Model.getFacade().getSpecializations(node);
			iter = specializations.iterator();
			while (iter.hasNext()) {
				Object specialization = iter.next();
				if (canAddEdge(specialization)) {
					addEdge(specialization);
				}
			}
		}
		if (Model.getFacade().isAAssociation(node)) {
			Collection ends = Model.getFacade().getConnections(node);
			Iterator iter = ends.iterator();
			while (iter.hasNext()) {
				Object associationEnd = iter.next();
				if (canAddEdge(associationEnd)) {
					addEdge(associationEnd);
				}
			}
		}
	}
	public void vetoableChange(PropertyChangeEvent pce) {
		if ("ownedElement".equals(pce.getPropertyName())) {
			List oldOwned = (List) pce.getOldValue();
			Object elementImport = pce.getNewValue();
			Object modelElement = Model.getFacade().getModelElement(elementImport);
			if (oldOwned.contains(elementImport)) {
				LOG.debug("model removed " + modelElement);
				if (Model.getFacade().isAClassifier(modelElement)) {
					removeNode(modelElement);
				}
				if (Model.getFacade().isAPackage(modelElement)) {
					removeNode(modelElement);
				}
				if (Model.getFacade().isAAssociation(modelElement)) {
					removeEdge(modelElement);
				}
				if (Model.getFacade().isADependency(modelElement)) {
					removeEdge(modelElement);
				}
				if (Model.getFacade().isAGeneralization(modelElement)) {
					removeEdge(modelElement);
				}
			}else {
				LOG.debug("model added " + modelElement);
			}
		}
	}
	static final long serialVersionUID = -2638688086415040146l;
	@Override public boolean canChangeConnectedNode(Object newNode,Object oldNode,Object edge) {
		if (newNode == oldNode) {
			return false;
		}
		if (!(Model.getFacade().isAClass(newNode)||Model.getFacade().isAClass(oldNode)||Model.getFacade().isAAssociation(edge))) {
			return false;
		}
		return true;
	}
	@Override public void changeConnectedNode(Object newNode,Object oldNode,Object edge,boolean isSource) {
		if (Model.getFacade().isAAssociation(edge)) {
			rerouteAssociation(newNode,oldNode,edge,isSource);
		}else if (Model.getFacade().isAGeneralization(edge)) {
			rerouteGeneralization(newNode,oldNode,edge,isSource);
		}else if (Model.getFacade().isADependency(edge)) {
			rerouteDependency(newNode,oldNode,edge,isSource);
		}else if (Model.getFacade().isALink(edge)) {
			rerouteLink(newNode,oldNode,edge,isSource);
		}
	}
	private void rerouteAssociation(Object newNode,Object oldNode,Object edge,boolean isSource) {
		if (!(Model.getFacade().isAClassifier(newNode))||!(Model.getFacade().isAClassifier(oldNode))) {
			return;
		}
		Object otherNode = null;
		if (isSource) {
			otherNode = Model.getCoreHelper().getDestination(edge);
		}else {
			otherNode = Model.getCoreHelper().getSource(edge);
		}
		if (Model.getFacade().isAInterface(newNode)&&Model.getFacade().isAInterface(otherNode)) {
			return;
		}
		Object edgeAssoc = edge;
		Object theEnd = null;
		Object theOtherEnd = null;
		Collection connections = Model.getFacade().getConnections(edgeAssoc);
		Iterator iter = connections.iterator();
		if (isSource) {
			theEnd = iter.next();
			theOtherEnd = iter.next();
		}else {
			theOtherEnd = iter.next();
			theEnd = iter.next();
		}
		Model.getCoreHelper().setType(theEnd,newNode);
	}
	private void rerouteGeneralization(Object newNode,Object oldNode,Object edge,boolean isSource) {
	}
	private void rerouteDependency(Object newNode,Object oldNode,Object edge,boolean isSource) {
	}
	private void rerouteLink(Object newNode,Object oldNode,Object edge,boolean isSource) {
	}
}



