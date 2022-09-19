package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.undo.UndoableAction;


public class ActionAddExistingEdge extends UndoableAction {
	private static final long serialVersionUID = 736094733140639882l;
	private Object edge = null;
	public ActionAddExistingEdge(String name,Object edgeObject) {
		super(name);
		edge = edgeObject;
	}
	@Override public void actionPerformed(ActionEvent arg0) {
		super.actionPerformed(arg0);
		if (edge == null) {
			return;
		}
		MutableGraphModel gm = (MutableGraphModel) DiagramUtils.getActiveDiagram().getGraphModel();
		if (gm.canAddEdge(edge)) {
			gm.addEdge(edge);
			if (Model.getFacade().isAAssociationClass(edge)) {
				ModeCreateAssociationClass.buildInActiveLayer(Globals.curEditor(),edge);
			}
		}
	}
	@Override public boolean isEnabled() {
		Object target = TargetManager.getInstance().getModelTarget();
		ArgoDiagram dia = DiagramUtils.getActiveDiagram();
		if (dia == null) {
			return false;
		}
		MutableGraphModel gm = (MutableGraphModel) dia.getGraphModel();
		return gm.canAddEdge(target);
	}
}



