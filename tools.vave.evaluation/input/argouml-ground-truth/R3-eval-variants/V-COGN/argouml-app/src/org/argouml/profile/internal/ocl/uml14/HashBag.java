package org.argouml.profile.internal.ocl.uml14;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class HashBag<E>implements Bag<E> {
	private Map<E,Integer>map = new HashMap<E,Integer>();
	public HashBag() {
	}
	public HashBag(Collection col) {
		this();
		addAll(col);
	}
	public int count(Object element) {
		Integer c = map.get(element);
		return c == null?0:c;
	}
	public boolean add(E e) {
		if (e != null) {
			if (map.get(e) == null) {
				map.put(e,1);
			}else {
				map.put(e,map.get(e) + 1);
			}
		}
		return true;
	}
	@SuppressWarnings("unchecked")public boolean addAll(Collection c) {
		for (Object object:c) {
			add((E) object);
		}
		return true;
	}
	public void clear() {
		map.clear();
	}
	public boolean contains(Object o) {
		return map.containsKey(o);
	}
	public boolean containsAll(Collection c) {
		return map.keySet().containsAll(c);
	}
	public boolean isEmpty() {
		return map.isEmpty();
	}
	public Iterator<E>iterator() {
		return map.keySet().iterator();
	}
	public boolean remove(Object o) {
		return(map.remove(o) == null);
	}
	public boolean removeAll(Collection c) {
		boolean changed = false;
		for (Object object:c) {
			changed |= remove(object);
		}
		return changed;
	}
	public boolean retainAll(Collection c) {
		throw new UnsupportedOperationException();
	}
	public int size() {
		int sum = 0;
		for (E e:map.keySet()) {
			sum += count(e);
		}
		return sum;
	}
	public Object[]toArray() {
		return map.keySet().toArray();
	}
	public<T>T[]toArray(T[]a) {
		return map.keySet().toArray(a);
	}
	@Override public String toString() {
		return map.toString();
	}
	@Override public boolean equals(Object obj) {
		if (obj instanceof Bag) {
			Bag bag = (Bag) obj;
			for (Object object:bag) {
				if (count(object) != bag.count(object)) {
					return false;
				}
			}
			return true;
		}else {
			return false;
		}
	}
	@Override public int hashCode() {
		return map.hashCode() * 35;
	}
}



