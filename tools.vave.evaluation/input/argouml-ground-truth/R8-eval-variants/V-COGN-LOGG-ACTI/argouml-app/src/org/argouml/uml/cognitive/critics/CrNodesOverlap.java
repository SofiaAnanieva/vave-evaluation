package org.argouml.uml.cognitive.critics;

import java.awt.Rectangle;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.FigNode;


public class CrNodesOverlap extends CrUML {
	public CrNodesOverlap() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.CLASS_SELECTION);
		addSupportedDecision(UMLDecision.EXPECTED_USAGE);
		addSupportedDecision(UMLDecision.STATE_MACHINES);
		setKnowledgeTypes(Critic.KT_PRESENTATION);
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(dm instanceof Diagram)) {
			return NO_PROBLEM;
		}
		Diagram d = (Diagram) dm;
		ListSet offs = computeOffenders(d);
		if (offs == null)return NO_PROBLEM;
		return PROBLEM_FOUND;
	}
	@Override public ToDoItem toDoItem(Object dm,Designer dsgr) {
		Diagram d = (Diagram) dm;
		ListSet offs = computeOffenders(d);
		return new ToDoItem(this,offs,dsgr);
	}
	@Override public boolean stillValid(ToDoItem i,Designer dsgr) {
		if (!isActive()) {
			return false;
		}
		ListSet offs = i.getOffenders();
		Diagram d = (Diagram) offs.get(0);
		ListSet newOffs = computeOffenders(d);
		boolean res = offs.equals(newOffs);
		return res;
	}
	public ListSet computeOffenders(Diagram d) {
		List figs = d.getLayer().getContents();
		int numFigs = figs.size();
		ListSet offs = null;
		for (int i = 0;i < numFigs - 1;i++) {
			Object oi = figs.get(i);
			if (!(oi instanceof FigNode)) {
				continue;
			}
			FigNode fni = (FigNode) oi;
			Rectangle boundsi = fni.getBounds();
			for (int j = i + 1;j < numFigs;j++) {
				Object oj = figs.get(j);
				if (!(oj instanceof FigNode)) {
					continue;
				}
				FigNode fnj = (FigNode) oj;
				if (fnj.intersects(boundsi)) {
					if ((!((fni instanceof FigClass)||(fni instanceof FigInterface)))||(!((fnj instanceof FigClass)||(fnj instanceof FigInterface))))continue;
					if (offs == null) {
						offs = new ListSet();
						offs.add(d);
					}
					offs.add(fni);
					offs.add(fnj);
					break;
				}
			}
		}
		return offs;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		return ret;
	}
}



