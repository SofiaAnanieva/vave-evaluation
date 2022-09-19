package org.argouml.uml.diagram.activity.ui;

import javax.swing.Icon;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.presentation.Fig;


public class SelectionActionState extends SelectionNodeClarifiers2 {
	private static Icon trans = ResourceLoaderWrapper.lookupIconResource("Transition");
	private static Icon transDown = ResourceLoaderWrapper.lookupIconResource("TransitionDown");
	private static Icon icons[] =  {transDown,transDown,trans,trans,null};
	private static String instructions[] =  {"Add an incoming transition","Add an outgoing transition","Add an incoming transition","Add an outgoing transition",null,"Move object(s)"};
	private boolean showIncomingLeft = true;
	private boolean showIncomingAbove = true;
	private boolean showOutgoingRight = true;
	private boolean showOutgoingBelow = true;
	public SelectionActionState(Fig f) {
		super(f);
	}
	public void setOutgoingButtonEnabled(boolean b) {
		setOutgoingRightButtonEnabled(b);
		setOutgoingBelowButtonEnabled(b);
	}
	public void setIncomingButtonEnabled(boolean b) {
		setIncomingAboveButtonEnabled(b);
		setIncomingLeftButtonEnabled(b);
	}
	public void setIncomingLeftButtonEnabled(boolean b) {
		showIncomingLeft = b;
	}
	public void setOutgoingRightButtonEnabled(boolean b) {
		showOutgoingRight = b;
	}
	public void setIncomingAboveButtonEnabled(boolean b) {
		showIncomingAbove = b;
	}
	public void setOutgoingBelowButtonEnabled(boolean b) {
		showOutgoingBelow = b;
	}
	@Override protected Object getNewNodeType(int index) {
		return Model.getMetaTypes().getActionState();
	}
	@Override protected Object getNewNode(int arg0) {
		return Model.getActivityGraphsFactory().createActionState();
	}
	@Override protected Icon[]getIcons() {
		Icon[]workingIcons = new Icon[icons.];
		System.arraycopy(icons,0,workingIcons,0,icons.);
		if (!showOutgoingBelow) {
			workingIcons[BOTTOM - BASE] = null;
		}
		if (!showIncomingAbove) {
			workingIcons[TOP - BASE] = null;
		}
		if (!showIncomingLeft) {
			workingIcons[LEFT - BASE] = null;
		}
		if (!showOutgoingRight) {
			workingIcons[RIGHT - BASE] = null;
		}
		return workingIcons;
	}
	@Override protected String getInstructions(int index) {
		return instructions[index - BASE];
	}
	@Override protected Object getNewEdgeType(int index) {
		return Model.getMetaTypes().getTransition();
	}
	@Override protected boolean isReverseEdge(int index) {
		if (index == TOP||index == LEFT) {
			return true;
		}
		return false;
	}
}



