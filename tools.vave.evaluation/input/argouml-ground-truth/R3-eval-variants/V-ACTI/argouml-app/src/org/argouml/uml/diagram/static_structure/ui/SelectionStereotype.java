package org.argouml.uml.diagram.static_structure.ui;

import java.awt.event.MouseEvent;
import javax.swing.Icon;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.StereotypeUtility;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;


public class SelectionStereotype extends SelectionNodeClarifiers2 {
	private static Icon inheritIcon = ResourceLoaderWrapper.lookupIconResource("Generalization");
	private static Icon dependIcon = ResourceLoaderWrapper.lookupIconResource("Dependency");
	private boolean useComposite;
	private static Icon icons[] =  {dependIcon,inheritIcon,null,null,null};
	private static String instructions[] =  {"Add a baseClass","Add a subStereotype",null,null,null,"Move object(s)"};
	public SelectionStereotype(Fig f) {
		super(f);
	}
	@Override public void mouseEntered(MouseEvent me) {
		super.mouseEntered(me);
		useComposite = me.isShiftDown();
	}
	@Override protected Object getNewNode(int index) {
		if (index == 0) {
			index = getButton();
		}
		Object ns = Model.getFacade().getNamespace(getContent().getOwner());
		switch (index) {case 10:
			Object clazz = Model.getCoreFactory().buildClass(ns);
			StereotypeUtility.dealWithStereotypes(clazz,"metaclass",false);
			return clazz;
		case 11:
			Object st = Model.getExtensionMechanismsFactory().createStereotype();
			Model.getCoreHelper().setNamespace(st,ns);
			return st;
		}
		return null;
	}
	@Override protected Object getNewNodeType(int index) {
		switch (index) {case 10:
			return Model.getMetaTypes().getClass();
		case 11:
			return Model.getMetaTypes().getStereotype();
		}
		return null;
	}
	@Override protected Object createEdgeAbove(MutableGraphModel mgm,Object newNode) {
		Object dep = super.createEdgeAbove(mgm,newNode);
		StereotypeUtility.dealWithStereotypes(dep,"stereotype",false);
		return dep;
	}
	@Override protected Icon[]getIcons() {
		if (Model.getModelManagementHelper().isReadOnly(getContent().getOwner())) {
			return new Icon[] {null,inheritIcon,null,null,null};
		}
		return icons;
	}
	@Override protected String getInstructions(int index) {
		return instructions[index - BASE];
	}
	@Override protected Object getNewEdgeType(int index) {
		if (index == TOP) {
			return Model.getMetaTypes().getDependency();
		}else if (index == BOTTOM) {
			return Model.getMetaTypes().getGeneralization();
		}
		return null;
	}
	@Override protected boolean isReverseEdge(int index) {
		if (index == BOTTOM) {
			return true;
		}
		return false;
	}
	@Override protected boolean isEdgePostProcessRequested() {
		return useComposite;
	}
}



