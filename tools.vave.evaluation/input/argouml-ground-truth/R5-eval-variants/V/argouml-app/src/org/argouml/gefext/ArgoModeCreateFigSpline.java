package org.argouml.gefext;

import java.awt.event.MouseEvent;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.ModeCreateFigSpline;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigSpline;


public class ArgoModeCreateFigSpline extends ModeCreateFigSpline {
	public String instructions() {
		return Translator.localize("statusmsg.help.create.spline");
	}
	public Fig createNewItem(MouseEvent me,int snapX,int snapY) {
		FigSpline p = new ArgoFigSpline(snapX,snapY);
		p.addPoint(snapX,snapY);
		_startX = snapX;
		_startY = snapY;
		_lastX = snapX;
		_lastY = snapY;
		_npoints = 2;
		return p;
	}
}



