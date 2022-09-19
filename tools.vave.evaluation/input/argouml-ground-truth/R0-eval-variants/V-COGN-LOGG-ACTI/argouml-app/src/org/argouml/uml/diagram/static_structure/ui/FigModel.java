package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Polygon;
import java.awt.Rectangle;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigPoly;


public class FigModel extends FigPackage {
	private FigPoly figPoly = new FigPoly(LINE_COLOR,SOLID_FILL_COLOR);
	@SuppressWarnings("deprecation")@Deprecated public FigModel(Object modelElement,int x,int y) {
		super(modelElement,x,y);
		constructFigs();
	}
	private void constructFigs() {
		int[]xpoints =  {125,130,135,125};
		int[]ypoints =  {45,40,45,45};
		Polygon polygon = new Polygon(xpoints,ypoints,4);
		figPoly.setPolygon(polygon);
		figPoly.setFilled(false);
		addFig(figPoly);
		setBounds(getBounds());
		updateEdges();
	}
	@Deprecated public FigModel(@SuppressWarnings("unused")GraphModel gm,Object node) {
		this(node,0,0);
	}
	public FigModel(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		constructFigs();
	}
	@Override protected void setStandardBounds(int x,int y,int w,int h) {
		if (figPoly != null) {
			Rectangle oldBounds = getBounds();
			figPoly.translate((x - oldBounds.x) + (w - oldBounds.width),y - oldBounds.y);
		}
		super.setStandardBounds(x,y,w,h);
	}
}



