package org.argouml.uml.ui.foundation.core;

import java.util.Collection;
import java.util.List;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementOrderedListModel2;


public class UMLClassifierParameterListModel extends UMLModelElementOrderedListModel2 {
	public UMLClassifierParameterListModel() {
		super("parameter");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getParameters(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getParameters(getTarget()).contains(element);
	}
	protected void moveDown(int index) {
		Object clss = getTarget();
		Collection c = Model.getFacade().getParameters(clss);
		if (c instanceof List&&index < c.size() - 1) {
			Object mem = ((List) c).get(index);
			Model.getCoreHelper().removeParameter(clss,mem);
			Model.getCoreHelper().addParameter(clss,index + 1,mem);
		}
	}
	@Override protected void moveToBottom(int index) {
		Object clss = getTarget();
		Collection c = Model.getFacade().getParameters(clss);
		if (c instanceof List&&index < c.size() - 1) {
			Object mem = ((List) c).get(index);
			Model.getCoreHelper().removeParameter(clss,mem);
			Model.getCoreHelper().addParameter(clss,c.size() - 1,mem);
		}
	}
	@Override protected void moveToTop(int index) {
		Object clss = getTarget();
		Collection c = Model.getFacade().getParameters(clss);
		if (c instanceof List&&index > 0) {
			Object mem = ((List) c).get(index);
			Model.getCoreHelper().removeParameter(clss,mem);
			Model.getCoreHelper().addParameter(clss,0,mem);
		}
	}
}



