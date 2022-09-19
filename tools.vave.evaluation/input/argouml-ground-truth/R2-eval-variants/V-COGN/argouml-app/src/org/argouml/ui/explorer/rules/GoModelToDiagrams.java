package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;


public class GoModelToDiagrams extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.model.diagram");
	}
	public Collection getChildren(Object model) {
		if (Model.getFacade().isAModel(model)) {
			List returnList = new ArrayList();
			Project proj = ProjectManager.getManager().getCurrentProject();
			for (ArgoDiagram diagram:proj.getDiagramList()) {
				if (isInPath(diagram.getNamespace(),model)) {
					returnList.add(diagram);
				}
			}
			return returnList;
		}
		return Collections.EMPTY_SET;
	}
	private boolean isInPath(Object namespace,Object model) {
		if (namespace == model) {
			return true;
		}
		Object ns = Model.getFacade().getNamespace(namespace);
		while (ns != null) {
			if (model == ns) {
				return true;
			}
			ns = Model.getFacade().getNamespace(ns);
		}
		return false;
	}
	public Set getDependencies(Object parent) {
		if (Model.getFacade().isAModel(parent)) {
			Set set = new HashSet();
			set.add(parent);
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



