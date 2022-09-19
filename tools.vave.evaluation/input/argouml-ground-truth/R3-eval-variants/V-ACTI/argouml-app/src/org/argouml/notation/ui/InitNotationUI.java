package org.argouml.notation.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.Argo;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.application.api.InitSubsystem;


public class InitNotationUI implements InitSubsystem {
	public void init() {
	}
	public List<GUISettingsTabInterface>getProjectSettingsTabs() {
		List<GUISettingsTabInterface>result = new ArrayList<GUISettingsTabInterface>();
		result.add(new SettingsTabNotation(Argo.SCOPE_PROJECT));
		return result;
	}
	public List<GUISettingsTabInterface>getSettingsTabs() {
		List<GUISettingsTabInterface>result = new ArrayList<GUISettingsTabInterface>();
		result.add(new SettingsTabNotation(Argo.SCOPE_APPLICATION));
		return result;
	}
	public List<AbstractArgoJPanel>getDetailsTabs() {
		return Collections.emptyList();
	}
}



