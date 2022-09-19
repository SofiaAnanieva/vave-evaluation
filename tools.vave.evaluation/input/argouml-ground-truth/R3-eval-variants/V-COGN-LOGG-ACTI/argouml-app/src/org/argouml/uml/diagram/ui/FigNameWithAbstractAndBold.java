package org.argouml.uml.diagram.ui;

import java.awt.Font;
import java.awt.Rectangle;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigNameWithAbstract;


class FigNameWithAbstractAndBold extends FigNameWithAbstract {
	@SuppressWarnings("deprecation")@Deprecated public FigNameWithAbstractAndBold(int x,int y,int w,int h,boolean expandOnly) {
		super(x,y,w,h,expandOnly);
	}
	public FigNameWithAbstractAndBold(Object owner,Rectangle bounds,DiagramSettings settings,boolean expandOnly) {
		super(owner,bounds,settings,expandOnly);
	}
	@Override protected int getFigFontStyle() {
		boolean showBoldName = getSettings().isShowBoldNames();
		int boldStyle = showBoldName?Font.BOLD:Font.PLAIN;
		return super.getFigFontStyle()|boldStyle;
	}
}



