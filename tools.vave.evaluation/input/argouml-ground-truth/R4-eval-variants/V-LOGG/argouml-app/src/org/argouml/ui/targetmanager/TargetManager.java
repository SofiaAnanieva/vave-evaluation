package org.argouml.ui.targetmanager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.management.ListenerNotFoundException;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.swing.event.EventListenerList;
import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;


public final class TargetManager {
	private final class HistoryManager implements TargetListener {
	private static final int MAX_SIZE = 100;
	private List history = new ArrayList();
	private boolean navigateBackward;
	private int currentTarget = -1;
	private Remover umlListener = new HistoryRemover();
	private HistoryManager() {
		addTargetListener(this);
	}
	private void putInHistory(Object target) {
		if (currentTarget > -1) {
			Object theModelTarget = target instanceof Fig?((Fig) target).getOwner():target;
			Object oldTarget = ((WeakReference) history.get(currentTarget)).get();
			oldTarget = oldTarget instanceof Fig?((Fig) oldTarget).getOwner():oldTarget;
			if (oldTarget == theModelTarget) {
				return;
			}
		}
		if (target != null&&!navigateBackward) {
			if (currentTarget + 1 == history.size()) {
				umlListener.addListener(target);
				history.add(new WeakReference(target));
				currentTarget++;
				resize();
			}else {
				WeakReference ref = currentTarget > -1?(WeakReference) history.get(currentTarget):null;
				if (currentTarget == -1||!ref.get().equals(target)) {
					int size = history.size();
					for (int i = currentTarget + 1;i < size;i++) {
						umlListener.removeListener(history.remove(currentTarget + 1));
					}
					history.add(new WeakReference(target));
					umlListener.addListener(target);
					currentTarget++;
				}
			}
		}
	}
	private void resize() {
		int size = history.size();
		if (size > MAX_SIZE) {
			int oversize = size - MAX_SIZE;
			int halfsize = size / 2;
			if (currentTarget > halfsize&&oversize < halfsize) {
				for (int i = 0;i < oversize;i++) {
					umlListener.removeListener(history.remove(0));
				}
				currentTarget -= oversize;
			}
		}
	}
	private void navigateForward() {
		if (currentTarget >= history.size() - 1) {
			throw new IllegalStateException("NavigateForward is not allowed " + "since the targetpointer is pointing at " + "the upper boundary " + "of the history");
		}
		setTarget(((WeakReference) history.get(++currentTarget)).get());
	}
	private void navigateBackward() {
		if (currentTarget == 0) {
			throw new IllegalStateException("NavigateBackward is not allowed " + "since the targetpointer is pointing at " + "the lower boundary " + "of the history");
		}
		navigateBackward = true;
		if (targets.size() == 0) {
			setTarget(((WeakReference) history.get(currentTarget)).get());
		}else {
			setTarget(((WeakReference) history.get(--currentTarget)).get());
		}
		navigateBackward = false;
	}
	private boolean navigateBackPossible() {
		return currentTarget > 0;
	}
	private boolean navigateForwardPossible() {
		return currentTarget < history.size() - 1;
	}
	public void targetAdded(TargetEvent e) {
		Object[]addedTargets = e.getAddedTargets();
		for (int i = addedTargets. - 1;i >= 0;i--) {
			putInHistory(addedTargets[i]);
		}
	}
	public void targetRemoved(TargetEvent e) {
	}
	public void targetSet(TargetEvent e) {
		Object[]newTargets = e.getNewTargets();
		for (int i = newTargets. - 1;i >= 0;i--) {
			putInHistory(newTargets[i]);
		}
	}
	private void clean() {
		umlListener.removeAllListeners(history);
		history = new ArrayList();
		currentTarget = -1;
	}
	private void removeHistoryTarget(Object o) {
		if (o instanceof Diagram) {
			Iterator it = ((Diagram) o).getEdges().iterator();
			while (it.hasNext()) {
				removeHistoryTarget(it.next());
			}
			it = ((Diagram) o).getNodes().iterator();
			while (it.hasNext()) {
				removeHistoryTarget(it.next());
			}
		}
		ListIterator it = history.listIterator();
		while (it.hasNext()) {
			WeakReference ref = (WeakReference) it.next();
			Object historyObject = ref.get();
			if (Model.getFacade().isAModelElement(o)) {
				historyObject = historyObject instanceof Fig?((Fig) historyObject).getOwner():historyObject;
			}
			if (o == historyObject) {
				if (history.indexOf(ref) <= currentTarget) {
					currentTarget--;
				}
				it.remove();
			}
		}
	}
}
	private static final Logger LOG = Logger.getLogger(TargetManager.class);
	private static TargetManager instance = new TargetManager();
	private List targets = new ArrayList();
	private Object modelTarget;
	private Fig figTarget;
	private EventListenerList listenerList = new EventListenerList();
	private HistoryManager historyManager = new HistoryManager();
	private Remover umlListener = new TargetRemover();
	private boolean inTransaction = false;
	public static TargetManager getInstance() {
		return instance;
	}
	private TargetManager() {
	}
	public synchronized void setTarget(Object o) {
		if (isInTargetTransaction()) {
			return;
		}
		if ((targets.size() == 0&&o == null)||(targets.size() == 1&&targets.get(0).equals(o))) {
			return;
		}
		startTargetTransaction();
		Object[]oldTargets = targets.toArray();
		umlListener.removeAllListeners(targets);
		targets.clear();
		if (o != null) {
			Object newTarget;
			if (o instanceof Diagram) {
				newTarget = o;
			}else {
				newTarget = getOwner(o);
			}
			targets.add(newTarget);
			umlListener.addListener(newTarget);
		}
		internalOnSetTarget(TargetEvent.TARGET_SET,oldTargets);
		endTargetTransaction();
	}
	private void internalOnSetTarget(String eventName,Object[]oldTargets) {
		TargetEvent event = new TargetEvent(this,eventName,oldTargets,targets.toArray());
		if (targets.size() > 0) {
			figTarget = determineFigTarget(targets.get(0));
			modelTarget = determineModelTarget(targets.get(0));
		}else {
			figTarget = null;
			modelTarget = null;
		}
		if (TargetEvent.TARGET_SET.equals(eventName)) {
			fireTargetSet(event);
			return;
		}else if (TargetEvent.TARGET_ADDED.equals(eventName)) {
			fireTargetAdded(event);
			return;
		}else if (TargetEvent.TARGET_REMOVED.equals(eventName)) {
			fireTargetRemoved(event);
			return;
		}
		LOG.error("Unknown eventName: " + eventName);
	}
	public synchronized Object getTarget() {
		return targets.size() > 0?targets.get(0):null;
	}
	public synchronized void setTargets(Collection targetsCollection) {
		Iterator ntarg;
		if (isInTargetTransaction()) {
			return;
		}
		Collection targetsList = new ArrayList();
		if (targetsCollection != null) {
			targetsList.addAll(targetsCollection);
		}
		List modifiedList = new ArrayList();
		Iterator it = targetsList.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			o = getOwner(o);
			if ((o != null)&&!modifiedList.contains(o)) {
				modifiedList.add(o);
			}
		}
		targetsList = modifiedList;
		Object[]oldTargets = null;
		if (targetsList.size() == targets.size()) {
			boolean first = true;
			ntarg = targetsList.iterator();
			while (ntarg.hasNext()) {
				Object targ = ntarg.next();
				if (targ == null) {
					continue;
				}
				if (!targets.contains(targ)||(first&&targ != getTarget())) {
					oldTargets = targets.toArray();
					break;
				}
				first = false;
			}
		}else {
			oldTargets = targets.toArray();
		}
		if (oldTargets == null) {
			return;
		}
		startTargetTransaction();
		umlListener.removeAllListeners(targets);
		targets.clear();
		ntarg = targetsList.iterator();
		while (ntarg.hasNext()) {
			Object targ = ntarg.next();
			if (targets.contains(targ)) {
				continue;
			}
			targets.add(targ);
			umlListener.addListener(targ);
		}
		internalOnSetTarget(TargetEvent.TARGET_SET,oldTargets);
		endTargetTransaction();
	}
	public synchronized void addTarget(Object target) {
		if (target instanceof TargetListener) {
			LOG.warn("addTarget method received a TargetListener, " + "perhaps addTargetListener was intended! - " + target);
		}
		if (isInTargetTransaction()) {
			return;
		}
		Object newTarget = getOwner(target);
		if (target == null||targets.contains(target)||targets.contains(newTarget)) {
			return;
		}
		startTargetTransaction();
		Object[]oldTargets = targets.toArray();
		targets.add(0,newTarget);
		umlListener.addListener(newTarget);
		internalOnSetTarget(TargetEvent.TARGET_ADDED,oldTargets);
		endTargetTransaction();
	}
	public synchronized void removeTarget(Object target) {
		if (isInTargetTransaction()) {
			return;
		}
		if (target == null) {
			return;
		}
		startTargetTransaction();
		Object[]oldTargets = targets.toArray();
		Collection c = getOwnerAndAllFigs(target);
		targets.removeAll(c);
		umlListener.removeAllListeners(c);
		if (targets.size() != oldTargets.) {
			internalOnSetTarget(TargetEvent.TARGET_REMOVED,oldTargets);
		}
		endTargetTransaction();
	}
	private Collection getOwnerAndAllFigs(Object o) {
		Collection c = new ArrayList();
		c.add(o);
		if (o instanceof Fig) {
			if (((Fig) o).getOwner() != null) {
				o = ((Fig) o).getOwner();
				c.add(o);
			}
		}
		if (!(o instanceof Fig)) {
			Project p = ProjectManager.getManager().getCurrentProject();
			Collection col = p.findAllPresentationsFor(o);
			if (col != null&&!col.isEmpty()) {
				c.addAll(col);
			}
		}
		return c;
	}
	public Object getOwner(Object o) {
		if (o instanceof Fig) {
			if (((Fig) o).getOwner() != null) {
				o = ((Fig) o).getOwner();
			}
		}
		return o;
	}
	public synchronized List getTargets() {
		return Collections.unmodifiableList(targets);
	}
	public synchronized Object getSingleTarget() {
		return targets.size() == 1?targets.get(0):null;
	}
	public synchronized Collection getModelTargets() {
		ArrayList t = new ArrayList();
		Iterator iter = getTargets().iterator();
		while (iter.hasNext()) {
			t.add(determineModelTarget(iter.next()));
		}
		return t;
	}
	public synchronized Object getSingleModelTarget() {
		int i = 0;
		Iterator iter = getTargets().iterator();
		while (iter.hasNext()) {
			if (determineModelTarget(iter.next()) != null) {
				i++;
			}
			if (i > 1) {
				break;
			}
		}
		if (i == 1) {
			return modelTarget;
		}
		return null;
	}
	public void addTargetListener(TargetListener listener) {
		listenerList.add(TargetListener.class,listener);
	}
	public void removeTargetListener(TargetListener listener) {
		listenerList.remove(TargetListener.class,listener);
	}
	private void fireTargetSet(TargetEvent targetEvent) {
		Object[]listeners = listenerList.getListenerList();
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			try {
				if (listeners[i] == TargetListener.class) {
					((TargetListener) listeners[i + 1]).targetSet(targetEvent);
				}
			}catch (RuntimeException e) {
				LOG.error("While calling targetSet for " + targetEvent + " in " + listeners[i + 1] + " an error is thrown.",e);
				e.printStackTrace();
			}
		}
	}
	private void fireTargetAdded(TargetEvent targetEvent) {
		Object[]listeners = listenerList.getListenerList();
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			try {
				if (listeners[i] == TargetListener.class) {
					((TargetListener) listeners[i + 1]).targetAdded(targetEvent);
				}
			}catch (RuntimeException e) {
				LOG.error("While calling targetAdded for " + targetEvent + " in " + listeners[i + 1] + " an error is thrown.",e);
				e.printStackTrace();
			}
		}
	}
	private void fireTargetRemoved(TargetEvent targetEvent) {
		Object[]listeners = listenerList.getListenerList();
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			try {
				if (listeners[i] == TargetListener.class) {
					((TargetListener) listeners[i + 1]).targetRemoved(targetEvent);
				}
			}catch (RuntimeException e) {
				LOG.warn("While calling targetRemoved for " + targetEvent + " in " + listeners[i + 1] + " an error is thrown.",e);
			}
		}
	}
	private void startTargetTransaction() {
		inTransaction = true;
	}
	private boolean isInTargetTransaction() {
		return inTransaction;
	}
	private void endTargetTransaction() {
		inTransaction = false;
	}
	public Fig getFigTarget() {
		return figTarget;
	}
	private Fig determineFigTarget(Object target) {
		if (!(target instanceof Fig)) {
			Project p = ProjectManager.getManager().getCurrentProject();
			Collection col = p.findFigsForMember(target);
			if (col == null||col.isEmpty()) {
				target = null;
			}else {
				target = col.iterator().next();
			}
		}
		return target instanceof Fig?(Fig) target:null;
	}
	public Object getModelTarget() {
		return modelTarget;
	}
	private Object determineModelTarget(Object target) {
		if (target instanceof Fig) {
			Object owner = ((Fig) target).getOwner();
			if (Model.getFacade().isAUMLElement(owner)) {
				target = owner;
			}
		}
		return target instanceof Diagram||Model.getFacade().isAUMLElement(target)?target:null;
	}
	public void navigateForward()throws IllegalStateException {
		historyManager.navigateForward();
		LOG.debug("Navigate forward");
	}
	public void navigateBackward()throws IllegalStateException {
		historyManager.navigateBackward();
		LOG.debug("Navigate backward");
	}
	public boolean navigateForwardPossible() {
		return historyManager.navigateForwardPossible();
	}
	public boolean navigateBackPossible() {
		return historyManager.navigateBackPossible();
	}
	public void cleanHistory() {
		historyManager.clean();
	}
	public void removeHistoryElement(Object o) {
		historyManager.removeHistoryTarget(o);
	}
	private abstract class Remover implements PropertyChangeListener,NotificationListener {
	protected Remover() {
		ProjectManager.getManager().addPropertyChangeListener(this);
	}
	private void addListener(Object o) {
		if (Model.getFacade().isAModelElement(o)) {
			Model.getPump().addModelEventListener(this,o,"remove");
		}else if (o instanceof Diagram) {
			((Diagram) o).addPropertyChangeListener(this);
		}else if (o instanceof NotificationEmitter) {
			((NotificationEmitter) o).addNotificationListener(this,null,o);
		}
	}
	private void removeListener(Object o) {
		if (Model.getFacade().isAModelElement(o)) {
			Model.getPump().removeModelEventListener(this,o,"remove");
		}else if (o instanceof Diagram) {
			((Diagram) o).removePropertyChangeListener(this);
		}else if (o instanceof NotificationEmitter) {
			try {
				((NotificationEmitter) o).removeNotificationListener(this);
			}catch (ListenerNotFoundException e) {
				LOG.error("Notification Listener for " + "CommentEdge not found",e);
			}
		}
	}
	private void removeAllListeners(Collection c) {
		Iterator i = c.iterator();
		while (i.hasNext()) {
			removeListener(i.next());
		}
	}
	public void propertyChange(PropertyChangeEvent evt) {
		if ("remove".equals(evt.getPropertyName())) {
			remove(evt.getSource());
		}
	}
	public void handleNotification(Notification notification,Object handback) {
		if ("remove".equals(notification.getType())) {
			remove(notification.getSource());
		}
	}
	protected abstract void remove(Object obj);
}
	private class TargetRemover extends Remover {
	protected void remove(Object obj) {
		removeTarget(obj);
	}
}
	private class HistoryRemover extends Remover {
	protected void remove(Object obj) {
		historyManager.removeHistoryTarget(obj);
	}
}
}



