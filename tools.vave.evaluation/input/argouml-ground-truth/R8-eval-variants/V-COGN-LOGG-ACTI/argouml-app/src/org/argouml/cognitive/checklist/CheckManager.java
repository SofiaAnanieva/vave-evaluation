package org.argouml.cognitive.checklist;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Enumeration;


public class CheckManager implements Serializable {
	private static Hashtable lists = new Hashtable();
	private static Hashtable statuses = new Hashtable();
	public CheckManager() {
	}
	public static Checklist getChecklistFor(Object dm) {
		Checklist cl;
		java.lang.
				Class cls = dm.getClass();
		while (cls != null) {
			cl = lookupChecklist(cls);
			if (cl != null) {
				return cl;
			}
			cls = cls.getSuperclass();
		}
		return null;
	}
	private static Checklist lookupChecklist(Class cls) {
		if (lists.contains(cls)) {
			return(Checklist) lists.get(cls);
		}
		Enumeration enumeration = lists.keys();
		while (enumeration.hasMoreElements()) {
			Object clazz = enumeration.nextElement();
			Class[]intfs = cls.getInterfaces();
			for (int i = 0;i < intfs.;i++) {
				if (intfs[i].equals(clazz)) {
					Checklist chlist = (Checklist) lists.get(clazz);
					lists.put(cls,chlist);
					return chlist;
				}
			}
		}
		return null;
	}
	public static void register(Object dm,Checklist cl) {
		lists.put(dm,cl);
	}
	public static ChecklistStatus getStatusFor(Object dm) {
		ChecklistStatus cls = (ChecklistStatus) statuses.get(dm);
		if (cls == null) {
			cls = new ChecklistStatus();
			statuses.put(dm,cls);
		}
		return cls;
	}
}



