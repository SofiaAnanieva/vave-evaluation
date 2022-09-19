package org.argouml.gefext;

import java.awt.event.MouseEvent;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.ModeCreateFigRRect;
import org.tigris.gef.presentation.Fig;


public class ArgoModeCreateFigRRect extends ModeCreateFigRRect {
	@Override public Fig createNewItem(MouseEvent me,int snapX,int snapY) {
		return new ArgoFigRRect(snapX,snapY,0,0);
	}
	@Override public String instructions() {
		return Translator.localize("statusmsg.help.create.rrect");
	}
}



