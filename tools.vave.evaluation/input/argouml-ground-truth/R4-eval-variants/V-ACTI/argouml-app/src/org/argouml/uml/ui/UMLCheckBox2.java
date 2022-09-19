package org.argouml.uml.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.JCheckBox;
import org.argouml.model.Model;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.tigris.gef.presentation.Fig;


public abstract class UMLCheckBox2 extends JCheckBox implements TargetListener,PropertyChangeListener {
	private Object checkBoxTarget;
	private String propertySetName;
	public UMLCheckBox2(String text,Action a,String name) {
		super(text);
		setFont(LookAndFeelMgr.getInstance().getStandardFont());
		propertySetName = name;
		addActionListener(a);
		setActionCommand((String) a.getValue(Action.ACTION_COMMAND_KEY));
	}
	public void propertyChange(PropertyChangeEvent evt) {
		buildModel();
	}
	public Object getTarget() {
		return checkBoxTarget;
	}
	public void setTarget(Object target) {
		target = target instanceof Fig?((Fig) target).getOwner():target;
		if (Model.getFacade().isAUMLElement(checkBoxTarget)) {
			Model.getPump().removeModelEventListener(this,checkBoxTarget,propertySetName);
		}
		if (Model.getFacade().isAUMLElement(target)) {
			checkBoxTarget = target;
			Model.getPump().addModelEventListener(this,checkBoxTarget,propertySetName);
			buildModel();
		}
	}
	public abstract void buildModel();
	public void targetAdded(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetRemoved(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetSet(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
}



