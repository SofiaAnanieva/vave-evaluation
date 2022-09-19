package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import org.argouml.cognitive.ui.WizStepChoice;
import org.argouml.cognitive.ui.WizStepCue;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class WizOperName extends WizMEName {
	private boolean possibleConstructor;
	private boolean stereotypePathChosen;
	private String option0 = Translator.localize("critics.WizOperName-options1");
	private String option1 = Translator.localize("critics.WizOperName-options2");
	private WizStepChoice step1;
	private WizStepCue step2;
	private Object createStereotype;
	private boolean addedCreateStereotype;
	@Override public int getNumSteps() {
		if (possibleConstructor) {
			return 2;
		}
		return 1;
	}
	private List<String>getOptions() {
		List<String>res = new ArrayList<String>();
		res.add(option0);
		res.add(option1);
		return res;
	}
	public void setPossibleConstructor(boolean b) {
		possibleConstructor = b;
	}
	@Override public JPanel makePanel(int newStep) {
		if (!possibleConstructor) {
			return super.makePanel(newStep);
		}
		switch (newStep) {case 0:
			return super.makePanel(newStep);
		case 1:
			if (step1 == null) {
				step1 = new WizStepChoice(this,getInstructions(),getOptions());
				step1.setTarget(getToDoItem());
			}
			return step1;
		case 2:
			if (stereotypePathChosen) {
				if (step2 == null) {
					step2 = new WizStepCue(this,Translator.localize("critics.WizOperName-stereotype"));
					step2.setTarget(getToDoItem());
				}
				return step2;
			}
			return super.makePanel(1);
		default:
		}
		return null;
	}
	@Override public void undoAction(int origStep) {
		super.undoAction(origStep);
		if (getStep() >= 1) {
			removePanel(origStep);
		}
		if (origStep == 1) {
			Object oper = getModelElement();
			if (addedCreateStereotype) {
				Model.getCoreHelper().removeStereotype(oper,createStereotype);
			}
		}
	}
	@Override public void doAction(int oldStep) {
		if (!possibleConstructor) {
			super.doAction(oldStep);
			return;
		}
		switch (oldStep) {case 1:
			int choice = -1;
			if (step1 != null) {
				choice = step1.getSelectedIndex();
			}
			switch (choice) {case-1:
				throw new IllegalArgumentException("nothing selected, should not get here");
			case 0:
				stereotypePathChosen = true;
				Object oper = getModelElement();
				Object m = Model.getFacade().getModel(oper);
				Object theStereotype = null;
				for (Iterator iter = Model.getFacade().getOwnedElements(m).iterator();iter.hasNext();) {
					Object candidate = iter.next();
					if (!(Model.getFacade().isAStereotype(candidate))) {
						continue;
					}
					if (!("create".equals(Model.getFacade().getName(candidate)))) {
						continue;
					}
					Collection baseClasses = Model.getFacade().getBaseClasses(candidate);
					Iterator iter2 = baseClasses != null?baseClasses.iterator():null;
					if (iter2 == null||!("BehavioralFeature".equals(iter2.next()))) {
						continue;
					}
					theStereotype = candidate;
					break;
				}
				if (theStereotype == null) {
					theStereotype = Model.getExtensionMechanismsFactory().buildStereotype("create",m);
					Model.getCoreHelper().setName(theStereotype,"create");
					Model.getExtensionMechanismsHelper().addBaseClass(theStereotype,"BehavioralFeature");
					Object targetNS = findNamespace(Model.getFacade().getNamespace(oper),Model.getFacade().getModel(oper));
					Model.getCoreHelper().addOwnedElement(targetNS,theStereotype);
				}
				try {
					createStereotype = theStereotype;
					Model.getCoreHelper().addStereotype(oper,theStereotype);
					addedCreateStereotype = true;
				}catch (Exception pve) {
				}
				return;
			case 1:
				stereotypePathChosen = false;
				return;
			default:
			}
			return;
		case 2:
			if (!stereotypePathChosen) {
				super.doAction(1);
			}
			return;
		default:
		}
	}
	private static Object findNamespace(Object phantomNS,Object targetModel) {
		Object ns = null;
		Object targetParentNS = null;
		if (phantomNS == null) {
			return targetModel;
		}
		Object parentNS = Model.getFacade().getNamespace(phantomNS);
		if (parentNS == null) {
			return targetModel;
		}
		targetParentNS = findNamespace(parentNS,targetModel);
		Collection ownedElements = Model.getFacade().getOwnedElements(targetParentNS);
		String phantomName = Model.getFacade().getName(phantomNS);
		String targetName;
		if (ownedElements != null) {
			Object ownedElement;
			Iterator iter = ownedElements.iterator();
			while (iter.hasNext()) {
				ownedElement = iter.next();
				targetName = Model.getFacade().getName(ownedElement);
				if (targetName != null&&phantomName.equals(targetName)) {
					if (Model.getFacade().isAPackage(ownedElement)) {
						ns = ownedElement;
						break;
					}
				}
			}
		}
		if (ns == null) {
			ns = Model.getModelManagementFactory().createPackage();
			Model.getCoreHelper().setName(ns,phantomName);
			Model.getCoreHelper().addOwnedElement(targetParentNS,ns);
		}
		return ns;
	}
	private static final long serialVersionUID = -4013730212763172160l;
}



