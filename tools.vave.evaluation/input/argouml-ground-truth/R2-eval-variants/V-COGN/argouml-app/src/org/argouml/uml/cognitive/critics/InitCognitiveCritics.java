package org.argouml.uml.cognitive.critics;

import java.util.Collections;
import java.util.List;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.application.api.InitSubsystem;
import org.argouml.profile.ProfileFacade;


public class InitCognitiveCritics implements InitSubsystem {
	public void init() {
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



