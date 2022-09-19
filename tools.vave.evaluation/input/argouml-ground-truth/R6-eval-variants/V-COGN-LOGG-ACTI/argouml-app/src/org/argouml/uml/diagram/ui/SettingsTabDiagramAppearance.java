package org.argouml.uml.diagram.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.argouml.application.api.Argo;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectSettings;
import org.argouml.swingext.JLinkButton;
import org.argouml.ui.ActionProjectSettings;
import org.argouml.ui.ArgoJFontChooser;
import org.argouml.uml.diagram.DiagramAppearance;
import org.argouml.util.ArgoFrame;


public class SettingsTabDiagramAppearance extends JPanel implements GUISettingsTabInterface {
	private JButton jbtnDiagramFont;
	private String selectedDiagramFontName;
	private int selectedDiagramFontSize;
	private int scope;
	private JLabel jlblDiagramFont = null;
	public SettingsTabDiagramAppearance(int settingsScope) {
		super();
		scope = settingsScope;
		initialize();
	}
	private void initialize() {
		this.setLayout(new BorderLayout());
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		if (scope == Argo.SCOPE_APPLICATION) {
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
		}
		JPanel settings = new JPanel();
		jlblDiagramFont = new JLabel();
		jlblDiagramFont.setText(Translator.localize("label.diagramappearance.diagramfont"));
		settings.add(getJbtnDiagramFont());
		settings.add(jlblDiagramFont);
		top.add(settings,BorderLayout.CENTER);
		this.add(top,BorderLayout.NORTH);
		this.setSize(new Dimension(296,169));
	}
	private JButton getJbtnDiagramFont() {
		if (jbtnDiagramFont == null) {
			jbtnDiagramFont = new JButton(Translator.localize("label.diagramappearance.changefont"));
			jbtnDiagramFont.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ArgoJFontChooser jFontChooser = new ArgoJFontChooser(ArgoFrame.getInstance(),jbtnDiagramFont,selectedDiagramFontName,selectedDiagramFontSize);
					jFontChooser.setVisible(true);
					if (jFontChooser.isOk()) {
						selectedDiagramFontName = jFontChooser.getResultName();
						selectedDiagramFontSize = jFontChooser.getResultSize();
					}
				}
			});
		}
		return jbtnDiagramFont;
	}
	public void handleSettingsTabRefresh() {
		if (scope == Argo.SCOPE_APPLICATION) {
			selectedDiagramFontName = DiagramAppearance.getInstance().getConfiguredFontName();
			selectedDiagramFontSize = Configuration.getInteger(DiagramAppearance.KEY_FONT_SIZE);
		}
		if (scope == Argo.SCOPE_PROJECT) {
			Project p = ProjectManager.getManager().getCurrentProject();
			ProjectSettings ps = p.getProjectSettings();
			selectedDiagramFontName = ps.getFontName();
			selectedDiagramFontSize = ps.getFontSize();
		}
	}
	protected static boolean getBoolean(ConfigurationKey key) {
		return Configuration.getBoolean(key,false);
	}
	public void handleSettingsTabSave() {
		if (scope == Argo.SCOPE_APPLICATION) {
			Configuration.setString(DiagramAppearance.KEY_FONT_NAME,selectedDiagramFontName);
			Configuration.setInteger(DiagramAppearance.KEY_FONT_SIZE,selectedDiagramFontSize);
		}
		if (scope == Argo.SCOPE_PROJECT) {
			Project p = ProjectManager.getManager().getCurrentProject();
			ProjectSettings ps = p.getProjectSettings();
			ps.setFontName(selectedDiagramFontName);
			ps.setFontSize(selectedDiagramFontSize);
		}
	}
	public void handleSettingsTabCancel() {
		handleSettingsTabRefresh();
	}
	public void handleResetToDefault() {
		if (scope == Argo.SCOPE_PROJECT) {
			selectedDiagramFontName = DiagramAppearance.getInstance().getConfiguredFontName();
			selectedDiagramFontSize = Configuration.getInteger(DiagramAppearance.KEY_FONT_SIZE);
		}
	}
	public String getTabKey() {
		return"tab.diagramappearance";
	}
	public JPanel getTabPanel() {
		return this;
	}
	protected JButton createCheckBox(String key) {
		JButton j = new JButton(Translator.localize(key));
		return j;
	}
	protected JLabel createLabel(String key) {
		return new JLabel(Translator.localize(key));
	}
	protected JButton createButton(String key) {
		return new JButton(Translator.localize(key));
	}
	public void setVisible(boolean arg0) {
		super.setVisible(arg0);
		if (arg0)handleSettingsTabRefresh();
	}
}



