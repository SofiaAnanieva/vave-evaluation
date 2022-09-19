package org.argouml.application.api;

import javax.swing.JPanel;


public interface GUISettingsTabInterface {
	void handleSettingsTabSave();
	void handleSettingsTabCancel();
	void handleSettingsTabRefresh();
	void handleResetToDefault();
	String getTabKey();
	JPanel getTabPanel();
}



