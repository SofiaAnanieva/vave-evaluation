package org.argouml.ui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.i18n.Translator;
import org.tigris.swidgets.Property;


class SettingsTabLayout extends JPanel implements GUISettingsTabInterface {
	SettingsTabLayout() {
			super();
			setLayout(new BorderLayout());
			final String[]positions =  {"North","South","East"};
			final String paneColumnHeader = "Pane";
			final String positionColumnHeader = "Position";
			JPanel top = new JPanel(new BorderLayout());
			top.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			add(top,BorderLayout.CENTER);
			JLabel restart = new JLabel(Translator.localize("label.restart-application"));
			restart.setHorizontalAlignment(SwingConstants.CENTER);
			restart.setVerticalAlignment(SwingConstants.CENTER);
			restart.setBorder(BorderFactory.createEmptyBorder(10,2,10,2));
			add(restart,BorderLayout.SOUTH);
		}
	private Property createProperty(String text,String[]positions,Class tab) {
		ConfigurationKey key = makeKey(tab);
		String currentValue = Configuration.getString(key,"South");
		return new Property(Translator.localize(text),String.class,currentValue,positions);
	}
	private void loadPosition(Property position,Class tab) {
		ConfigurationKey key = makeKey(tab);
		position.setCurrentValue(Configuration.getString(key,"South"));
	}
	private void savePosition(Property position,Class tab) {
		ConfigurationKey key = makeKey(tab);
		Configuration.setString(key,position.getCurrentValue().toString());
	}
	private ConfigurationKey makeKey(Class tab) {
		String className = tab.getName();
		String shortClassName = className.substring(className.lastIndexOf('.') + 1).toLowerCase();
		ConfigurationKey key = Configuration.makeKey("layout",shortClassName);
		return key;
	}
	public void handleSettingsTabRefresh() {
	}
	public void handleSettingsTabSave() {
	}
	public void handleSettingsTabCancel() {
	}
	public void handleResetToDefault() {
	}
	public String getTabKey() {
		return"tab.layout";
	}
	public JPanel getTabPanel() {
		return this;
	}
	private static final long serialVersionUID = 739259705815092510l;
}



