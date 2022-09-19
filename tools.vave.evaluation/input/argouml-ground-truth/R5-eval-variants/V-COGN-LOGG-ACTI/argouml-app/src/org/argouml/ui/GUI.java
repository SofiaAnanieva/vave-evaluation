package org.argouml.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.ui.ProjectSettingsTabProfile;
import org.argouml.ui.ProjectSettingsTabProperties;


public final class GUI {
	private GUI() {
		addSettingsTab(new SettingsTabPreferences());
		addSettingsTab(new SettingsTabEnvironment());
		addSettingsTab(new SettingsTabUser());
		addSettingsTab(new SettingsTabAppearance());
		addSettingsTab(new SettingsTabProfile());
		addProjectSettingsTab(new ProjectSettingsTabProperties());
		addProjectSettingsTab(new ProjectSettingsTabProfile());
	}
	private static GUI instance = new GUI();
	public static GUI getInstance() {
		return instance;
	}
	private List<GUISettingsTabInterface>settingsTabs = new ArrayList<GUISettingsTabInterface>();
	public void addSettingsTab(final GUISettingsTabInterface panel) {
		settingsTabs.add(panel);
	}
	public final List<GUISettingsTabInterface>getSettingsTabs() {
		return Collections.unmodifiableList(settingsTabs);
	}
	private List<GUISettingsTabInterface>projectSettingsTabs = new ArrayList<GUISettingsTabInterface>();
	public void addProjectSettingsTab(final GUISettingsTabInterface panel) {
		projectSettingsTabs.add(panel);
	}
	public final List<GUISettingsTabInterface>getProjectSettingsTabs() {
		return Collections.unmodifiableList(projectSettingsTabs);
	}
}



