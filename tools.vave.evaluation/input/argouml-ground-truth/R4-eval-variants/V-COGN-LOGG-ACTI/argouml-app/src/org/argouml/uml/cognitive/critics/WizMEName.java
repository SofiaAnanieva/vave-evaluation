package org.argouml.uml.cognitive.critics;

import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.argouml.cognitive.ui.WizStepTextField;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class WizMEName extends UMLWizard {
	private static final Logger LOG = Logger.getLogger(WizMEName.class);
	private String instructions = Translator.localize("critics.WizMEName-ins");
	private String label = Translator.localize("label.name");
	private boolean mustEdit = false;
	private WizStepTextField step1 = null;
	private String origSuggest;
	public WizMEName() {
	}
	public void setInstructions(String s) {
		instructions = s;
	}
	public void setMustEdit(boolean b) {
		mustEdit = b;
	}
	public JPanel makePanel(int newStep) {
		switch (newStep) {case 1:
			if (step1 == null) {
				step1 = new WizStepTextField(this,instructions,label,offerSuggestion());
			}
			return step1;
		}
		return null;
	}
	public void setSuggestion(String s) {
		origSuggest = s;
		super.setSuggestion(s);
	}
	public boolean canGoNext() {
		if (!super.canGoNext())return false;
		if (step1 != null) {
			boolean changed = origSuggest.equals(step1.getText());
			if (mustEdit&&!changed)return false;
		}
		return true;
	}
	public void doAction(int oldStep) {
		LOG.debug("doAction " + oldStep);
		switch (oldStep) {case 1:
			String newName = getSuggestion();
			if (step1 != null) {
				newName = step1.getText();
			}
			try {
				Object me = getModelElement();
				Model.getCoreHelper().setName(me,newName);
			}catch (Exception pve) {
				LOG.error("could not set name",pve);
			}
			break;
		}
	}
	protected String getInstructions() {
		return instructions;
	}
}



