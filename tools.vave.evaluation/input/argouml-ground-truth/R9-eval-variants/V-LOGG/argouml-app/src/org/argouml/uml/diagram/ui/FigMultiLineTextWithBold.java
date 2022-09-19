package org.argouml.uml.diagram.ui;

import java.awt.Font;
import java.awt.Rectangle;
import org.argouml.uml.diagram.DiagramSettings;


public class FigMultiLineTextWithBold extends FigMultiLineText {
	@SuppressWarnings("deprecation")@Deprecated public FigMultiLineTextWithBold(int x,int y,int w,int h,boolean expandOnly) {
		super(x,y,w,h,expandOnly);
	}
	public FigMultiLineTextWithBold(Object owner,Rectangle bounds,DiagramSettings settings,boolean expandOnly) {
		super(owner,bounds,settings,expandOnly);
	}
	@Override protected int getFigFontStyle() {
		boolean showBoldName = getSettings().isShowBoldNames();
		int boldStyle = showBoldName?Font.BOLD:Font.PLAIN;
		return super.getFigFontStyle()|boldStyle;
	}
}



