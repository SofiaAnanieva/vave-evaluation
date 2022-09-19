package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.argouml.application.api.Argo;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;


public class ProjectSettingsTabProperties extends JPanel implements GUISettingsTabInterface {
	private JTextField userFullname;
	private JTextField userEmail;
	private JTextArea description;
	private JTextField version;
	ProjectSettingsTabProperties() {
			setLayout(new BorderLayout());
			JPanel top = new JPanel();
			top.setLayout(new GridBagLayout());
			GridBagConstraints labelConstraints = new GridBagConstraints();
			labelConstraints.anchor = GridBagConstraints.LINE_START;
			labelConstraints.gridy = 0;
			labelConstraints.gridx = 0;
			labelConstraints.gridwidth = 1;
			labelConstraints.gridheight = 1;
			labelConstraints.insets = new Insets(2,20,2,4);
			labelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
			GridBagConstraints fieldConstraints = new GridBagConstraints();
			fieldConstraints.anchor = GridBagConstraints.LINE_END;
			fieldConstraints.fill = GridBagConstraints.BOTH;
			fieldConstraints.gridy = 0;
			fieldConstraints.gridx = 1;
			fieldConstraints.gridwidth = 3;
			fieldConstraints.gridheight = 1;
			fieldConstraints.weightx = 1.0;
			fieldConstraints.insets = new Insets(2,4,2,20);
			labelConstraints.gridy = 0;
			fieldConstraints.gridy = 0;
			top.add(new JLabel(Translator.localize("label.user")),labelConstraints);
			userFullname = new JTextField();
			top.add(userFullname,fieldConstraints);
			labelConstraints.gridy = 1;
			fieldConstraints.gridy = 1;
			top.add(new JLabel(Translator.localize("label.email")),labelConstraints);
			userEmail = new JTextField();
			top.add(userEmail,fieldConstraints);
			labelConstraints.gridy = 2;
			fieldConstraints.gridy = 2;
			fieldConstraints.weighty = 1.0;
			labelConstraints.weighty = 1.0;
			JLabel lblDescription = new JLabel(Translator.localize("label.project.description"));
			lblDescription.setVerticalAlignment(SwingConstants.TOP);
			top.add(lblDescription,labelConstraints);
			description = new JTextArea();
			JScrollPane area = new JScrollPane(description);
			area.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			description.setMargin(new Insets(3,3,3,3));
			description.setLineWrap(true);
			description.setWrapStyleWord(true);
			top.add(area,fieldConstraints);
			labelConstraints.gridy = 3;
			fieldConstraints.gridy = 3;
			fieldConstraints.weighty = 0.0;
			labelConstraints.weighty = 0.0;
			top.add(new JLabel(Translator.localize("label.argouml.version")),labelConstraints);
			version = new JTextField();
			version.setEditable(false);
			top.add(version,fieldConstraints);
			add(top,BorderLayout.CENTER);
		}
	public void handleSettingsTabRefresh() {
		Project p = ProjectManager.getManager().getCurrentProject();
		userFullname.setText(p.getAuthorname());
		userEmail.setText(p.getAuthoremail());
		description.setText(p.getDescription());
		description.setCaretPosition(0);
		version.setText(p.getVersion());
	}
	public void handleSettingsTabSave() {
		Project p = ProjectManager.getManager().getCurrentProject();
		p.setAuthorname(userFullname.getText());
		p.setAuthoremail(userEmail.getText());
		p.setDescription(description.getText());
	}
	public void handleSettingsTabCancel() {
		handleSettingsTabRefresh();
	}
	public void handleResetToDefault() {
		userFullname.setText(Configuration.getString(Argo.KEY_USER_FULLNAME));
		userEmail.setText(Configuration.getString(Argo.KEY_USER_EMAIL));
	}
	public String getTabKey() {
		return"tab.user";
	}
	public JPanel getTabPanel() {
		return this;
	}
}



