package org.argouml.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.argouml.application.api.Argo;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.SaveGraphicsManager;
import org.argouml.util.SuffixFilter;
import org.tigris.swidgets.LabelledLayout;


class SettingsTabEnvironment extends JPanel implements GUISettingsTabInterface {
	private JTextField fieldArgoExtDir;
	private JTextField fieldJavaHome;
	private JTextField fieldUserHome;
	private JTextField fieldUserDir;
	private JTextField fieldStartupDir;
	private JComboBox fieldGraphicsFormat;
	private JComboBox fieldGraphicsResolution;
	private Collection<GResolution>theResolutions;
	SettingsTabEnvironment() {
			super();
			setLayout(new BorderLayout());
			int labelGap = 10;
			int componentGap = 5;
			JPanel top = new JPanel(new LabelledLayout(labelGap,componentGap));
			JLabel label = new JLabel(Translator.localize("label.default.graphics-format"));
			fieldGraphicsFormat = new JComboBox();
			label.setLabelFor(fieldGraphicsFormat);
			top.add(label);
			top.add(fieldGraphicsFormat);
			label = new JLabel(Translator.localize("label.default.graphics-resolution"));
			theResolutions = new ArrayList<GResolution>();
			theResolutions.add(new GResolution(1,"combobox.item.resolution-1"));
			theResolutions.add(new GResolution(2,"combobox.item.resolution-2"));
			theResolutions.add(new GResolution(4,"combobox.item.resolution-4"));
			fieldGraphicsResolution = new JComboBox();
			label.setLabelFor(fieldGraphicsResolution);
			top.add(label);
			top.add(fieldGraphicsResolution);
			label = new JLabel(System.getProperty("argo.ext.dir"));
			JTextField j2 = new JTextField();
			fieldArgoExtDir = j2;
			fieldArgoExtDir.setEnabled(false);
			label.setLabelFor(fieldArgoExtDir);
			top.add(label);
			top.add(fieldArgoExtDir);
			label = new JLabel("on");
			JTextField j3 = new JTextField();
			fieldJavaHome = j3;
			fieldJavaHome.setEnabled(false);
			label.setLabelFor(fieldJavaHome);
			top.add(label);
			top.add(fieldJavaHome);
			label = new JLabel("on");
			JTextField j4 = new JTextField();
			fieldUserHome = j4;
			fieldUserHome.setEnabled(false);
			label.setLabelFor(fieldUserHome);
			top.add(label);
			top.add(fieldUserHome);
			label = new JLabel("on");
			JTextField j5 = new JTextField();
			fieldUserDir = j5;
			fieldUserDir.setEnabled(false);
			label.setLabelFor(fieldUserDir);
			top.add(label);
			top.add(fieldUserDir);
			label = new JLabel(Translator.localize("label.startup-directory"));
			JTextField j6 = new JTextField();
			fieldStartupDir = j6;
			fieldStartupDir.setEnabled(false);
			label.setLabelFor(fieldStartupDir);
			top.add(label);
			top.add(fieldStartupDir);
			top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			add(top,BorderLayout.NORTH);
			JPanel bottom = new JPanel();
			bottom.add(new JLabel(Translator.localize("label.graphics-export-resolution.warning")));
			add(bottom,BorderLayout.SOUTH);
		}
	public void handleSettingsTabRefresh() {
		fieldArgoExtDir.setText(System.getProperty("argo.ext.dir"));
		fieldJavaHome.setText(System.getProperty("java.home"));
		fieldUserHome.setText(System.getProperty("user.home"));
		fieldUserDir.setText(Configuration.getString(Argo.KEY_STARTUP_DIR,System.getProperty("user.dir")));
		fieldStartupDir.setText(Argo.getDirectory());
		fieldGraphicsFormat.removeAllItems();
		Collection c = SaveGraphicsManager.getInstance().getSettingsList();
		fieldGraphicsFormat.setModel(new DefaultComboBoxModel(c.toArray()));
		fieldGraphicsResolution.removeAllItems();
		fieldGraphicsResolution.setModel(new DefaultComboBoxModel(theResolutions.toArray()));
		int defaultResolution = Configuration.getInteger(SaveGraphicsManager.KEY_GRAPHICS_RESOLUTION,1);
		for (GResolution gr:theResolutions) {
			if (defaultResolution == gr.getResolution()) {
				fieldGraphicsResolution.setSelectedItem(gr);
				break;
			}
		}
	}
	public void handleSettingsTabSave() {
		Configuration.setString(Argo.KEY_STARTUP_DIR,fieldUserDir.getText());
		GResolution r = (GResolution) fieldGraphicsResolution.getSelectedItem();
		Configuration.setInteger(SaveGraphicsManager.KEY_GRAPHICS_RESOLUTION,r.getResolution());
		SaveGraphicsManager.getInstance().setDefaultFilter((SuffixFilter) fieldGraphicsFormat.getSelectedItem());
	}
	public void handleSettingsTabCancel() {
		handleSettingsTabRefresh();
	}
	public void handleResetToDefault() {
	}
	public String getTabKey() {
		return"tab.environment";
	}
	public JPanel getTabPanel() {
		return this;
	}
	private static final long serialVersionUID = 543442930918741133l;
}

class GResolution {
	private int resolution;
	private String label;
	GResolution(int r,String name) {
			resolution = r;
			label = Translator.localize(name);
		}
	int getResolution() {
		return resolution;
	}
	public String toString() {
		return label;
	}
}



