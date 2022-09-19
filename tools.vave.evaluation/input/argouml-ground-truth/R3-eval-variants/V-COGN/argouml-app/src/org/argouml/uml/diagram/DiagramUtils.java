package org.argouml.uml.diagram;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerManager;
import org.tigris.gef.base.LayerPerspective;


public class DiagramUtils {
	private DiagramUtils() {
	}
	public static ArgoDiagram getActiveDiagram() {
		LayerPerspective layer = getActiveLayer();
		if (layer != null) {
			return(ArgoDiagram) layer.getDiagram();
		}
		return null;
	}
	private static LayerPerspective getActiveLayer() {
		Editor editor = Globals.curEditor();
		if (editor != null) {
			LayerManager manager = editor.getLayerManager();
			if (manager != null) {
				Layer layer = manager.getActiveLayer();
				if (layer instanceof LayerPerspective) {
					return(LayerPerspective) layer;
				}
			}
		}
		return null;
	}
}



