package org.argouml.ui;

import javax.swing.table.AbstractTableModel;


public class CheckboxTableModel extends AbstractTableModel {
	public CheckboxTableModel(Object[]labels,Object[]data,String colName1,String colName2) {
		elements = new Object[labels.][3];
		for (int i = 0;i < elements.;i++) {
			elements[i][0] = labels[i];
			elements[i][1] = Boolean.TRUE;
			if (data != null&&i < data.) {
				elements[i][2] = data[i];
			}else {
				elements[i][2] = null;
			}
		}
		columnName1 = colName1;
		columnName2 = colName2;
	}
	public int getColumnCount() {
		return 2;
	}
	public String getColumnName(int col) {
		if (col == 0) {
			return columnName1;
		}else if (col == 1) {
			return columnName2;
		}
		return null;
	}
	public int getRowCount() {
		return elements.;
	}
	public Object getValueAt(int row,int col) {
		if (row < elements.&&col < 3) {
			return elements[row][col];
		}else {
			throw new IllegalArgumentException("Index out of bounds");
		}
	}
	public void setValueAt(Object ob,int row,int col) {
		elements[row][col] = ob;
	}
	public Class getColumnClass(int col) {
		if (col == 0) {
			return String.class;
		}else if (col == 1) {
			return Boolean.class;
		}else if (col == 2) {
			return Object.class;
		}
		return null;
	}
	public boolean isCellEditable(int row,int col) {
		return col == 1&&row < elements.;
	}
	private Object[][]elements;
	private String columnName1,columnName2;
	private static final long serialVersionUID = 111532940880908401l;
}



