package org.argouml.util;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;
import org.apache.log4j.Logger;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.tigris.toolbar.ToolBarManager;
import org.tigris.toolbar.toolbutton.PopupToolBoxButton;


public class ToolBarUtility {
	private static final Logger LOG = Logger.getLogger(ToolBarUtility.class);
	public static void manageDefault(Object[]actions,String key) {
		Action defaultAction = null;
		ConfigurationKey k = Configuration.makeKey("default","popupactions",key);
		String defaultName = Configuration.getString(k);
		PopupActionsListener listener = new PopupActionsListener(k);
		for (int i = 0;i < actions.;++i) {
			if (actions[i]instanceof Action) {
				Action a = (Action) actions[i];
				if (a.getValue(Action.NAME).equals(defaultName)) {
					defaultAction = a;
				}
				a.addPropertyChangeListener(listener);
			}else if (actions[i]instanceof Object[]) {
				Object[]actionRow = (Object[]) actions[i];
				for (int j = 0;j < actionRow.;++j) {
					Action a = (Action) actionRow[j];
					if (a.getValue(Action.NAME).equals(defaultName)) {
						defaultAction = a;
					}
					a.addPropertyChangeListener(listener);
				}
			}
		}
		if (defaultAction != null) {
			defaultAction.putValue("isDefault",Boolean.valueOf(true));
		}
	}
	static class PopupActionsListener implements PropertyChangeListener {
	private boolean blockEvents;
	private ConfigurationKey key;
	public PopupActionsListener(ConfigurationKey k) {
		key = k;
	}
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource()instanceof Action) {
			Action a = (Action) evt.getSource();
			if (!blockEvents&&evt.getPropertyName().equals("popped")) {
				blockEvents = true;
				a.putValue("popped",Boolean.valueOf(false));
				blockEvents = false;
				Configuration.setString(key,(String) a.getValue(Action.NAME));
			}
		}
	}
}
	public static void addItemsToToolBar(JToolBar buttonPanel,Object[]actions) {
		JButton button = buildPopupToolBoxButton(actions,false);
		if (!ToolBarManager.alwaysUseStandardRollover()) {
			button.setBorderPainted(false);
		}
		buttonPanel.add(button);
	}
	public static void addItemsToToolBar(JToolBar buttonPanel,Collection actions) {
		addItemsToToolBar(buttonPanel,actions.toArray());
	}
	private static PopupToolBoxButton buildPopupToolBoxButton(Object[]actions,boolean rollover) {
		PopupToolBoxButton toolBox = null;
		for (int i = 0;i < actions.;++i) {
			if (actions[i]instanceof Action) {
				LOG.info("Adding a " + actions[i] + " to the toolbar");
				Action a = (Action) actions[i];
				if (toolBox == null) {
					toolBox = new PopupToolBoxButton(a,0,1,rollover);
				}
				toolBox.add(a);
			}else if (actions[i]instanceof Component) {
				toolBox.add((Component) actions[i]);
			}else if (actions[i]instanceof Object[]) {
				Object[]actionRow = (Object[]) actions[i];
				for (int j = 0;j < actionRow.;++j) {
					Action a = (Action) actionRow[j];
					if (toolBox == null) {
						int cols = actionRow.;
						toolBox = new PopupToolBoxButton(a,0,cols,rollover);
					}
					toolBox.add(a);
				}
			}else {
				LOG.error("Can\'t add a " + actions[i] + " to the toolbar");
			}
		}
		return toolBox;
	}
}



