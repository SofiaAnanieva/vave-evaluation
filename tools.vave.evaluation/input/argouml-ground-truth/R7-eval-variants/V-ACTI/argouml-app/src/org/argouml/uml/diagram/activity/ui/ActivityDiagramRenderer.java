package org.argouml.uml.diagram.activity.ui;

import java.util.Map;
import org.argouml.uml.diagram.state.ui.StateDiagramRenderer;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigNode;


public class ActivityDiagramRenderer extends StateDiagramRenderer {
	@Override public FigNode getFigNodeFor(GraphModel gm,Layer lay,Object node,Map styleAttributes) {
		FigNode figNode = null;
		Diagram diag = ((LayerPerspective) lay).getDiagram();
		if (diag instanceof UMLDiagram&&((UMLDiagram) diag).doesAccept(node)) {
			figNode = ((UMLDiagram) diag).drop(node,null);
		}else {
			figNode = super.getFigNodeFor(gm,lay,node,styleAttributes);
			if (figNode == null) {
				return null;
			}
		}
		lay.add(figNode);
		return figNode;
	}
}



