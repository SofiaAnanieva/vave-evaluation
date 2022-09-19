package org.argouml.uml.diagram.static_structure.layout;

import org.argouml.uml.diagram.layout.LayoutedEdge;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigPoly;


public abstract class ClassdiagramEdge implements LayoutedEdge {
	private static int vGap;
	private static int hGap;
	private FigEdge currentEdge = null;
	private FigPoly underlyingFig = null;
	private Fig destFigNode;
	private Fig sourceFigNode;
	public ClassdiagramEdge(FigEdge edge) {
		currentEdge = edge;
		underlyingFig = new FigPoly();
		underlyingFig.setLineColor(edge.getFig().getLineColor());
		destFigNode = edge.getDestFigNode();
		sourceFigNode = edge.getSourceFigNode();
	}
	public abstract void layout();
	public static void setHGap(int h) {
		hGap = h;
	}
	public static void setVGap(int v) {
		vGap = v;
	}
	public static int getHGap() {
		return hGap;
	}
	public static int getVGap() {
		return vGap;
	}
	Fig getDestFigNode() {
		return destFigNode;
	}
	Fig getSourceFigNode() {
		return sourceFigNode;
	}
	protected FigEdge getCurrentEdge() {
		return currentEdge;
	}
	protected FigPoly getUnderlyingFig() {
		return underlyingFig;
	}
}



