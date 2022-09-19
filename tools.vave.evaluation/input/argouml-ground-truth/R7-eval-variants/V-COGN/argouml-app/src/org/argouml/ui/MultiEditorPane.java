package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.ModeLabelDragFactory;
import org.argouml.uml.diagram.ui.TabDiagram;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ModeDragScrollFactory;
import org.tigris.gef.base.ModeFactory;
import org.tigris.gef.base.ModePopupFactory;
import org.tigris.gef.base.ModeSelectFactory;


public class MultiEditorPane extends JPanel implements ChangeListener,MouseListener,TargetListener {
	 {
		ArrayList<ModeFactory>modeFactories = new ArrayList<ModeFactory>();
		modeFactories.add(new ModeLabelDragFactory());
		modeFactories.add(new ModeSelectFactory());
		modeFactories.add(new ModePopupFactory());
		modeFactories.add(new ModeDragScrollFactory());
		Globals.setDefaultModeFactories(modeFactories);
	}
	private final JPanel[]tabInstances = new JPanel[] {new TabDiagram()};
	private JTabbedPane tabs = new JTabbedPane(SwingConstants.BOTTOM);
	private List<JPanel>tabPanels = new ArrayList<JPanel>(Arrays.asList(tabInstances));
	private Component lastTab;
	public MultiEditorPane() {
		setLayout(new BorderLayout());
		add(tabs,BorderLayout.CENTER);
		for (int i = 0;i < tabPanels.size();i++) {
			String title = "tab";
			JPanel t = tabPanels.get(i);
			if (t instanceof AbstractArgoJPanel) {
				title = ((AbstractArgoJPanel) t).getTitle();
			}
			tabs.addTab("As " + title,t);
			tabs.setEnabledAt(i,false);
			if (t instanceof TargetListener) {
				TargetManager.getInstance().addTargetListener((TargetListener) t);
			}
		}
		tabs.addChangeListener(this);
		tabs.addMouseListener(this);
		setTarget(null);
	}
	@Override public Dimension getPreferredSize() {
		return new Dimension(400,500);
	}
	@Override public Dimension getMinimumSize() {
		return new Dimension(100,100);
	}
	private void setTarget(Object t) {
		enableTabs(t);
		for (int i = 0;i < tabs.getTabCount();i++) {
			Component tab = tabs.getComponentAt(i);
			if (tab.isEnabled()) {
				tabs.setSelectedComponent(tab);
				break;
			}
		}
	}
	private void enableTabs(Object t) {
		for (int i = 0;i < tabs.getTabCount();i++) {
			Component tab = tabs.getComponentAt(i);
			if (tab instanceof TabTarget) {
				TabTarget targetTab = (TabTarget) tab;
				boolean shouldBeEnabled = targetTab.shouldBeEnabled(t);
				tabs.setEnabledAt(i,shouldBeEnabled);
			}
		}
	}
	public int getIndexOfNamedTab(String tabName) {
		for (int i = 0;i < tabPanels.size();i++) {
			String title = tabs.getTitleAt(i);
			if (title != null&&title.equals(tabName)) {
				return i;
			}
		}
		return-1;
	}
	public void selectTabNamed(String tabName) {
		int index = getIndexOfNamedTab(tabName);
		if (index != -1) {
			tabs.setSelectedIndex(index);
		}
	}
	public void selectNextTab() {
		int size = tabPanels.size();
		int currentTab = tabs.getSelectedIndex();
		for (int i = 1;i < tabPanels.size();i++) {
			int newTab = (currentTab + i) % size;
			if (tabs.isEnabledAt(newTab)) {
				tabs.setSelectedIndex(newTab);
				return;
			}
		}
	}
	public void stateChanged(ChangeEvent e) {
		if (lastTab != null) {
			lastTab.setVisible(false);
		}
		lastTab = tabs.getSelectedComponent();
		lastTab.setVisible(true);
		if (lastTab instanceof TabModelTarget) {
			((TabModelTarget) lastTab).refresh();
		}
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
		int tab = tabs.getSelectedIndex();
		if (tab != -1) {
			Rectangle tabBounds = tabs.getBoundsAt(tab);
			if (!tabBounds.contains(me.getX(),me.getY()))return;
			if (me.getClickCount() == 1) {
				mySingleClick(tab);
				me.consume();
			}else if (me.getClickCount() >= 2) {
				myDoubleClick(tab);
				me.consume();
			}
		}
	}
	public void mySingleClick(int tab) {
	}
	public void myDoubleClick(int tab) {
	}
	public void targetAdded(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetRemoved(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetSet(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	protected JTabbedPane getTabs() {
		return tabs;
	}
}



