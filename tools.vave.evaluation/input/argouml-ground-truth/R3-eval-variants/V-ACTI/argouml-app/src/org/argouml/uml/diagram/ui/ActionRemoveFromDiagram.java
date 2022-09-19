package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.uml.CommentEdge;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.di.GraphElement;
import org.tigris.gef.graph.MutableGraphSupport;
import org.tigris.gef.presentation.Fig;


public class ActionRemoveFromDiagram extends AbstractAction {
	public ActionRemoveFromDiagram(String name) {
		super(name,ResourceLoaderWrapper.lookupIcon("RemoveFromDiagram"));
		String localMnemonic = Translator.localize("action.remove-from-diagram.mnemonic");
		if (localMnemonic != null&&localMnemonic.length() == 1) {
			putValue(Action.MNEMONIC_KEY,Integer.valueOf(localMnemonic.charAt(0)));
		}
		putValue(Action.SHORT_DESCRIPTION,name);
	}
	public void actionPerformed(ActionEvent ae) {
		Editor ce = Globals.curEditor();
		MutableGraphSupport graph = (MutableGraphSupport) ce.getGraphModel();
		List<Fig>figs = ce.getSelectionManager().getFigs();
		for (Fig f:figs) {
			if (!(f.getOwner()instanceof CommentEdge)) {
				if (f instanceof GraphElement) {
					f.removeFromDiagram();
				}else {
					graph.removeFig(f);
				}
			}
		}
	}
}



