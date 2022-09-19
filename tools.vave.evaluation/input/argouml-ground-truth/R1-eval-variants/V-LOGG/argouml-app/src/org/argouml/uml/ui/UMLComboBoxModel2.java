package org.argouml.uml.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.apache.log4j.Logger;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.uml.diagram.ArgoDiagram;
import org.tigris.gef.presentation.Fig;


public abstract class UMLComboBoxModel2 extends AbstractListModel implements PropertyChangeListener,ComboBoxModel,TargetListener,PopupMenuListener {
	private static final Logger LOG = Logger.getLogger(UMLComboBoxModel2.class);
	protected static final String CLEARED = "<none>";
	private Object comboBoxTarget = null;
	private List objects = new LinkedList();
	private Object selectedObject = null;
	private boolean isClearable = false;
	private String propertySetName;
	private boolean fireListEvents = true;
	protected boolean buildingModel = false;
	private boolean processingWillBecomeVisible = false;
	private boolean modelValid;
	public UMLComboBoxModel2(String name,boolean clearable) {
		super();
		if (name == null||name.equals("")) {
			throw new IllegalArgumentException("one of the arguments is null");
		}
		isClearable = clearable;
		propertySetName = name;
	}
	public final void propertyChange(final PropertyChangeEvent pve) {
		if (pve instanceof UmlChangeEvent) {
			final UmlChangeEvent event = (UmlChangeEvent) pve;
			Runnable doWorkRunnable = new Runnable() {
			public void run() {
				try {
					modelChanged(event);
				}catch (InvalidElementException e) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("event = " + event.getClass().getName());
						LOG.debug("source = " + event.getSource());
						LOG.debug("old = " + event.getOldValue());
						LOG.debug("name = " + event.getPropertyName());
						LOG.debug("updateLayout method accessed " + "deleted element ",e);
					}
				}
			}
		};
			SwingUtilities.invokeLater(doWorkRunnable);
		}
	}
	public void modelChanged(UmlChangeEvent evt) {
		buildingModel = true;
		if (evt instanceof AttributeChangeEvent) {
			if (evt.getPropertyName().equals(propertySetName)) {
				if (evt.getSource() == getTarget()&&(isClearable||getChangedElement(evt) != null)) {
					Object elem = getChangedElement(evt);
					if (elem != null&&!contains(elem)) {
						addElement(elem);
					}
					buildingModel = false;
					setSelectedItem(elem);
				}
			}
		}else if (evt instanceof DeleteInstanceEvent) {
			if (contains(getChangedElement(evt))) {
				Object o = getChangedElement(evt);
				removeElement(o);
			}
		}else if (evt instanceof AddAssociationEvent) {
			if (getTarget() != null&&isValidEvent(evt)) {
				if (evt.getPropertyName().equals(propertySetName)&&(evt.getSource() == getTarget())) {
					Object elem = evt.getNewValue();
					setSelectedItem(elem);
				}else {
					Object o = getChangedElement(evt);
					addElement(o);
				}
			}
		}else if (evt instanceof RemoveAssociationEvent&&isValidEvent(evt)) {
			if (evt.getPropertyName().equals(propertySetName)&&(evt.getSource() == getTarget())) {
				if (evt.getOldValue() == internal2external(getSelectedItem())) {
					setSelectedItem(external2internal(evt.getNewValue()));
				}
			}else {
				Object o = getChangedElement(evt);
				if (contains(o)) {
					removeElement(o);
				}
			}
		}else if (evt.getSource()instanceof ArgoDiagram&&evt.getPropertyName().equals(propertySetName)) {
			addElement(evt.getNewValue());
			buildingModel = false;
			setSelectedItem(evt.getNewValue());
		}
		buildingModel = false;
	}
	protected abstract boolean isValidElement(Object element);
	protected abstract void buildModelList();
	protected String getName(Object obj) {
		try {
			Object n = Model.getFacade().getName(obj);
			String name = (n != null?(String) n:"");
			return name;
		}catch (InvalidElementException e) {
			return"";
		}
	}
	protected void setElements(Collection elements) {
		if (elements != null) {
			ArrayList toBeRemoved = new ArrayList();
			for (Object o:objects) {
				if (!elements.contains(o)&&!(isClearable&&("".equals(o)||CLEARED.equals(o)))) {
					toBeRemoved.add(o);
				}
			}
			removeAll(toBeRemoved);
			addAll(elements);
			if (isClearable&&!elements.contains(CLEARED)) {
				addElement(CLEARED);
			}
			if (!objects.contains(selectedObject)) {
				selectedObject = null;
			}
		}else {
			throw new IllegalArgumentException("In setElements: may not set " + "elements to null collection");
		}
	}
	protected Object getTarget() {
		return comboBoxTarget;
	}
	protected void removeAll(Collection col) {
		int first = -1;
		int last = -1;
		fireListEvents = false;
		for (Object o:col) {
			int index = getIndexOf(o);
			removeElement(o);
			if (first == -1) {
				first = index;
				last = index;
			}else {
				if (index != last + 1) {
					fireListEvents = true;
					fireIntervalRemoved(this,first,last);
					fireListEvents = false;
					first = index;
					last = index;
				}else {
					last++;
				}
			}
		}
		fireListEvents = true;
	}
	protected void addAll(Collection col) {
		Object selected = getSelectedItem();
		fireListEvents = false;
		int oldSize = objects.size();
		for (Object o:col) {
			addElement(o);
		}
		setSelectedItem(external2internal(selected));
		fireListEvents = true;
		if (objects.size() != oldSize) {
			fireIntervalAdded(this,oldSize == 0?0:oldSize - 1,objects.size() - 1);
		}
	}
	protected Object getChangedElement(PropertyChangeEvent e) {
		if (e instanceof AssociationChangeEvent) {
			return((AssociationChangeEvent) e).getChangedValue();
		}
		return e.getNewValue();
	}
	public void setTarget(Object theNewTarget) {
		if (theNewTarget != null&&theNewTarget.equals(comboBoxTarget)) {
			LOG.debug("Ignoring duplicate setTarget request " + theNewTarget);
			return;
		}
		modelValid = false;
		LOG.debug("setTarget target :  " + theNewTarget);
		theNewTarget = theNewTarget instanceof Fig?((Fig) theNewTarget).getOwner():theNewTarget;
		if (Model.getFacade().isAModelElement(theNewTarget)||theNewTarget instanceof ArgoDiagram) {
			if (Model.getFacade().isAModelElement(comboBoxTarget)) {
				Model.getPump().removeModelEventListener(this,comboBoxTarget,propertySetName);
				removeOtherModelEventListeners(comboBoxTarget);
			}else if (comboBoxTarget instanceof ArgoDiagram) {
				((ArgoDiagram) comboBoxTarget).removePropertyChangeListener(ArgoDiagram.NAMESPACE_KEY,this);
			}
			if (Model.getFacade().isAModelElement(theNewTarget)) {
				comboBoxTarget = theNewTarget;
				Model.getPump().addModelEventListener(this,comboBoxTarget,propertySetName);
				addOtherModelEventListeners(comboBoxTarget);
				buildingModel = true;
				buildMinimalModelList();
				setSelectedItem(external2internal(getSelectedModelElement()));
				buildingModel = false;
				if (getSize() > 0) {
					fireIntervalAdded(this,0,getSize() - 1);
				}
			}else if (theNewTarget instanceof ArgoDiagram) {
				comboBoxTarget = theNewTarget;
				ArgoDiagram diagram = (ArgoDiagram) theNewTarget;
				diagram.addPropertyChangeListener(ArgoDiagram.NAMESPACE_KEY,this);
				buildingModel = true;
				buildMinimalModelList();
				setSelectedItem(external2internal(getSelectedModelElement()));
				buildingModel = false;
				if (getSize() > 0) {
					fireIntervalAdded(this,0,getSize() - 1);
				}
			}else {
				comboBoxTarget = null;
				removeAllElements();
			}
			if (getSelectedItem() != null&&isClearable) {
				addElement(CLEARED);
			}
		}
	}
	protected void buildMinimalModelList() {
		buildModelListTimed();
	}
	private void buildModelListTimed() {
		long startTime = System.currentTimeMillis();
		try {
			buildModelList();
			long endTime = System.currentTimeMillis();
			LOG.debug("buildModelList took " + (endTime - startTime) + " msec. for " + this.getClass().getName());
		}catch (InvalidElementException e) {
			LOG.warn("buildModelList attempted to operate on " + "deleted element");
		}
	}
	protected void removeOtherModelEventListeners(Object oldTarget) {
	}
	protected void addOtherModelEventListeners(Object newTarget) {
	}
	protected abstract Object getSelectedModelElement();
	public Object getElementAt(int index) {
		if (index >= 0&&index < objects.size()) {
			return objects.get(index);
		}
		return null;
	}
	public int getSize() {
		return objects.size();
	}
	public int getIndexOf(Object o) {
		return objects.indexOf(o);
	}
	public void addElement(Object o) {
		if (!objects.contains(o)) {
			objects.add(o);
			fireIntervalAdded(this,objects.size() - 1,objects.size() - 1);
		}
	}
	public void setSelectedItem(Object o) {
		if ((selectedObject != null&&!selectedObject.equals(o))||(selectedObject == null&&o != null)) {
			selectedObject = o;
			fireContentsChanged(this,-1,-1);
		}
	}
	public void removeElement(Object o) {
		int index = objects.indexOf(o);
		if (getElementAt(index) == selectedObject) {
			if (!isClearable) {
				if (index == 0) {
					setSelectedItem(getSize() == 1?null:getElementAt(index + 1));
				}else {
					setSelectedItem(getElementAt(index - 1));
				}
			}
		}
		if (index >= 0) {
			objects.remove(index);
			fireIntervalRemoved(this,index,index);
		}
	}
	public void removeAllElements() {
		int startIndex = 0;
		int endIndex = Math.max(0,objects.size() - 1);
		objects.clear();
		selectedObject = null;
		fireIntervalRemoved(this,startIndex,endIndex);
	}
	public Object getSelectedItem() {
		return selectedObject;
	}
	private Object external2internal(Object o) {
		return o == null&&isClearable?CLEARED:o;
	}
	private Object internal2external(Object o) {
		return isClearable&&CLEARED.equals(o)?null:o;
	}
	public boolean contains(Object elem) {
		if (objects.contains(elem)) {
			return true;
		}
		if (elem instanceof Collection) {
			for (Object o:(Collection) elem) {
				if (!objects.contains(o)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	protected boolean isValidEvent(PropertyChangeEvent e) {
		boolean valid = false;
		if (!(getChangedElement(e)instanceof Collection)) {
			if ((e.getNewValue() == null&&e.getOldValue() != null)||isValidElement(getChangedElement(e))) {
				valid = true;
			}
		}else {
			Collection col = (Collection) getChangedElement(e);
			if (!col.isEmpty()) {
				valid = true;
				for (Object o:col) {
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
	@Override protected void fireContentsChanged(Object source,int index0,int index1) {
		if (fireListEvents&&!buildingModel) {
			super.fireContentsChanged(source,index0,index1);
		}
	}
	@Override protected void fireIntervalAdded(Object source,int index0,int index1) {
		if (fireListEvents&&!buildingModel) {
			super.fireIntervalAdded(source,index0,index1);
		}
	}
	@Override protected void fireIntervalRemoved(Object source,int index0,int index1) {
		if (fireListEvents&&!buildingModel) {
			super.fireIntervalRemoved(source,index0,index1);
		}
	}
	public void targetAdded(TargetEvent e) {
		LOG.debug("targetAdded targetevent :  " + e);
		setTarget(e.getNewTarget());
	}
	public void targetRemoved(TargetEvent e) {
		LOG.debug("targetRemoved targetevent :  " + e);
		Object currentTarget = comboBoxTarget;
		Object oldTarget = e.getOldTargets(). > 0?e.getOldTargets()[0]:null;
		if (oldTarget instanceof Fig) {
			oldTarget = ((Fig) oldTarget).getOwner();
		}
		if (oldTarget == currentTarget) {
			if (Model.getFacade().isAModelElement(currentTarget)) {
				Model.getPump().removeModelEventListener(this,currentTarget,propertySetName);
			}
			comboBoxTarget = e.getNewTarget();
		}
		setTarget(e.getNewTarget());
	}
	public void targetSet(TargetEvent e) {
		LOG.debug("targetSet targetevent :  " + e);
		setTarget(e.getNewTarget());
	}
	protected boolean isClearable() {
		return isClearable;
	}
	protected String getPropertySetName() {
		return propertySetName;
	}
	protected boolean isFireListEvents() {
		return fireListEvents;
	}
	protected void setFireListEvents(boolean events) {
		this.fireListEvents = events;
	}
	protected boolean isLazy() {
		return false;
	}
	protected void setModelInvalid() {
		assert isLazy();
		modelValid = false;
	}
	public void popupMenuCanceled(PopupMenuEvent e) {
	}
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
	}
	public void popupMenuWillBecomeVisible(PopupMenuEvent ev) {
		if (isLazy()&&!modelValid&&!processingWillBecomeVisible) {
			buildModelListTimed();
			modelValid = true;
			JComboBox list = (JComboBox) ev.getSource();
			processingWillBecomeVisible = true;
			try {
				list.getUI().setPopupVisible(list,true);
			}finally {
				processingWillBecomeVisible = false;
			}
		}
	}
}



