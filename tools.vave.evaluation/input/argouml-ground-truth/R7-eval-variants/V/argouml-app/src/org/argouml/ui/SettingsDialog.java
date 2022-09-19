package org.argouml.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.util.ArgoDialog;
import org.argouml.ui.GUI;


class SettingsDialog extends ArgoDialog implements WindowListener {
	private JButton applyButton;
	private JTabbedPane tabs;
	private boolean windowOpen;
	SettingsDialog() {
			super(Translator.localize("dialog.settings"),ArgoDialog.OK_CANCEL_OPTION,true);
			tabs = new JTabbedPane();
			applyButton = new JButton(Translator.localize("button.apply"));
			String mnemonic = Translator.localize("button.apply.mnemonic");
			if (mnemonic != null&&mnemonic.length() > 0) {
				applyButton.setMnemonic(mnemonic.charAt(0));
			}
			applyButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					handleSave();
				}
			});
			addButton(applyButton);
			settingsTabs = GUI.getInstance().getSettingsTabs();
			for (GUISettingsTabInterface stp:settingsTabs) {
				tabs.addTab(Translator.localize(stp.getTabKey()),stp.getTabPanel());
			}
			final int minimumWidth = 480;
			tabs.setPreferredSize(new Dimension(Math.max(tabs.getPreferredSize().width,minimumWidth),tabs.getPreferredSize().height));
			tabs.setTabPlacement(SwingConstants.LEFT);
			setContent(tabs);
			addWindowListener(this);
		}
	@Override public void setVisible(boolean show) {
		if (show) {
			handleRefresh();
			toFront();
		}
		super.setVisible(show);
	}
	public void actionPerformed(ActionEvent ev) {
		super.actionPerformed(ev);
		if (ev.getSource() == getOkButton()) {
			handleSave();
		}else if (ev.getSource() == getCancelButton()) {
			handleCancel();
		}
	}
	private void handleSave() {
		for (GUISettingsTabInterface tab:settingsTabs) {
			tab.handleSettingsTabSave();
		}
		windowOpen = false;
		Configuration.save();
	}
	private void handleCancel() {
		for (GUISettingsTabInterface tab:settingsTabs) {
			tab.handleSettingsTabCancel();
		}
		windowOpen = false;
	}
	private void handleRefresh() {
		for (GUISettingsTabInterface tab:settingsTabs) {
			tab.handleSettingsTabRefresh();
		}
	}
	private void handleOpen() {
		if (!windowOpen) {
			getOkButton().requestFocusInWindow();
			windowOpen = true;
		}
	}
	public void windowActivated(WindowEvent e) {
		handleOpen();
	}
	public void windowClosed(WindowEvent e) {
	}
	public void windowDeactivated(WindowEvent e) {
	}
	public void windowDeiconified(WindowEvent e) {
	}
	public void windowIconified(WindowEvent e) {
	}
	public void windowOpened(WindowEvent e) {
		handleOpen();
	}
	public void windowClosing(WindowEvent e) {
		handleCancel();
	}
	private static final long serialVersionUID = -8233301947357843703l;
	private List<GUISettingsTabInterface>settingsTabs;
}



