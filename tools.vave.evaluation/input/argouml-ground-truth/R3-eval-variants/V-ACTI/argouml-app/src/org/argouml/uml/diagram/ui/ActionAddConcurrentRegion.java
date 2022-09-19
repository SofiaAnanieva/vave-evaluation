package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.model.StateMachinesFactory;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.LayerDiagram;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.UndoableAction;
import org.argouml.uml.diagram.ui.ArgoFig;
import org.argouml.uml.diagram.ui.FigNodeModelElement;


public class ActionAddConcurrentRegion extends UndoableAction {
	public ActionAddConcurrentRegion() {
		super(Translator.localize("action.add-concurrent-region"),ResourceLoaderWrapper.lookupIcon("action.add-concurrent-region"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.add-concurrent-region"));
	}
	public boolean isEnabled() {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getStateMachinesHelper().isTopState(target)) {
			return false;
		}
		return TargetManager.getInstance().getModelTargets().size() < 2;
	}
	public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		try {
			Fig f = TargetManager.getInstance().getFigTarget();
			if (Model.getFacade().isAConcurrentRegion(f.getOwner())) {
				f = f.getEnclosingFig();
			}
			Editor editor = Globals.curEditor();
			GraphModel gm = editor.getGraphModel();
			LayerDiagram lay = ((LayerDiagram) editor.getLayerManager().getActiveLayer());
			Rectangle rName = ((FigNodeModelElement) f).getNameFig().getBounds();
			Rectangle rFig = f.getBounds();
			if (!(gm instanceof MutableGraphModel)) {
				return;
			}
			editor.getSelectionManager().select(f);
		}catch (Exception ex) {
		}
	}
}



