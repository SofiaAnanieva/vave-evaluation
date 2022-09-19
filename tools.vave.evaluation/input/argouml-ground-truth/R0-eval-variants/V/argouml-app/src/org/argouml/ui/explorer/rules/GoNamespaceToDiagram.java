package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;


public class GoNamespaceToDiagram extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.package.diagram");
	}
	public Collection getChildren(Object namespace) {
		if (Model.getFacade().isANamespace(namespace)) {
			List returnList = new ArrayList();
			Project proj = ProjectManager.getManager().getCurrentProject();
			for (ArgoDiagram diagram:proj.getDiagramList()) {
				if (diagram.getNamespace() == namespace) {
					returnList.add(diagram);
				}
			}
			return returnList;
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		return Collections.EMPTY_SET;
	}
}



