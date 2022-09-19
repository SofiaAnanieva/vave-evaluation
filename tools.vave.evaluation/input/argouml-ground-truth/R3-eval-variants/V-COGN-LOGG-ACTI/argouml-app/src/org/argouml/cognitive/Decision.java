package org.argouml.cognitive;

import org.argouml.cognitive.Translator;


public class Decision {
	public static final Decision UNSPEC = new Decision("misc.decision.uncategorized",1);
	private String name;
	private int priority;
	public Decision(String n,int p) {
		name = Translator.localize(n);
		priority = p;
	}
	@Override public int hashCode() {
		if (name == null) {
			return 0;
		}
		return name.hashCode();
	}
	@Override public boolean equals(Object d2) {
		if (!(d2 instanceof Decision)) {
			return false;
		}
		return((Decision) d2).getName().equals(getName());
	}
	public String getName() {
		return name;
	}
	public void setName(String n) {
		name = n;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int p) {
		priority = p;
	}
	@Override public String toString() {
		return getName();
	}
}



