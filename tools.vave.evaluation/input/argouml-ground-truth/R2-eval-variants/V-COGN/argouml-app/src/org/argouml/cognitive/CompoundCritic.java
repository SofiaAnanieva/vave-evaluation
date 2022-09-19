package org.argouml.cognitive;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import org.argouml.cognitive.ToDoItem;


public class CompoundCritic extends Critic {
	private List<Critic>critics = new ArrayList<Critic>();
	private Set<Object>extraDesignMaterials = new HashSet<Object>();
	public CompoundCritic() {
	}
	public CompoundCritic(Critic c1,Critic c2) {
		this();
		critics.add(c1);
		critics.add(c2);
	}
	public CompoundCritic(Critic c1,Critic c2,Critic c3) {
		this(c1,c2);
		critics.add(c3);
	}
	public CompoundCritic(Critic c1,Critic c2,Critic c3,Critic c4) {
		this(c1,c2,c3);
		critics.add(c4);
	}
	public void setCritics(List<Critic>c) {
		critics = c;
	}
	public List<Critic>getCriticList() {
		return critics;
	}
	public void addCritic(Critic c) {
		critics.add(c);
	}
	public void removeCritic(Critic c) {
		critics.remove(c);
	}
	@Override public void critique(Object dm,Designer dsgr) {
		for (Critic c:critics) {
			if (c.isActive()&&c.predicate(dm,dsgr)) {
				ToDoItem item = c.toDoItem(dm,dsgr);
				postItem(item,dm,dsgr);
				return;
			}
		}
	}
	@Override public boolean supports(Decision d) {
		for (Critic c:critics) {
			if (c.supports(d)) {
				return true;
			}
		}
		return false;
	}
	@Override public List<Decision>getSupportedDecisions() {
		throw new UnsupportedOperationException();
	}
	@Override public void addSupportedDecision(Decision d) {
		throw new UnsupportedOperationException();
	}
	@Override public boolean supports(Goal g) {
		for (Critic c:critics) {
			if (c.supports(g)) {
				return true;
			}
		}
		return false;
	}
	@Override public List<Goal>getSupportedGoals() {
		throw new UnsupportedOperationException();
	}
	@Override public void addSupportedGoal(Goal g) {
		throw new UnsupportedOperationException();
	}
	@Override public boolean containsKnowledgeType(String type) {
		for (Critic c:critics) {
			if (c.containsKnowledgeType(type)) {
				return true;
			}
		}
		return false;
	}
	@Override public void addKnowledgeType(String type) {
		throw new UnsupportedOperationException();
	}
	@Override public String expand(String desc,ListSet offs) {
		throw new UnsupportedOperationException();
	}
	@Override public Icon getClarifier() {
		throw new UnsupportedOperationException();
	}
	@Override public boolean isActive() {
		for (Critic c:critics) {
			if (c.isActive()) {
				return true;
			}
		}
		return false;
	}
	@Override public boolean isEnabled() {
		return true;
	}
	@Override public ToDoItem toDoItem(Object dm,Designer dsgr) {
		throw new UnsupportedOperationException();
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		for (Critic cr:this.critics) {
			ret.addAll(cr.getCriticizedDesignMaterials());
		}
		ret.addAll(extraDesignMaterials);
		return ret;
	}
	public void addExtraCriticizedDesignMaterial(Object dm) {
		this.extraDesignMaterials.add(dm);
	}
	public String toString() {
		return critics.toString();
	}
}



