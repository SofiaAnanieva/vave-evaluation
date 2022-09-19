package org.argouml.cognitive;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import javax.swing.Action;
import javax.swing.Icon;
import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.model.InvalidElementException;
import org.tigris.gef.util.ChildGenerator;
import org.tigris.gef.util.EnumerationEmpty;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.Poster;


public final class Designer implements Poster,Runnable,PropertyChangeListener {
	private static Designer theDesignerSingleton = new Designer();
	private static boolean userWorking;
	private static List<Decision>unspecifiedDecision;
	private static List<Goal>unspecifiedGoal;
	private static Action saveAction;
	static {
	unspecifiedDecision = new ArrayList<Decision>();
	unspecifiedDecision.add(Decision.UNSPEC);
	unspecifiedGoal = new ArrayList<Goal>();
	unspecifiedGoal.add(Goal.getUnspecifiedGoal());
}
	public static final ConfigurationKey AUTO_CRITIQUE = Configuration.makeKey("cognitive","autocritique");
	private ToDoList toDoList;
	private Properties prefs;
	private String designerName;
	private DecisionModel decisions;
	private GoalModel goals;
	private Agency agency;
	private Icon clarifier;
	private Thread critiquerThread;
	private int critiquingInterval;
	private int critiqueCPUPercent;
	private List<Object>hotQueue;
	private List<Long>hotReasonQueue;
	private List<Object>addQueue;
	private List<Long>addReasonQueue;
	private List<Object>removeQueue;
	private static int longestAdd;
	private static int longestHot;
	private List<Object>warmQueue;
	private ChildGenerator childGenerator;
	private static Object critiquingRoot;
	private long critiqueDuration;
	private int critiqueLock;
	private static PropertyChangeSupport pcs;
	public static final String MODEL_TODOITEM_ADDED = "MODEL_TODOITEM_ADDED";
	public static final String MODEL_TODOITEM_DISMISSED = "MODEL_TODOITEM_DISMISSED";
	private Designer() {
		decisions = new DecisionModel();
		goals = new GoalModel();
		agency = new Agency();
		prefs = new Properties();
		toDoList = new ToDoList();
		toDoList.spawnValidityChecker(this);
		userWorking = false;
		critiquingInterval = 8000;
		critiqueCPUPercent = 10;
		hotQueue = new ArrayList<Object>();
		hotReasonQueue = new ArrayList<Long>();
		addQueue = new ArrayList<Object>();
		addReasonQueue = new ArrayList<Long>();
		removeQueue = new ArrayList<Object>();
		longestAdd = 0;
		longestHot = 0;
		warmQueue = new ArrayList<Object>();
		childGenerator = new EmptyChildGenerator();
		critiqueLock = 0;
	}
	public static Designer theDesigner() {
		return theDesignerSingleton;
	}
	public void spawnCritiquer(Object root) {
		critiquerThread = new Thread(this,"CritiquingThread");
		critiquerThread.setDaemon(true);
		critiquerThread.setPriority(Thread.currentThread().getPriority() - 1);
		critiquerThread.start();
		critiquingRoot = root;
	}
	public void run() {
		try {
			while (true) {
				long critiqueStartTime;
				long cutoffTime;
				int minWarmElements = 5;
				int size;
				synchronized (this) {
					while (!Configuration.getBoolean(Designer.AUTO_CRITIQUE,true)) {
						try {
							this.wait();
						}catch (InterruptedException ignore) {
						}
					}
				}
				if (critiquingRoot != null&&critiqueLock <= 0) {
					synchronized (this) {
						critiqueStartTime = System.currentTimeMillis();
						cutoffTime = critiqueStartTime + 3000;
						size = addQueue.size();
						for (int i = 0;i < size;i++) {
							hotQueue.add(addQueue.get(i));
							hotReasonQueue.add(addReasonQueue.get(i));
						}
						addQueue.clear();
						addReasonQueue.clear();
						longestHot = Math.max(longestHot,hotQueue.size());
						agency.determineActiveCritics(this);
						while (hotQueue.size() > 0) {
							Object dm = hotQueue.get(0);
							Long reasonCode = hotReasonQueue.get(0);
							hotQueue.remove(0);
							hotReasonQueue.remove(0);
							Agency.applyAllCritics(dm,theDesigner(),reasonCode.longValue());
						}
						size = removeQueue.size();
						for (int i = 0;i < size;i++) {
							warmQueue.remove(removeQueue.get(i));
						}
						removeQueue.clear();
						if (warmQueue.size() == 0) {
							warmQueue.add(critiquingRoot);
						}
						while (warmQueue.size() > 0&&(System.currentTimeMillis() < cutoffTime||minWarmElements > 0)) {
							if (minWarmElements > 0) {
								minWarmElements--;
							}
							Object dm = warmQueue.get(0);
							warmQueue.remove(0);
							try {
								Agency.applyAllCritics(dm,theDesigner());
								java.util.
									Enumeration subDMs = childGenerator.gen(dm);
								while (subDMs.hasMoreElements()) {
									Object nextDM = subDMs.nextElement();
									if (!(warmQueue.contains(nextDM))) {
										warmQueue.add(nextDM);
									}
								}
							}catch (InvalidElementException e) {
							}
						}
					}
				}else {
					critiqueStartTime = System.currentTimeMillis();
				}
				critiqueDuration = System.currentTimeMillis() - critiqueStartTime;
				long cycleDuration = (critiqueDuration * 100) / critiqueCPUPercent;
				long sleepDuration = Math.min(cycleDuration - critiqueDuration,3000);
				sleepDuration = Math.max(sleepDuration,1000);
				try {
					Thread.sleep(sleepDuration);
				}catch (InterruptedException ignore) {
				}
			}
		}catch (Exception e) {
		}
	}
	public synchronized void critiqueASAP(Object dm,String reason) {
		long rCode = Critic.reasonCodeFor(reason);
		if (!userWorking) {
			return;
		}
		if ("remove".equals(reason)) {
			return;
		}
		int addQueueIndex = addQueue.indexOf(dm);
		if (addQueueIndex == -1) {
			addQueue.add(dm);
			Long reasonCodeObj = new Long(rCode);
			addReasonQueue.add(reasonCodeObj);
		}else {
			Long reasonCodeObj = addReasonQueue.get(addQueueIndex);
			long rc = reasonCodeObj.longValue()|rCode;
			Long newReasonCodeObj = new Long(rc);
			addReasonQueue.set(addQueueIndex,newReasonCodeObj);
		}
		removeQueue.add(dm);
		longestAdd = Math.max(longestAdd,addQueue.size());
	}
	public void critique(Object des) {
		Agency.applyAllCritics(des,this);
	}
	public static void addListener(PropertyChangeListener pcl) {
		if (pcs == null) {
			pcs = new PropertyChangeSupport(theDesigner());
		}
		pcs.addPropertyChangeListener(pcl);
	}
	public static void removeListener(PropertyChangeListener p) {
		if (pcs != null) {
			pcs.removePropertyChangeListener(p);
		}
	}
	public static void setSaveAction(Action theSaveAction) {
		saveAction = theSaveAction;
	}
	public static void firePropertyChange(String property,Object oldValue,Object newValue) {
		if (pcs != null) {
			pcs.firePropertyChange(property,oldValue,newValue);
		}
		if (MODEL_TODOITEM_ADDED.equals(property)||MODEL_TODOITEM_DISMISSED.equals(property)) {
			if (saveAction != null) {
				saveAction.setEnabled(true);
			}
		}
	}
	public void propertyChange(PropertyChangeEvent pce) {
		if (pce.getPropertyName().equals(Argo.KEY_USER_FULLNAME.getKey())) {
			designerName = pce.getNewValue().toString();
		}else {
			critiqueASAP(pce.getSource(),pce.getPropertyName());
		}
	}
	public void determineActiveCritics() {
		agency.determineActiveCritics(this);
	}
	public boolean getAutoCritique() {
		return Configuration.getBoolean(Designer.AUTO_CRITIQUE,true);
	}
	public void setAutoCritique(boolean b) {
		Configuration.setBoolean(Designer.AUTO_CRITIQUE,b);
		synchronized (this) {
			if (b) {
				this.notifyAll();
			}
		}
	}
	public int getCritiquingInterval() {
		return critiquingInterval;
	}
	public void setCritiquingInterval(int i) {
		critiquingInterval = i;
	}
	public static void disableCritiquing() {
		synchronized (theDesigner()) {
			theDesigner().critiqueLock++;
		}
	}
	public static void enableCritiquing() {
		synchronized (theDesigner()) {
			theDesigner().critiqueLock--;
		}
	}
	public static void clearCritiquing() {
		synchronized (theDesigner()) {
			theDesigner().toDoList.removeAllElements();
			theDesigner().hotQueue.clear();
			theDesigner().hotReasonQueue.clear();
			theDesigner().addQueue.clear();
			theDesigner().addReasonQueue.clear();
			theDesigner().removeQueue.clear();
			theDesigner().warmQueue.clear();
		}
	}
	public static void setCritiquingRoot(Object d) {
		synchronized (theDesigner()) {
			critiquingRoot = d;
		}
	}
	public static Object getCritiquingRoot() {
		synchronized (theDesigner()) {
			return critiquingRoot;
		}
	}
	public ChildGenerator getChildGenerator() {
		return childGenerator;
	}
	public void setChildGenerator(ChildGenerator cg) {
		childGenerator = cg;
	}
	public DecisionModel getDecisionModel() {
		return decisions;
	}
	public GoalModel getGoalModel() {
		return goals;
	}
	public List<Goal>getGoalList() {
		return goals.getGoalList();
	}
	public boolean stillValid(ToDoItem i,Designer d) {
		return true;
	}
	public boolean supports(Decision d) {
		return d == Decision.UNSPEC;
	}
	public List<Decision>getSupportedDecisions() {
		return unspecifiedDecision;
	}
	public boolean supports(Goal g) {
		return true;
	}
	public List<Goal>getSupportedGoals() {
		return unspecifiedGoal;
	}
	public boolean containsKnowledgeType(String type) {
		return type.equals("Designer\'s");
	}
	public String expand(String desc,ListSet offs) {
		return desc;
	}
	public Icon getClarifier() {
		return clarifier;
	}
	public void setClarifier(Icon clar) {
		clarifier = clar;
	}
	public ToDoList getToDoList() {
		return toDoList;
	}
	public void removeToDoItems(ToDoList list) {
		toDoList.removeAll(list);
	}
	public Properties getPrefs() {
		return prefs;
	}
	public boolean isConsidering(Decision d) {
		return d.getPriority() > 0;
	}
	public void setDecisionPriority(String decision,int priority) {
		decisions.setDecisionPriority(decision,priority);
	}
	public boolean hasGoal(String goal) {
		return goals.hasGoal(goal);
	}
	public void setGoalPriority(String goal,int priority) {
		goals.setGoalPriority(goal,priority);
	}
	public void startDesiring(String goal) {
		goals.startDesiring(goal);
	}
	public void stopDesiring(String goal) {
		goals.stopDesiring(goal);
	}
	public void snooze() {
	}
	public void unsnooze() {
	}
	public Agency getAgency() {
		return agency;
	}
	public void inform(ToDoItem item) {
		toDoList.addElement(item);
	}
	public void setDesignerName(String name) {
		designerName = name;
	}
	public String getDesignerName() {
		return designerName;
	}
	@Override public String toString() {
		return getDesignerName();
	}
	public void fixIt(ToDoItem item,Object arg) {
	}
	public boolean canFixIt(ToDoItem item) {
		return false;
	}
	public static void setUserWorking(boolean working) {
		userWorking = working;
	}
	public static boolean isUserWorking() {
		return userWorking;
	}
	static class EmptyChildGenerator implements ChildGenerator {
	public Enumeration gen(Object o) {
		return EnumerationEmpty.theInstance();
	}
	private static final long serialVersionUID = 7599621170029351645l;
}
	private static final long serialVersionUID = -3647853023882216454l;
}



