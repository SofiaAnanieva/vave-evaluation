package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.swingext.LeftArrowIcon;
import org.argouml.swingext.UpArrowIcon;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.TabProps;
import org.tigris.swidgets.Orientable;
import org.tigris.swidgets.Orientation;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.ProjectBrowser.Position;


public class DetailsPane extends JPanel implements ChangeListener,MouseListener,Orientable,TargetListener {
	private JTabbedPane topLevelTabbedPane = new JTabbedPane();
	private Object currentTarget;
	private List<JPanel>tabPanelList = new ArrayList<JPanel>();
	private int lastNonNullTab = -1;
	private EventListenerList listenerList = new EventListenerList();
	private Orientation orientation;
	private boolean hasTabs = false;
	private void addTargetListener(TargetListener listener) {
		listenerList.add(TargetListener.class,listener);
	}
	private void removeTargetListener(TargetListener listener) {
		listenerList.remove(TargetListener.class,listener);
	}
	public DetailsPane(String compassPoint,Orientation theOrientation) {
		orientation = theOrientation;
		loadTabs(compassPoint,theOrientation);
		setOrientation(orientation);
		setLayout(new BorderLayout());
		setFont(new Font("Dialog",Font.PLAIN,10));
		add(topLevelTabbedPane,BorderLayout.CENTER);
		setTarget(null,true);
		topLevelTabbedPane.addMouseListener(this);
		topLevelTabbedPane.addChangeListener(this);
	}
	private void loadTabs(String direction,Orientation theOrientation) {
		if (Position.South.toString().equalsIgnoreCase(direction)||"detail".equalsIgnoreCase(direction)) {
			hasTabs = true;
		}
	}
	boolean hasTabs() {
		return hasTabs;
	}
	JTabbedPane getTabs() {
		return topLevelTabbedPane;
	}
	public void addTab(AbstractArgoJPanel p,boolean atEnd) {
		Icon icon = p.getIcon();
		String title = Translator.localize(p.getTitle());
		if (atEnd) {
			topLevelTabbedPane.addTab(title,icon,p);
			tabPanelList.add(p);
		}else {
			topLevelTabbedPane.insertTab(title,icon,p,null,0);
			tabPanelList.add(0,p);
		}
	}
	@Deprecated public boolean setToDoItem(Object item) {
		enableTabs(item);
		for (JPanel t:tabPanelList) {
			if (t instanceof TabToDoTarget) {
				((TabToDoTarget) t).setTarget(item);
				topLevelTabbedPane.setSelectedComponent(t);
				return true;
			}
		}
		return false;
	}
	private boolean selectPropsTab(Object target) {
		if (getTabProps().shouldBeEnabled(target)) {
			int indexOfPropPanel = topLevelTabbedPane.indexOfComponent(getTabProps());
			topLevelTabbedPane.setSelectedIndex(indexOfPropPanel);
			lastNonNullTab = indexOfPropPanel;
			return true;
		}
		return false;
	}
	private void setTarget(Object target,boolean defaultToProperties) {
		enableTabs(target);
		if (target != null) {
			boolean tabSelected = false;
			if (defaultToProperties||lastNonNullTab < 0) {
				tabSelected = selectPropsTab(target);
			}else {
				Component selectedTab = topLevelTabbedPane.getComponentAt(lastNonNullTab);
				if (selectedTab instanceof TabTarget) {
					if (((TabTarget) selectedTab).shouldBeEnabled(target)) {
						topLevelTabbedPane.setSelectedIndex(lastNonNullTab);
						tabSelected = true;
					}else {
						tabSelected = selectPropsTab(target);
					}
				}
			}
			if (!tabSelected) {
				for (int i = lastNonNullTab + 1;i < topLevelTabbedPane.getTabCount();i++) {
					Component tab = topLevelTabbedPane.getComponentAt(i);
					if (tab instanceof TabTarget) {
						if (((TabTarget) tab).shouldBeEnabled(target)) {
							topLevelTabbedPane.setSelectedIndex(i);
							((TabTarget) tab).setTarget(target);
							lastNonNullTab = i;
							tabSelected = true;
							break;
						}
					}
				}
			}
			if (!tabSelected) {
				JPanel tab = tabPanelList.get(0);
				if (!(tab instanceof TabToDoTarget)) {
					for (JPanel panel:tabPanelList) {
						if (panel instanceof TabToDoTarget) {
							tab = panel;
							break;
						}
					}
				}
				if (tab instanceof TabToDoTarget) {
					topLevelTabbedPane.setSelectedComponent(tab);
					((TabToDoTarget) tab).setTarget(target);
					lastNonNullTab = topLevelTabbedPane.getSelectedIndex();
				}
			}
		}else {
			JPanel tab = tabPanelList.isEmpty()?null:(JPanel) tabPanelList.get(0);
			if (!(tab instanceof TabToDoTarget)) {
				Iterator it = tabPanelList.iterator();
				while (it.hasNext()) {
					Object o = it.next();
					if (o instanceof TabToDoTarget) {
						tab = (JPanel) o;
						break;
					}
				}
			}
			if (tab instanceof TabToDoTarget) {
				topLevelTabbedPane.setSelectedComponent(tab);
				((TabToDoTarget) tab).setTarget(target);
			}else {
				topLevelTabbedPane.setSelectedIndex(-1);
			}
		}
		currentTarget = target;
	}
	public Object getTarget() {
		return currentTarget;
	}
	@Override public Dimension getMinimumSize() {
		return new Dimension(100,100);
	}
	public int getIndexOfNamedTab(String tabName) {
		for (int i = 0;i < tabPanelList.size();i++) {
			String title = topLevelTabbedPane.getTitleAt(i);
			if (title != null&&title.equals(tabName)) {
				return i;
			}
		}
		return-1;
	}
	public int getTabCount() {
		return tabPanelList.size();
	}
	public boolean selectTabNamed(String tabName) {
		int index = getIndexOfNamedTab(tabName);
		if (index != -1) {
			topLevelTabbedPane.setSelectedIndex(index);
			return true;
		}
		return false;
	}
	public void addToPropTab(Class c,PropPanel p) {
		for (JPanel panel:tabPanelList) {
			if (panel instanceof TabProps) {
				((TabProps) panel).addPanel(c,p);
			}
		}
	}
	public TabProps getTabProps() {
		for (JPanel tab:tabPanelList) {
			if (tab instanceof TabProps) {
				return(TabProps) tab;
			}
		}
		return null;
	}
	public AbstractArgoJPanel getTab(Class<?extends AbstractArgoJPanel>tabClass) {
		for (JPanel tab:tabPanelList) {
			if (tab.getClass().equals(tabClass)) {
				return(AbstractArgoJPanel) tab;
			}
		}
		return null;
	}
	public void stateChanged(ChangeEvent e) {
		Component sel = topLevelTabbedPane.getSelectedComponent();
		if (lastNonNullTab >= 0) {
			JPanel tab = tabPanelList.get(lastNonNullTab);
			if (tab instanceof TargetListener) {
				removeTargetListener((TargetListener) tab);
			}
		}
		Object target = TargetManager.getInstance().getSingleTarget();
		if (!(sel instanceof TabToDoTarget)) {
			if (sel instanceof TabTarget) {
				((TabTarget) sel).setTarget(target);
			}else if (sel instanceof TargetListener) {
				removeTargetListener((TargetListener) sel);
				addTargetListener((TargetListener) sel);
				((TargetListener) sel).targetSet(new TargetEvent(this,TargetEvent.TARGET_SET,new Object[] {},new Object[] {target}));
			}
		}
		if (target != null&&Model.getFacade().isAUMLElement(target)&&topLevelTabbedPane.getSelectedIndex() > 0) {
			lastNonNullTab = topLevelTabbedPane.getSelectedIndex();
		}
	}
	public void mySingleClick(int tab) {
	}
	public void myDoubleClick(int tab) {
	}
	public void mousePressed(MouseEvent me) {
	}
	public void mouseReleased(MouseEvent me) {
	}
	public void mouseEntered(MouseEvent me) {
	}
	public void mouseExited(MouseEvent me) {
	}
	public void mouseClicked(MouseEvent me) {
		int tab = topLevelTabbedPane.getSelectedIndex();
		if (tab != -1) {
			Rectangle tabBounds = topLevelTabbedPane.getBoundsAt(tab);
			if (!tabBounds.contains(me.getX(),me.getY())) {
				return;
			}
			if (me.getClickCount() == 1) {
				mySingleClick(tab);
			}else if (me.getClickCount() >= 2) {
				myDoubleClick(tab);
			}
		}
	}
	private Icon upArrowIcon = new UpArrowIcon();
	private Icon leftArrowIcon = new LeftArrowIcon();
	public void setOrientation(Orientation newOrientation) {
		for (JPanel t:tabPanelList) {
			if (t instanceof Orientable) {
				Orientable o = (Orientable) t;
				o.setOrientation(newOrientation);
			}
		}
	}
	public void targetAdded(TargetEvent e) {
		setTarget(e.getNewTarget(),false);
		fireTargetAdded(e);
	}
	public void targetRemoved(TargetEvent e) {
		setTarget(e.getNewTarget(),false);
		fireTargetRemoved(e);
	}
	public void targetSet(TargetEvent e) {
		setTarget(e.getNewTarget(),false);
		fireTargetSet(e);
	}
	private void enableTabs(Object target) {
		for (int i = 0;i < tabPanelList.size();i++) {
			JPanel tab = tabPanelList.get(i);
			boolean shouldEnable = false;
			if (tab instanceof TargetListener) {
				if (tab instanceof TabTarget) {
					shouldEnable = ((TabTarget) tab).shouldBeEnabled(target);
				}else {
					if (tab instanceof TabToDoTarget) {
						shouldEnable = true;
					}
				}
				removeTargetListener((TargetListener) tab);
				if (shouldEnable) {
					addTargetListener((TargetListener) tab);
				}
			}
			topLevelTabbedPane.setEnabledAt(i,shouldEnable);
		}
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



