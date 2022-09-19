package org.argouml.uml.diagram;

import org.argouml.model.DiDiagram;
import org.argouml.model.DiElement;
import org.argouml.model.Model;
import org.tigris.gef.graph.GraphEvent;
import org.tigris.gef.graph.GraphListener;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.argouml.uml.diagram.UMLMutableGraphSupport;


public final class GraphChangeAdapter implements GraphListener {
	private static final GraphChangeAdapter INSTANCE = new GraphChangeAdapter();
	public static GraphChangeAdapter getInstance() {
		return INSTANCE;
	}
	private GraphChangeAdapter() {
	}
	public DiDiagram createDiagram(Class type,Object owner) {
		if (Model.getDiagramInterchangeModel() != null) {
			return Model.getDiagramInterchangeModel().createDiagram(type,owner);
		}
		return null;
	}
	public void removeDiagram(DiDiagram dd) {
		if (Model.getDiagramInterchangeModel() != null) {
			Model.getDiagramInterchangeModel().deleteDiagram(dd);
		}
	}
	public DiElement createElement(GraphModel gm,Object node) {
		if (Model.getDiagramInterchangeModel() != null) {
			return Model.getDiagramInterchangeModel().createElement(((UMLMutableGraphSupport) gm).getDiDiagram(),node);
		}
		return null;
	}
	public void removeElement(DiElement element) {
		if (Model.getDiagramInterchangeModel() != null) {
			Model.getDiagramInterchangeModel().deleteElement(element);
		}
	}
	public void nodeAdded(GraphEvent e) {
		Object source = e.getSource();
		Object arg = e.getArg();
		if (source instanceof Fig) {
			source = ((Fig) source).getOwner();
		}
		if (arg instanceof Fig) {
			arg = ((Fig) arg).getOwner();
		}
		Model.getDiagramInterchangeModel().nodeAdded(source,arg);
	}
	public void edgeAdded(GraphEvent e) {
		Object source = e.getSource();
		Object arg = e.getArg();
		if (source instanceof Fig) {
			source = ((Fig) source).getOwner();
		}
		if (arg instanceof Fig) {
			arg = ((Fig) arg).getOwner();
		}
		Model.getDiagramInterchangeModel().edgeAdded(source,arg);
	}
	public void nodeRemoved(GraphEvent e) {
		Object source = e.getSource();
		Object arg = e.getArg();
		if (source instanceof Fig) {
			source = ((Fig) source).getOwner();
		}
		if (arg instanceof Fig) {
			arg = ((Fig) arg).getOwner();
		}
		Model.getDiagramInterchangeModel().nodeRemoved(source,arg);
	}
	public void edgeRemoved(GraphEvent e) {
		Object source = e.getSource();
		Object arg = e.getArg();
		if (source instanceof Fig) {
			source = ((Fig) source).getOwner();
		}
		if (arg instanceof Fig) {
			arg = ((Fig) arg).getOwner();
		}
		Model.getDiagramInterchangeModel().edgeRemoved(source,arg);
	}
	public void graphChanged(GraphEvent e) {
		Object source = e.getSource();
		Object arg = e.getArg();
		if (source instanceof Fig) {
			source = ((Fig) source).getOwner();
		}
		if (arg instanceof Fig) {
			arg = ((Fig) arg).getOwner();
		}
		Model.getDiagramInterchangeModel().graphChanged(source,arg);
	}
}



