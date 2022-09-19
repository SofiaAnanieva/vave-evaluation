package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.CompartmentFigText;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.Handle;
import org.argouml.model.ModelEventPump;


public abstract class FigFeature extends CompartmentFigText {
	private static final String EVENT_NAME = "ownerScope";
	private static class SelectionFeature extends Selection {
	public SelectionFeature(Fig f) {
		super(f);
	}
	public void dragHandle(int mx,int my,int anX,int anY,Handle h) {
	}
	public void hitHandle(Rectangle r,Handle h) {
	}
	private static final long serialVersionUID = 7437255966804296937l;
}
	@SuppressWarnings("deprecation")@Deprecated public FigFeature(int x,int y,int w,int h,Fig aFig,NotationProvider np) {
		super(x,y,w,h,aFig,np);
	}
	@Deprecated public FigFeature(Object owner,Rectangle bounds,DiagramSettings settings,NotationProvider np) {
		super(owner,bounds,settings,np);
		updateOwnerScope(Model.getFacade().isStatic(owner));
		Model.getPump().addModelEventListener(this,owner,EVENT_NAME);
	}
	public FigFeature(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		updateOwnerScope(Model.getFacade().isStatic(owner));
		Model.getPump().addModelEventListener(this,owner,EVENT_NAME);
	}
	@SuppressWarnings("deprecation")@Deprecated@Override public void setOwner(Object owner) {
		super.setOwner(owner);
		if (owner != null) {
			updateOwnerScope(Model.getFacade().isStatic(owner));
			Model.getPump().addModelEventListener(this,owner,EVENT_NAME);
		}
	}
	@Override public void removeFromDiagram() {
		Model.getPump().removeModelEventListener(this,getOwner(),EVENT_NAME);
		super.removeFromDiagram();
	}
	@Override public void propertyChange(PropertyChangeEvent pce) {
		super.propertyChange(pce);
		if (EVENT_NAME.equals(pce.getPropertyName())) {
			updateOwnerScope(Model.getScopeKind().getClassifier().equals(pce.getNewValue()));
		}
	}
	@Override public Selection makeSelection() {
		return new SelectionFeature(this);
	}
	@Override public void setTextFilled(boolean filled) {
		super.setTextFilled(false);
	}
	@Override public void setFilled(boolean filled) {
		super.setFilled(false);
	}
	protected void updateOwnerScope(boolean isClassifier) {
		setUnderline(isClassifier);
	}
}



