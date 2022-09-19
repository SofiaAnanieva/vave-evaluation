package org.argouml.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.argouml.model.Model;


public class ItemUID {
	private static final Class MYCLASS = (new ItemUID()).getClass();
	private String id;
	public ItemUID() {
		id = generateID();
	}
	public ItemUID(String param) {
		id = param;
	}
	public String toString() {
		return id;
	}
	public static String generateID() {
		return(new java.rmi.server.UID()).toString();
	}
	public static String getIDOfObject(Object obj,boolean canCreate) {
		String s = readObjectID(obj);
		if (s == null&&canCreate) {
			s = createObjectID(obj);
		}
		return s;
	}
	protected static String readObjectID(Object obj) {
		if (Model.getFacade().isAUMLElement(obj)) {
			return Model.getFacade().getUUID(obj);
		}
		if (obj instanceof IItemUID) {
			final ItemUID itemUid = ((IItemUID) obj).getItemUID();
			return(itemUid == null?null:itemUid.toString());
		}
		Object rv;
		try {
			Method m = obj.getClass().getMethod("getItemUID",(Class[]) null);
			rv = m.invoke(obj,(Object[]) null);
		}catch (NoSuchMethodException nsme) {
			try {
				Method m = obj.getClass().getMethod("getUUID",(Class[]) null);
				rv = m.invoke(obj,(Object[]) null);
				return(String) rv;
			}catch (NoSuchMethodException nsme2) {
				return null;
			}catch (IllegalArgumentException iare) {
				return null;
			}catch (IllegalAccessException iace) {
				return null;
			}catch (InvocationTargetException tie) {
				return null;
			}
		}catch (SecurityException se) {
			return null;
		}catch (InvocationTargetException tie) {
			return null;
		}catch (IllegalAccessException iace) {
			return null;
		}catch (IllegalArgumentException iare) {
			return null;
		}catch (ExceptionInInitializerError eiie) {
			return null;
		}
		if (rv == null) {
			return null;
		}
		if (!(rv instanceof ItemUID)) {
			return null;
		}
		return rv.toString();
	}
	protected static String createObjectID(Object obj) {
		if (Model.getFacade().isAUMLElement(obj)) {
			return null;
		}
		if (obj instanceof IItemUID) {
			ItemUID uid = new ItemUID();
			((IItemUID) obj).setItemUID(uid);
			return uid.toString();
		}
		Class[]params = new Class[1];
		Object[]mparam;
		params[0] = MYCLASS;
		try {
			Method m = obj.getClass().getMethod("setItemUID",params);
			mparam = new Object[1];
			mparam[0] = new ItemUID();
			m.invoke(obj,mparam);
		}catch (NoSuchMethodException nsme) {
			return null;
		}catch (SecurityException se) {
			return null;
		}catch (InvocationTargetException tie) {
			return null;
		}catch (IllegalAccessException iace) {
			return null;
		}catch (IllegalArgumentException iare) {
			return null;
		}catch (ExceptionInInitializerError eiie) {
			return null;
		}
		return mparam[0].toString();
	}
}



