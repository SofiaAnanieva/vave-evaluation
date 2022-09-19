package org.argouml.uml.diagram.static_structure.layout;

import org.apache.log4j.Logger;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;


public abstract class ClassdiagramInheritanceEdge extends ClassdiagramEdge {
	private static final Logger LOG = Logger.getLogger(ClassdiagramInheritanceEdge.class);
	private static final int EPSILON = 5;
	private Fig high,low;
	private int offset;
	public ClassdiagramInheritanceEdge(FigEdge edge) {
		super(edge);
		high = getDestFigNode();
		low = getSourceFigNode();
		offset = 0;
	}
	public int getVerticalOffset() {
		return(getVGap() / 2) - 10 + getOffset();
	}
	public int getCenterHigh() {
		return(int) (high.getLocation().getX() + high.getSize().width / 2) + getOffset();
	}
	public int getCenterLow() {
		return(int) (low.getLocation().getX() + low.getSize().width / 2) + getOffset();
	}
	public int getDownGap() {
		return(int) (low.getLocation().getY() - getVerticalOffset());
	}
	public void layout() {
		Fig fig = getUnderlyingFig();
		int centerHigh = getCenterHigh();
		int centerLow = getCenterLow();
		int difference = centerHigh - centerLow;
		if (Math.abs(difference) < EPSILON) {
			fig.addPoint(centerLow + (difference / 2 + (difference % 2)),(int) (low.getLocation().getY()));
			fig.addPoint(centerHigh - (difference / 2),high.getLocation().y + high.getSize().height);
		}else {
			fig.addPoint(centerLow,(int) (low.getLocation().getY()));
			if (LOG.isDebugEnabled()) {
				LOG.debug("Point: x: " + centerLow + " y: " + low.getLocation().y);
			}
			getUnderlyingFig().addPoint(centerHigh - difference,getDownGap());
			getUnderlyingFig().addPoint(centerHigh,getDownGap());
			if (LOG.isDebugEnabled()) {
				LOG.debug("Point: x: " + (centerHigh - difference) + " y: " + getDownGap());
				LOG.debug("Point: x: " + centerHigh + " y: " + getDownGap());
			}
			fig.addPoint(centerHigh,high.getLocation().y + high.getSize().height);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Point x: " + centerHigh + " y: " + (high.getLocation().y + high.getSize().height));
			}
		}
		fig.setFilled(false);
		getCurrentEdge().setFig(getUnderlyingFig());
	}
	public void setOffset(int anOffset) {
		offset = anOffset;
	}
	public int getOffset() {
		return offset;
	}
}



