package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.UndoableAction;


public class ActionEdgesDisplay extends UndoableAction {
	private static UndoableAction showEdges = new ActionEdgesDisplay(true,Translator.localize("menu.popup.add.all-relations"));
	private static UndoableAction hideEdges = new ActionEdgesDisplay(false,Translator.localize("menu.popup.remove.all-relations"));
	private boolean show;
	protected ActionEdgesDisplay(boolean showEdge,String desc) {
		super(desc,null);
		putValue(Action.SHORT_DESCRIPTION,desc);
		show = showEdge;
	}
	@Override public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		ArgoDiagram d = DiagramUtils.getActiveDiagram();
		Editor ce = Globals.curEditor();
		MutableGraphModel mgm = (MutableGraphModel) ce.getGraphModel();
		Enumeration e = ce.getSelectionManager().selections().elements();
		while (e.hasMoreElements()) {
			Selection sel = (Selection) e.nextElement();
			Object owner = sel.getContent().getOwner();
			if (show) {
				mgm.addNodeRelatedEdges(owner);
			}else {
				List edges = mgm.getInEdges(owner);
				edges.addAll(mgm.getOutEdges(owner));
				Iterator e2 = edges.iterator();
				while (e2.hasNext()) {
					Object edge = e2.next();
					if (Model.getFacade().isAAssociationEnd(edge)) {
						edge = Model.getFacade().getAssociation(edge);
					}
					Fig fig = d.presentationFor(edge);
					if (fig != null) {
						fig.removeFromDiagram();
					}
				}
			}
		}
	}
	@Override public boolean isEnabled() {
		return true;
	}
	public static UndoableAction getShowEdges() {
		return showEdges;
	}
	public static UndoableAction getHideEdges() {
		return hideEdges;
	}
}



