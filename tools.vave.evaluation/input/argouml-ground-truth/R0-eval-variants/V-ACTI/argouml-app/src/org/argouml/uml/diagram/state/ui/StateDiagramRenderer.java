package org.argouml.uml.diagram.state.ui;

import java.util.Map;
import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UmlDiagramRenderer;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;


public class StateDiagramRenderer extends UmlDiagramRenderer {
	public FigNode getFigNodeFor(GraphModel gm,Layer lay,Object node,Map styleAttributes) {
		assert node != null;
		FigNode figNode = null;
		Diagram diag = ((LayerPerspective) lay).getDiagram();
		if (diag instanceof UMLDiagram&&((UMLDiagram) diag).doesAccept(node)) {
			figNode = ((UMLDiagram) diag).drop(node,null);
		}else {
			throw new IllegalArgumentException("Node is not a recognised type. Received " + node.getClass().getName());
		}
		lay.add(figNode);
		return figNode;
	}
	public FigEdge getFigEdgeFor(GraphModel gm,Layer lay,Object edge,Map styleAttributes) {
		assert edge != null;
		assert lay instanceof LayerPerspective;
		ArgoDiagram diag = (ArgoDiagram) ((LayerPerspective) lay).getDiagram();
		DiagramSettings settings = diag.getDiagramSettings();
		FigEdge newEdge = null;
		if (Model.getFacade().isATransition(edge)) {
			newEdge = new FigTransition(edge,settings);
		}else if (edge instanceof CommentEdge) {
			newEdge = new FigEdgeNote(edge,settings);
		}
		if (newEdge == null) {
			return null;
		}
		lay.add(newEdge);
		return newEdge;
	}
}



