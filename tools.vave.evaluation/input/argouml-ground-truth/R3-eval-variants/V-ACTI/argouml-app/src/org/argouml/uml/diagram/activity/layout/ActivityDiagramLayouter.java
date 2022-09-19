package org.argouml.uml.diagram.activity.layout;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.layout.LayoutedObject;
import org.argouml.uml.diagram.layout.Layouter;
import org.tigris.gef.presentation.Fig;


public class ActivityDiagramLayouter implements Layouter {
	private ArgoDiagram diagram;
	private List objects = new ArrayList();
	private static final Point STARTING_POINT = new Point(100,10);
	private static final int OFFSET_Y = 25;
	private Object finalState = null;
	public ActivityDiagramLayouter(ArgoDiagram d) {
		this.diagram = d;
	}
	public void add(LayoutedObject object) {
		objects.add(object);
	}
	public void remove(LayoutedObject object) {
		objects.remove(object);
	}
	public LayoutedObject[]getObjects() {
		return(LayoutedObject[]) objects.toArray();
	}
	public LayoutedObject getObject(int index) {
		return(LayoutedObject) objects.get(index);
	}
	public void layout() {
		Object first = null;
		for (Iterator it = diagram.getNodes().iterator();it.hasNext();) {
			Object node = it.next();
			if (Model.getFacade().isAPseudostate(node)&&Model.getDataTypesHelper().equalsINITIALKind(Model.getFacade().getKind(node))) {
				first = node;
				break;
			}
		}
		assert first != null;
		assert Model.getFacade().getIncomings(first).isEmpty();
		int lastIndex = placeNodes(new ArrayList(),first,0);
		Point location = new Point(STARTING_POINT);
		location.y += OFFSET_Y * (lastIndex + 2);
		diagram.getContainingFig(finalState).setLocation(location);
	}
	private int placeNodes(List seen,Object node,int index) {
		if (!seen.contains(node)) {
			seen.add(node);
			if (Model.getFacade().isAFinalState(node)) {
				finalState = node;
			}
			Fig fig = diagram.getContainingFig(node);
			Point location = new Point(STARTING_POINT.x - fig.getWidth() / 2,STARTING_POINT.y + OFFSET_Y * index++);
			fig.setLocation(location);
			for (Iterator it = Model.getFacade().getOutgoings(node).iterator();it.hasNext();) {
				index = placeNodes(seen,Model.getFacade().getTarget(it.next()),index);
			}
		}
		return index;
	}
	public Dimension getMinimumDiagramSize() {
		return new Dimension(STARTING_POINT.x + 300,STARTING_POINT.y + OFFSET_Y * objects.size());
	}
}



