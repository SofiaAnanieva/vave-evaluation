package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import org.argouml.model.Model;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.reveng.DiagramInterface;
import org.tigris.gef.base.Globals;
import org.tigris.gef.undo.UndoableAction;


public class ActionAddAllClassesFromModel extends UndoableAction {
	private Object object;
	public ActionAddAllClassesFromModel(String name,Object o) {
		super(name);
		object = o;
	}
	public boolean isEnabled() {
		return object instanceof UMLClassDiagram;
	}
	public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		if (object instanceof UMLClassDiagram) {
			DiagramInterface diagram = new DiagramInterface(Globals.curEditor());
			diagram.setCurrentDiagram((UMLClassDiagram) object);
			Object namespace = ((UMLClassDiagram) object).getNamespace();
			Iterator elements = Model.getFacade().getOwnedElements(namespace).iterator();
			while (elements.hasNext()) {
				Object element = elements.next();
				if (Model.getFacade().isAClass(element)&&!Model.getFacade().isAAssociationClass(element)) {
					diagram.addClass(element,false);
				}
			}
		}
	}
}



