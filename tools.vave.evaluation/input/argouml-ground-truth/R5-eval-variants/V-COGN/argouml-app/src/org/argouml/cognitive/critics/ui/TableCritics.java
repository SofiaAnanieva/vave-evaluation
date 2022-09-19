package org.argouml.cognitive.critics.ui;

import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.argouml.cognitive.Critic;


class TableCritics extends JTable {
	private boolean initialised;
	private static final String DESC_WIDTH_TEXT = "This is Sample Text for determining Column Width";
	public TableCritics(TableModel model,ListSelectionListener lsl,TableModelListener tml) {
		super(model);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setShowVerticalLines(false);
		getSelectionModel().addListSelectionListener(lsl);
		getModel().addTableModelListener(tml);
		setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		initialised = true;
		setColumnWidths();
	}
	private void setColumnWidths() {
		if (!initialised) {
			return;
		}
		TableColumn checkCol = getColumnModel().getColumn(0);
		TableColumn descCol = getColumnModel().getColumn(1);
		TableColumn actCol = getColumnModel().getColumn(2);
		checkCol.setMinWidth(35);
		checkCol.setMaxWidth(35);
		checkCol.setWidth(30);
		int descWidth = getFontMetrics(getFont()).stringWidth(DESC_WIDTH_TEXT);
		descCol.setMinWidth(descWidth);
		descCol.setWidth(descWidth);
		actCol.setMinWidth(50);
		actCol.setMaxWidth(55);
		actCol.setWidth(55);
		if (getColumnModel().getColumnCount() > 3) {
			descCol.setMinWidth(descWidth / 2);
			TableColumn prioCol = getColumnModel().getColumn(3);
			prioCol.setMinWidth(45);
			prioCol.setMaxWidth(50);
			prioCol.setWidth(50);
		}
	}
	public Critic getCriticAtRow(int row) {
		TableModelCritics model = (TableModelCritics) getModel();
		return model.getCriticAtRow(row);
	}
	public Dimension getInitialSize() {
		return new Dimension(getColumnModel().getTotalColumnWidth() + 20,0);
	}
	public void setAdvanced(boolean mode) {
		TableModelCritics model = (TableModelCritics) getModel();
		model.setAdvanced(mode);
	}
	@Override public void tableChanged(TableModelEvent e) {
		super.tableChanged(e);
		setColumnWidths();
	}
}



