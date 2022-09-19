package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Polygon;
import java.awt.Rectangle;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigPoly;
import org.argouml.uml.diagram.static_structure.ui.FigPackage;


public class FigSubsystem extends FigPackage {
	private FigPoly figPoly = new FigPoly(LINE_COLOR,SOLID_FILL_COLOR);
	@SuppressWarnings("deprecation")@Deprecated public FigSubsystem(Object modelElement,int x,int y) {
		super(modelElement,x,y);
		constructFigs();
	}
	private void constructFigs() {
		int[]xpoints =  {125,125,130,130,130,135,135};
		int[]ypoints =  {45,40,40,35,40,40,45};
		Polygon polygon = new Polygon(xpoints,ypoints,7);
		figPoly.setPolygon(polygon);
		figPoly.setFilled(false);
		addFig(figPoly);
		Rectangle r = getBounds();
		setBounds(r.x,r.y,r.width,r.height);
		updateEdges();
	}
	@Deprecated public FigSubsystem(@SuppressWarnings("unused")GraphModel gm,Object node) {
		this(node,0,0);
	}
	public FigSubsystem(Object owner,Rectangle bounds,DiagramSettings settings) {
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



