package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.argouml.application.api.Argo;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;


class SettingsTabPreferences extends JPanel implements GUISettingsTabInterface {
	private JCheckBox chkSplash;
	private JCheckBox chkReloadRecent;
	private JCheckBox chkStripDiagrams;
	SettingsTabPreferences() {
			setLayout(new BorderLayout());
			JPanel top = new JPanel();
			top.setLayout(new GridBagLayout());
			GridBagConstraints checkConstraints = new GridBagConstraints();
			checkConstraints.anchor = GridBagConstraints.LINE_START;
			checkConstraints.gridy = 0;
			checkConstraints.gridx = 0;
			checkConstraints.gridwidth = 1;
			checkConstraints.gridheight = 1;
			checkConstraints.insets = new Insets(4,10,0,10);
			checkConstraints.gridy = 2;
			JCheckBox j = new JCheckBox(Translator.localize("label.splash"));
			chkSplash = j;
			top.add(chkSplash,checkConstraints);
			checkConstraints.gridy++;
			JCheckBox j2 = new JCheckBox(Translator.localize("label.reload-recent"));
			chkReloadRecent = j2;
			top.add(chkReloadRecent,checkConstraints);
			checkConstraints.gridy++;
			JCheckBox j3 = new JCheckBox(Translator.localize("label.strip-diagrams"));
			chkStripDiagrams = j3;
			top.add(chkStripDiagrams,checkConstraints);
			checkConstraints.fill = GridBagConstraints.HORIZONTAL;
			add(top,BorderLayout.NORTH);
		}
	public void handleSettingsTabRefresh() {
		chkSplash.setSelected(Configuration.getBoolean(Argo.KEY_SPLASH,true));
		chkReloadRecent.setSelected(Configuration.getBoolean(Argo.KEY_RELOAD_RECENT_PROJECT,false));
		chkStripDiagrams.setSelected(Configuration.getBoolean(Argo.KEY_XMI_STRIP_DIAGRAMS,false));
	}
	public void handleSettingsTabSave() {
		Configuration.setBoolean(Argo.KEY_SPLASH,chkSplash.isSelected());
		Configuration.setBoolean(Argo.KEY_RELOAD_RECENT_PROJECT,chkReloadRecent.isSelected());
		Configuration.setBoolean(Argo.KEY_XMI_STRIP_DIAGRAMS,chkStripDiagrams.isSelected());
	}
	public void handleSettingsTabCancel() {
		handleSettingsTabRefresh();
	}
	public void handleResetToDefault() {
	}
	public JPanel getTabPanel() {
		return this;
	}
	public String getTabKey() {
		return"tab.preferences";
	}
	private static final long serialVersionUID = -340220974967836979l;
}



