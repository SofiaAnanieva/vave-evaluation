package org.argouml.uml.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractListModel;


public class SortedListModel extends AbstractListModel implements Collection {
	private Set delegate = new TreeSet(new PathComparator());
	public int getSize() {
		return delegate.size();
	}
	public Object getElementAt(int index) {
		Object result = null;
		Iterator it = delegate.iterator();
		while (index >= 0) {
			if (it.hasNext()) {
				result = it.next();
			}else {
				throw new ArrayIndexOutOfBoundsException();
			}
			index--;
		}
		return result;
	}
	public Object get(int index) {
		return getElementAt(index);
	}
	public int indexOf(Object o) {
		int index = 0;
		Iterator it = delegate.iterator();
		if (o == null) {
			while (it.hasNext()) {
				if (o == it.next()) {
					return index;
				}
				index++;
			}
		}else {
			while (it.hasNext()) {
				if (o.equals(it.next())) {
					return index;
				}
				index++;
			}
		}
		return-1;
	}
	public int size() {
		return getSize();
	}
	public boolean isEmpty() {
		return delegate.isEmpty();
	}
	public boolean contains(Object elem) {
		return delegate.contains(elem);
	}
	public boolean add(Object obj) {
		boolean status = delegate.add(obj);
		int index = indexOf(obj);
		fireIntervalAdded(this,index,index);
		return status;
	}
	public boolean addAll(Collection c) {
		boolean status = delegate.addAll(c);
		fireContentsChanged(this,0,delegate.size() - 1);
		return status;
	}
	public boolean remove(Object obj) {
		int index = indexOf(obj);
		boolean rv = delegate.remove(obj);
		if (index >= 0) {
			fireIntervalRemoved(this,index,index);
		}
		return rv;
	}
	public boolean removeAll(Collection c) {
		boolean status = false;
		for (Object o:c) {
			status = status|remove(o);
		}
		return status;
	}
	@Override public String toString() {
		return delegate.toString();
	}
	public Object[]toArray() {
		return delegate.toArray();
	}
	public Object[]toArray(Object[]a) {
		return delegate.toArray(a);
	}
	public void clear() {
		int index1 = delegate.size() - 1;
		delegate.clear();
		if (index1 >= 0) {
			fireIntervalRemoved(this,0,index1);
		}
	}
	public boolean containsAll(Collection c) {
		return delegate.containsAll(c);
	}
	public Iterator iterator() {
		return delegate.iterator();
	}
	public boolean retainAll(Collection c) {
		int size = delegate.size();
		boolean status = delegate.retainAll(c);
		fireContentsChanged(this,0,size - 1);
		return status;
	}
}



