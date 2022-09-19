package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.undo.UndoableAction;


public abstract class ActionAddDiagram extends UndoableAction {
	public ActionAddDiagram(String s) {
		super(Translator.localize(s),ResourceLoaderWrapper.lookupIcon(s));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize(s));
	}
	@Override public void actionPerformed(ActionEvent e) {
		Project p = ProjectManager.getManager().getCurrentProject();
		Object ns = findNamespace();
		if (ns != null&&isValidNamespace(ns)) {
			super.actionPerformed(e);
			DiagramSettings settings = p.getProjectSettings().getDefaultDiagramSettings();
			ArgoDiagram diagram = createDiagram(ns,settings);
			p.addMember(diagram);
			ExplorerEventAdaptor.getInstance().modelElementAdded(ns);
			TargetManager.getInstance().setTarget(diagram);
		}else {
			throw new IllegalStateException("No valid namespace found");
		}
	}
	protected Object findNamespace() {
		Project p = ProjectManager.getManager().getCurrentProject();
		Object target = TargetManager.getInstance().getModelTarget();
		Object ns = null;
		if (target == null||!Model.getFacade().isAModelElement(target)||Model.getModelManagementHelper().isReadOnly(target)) {
			Collection c = p.getRoots();
			if ((c != null)&&!c.isEmpty()) {
				target = c.iterator().next();
			}
		}
		if (Model.getFacade().isANamespace(target)) {
			ns = target;
		}else {
			Object owner = null;
			if (Model.getFacade().isAOperation(target)) {
				owner = Model.getFacade().getOwner(target);
				if (owner != null&&Model.getFacade().isANamespace(owner)) {
					ns = owner;
				}
			}
			if (ns == null&&Model.getFacade().isAModelElement(target)) {
				owner = Model.getFacade().getNamespace(target);
				if (owner != null&&Model.getFacade().isANamespace(owner)) {
					ns = owner;
				}
			}
		}
		if (ns == null) {
			ns = p.getRoot();
		}
		return ns;
	}
	public abstract boolean isValidNamespace(Object ns);
	@Deprecated public ArgoDiagram createDiagram(@SuppressWarnings("unused")Object ns) {
		return null;
	}
	public ArgoDiagram createDiagram(Object owner,DiagramSettings settings) {
		ArgoDiagram d = createDiagram(owner);
		d.setDiagramSettings(settings);
		return d;
	}
}



