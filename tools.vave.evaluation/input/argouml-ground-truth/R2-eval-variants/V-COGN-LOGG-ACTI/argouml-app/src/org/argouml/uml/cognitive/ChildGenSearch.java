package org.argouml.uml.cognitive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.util.ChildGenerator;


public class ChildGenSearch implements ChildGenerator {
	private static final ChildGenSearch INSTANCE = new ChildGenSearch();
	private ChildGenSearch() {
		super();
	}
	public Iterator childIterator(Object parent) {
		List res = new ArrayList();
		if (parent instanceof Project) {
			Project p = (Project) parent;
			res.addAll(p.getUserDefinedModelList());
			res.addAll(p.getDiagramList());
		}else if (parent instanceof ArgoDiagram) {
			ArgoDiagram d = (ArgoDiagram) parent;
			res.addAll(d.getGraphModel().getNodes());
			res.addAll(d.getGraphModel().getEdges());
		}else if (Model.getFacade().isAModelElement(parent)) {
			res.addAll(Model.getFacade().getModelElementContents(parent));
		}
		return res.iterator();
	}
	public static ChildGenSearch getInstance() {
		return INSTANCE;
	}
}



