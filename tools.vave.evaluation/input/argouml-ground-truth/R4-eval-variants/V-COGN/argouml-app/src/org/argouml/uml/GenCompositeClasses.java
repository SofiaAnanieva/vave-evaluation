package org.argouml.uml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.argouml.model.Model;
import org.tigris.gef.util.ChildGenerator;


public class GenCompositeClasses implements ChildGenerator {
	private static final GenCompositeClasses SINGLETON = new GenCompositeClasses();
	public static GenCompositeClasses getSINGLETON() {
		return SINGLETON;
	}
	public Enumeration gen(Object o) {
		return Collections.enumeration(collectChildren(o));
	}
	protected Collection collectChildren(Object o) {
		List res = new ArrayList();
		if (!(Model.getFacade().isAClassifier(o))) {
			return res;
		}
		Object cls = o;
		List ends = new ArrayList(Model.getFacade().getAssociationEnds(cls));
		if (ends == null) {
			return res;
		}
		Iterator assocEnds = ends.iterator();
		while (assocEnds.hasNext()) {
			Object ae = assocEnds.next();
			if (Model.getAggregationKind().getComposite().equals(Model.getFacade().getAggregation(ae))) {
				Object asc = Model.getFacade().getAssociation(ae);
				ArrayList conn = new ArrayList(Model.getFacade().getConnections(asc));
				if (conn == null||conn.size() != 2) {
					continue;
				}
				Object otherEnd = (ae == conn.get(0))?conn.get(1):conn.get(0);
				if (Model.getFacade().getType(ae) != Model.getFacade().getType(otherEnd)) {
					res.add(Model.getFacade().getType(otherEnd));
				}
			}
		}
		return res;
	}
	private static final long serialVersionUID = -6027679124153204193l;
}



