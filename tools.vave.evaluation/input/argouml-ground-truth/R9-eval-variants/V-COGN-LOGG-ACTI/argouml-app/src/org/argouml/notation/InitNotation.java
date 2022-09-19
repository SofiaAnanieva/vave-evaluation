package org.argouml.notation;

import java.util.Collections;
import java.util.List;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.application.api.InitSubsystem;


public class InitNotation implements InitSubsystem {
	public void init() {
		NotationProviderFactory2.getInstance();
	}
	public List<GUISettingsTabInterface>getProjectSettingsTabs() {
		return Collections.emptyList();
	}
	public List<GUISettingsTabInterface>getSettingsTabs() {
		return Collections.emptyList();
	}
	public List<AbstractArgoJPanel>getDetailsTabs() {
		return Collections.emptyList();
	}
}



