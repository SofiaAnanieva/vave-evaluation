package org.argouml.model.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class CollectionHelper {
	static void update(Collection base,Collection updates) {
		if (updates == null) {
			base.clear();
			return;
		}
		Collection toBeRemoved = new ArrayList();
		Collection toBeAdded = new ArrayList();
		Iterator oldIt = base.iterator();
		while (oldIt.hasNext()) {
			Object obj = oldIt.next();
			if (!updates.contains(obj)) {
				toBeRemoved.add(obj);
			}
		}
		Iterator newIt = updates.iterator();
		while (newIt.hasNext()) {
			Object obj = newIt.next();
			if (!base.contains(obj)) {
				toBeAdded.add(obj);
			}
		}
		base.removeAll(toBeRemoved);
		base.addAll(toBeAdded);
	}
}



