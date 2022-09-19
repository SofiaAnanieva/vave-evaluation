package org.argouml.ui.cmd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.i18n.Translator;
import org.argouml.util.KeyEventUtils;


class SettingsTabShortcuts extends JPanel implements GUISettingsTabInterface,ActionListener,ListSelectionListener,ShortcutChangedListener {
	private static final long serialVersionUID = -2033414439459450620l;
	private static final String NONE_NAME = Translator.localize("label.shortcut-none");
	private static final String DEFAULT_NAME = Translator.localize("label.shortcut-default");
	private static final String CUSTOM_NAME = Translator.localize("label.shortcut-custom");
	private JTable table;
	private JPanel selectedContainer;
	private ShortcutField shortcutField = new ShortcutField("",12);
	private Color shortcutFieldDefaultBg = null;
	private JRadioButton customButton = new JRadioButton(CUSTOM_NAME);
	private JRadioButton defaultButton = new JRadioButton(DEFAULT_NAME);
	private JRadioButton noneButton = new JRadioButton(NONE_NAME);
	private JLabel warningLabel = new JLabel(" ");
	private ActionWrapper target;
	private ActionWrapper[]actions = ShortcutMgr.getShortcuts();
	private int lastRowSelected = -1;
	private final String[]columnNames =  {Translator.localize("misc.column-name.action"),Translator.localize("misc.column-name.shortcut"),Translator.localize("misc.column-name.default")};
	private Object[][]elements;
	public String getTabKey() {
		return"tab.shortcuts";
	}
	public JPanel getTabPanel() {
		if (table == null) {
			setLayout(new GridBagLayout());
			GridBagConstraints panelConstraints = new GridBagConstraints();
			panelConstraints.gridx = 0;
			panelConstraints.gridy = 0;
			panelConstraints.anchor = GridBagConstraints.NORTH;
			panelConstraints.fill = GridBagConstraints.BOTH;
			panelConstraints.weightx = 5;
			panelConstraints.weighty = 15;
			table = new JTable(new ShortcutTableModel());
			table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			table.setShowVerticalLines(true);
			table.setDefaultRenderer(KeyStroke.class,new KeyStrokeCellRenderer());
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.getSelectionModel().addListSelectionListener(this);
			JPanel tableContainer = new JPanel(new BorderLayout());
			tableContainer.setBorder(BorderFactory.createTitledBorder(Translator.localize("dialog.shortcut.titled-border.actions")));
			tableContainer.add(new JScrollPane(table));
			add(tableContainer,panelConstraints);
			customButton.addActionListener(this);
			defaultButton.addActionListener(this);
			noneButton.addActionListener(this);
			selectedContainer = new JPanel(new GridBagLayout());
			selectedContainer.setBorder(BorderFactory.createTitledBorder(Translator.localize("dialog.shortcut.titled-border.selected")));
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.insets = new Insets(0,5,10,0);
			noneButton.setActionCommand(NONE_NAME);
			defaultButton.setActionCommand(DEFAULT_NAME);
			customButton.setActionCommand(CUSTOM_NAME);
			noneButton.addActionListener(this);
			defaultButton.addActionListener(this);
			customButton.addActionListener(this);
			ButtonGroup radioButtonGroup = new ButtonGroup();
			radioButtonGroup.add(noneButton);
			radioButtonGroup.add(defaultButton);
			radioButtonGroup.add(customButton);
			selectedContainer.add(noneButton,constraints);
			constraints.gridx = 1;
			constraints.insets = new Insets(0,5,10,0);
			selectedContainer.add(defaultButton,constraints);
			constraints.gridx = 2;
			constraints.insets = new Insets(0,5,10,0);
			selectedContainer.add(customButton,constraints);
			constraints.gridx = 3;
			constraints.weightx = 10.0;
			constraints.insets = new Insets(0,10,10,15);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			shortcutField.addShortcutChangedListener(this);
			shortcutFieldDefaultBg = shortcutField.getBackground();
			selectedContainer.add(shortcutField,constraints);
			constraints.gridwidth = 4;
			constraints.gridy = 1;
			constraints.gridx = 0;
			constraints.anchor = GridBagConstraints.WEST;
			constraints.insets = new Insets(0,10,5,10);
			warningLabel.setForeground(Color.RED);
			selectedContainer.add(warningLabel,constraints);
			panelConstraints.gridy = 1;
			panelConstraints.anchor = GridBagConstraints.CENTER;
			panelConstraints.fill = GridBagConstraints.BOTH;
			panelConstraints.weightx = 1;
			panelConstraints.weighty = 1;
			add(selectedContainer,panelConstraints);
			JLabel restart = new JLabel(Translator.localize("label.restart-application"));
			restart.setHorizontalAlignment(SwingConstants.CENTER);
			restart.setVerticalAlignment(SwingConstants.CENTER);
			restart.setBorder(BorderFactory.createEmptyBorder(10,2,10,2));
			panelConstraints.gridy = 2;
			panelConstraints.anchor = GridBagConstraints.CENTER;
			panelConstraints.fill = GridBagConstraints.BOTH;
			panelConstraints.weightx = 1;
			panelConstraints.weighty = 0;
			add(restart,panelConstraints);
			this.enableFields(false);
		}
		return this;
	}
	private void enableFields(boolean enable) {
		shortcutField.setEditable(enable);
		customButton.setEnabled(enable);
		defaultButton.setEnabled(enable);
		noneButton.setEnabled(enable);
	}
	private void setTarget(Object t) {
		target = (ActionWrapper) t;
		enableFields(true);
		selectedContainer.setBorder(BorderFactory.createTitledBorder(Translator.localize("dialog.shortcut.titled-border.selected-partial") + " \"" + target.getActionName() + "\""));
		shortcutField.setText(KeyEventUtils.formatKeyStroke(target.getCurrentShortcut()));
		resetKeyStrokeConflict();
		if (target.getCurrentShortcut() == null) {
			noneButton.setSelected(true);
			shortcutField.setEnabled(false);
		}else if (target.getDefaultShortcut() != null&&target.getCurrentShortcut().equals(target.getDefaultShortcut())) {
			defaultButton.setSelected(true);
			shortcutField.setEnabled(false);
		}else {
			customButton.setSelected(true);
			shortcutField.setEnabled(true);
			shortcutField.requestFocus();
		}
	}
	public void handleResetToDefault() {
	}
	public void handleSettingsTabCancel() {
	}
	public void handleSettingsTabRefresh() {
		actions = ShortcutMgr.getShortcuts();
		table.setModel(new ShortcutTableModel());
	}
	public void handleSettingsTabSave() {
		if (getActionAlreadyAssigned(ShortcutMgr.decodeKeyStroke(shortcutField.getText())) != null) {
			JOptionPane.showMessageDialog(this,Translator.localize("optionpane.shortcut-save-conflict"),Translator.localize("optionpane.shortcut-save-conflict-title"),JOptionPane.WARNING_MESSAGE);
		}else {
			ShortcutMgr.saveShortcuts(actions);
		}
	}
	public void valueChanged(ListSelectionEvent lse) {
		if (lse.getValueIsAdjusting()) {
			return;
		}
		Object src = lse.getSource();
		if (src != table.getSelectionModel()||table.getSelectedRow() == -1) {
			return;
		}
		if (!noneButton.isSelected()) {
			ActionWrapper oldAction = getActionAlreadyAssigned(ShortcutMgr.decodeKeyStroke(shortcutField.getText()));
			if (oldAction != null) {
				String t = MessageFormat.format(Translator.localize("optionpane.conflict-shortcut"),new Object[] {shortcutField.getText(),oldAction.getActionName()});
				int response = JOptionPane.showConfirmDialog(this,t,t,JOptionPane.YES_NO_OPTION);
				switch (response) {case JOptionPane.YES_OPTION:
					oldAction.setCurrentShortcut(null);
					table.setValueAt(oldAction,-1,-1);
					break;
				case JOptionPane.NO_OPTION:
					table.getSelectionModel().removeListSelectionListener(this);
					table.getSelectionModel().setSelectionInterval(lastRowSelected,lastRowSelected);
					table.getSelectionModel().addListSelectionListener(this);
					return;
				}
			}
		}
		setTarget(actions[table.getSelectedRow()]);
		lastRowSelected = table.getSelectedRow();
	}
	public ActionWrapper getActionAlreadyAssigned(KeyStroke keyStroke) {
		for (int i = 0;i < actions.length;i++) {
			if (actions[i].getCurrentShortcut() != null&&actions[i].getCurrentShortcut().equals(keyStroke)&&!actions[i].getActionName().equals(target.getActionName())) {
				return actions[i];
			}
		}
		KeyStroke duplicate = ShortcutMgr.getDuplicate(keyStroke);
		if (duplicate != null) {
			for (int i = 0;i < actions.length;i++) {
				if (actions[i].getCurrentShortcut() != null&&actions[i].getCurrentShortcut().equals(duplicate)&&!actions[i].getActionName().equals(target.getActionName())) {
					return actions[i];
				}
			}
		}
		return null;
	}
	public void actionPerformed(ActionEvent e) {
		resetKeyStrokeConflict();
		if (e.getSource() == customButton) {
			setKeyStrokeValue(ShortcutMgr.decodeKeyStroke(shortcutField.getText()));
			shortcutField.setEnabled(true);
			shortcutField.requestFocus();
		}else if (e.getSource() == defaultButton) {
			setKeyStrokeValue(target.getDefaultShortcut());
			shortcutField.setEnabled(false);
			checkShortcutAlreadyAssigned(target.getDefaultShortcut());
		}else if (e.getSource() == noneButton) {
			setKeyStrokeValue(null);
			shortcutField.setEnabled(false);
		}
	}
	private void resetKeyStrokeConflict() {
		this.warningLabel.setText(" ");
		this.shortcutField.setBackground(shortcutFieldDefaultBg);
	}
	private void setKeyStrokeValue(KeyStroke newKeyStroke) {
		String formattedKeyStroke = KeyEventUtils.formatKeyStroke(newKeyStroke);
		shortcutField.setText(formattedKeyStroke);
		table.getModel().setValueAt(newKeyStroke,table.getSelectedRow(),1);
		actions[table.getSelectedRow()].setCurrentShortcut(newKeyStroke);
		table.repaint();
	}
	public void shortcutChange(ShortcutChangedEvent event) {
		checkShortcutAlreadyAssigned(event.getKeyStroke());
		setKeyStrokeValue(event.getKeyStroke());
		this.selectedContainer.repaint();
	}
	private void checkShortcutAlreadyAssigned(KeyStroke newKeyStroke) {
		ActionWrapper oldAction = getActionAlreadyAssigned(newKeyStroke);
		if (oldAction != null) {
			this.shortcutField.setBackground(Color.YELLOW);
			this.warningLabel.setText(MessageFormat.format(Translator.localize("misc.shortcuts.conflict"),new Object[] {KeyEventUtils.formatKeyStroke(oldAction.getCurrentShortcut()),oldAction.getActionName()}));
		}else {
			resetKeyStrokeConflict();
		}
	}
	class ShortcutTableModel extends AbstractTableModel {
		public ShortcutTableModel() {
			elements = new Object[actions.length][3];
			for (int i = 0;i < elements.;i++) {
				ActionWrapper currentAction = actions[i];
				elements[i][0] = currentAction.getActionName();
				elements[i][1] = currentAction.getCurrentShortcut();
				elements[i][2] = currentAction.getDefaultShortcut();
			}
		}
		public int getColumnCount() {
			return columnNames.;
		}
		@Override public String getColumnName(int col) {
			return columnNames[col];
		}
		public int getRowCount() {
			return elements.;
		}
		public Object getValueAt(int row,int col) {
			return elements[row][col];
		}
		@Override public void setValueAt(Object ob,int row,int col) {
			if (ob instanceof ActionWrapper) {
				ActionWrapper newValueAction = (ActionWrapper) ob;
				for (int i = 0;i < elements.;i++) {
					if (elements[i][0].equals(newValueAction.getActionName())) {
						elements[i][1] = newValueAction.getCurrentShortcut();
						repaint();
						break;
					}
				}
				for (int i = 0;i < actions.length;i++) {
					if (actions[i].getKey().equals(newValueAction.getKey())) {
						actions[i].setCurrentShortcut(newValueAction.getCurrentShortcut());
						break;
					}
				}
			}else {
				elements[row][col] = ob;
			}
		}
		@Override public Class<?>getColumnClass(int col) {
			switch (col) {case 0:
				return String.class;
			case 1:
				return KeyStroke.class;
			case 2:
				return KeyStroke.class;
			default:
				return null;
			}
		}
		@Override public boolean isCellEditable(int row,int col) {
			return false;
		}
	}
}

class KeyStrokeCellRenderer extends DefaultTableCellRenderer {
	public KeyStrokeCellRenderer() {
		super();
		setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	}
	@Override public void setValue(Object value) {
		if (value != null&&value instanceof KeyStroke) {
			value = KeyEventUtils.formatKeyStroke((KeyStroke) value);
		}
		super.setValue(value);
	}
}



