package org.argouml.cognitive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;


public class ListSet<T extends Object>implements Serializable,Set<T>,List<T> {
	private static final int TC_LIMIT = 50;
	private List<T>list;
	private Set<T>set;
	private final Object mutex = new Object();
	public ListSet() {
		list = Collections.synchronizedList(new ArrayList<T>());
		set = new HashSet<T>();
	}
	public ListSet(int n) {
		list = Collections.synchronizedList(new ArrayList<T>(n));
		set = new HashSet<T>(n);
	}
	public ListSet(T o1) {
		list = Collections.synchronizedList(new ArrayList<T>());
		set = new HashSet<T>();
		add(o1);
	}
	public void addAllElements(Enumeration<T>iter) {
		while (iter.hasMoreElements()) {
			add(iter.nextElement());
		}
	}
	public void addAllElements(Iterator<T>iter) {
		while (iter.hasNext()) {
			add(iter.next());
		}
	}
	public void addAllElementsSuchThat(Iterator<T>iter,org.argouml.util.Predicate p) {
		if (p instanceof org.argouml.util.PredicateTrue) {
			addAllElements(iter);
		}else {
			while (iter.hasNext()) {
				T e = iter.next();
				if (p.evaluate(e)) {
					add(e);
				}
			}
		}
	}
	public void addAllElementsSuchThat(ListSet<T>s,org.argouml.util.Predicate p) {
		synchronized (s.mutex()) {
			addAllElementsSuchThat(s.iterator(),p);
		}
	}
	public boolean remove(Object o) {
		synchronized (mutex) {
			boolean result = contains(o);
			if (o != null) {
				list.remove(o);
				set.remove(o);
			}
			return result;
		}
	}
	public void removeElement(Object o) {
		if (o != null) {
			list.remove(o);
		}
	}
	public void removeAllElements() {
		clear();
	}
	public boolean contains(Object o) {
		synchronized (mutex) {
			if (o != null) {
				return set.contains(o);
			}
		}
		return false;
	}
	public boolean containsSuchThat(org.argouml.util.Predicate p) {
		return findSuchThat(p) != null;
	}
	public Object findSuchThat(org.argouml.util.Predicate p) {
		synchronized (list) {
			for (Object o:list) {
				if (p.evaluate(o)) {
					return o;
				}
			}
		}
		return null;
	}
	@Override public int hashCode() {
		return 0;
	}
	@Override public boolean equals(Object o) {
		if (!(o instanceof ListSet)) {
			return false;
		}
		ListSet set = (ListSet) o;
		if (set.size() != size()) {
			return false;
		}
		synchronized (list) {
			for (Object obj:list) {
				if (!(set.contains(obj))) {
					return false;
				}
			}
		}
		return true;
	}
	public int size() {
		return list.size();
	}
	@Override public String toString() {
		StringBuilder sb = new StringBuilder("Set{");
		synchronized (list) {
			for (Iterator it = iterator();it.hasNext();) {
				sb.append(it.next());
				if (it.hasNext()) {
					sb.append(", ");
				}
			}
		}
		sb.append("}");
		return sb.toString();
	}
	public ListSet<T>transitiveClosure(org.argouml.util.ChildGenerator cg) {
		return transitiveClosure(cg,TC_LIMIT,org.argouml.util.PredicateTrue.getInstance());
	}
	public ListSet<T>reachable(org.argouml.util.ChildGenerator cg) {
		return reachable(cg,TC_LIMIT,org.argouml.util.PredicateTrue.getInstance());
	}
	public ListSet<T>reachable(org.argouml.util.ChildGenerator cg,int max,org.argouml.util.Predicate predicate) {
		ListSet<T>kids = new ListSet<T>();
		synchronized (list) {
			for (Object r:list) {
				kids.addAllElementsSuchThat(cg.childIterator(r),predicate);
			}
		}
		return kids.transitiveClosure(cg,max,predicate);
	}
	public ListSet<T>transitiveClosure(org.argouml.util.ChildGenerator cg,int max,org.argouml.util.Predicate predicate) {
		int iterCount = 0;
		int lastSize = -1;
		ListSet<T>touched = new ListSet<T>();
		ListSet<T>frontier;
		ListSet<T>recent = this;
		touched.addAll(this);
		while ((iterCount < max)&&(touched.size() > lastSize)) {
			iterCount++;
			lastSize = touched.size();
			frontier = new ListSet<T>();
			synchronized (recent) {
				for (T recentElement:recent) {
					Iterator frontierChildren = cg.childIterator(recentElement);
					frontier.addAllElementsSuchThat(frontierChildren,predicate);
				}
			}
			touched.addAll(frontier);
			recent = frontier;
		}
		return touched;
	}
	public boolean isEmpty() {
		return list.isEmpty();
	}
	public Iterator<T>iterator() {
		return list.iterator();
	}
	public Object mutex() {
		return list;
	}
	public Object[]toArray() {
		return list.toArray();
	}
	public<A>A[]toArray(A[]arg0) {
		return list.toArray(arg0);
	}
	public boolean add(T arg0) {
		synchronized (mutex) {
			boolean result = set.contains(arg0);
			if (!result) {
				set.add(arg0);
				list.add(arg0);
			}
			return!result;
		}
	}
	public boolean containsAll(Collection arg0) {
		synchronized (mutex) {
			return set.containsAll(arg0);
		}
	}
	public boolean addAll(Collection<?extends T>arg0) {
		return list.addAll(arg0);
	}
	public boolean retainAll(Collection<?>arg0) {
		return list.retainAll(arg0);
	}
	public boolean removeAll(Collection arg0) {
		boolean result = false;
		for (Iterator iter = arg0.iterator();iter.hasNext();) {
			result = result||remove(iter.next());
		}
		return result;
	}
	public void clear() {
		synchronized (mutex) {
			list.clear();
			set.clear();
		}
	}
	public boolean addAll(int arg0,Collection<?extends T>arg1) {
		return list.addAll(arg0,arg1);
	}
	public T get(int index) {
		return list.get(index);
	}
	public T set(int arg0,T o) {
		throw new UnsupportedOperationException("set() method not supported");
	}
	public void add(int arg0,T arg1) {
		synchronized (mutex) {
			if (!set.contains(arg1)) {
				list.add(arg0,arg1);
			}
		}
	}
	public T remove(int index) {
		synchronized (mutex) {
			T removedElement = list.remove(index);
			set.remove(removedElement);
			return removedElement;
		}
	}
	public int indexOf(Object o) {
		return list.indexOf(o);
	}
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}
	public ListIterator<T>listIterator() {
		return list.listIterator();
	}
	public ListIterator<T>listIterator(int index) {
		return list.listIterator(index);
	}
	public List<T>subList(int fromIndex,int toIndex) {
		return subList(fromIndex,toIndex);
	}
}



