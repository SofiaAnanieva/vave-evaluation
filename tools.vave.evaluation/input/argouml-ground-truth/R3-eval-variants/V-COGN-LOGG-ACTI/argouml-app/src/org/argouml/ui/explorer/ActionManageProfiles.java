package org.argouml.ui.explorer;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.GUI;
import org.argouml.ui.ProjectSettingsDialog;
import org.argouml.ui.ProjectSettingsTabProfile;


public class ActionManageProfiles extends AbstractAction {
	private ProjectSettingsDialog dialog;
	private ProjectSettingsTabProfile profilesTab;
	public ActionManageProfiles() {
		super(Translator.localize("action.manage-profiles"),ResourceLoaderWrapper.lookupIcon("action.manage-profiles"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.manage-profiles"));
	}
	public void actionPerformed(ActionEvent e) {
		if (profilesTab == null) {
			Iterator iter = GUI.getInstance().getProjectSettingsTabs().iterator();
			while (iter.hasNext()) {
				GUISettingsTabInterface stp = (GUISettingsTabInterface) iter.next();
				if (stp instanceof ProjectSettingsTabProfile) {
					profilesTab = (ProjectSettingsTabProfile) stp;
				}
			}
		}
		if (dialog == null) {
			dialog = new ProjectSettingsDialog();
		}
		dialog.showDialog(profilesTab);
	}
}



