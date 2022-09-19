package org.argouml.gefext;

import java.awt.event.MouseEvent;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.ModeCreateFigRect;
import org.tigris.gef.presentation.Fig;


public class ArgoModeCreateFigRect extends ModeCreateFigRect {
	@Override public Fig createNewItem(MouseEvent me,int snapX,int snapY) {
		return new ArgoFigRect(snapX,snapY,0,0);
	}
	@Override public String instructions() {
		return Translator.localize("statusmsg.help.create.rect");
	}
}



