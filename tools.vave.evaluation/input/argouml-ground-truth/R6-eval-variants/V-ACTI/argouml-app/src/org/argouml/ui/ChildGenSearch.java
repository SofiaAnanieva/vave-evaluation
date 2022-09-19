package org.argouml.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.util.ChildGenerator;


public class ChildGenSearch implements ChildGenerator {
	public Iterator childIterator(Object o) {
		List res = new ArrayList();
		if (o instanceof Project) {
			Project p = (Project) o;
			res.addAll(p.getUserDefinedModelList());
			res.addAll(p.getDiagramList());
		}else if (o instanceof ArgoDiagram) {
			ArgoDiagram d = (ArgoDiagram) o;
			res.addAll(d.getGraphModel().getNodes());
			res.addAll(d.getGraphModel().getEdges());
		}else if (Model.getFacade().isAModelElement(o)) {
			res.addAll(Model.getFacade().getModelElementContents(o));
		}
		return res.iterator();
	}
}



