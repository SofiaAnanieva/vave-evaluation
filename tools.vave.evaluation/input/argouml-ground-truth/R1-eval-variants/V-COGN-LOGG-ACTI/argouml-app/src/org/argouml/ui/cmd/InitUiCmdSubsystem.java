package org.argouml.ui.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.application.api.InitSubsystem;


public class InitUiCmdSubsystem implements InitSubsystem {
	public List<GUISettingsTabInterface>getProjectSettingsTabs() {
		return Collections.emptyList();
	}
	public List<GUISettingsTabInterface>getSettingsTabs() {
		List<GUISettingsTabInterface>result = new ArrayList<GUISettingsTabInterface>();
		result.add(new SettingsTabShortcuts());
		return result;
	}
	public void init() {
		ActionAdjustSnap.init();
		ActionAdjustGrid.init();
	}
	public List<AbstractArgoJPanel>getDetailsTabs() {
		return Collections.emptyList();
	}
}



