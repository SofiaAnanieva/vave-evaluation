package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.Diagram;


public class GoDiagramToNode extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.diagram.node");
	}
	public Collection getChildren(Object parent) {
		if (parent instanceof Diagram) {
			return((Diagram) parent).getNodes();
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		return Collections.EMPTY_SET;
	}
}



