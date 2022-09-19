package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.argouml.cognitive.ui.WizStepManyTextFields;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class WizManyNames extends UMLWizard {
	private static final Logger LOG = Logger.getLogger(WizManyNames.class);
	private String instructions = Translator.localize("critics.WizManyNames-ins");
	private List mes;
	private WizStepManyTextFields step1;
	public WizManyNames() {
	}
	public void setModelElements(List elements) {
		int mSize = elements.size();
		for (int i = 0;i < 3&&i < mSize;++i) {
			if (!Model.getFacade().isAModelElement(elements.get(i))) {
				throw new IllegalArgumentException("The list should contain model elements in " + "the first 3 positions");
			}
		}
		mes = elements;
	}
	public JPanel makePanel(int newStep) {
		switch (newStep) {case 1:
			if (step1 == null) {
				List<String>names = new ArrayList<String>();
				int size = mes.size();
				for (int i = 0;i < size;i++) {
					Object me = mes.get(i);
					names.add(Model.getFacade().getName(me));
				}
				step1 = new WizStepManyTextFields(this,instructions,names);
			}
			return step1;
		default:
		}
		return null;
	}
	public void doAction(int oldStep) {
		LOG.debug("doAction " + oldStep);
		switch (oldStep) {case 1:
			List<String>newNames = null;
			if (step1 != null) {
				newNames = step1.getStringList();
			}
			try {
				int size = mes.size();
				for (int i = 0;i < size;i++) {
					Object me = mes.get(i);
					Model.getCoreHelper().setName(me,newNames.get(i));
				}
			}catch (Exception pve) {
				LOG.error("could not set name",pve);
			}
			break;
		default:
		}
	}
	private static final long serialVersionUID = -2827847568754795770l;
}



