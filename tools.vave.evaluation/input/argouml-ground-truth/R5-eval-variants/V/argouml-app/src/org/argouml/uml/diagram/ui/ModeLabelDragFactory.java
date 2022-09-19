package org.argouml.uml.diagram.ui;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.FigModifyingMode;
import org.tigris.gef.base.ModeFactory;


public class ModeLabelDragFactory implements ModeFactory {
	public FigModifyingMode createMode() {
		return new ModeLabelDrag();
	}
	public FigModifyingMode createMode(Editor editor) {
		return new ModeLabelDrag(editor);
	}
}



