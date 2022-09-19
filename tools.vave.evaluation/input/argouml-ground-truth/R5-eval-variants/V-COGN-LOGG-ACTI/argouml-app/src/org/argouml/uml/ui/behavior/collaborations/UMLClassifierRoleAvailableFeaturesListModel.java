package org.argouml.uml.ui.behavior.collaborations;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.tigris.gef.presentation.Fig;


public class UMLClassifierRoleAvailableFeaturesListModel extends UMLModelElementListModel2 {
	public UMLClassifierRoleAvailableFeaturesListModel() {
		super();
	}
	protected void buildModelList() {
	}
	public void propertyChange(PropertyChangeEvent e) {
		if (e instanceof AddAssociationEvent) {
			if (e.getPropertyName().equals("base")&&e.getSource() == getTarget()) {
				Object clazz = getChangedElement(e);
				addAll(Model.getFacade().getFeatures(clazz));
				Model.getPump().addModelEventListener(this,clazz,"feature");
			}else if (e.getPropertyName().equals("feature")&&Model.getFacade().getBases(getTarget()).contains(e.getSource())) {
				addElement(getChangedElement(e));
			}
		}else if (e instanceof RemoveAssociationEvent) {
			if (e.getPropertyName().equals("base")&&e.getSource() == getTarget()) {
				Object clazz = getChangedElement(e);
				Model.getPump().removeModelEventListener(this,clazz,"feature");
			}else if (e.getPropertyName().equals("feature")&&Model.getFacade().getBases(getTarget()).contains(e.getSource())) {
				removeElement(getChangedElement(e));
			}
		}else {
			super.propertyChange(e);
		}
	}
	public void setTarget(Object target) {
		if (getTarget() != null) {
			Enumeration enumeration = elements();
			while (enumeration.hasMoreElements()) {
				Object base = enumeration.nextElement();
				Model.getPump().removeModelEventListener(this,base,"feature");
			}
			Model.getPump().removeModelEventListener(this,getTarget(),"base");
		}
		target = target instanceof Fig?((Fig) target).getOwner():target;
		if (!Model.getFacade().isAModelElement(target))return;
		setListTarget(target);
		if (getTarget() != null) {
			Collection bases = Model.getFacade().getBases(getTarget());
			Iterator it = bases.iterator();
			while (it.hasNext()) {
				Object base = it.next();
				Model.getPump().addModelEventListener(this,base,"feature");
			}
			Model.getPump().addModelEventListener(this,getTarget(),"base");
			removeAllElements();
			setBuildingModel(true);
			buildModelList();
			setBuildingModel(false);
			if (getSize() > 0) {
				fireIntervalAdded(this,0,getSize() - 1);
			}
		}
	}
	protected boolean isValidElement(Object element) {
		return false;
	}
}



