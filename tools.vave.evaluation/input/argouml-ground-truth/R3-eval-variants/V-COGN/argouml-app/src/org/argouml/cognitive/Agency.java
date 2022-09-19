package org.argouml.cognitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;


public class Agency extends Observable {
	private static Hashtable<Class,List<Critic>>criticRegistry = new Hashtable<Class,List<Critic>>(100);
	private static List<Critic>critics = new ArrayList<Critic>();
	private ControlMech controlMech;
	private static Hashtable<String,Critic>singletonCritics = new Hashtable<String,Critic>(40);
	public Agency(ControlMech cm) {
		controlMech = cm;
	}
	public Agency() {
		controlMech = new StandardCM();
	}
	public static Agency theAgency() {
		Designer dsgr = Designer.theDesigner();
		if (dsgr == null) {
			return null;
		}
		return dsgr.getAgency();
	}
	private static Hashtable<Class,List<Critic>>getCriticRegistry() {
		return criticRegistry;
	}
	public static List<Critic>getCriticList() {
		return critics;
	}
	protected static void addCritic(Critic cr) {
		if (critics.contains(cr)) {
			return;
		}
		if (!(cr instanceof CompoundCritic)) {
			critics.add(cr);
		}else {
			for (Critic c:((CompoundCritic) cr).getCriticList()) {
				addCritic(c);
			}
			return;
		}
	}
	public static void register(String crClassName,String dmClassName) {
		Class dmClass;
		try {
			dmClass = Class.forName(dmClassName);
		}catch (java.lang.ClassNotFoundException e) {
			return;
		}
		Critic cr = singletonCritics.get(crClassName);
		if (cr == null) {
			Class crClass;
			try {
				crClass = Class.forName(crClassName);
			}catch (java.lang.ClassNotFoundException e) {
				return;
			}
			try {
				cr = (Critic) crClass.newInstance();
			}catch (java.lang.IllegalAccessException e) {
				return;
			}catch (java.lang.InstantiationException e) {
				return;
			}
			singletonCritics.put(crClassName,cr);
			addCritic(cr);
		}
		register(cr,dmClass);
	}
	public static void register(Critic cr,Class clazz) {
		List<Critic>theCritics = getCriticRegistry().get(clazz);
		if (theCritics == null) {
			theCritics = new ArrayList<Critic>();
			criticRegistry.put(clazz,theCritics);
		}
		if (!theCritics.contains(cr)) {
			theCritics.add(cr);
			notifyStaticObservers(cr);
			cachedCritics.remove(clazz);
			addCritic(cr);
		}
	}
	public static void register(Critic cr,Object clazz) {
		register(cr,(Class) clazz);
	}
	public static void register(Critic cr) {
		Set<Object>metas = cr.getCriticizedDesignMaterials();
		for (Object meta:metas) {
			register(cr,meta);
		}
	}
	private static Hashtable<Class,Collection<Critic>>cachedCritics = new Hashtable<Class,Collection<Critic>>();
	public static Collection<Critic>criticsForClass(Class clazz) {
		Collection<Critic>col = cachedCritics.get(clazz);
		if (col == null) {
			col = new ArrayList<Critic>();
			col.addAll(criticListForSpecificClass(clazz));
			Collection<Class>classes = new ArrayList<Class>();
			if (clazz.getSuperclass() != null) {
				classes.add(clazz.getSuperclass());
			}
			if (clazz.getInterfaces() != null) {
				classes.addAll(Arrays.asList(clazz.getInterfaces()));
			}
			for (Class c:classes) {
				col.addAll(criticsForClass(c));
			}
			cachedCritics.put(clazz,col);
		}
		return col;
	}
	protected static List<Critic>criticListForSpecificClass(Class clazz) {
		List<Critic>theCritics = getCriticRegistry().get(clazz);
		if (theCritics == null) {
			theCritics = new ArrayList<Critic>();
			criticRegistry.put(clazz,theCritics);
		}
		return theCritics;
	}
	public static void applyAllCritics(Object dm,Designer d,long reasonCode) {
		Class dmClazz = dm.getClass();
		Collection<Critic>c = criticsForClass(dmClazz);
		applyCritics(dm,d,c,reasonCode);
	}
	public static void applyAllCritics(Object dm,Designer d) {
		Class dmClazz = dm.getClass();
		Collection<Critic>c = criticsForClass(dmClazz);
		applyCritics(dm,d,c,-1l);
	}
	public static void applyCritics(Object dm,Designer d,Collection<Critic>theCritics,long reasonCode) {
		for (Critic c:theCritics) {
			if (c.isActive()&&c.matchReason(reasonCode)) {
				try {
					c.critique(dm,d);
				}catch (Exception ex) {
					c.setEnabled(false);
				}
			}
		}
	}
	public void determineActiveCritics(Designer d) {
		for (Critic c:critics) {
			if (controlMech.isRelevant(c,d)) {
				c.beActive();
			}else {
				c.beInactive();
			}
		}
	}
	public static void addStaticObserver(Observer obs) {
		Agency a = theAgency();
		if (a == null) {
			return;
		}
		a.addObserver(obs);
	}
	public static void notifyStaticObservers(Object o) {
		if (theAgency() != null) {
			theAgency().setChanged();
			theAgency().notifyObservers(o);
		}
	}
}



