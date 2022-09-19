package org.argouml.uml.cognitive.critics;

import org.argouml.cognitive.ToDoItem;


public abstract class AbstractCrTooMany extends CrUML {
	private int criticThreshold;
	public void setThreshold(int threshold) {
		criticThreshold = threshold;
	}
	public int getThreshold() {
		return criticThreshold;
	}
	public Class getWizardClass(ToDoItem item) {
		return WizTooMany.class;
	}
}



