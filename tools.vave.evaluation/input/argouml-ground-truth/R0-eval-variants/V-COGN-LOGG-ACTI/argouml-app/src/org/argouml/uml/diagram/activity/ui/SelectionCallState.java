package org.argouml.uml.diagram.activity.ui;

import org.argouml.model.Model;
import org.tigris.gef.presentation.Fig;


public class SelectionCallState extends SelectionActionState {
	public SelectionCallState(Fig f) {
		super(f);
	}
	protected Object getNewNodeType(int buttonCode) {
		return Model.getMetaTypes().getCallState();
	}
	protected Object getNewNode(int buttonCode) {
		return Model.getActivityGraphsFactory().createCallState();
	}
}



