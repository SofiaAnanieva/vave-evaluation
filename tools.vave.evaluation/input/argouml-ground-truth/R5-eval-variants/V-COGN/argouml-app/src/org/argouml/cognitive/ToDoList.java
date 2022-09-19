package org.argouml.cognitive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import javax.swing.event.EventListenerList;
import org.argouml.i18n.Translator;
import org.argouml.model.InvalidElementException;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.UnresolvableException;
import org.argouml.cognitive.Poster;


public class ToDoList extends Observable implements Runnable {
	private static final int SLEEP_SECONDS = 3;
	private List<ToDoItem>items;
	private Set<ToDoItem>itemSet;
	private volatile ListSet allOffenders;
	private volatile ListSet<Poster>allPosters;
	private Set<ResolvedCritic>resolvedItems;
	private Thread validityChecker;
	private Designer designer;
	private EventListenerList listenerList;
	private static int longestToDoList;
	private static int numNotValid;
	private boolean isPaused;
	private Object pausedMutex = new Object();
	ToDoList() {
			items = Collections.synchronizedList(new ArrayList<ToDoItem>(100));
			itemSet = Collections.synchronizedSet(new HashSet<ToDoItem>(100));
			resolvedItems = Collections.synchronizedSet(new LinkedHashSet<ResolvedCritic>(100));
			listenerList = new EventListenerList();
			longestToDoList = 0;
			numNotValid = 0;
		}
	public synchronized void spawnValidityChecker(Designer d) {
		designer = d;
		validityChecker = new Thread(this,"Argo-ToDoValidityCheckingThread");
		validityChecker.setDaemon(true);
		validityChecker.setPriority(Thread.MIN_PRIORITY);
		setPaused(false);
		validityChecker.start();
	}
	public void run() {
		final List<ToDoItem>removes = new ArrayList<ToDoItem>();
		while (true) {
			synchronized (pausedMutex) {
				while (isPaused) {
					try {
						pausedMutex.wait();
					}catch (InterruptedException ignore) {
					}
				}
			}
			forceValidityCheck(removes);
			removes.clear();
			try {
				Thread.sleep(SLEEP_SECONDS * 1000);
			}catch (InterruptedException ignore) {
			}
		}
	}
	public void forceValidityCheck() {
		final List<ToDoItem>removes = new ArrayList<ToDoItem>();
		forceValidityCheck(removes);
	}
	@Deprecated protected synchronized void forceValidityCheck(final List<ToDoItem>removes) {
		synchronized (items) {
			for (ToDoItem item:items) {
				boolean valid;
				try {
					valid = item.stillValid(designer);
				}catch (InvalidElementException ex) {
					valid = false;
				}catch (Exception ex) {
					valid = false;
					StringBuffer buf = new StringBuffer("Exception raised in ToDo list cleaning");
					buf.append("\n");
					buf.append(item.toString());
				}
				if (!valid) {
					numNotValid++;
					removes.add(item);
				}
			}
		}
		for (ToDoItem item:removes) {
			removeE(item);
		}
		recomputeAllOffenders();
		recomputeAllPosters();
		fireToDoItemsRemoved(removes);
	}
	public void pause() {
		synchronized (pausedMutex) {
			isPaused = true;
		}
	}
	public void resume() {
		synchronized (pausedMutex) {
			isPaused = false;
			pausedMutex.notifyAll();
		}
	}
	public boolean isPaused() {
		synchronized (pausedMutex) {
			return isPaused;
		}
	}
	public void setPaused(boolean paused) {
		if (paused) {
			pause();
		}else {
			resume();
		}
	}
	public void notifyObservers(String action,Object arg) {
		setChanged();
		List<Object>l = new ArrayList<Object>(2);
		l.add(action);
		l.add(arg);
		super.notifyObservers(l);
	}
	public void notifyObservers(Object o) {
		setChanged();
		super.notifyObservers(o);
	}
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}
	public List<ToDoItem>getToDoItemList() {
		return items;
	}
	public Set<ResolvedCritic>getResolvedItems() {
		return resolvedItems;
	}
	public ListSet getOffenders() {
		ListSet all = allOffenders;
		if (all == null) {
			int size = items.size();
			all = new ListSet(size * 2);
			synchronized (items) {
				for (ToDoItem item:items) {
					all.addAll(item.getOffenders());
				}
			}
			allOffenders = all;
		}
		return all;
	}
	private void addOffenders(ListSet newoffs) {
		if (allOffenders != null) {
			allOffenders.addAll(newoffs);
		}
	}
	public ListSet<Poster>getPosters() {
		ListSet<Poster>all = allPosters;
		if (all == null) {
			all = new ListSet<Poster>();
			synchronized (items) {
				for (ToDoItem item:items) {
					all.add(item.getPoster());
				}
			}
			allPosters = all;
		}
		return all;
	}
	private void addPosters(Poster newp) {
		if (allPosters != null) {
			allPosters.add(newp);
		}
	}
	public static List<Decision>getDecisionList() {
		return new ArrayList<Decision>();
	}
	public static List<Goal>getGoalList() {
		return new ArrayList<Goal>();
	}
	private void addE(ToDoItem item) {
		if (itemSet.contains(item)) {
			return;
		}
		if (item.getPoster()instanceof Critic) {
			ResolvedCritic rc;
			try {
				rc = new ResolvedCritic((Critic) item.getPoster(),item.getOffenders(),false);
				Iterator<ResolvedCritic>elems = resolvedItems.iterator();
				while (elems.hasNext()) {
					if (elems.next().equals(rc)) {
						return;
					}
				}
			}catch (UnresolvableException ure) {
			}
		}
		items.add(item);
		itemSet.add(item);
		longestToDoList = Math.max(longestToDoList,items.size());
		addOffenders(item.getOffenders());
		addPosters(item.getPoster());
		notifyObservers("addElement",item);
		fireToDoItemAdded(item);
	}
	public void addElement(ToDoItem item) {
		addE(item);
	}
	public void removeAll(ToDoList list) {
		List<ToDoItem>itemList = list.getToDoItemList();
		synchronized (itemList) {
			for (ToDoItem item:itemList) {
				removeE(item);
			}
			recomputeAllOffenders();
			recomputeAllPosters();
			fireToDoItemsRemoved(itemList);
		}
	}
	private boolean removeE(ToDoItem item) {
		itemSet.remove(item);
		return items.remove(item);
	}
	public boolean removeElement(ToDoItem item) {
		boolean res = removeE(item);
		recomputeAllOffenders();
		recomputeAllPosters();
		fireToDoItemRemoved(item);
		notifyObservers("removeElement",item);
		return res;
	}
	public boolean resolve(ToDoItem item) {
		boolean res = removeE(item);
		fireToDoItemRemoved(item);
		return res;
	}
	public boolean explicitlyResolve(ToDoItem item,String reason)throws UnresolvableException {
		if (item.getPoster()instanceof Designer) {
			boolean res = resolve(item);
			return res;
		}
		if (!(item.getPoster()instanceof Critic)) {
			throw new UnresolvableException(Translator.localize("misc.todo-unresolvable",new Object[] {item.getPoster().getClass()}));
		}
		ResolvedCritic rc = new ResolvedCritic((Critic) item.getPoster(),item.getOffenders());
		boolean res = resolve(item);
		if (res) {
			res = addResolvedCritic(rc);
		}
		return res;
	}
	public boolean addResolvedCritic(ResolvedCritic rc) {
		return resolvedItems.add(rc);
	}
	public void removeAllElements() {
		List<ToDoItem>oldItems = new ArrayList<ToDoItem>(items);
		items.clear();
		itemSet.clear();
		recomputeAllOffenders();
		recomputeAllPosters();
		notifyObservers("removeAllElements");
		fireToDoItemsRemoved(oldItems);
	}
	public List<ToDoItem>elementListForOffender(Object offender) {
		List<ToDoItem>offenderItems = new ArrayList<ToDoItem>();
		synchronized (items) {
			for (ToDoItem item:items) {
				if (item.getOffenders().contains(offender)) {
					offenderItems.add(item);
				}
			}
		}
		return offenderItems;
	}
	public int size() {
		return items.size();
	}
	public ToDoItem get(int index) {
		return items.get(index);
	}
	@Deprecated protected void recomputeAllOffenders() {
		allOffenders = null;
	}
	@Deprecated protected void recomputeAllPosters() {
		allPosters = null;
	}
	public void addToDoListListener(ToDoListListener l) {
		listenerList.add(ToDoListListener.class,l);
	}
	public void removeToDoListListener(ToDoListListener l) {
		listenerList.remove(ToDoListListener.class,l);
	}
	@Deprecated protected void fireToDoListChanged() {
		Object[]listeners = listenerList.getListenerList();
		ToDoListEvent e = null;
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			if (listeners[i] == ToDoListListener.class) {
				if (e == null) {
					e = new ToDoListEvent();
				}
				((ToDoListListener) listeners[i + 1]).toDoListChanged(e);
			}
		}
	}
	@Deprecated protected void fireToDoItemChanged(ToDoItem item) {
		Object[]listeners = listenerList.getListenerList();
		ToDoListEvent e = null;
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			if (listeners[i] == ToDoListListener.class) {
				if (e == null) {
					List<ToDoItem>its = new ArrayList<ToDoItem>();
					its.add(item);
					e = new ToDoListEvent(its);
				}
				((ToDoListListener) listeners[i + 1]).toDoItemsChanged(e);
			}
		}
	}
	protected void fireToDoItemAdded(ToDoItem item) {
		List<ToDoItem>l = new ArrayList<ToDoItem>();
		l.add(item);
		fireToDoItemsAdded(l);
	}
	@Deprecated protected void fireToDoItemsAdded(List<ToDoItem>theItems) {
		if (theItems.size() > 0) {
			final Object[]listeners = listenerList.getListenerList();
			ToDoListEvent e = null;
			for (int i = listeners. - 2;i >= 0;i -= 2) {
				if (listeners[i] == ToDoListListener.class) {
					if (e == null) {
						e = new ToDoListEvent(theItems);
					}
					((ToDoListListener) listeners[i + 1]).toDoItemsAdded(e);
				}
			}
		}
	}
	@Deprecated protected void fireToDoItemRemoved(ToDoItem item) {
		List<ToDoItem>l = new ArrayList<ToDoItem>();
		l.add(item);
		fireToDoItemsRemoved(l);
	}
	@Deprecated protected void fireToDoItemsRemoved(final List<ToDoItem>theItems) {
		if (theItems.size() > 0) {
			final Object[]listeners = listenerList.getListenerList();
			ToDoListEvent e = null;
			for (int i = listeners. - 2;i >= 0;i -= 2) {
				if (listeners[i] == ToDoListListener.class) {
					if (e == null) {
						e = new ToDoListEvent(theItems);
					}
					((ToDoListListener) listeners[i + 1]).toDoItemsRemoved(e);
				}
			}
		}
	}
	@Override public String toString() {
		StringBuffer res = new StringBuffer(100);
		res.append(getClass().getName()).append(" {\n");
		List<ToDoItem>itemList = getToDoItemList();
		synchronized (itemList) {
			for (ToDoItem item:itemList) {
				res.append("    ").append(item.toString()).append("\n");
			}
		}
		res.append("  }");
		return res.toString();
	}
}



