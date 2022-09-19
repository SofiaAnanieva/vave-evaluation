package org.argouml.uml.cognitive.critics;

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.uml.cognitive.UMLDecision;
import org.tigris.gef.presentation.FigEdge;


public class CrZeroLengthEdge extends CrUML {
	private static final int THRESHOLD = 20;
	public CrZeroLengthEdge() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.RELATIONSHIPS);
		addSupportedDecision(UMLDecision.INHERITANCE);
		addSupportedDecision(UMLDecision.STATE_MACHINES);
		setKnowledgeTypes(Critic.KT_PRESENTATION);
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(dm instanceof FigEdge))return NO_PROBLEM;
		FigEdge fe = (FigEdge) dm;
		int length = fe.getPerimeterLength();
		if (length > THRESHOLD)return NO_PROBLEM;
		return PROBLEM_FOUND;
	}
}



