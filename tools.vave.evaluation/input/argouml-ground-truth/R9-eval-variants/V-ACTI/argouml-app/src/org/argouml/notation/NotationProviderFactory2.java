package org.argouml.notation;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public final class NotationProviderFactory2 {
	private static String currentLanguage;
	public static final int TYPE_NAME = 1;
	public static final int TYPE_TRANSITION = 2;
	public static final int TYPE_STATEBODY = 3;
	public static final int TYPE_ACTIONSTATE = 4;
	public static final int TYPE_ATTRIBUTE = 5;
	public static final int TYPE_OPERATION = 6;
	public static final int TYPE_OBJECT = 7;
	public static final int TYPE_COMPONENTINSTANCE = 8;
	public static final int TYPE_NODEINSTANCE = 9;
	public static final int TYPE_OBJECTFLOWSTATE_TYPE = 10;
	public static final int TYPE_OBJECTFLOWSTATE_STATE = 11;
	public static final int TYPE_CALLSTATE = 12;
	public static final int TYPE_CLASSIFIERROLE = 13;
	public static final int TYPE_MESSAGE = 14;
	public static final int TYPE_EXTENSION_POINT = 15;
	public static final int TYPE_ASSOCIATION_END_NAME = 16;
	public static final int TYPE_ASSOCIATION_ROLE = 17;
	public static final int TYPE_ASSOCIATION_NAME = 18;
	public static final int TYPE_MULTIPLICITY = 19;
	public static final int TYPE_ENUMERATION_LITERAL = 20;
	public static final int TYPE_SD_MESSAGE = 21;
	private NotationName defaultLanguage;
	private Map<NotationName,Map<Integer,Class>>allLanguages;
	private static NotationProviderFactory2 instance;
	private NotationProviderFactory2() {
		super();
		allLanguages = new HashMap<NotationName,Map<Integer,Class>>();
	}
	public static NotationProviderFactory2 getInstance() {
		if (instance == null) {
			instance = new NotationProviderFactory2();
		}
		return instance;
	}
	@Deprecated public NotationProvider getNotationProvider(int type,Object object) {
		NotationName name = Notation.findNotation(currentLanguage);
		return getNotationProvider(type,object,name);
	}
	public NotationProvider getNotationProvider(int type,Object object,NotationName name) {
		Class clazz = getNotationProviderClass(type,name);
		if (clazz != null) {
			try {
				try {
					Class[]mp =  {};
					Method m = clazz.getMethod("getInstance",mp);
					return(NotationProvider) m.invoke(null,(Object[]) mp);
				}catch (Exception e) {
					Class[]cp =  {Object.class};
					Constructor constructor = clazz.getConstructor(cp);
					Object[]params =  {object};
					return(NotationProvider) constructor.newInstance(params);
				}
			}catch (SecurityException e) {
			}catch (NoSuchMethodException e) {
			}catch (IllegalArgumentException e) {
			}catch (InstantiationException e) {
			}catch (IllegalAccessException e) {
			}catch (InvocationTargetException e) {
			}
		}
		return null;
	}
	@Deprecated public NotationProvider getNotationProvider(int type,Object object,PropertyChangeListener listener) {
		NotationName name = Notation.findNotation(currentLanguage);
		return getNotationProvider(type,object,listener,name);
	}
	public NotationProvider getNotationProvider(int type,Object object,PropertyChangeListener listener,NotationName name) {
		NotationProvider p = getNotationProvider(type,object,name);
		p.initialiseListener(listener,object);
		return p;
	}
	private Class getNotationProviderClass(int type,NotationName name) {
		if (allLanguages.containsKey(name)) {
			Map<Integer,Class>t = allLanguages.get(name);
			if (t.containsKey(Integer.valueOf(type))) {
				return t.get(Integer.valueOf(type));
			}
		}
		Map<Integer,Class>t = allLanguages.get(defaultLanguage);
		if (t != null&&t.containsKey(Integer.valueOf(type))) {
			return t.get(Integer.valueOf(type));
		}
		return null;
	}
	public void addNotationProvider(int type,NotationName notationName,Class provider) {
		if (allLanguages.containsKey(notationName)) {
			Map<Integer,Class>t = allLanguages.get(notationName);
			t.put(Integer.valueOf(type),provider);
		}else {
			Map<Integer,Class>t = new HashMap<Integer,Class>();
			t.put(Integer.valueOf(type),provider);
			allLanguages.put(notationName,t);
		}
	}
	public void setDefaultNotation(NotationName notationName) {
		if (allLanguages.containsKey(notationName)) {
			defaultLanguage = notationName;
		}
	}
	public boolean removeNotation(NotationName notationName) {
		if (defaultLanguage == notationName) {
			return false;
		}
		if (allLanguages.containsKey(notationName)) {
			return allLanguages.remove(notationName) != null&&Notation.removeNotation(notationName);
		}
		return false;
	}
	@Deprecated public static void setCurrentLanguage(String theCurrentLanguage) {
		NotationProviderFactory2.currentLanguage = theCurrentLanguage;
	}
}



