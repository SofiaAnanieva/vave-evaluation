package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Collection;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.swingext.UpArrowIcon;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.TabModelTarget;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewTagDefinition;
import org.argouml.uml.ui.foundation.extension_mechanisms.UMLTagDefinitionComboBoxModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.UndoableAction;
import org.tigris.toolbar.ToolBar;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLListCellRenderer2;


public class TabTaggedValues extends AbstractArgoJPanel implements TabModelTarget,ListSelectionListener,ComponentListener {
	private static final long serialVersionUID = -8566948113385239423l;
	private Object target;
	private boolean shouldBeEnabled = false;
	private JTable table = new JTable(10,2);
	private JLabel titleLabel;
	private JToolBar buttonPanel;
	private UMLComboBox2 tagDefinitionsComboBox;
	private UMLComboBoxModel2 tagDefinitionsComboBoxModel;
	public TabTaggedValues() {
		super("tab.tagged-values");
		setIcon(new UpArrowIcon());
		buttonPanel = new ToolBar();
		buttonPanel.setName(getTitle());
		buttonPanel.setFloatable(false);
		JButton b = new JButton();
		buttonPanel.add(b);
		b.setAction(new ActionNewTagDefinition());
		b.setText("");
		b.setFocusable(false);
		b = new JButton();
		buttonPanel.add(b);
		b.setToolTipText(Translator.localize("button.delete"));
		b.setAction(new ActionRemoveTaggedValue(table));
		b.setText("");
		b.setFocusable(false);
		table.setModel(new TabTaggedValuesModel());
		table.setRowSelectionAllowed(false);
		tagDefinitionsComboBoxModel = new UMLTagDefinitionComboBoxModel();
		tagDefinitionsComboBox = new UMLComboBox2(tagDefinitionsComboBoxModel);
		Class tagDefinitionClass = (Class) Model.getMetaTypes().getTagDefinition();
		tagDefinitionsComboBox.setRenderer(new UMLListCellRenderer2(false));
		table.setDefaultEditor(tagDefinitionClass,new DefaultCellEditor(tagDefinitionsComboBox));
		table.setDefaultRenderer(tagDefinitionClass,new UMLTableCellRenderer());
		table.getSelectionModel().addListSelectionListener(this);
		JScrollPane sp = new JScrollPane(table);
		Font labelFont = LookAndFeelMgr.getInstance().getStandardFont();
		table.setFont(labelFont);
		titleLabel = new JLabel("none");
		resizeColumns();
		setLayout(new BorderLayout());
		titleLabel.setLabelFor(buttonPanel);
		JPanel topPane = new JPanel(new BorderLayout());
		topPane.add(titleLabel,BorderLayout.WEST);
		topPane.add(buttonPanel,BorderLayout.CENTER);
		add(topPane,BorderLayout.NORTH);
		add(sp,BorderLayout.CENTER);
		addComponentListener(this);
	}
	public void resizeColumns() {
		TableColumn keyCol = table.getColumnModel().getColumn(0);
		TableColumn valCol = table.getColumnModel().getColumn(1);
		keyCol.setMinWidth(50);
		keyCol.setWidth(150);
		keyCol.setPreferredWidth(150);
		valCol.setMinWidth(250);
		valCol.setWidth(550);
		valCol.setPreferredWidth(550);
		table.doLayout();
	}
	public void setTarget(Object theTarget) {
		stopEditing();
		Object t = (theTarget instanceof Fig)?((Fig) theTarget).getOwner():theTarget;
		if (!(Model.getFacade().isAModelElement(t))) {
			target = null;
			shouldBeEnabled = false;
			return;
		}
		target = t;
		shouldBeEnabled = true;
		if (isVisible()) {
			setTargetInternal(target);
		}
	}
	private void stopEditing() {
		if (table.isEditing()) {
			TableCellEditor ce = table.getCellEditor();
			try {
				if (ce != null&&!ce.stopCellEditing()) {
					ce.cancelCellEditing();
				}
			}catch (InvalidElementException e) {
			}
		}
	}
	private void setTargetInternal(Object t) {
		tagDefinitionsComboBoxModel.setTarget(t);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		((TabTaggedValuesModel) table.getModel()).setTarget(t);
		table.sizeColumnsToFit(0);
		if (t != null) {
			titleLabel.setText("Target: " + Model.getFacade().getUMLClassName(t) + " (" + Model.getFacade().getName(t) + ")");
		}else {
			titleLabel.setText("none");
		}
		validate();
	}
	public Object getTarget() {
		return target;
	}
	public void refresh() {
		setTarget(target);
	}
	public boolean shouldBeEnabled(Object theTarget) {
		Object t = (theTarget instanceof Fig)?((Fig) theTarget).getOwner():theTarget;
		if (!(Model.getFacade().isAModelElement(t))) {
			shouldBeEnabled = false;
			return shouldBeEnabled;
		}
		shouldBeEnabled = true;
		return true;
	}
	public void targetAdded(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetRemoved(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetSet(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	protected TabTaggedValuesModel getTableModel() {
		return(TabTaggedValuesModel) table.getModel();
	}
	protected JTable getTable() {
		return table;
	}
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			DefaultListSelectionModel sel = (DefaultListSelectionModel) e.getSource();
			Collection tvs = Model.getFacade().getTaggedValuesCollection(target);
			int index = sel.getLeadSelectionIndex();
			if (index >= 0&&index < tvs.size()) {
				Object tagDef = Model.getFacade().getTagDefinition(TabTaggedValuesModel.getFromCollection(tvs,index));
				tagDefinitionsComboBoxModel.setSelectedItem(tagDef);
			}
		}
	}
	public void componentShown(ComponentEvent e) {
		setTargetInternal(target);
	}
	public void componentHidden(ComponentEvent e) {
		stopEditing();
		setTargetInternal(null);
	}
	public void componentMoved(ComponentEvent e) {
	}
	public void componentResized(ComponentEvent e) {
	}
}

class ActionRemoveTaggedValue extends UndoableAction {
	private static final long serialVersionUID = 8276763533039642549l;
	private JTable table;
	public ActionRemoveTaggedValue(JTable tableTv) {
		super(Translator.localize("button.delete"),ResourceLoaderWrapper.lookupIcon("Delete"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("button.delete"));
		table = tableTv;
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		TabTaggedValuesModel model = (TabTaggedValuesModel) table.getModel();
		model.removeRow(table.getSelectedRow());
	}
}



