package org.argouml.uml.ui;

import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;


public abstract class ActionNavigateUpPreviousDown extends AbstractActionNavigate {
	public ActionNavigateUpPreviousDown() {
		super("button.go-up-previous-down",true);
		putValue(Action.SMALL_ICON,ResourceLoaderWrapper.lookupIconResource("NavigateUpPrevious"));
	}
	protected Object navigateTo(Object source) {
		Object up = getParent(source);
		List family = getFamily(up);
		assert family.contains(source);
		Iterator it = family.iterator();
		Object previous = null;
		while (it.hasNext()) {
			Object child = it.next();
			if (child == source) {
				return previous;
			}
			previous = child;
		}
		return null;
	}
	public abstract List getFamily(Object parent);
	public abstract Object getParent(Object child);
}



