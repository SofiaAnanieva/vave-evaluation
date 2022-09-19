package org.argouml.uml.cognitive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.ChildGenerator;


@Deprecated public class ChildGenFind implements ChildGenerator {
	private static final ChildGenFind SINGLETON = new ChildGenFind();
	public Enumeration gen(Object o) {
		List res = new ArrayList();
		if (o instanceof Project) {
			Project p = (Project) o;
			res.addAll(p.getUserDefinedModelList());
			res.addAll(p.getDiagramList());
		}else if (o instanceof Diagram) {
			Diagram d = (Diagram) o;
			res.addAll(d.getGraphModel().getNodes());
			res.addAll(d.getGraphModel().getEdges());
		}else if (Model.getFacade().isAModelElement(o)) {
			res.addAll(Model.getFacade().getModelElementContents(o));
		}
		return Collections.enumeration(res);
	}
	public static ChildGenFind getSingleton() {
		return SINGLETON;
	}
}



