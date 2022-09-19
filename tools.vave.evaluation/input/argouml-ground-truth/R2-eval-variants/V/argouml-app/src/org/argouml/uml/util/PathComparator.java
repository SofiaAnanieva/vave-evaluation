package org.argouml.uml.util;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.argouml.model.Model;


public class PathComparator implements Comparator {
	private Collator collator;
	public PathComparator() {
		collator = Collator.getInstance();
		collator.setStrength(Collator.PRIMARY);
	}
	public int compare(Object o1,Object o2) {
		if (o1 == null) {
			if (o2 == null) {
				return 0;
			}
			return-1;
		}
		if (o2 == null) {
			return 1;
		}
		if (o1.equals(o2)) {
			return 0;
		}
		if (o1 instanceof String) {
			if (o2 instanceof String) {
				return collator.compare((String) o1,(String) o2);
			}else if (Model.getFacade().isAUMLElement(o2)) {
				return-1;
			}
		}
		if (o2 instanceof String&&Model.getFacade().isAUMLElement(o1)) {
			return 1;
		}
		String name1,name2;
		try {
			name1 = Model.getFacade().getName(o1);
			name2 = Model.getFacade().getName(o2);
		}catch (IllegalArgumentException e) {
			throw new ClassCastException("Model element or String required" + "\n - o1 = " + ((o1 == null)?"(null)":o1.toString()) + "\n - o2 = " + ((o2 == null)?"(null)":o2.toString()));
		}
		if (name1 != null&&name2 != null) {
			int comparison = collator.compare(name1,name2);
			if (comparison != 0) {
				return comparison;
			}
		}
		return comparePaths(o1,o2);
	}
	private int comparePaths(Object o1,Object o2) {
		List<String>path1 = Model.getModelManagementHelper().getPathList(o1);
		Collections.reverse(path1);
		List<String>path2 = Model.getModelManagementHelper().getPathList(o2);
		Collections.reverse(path2);
		Iterator<String>i2 = path2.iterator();
		Iterator<String>i1 = path1.iterator();
		int caseSensitiveComparison = 0;
		while (i2.hasNext()) {
			String name2 = i2.next();
			if (!i1.hasNext()) {
				return-1;
			}
			String name1 = i1.next();
			int comparison;
			if (name1 == null) {
				if (name2 == null) {
					comparison = 0;
				}else {
					comparison = -1;
				}
			}else if (name2 == null) {
				comparison = 1;
			}else {
				comparison = collator.compare(name1,name2);
			}
			if (comparison != 0) {
				return comparison;
			}
			if (caseSensitiveComparison == 0) {
				if (name1 == null) {
					if (name2 == null) {
						caseSensitiveComparison = 0;
					}else {
						caseSensitiveComparison = -1;
					}
				}else if (name2 == null) {
					caseSensitiveComparison = 1;
				}else {
					caseSensitiveComparison = name1.compareTo(name2);
				}
			}
		}
		if (i2.hasNext()) {
			return 1;
		}
		if (caseSensitiveComparison != 0) {
			return caseSensitiveComparison;
		}
		return o1.toString().compareTo(o2.toString());
	}
}



