package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;
import org.argouml.ui.ZoomSliderButton;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ZoomAction;


public class ZoomActionProxy extends ZoomAction {
	private double zoomFactor;
	public ZoomActionProxy(double zF) {
		super(zF);
		zoomFactor = zF;
	}
	@Override public void actionPerformed(ActionEvent arg0) {
		Editor ed = Globals.curEditor();
		if (ed == null) {
			return;
		}
		if ((zoomFactor == 0)||((ed.getScale() * zoomFactor > ZoomSliderButton.MINIMUM_ZOOM / 100.0)&&ed.getScale() * zoomFactor < ZoomSliderButton.MAXIMUM_ZOOM / 100.0)) {
			super.actionPerformed(arg0);
		}
	}
}



