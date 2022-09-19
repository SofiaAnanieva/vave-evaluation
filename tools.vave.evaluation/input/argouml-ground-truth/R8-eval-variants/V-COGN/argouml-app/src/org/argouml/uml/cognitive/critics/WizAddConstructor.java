package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import javax.swing.JPanel;
import org.argouml.cognitive.ui.WizStepTextField;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;


public class WizAddConstructor extends UMLWizard {
	private WizStepTextField step1;
	private String label = Translator.localize("label.name");
	private String instructions = Translator.localize("critics.WizAddConstructor-ins");
	public WizAddConstructor() {
		super();
	}
	public void doAction(int oldStep) {
		Object oper;
		Collection savedTargets;
		switch (oldStep) {case 1:
			String newName = getSuggestion();
			if (step1 != null) {
				newName = step1.getText();
			}
			Object me = getModelElement();
			savedTargets = TargetManager.getInstance().getTargets();
			Object returnType = ProjectManager.getManager().getCurrentProject().getDefaultReturnType();
			oper = Model.getCoreFactory().buildOperation2(me,returnType,newName);
			Model.getCoreHelper().addStereotype(oper,getCreateStereotype(oper));
			TargetManager.getInstance().setTargets(savedTargets);
			break;
		}
	}
	private Object getCreateStereotype(Object obj) {
		return ProjectManager.getManager().getCurrentProject().getProfileConfiguration().findStereotypeForObject("create",obj);
	}
	public void setInstructions(String s) {
		instructions = s;
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
	private static final long serialVersionUID = -4661562206721689576l;
}



