package org.argouml.uml.ui.foundation.core;

import java.util.List;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementOrderedListModel2;


public class UMLClassifierFeatureListModel extends UMLModelElementOrderedListModel2 {
	public UMLClassifierFeatureListModel() {
		super("feature");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getFeatures(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getFeatures(getTarget()).contains(element);
	}
	protected void moveDown(int index) {
		Object clss = getTarget();
		List c = Model.getFacade().getFeatures(clss);
		if (index < c.size() - 1) {
			Object mem = c.get(index);
			Model.getCoreHelper().removeFeature(clss,mem);
			Model.getCoreHelper().addFeature(clss,index + 1,mem);
		}
	}
	@Override protected void moveToBottom(int index) {
		Object clss = getTarget();
		List c = Model.getFacade().getFeatures(clss);
		if (index < c.size() - 1) {
			Object mem = c.get(index);
			Model.getCoreHelper().removeFeature(clss,mem);
			Model.getCoreHelper().addFeature(clss,c.size(),mem);
		}
	}
	@Override protected void moveToTop(int index) {
		Object clss = getTarget();
		List c = Model.getFacade().getFeatures(clss);
		if (index > 0) {
			Object mem = c.get(index);
			Model.getCoreHelper().removeFeature(clss,mem);
			Model.getCoreHelper().addFeature(clss,0,mem);
		}
	}
}



