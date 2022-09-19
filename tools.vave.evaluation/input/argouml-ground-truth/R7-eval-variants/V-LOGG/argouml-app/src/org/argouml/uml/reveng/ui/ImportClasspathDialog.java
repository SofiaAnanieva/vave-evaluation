package org.argouml.uml.reveng.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.reveng.SettingsTypes.PathListSelection;
import org.tigris.gef.base.Globals;


public class ImportClasspathDialog extends JPanel {
	private JList paths;
	private DefaultListModel pathsModel;
	private JButton addButton;
	private JButton removeButton;
	private JFileChooser chooser;
	private PathListSelection setting;
	public ImportClasspathDialog(PathListSelection pathListSetting) {
		super();
		setting = pathListSetting;
		setToolTipText(setting.getDescription());
		setLayout(new BorderLayout(0,0));
		JLabel label = new JLabel(setting.getLabel());
		add(label,BorderLayout.NORTH);
		pathsModel = new DefaultListModel();
		for (String path:setting.getDefaultPathList()) {
			pathsModel.addElement(path);
		}
		paths = new JList(pathsModel);
		paths.setVisibleRowCount(5);
		paths.setToolTipText(setting.getDescription());
		JScrollPane listScroller = new JScrollPane(paths);
		add(listScroller,BorderLayout.CENTER);
		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new GridLayout(0,2,50,0));
		addButton = new JButton(Translator.localize("button.add"));
		controlsPanel.add(addButton);
		addButton.addActionListener(new AddListener());
		removeButton = new JButton(Translator.localize("button.remove"));
		controlsPanel.add(removeButton);
		removeButton.addActionListener(new RemoveListener());
		add(controlsPanel,BorderLayout.SOUTH);
	}
	private void updatePathList() {
		List<String>pathList = new ArrayList<String>();
		for (int i = 0;i < pathsModel.size();i++) {
			String path = (String) pathsModel.getElementAt(i);
			pathList.add(path);
		}
		setting.setPathList(pathList);
	}
	class RemoveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int index = paths.getSelectedIndex();
			if (index < 0) {
				return;
			}
			pathsModel.remove(index);
			updatePathList();
			int size = pathsModel.getSize();
			if (size == 0) {
				removeButton.setEnabled(false);
			}else {
				if (index == pathsModel.getSize()) {
					index--;
				}
				paths.setSelectedIndex(index);
				paths.ensureIndexIsVisible(index);
			}
		}
	}
	class AddListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (chooser == null) {
				chooser = new JFileChooser(Globals.getLastDirectory());
				if (chooser == null) {
					chooser = new JFileChooser();
				}
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setMultiSelectionEnabled(true);
				chooser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e1) {
						if (e1.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
							File[]files = chooser.getSelectedFiles();
							for (File theFile:files) {
								if (theFile != null) {
									pathsModel.addElement(theFile.toString());
								}
							}
							updatePathList();
						}else if (e1.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
						}
					}
				});
			}
			chooser.showOpenDialog(new Frame());
		}
	}
}



