package org.argouml.uml.ui.foundation.core;

import java.util.List;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementOrderedListModel2;


public class UMLEnumerationLiteralsListModel extends UMLModelElementOrderedListModel2 {
	public UMLEnumerationLiteralsListModel() {
		super("literal");
	}
	protected void buildModelList() {
		if (Model.getFacade().isAEnumeration(getTarget())) {
			setAllElements(Model.getFacade().getEnumerationLiterals(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		if (Model.getFacade().isAEnumeration(getTarget())) {
			List literals = Model.getFacade().getEnumerationLiterals(getTarget());
			return literals.contains(element);
		}
		return false;
	}
	protected void moveDown(int index) {
		Object clss = getTarget();
		List c = Model.getFacade().getEnumerationLiterals(clss);
		if (index < c.size() - 1) {
			Object mem = c.get(index);
			Model.getCoreHelper().removeLiteral(clss,mem);
			Model.getCoreHelper().addLiteral(clss,index + 1,mem);
		}
	}
	@Override protected void moveToBottom(int index) {
		Object clss = getTarget();
		List c = Model.getFacade().getEnumerationLiterals(clss);
		if (index < c.size() - 1) {
			Object mem = c.get(index);
			Model.getCoreHelper().removeLiteral(clss,mem);
			Model.getCoreHelper().addLiteral(clss,c.size(),mem);
		}
	}
	@Override protected void moveToTop(int index) {
		Object clss = getTarget();
		List c = Model.getFacade().getEnumerationLiterals(clss);
		if (index > 0) {
			Object mem = c.get(index);
			Model.getCoreHelper().removeLiteral(clss,mem);
			Model.getCoreHelper().addLiteral(clss,0,mem);
		}
	}
	private static final long serialVersionUID = 4111214628991094451l;
}



