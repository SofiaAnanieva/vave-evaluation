package org.argouml.notation.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.swingext.JLinkButton;
import org.argouml.ui.ActionProjectSettings;
import org.argouml.ui.ShadowComboBox;


public class SettingsTabNotation extends JPanel implements GUISettingsTabInterface {
	private JComboBox notationLanguage;
	private JCheckBox showBoldNames;
	private JCheckBox useGuillemots;
	private JCheckBox showAssociationNames;
	private JCheckBox showVisibility;
	private JCheckBox showMultiplicity;
	private JCheckBox showInitialValue;
	private JCheckBox showProperties;
	private JCheckBox showTypes;
	private JCheckBox showStereotypes;
	private JCheckBox showSingularMultiplicities;
	private JCheckBox hideBidirectionalArrows;
	private ShadowComboBox defaultShadowWidth;
	private int scope;
	public SettingsTabNotation(int settingsScope) {
		super();
		scope = settingsScope;
		setLayout(new BorderLayout());
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		if (settingsScope == Argo.SCOPE_APPLICATION) {
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
		settings.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(0,30,0,4);
		constraints.gridy = GridBagConstraints.RELATIVE;
		JPanel notationLanguagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
		JLabel notationLanguageLabel = createLabel("label.notation-language");
		notationLanguage = new NotationComboBox();
		notationLanguageLabel.setLabelFor(notationLanguage);
		notationLanguagePanel.add(notationLanguageLabel);
		notationLanguagePanel.add(notationLanguage);
		settings.add(notationLanguagePanel,constraints);
		showBoldNames = createCheckBox("label.show-bold-names");
		settings.add(showBoldNames,constraints);
		useGuillemots = createCheckBox("label.use-guillemots");
		settings.add(useGuillemots,constraints);
		showAssociationNames = createCheckBox("label.show-associationnames");
		settings.add(showAssociationNames,constraints);
		showVisibility = createCheckBox("label.show-visibility");
		settings.add(showVisibility,constraints);
		showMultiplicity = createCheckBox("label.show-multiplicity");
		settings.add(showMultiplicity,constraints);
		showInitialValue = createCheckBox("label.show-initialvalue");
		settings.add(showInitialValue,constraints);
		showProperties = createCheckBox("label.show-properties");
		settings.add(showProperties,constraints);
		showTypes = createCheckBox("label.show-types");
		settings.add(showTypes,constraints);
		showStereotypes = createCheckBox("label.show-stereotypes");
		settings.add(showStereotypes,constraints);
		showSingularMultiplicities = createCheckBox("label.show-singular-multiplicities");
		settings.add(showSingularMultiplicities,constraints);
		hideBidirectionalArrows = createCheckBox("label.hide-bidirectional-arrows");
		settings.add(hideBidirectionalArrows,constraints);
		constraints.insets = new Insets(5,30,0,4);
		JPanel defaultShadowWidthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
		JLabel defaultShadowWidthLabel = createLabel("label.default-shadow-width");
		defaultShadowWidth = new ShadowComboBox();
		defaultShadowWidthLabel.setLabelFor(defaultShadowWidth);
		defaultShadowWidthPanel.add(defaultShadowWidthLabel);
		defaultShadowWidthPanel.add(defaultShadowWidth);
		settings.add(defaultShadowWidthPanel,constraints);
		top.add(settings,BorderLayout.CENTER);
		add(top,BorderLayout.NORTH);
	}
	public void handleSettingsTabRefresh() {
		if (scope == Argo.SCOPE_APPLICATION) {
			showBoldNames.setSelected(getBoolean(Notation.KEY_SHOW_BOLD_NAMES));
			useGuillemots.setSelected(getBoolean(Notation.KEY_USE_GUILLEMOTS));
			notationLanguage.setSelectedItem(Notation.getConfiguredNotation());
			showAssociationNames.setSelected(Configuration.getBoolean(Notation.KEY_SHOW_ASSOCIATION_NAMES,true));
			showVisibility.setSelected(getBoolean(Notation.KEY_SHOW_VISIBILITY));
			showInitialValue.setSelected(getBoolean(Notation.KEY_SHOW_INITIAL_VALUE));
			showProperties.setSelected(getBoolean(Notation.KEY_SHOW_PROPERTIES));
			showTypes.setSelected(Configuration.getBoolean(Notation.KEY_SHOW_TYPES,true));
			showMultiplicity.setSelected(getBoolean(Notation.KEY_SHOW_MULTIPLICITY));
			showStereotypes.setSelected(getBoolean(Notation.KEY_SHOW_STEREOTYPES));
			showSingularMultiplicities.setSelected(Configuration.getBoolean(Notation.KEY_SHOW_SINGULAR_MULTIPLICITIES,true));
			hideBidirectionalArrows.setSelected(Configuration.getBoolean(Notation.KEY_HIDE_BIDIRECTIONAL_ARROWS,true));
			defaultShadowWidth.setSelectedIndex(Configuration.getInteger(Notation.KEY_DEFAULT_SHADOW_WIDTH,1));
		}
		if (scope == Argo.SCOPE_PROJECT) {
			Project p = ProjectManager.getManager().getCurrentProject();
			ProjectSettings ps = p.getProjectSettings();
			notationLanguage.setSelectedItem(Notation.findNotation(ps.getNotationLanguage()));
			showBoldNames.setSelected(ps.getShowBoldNamesValue());
			useGuillemots.setSelected(ps.getUseGuillemotsValue());
			showAssociationNames.setSelected(ps.getShowAssociationNamesValue());
			showVisibility.setSelected(ps.getShowVisibilityValue());
			showMultiplicity.setSelected(ps.getShowMultiplicityValue());
			showInitialValue.setSelected(ps.getShowInitialValueValue());
			showProperties.setSelected(ps.getShowPropertiesValue());
			showTypes.setSelected(ps.getShowTypesValue());
			showStereotypes.setSelected(ps.getShowStereotypesValue());
			showSingularMultiplicities.setSelected(ps.getShowSingularMultiplicitiesValue());
			hideBidirectionalArrows.setSelected(ps.getHideBidirectionalArrowsValue());
			defaultShadowWidth.setSelectedIndex(ps.getDefaultShadowWidthValue());
		}
	}
	protected static boolean getBoolean(ConfigurationKey key) {
		return Configuration.getBoolean(key,false);
	}
	public void handleSettingsTabSave() {
		if (scope == Argo.SCOPE_APPLICATION) {
			Notation.setDefaultNotation((NotationName) notationLanguage.getSelectedItem());
			Configuration.setBoolean(Notation.KEY_SHOW_BOLD_NAMES,showBoldNames.isSelected());
			Configuration.setBoolean(Notation.KEY_USE_GUILLEMOTS,useGuillemots.isSelected());
			Configuration.setBoolean(Notation.KEY_SHOW_ASSOCIATION_NAMES,showAssociationNames.isSelected());
			Configuration.setBoolean(Notation.KEY_SHOW_VISIBILITY,showVisibility.isSelected());
			Configuration.setBoolean(Notation.KEY_SHOW_MULTIPLICITY,showMultiplicity.isSelected());
			Configuration.setBoolean(Notation.KEY_SHOW_INITIAL_VALUE,showInitialValue.isSelected());
			Configuration.setBoolean(Notation.KEY_SHOW_PROPERTIES,showProperties.isSelected());
			Configuration.setBoolean(Notation.KEY_SHOW_TYPES,showTypes.isSelected());
			Configuration.setBoolean(Notation.KEY_SHOW_STEREOTYPES,showStereotypes.isSelected());
			Configuration.setBoolean(Notation.KEY_SHOW_SINGULAR_MULTIPLICITIES,showSingularMultiplicities.isSelected());
			Configuration.setBoolean(Notation.KEY_HIDE_BIDIRECTIONAL_ARROWS,hideBidirectionalArrows.isSelected());
			Configuration.setInteger(Notation.KEY_DEFAULT_SHADOW_WIDTH,defaultShadowWidth.getSelectedIndex());
		}
		if (scope == Argo.SCOPE_PROJECT) {
			Project p = ProjectManager.getManager().getCurrentProject();
			ProjectSettings ps = p.getProjectSettings();
			NotationName nn = (NotationName) notationLanguage.getSelectedItem();
			if (nn != null)ps.setNotationLanguage(nn.getConfigurationValue());
			ps.setShowBoldNames(showBoldNames.isSelected());
			ps.setUseGuillemots(useGuillemots.isSelected());
			ps.setShowAssociationNames(showAssociationNames.isSelected());
			ps.setShowVisibility(showVisibility.isSelected());
			ps.setShowMultiplicity(showMultiplicity.isSelected());
			ps.setShowInitialValue(showInitialValue.isSelected());
			ps.setShowProperties(showProperties.isSelected());
			ps.setShowTypes(showTypes.isSelected());
			ps.setShowStereotypes(showStereotypes.isSelected());
			ps.setShowSingularMultiplicities(showSingularMultiplicities.isSelected());
			ps.setDefaultShadowWidth(defaultShadowWidth.getSelectedIndex());
			ps.setHideBidirectionalArrows(hideBidirectionalArrows.isSelected());
		}
	}
	public void handleSettingsTabCancel() {
		handleSettingsTabRefresh();
	}
	public void handleResetToDefault() {
		if (scope == Argo.SCOPE_PROJECT) {
			notationLanguage.setSelectedItem(Notation.getConfiguredNotation());
			showBoldNames.setSelected(getBoolean(Notation.KEY_SHOW_BOLD_NAMES));
			useGuillemots.setSelected(getBoolean(Notation.KEY_USE_GUILLEMOTS));
			showAssociationNames.setSelected(Configuration.getBoolean(Notation.KEY_SHOW_ASSOCIATION_NAMES,true));
			showVisibility.setSelected(getBoolean(Notation.KEY_SHOW_VISIBILITY));
			showMultiplicity.setSelected(getBoolean(Notation.KEY_SHOW_MULTIPLICITY));
			showInitialValue.setSelected(getBoolean(Notation.KEY_SHOW_INITIAL_VALUE));
			showProperties.setSelected(Configuration.getBoolean(Notation.KEY_SHOW_PROPERTIES));
			showTypes.setSelected(Configuration.getBoolean(Notation.KEY_SHOW_TYPES,true));
			showStereotypes.setSelected(Configuration.getBoolean(Notation.KEY_SHOW_STEREOTYPES));
			showSingularMultiplicities.setSelected(Configuration.getBoolean(Notation.KEY_SHOW_SINGULAR_MULTIPLICITIES));
			hideBidirectionalArrows.setSelected(Configuration.getBoolean(Notation.KEY_HIDE_BIDIRECTIONAL_ARROWS,true));
			defaultShadowWidth.setSelectedIndex(Configuration.getInteger(Notation.KEY_DEFAULT_SHADOW_WIDTH,1));
		}
	}
	public String getTabKey() {
		return"tab.notation";
	}
	public JPanel getTabPanel() {
		return this;
	}
	protected JCheckBox createCheckBox(String key) {
		JCheckBox j = new JCheckBox(Translator.localize(key));
		return j;
	}
	protected JLabel createLabel(String key) {
		return new JLabel(Translator.localize(key));
	}
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			handleSettingsTabRefresh();
		}
	}
}



