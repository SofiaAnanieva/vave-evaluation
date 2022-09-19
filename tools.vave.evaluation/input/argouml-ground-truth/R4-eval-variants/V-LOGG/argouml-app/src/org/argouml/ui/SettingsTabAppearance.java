package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.argouml.application.api.Argo;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.tigris.swidgets.LabelledLayout;


class SettingsTabAppearance extends JPanel implements GUISettingsTabInterface {
	private JComboBox lookAndFeel;
	private JComboBox metalTheme;
	private JComboBox language;
	private JLabel metalLabel;
	private JCheckBox smoothEdges;
	private Locale locale;
	SettingsTabAppearance() {
			setLayout(new BorderLayout());
			int labelGap = 10;
			int componentGap = 10;
			JPanel top = new JPanel(new LabelledLayout(labelGap,componentGap));
			JLabel label = new JLabel(Translator.localize("label.look-and-feel"));
			lookAndFeel = new JComboBox(LookAndFeelMgr.getInstance().getAvailableLookAndFeelNames());
			lookAndFeel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setMetalThemeState();
				}
			});
			label.setLabelFor(lookAndFeel);
			top.add(label);
			top.add(lookAndFeel);
			metalLabel = new JLabel(Translator.localize("label.metal-theme"));
			metalTheme = new JComboBox(LookAndFeelMgr.getInstance().getAvailableThemeNames());
			metalLabel.setLabelFor(metalTheme);
			top.add(metalLabel);
			top.add(metalTheme);
			JCheckBox j = new JCheckBox(Translator.localize("label.smooth-edges"));
			smoothEdges = j;
			JLabel emptyLabel = new JLabel();
			emptyLabel.setLabelFor(smoothEdges);
			top.add(emptyLabel);
			top.add(smoothEdges);
			JLabel languageLabel = new JLabel(Translator.localize("label.language"));
			Collection<MyLocale>c = MyLocale.getLocales();
			language = new JComboBox(c.toArray());
			Object o = MyLocale.getDefault(c);
			if (o != null) {
				language.setSelectedItem(o);
			}else {
				language.setSelectedIndex(-1);
			}
			language.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JComboBox combo = (JComboBox) e.getSource();
					locale = ((MyLocale) combo.getSelectedItem()).getLocale();
				}
			});
			languageLabel.setLabelFor(language);
			top.add(languageLabel);
			top.add(language);
			top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			add(top,BorderLayout.CENTER);
			JLabel restart = new JLabel(Translator.localize("label.restart-application"));
			restart.setHorizontalAlignment(SwingConstants.CENTER);
			restart.setVerticalAlignment(SwingConstants.CENTER);
			restart.setBorder(BorderFactory.createEmptyBorder(10,2,10,2));
			add(restart,BorderLayout.SOUTH);
			setMetalThemeState();
		}
	private void setMetalThemeState() {
		String lafName = (String) lookAndFeel.getSelectedItem();
		boolean enabled = LookAndFeelMgr.getInstance().isThemeCompatibleLookAndFeel(LookAndFeelMgr.getInstance().getLookAndFeelFromName(lafName));
		metalLabel.setEnabled(enabled);
		metalTheme.setEnabled(enabled);
	}
	public void handleSettingsTabRefresh() {
		String laf = LookAndFeelMgr.getInstance().getCurrentLookAndFeelName();
		String theme = LookAndFeelMgr.getInstance().getCurrentThemeName();
		lookAndFeel.setSelectedItem(laf);
		metalTheme.setSelectedItem(theme);
		smoothEdges.setSelected(Configuration.getBoolean(Argo.KEY_SMOOTH_EDGES,false));
	}
	public void handleSettingsTabSave() {
		LookAndFeelMgr.getInstance().setCurrentLAFAndThemeByName((String) lookAndFeel.getSelectedItem(),(String) metalTheme.getSelectedItem());
		Configuration.setBoolean(Argo.KEY_SMOOTH_EDGES,smoothEdges.isSelected());
		if (locale != null) {
			Configuration.setString(Argo.KEY_LOCALE,locale.toString());
		}
	}
	public void handleSettingsTabCancel() {
	}
	public void handleResetToDefault() {
	}
	public String getTabKey() {
		return"tab.appearance";
	}
	public JPanel getTabPanel() {
		return this;
	}
	private static final long serialVersionUID = -6779214318672690570l;
}

class MyLocale {
	private Locale myLocale;
	MyLocale(Locale locale) {
			myLocale = locale;
		}
	Locale getLocale() {
		return myLocale;
	}
	public String toString() {
		StringBuffer displayString = new StringBuffer(myLocale.toString());
		displayString.append(" (");
		displayString.append(myLocale.getDisplayLanguage(myLocale));
		if (myLocale.getDisplayCountry(myLocale) != null&&myLocale.getDisplayCountry(myLocale).length() > 0) {
			displayString.append(" ");
			displayString.append(myLocale.getDisplayCountry(myLocale));
		}
		displayString.append(")");
		if (myLocale.equals(Translator.getSystemDefaultLocale())) {
			displayString.append(" - Default");
		}
		return displayString.toString();
	}
	static Collection<MyLocale>getLocales() {
		Collection<MyLocale>c = new ArrayList<MyLocale>();
		for (Locale locale:Arrays.asList(Translator.getLocales())) {
			c.add(new MyLocale(locale));
		}
		return c;
	}
	static MyLocale getDefault(Collection<MyLocale>c) {
		Locale locale = Locale.getDefault();
		for (MyLocale ml:c) {
			if (locale.equals(ml.getLocale())) {
				return ml;
			}
		}
		return null;
	}
}



