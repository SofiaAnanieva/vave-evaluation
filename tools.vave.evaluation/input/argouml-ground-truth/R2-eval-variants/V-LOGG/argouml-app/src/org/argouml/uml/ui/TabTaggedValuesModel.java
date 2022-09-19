package org.argouml.uml.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;


public class TabTaggedValuesModel extends AbstractTableModel implements VetoableChangeListener,DelayedVChangeListener,PropertyChangeListener {
	private static final Logger LOG = Logger.getLogger(TabTaggedValuesModel.class);
	private Object target;
	public TabTaggedValuesModel() {
	}
	public void setTarget(Object t) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Set target to " + t);
		}
		if (t != null&&!Model.getFacade().isAModelElement(t)) {
			throw new IllegalArgumentException();
		}
		if (target != t) {
			if (target != null) {
				Model.getPump().removeModelEventListener(this,target);
			}
			target = t;
			if (t != null) {
				Model.getPump().addModelEventListener(this,t,new String[] {"taggedValue","referenceTag"});
			}
		}
		fireTableDataChanged();
	}
	public int getColumnCount() {
		return 2;
	}
	@Override public String getColumnName(int c) {
		if (c == 0) {
			return Translator.localize("label.taggedvaluespane.tag");
		}
		if (c == 1) {
			return Translator.localize("label.taggedvaluespane.value");
		}
		return"XXX";
	}
	@Override public Class getColumnClass(int c) {
		if (c == 0) {
			return(Class) Model.getMetaTypes().getTagDefinition();
		}
		if (c == 1) {
			return String.class;
		}
		return null;
	}
	@Override public boolean isCellEditable(int row,int col) {
		return true;
	}
	public int getRowCount() {
		if (target == null) {
			return 0;
		}
		try {
			Collection tvs = Model.getFacade().getTaggedValuesCollection(target);
			return tvs.size() + 1;
		}catch (InvalidElementException e) {
			return 0;
		}
	}
	public Object getValueAt(int row,int col) {
		Collection tvs = Model.getFacade().getTaggedValuesCollection(target);
		if (row > tvs.size()||col > 1) {
			throw new IllegalArgumentException();
		}
		if (row == tvs.size()) {
			return"";
		}
		Object tv = tvs.toArray()[row];
		if (col == 0) {
			Object n = Model.getFacade().getTagDefinition(tv);
			if (n == null) {
				return"";
			}
			return n;
		}
		if (col == 1) {
			String be = Model.getFacade().getValueOfTag(tv);
			if (be == null) {
				return"";
			}
			return be;
		}
		return"TV-" + row * 2 + col;
	}
	@Override public void setValueAt(Object aValue,int rowIndex,int columnIndex) {
		if (columnIndex != 0&&columnIndex != 1) {
			return;
		}
		if (columnIndex == 1&&aValue == null) {
			aValue = "";
		}
		if ((aValue == null||"".equals(aValue))&&columnIndex == 0) {
			removeRow(rowIndex);
			return;
		}
		Collection tvs = Model.getFacade().getTaggedValuesCollection(target);
		if (tvs.size() <= rowIndex) {
			if (columnIndex == 0) {
				addRow(new Object[] {aValue,null});
			}
			if (columnIndex == 1) {
				addRow(new Object[] {null,aValue});
			}
		}else {
			Object tv = getFromCollection(tvs,rowIndex);
			if (columnIndex == 0) {
				Model.getExtensionMechanismsHelper().setType(tv,aValue);
			}else if (columnIndex == 1) {
				Model.getExtensionMechanismsHelper().setDataValues(tv,new String[] {(String) aValue});
			}
			fireTableChanged(new TableModelEvent(this,rowIndex,rowIndex,columnIndex));
		}
	}
	public void addRow(Object[]values) {
		Object tagType = values[0];
		String tagValue = (String) values[1];
		if (tagType == null) {
			tagType = "";
		}
		if (tagValue == null) {
			tagValue = "";
		}
		Object tv = Model.getExtensionMechanismsFactory().createTaggedValue();
		Model.getExtensionMechanismsHelper().addTaggedValue(target,tv);
		Model.getExtensionMechanismsHelper().setType(tv,tagType);
		Model.getExtensionMechanismsHelper().setDataValues(tv,new String[] {tagValue});
		fireTableChanged(new TableModelEvent(this));
	}
	public void removeRow(int row) {
		Collection c = Model.getFacade().getTaggedValuesCollection(target);
		if ((row >= 0)&&(row < c.size())) {
			Object element = getFromCollection(c,row);
			Model.getUmlFactory().delete(element);
			fireTableChanged(new TableModelEvent(this));
		}
	}
	static Object getFromCollection(Collection collection,int index) {
		if (collection instanceof List) {
			return((List) collection).get(index);
		}
		if (index >= collection.size()||index < 0) {
			throw new IndexOutOfBoundsException();
		}
		Iterator it = collection.iterator();
		for (int i = 0;i < index;i++) {
			it.next();
		}
		return it.next();
	}
	public void propertyChange(PropertyChangeEvent evt) {
		if ("taggedValue".equals(evt.getPropertyName())||"referenceTag".equals(evt.getPropertyName())) {
			fireTableChanged(new TableModelEvent(this));
		}
		if (evt instanceof DeleteInstanceEvent&&evt.getSource() == target) {
			setTarget(null);
		}
	}
	public void vetoableChange(PropertyChangeEvent pce) {
		DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this,pce);
		SwingUtilities.invokeLater(delayedNotify);
	}
	public void delayedVetoableChange(PropertyChangeEvent pce) {
		fireTableDataChanged();
	}
	private static final long serialVersionUID = -5711005901444956345l;
}



