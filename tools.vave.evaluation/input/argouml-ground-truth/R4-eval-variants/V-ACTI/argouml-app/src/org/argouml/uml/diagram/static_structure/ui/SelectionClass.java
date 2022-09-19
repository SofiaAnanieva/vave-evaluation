package org.argouml.uml.diagram.static_structure.ui;

import java.awt.event.MouseEvent;
import javax.swing.Icon;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.Fig;


public class SelectionClass extends SelectionNodeClarifiers2 {
	private static Icon inherit = ResourceLoaderWrapper.lookupIconResource("Generalization");
	private static Icon assoc = ResourceLoaderWrapper.lookupIconResource("Association");
	private static Icon compos = ResourceLoaderWrapper.lookupIconResource("CompositeAggregation");
	private static Icon selfassoc = ResourceLoaderWrapper.lookupIconResource("SelfAssociation");
	private boolean useComposite;
	private static Icon icons[] =  {inherit,inherit,assoc,assoc,selfassoc};
	private static String instructions[] =  {"Add a superclass","Add a subclass","Add an associated class","Add an associated class","Add a self association","Move object(s)"};
	private static Object edgeType[] =  {Model.getMetaTypes().getGeneralization(),Model.getMetaTypes().getGeneralization(),Model.getMetaTypes().getAssociation(),Model.getMetaTypes().getAssociation(),Model.getMetaTypes().getAssociation()};
	public SelectionClass(Fig f) {
		super(f);
	}
	@Override protected Icon[]getIcons() {
		Icon workingIcons[] = new Icon[icons.];
		System.arraycopy(icons,0,workingIcons,0,icons.);
		if (useComposite) {
			workingIcons[LEFT - BASE] = compos;
			workingIcons[RIGHT - BASE] = compos;
		}
		if (Model.getModelManagementHelper().isReadOnly(getContent().getOwner())) {
			return new Icon[] {null,inherit,null,null,null};
		}
		return workingIcons;
	}
	@Override protected String getInstructions(int index) {
		return instructions[index - BASE];
	}
	@Override protected Object getNewNodeType(int i) {
		return Model.getMetaTypes().getUMLClass();
	}
	@Override protected Object getNewEdgeType(int i) {
		if (i == 0) {
			i = getButton();
		}
		return edgeType[i - 10];
	}
	@Override protected boolean isReverseEdge(int i) {
		if (i == BOTTOM||i == LEFT) {
			return true;
		}
		return false;
	}
	@Override protected boolean isDraggableHandle(int index) {
		if (index == LOWER_LEFT) {
			return false;
		}
		return true;
	}
	@Override protected boolean isEdgePostProcessRequested() {
		return useComposite;
	}
	@Override public void mouseEntered(MouseEvent me) {
		super.mouseEntered(me);
		useComposite = me.isShiftDown();
	}
	@Override protected Object getNewNode(int index) {
		return Model.getCoreFactory().buildClass();
	}
}



