package org.argouml.uml.diagram.ui;

import java.awt.Color;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigRect;
import org.argouml.uml.diagram.ui.ArgoFig;
import org.argouml.uml.diagram.ui.PathItemPlacement;


public class ArgoFigUtil {
	public static Project getProject(ArgoFig fig) {
		if (fig instanceof Fig) {
			Fig f = (Fig) fig;
			LayerPerspective layer = (LayerPerspective) f.getLayer();
			if (layer == null) {
				Editor editor = Globals.curEditor();
				if (editor == null) {
					return ProjectManager.getManager().getCurrentProject();
				}
				Layer lay = editor.getLayerManager().getActiveLayer();
				if (lay instanceof LayerPerspective) {
					layer = (LayerPerspective) lay;
				}
			}
			if (layer == null) {
				return ProjectManager.getManager().getCurrentProject();
			}
			GraphModel gm = layer.getGraphModel();
			if (gm instanceof UMLMutableGraphSupport) {
				Project project = ((UMLMutableGraphSupport) gm).getProject();
				if (project != null) {
					return project;
				}
			}
			return ProjectManager.getManager().getCurrentProject();
		}
		return null;
	}
	static void markPosition(FigEdge fe,int pct,int delta,int angle,int offset,Color color) {
		if (false) {
			Fig f;
			f = new FigCircle(0,0,5,5,color,Color.red);
			fe.addPathItem(f,new PathItemPlacement(fe,f,pct,delta,angle,0));
			f = new FigRect(0,0,100,20,color,Color.red);
			f.setFilled(false);
			fe.addPathItem(f,new PathItemPlacement(fe,f,pct,delta,angle,offset));
			f = new FigCircle(0,0,5,5,color,Color.blue);
			fe.addPathItem(f,new PathItemPlacement(fe,f,pct,delta,angle,offset));
		}
	}
}



