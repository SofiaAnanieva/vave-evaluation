package org.argouml.uml.ui.foundation.core;

import java.util.List;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementOrderedListModel2;


public class UMLClassAttributeListModel extends UMLModelElementOrderedListModel2 {
	public UMLClassAttributeListModel() {
		super("feature");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getAttributes(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return(Model.getFacade().getAttributes(getTarget()).contains(element));
	}
	protected void moveDown(int index1) {
		int index2 = index1 + 1;
		Object clss = getTarget();
		List c = Model.getFacade().getAttributes(clss);
		if (index1 < c.size() - 1) {
			Object mem1 = c.get(index1);
			Object mem2 = c.get(index2);
			List f = Model.getFacade().getFeatures(clss);
			index2 = f.indexOf(mem2);
			Model.getCoreHelper().removeFeature(clss,mem1);
			Model.getCoreHelper().addFeature(clss,index2,mem1);
		}
	}
	@Override protected void moveToBottom(int index) {
		Object clss = getTarget();
		List c = Model.getFacade().getAttributes(clss);
		if (index < c.size() - 1) {
			Object mem1 = c.get(index);
			Model.getCoreHelper().removeFeature(clss,mem1);
			Model.getCoreHelper().addFeature(clss,c.size() - 1,mem1);
		}
	}
	@Override protected void moveToTop(int index) {
		Object clss = getTarget();
		List c = Model.getFacade().getAttributes(clss);
		if (index > 0) {
			Object mem1 = c.get(index);
			Model.getCoreHelper().removeFeature(clss,mem1);
			Model.getCoreHelper().addFeature(clss,0,mem1);
		}
	}
}



