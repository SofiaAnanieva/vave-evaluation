package org.argouml.uml.diagram.activity.ui;

import org.argouml.model.Model;
import org.argouml.ui.CmdCreateNode;
import org.tigris.gef.base.Mode;


public class ActionCreatePartition extends CmdCreateNode {
	private Object machine;
	public ActionCreatePartition(Object activityGraph) {
		super(Model.getMetaTypes().getPartition(),"button.new-partition");
		machine = activityGraph;
	}
	@Override protected Mode createMode(String instructions) {
		return new ModePlacePartition(this,instructions,machine);
	}
}



