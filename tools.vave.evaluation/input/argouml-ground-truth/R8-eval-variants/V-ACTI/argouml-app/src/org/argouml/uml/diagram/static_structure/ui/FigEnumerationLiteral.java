package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Rectangle;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.CompartmentFigText;
import org.tigris.gef.presentation.Fig;


public class FigEnumerationLiteral extends CompartmentFigText {
	@SuppressWarnings("deprecation")@Deprecated public FigEnumerationLiteral(int x,int y,int w,int h,Fig aFig,NotationProvider np) {
		super(x,y,w,h,aFig,np);
	}
	@SuppressWarnings("deprecation")@Deprecated public FigEnumerationLiteral(Object owner,Rectangle bounds,DiagramSettings settings,NotationProvider np) {
		super(owner,bounds,settings,np);
	}
	public FigEnumerationLiteral(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
	}
	@Override protected int getNotationProviderType() {
		return NotationProviderFactory2.TYPE_ENUMERATION_LITERAL;
	}
}



