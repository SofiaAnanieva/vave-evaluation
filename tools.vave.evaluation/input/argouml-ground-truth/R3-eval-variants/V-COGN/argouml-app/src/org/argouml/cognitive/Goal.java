package org.argouml.cognitive;

import org.argouml.cognitive.Translator;


public class Goal {
	private static final Goal UNSPEC = new Goal("label.goal.unspecified",1);
	private String name;
	private int priority;
	public Goal(String n,int p) {
		name = Translator.localize(n);
		priority = p;
	}
	public int hashCode() {
		if (name == null) {
			return 0;
		}
		return name.hashCode();
	}
	public boolean equals(Object d2) {
		if (!(d2 instanceof Goal)) {
			return false;
		}
		return((Goal) d2).getName().equals(getName());
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
	public String toString() {
		return getName();
	}
	public static Goal getUnspecifiedGoal() {
		return UNSPEC;
	}
}



