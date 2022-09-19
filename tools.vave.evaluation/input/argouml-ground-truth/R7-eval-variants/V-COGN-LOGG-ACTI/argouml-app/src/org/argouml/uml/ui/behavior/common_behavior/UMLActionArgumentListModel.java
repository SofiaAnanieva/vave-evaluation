package org.argouml.uml.ui.behavior.common_behavior;

import java.util.List;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementOrderedListModel2;


public class UMLActionArgumentListModel extends UMLModelElementOrderedListModel2 {
	public UMLActionArgumentListModel() {
		super("actualArgument");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getActualArguments(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isAArgument(element);
	}
	private static final long serialVersionUID = -3265997785192090331l;
	protected void moveDown(int index) {
		Object clss = getTarget();
		List c = Model.getFacade().getActualArguments(clss);
		if (index < c.size() - 1) {
			Object mem = c.get(index);
			Model.getCommonBehaviorHelper().removeActualArgument(clss,mem);
			Model.getCommonBehaviorHelper().addActualArgument(clss,index + 1,mem);
		}
	}
	@Override protected void moveToBottom(int index) {
		Object clss = getTarget();
		List c = Model.getFacade().getActualArguments(clss);
		if (index < c.size() - 1) {
			Object mem = c.get(index);
			Model.getCommonBehaviorHelper().removeActualArgument(clss,mem);
			Model.getCommonBehaviorHelper().addActualArgument(clss,c.size() - 1,mem);
		}
	}
	@Override protected void moveToTop(int index) {
		Object clss = getTarget();
		List c = Model.getFacade().getActualArguments(clss);
		if (index > 0) {
			Object mem = c.get(index);
			Model.getCommonBehaviorHelper().removeActualArgument(clss,mem);
			Model.getCommonBehaviorHelper().addActualArgument(clss,0,mem);
		}
	}
}



