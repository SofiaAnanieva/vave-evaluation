package org.argouml.uml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.argouml.model.Model;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.ChildGenerator;


public class ChildGenRelated implements ChildGenerator {
	private static final ChildGenRelated SINGLETON = new ChildGenRelated();
	public static ChildGenRelated getSingleton() {
		return SINGLETON;
	}
	public Enumeration gen(Object o) {
		if (Model.getFacade().isAPackage(o)) {
			return null;
		}
		if (o instanceof Diagram) {
			List res = new ArrayList();
			Diagram d = (Diagram) o;
			res.add(d.getGraphModel().getNodes());
			res.add(d.getGraphModel().getEdges());
			return Collections.enumeration(res);
		}
		if (Model.getFacade().isAUMLElement(o)) {
			return Collections.enumeration(Model.getFacade().getModelElementAssociated(o));
		}
		throw new IllegalArgumentException("Unknown element type " + o);
	}
	private static final long serialVersionUID = -893946595629032267l;
}



