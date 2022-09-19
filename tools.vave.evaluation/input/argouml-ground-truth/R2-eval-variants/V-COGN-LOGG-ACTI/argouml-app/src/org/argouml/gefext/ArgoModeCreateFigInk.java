package org.argouml.gefext;

import java.awt.event.MouseEvent;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.ModeCreateFigInk;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigInk;


public class ArgoModeCreateFigInk extends ModeCreateFigInk {
	public String instructions() {
		return Translator.localize("statusmsg.help.create.ink");
	}
	public Fig createNewItem(MouseEvent me,int snapX,int snapY) {
		FigInk p = new ArgoFigInk(snapX,snapY);
		_lastX = snapX;
		_lastY = snapY;
		return p;
	}
}



