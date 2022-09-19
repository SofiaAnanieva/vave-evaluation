package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;
import java.util.Collection;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.static_structure.ui.FigOperation;
import org.argouml.uml.diagram.ui.FigSingleLineTextWithNotation;


public class FigOperationsCompartment extends FigEditableCompartment {
	private static final long serialVersionUID = -2605582251722944961l;
	@SuppressWarnings("deprecation")@Deprecated public FigOperationsCompartment(int x,int y,int w,int h) {
		super(x,y,w,h);
	}
	public FigOperationsCompartment(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		super.populate();
	}
	protected Collection getUmlCollection() {
		Object classifier = getOwner();
		return Model.getFacade().getOperationsAndReceptions(classifier);
	}
	protected int getNotationType() {
		return NotationProviderFactory2.TYPE_OPERATION;
	}
	@SuppressWarnings("deprecation")@Deprecated@Override protected FigSingleLineTextWithNotation createFigText(Object owner,Rectangle bounds,DiagramSettings settings,NotationProvider np) {
		return new FigOperation(owner,bounds,settings,np);
	}
	@Override protected FigSingleLineTextWithNotation createFigText(Object owner,Rectangle bounds,DiagramSettings settings) {
		return new FigOperation(owner,bounds,settings);
	}
	protected void createModelElement() {
		Object classifier = getGroup().getOwner();
		Project project = getProject();
		Object returnType = project.getDefaultReturnType();
		Object oper = Model.getCoreFactory().buildOperation(classifier,returnType);
		TargetManager.getInstance().setTarget(oper);
	}
}



