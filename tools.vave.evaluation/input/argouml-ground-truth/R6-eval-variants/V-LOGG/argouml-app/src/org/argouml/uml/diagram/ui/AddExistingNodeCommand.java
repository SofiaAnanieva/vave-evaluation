package org.argouml.uml.diagram.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.tigris.gef.base.Command;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ModePlace;
import org.tigris.gef.graph.GraphFactory;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.argouml.uml.diagram.ui.UMLDiagram;


public class AddExistingNodeCommand implements Command,GraphFactory {
	private Object object;
	private Point location;
	private int count;
	public AddExistingNodeCommand(Object o) {
		object = o;
	}
	public AddExistingNodeCommand(Object o,DropTargetDropEvent event,int cnt) {
		object = o;
		location = event.getLocation();
		count = cnt;
	}
	public AddExistingNodeCommand(Object o,Point dropLocation,int cnt) {
		object = o;
		location = dropLocation;
		count = cnt;
	}
	public void execute() {
		Editor ce = Globals.curEditor();
		GraphModel gm = ce.getGraphModel();
		if (!(gm instanceof MutableGraphModel)) {
			return;
		}
		String instructions = null;
		ModePlace placeMode = null;
		if (object != null) {
			ArgoDiagram activeDiagram = DiagramUtils.getActiveDiagram();
			if (activeDiagram instanceof UMLDiagram&&((UMLDiagram) activeDiagram).doesAccept(object)) {
				instructions = ((UMLDiagram) activeDiagram).getInstructions(object);
				placeMode = ((UMLDiagram) activeDiagram).getModePlace(this,instructions);
				placeMode.setAddRelatedEdges(true);
			}else {
				instructions = Translator.localize("misc.message.click-on-diagram-to-add",new Object[] {Model.getFacade().toString(object)});
				placeMode = new ModePlace(this,instructions);
				placeMode.setAddRelatedEdges(true);
			}
			Globals.showStatus(instructions);
		}
		if (location == null) {
			Globals.mode(placeMode,false);
		}else {
			Point p = new Point(location.x + (count * 100),location.y);
			Rectangle r = ce.getJComponent().getVisibleRect();
			p.translate(r.x,r.y);
			MouseEvent me = new MouseEvent(ce.getJComponent(),0,0,0,p.x,p.y,0,false);
			placeMode.mousePressed(me);
			me = new MouseEvent(ce.getJComponent(),0,0,0,p.x,p.y,0,false);
			placeMode.mouseReleased(me);
			ArgoDiagram diagram = DiagramUtils.getActiveDiagram();
			Fig aFig = diagram.presentationFor(object);
			aFig.setSize(aFig.getPreferredSize());
		}
	}
	public GraphModel makeGraphModel() {
		return null;
	}
	public Object makeEdge() {
		return null;
	}
	public Object makeNode() {
		return object;
	}
}



