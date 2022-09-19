package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.cognitive.Critic;
import org.argouml.model.Model;
import org.argouml.swingext.UpArrowIcon;
import org.argouml.ui.TabModelTarget;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.ui.PropPanelString;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;
import org.tigris.swidgets.Horizontal;
import org.tigris.swidgets.Orientable;
import org.tigris.swidgets.Orientation;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.PropPanelFactory;
import org.argouml.uml.ui.PropPanelFactoryManager;


public class TabProps extends AbstractArgoJPanel implements TabModelTarget {
	private JPanel blankPanel = new JPanel();
	private Hashtable<Class,TabModelTarget>panels = new Hashtable<Class,TabModelTarget>();
	private JPanel lastPanel;
	private String panelClassBaseName = "";
	private Object target;
	private EventListenerList listenerList = new EventListenerList();
	public TabProps() {
		this("tab.properties","ui.PropPanel");
	}
	public TabProps(String tabName,String panelClassBase) {
		super(tabName);
		setIcon(new UpArrowIcon());
		TargetManager.getInstance().addTargetListener(this);
		setOrientation(Horizontal.getInstance());
		panelClassBaseName = panelClassBase;
		setLayout(new BorderLayout());
	}
	@Override public void setOrientation(Orientation orientation) {
		super.setOrientation(orientation);
		Enumeration pps = panels.elements();
		while (pps.hasMoreElements()) {
			Object o = pps.nextElement();
			if (o instanceof Orientable) {
				Orientable orientable = (Orientable) o;
				orientable.setOrientation(orientation);
			}
		}
	}
	public void addPanel(Class clazz,PropPanel panel) {
		panels.put(clazz,panel);
	}
	@Deprecated public void setTarget(Object target) {
		target = (target instanceof Fig)?((Fig) target).getOwner():target;
		if (!(target == null||Model.getFacade().isAUMLElement(target)||target instanceof ArgoDiagram||target instanceof Critic)) {
			target = null;
		}
		if (lastPanel != null) {
			remove(lastPanel);
			if (lastPanel instanceof TargetListener) {
				removeTargetListener((TargetListener) lastPanel);
			}
		}
		this.target = target;
		if (target == null) {
			add(blankPanel,BorderLayout.CENTER);
			validate();
			repaint();
			lastPanel = blankPanel;
		}else {
			TabModelTarget newPanel = null;
			newPanel = findPanelFor(target);
			if (newPanel != null) {
				addTargetListener(newPanel);
			}
			if (newPanel instanceof JPanel) {
				add((JPanel) newPanel,BorderLayout.CENTER);
				lastPanel = (JPanel) newPanel;
			}else {
				add(blankPanel,BorderLayout.CENTER);
				validate();
				repaint();
				lastPanel = blankPanel;
			}
		}
	}
	public void refresh() {
		setTarget(TargetManager.getInstance().getTarget());
	}
	private TabModelTarget findPanelFor(Object trgt) {
		TabModelTarget panel = panels.get(trgt.getClass());
		if (panel != null) {
			return panel;
		}
		panel = createPropPanel(trgt);
		if (panel != null) {
			panels.put(trgt.getClass(),panel);
			return panel;
		}
		return null;
	}
	private TabModelTarget createPropPanel(Object targetObject) {
		TabModelTarget propPanel = null;
		for (PropPanelFactory factory:PropPanelFactoryManager.getFactories()) {
			propPanel = factory.createPropPanel(targetObject);
			if (propPanel != null)return propPanel;
		}
		if (targetObject instanceof FigText) {
			propPanel = new PropPanelString();
		}
		if (propPanel instanceof Orientable) {
			((Orientable) propPanel).setOrientation(getOrientation());
		}
		if (propPanel instanceof PropPanel) {
			((PropPanel) propPanel).setOrientation(getOrientation());
		}
		return propPanel;
	}
	protected String getClassBaseName() {
		return panelClassBaseName;
	}
	@Deprecated public Object getTarget() {
		return target;
	}
	public boolean shouldBeEnabled(Object target) {
		if (target instanceof Fig) {
			target = ((Fig) target).getOwner();
		}
		return((target instanceof Diagram||Model.getFacade().isAUMLElement(target))||target instanceof Critic&&findPanelFor(target) != null);
	}
	public void targetAdded(TargetEvent targetEvent) {
		setTarget(TargetManager.getInstance().getSingleTarget());
		fireTargetAdded(targetEvent);
		if (listenerList.getListenerCount() > 0) {
			validate();
			repaint();
		}
	}
	public void targetRemoved(TargetEvent targetEvent) {
		setTarget(TargetManager.getInstance().getSingleTarget());
		fireTargetRemoved(targetEvent);
		validate();
		repaint();
	}
	public void targetSet(TargetEvent targetEvent) {
		setTarget(TargetManager.getInstance().getSingleTarget());
		fireTargetSet(targetEvent);
		validate();
		repaint();
	}
	private void addTargetListener(TargetListener listener) {
		listenerList.add(TargetListener.class,listener);
	}
	private void removeTargetListener(TargetListener listener) {
		listenerList.remove(TargetListener.class,listener);
	}
	private void fireTargetSet(TargetEvent targetEvent) {
		Object[]listeners = listenerList.getListenerList();
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			if (listeners[i] == TargetListener.class) {
				((TargetListener) listeners[i + 1]).targetSet(targetEvent);
			}
		}
	}
	private void fireTargetAdded(TargetEvent targetEvent) {
		Object[]listeners = listenerList.getListenerList();
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			if (listeners[i] == TargetListener.class) {
				((TargetListener) listeners[i + 1]).targetAdded(targetEvent);
			}
		}
	}
	private void fireTargetRemoved(TargetEvent targetEvent) {
		Object[]listeners = listenerList.getListenerList();
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			if (listeners[i] == TargetListener.class) {
				((TargetListener) listeners[i + 1]).targetRemoved(targetEvent);
			}
		}
	}
}



