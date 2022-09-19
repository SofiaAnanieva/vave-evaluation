package org.argouml.uml.cognitive;

import java.util.Iterator;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Highlightable;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ProjectActions;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;


public class UMLToDoItem extends ToDoItem {
	public UMLToDoItem(Poster poster,String h,int p,String d,String m,ListSet offs) {
		super(poster,h,p,d,m,offs);
	}
	public UMLToDoItem(Poster poster,String h,int p,String d,String m) {
		super(poster,h,p,d,m);
	}
	public UMLToDoItem(Critic c,Object dm,Designer dsgr) {
		super(c,dm,dsgr);
	}
	public UMLToDoItem(Critic c,ListSet offs,Designer dsgr) {
		super(c,offs,dsgr);
	}
	public UMLToDoItem(Critic c) {
		super(c);
	}
	@Override public void action() {
		deselect();
		ProjectActions.jumpToDiagramShowing(getOffenders());
		select();
	}
	@Override public void deselect() {
		Project p = ProjectManager.getManager().getCurrentProject();
		for (Object dm:getOffenders()) {
			if (dm instanceof Highlightable) {
				((Highlightable) dm).setHighlight(false);
			}else if (p != null) {
				Iterator iterFigs = p.findFigsForMember(dm).iterator();
				while (iterFigs.hasNext()) {
					Object f = iterFigs.next();
					if (f instanceof Highlightable) {
						((Highlightable) f).setHighlight(false);
					}
				}
			}
		}
	}
	@Override public void select() {
		Project p = ProjectManager.getManager().getCurrentProject();
		for (Object dm:getOffenders()) {
			if (dm instanceof Highlightable) {
				((Highlightable) dm).setHighlight(true);
			}else if (p != null) {
				Iterator iterFigs = p.findFigsForMember(dm).iterator();
				while (iterFigs.hasNext()) {
					Object f = iterFigs.next();
					if (f instanceof Highlightable) {
						((Highlightable) f).setHighlight(true);
					}
				}
			}
		}
	}
	@Override public ListSet getOffenders() {
		final ListSet offenders = super.getOffenders();
		assert offenders.size() <= 0||Model.getFacade().isAUMLElement(offenders.get(0))||offenders.get(0)instanceof Fig||offenders.get(0)instanceof Diagram;
		return offenders;
	}
	@Override protected void checkArgument(Object dm) {
		if (!Model.getFacade().isAUMLElement(dm)&&!(dm instanceof Fig)&&!(dm instanceof Diagram)) {
			throw new IllegalArgumentException("The offender must be a model element, " + "a Fig or a Diagram");
		}
	}
}



