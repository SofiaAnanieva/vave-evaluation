package org.argouml.cognitive.checklist.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.application.api.InitSubsystem;


public class InitCheckListUI implements InitSubsystem {
	public List<AbstractArgoJPanel>getDetailsTabs() {
		List<AbstractArgoJPanel>result = new ArrayList<AbstractArgoJPanel>();
		result.add(new TabChecklist());
		return result;
	}
	public List<GUISettingsTabInterface>getProjectSettingsTabs() {
		return Collections.emptyList();
	}
	public List<GUISettingsTabInterface>getSettingsTabs() {
		return Collections.emptyList();
	}
	public void init() {
	}
}



