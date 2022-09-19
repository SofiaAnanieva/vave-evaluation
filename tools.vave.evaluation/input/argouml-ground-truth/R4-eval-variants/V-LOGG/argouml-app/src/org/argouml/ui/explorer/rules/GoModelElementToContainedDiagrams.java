package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;


public class GoModelElementToContainedDiagrams extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.model-element.contained-diagrams");
	}
	public Collection getChildren(Object parent) {
		if (Model.getFacade().isAModelElement(parent)) {
			Project p = ProjectManager.getManager().getCurrentProject();
			Set<ArgoDiagram>ret = new HashSet<ArgoDiagram>();
			for (ArgoDiagram diagram:p.getDiagramList()) {
				if (diagram.getNamespace() == parent) {
					ret.add(diagram);
				}
			}
			return ret;
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		Set set = new HashSet();
		if (Model.getFacade().isAModelElement(parent)) {
			set.add(parent);
		}
		return set;
	}
}



