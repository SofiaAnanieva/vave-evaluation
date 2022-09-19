package org.argouml.ui.cmd;

import java.util.ArrayList;
import java.util.List;
import org.tigris.gef.base.Cmd;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.SelectionManager;
import org.tigris.gef.presentation.Fig;
import org.argouml.i18n.Translator;


public class CmdSetPreferredSize extends Cmd {
	public CmdSetPreferredSize() {
		super(Translator.localize("action.set-minimum-size"));
	}
	public void setFigToResize(Fig f) {
		List<Fig>figs = new ArrayList<Fig>(1);
		figs.add(f);
		setArg("figs",figs);
	}
	public void setFigToResize(List figs) {
		setArg("figs",figs);
	}
	public void doIt() {
		Editor ce = Globals.curEditor();
		List<Fig>figs = (List<Fig>) getArg("figs");
		if (figs == null) {
			SelectionManager sm = ce.getSelectionManager();
			if (sm.getLocked()) {
				Globals.showStatus(Translator.localize("action.locked-objects-not-modify"));
				return;
			}
			figs = sm.getFigs();
		}
		if (figs == null) {
			return;
		}
		int size = figs.size();
		if (size == 0) {
			return;
		}
		for (int i = 0;i < size;i++) {
			Fig fi = figs.get(i);
			if (fi.isResizable()&&(fi.getEnclosedFigs() == null||fi.getEnclosedFigs().size() == 0)) {
				fi.setSize(fi.getMinimumSize());
				Globals.showStatus(Translator.localize("action.setting-size",new Object[] {fi}));
			}
			fi.endTrans();
		}
	}
	public void undoIt() {
	}
}



