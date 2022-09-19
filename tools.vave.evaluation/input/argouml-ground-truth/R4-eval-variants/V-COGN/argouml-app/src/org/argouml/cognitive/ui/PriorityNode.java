package org.argouml.cognitive.ui;

import java.util.ArrayList;
import java.util.List;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.Translator;


public class PriorityNode {
	private static final String HIGH = Translator.localize("misc.level.high");
	private static final String MEDIUM = Translator.localize("misc.level.medium");
	private static final String LOW = Translator.localize("misc.level.low");
	private static List<PriorityNode>priorities = null;
	private String name;
	private int priority;
	public PriorityNode(String n,int pri) {
		name = n;
		priority = pri;
	}
	public static List<PriorityNode>getPriorityList() {
		if (priorities == null) {
			priorities = new ArrayList<PriorityNode>();
			priorities.add(new PriorityNode(HIGH,ToDoItem.HIGH_PRIORITY));
			priorities.add(new PriorityNode(MEDIUM,ToDoItem.MED_PRIORITY));
			priorities.add(new PriorityNode(LOW,ToDoItem.LOW_PRIORITY));
		}
		return priorities;
	}
	public String getName() {
		return name;
	}
	public int getPriority() {
		return priority;
	}
	@Override public String toString() {
		return getName();
	}
}



