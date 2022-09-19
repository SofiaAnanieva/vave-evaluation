package org.argouml.gefext;

import java.awt.event.MouseEvent;
import org.argouml.i18n.Translator;
import org.argouml.uml.diagram.ui.ArgoFig;
import org.tigris.gef.base.ModeCreateFigLine;
import org.tigris.gef.presentation.Fig;


public class ArgoModeCreateFigLine extends ModeCreateFigLine {
	@Override public Fig createNewItem(MouseEvent me,int snapX,int snapY) {
		Fig line = new ArgoFigLine(snapX,snapY,snapX,snapY);
		return line;
	}
	@Override public String instructions() {
		return Translator.localize("statusmsg.help.create.line");
	}
}



