package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.argouml.application.api.Argo;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.swingext.JLinkButton;


class SettingsTabUser extends JPanel implements GUISettingsTabInterface {
	private JTextField userFullname;
	private JTextField userEmail;
	SettingsTabUser() {
			setLayout(new BorderLayout());
			JPanel top = new JPanel();
			top.setLayout(new BorderLayout());
			JPanel warning = new JPanel();
			warning.setLayout(new BoxLayout(warning,BoxLayout.PAGE_AXIS));
			JLabel warningLabel = new JLabel(Translator.localize("label.warning"));
			warningLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
			warning.add(warningLabel);
			JLinkButton projectSettings = new JLinkButton();
			projectSettings.setAction(new ActionProjectSettings());
			projectSettings.setText(Translator.localize("button.project-settings"));
			projectSettings.setIcon(null);
			projectSettings.setAlignmentX(Component.RIGHT_ALIGNMENT);
			warning.add(projectSettings);
			top.add(warning,BorderLayout.NORTH);
			JPanel settings = new JPanel();
			settings.setLayout(new GridBagLayout());
			GridBagConstraints labelConstraints = new GridBagConstraints();
			labelConstraints.anchor = GridBagConstraints.WEST;
			labelConstraints.gridy = 0;
			labelConstraints.gridx = 0;
			labelConstraints.gridwidth = 1;
			labelConstraints.gridheight = 1;
			labelConstraints.insets = new Insets(2,20,2,4);
			GridBagConstraints fieldConstraints = new GridBagConstraints();
			fieldConstraints.anchor = GridBagConstraints.EAST;
			fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
			fieldConstraints.gridy = 0;
			fieldConstraints.gridx = 1;
			fieldConstraints.gridwidth = 3;
			fieldConstraints.gridheight = 1;
			fieldConstraints.weightx = 1.0;
			fieldConstraints.insets = new Insets(2,4,2,20);
			labelConstraints.gridy = 0;
			fieldConstraints.gridy = 0;
			settings.add(new JLabel(Translator.localize("label.user")),labelConstraints);
			JTextField j = new JTextField();
			userFullname = j;
			settings.add(userFullname,fieldConstraints);
			labelConstraints.gridy = 1;
			fieldConstraints.gridy = 1;
			settings.add(new JLabel(Translator.localize("label.email")),labelConstraints);
			JTextField j1 = new JTextField();
			userEmail = j1;
			settings.add(userEmail,fieldConstraints);
			top.add(settings,BorderLayout.CENTER);
			add(top,BorderLayout.NORTH);
		}
	public void handleSettingsTabRefresh() {
		userFullname.setText(Configuration.getString(Argo.KEY_USER_FULLNAME));
		userEmail.setText(Configuration.getString(Argo.KEY_USER_EMAIL));
	}
	public void handleSettingsTabSave() {
		Configuration.setString(Argo.KEY_USER_FULLNAME,userFullname.getText());
		Configuration.setString(Argo.KEY_USER_EMAIL,userEmail.getText());
	}
	public void handleSettingsTabCancel() {
		handleSettingsTabRefresh();
	}
	public void handleResetToDefault() {
	}
	public String getTabKey() {
		return"tab.user";
	}
	public JPanel getTabPanel() {
		return this;
	}
	private static final long serialVersionUID = -742258688091914619l;
}



