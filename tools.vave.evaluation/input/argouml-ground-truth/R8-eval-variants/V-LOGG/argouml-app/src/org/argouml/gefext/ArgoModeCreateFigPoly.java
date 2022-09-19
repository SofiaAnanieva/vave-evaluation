package org.argouml.gefext;

import java.awt.event.MouseEvent;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.ModeCreateFigPoly;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigPoly;


public class ArgoModeCreateFigPoly extends ModeCreateFigPoly {
	public String instructions() {
		return Translator.localize("statusmsg.help.create.poly");
	}
	@Override public Fig createNewItem(MouseEvent me,int snapX,int snapY) {
		FigPoly p = new ArgoFigPoly(snapX,snapY);
		p.addPoint(snapX,snapY);
		_lastX = snapX;
		_lastY = snapY;
		_startX = snapX;
		_startY = snapY;
		_npoints = 2;
		return p;
	}
}



