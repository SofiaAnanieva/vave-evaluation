package org.argouml.uml.diagram;

import java.util.Map;
import org.argouml.model.CoreFactory;
import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.activity.ui.FigActionState;
import org.argouml.uml.diagram.activity.ui.FigCallState;
import org.argouml.uml.diagram.activity.ui.FigObjectFlowState;
import org.argouml.uml.diagram.activity.ui.FigPartition;
import org.argouml.uml.diagram.activity.ui.FigSubactivityState;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.static_structure.ui.FigDataType;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.argouml.uml.diagram.static_structure.ui.FigEnumeration;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.static_structure.ui.FigLink;
import org.argouml.uml.diagram.static_structure.ui.FigModel;
import org.argouml.uml.diagram.static_structure.ui.FigPackage;
import org.argouml.uml.diagram.static_structure.ui.FigStereotypeDeclaration;
import org.argouml.uml.diagram.static_structure.ui.FigSubsystem;
import org.argouml.uml.diagram.ui.FigAbstraction;
import org.argouml.uml.diagram.ui.FigAssociation;
import org.argouml.uml.diagram.ui.FigAssociationClass;
import org.argouml.uml.diagram.ui.FigAssociationEnd;
import org.argouml.uml.diagram.ui.FigClassAssociationClass;
import org.argouml.uml.diagram.ui.FigDependency;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigGeneralization;
import org.argouml.uml.diagram.ui.FigMessage;
import org.argouml.uml.diagram.ui.FigNodeAssociation;
import org.argouml.uml.diagram.ui.FigPermission;
import org.argouml.uml.diagram.ui.FigUsage;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphEdgeRenderer;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;


public abstract class UmlDiagramRenderer implements GraphNodeRenderer,GraphEdgeRenderer {
	@Deprecated public FigNode getFigNodeFor(Object node,int x,int y,Map styleAttributes) {
		if (node == null) {
			throw new IllegalArgumentException("A model element must be supplied");
		}
		FigNode figNode = null;
		if (Model.getFacade().isAComment(node)) {
			figNode = new FigComment();
		}else if (Model.getFacade().isAAssociationClass(node)) {
			figNode = new FigClassAssociationClass(node,x,y,10,10);
		}else if (Model.getFacade().isAClass(node)) {
			figNode = new FigClass(node,x,y,10,10);
		}else if (Model.getFacade().isAInterface(node)) {
			figNode = new FigInterface();
		}else if (Model.getFacade().isAEnumeration(node)) {
			figNode = new FigEnumeration();
		}else if (Model.getFacade().isAStereotype(node)) {
			figNode = new FigStereotypeDeclaration();
		}else if (Model.getFacade().isADataType(node)) {
			figNode = new FigDataType();
		}else if (Model.getFacade().isAModel(node)) {
			figNode = new FigModel(node,x,y);
		}else if (Model.getFacade().isASubsystem(node)) {
			figNode = new FigSubsystem(node,x,y);
		}else if (Model.getFacade().isAPackage(node)) {
			figNode = new FigPackage(node,x,y);
		}else if (Model.getFacade().isAAssociation(node)) {
			figNode = new FigNodeAssociation();
		}else if (Model.getFacade().isAPartition(node)) {
			figNode = new FigPartition();
		}else if (Model.getFacade().isACallState(node)) {
			figNode = new FigCallState();
		}else if (Model.getFacade().isAObjectFlowState(node)) {
			figNode = new FigObjectFlowState();
		}else if (Model.getFacade().isASubactivityState(node)) {
			figNode = new FigSubactivityState();
		}else if (Model.getFacade().isAMessage(node)) {
			figNode = new FigMessage();
		}else if (Model.getFacade().isAComment(node)) {
			figNode = new FigComment();
		}else if (Model.getFacade().isAActionState(node)) {
			figNode = new FigActionState();
		}
		if (figNode == null) {
			throw new IllegalArgumentException("Failed to construct a FigNode for " + node);
		}
		setStyleAttributes(figNode,styleAttributes);
		return figNode;
	}
	private void setStyleAttributes(Fig fig,Map<String,String>attributeMap) {
		String name;
		String value;
		for (Map.Entry<String,String>entry:attributeMap.entrySet()) {
			name = entry.getKey();
			value = entry.getValue();
			if ("operationsVisible".equals(name)) {
				((OperationsCompartmentContainer) fig).setOperationsVisible(value.equalsIgnoreCase("true"));
			}else if ("attributesVisible".equals(name)) {
				((AttributesCompartmentContainer) fig).setAttributesVisible(value.equalsIgnoreCase("true"));
			}
		}
	}
	@Deprecated public FigEdge getFigEdgeFor(Object edge,Map styleAttributes) {
		if (edge == null) {
			throw new IllegalArgumentException("A model edge must be supplied");
		}
		FigEdge newEdge = null;
		if (Model.getFacade().isAAssociationClass(edge)) {
			newEdge = new FigAssociationClass();
		}else if (Model.getFacade().isAAssociationEnd(edge)) {
			newEdge = new FigAssociationEnd();
		}else if (Model.getFacade().isAAssociation(edge)) {
			newEdge = new FigAssociation();
		}else if (Model.getFacade().isALink(edge)) {
			newEdge = new FigLink();
		}else if (Model.getFacade().isAGeneralization(edge)) {
			newEdge = new FigGeneralization();
		}else if (Model.getFacade().isAPackageImport(edge)) {
			newEdge = new FigPermission();
		}else if (Model.getFacade().isAUsage(edge)) {
			newEdge = new FigUsage();
		}else if (Model.getFacade().isADependency(edge)) {
			if (Model.getExtensionMechanismsHelper().hasStereotype(edge,CoreFactory.REALIZE_STEREOTYPE)) {
				newEdge = new FigAbstraction();
			}else {
				newEdge = new FigDependency();
			}
		}else if (edge instanceof CommentEdge) {
			newEdge = null;
		}
		if (newEdge == null) {
			throw new IllegalArgumentException("Failed to construct a FigEdge for " + edge);
		}
		return newEdge;
	}
	protected final void setPorts(Layer layer,FigEdge newEdge) {
		Object modelElement = newEdge.getOwner();
		if (newEdge.getSourcePortFig() == null) {
			Object source;
			if (modelElement instanceof CommentEdge) {
				source = ((CommentEdge) modelElement).getSource();
			}else {
				source = Model.getUmlHelper().getSource(modelElement);
			}
			FigNode sourceNode = getNodePresentationFor(layer,source);
			assert(sourceNode != null):"No FigNode found for " + source;
			setSourcePort(newEdge,sourceNode);
		}
		if (newEdge.getDestPortFig() == null) {
			Object dest;
			if (modelElement instanceof CommentEdge) {
				dest = ((CommentEdge) modelElement).getDestination();
			}else {
				dest = Model.getUmlHelper().getDestination(newEdge.getOwner());
			}
			setDestPort(newEdge,getNodePresentationFor(layer,dest));
		}
		if (newEdge.getSourcePortFig() == null||newEdge.getDestPortFig() == null) {
			throw new IllegalStateException("Edge of type " + newEdge.getClass().getName() + " created with no source or destination port");
		}
	}
	private void setSourcePort(FigEdge edge,FigNode source) {
		edge.setSourcePortFig(source);
		edge.setSourceFigNode(source);
	}
	private void setDestPort(FigEdge edge,FigNode dest) {
		edge.setDestPortFig(dest);
		edge.setDestFigNode(dest);
	}
	private FigNode getNodePresentationFor(Layer lay,Object modelElement) {
		assert modelElement != null:"A modelElement must be supplied";
		for (Object fig:lay.getContentsNoEdges()) {
			if (fig instanceof FigNode&&((FigNode) fig).getOwner().equals(modelElement)) {
				return((FigNode) fig);
			}
		}
		for (Object fig:lay.getContentsEdgesOnly()) {
			if (fig instanceof FigEdgeModelElement&&modelElement.equals(((FigEdgeModelElement) fig).getOwner())) {
				return((FigEdgeModelElement) fig).getEdgePort();
			}
		}
		return null;
	}
}



