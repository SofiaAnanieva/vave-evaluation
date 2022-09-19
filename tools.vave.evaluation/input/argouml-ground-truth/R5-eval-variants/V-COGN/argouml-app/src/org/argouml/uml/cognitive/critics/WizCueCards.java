package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.argouml.cognitive.ui.WizStepCue;


public class WizCueCards extends UMLWizard {
	private List cues = new ArrayList();
	public WizCueCards() {
	}
	@Override public int getNumSteps() {
		return cues.size();
	}
	public void addCue(String s) {
		cues.add(s);
	}
	public JPanel makePanel(int newStep) {
		if (newStep <= getNumSteps()) {
			String c = (String) cues.get(newStep - 1);
			return new WizStepCue(this,c);
		}
		return null;
	}
	public void doAction(int oldStep) {
	}
	@Override public boolean canFinish() {
		return getStep() == getNumSteps();
	}
}



