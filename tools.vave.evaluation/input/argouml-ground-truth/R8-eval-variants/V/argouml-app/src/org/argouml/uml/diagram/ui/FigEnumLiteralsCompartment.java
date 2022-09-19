package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;
import java.util.Collection;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.static_structure.ui.FigEnumerationLiteral;
import org.argouml.uml.diagram.ui.FigEditableCompartment;
import org.argouml.uml.diagram.ui.FigSingleLineTextWithNotation;


public class FigEnumLiteralsCompartment extends FigEditableCompartment {
	private static final long serialVersionUID = 829674049363538379l;
	@SuppressWarnings("deprecation")@Deprecated public FigEnumLiteralsCompartment(int x,int y,int w,int h) {
		super(x,y,w,h);
	}
	public FigEnumLiteralsCompartment(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		super.populate();
	}
	protected Collection getUmlCollection() {
		return Model.getFacade().getEnumerationLiterals(getOwner());
	}
	protected int getNotationType() {
		return NotationProviderFactory2.TYPE_ENUMERATION_LITERAL;
	}
	protected void createModelElement() {
		Object enumeration = getGroup().getOwner();
		Object literal = Model.getCoreFactory().buildEnumerationLiteral("literal",enumeration);
		TargetManager.getInstance().setTarget(literal);
	}
	@SuppressWarnings("deprecation")@Deprecated@Override protected FigSingleLineTextWithNotation createFigText(Object owner,Rectangle bounds,DiagramSettings settings,NotationProvider np) {
		return new FigEnumerationLiteral(owner,bounds,settings,np);
	}
	@Override protected FigSingleLineTextWithNotation createFigText(Object owner,Rectangle bounds,DiagramSettings settings) {
		return new FigEnumerationLiteral(owner,bounds,settings);
	}
}



