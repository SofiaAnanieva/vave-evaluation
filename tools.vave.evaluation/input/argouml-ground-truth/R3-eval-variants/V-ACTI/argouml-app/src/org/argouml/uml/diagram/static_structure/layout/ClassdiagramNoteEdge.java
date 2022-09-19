package org.argouml.uml.diagram.static_structure.layout;

import java.awt.Point;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigPoly;


public class ClassdiagramNoteEdge extends ClassdiagramEdge {
	public ClassdiagramNoteEdge(FigEdge edge) {
		super(edge);
	}
	public void layout() {
		Fig fs = getSourceFigNode();
		Fig fd = getDestFigNode();
		if (fs.getLocation().x < fd.getLocation().x) {
			addPoints(fs,fd);
		}else {
			addPoints(fd,fs);
		}
		FigPoly fig = getUnderlyingFig();
		fig.setFilled(false);
		getCurrentEdge().setFig(fig);
	}
	private void addPoints(Fig fs,Fig fd) {
		FigPoly fig = getUnderlyingFig();
		Point p = fs.getLocation();
		p.translate(fs.getWidth(),fs.getHeight() / 2);
		fig.addPoint(p);
		p = fd.getLocation();
		p.translate(0,fd.getHeight() / 2);
		fig.addPoint(p);
	}
}



