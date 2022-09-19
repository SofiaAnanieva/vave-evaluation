package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import org.argouml.i18n.Translator;
import org.argouml.uml.diagram.DiagramUtils;
import org.tigris.gef.base.ModeCreateFigText;
import org.tigris.gef.presentation.Fig;
import org.argouml.uml.diagram.ui.ArgoFigText;


public class ArgoModeCreateFigText extends ModeCreateFigText {
	@Override public String instructions() {
		return Translator.localize("statusmsg.help.create.text");
	}
	@Override public Fig createNewItem(MouseEvent e,int snapX,int snapY) {
		return new ArgoFigText(null,new Rectangle(snapX,snapY,0,0),DiagramUtils.getActiveDiagram().getDiagramSettings(),true);
	}
}



