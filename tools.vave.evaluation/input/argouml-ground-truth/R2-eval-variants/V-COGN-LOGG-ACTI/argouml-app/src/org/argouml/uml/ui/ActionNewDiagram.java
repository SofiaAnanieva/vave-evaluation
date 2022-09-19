package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.tigris.gef.undo.UndoableAction;


public abstract class ActionNewDiagram extends UndoableAction {
	private static final Logger LOG = Logger.getLogger(ActionNewDiagram.class);
	protected ActionNewDiagram(String name) {
		super(Translator.localize(name),ResourceLoaderWrapper.lookupIcon(name));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize(name));
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Project p = ProjectManager.getManager().getCurrentProject();
		Object ns = findNamespace();
		if (ns != null&&isValidNamespace(ns)) {
			ArgoDiagram diagram = createDiagram(ns);
			assert(diagram != null):"No diagram was returned by the concrete class";
			p.addMember(diagram);
			ExplorerEventAdaptor.getInstance().modelElementAdded(diagram.getNamespace());
			TargetManager.getInstance().setTarget(diagram);
		}else {
			LOG.error("No valid namespace found");
			throw new IllegalStateException("No valid namespace found");
		}
	}
	protected Object findNamespace() {
		Project p = ProjectManager.getManager().getCurrentProject();
		return p.getRoot();
	}
	protected abstract ArgoDiagram createDiagram(Object namespace);
	public boolean isValidNamespace(Object ns) {
		return true;
	}
}



