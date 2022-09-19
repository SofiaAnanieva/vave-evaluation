package org.argouml.uml.ui;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramSettings;


public class ActionClassDiagram extends ActionAddDiagram {
	private static final Logger LOG = Logger.getLogger(ActionClassDiagram.class);
	public ActionClassDiagram() {
		super("action.class-diagram");
	}
	@SuppressWarnings("deprecation")@Override public ArgoDiagram createDiagram(Object ns) {
		if (isValidNamespace(ns)) {
			return DiagramFactory.getInstance().createDiagram(DiagramFactory.DiagramType.Class,ns,null);
		}
		LOG.error("No namespace as argument");
		LOG.error(ns);
		throw new IllegalArgumentException("The argument " + ns + "is not a namespace.");
	}
	@Override public ArgoDiagram createDiagram(Object ns,DiagramSettings settings) {
		if (isValidNamespace(ns)) {
			return DiagramFactory.getInstance().create(DiagramFactory.DiagramType.Class,ns,settings);
		}
		LOG.error("No namespace as argument");
		LOG.error(ns);
		throw new IllegalArgumentException("The argument " + ns + "is not a namespace.");
	}
	public boolean isValidNamespace(Object handle) {
		return Model.getFacade().isANamespace(handle);
	}
	private static final long serialVersionUID = 2415943949021223859l;
}



