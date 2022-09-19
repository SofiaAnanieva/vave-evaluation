package org.argouml.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.argouml.uml.diagram.ui.FigEdgeAssociationClass;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigGroup;


public final class PgmlUtility {
	private PgmlUtility() {
	}
	public static String getVisibility(Fig f) {
		if (f.isVisible())return null;
		return"0";
	}
	public static List getEdges(Diagram diagram) {
		Layer lay = diagram.getLayer();
		Collection edges = lay.getContentsEdgesOnly();
		List returnEdges = new ArrayList(edges.size());
		getEdges(diagram,edges,returnEdges);
		return returnEdges;
	}
	public static List getContents(Diagram diagram) {
		Layer lay = diagram.getLayer();
		List contents = lay.getContents();
		int size = contents.size();
		List list = new ArrayList(size);
		for (int i = 0;i < size;i++) {
			Object o = contents.get(i);
			if (!(o instanceof FigEdge)) {
				list.add(o);
			}
		}
		getEdges(diagram,lay.getContentsEdgesOnly(),list);
		return list;
	}
	private static void getEdges(Diagram diagram,Collection edges,List returnEdges) {
		Iterator it = edges.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (!(o instanceof FigEdgeNote)&&!(o instanceof FigEdgeAssociationClass)) {
				returnEdges.add(o);
			}
		}
		it = edges.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof FigEdgeAssociationClass) {
				returnEdges.add(o);
			}
		}
		it = edges.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof FigEdgeNote) {
				returnEdges.add(o);
			}
		}
	}
	public static String getEnclosingId(Fig f) {
		Fig encloser = f.getEnclosingFig();
		if (encloser == null) {
			return null;
		}
		return getId(encloser);
	}
	public static String getId(Fig f) {
		if (f == null) {
			throw new IllegalArgumentException("A fig must be supplied");
		}
		if (f.getGroup() != null) {
			String groupId = f.getGroup().getId();
			if (f.getGroup()instanceof FigGroup) {
				FigGroup group = (FigGroup) f.getGroup();
				return groupId + "." + (group.getFigs()).indexOf(f);
			}else if (f.getGroup()instanceof FigEdge) {
				FigEdge edge = (FigEdge) f.getGroup();
				return groupId + "." + (((List) edge.getPathItemFigs()).indexOf(f) + 1);
			}else {
				return groupId + ".0";
			}
		}
		Layer layer = f.getLayer();
		if (layer == null) {
			return"LAYER_NULL";
		}
		List c = layer.getContents();
		int index = c.indexOf(f);
		return"Fig" + index;
	}
}



