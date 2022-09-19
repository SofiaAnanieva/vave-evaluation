package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementOrderedListModel2;


public class UMLAssociationConnectionListModel extends UMLModelElementOrderedListModel2 {
	private Collection others;
	public UMLAssociationConnectionListModel() {
		super("connection");
	}
	protected void addOtherModelEventListeners(Object newTarget) {
		super.addOtherModelEventListeners(newTarget);
		others = new ArrayList(Model.getFacade().getConnections(newTarget));
		Iterator i = others.iterator();
		while (i.hasNext()) {
			Object end = i.next();
			Model.getPump().addModelEventListener(this,end,"name");
		}
	}
	protected void removeOtherModelEventListeners(Object oldTarget) {
		super.removeOtherModelEventListeners(oldTarget);
		Iterator i = others.iterator();
		while (i.hasNext()) {
			Object end = i.next();
			Model.getPump().removeModelEventListener(this,end,"name");
		}
		others.clear();
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getConnections(getTarget()));
		}
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isAAssociationEnd(o)&&Model.getFacade().getConnections(getTarget()).contains(o);
	}
	protected void moveDown(int index) {
		Object assoc = getTarget();
		List c = (List) Model.getFacade().getConnections(assoc);
		if (index < c.size() - 1) {
			Object mem = c.get(index);
			Model.getCoreHelper().removeConnection(assoc,mem);
			Model.getCoreHelper().addConnection(assoc,index + 1,mem);
		}
	}
	@Override protected void moveToBottom(int index) {
		Object assoc = getTarget();
		List c = (List) Model.getFacade().getConnections(assoc);
		if (index < c.size() - 1) {
			Object mem1 = c.get(index);
			Model.getCoreHelper().removeConnection(assoc,mem1);
			Model.getCoreHelper().addConnection(assoc,c.size() - 1,mem1);
		}
	}
	@Override protected void moveToTop(int index) {
		Object assoc = getTarget();
		List c = (List) Model.getFacade().getConnections(assoc);
		if (index > 0) {
			Object mem1 = c.get(index);
			Model.getCoreHelper().removeConnection(assoc,mem1);
			Model.getCoreHelper().addConnection(assoc,0,mem1);
		}
	}
}



