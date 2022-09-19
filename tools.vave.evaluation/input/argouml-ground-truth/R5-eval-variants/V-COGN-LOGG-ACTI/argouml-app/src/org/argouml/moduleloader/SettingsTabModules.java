package org.argouml.moduleloader;

import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.i18n.Translator;
import org.tigris.swidgets.LabelledLayout;


class SettingsTabModules extends JPanel implements GUISettingsTabInterface {
	private JTable table;
	private JTextField fieldAllExtDirs;
	private String[]columnNames =  {Translator.localize("misc.column-name.module"),Translator.localize("misc.column-name.enabled")};
	private Object[][]elements;
	SettingsTabModules() {
		}
	class ModuleTableModel extends AbstractTableModel {
		public ModuleTableModel() {
			Object[]arr = ModuleLoader2.allModules().toArray();
			elements = new Object[arr.][2];
			for (int i = 0;i < elements.;i++) {
				elements[i][0] = arr[i];
				elements[i][1] = Boolean.valueOf(ModuleLoader2.isSelected((String) arr[i]));
			}
		}
		public int getColumnCount() {
			return columnNames.;
		}
		public String getColumnName(int col) {
			return columnNames[col];
		}
		public int getRowCount() {
			return elements.;
		}
		public Object getValueAt(int row,int col) {
			if (row < elements.) {
				return elements[row][col];
			}else {
				return null;
			}
		}
		public void setValueAt(Object ob,int row,int col) {
			elements[row][col] = ob;
		}
		public Class getColumnClass(int col) {
			switch (col) {case 0:
				return String.class;
			case 1:
				return Boolean.class;
			default:
				return null;
			}
		}
		public boolean isCellEditable(int row,int col) {
			return col >= 1&&row < elements.;
		}
		private static final long serialVersionUID = -5970280716477119863l;
	}
	public void handleSettingsTabRefresh() {
		table.setModel(new ModuleTableModel());
		StringBuffer sb = new StringBuffer();
		List locations = ModuleLoader2.getInstance().getExtensionLocations();
		for (Iterator it = locations.iterator();it.hasNext();) {
			sb.append((String) it.next());
			sb.append("\n");
		}
		fieldAllExtDirs.setText(sb.substring(0,sb.length() - 1).toString());
	}
	public void handleSettingsTabSave() {
		if (elements != null) {
			for (int i = 0;i < elements.;i++) {
				ModuleLoader2.setSelected((String) elements[i][0],((Boolean) elements[i][1]).booleanValue());
			}
			ModuleLoader2.doLoad(false);
		}
	}
	public void handleSettingsTabCancel() {
	}
	public String getTabKey() {
		return"tab.modules";
	}
	public JPanel getTabPanel() {
		if (table == null) {
			setLayout(new BorderLayout());
			table = new JTable(new ModuleTableModel());
			table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			table.setShowVerticalLines(true);
			add(new JScrollPane(table),BorderLayout.CENTER);
			int labelGap = 10;
			int componentGap = 5;
			JPanel top = new JPanel(new LabelledLayout(labelGap,componentGap));
			JLabel label = new JLabel(Translator.localize("label.extension-directories"));
			JTextField j = new JTextField();
			fieldAllExtDirs = j;
			fieldAllExtDirs.setEnabled(false);
			label.setLabelFor(fieldAllExtDirs);
			top.add(label);
			top.add(fieldAllExtDirs);
			add(top,BorderLayout.NORTH);
		}
		return this;
	}
	private static final long serialVersionUID = 8945027241102020504l;
	public void handleResetToDefault() {
	}
}



