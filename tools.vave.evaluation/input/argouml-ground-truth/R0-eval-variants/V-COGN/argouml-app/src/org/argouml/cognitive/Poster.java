package org.argouml.cognitive;

import java.util.List;
import javax.swing.Icon;
import org.argouml.cognitive.ToDoItem;


public interface Poster {
	boolean stillValid(ToDoItem i,Designer d);
	boolean supports(Decision d);
	List<Decision>getSupportedDecisions();
	boolean supports(Goal g);
	List<Goal>getSupportedGoals();
	boolean containsKnowledgeType(String knowledgeType);
	String expand(String desc,ListSet offs);
	Icon getClarifier();
	void snooze();
	void unsnooze();
	void fixIt(ToDoItem item,Object arg);
	boolean canFixIt(ToDoItem item);
}



