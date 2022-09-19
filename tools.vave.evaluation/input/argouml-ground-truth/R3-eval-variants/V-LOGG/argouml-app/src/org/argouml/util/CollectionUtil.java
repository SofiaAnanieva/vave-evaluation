package org.argouml.util;

import java.util.Collection;
import java.util.List;


public final class CollectionUtil {
	private CollectionUtil() {
	}
	public static Object getFirstItem(Collection c) {
		if (c instanceof List) {
			return((List) c).get(0);
		}
		return c.iterator().next();
	}
	public static Object getFirstItemOrNull(Collection c) {
		if (c.size() == 0) {
			return null;
		}
		return getFirstItem(c);
	}
}



