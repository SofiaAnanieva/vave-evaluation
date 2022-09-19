package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.argouml.cognitive.ui.WizStepChoice;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class WizAssocComposite extends UMLWizard {
	private static final Logger LOG = Logger.getLogger(WizAssocComposite.class);
	private String instructions = Translator.localize("critics.WizAssocComposite-ins");
	private WizStepChoice step1Choice = null;
	private Object triggerAssociation = null;
	public WizAssocComposite() {
	}
	private Object getTriggerAssociation() {
		if ((triggerAssociation == null)&&(getToDoItem() != null)) {
			triggerAssociation = getModelElement();
		}
		return triggerAssociation;
	}
	private List<String>buildOptions() {
		Object asc = getTriggerAssociation();
		if (asc == null) {
			return null;
		}
		List<String>result = new ArrayList<String>();
		Iterator iter = Model.getFacade().getConnections(asc).iterator();
		Object ae0 = iter.next();
		Object ae1 = iter.next();
		Object cls0 = Model.getFacade().getType(ae0);
		Object cls1 = Model.getFacade().getType(ae1);
		String start = Translator.localize("misc.name.anon");
		String end = Translator.localize("misc.name.anon");
		if ((cls0 != null)&&(Model.getFacade().getName(cls0) != null)&&(!(Model.getFacade().getName(cls0).equals("")))) {
			start = Model.getFacade().getName(cls0);
		}
		if ((cls1 != null)&&(Model.getFacade().getName(cls1) != null)&&(!(Model.getFacade().getName(cls1).equals("")))) {
			end = Model.getFacade().getName(cls1);
		}
		result.add(start + Translator.localize("critics.WizAssocComposite-option1") + end);
		result.add(start + Translator.localize("critics.WizAssocComposite-option2") + end);
		result.add(end + Translator.localize("critics.WizAssocComposite-option1") + start);
		result.add(end + Translator.localize("critics.WizAssocComposite-option2") + start);
		result.add(Translator.localize("critics.WizAssocComposite-option3"));
		return result;
	}
	public void setInstructions(String s) {
		instructions = s;
	}
	public JPanel makePanel(int newStep) {
		switch (newStep) {case 1:
			if (step1Choice == null) {
				List<String>opts = buildOptions();
				if (opts != null) {
					step1Choice = new WizStepChoice(this,instructions,opts);
					step1Choice.setTarget(getToDoItem());
				}
			}
			return step1Choice;
		default:
		}
		return null;
	}
	public void doAction(int oldStep) {
		switch (oldStep) {case 1:
			int choice = -1;
			if (step1Choice != null) {
				choice = step1Choice.getSelectedIndex();
			}
			if (choice == -1) {
				LOG.warn("WizAssocComposite: nothing selected, " + "should not get here");
				return;
			}
			try {
				Iterator iter = Model.getFacade().getConnections(getTriggerAssociation()).iterator();
				Object ae0 = iter.next();
				Object ae1 = iter.next();
				switch (choice) {case 0:
					Model.getCoreHelper().setAggregation(ae0,Model.getAggregationKind().getComposite());
					Model.getCoreHelper().setAggregation(ae1,Model.getAggregationKind().getNone());
					break;
				case 1:
					Model.getCoreHelper().setAggregation(ae0,Model.getAggregationKind().getAggregate());
					Model.getCoreHelper().setAggregation(ae1,Model.getAggregationKind().getNone());
					break;
				case 2:
					Model.getCoreHelper().setAggregation(ae0,Model.getAggregationKind().getNone());
					Model.getCoreHelper().setAggregation(ae1,Model.getAggregationKind().getComposite());
					break;
				case 3:
					Model.getCoreHelper().setAggregation(ae0,Model.getAggregationKind().getNone());
					Model.getCoreHelper().setAggregation(ae1,Model.getAggregationKind().getAggregate());
					break;
				case 4:
					Model.getCoreHelper().setAggregation(ae0,Model.getAggregationKind().getNone());
					Model.getCoreHelper().setAggregation(ae1,Model.getAggregationKind().getNone());
					break;
				default:
				}
			}catch (Exception pve) {
				LOG.error("WizAssocComposite: could not set " + "aggregation.",pve);
			}
		default:
		}
	}
	@Override public boolean canFinish() {
		if (!super.canFinish()) {
			return false;
		}
		if (getStep() == 0) {
			return true;
		}
		if ((getStep() == 1)&&(step1Choice != null)&&(step1Choice.getSelectedIndex() != -1)) {
			return true;
		}
		return false;
	}
}



