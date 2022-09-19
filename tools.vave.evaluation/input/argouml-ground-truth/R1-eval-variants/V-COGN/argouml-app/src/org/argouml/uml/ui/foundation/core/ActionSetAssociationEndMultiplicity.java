package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.ActionSetMultiplicity;


public class ActionSetAssociationEndMultiplicity extends ActionSetMultiplicity {
	private static final ActionSetAssociationEndMultiplicity SINGLETON = new ActionSetAssociationEndMultiplicity();
	public ActionSetAssociationEndMultiplicity() {
		super();
	}
	public void setSelectedItem(Object item,Object target) {
		if (target != null&&Model.getFacade().isAAssociationEnd(target)) {
			if (Model.getFacade().isAMultiplicity(item)) {
				if (!item.equals(Model.getFacade().getMultiplicity(target))) {
					Model.getCoreHelper().setMultiplicity(target,item);
				}
			}else if (item instanceof String) {
				if (!item.equals(Model.getFacade().toString(Model.getFacade().getMultiplicity(target)))) {
					Model.getCoreHelper().setMultiplicity(target,Model.getDataTypesFactory().createMultiplicity((String) item));
				}
			}else {
				Model.getCoreHelper().setMultiplicity(target,null);
			}
		}
	}
	public static ActionSetAssociationEndMultiplicity getInstance() {
		return SINGLETON;
	}
}



