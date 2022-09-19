package org.argouml.uml.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JPopupMenu;
import org.apache.log4j.Logger;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;


public abstract class UMLModelElementListModel2 extends DefaultListModel implements TargetListener,PropertyChangeListener {
	private static final Logger LOG = Logger.getLogger(UMLModelElementListModel2.class);
	private String eventName = null;
	private Object listTarget = null;
	private boolean fireListEvents = true;
	private boolean buildingModel = false;
	private Object metaType;
	private boolean reverseDropConnection;
	public UMLModelElementListModel2() {
		super();
	}
	public UMLModelElementListModel2(String name) {
		super();
		eventName = name;
	}
	public UMLModelElementListModel2(String name,Object theMetaType) {
		super();
		this.metaType = theMetaType;
		eventName = name;
	}
	public UMLModelElementListModel2(String name,Object theMetaType,boolean reverseTheDropConnection) {
		super();
		this.metaType = theMetaType;
		eventName = name;
		this.reverseDropConnection = reverseTheDropConnection;
	}
	public Object getMetaType() {
		return metaType;
	}
	public boolean isReverseDropConnection() {
		return reverseDropConnection;
	}
	protected void setBuildingModel(boolean building) {
		this.buildingModel = building;
	}
	protected void setListTarget(Object t) {
		this.listTarget = t;
	}
	public void propertyChange(PropertyChangeEvent e) {
		if (e instanceof AttributeChangeEvent) {
			try {
				if (isValidEvent(e)) {
					rebuildModelList();
				}
			}catch (InvalidElementException iee) {
				return;
			}
		}else if (e instanceof AddAssociationEvent) {
			if (isValidEvent(e)) {
				Object o = getChangedElement(e);
				if (o instanceof Collection) {
					ArrayList tempList = new ArrayList((Collection) o);
					Iterator it = tempList.iterator();
					while (it.hasNext()) {
						Object o2 = it.next();
						addElement(o2);
					}
				}else {
					addElement(o);
				}
			}
		}else if (e instanceof RemoveAssociationEvent) {
			boolean valid = false;
			if (!(getChangedElement(e)instanceof Collection)) {
				valid = contains(getChangedElement(e));
			}else {
				Collection col = (Collection) getChangedElement(e);
				Iterator it = col.iterator();
				valid = true;
				while (it.hasNext()) {
					Object o = it.next();
					if (!contains(o)) {
						valid = false;
						break;
					}
				}
			}
			if (valid) {
				Object o = getChangedElement(e);
				if (o instanceof Collection) {
					Iterator it = ((Collection) o).iterator();
					while (it.hasNext()) {
						Object o3 = it.next();
						removeElement(o3);
					}
				}else {
					removeElement(o);
				}
			}
		}
	}
	private void rebuildModelList() {
		removeAllElements();
		buildingModel = true;
		try {
			buildModelList();
		}catch (InvalidElementException exception) {
			LOG.debug("buildModelList threw exception for target " + getTarget() + ": " + exception);
		}finally {
			buildingModel = false;
		}
		if (getSize() > 0) {
			fireIntervalAdded(this,0,getSize() - 1);
		}
	}
	protected abstract void buildModelList();
	protected void setAllElements(Collection col) {
		if (!isEmpty())removeAllElements();
		addAll(col);
	}
	protected void addAll(Collection col) {
		if (col.size() == 0)return;
		Iterator it = col.iterator();
		fireListEvents = false;
		int intervalStart = getSize() == 0?0:getSize() - 1;
		while (it.hasNext()) {
			Object o = it.next();
			addElement(o);
		}
		fireListEvents = true;
		fireIntervalAdded(this,intervalStart,getSize() - 1);
	}
	public Object getTarget() {
		return listTarget;
	}
	protected Object getChangedElement(PropertyChangeEvent e) {
		if (e instanceof AssociationChangeEvent) {
			return((AssociationChangeEvent) e).getChangedValue();
		}
		if (e instanceof AttributeChangeEvent) {
			return((AttributeChangeEvent) e).getSource();
		}
		return e.getNewValue();
	}
	public boolean contains(Object elem) {
		if (super.contains(elem)) {
			return true;
		}
		if (elem instanceof Collection) {
			Iterator it = ((Collection) elem).iterator();
			while (it.hasNext()) {
				if (!super.contains(it.next())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	public void setTarget(Object theNewTarget) {
		theNewTarget = theNewTarget instanceof Fig?((Fig) theNewTarget).getOwner():theNewTarget;
		if (Model.getFacade().isAUMLElement(theNewTarget)||theNewTarget instanceof Diagram) {
			if (Model.getFacade().isAUMLElement(listTarget)) {
				Model.getPump().removeModelEventListener(this,listTarget,eventName);
				removeOtherModelEventListeners(listTarget);
			}
			if (Model.getFacade().isAUMLElement(theNewTarget)) {
				listTarget = theNewTarget;
				Model.getPump().addModelEventListener(this,listTarget,eventName);
				addOtherModelEventListeners(listTarget);
				rebuildModelList();
			}else {
				listTarget = null;
				removeAllElements();
			}
		}
	}
	protected void removeOtherModelEventListeners(Object oldTarget) {
	}
	protected void addOtherModelEventListeners(Object newTarget) {
	}
	protected abstract boolean isValidElement(Object element);
	protected boolean isValidEvent(PropertyChangeEvent e) {
		boolean valid = false;
		if (!(getChangedElement(e)instanceof Collection)) {
			if ((e.getNewValue() == null&&e.getOldValue() != null)||isValidElement(getChangedElement(e))) {
				valid = true;
			}
		}else {
			Collection col = (Collection) getChangedElement(e);
			Iterator it = col.iterator();
			if (!col.isEmpty()) {
				valid = true;
				while (it.hasNext()) {
					Object o = it.next();
					if (!isValidElement(o)) {
						valid = false;
						break;
					}
				}
			}else {
				if (e.getOldValue()instanceof Collection&&!((Collection) e.getOldValue()).isEmpty()) {
					valid = true;
				}
			}
		}
		return valid;
	}
	public void addElement(Object obj) {
		if (obj != null&&!contains(obj)) {
			super.addElement(obj);
		}
	}
	String getEventName() {
		return eventName;
	}
	protected void setEventName(String theEventName) {
		eventName = theEventName;
	}
	public void targetAdded(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetRemoved(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetSet(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	protected void fireContentsChanged(Object source,int index0,int index1) {
		if (fireListEvents&&!buildingModel)super.fireContentsChanged(source,index0,index1);
	}
	protected void fireIntervalAdded(Object source,int index0,int index1) {
		if (fireListEvents&&!buildingModel)super.fireIntervalAdded(source,index0,index1);
	}
	protected void fireIntervalRemoved(Object source,int index0,int index1) {
		if (fireListEvents&&!buildingModel)super.fireIntervalRemoved(source,index0,index1);
	}
	public boolean buildPopup(JPopupMenu popup,int index) {
		return false;
	}
	protected boolean hasPopup() {
		return false;
	}
}



