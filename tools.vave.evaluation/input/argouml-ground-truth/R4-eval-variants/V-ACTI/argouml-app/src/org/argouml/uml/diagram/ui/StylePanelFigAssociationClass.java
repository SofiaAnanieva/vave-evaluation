package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import org.argouml.uml.diagram.static_structure.ui.StylePanelFigClass;
import org.tigris.gef.presentation.Fig;


public class StylePanelFigAssociationClass extends StylePanelFigClass implements ItemListener,FocusListener,KeyListener {
	public StylePanelFigAssociationClass() {
	}
	@Override protected void hasEditableBoundingBox(boolean value) {
		super.hasEditableBoundingBox(true);
	}
	@Override protected void setTargetBBox() {
		Fig target = getPanelTarget();
		if (target == null) {
			return;
		}
		Rectangle bounds = parseBBox();
		if (bounds == null) {
			return;
		}
		Rectangle oldAssociationBounds = target.getBounds();
		if (((FigAssociationClass) target).getAssociationClass() != null) {
			target = ((FigAssociationClass) target).getAssociationClass();
		}
		if (!target.getBounds().equals(bounds)&&!oldAssociationBounds.equals(bounds)) {
			target.setBounds(bounds.x,bounds.y,bounds.width,bounds.height);
			target.endTrans();
		}
	}
	@Override public void refresh() {
		super.refresh();
		Fig target = getPanelTarget();
		if (((FigAssociationClass) target).getAssociationClass() != null) {
			target = ((FigAssociationClass) target).getAssociationClass();
		}
		Rectangle figBounds = target.getBounds();
		Rectangle styleBounds = parseBBox();
		if (!(figBounds.equals(styleBounds))) {
			getBBoxField().setText(figBounds.x + "," + figBounds.y + "," + figBounds.width + "," + figBounds.height);
		}
	}
}



