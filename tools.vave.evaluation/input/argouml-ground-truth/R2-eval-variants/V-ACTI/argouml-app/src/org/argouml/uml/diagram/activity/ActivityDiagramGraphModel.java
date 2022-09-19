package org.argouml.uml.diagram.activity;

import org.argouml.model.Model;
import org.argouml.uml.diagram.state.StateDiagramGraphModel;


public class ActivityDiagramGraphModel extends StateDiagramGraphModel {
	public boolean canAddNode(Object node) {
		if (containsNode(node)) {
			return false;
		}
		if (Model.getFacade().isAPartition(node)) {
			return true;
		}
		return super.canAddNode(node);
	}
	private static final long serialVersionUID = 5047684232283453072l;
}



