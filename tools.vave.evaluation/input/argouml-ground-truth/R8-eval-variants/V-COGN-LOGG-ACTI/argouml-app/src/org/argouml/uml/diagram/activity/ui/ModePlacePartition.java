package org.argouml.uml.diagram.activity.ui;

import java.awt.event.MouseEvent;
import org.tigris.gef.base.ModePlace;
import org.tigris.gef.graph.GraphFactory;


public class ModePlacePartition extends ModePlace {
	private Object machine;
	public ModePlacePartition(GraphFactory gf,String instructions,Object activityGraph) {
		super(gf,instructions);
		machine = activityGraph;
	}
	@Override public void mouseReleased(MouseEvent me) {
		if (me.isConsumed()) {
			return;
		}
		FigPartition fig = (FigPartition) _pers;
		super.mouseReleased(me);
		fig.appendToPool(machine);
	}
}



