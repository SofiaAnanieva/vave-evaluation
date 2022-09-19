package org.argouml.cognitive;

import java.util.ArrayList;
import java.util.List;


public class StandardCM extends AndCM {
	public StandardCM() {
		addMech(new EnabledCM());
		addMech(new NotSnoozedCM());
		addMech(new DesignGoalsCM());
		addMech(new CurDecisionCM());
	}
}

class EnabledCM implements ControlMech {
	public boolean isRelevant(Critic c,Designer d) {
		return c.isEnabled();
	}
}

class NotSnoozedCM implements ControlMech {
	public boolean isRelevant(Critic c,Designer d) {
		return!c.snoozeOrder().getSnoozed();
	}
}

class DesignGoalsCM implements ControlMech {
	public boolean isRelevant(Critic c,Designer d) {
		return c.isRelevantToGoals(d);
	}
}

class CurDecisionCM implements ControlMech {
	public boolean isRelevant(Critic c,Designer d) {
		return c.isRelevantToDecisions(d);
	}
}

abstract class CompositeCM implements ControlMech {
	private List<ControlMech>mechs = new ArrayList<ControlMech>();
	protected List<ControlMech>getMechList() {
		return mechs;
	}
	public void addMech(ControlMech cm) {
		mechs.add(cm);
	}
}

class AndCM extends CompositeCM {
	public boolean isRelevant(Critic c,Designer d) {
		for (ControlMech cm:getMechList()) {
			if (!cm.isRelevant(c,d)) {
				return false;
			}
		}
		return true;
	}
}

class OrCM extends CompositeCM {
	public boolean isRelevant(Critic c,Designer d) {
		for (ControlMech cm:getMechList()) {
			if (cm.isRelevant(c,d)) {
				return true;
			}
		}
		return false;
	}
}



