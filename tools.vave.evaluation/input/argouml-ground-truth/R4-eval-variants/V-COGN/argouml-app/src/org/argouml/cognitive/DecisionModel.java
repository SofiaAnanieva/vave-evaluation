package org.argouml.cognitive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class DecisionModel extends Observable implements Serializable {
	private List<Decision>decisions = new ArrayList<Decision>();
	public DecisionModel() {
		decisions.add(Decision.UNSPEC);
	}
	public List<Decision>getDecisionList() {
		return decisions;
	}
	public synchronized void setDecisionPriority(String decision,int priority) {
		Decision d = findDecision(decision);
		if (null == d) {
			d = new Decision(decision,priority);
			decisions.add(d);
			return;
		}
		d.setPriority(priority);
		setChanged();
		notifyObservers(decision);
	}
	public void defineDecision(String decision,int priority) {
		Decision d = findDecision(decision);
		if (d == null) {
			setDecisionPriority(decision,priority);
		}
	}
	public void startConsidering(Decision d) {
		decisions.remove(d);
		decisions.add(d);
	}
	public void stopConsidering(Decision d) {
		decisions.remove(d);
	}
	protected Decision findDecision(String decName) {
		for (Decision d:decisions) {
			if (decName.equals(d.getName())) {
				return d;
			}
		}
		return null;
	}
}



