package org.argouml.application.api;

import java.util.List;


public interface InitSubsystem {
	public void init();
	public List<GUISettingsTabInterface>getSettingsTabs();
	public List<GUISettingsTabInterface>getProjectSettingsTabs();
	public List<AbstractArgoJPanel>getDetailsTabs();
}



