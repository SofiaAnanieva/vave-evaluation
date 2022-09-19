package org.argouml.uml.cognitive.critics;

import javax.swing.JPanel;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ui.WizStepTextField;
import org.argouml.i18n.Translator;


public class WizTooMany extends UMLWizard {
	private String instructions = Translator.localize("critics.WizTooMany-ins");
	private WizStepTextField step1;
	public WizTooMany() {
		super();
	}
	public int getNumSteps() {
		return 1;
	}
	public JPanel makePanel(int newStep) {
		switch (newStep) {case 1:
			if (step1 == null) {
				ToDoItem item = (ToDoItem) getToDoItem();
				AbstractCrTooMany critic = (AbstractCrTooMany) item.getPoster();
				step1 = new WizStepTextField(this,instructions,"Threshold",Integer.toString(critic.getThreshold()));
			}
			return step1;
		}
		return null;
	}
	public boolean canFinish() {
		if (!super.canFinish())return false;
		if (getStep() == 0)return true;
		if (getStep() == 1&&step1 != null) {
			try {
				Integer.parseInt(step1.getText());
				return true;
			}catch (NumberFormatException ex) {
			}
		}
		return false;
	}
	public void doAction(int oldStep) {
		switch (oldStep) {case 1:
			String newThreshold;
			ToDoItem item = (ToDoItem) getToDoItem();
			AbstractCrTooMany critic = (AbstractCrTooMany) item.getPoster();
			if (step1 != null) {
				newThreshold = step1.getText();
				try {
					critic.setThreshold(Integer.parseInt(newThreshold));
				}catch (NumberFormatException ex) {
				}
			}
			break;
		}
	}
}



