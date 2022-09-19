package org.argouml.uml.diagram.ui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Collection;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.undo.UndoableAction;
import org.argouml.uml.diagram.ui.UMLDiagram;


public class ActionAddExistingNodes extends UndoableAction {
	private Collection objects;
	public ActionAddExistingNodes(String name,Collection coll) {
		super(name);
		objects = coll;
	}
	@Override public boolean isEnabled() {
		ArgoDiagram dia = DiagramUtils.getActiveDiagram();
		if (dia == null) {
			return false;
		}
		MutableGraphModel gm = (MutableGraphModel) dia.getGraphModel();
		for (Object o:objects) {
			if (gm.canAddNode(o)) {
				return true;
			}
		}
		return false;
	}
	@Override public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		Editor ce = Globals.curEditor();
		GraphModel gm = ce.getGraphModel();
		if (!(gm instanceof MutableGraphModel)) {
			return;
		}
		String instructions = Translator.localize("misc.message.click-on-diagram-to-add");
		Globals.showStatus(instructions);
		final ModeAddToDiagram placeMode = new ModeAddToDiagram(objects,instructions);
		Globals.mode(placeMode,false);
	}
	public static void addNodes(Collection modelElements,Point location,ArgoDiagram diagram) {
		MutableGraphModel gm = (MutableGraphModel) diagram.getGraphModel();
		Collection oldTargets = TargetManager.getInstance().getTargets();
		int count = 0;
		for (Object me:modelElements) {
			if (diagram instanceof UMLDiagram&&((UMLDiagram) diagram).doesAccept(me)) {
				((UMLDiagram) diagram).drop(me,location);
			}else if (Model.getFacade().isANaryAssociation(me)) {
				AddExistingNodeCommand cmd = new AddExistingNodeCommand(me,location,count++);
				cmd.execute();
			}else if (Model.getFacade().isAUMLElement(me)) {
				if (gm.canAddEdge(me)) {
					gm.addEdge(me);
					if (Model.getFacade().isAAssociationClass(me)) {
						ModeCreateAssociationClass.buildInActiveLayer(Globals.curEditor(),me);
					}
				}else if (gm.canAddNode(me)) {
					AddExistingNodeCommand cmd = new AddExistingNodeCommand(me,location,count++);
					cmd.execute();
				}
			}
		}
		TargetManager.getInstance().setTargets(oldTargets);
	}
}



