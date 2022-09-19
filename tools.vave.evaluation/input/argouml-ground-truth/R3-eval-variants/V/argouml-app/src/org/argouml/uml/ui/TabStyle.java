package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Hashtable;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.swingext.UpArrowIcon;
import org.argouml.ui.StylePanel;
import org.argouml.ui.TabFigTarget;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.argouml.uml.diagram.ui.FigAssociationClass;
import org.argouml.uml.diagram.ui.FigClassAssociationClass;
import org.argouml.uml.util.namespace.Namespace;
import org.argouml.uml.util.namespace.StringNamespace;
import org.argouml.uml.util.namespace.StringNamespaceElement;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;


public class TabStyle extends AbstractArgoJPanel implements TabFigTarget,PropertyChangeListener,DelayedVChangeListener {
	private Fig target;
	private boolean shouldBeEnabled = false;
	private JPanel blankPanel = new JPanel();
	private Hashtable<Class,TabFigTarget>panels = new Hashtable<Class,TabFigTarget>();
	private JPanel lastPanel = null;
	private StylePanel stylePanel = null;
	private String[]stylePanelNames;
	private EventListenerList listenerList = new EventListenerList();
	public TabStyle(String tabName,String[]spn) {
		super(tabName);
		this.stylePanelNames = spn;
		setIcon(new UpArrowIcon());
		setLayout(new BorderLayout());
	}
	public TabStyle() {
		this("tab.style",new String[] {"StylePanel","SP"});
	}
	public void addPanel(Class c,StylePanel s) {
		panels.put(c,s);
	}
	public void setTarget(Object t) {
		if (target != null) {
			target.removePropertyChangeListener(this);
			if (target instanceof FigEdge) {
				((FigEdge) target).getFig().removePropertyChangeListener(this);
			}
			if (target instanceof FigAssociationClass) {
				FigClassAssociationClass ac = ((FigAssociationClass) target).getAssociationClass();
				if (ac != null) {
					ac.removePropertyChangeListener(this);
				}
			}
		}
		if (!(t instanceof Fig)) {
			if (Model.getFacade().isAModelElement(t)) {
				ArgoDiagram diagram = DiagramUtils.getActiveDiagram();
				if (diagram != null) {
					t = diagram.presentationFor(t);
				}
				if (!(t instanceof Fig)) {
					Project p = ProjectManager.getManager().getCurrentProject();
					Collection col = p.findFigsForMember(t);
					if (col == null||col.isEmpty()) {
						return;
					}
					t = col.iterator().next();
				}
				if (!(t instanceof Fig)) {
					return;
				}
			}else {
				return;
			}
		}
		target = (Fig) t;
		if (target != null) {
			target.addPropertyChangeListener(this);
			if (target instanceof FigEdge) {
				((FigEdge) target).getFig().addPropertyChangeListener(this);
			}
			if (target instanceof FigAssociationClass) {
				FigClassAssociationClass ac = ((FigAssociationClass) target).getAssociationClass();
				if (ac != null) {
					ac.addPropertyChangeListener(this);
				}
			}
		}
		if (lastPanel != null) {
			remove(lastPanel);
			if (lastPanel instanceof TargetListener) {
				removeTargetListener((TargetListener) lastPanel);
			}
		}
		if (t == null) {
			add(blankPanel,BorderLayout.NORTH);
			shouldBeEnabled = false;
			lastPanel = blankPanel;
			return;
		}
		shouldBeEnabled = true;
		stylePanel = null;
		Class targetClass = t.getClass();
		stylePanel = findPanelFor(targetClass);
		if (stylePanel != null) {
			removeTargetListener(stylePanel);
			addTargetListener(stylePanel);
			stylePanel.setTarget(target);
			add(stylePanel,BorderLayout.NORTH);
			shouldBeEnabled = true;
			lastPanel = stylePanel;
		}else {
			add(blankPanel,BorderLayout.NORTH);
			shouldBeEnabled = false;
			lastPanel = blankPanel;
		}
		validate();
		repaint();
	}
	public void refresh() {
		setTarget(target);
	}
	public StylePanel findPanelFor(Class targetClass) {
		Class panelClass = null;
		TabFigTarget p = panels.get(targetClass);
		if (p == null) {
			Class newClass = targetClass;
			while (newClass != null&&panelClass == null) {
				panelClass = panelClassFor(newClass);
				newClass = newClass.getSuperclass();
			}
			if (panelClass == null) {
				return null;
			}
			try {
				p = (TabFigTarget) panelClass.newInstance();
			}catch (IllegalAccessException ignore) {
				return null;
			}catch (InstantiationException ignore) {
				return null;
			}
			panels.put(targetClass,p);
		}
		return(StylePanel) p;
	}
	public Class panelClassFor(Class targetClass) {
		if (targetClass == null) {
			return null;
		}
		StringNamespace classNs = (StringNamespace) StringNamespace.parse(targetClass);
		StringNamespace baseNs = (StringNamespace) StringNamespace.parse("org.argouml.ui.",Namespace.JAVA_NS_TOKEN);
		StringNamespaceElement targetClassElement = (StringNamespaceElement) classNs.peekNamespaceElement();
		classNs.popNamespaceElement();
		String[]bases = new String[] {classNs.toString(),baseNs.toString()};
		for (String stylePanelName:stylePanelNames) {
			for (String baseName:bases) {
				String name = baseName + "." + stylePanelName + targetClassElement;
				Class cls = loadClass(name);
				if (cls != null) {
					return cls;
				}
			}
		}
		return null;
	}
	private Class loadClass(String name) {
		try {
			Class cls = Class.forName(name);
			return cls;
		}catch (ClassNotFoundException ignore) {
		}
		return null;
	}
	protected String[]getStylePanelNames() {
		return stylePanelNames;
	}
	public Object getTarget() {
		return target;
	}
	public boolean shouldBeEnabled(Object targetItem) {
		if (!(targetItem instanceof Fig)) {
			if (Model.getFacade().isAModelElement(targetItem)) {
				ArgoDiagram diagram = DiagramUtils.getActiveDiagram();
				if (diagram == null) {
					shouldBeEnabled = false;
					return false;
				}
				Fig f = diagram.presentationFor(targetItem);
				if (f == null) {
					shouldBeEnabled = false;
					return false;
				}
				targetItem = f;
			}else {
				shouldBeEnabled = false;
				return false;
			}
		}
		shouldBeEnabled = true;
		Class targetClass = targetItem.getClass();
		stylePanel = findPanelFor(targetClass);
		targetClass = targetClass.getSuperclass();
		if (stylePanel == null) {
			shouldBeEnabled = false;
		}
		return shouldBeEnabled;
	}
	public void propertyChange(PropertyChangeEvent pce) {
		DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this,pce);
		SwingUtilities.invokeLater(delayedNotify);
	}
	public void delayedVetoableChange(PropertyChangeEvent pce) {
		if (stylePanel != null) {
			stylePanel.refresh(pce);
		}
	}
	public void targetAdded(TargetEvent e) {
		setTarget(e.getNewTarget());
		fireTargetAdded(e);
	}
	public void targetRemoved(TargetEvent e) {
		setTarget(e.getNewTarget());
		fireTargetRemoved(e);
	}
	public void targetSet(TargetEvent e) {
		setTarget(e.getNewTarget());
		fireTargetSet(e);
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



