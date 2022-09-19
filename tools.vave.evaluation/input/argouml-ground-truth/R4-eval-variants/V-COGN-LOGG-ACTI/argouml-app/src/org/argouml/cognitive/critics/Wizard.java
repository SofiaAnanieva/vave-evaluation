package org.argouml.cognitive.critics;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;


public abstract class Wizard implements java.io.Serializable {
	private List<JPanel>panels = new ArrayList<JPanel>();
	private int step = 0;
	private boolean finished = false;
	private boolean started = false;
	private WizardItem item = null;
	public Wizard() {
	}
	protected void removePanel(int s) {
		panels.remove(s);
	}
	public void setToDoItem(WizardItem i) {
		item = i;
	}
	public WizardItem getToDoItem() {
		return item;
	}
	public int getProgress() {
		return step * 100 / getNumSteps();
	}
	public abstract int getNumSteps();
	public JPanel getCurrentPanel() {
		return getPanel(step);
	}
	public JPanel getPanel(int s) {
		if (s > 0&&s <= panels.size()) {
			return panels.get(s - 1);
		}
		return null;
	}
	public boolean canGoNext() {
		return step < getNumSteps();
	}
	public void next() {
		doAction(step);
		step++;
		JPanel p = makePanel(step);
		if (p != null) {
			panels.add(p);
		}
		started = true;
		if (item != null) {
			item.changed();
		}
	}
	public boolean canGoBack() {
		return step > 0;
	}
	public void back() {
		step--;
		if (step < 0)step = 0;
		undoAction(step);
		if (item != null)item.changed();
	}
	public boolean canFinish() {
		return true;
	}
	public boolean isStarted() {
		return started;
	}
	public boolean isFinished() {
		return finished;
	}
	public void finish() {
		started = true;
		int numSteps = getNumSteps();
		for (int i = step;i <= numSteps;i++) {
			doAction(i);
			if (item != null)item.changed();
		}
		finished = true;
	}
	public abstract JPanel makePanel(int newStep);
	public abstract void doAction(int oldStep);
	public void doAction() {
		doAction(step);
	}
	public void undoAction(int oldStep) {
	}
	public void undoAction() {
		undoAction(step);
	}
	protected int getStep() {
		return step;
	}
}



