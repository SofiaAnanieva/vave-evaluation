package org.argouml.persistence;

import java.util.StringTokenizer;
import org.tigris.gef.persistence.pgml.PGMLStackParser;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigEdgePoly;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigPoly;
import org.xml.sax.SAXException;


class FigEdgeHandler extends org.tigris.gef.persistence.pgml.FigEdgeHandler {
	public FigEdgeHandler(PGMLStackParser parser,FigEdge theEdge) {
		super(parser,theEdge);
	}
	public void addObject(Object o)throws SAXException {
		FigEdge edge = getFigEdge();
		if (o instanceof FigLine||o instanceof FigPoly) {
			edge.setFig((Fig) o);
			if (o instanceof FigPoly) {
				((FigPoly) o).setComplete(true);
			}
			edge.calcBounds();
			if (edge instanceof FigEdgePoly) {
				((FigEdgePoly) edge).setInitiallyLaidOut(true);
			}
			edge.updateAnnotationPositions();
		}
		if (o instanceof String) {
			PGMLStackParser parser = getPGMLStackParser();
			String body = (String) o;
			StringTokenizer st2 = new StringTokenizer(body,"=\"\' \t\n");
			String sourcePortFig = null;
			String destPortFig = null;
			String sourceFigNode = null;
			String destFigNode = null;
			while (st2.hasMoreElements()) {
				String attribute = st2.nextToken();
				String value = st2.nextToken();
				if (attribute.equals("sourcePortFig")) {
					sourcePortFig = value;
				}
				if (attribute.equals("destPortFig")) {
					destPortFig = value;
				}
				if (attribute.equals("sourceFigNode")) {
					sourceFigNode = value;
				}
				if (attribute.equals("destFigNode")) {
					destFigNode = value;
				}
			}
			((org.argouml.persistence.PGMLStackParser) parser).addFigEdge(edge,sourcePortFig,destPortFig,sourceFigNode,destFigNode);
		}
	}
}



