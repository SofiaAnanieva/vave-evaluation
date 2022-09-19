package org.argouml.uml.cognitive.critics;

import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.model.Model;


public abstract class UMLWizard extends Wizard {
	private String suggestion;
	public UMLWizard() {
		super();
	}
	public int getNumSteps() {
		return 1;
	}
	public Object getModelElement() {
		if (getToDoItem() != null) {
			ToDoItem item = (ToDoItem) getToDoItem();
			ListSet offs = item.getOffenders();
			if (offs.size() >= 1) {
				Object me = offs.get(0);
				return me;
			}
		}
		return null;
	}
	public String offerSuggestion() {
		if (suggestion != null)return suggestion;
		Object me = getModelElement();
		if (me != null) {
			String n = Model.getFacade().getName(me);
			return n;
		}
		return"";
	}
	public void setSuggestion(String s) {
		suggestion = s;
	}
	public String getSuggestion() {
		return suggestion;
	}
}



