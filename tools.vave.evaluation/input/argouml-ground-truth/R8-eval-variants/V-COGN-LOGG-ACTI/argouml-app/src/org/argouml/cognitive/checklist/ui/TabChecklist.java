package org.argouml.cognitive.checklist.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import org.apache.log4j.Logger;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.cognitive.Translator;
import org.argouml.cognitive.checklist.CheckItem;
import org.argouml.cognitive.checklist.CheckManager;
import org.argouml.cognitive.checklist.Checklist;
import org.argouml.cognitive.checklist.ChecklistStatus;
import org.argouml.model.Model;
import org.argouml.swingext.UpArrowIcon;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.TabModelTarget;
import org.argouml.ui.targetmanager.TargetEvent;
import org.tigris.gef.presentation.Fig;


public class TabChecklist extends AbstractArgoJPanel implements TabModelTarget,ActionListener,ListSelectionListener,ComponentListener {
	private Object target;
	private TableModelChecklist tableModel = null;
	private boolean shouldBeEnabled = false;
	private JTable table = new JTable(10,2);
	public TabChecklist() {
		super("tab.checklist");
		setIcon(new UpArrowIcon());
		tableModel = new TableModelChecklist(this);
		table.setModel(tableModel);
		Font labelFont = LookAndFeelMgr.getInstance().getStandardFont();
		table.setFont(labelFont);
		table.setIntercellSpacing(new Dimension(0,1));
		table.setShowVerticalLines(false);
		table.getSelectionModel().addListSelectionListener(this);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		TableColumn checkCol = table.getColumnModel().getColumn(0);
		TableColumn descCol = table.getColumnModel().getColumn(1);
		checkCol.setMinWidth(20);
		checkCol.setMaxWidth(30);
		checkCol.setWidth(30);
		descCol.setPreferredWidth(900);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.sizeColumnsToFit(-1);
		JScrollPane sp = new JScrollPane(table);
		setLayout(new BorderLayout());
		add(new JLabel(Translator.localize("tab.checklist.warning")),BorderLayout.NORTH);
		add(sp,BorderLayout.CENTER);
		addComponentListener(this);
	}
	private Object findTarget(Object t) {
		if (t instanceof Fig) {
			Fig f = (Fig) t;
			t = f.getOwner();
		}
		return t;
	}
	public void setTarget(Object t) {
		target = findTarget(t);
		if (target == null) {
			shouldBeEnabled = false;
			return;
		}
		shouldBeEnabled = true;
		if (isVisible()) {
			setTargetInternal(target);
		}
	}
	private void setTargetInternal(Object t) {
		if (t == null) {
			return;
		}
		Checklist cl = CheckManager.getChecklistFor(t);
		if (cl == null) {
			target = null;
			shouldBeEnabled = false;
			return;
		}
		tableModel.setTarget(t);
		resizeColumns();
	}
	public Object getTarget() {
		return target;
	}
	public void refresh() {
		setTarget(target);
	}
	public boolean shouldBeEnabled(Object t) {
		t = findTarget(t);
		if (t == null) {
			shouldBeEnabled = false;
			return shouldBeEnabled;
		}
		shouldBeEnabled = true;
		Checklist cl = CheckManager.getChecklistFor(t);
		if (cl == null) {
			shouldBeEnabled = false;
			return shouldBeEnabled;
		}
		return shouldBeEnabled;
	}
	public void resizeColumns() {
		TableColumn checkCol = table.getColumnModel().getColumn(0);
		TableColumn descCol = table.getColumnModel().getColumn(1);
		checkCol.setMinWidth(20);
		checkCol.setMaxWidth(30);
		checkCol.setWidth(30);
		descCol.setPreferredWidth(900);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.sizeColumnsToFit(0);
		validate();
	}
	public void actionPerformed(ActionEvent ae) {
	}
	public void valueChanged(ListSelectionEvent lse) {
	}
	public void targetAdded(TargetEvent e) {
	}
	public void targetRemoved(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetSet(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void componentShown(ComponentEvent e) {
		setTargetInternal(target);
	}
	public void componentHidden(ComponentEvent e) {
		setTargetInternal(null);
	}
	public void componentMoved(ComponentEvent e) {
	}
	public void componentResized(ComponentEvent e) {
	}
}

class TableModelChecklist extends AbstractTableModel implements VetoableChangeListener,PropertyChangeListener {
	private static final Logger LOG = Logger.getLogger(TableModelChecklist.class);
	private Object target;
	private TabChecklist panel;
	public TableModelChecklist(TabChecklist tc) {
		panel = tc;
	}
	public void setTarget(Object t) {
		if (Model.getFacade().isAElement(target)) {
			Model.getPump().removeModelEventListener(this,target);
		}
		target = t;
		if (Model.getFacade().isAElement(target)) {
			Model.getPump().addModelEventListener(this,target,"name");
		}
		fireTableStructureChanged();
	}
	public int getColumnCount() {
		return 2;
	}
	@Override public String getColumnName(int c) {
		if (c == 0) {
			return"X";
		}
		if (c == 1) {
			return Translator.localize("tab.checklist.description");
		}
		return"XXX";
	}
	public Class getColumnClass(int c) {
		if (c == 0) {
			return Boolean.class;
		}else if (c == 1) {
			return String.class;
		}else {
			return String.class;
		}
	}
	@Override public boolean isCellEditable(int row,int col) {
		return col == 0;
	}
	public int getRowCount() {
		if (target == null) {
			return 0;
		}
		Checklist cl = CheckManager.getChecklistFor(target);
		if (cl == null) {
			return 0;
		}
		return cl.size();
	}
	public Object getValueAt(int row,int col) {
		Checklist cl = CheckManager.getChecklistFor(target);
		if (cl == null) {
			return"no checklist";
		}
		CheckItem ci = cl.get(row);
		if (col == 0) {
			ChecklistStatus stat = CheckManager.getStatusFor(target);
			return(stat.contains(ci))?Boolean.TRUE:Boolean.FALSE;
		}else if (col == 1) {
			return ci.getDescription(target);
		}else {
			return"CL-" + row * 2 + col;
		}
	}
	@Override public void setValueAt(Object aValue,int rowIndex,int columnIndex) {
		LOG.debug("setting table value " + rowIndex + ", " + columnIndex);
		if (columnIndex != 0) {
			return;
		}
		if (!(aValue instanceof Boolean)) {
			return;
		}
		boolean val = ((Boolean) aValue).booleanValue();
		Checklist cl = CheckManager.getChecklistFor(target);
		if (cl == null) {
			return;
		}
		CheckItem ci = cl.get(rowIndex);
		if (columnIndex == 0) {
			ChecklistStatus stat = CheckManager.getStatusFor(target);
			if (val) {
				stat.add(ci);
			}else {
				stat.remove(ci);
			}
		}
	}
	public void vetoableChange(PropertyChangeEvent pce) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				fireTableStructureChanged();
				panel.resizeColumns();
			}
		});
	}
	public void propertyChange(PropertyChangeEvent evt) {
		fireTableStructureChanged();
		panel.resizeColumns();
	}
}



