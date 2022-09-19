package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.uml.util.SortedListModel;
import org.argouml.uml.ui.UMLListCellRenderer2;


public class UMLAddDialog extends JPanel implements ActionListener {
	private JList choicesList = null;
	private JList selectedList = null;
	private JButton addButton = null;
	private JButton removeButton = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JDialog dialog = null;
	private String title = null;
	private boolean multiSelectAllowed = false;
	private int returnValue;
	private boolean exclusive;
	public UMLAddDialog(final List theChoices,final List preselected,final String theTitle,final boolean multiselectAllowed,final boolean isExclusive) {
		this(theChoices,preselected,theTitle,new UMLListCellRenderer2(true),multiselectAllowed,isExclusive);
	}
	public UMLAddDialog(final List theChoices,final List preselected,final String theTitle,final ListCellRenderer renderer,final boolean multiselectAllowed,final boolean isExclusive) {
		multiSelectAllowed = multiselectAllowed;
		if (theChoices == null) {
			throw new IllegalArgumentException("There should always be choices in UMLAddDialog");
		}
		exclusive = isExclusive;
		List choices = new ArrayList(theChoices);
		if (isExclusive&&preselected != null&&!preselected.isEmpty()) {
			choices.removeAll(preselected);
		}
		if (theTitle != null) {
			title = theTitle;
		}else {
			title = "";
		}
		setLayout(new BorderLayout());
		JPanel upperPanel = new JPanel();
		JPanel panelChoices = new JPanel(new BorderLayout());
		JPanel panelSelected = new JPanel(new BorderLayout());
		choicesList = new JList(constructListModel(choices));
		choicesList.setMinimumSize(new Dimension(150,300));
		if (renderer != null) {
			choicesList.setCellRenderer(renderer);
		}
		if (multiselectAllowed) {
			choicesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}else {
			choicesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		choicesList.setVisibleRowCount(15);
		JScrollPane choicesScroll = new JScrollPane(choicesList);
		panelChoices.add(new JLabel(Translator.localize("label.choices")),BorderLayout.NORTH);
		panelChoices.add(choicesScroll,BorderLayout.CENTER);
		addButton = new JButton(ResourceLoaderWrapper.lookupIconResource("NavigateForward"));
		addButton.addActionListener(this);
		removeButton = new JButton(ResourceLoaderWrapper.lookupIconResource("NavigateBack"));
		removeButton.addActionListener(this);
		Box buttonBox = Box.createVerticalBox();
		buttonBox.add(addButton);
		buttonBox.add(Box.createRigidArea(new Dimension(0,5)));
		buttonBox.add(removeButton);
		selectedList = new JList(constructListModel(preselected));
		selectedList.setMinimumSize(new Dimension(150,300));
		if (renderer != null) {
			selectedList.setCellRenderer(renderer);
		}
		selectedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		selectedList.setVisibleRowCount(15);
		JScrollPane selectedScroll = new JScrollPane(selectedList);
		panelSelected.add(new JLabel(Translator.localize("label.selected")),BorderLayout.NORTH);
		panelSelected.add(selectedScroll,BorderLayout.CENTER);
		upperPanel.add(panelChoices);
		upperPanel.add(Box.createRigidArea(new Dimension(5,0)));
		upperPanel.add(buttonBox);
		upperPanel.add(Box.createRigidArea(new Dimension(5,0)));
		upperPanel.add(panelSelected);
		add(upperPanel,BorderLayout.NORTH);
		JPanel okCancelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		okButton = new JButton(Translator.localize("button.ok"));
		okButton.addActionListener(this);
		cancelButton = new JButton(Translator.localize("button.cancel"));
		cancelButton.addActionListener(this);
		okCancelPanel.add(okButton);
		okCancelPanel.add(cancelButton);
		okCancelPanel.setBorder(BorderFactory.createEmptyBorder(10,0,10,10));
		add(okCancelPanel,BorderLayout.SOUTH);
		setBorder(BorderFactory.createEmptyBorder(20,10,20,10));
		update();
	}
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(addButton)) {
			addSelection();
			update();
		}
		if (source.equals(removeButton)) {
			removeSelection();
			update();
		}
		if (source.equals(okButton)) {
			ok();
		}
		if (source.equals(cancelButton)) {
			cancel();
		}
	}
	private void update() {
		if (choicesList.getModel().getSize() == 0) {
			addButton.setEnabled(false);
		}else {
			addButton.setEnabled(true);
		}
		if (selectedList.getModel().getSize() == 0) {
			removeButton.setEnabled(false);
		}else {
			removeButton.setEnabled(true);
		}
		if (selectedList.getModel().getSize() > 1&&!multiSelectAllowed) {
			addButton.setEnabled(false);
			okButton.setEnabled(false);
		}else {
			addButton.setEnabled(true);
			okButton.setEnabled(true);
		}
	}
	protected AbstractListModel constructListModel(List list) {
		SortedListModel model = new SortedListModel();
		if (list != null) {
			model.addAll(list);
		}
		return model;
	}
	public int showDialog(Component parent) {
		Frame frame = parent instanceof Frame?(Frame) parent:(Frame) SwingUtilities.getAncestorOfClass(Frame.class,parent);
		dialog = new JDialog(frame,title,true);
		Container contentPane = dialog.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(this,BorderLayout.CENTER);
		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				cancel();
			}
		});
		dialog.pack();
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
		return returnValue;
	}
	private List getChoices() {
		List result = new ArrayList();
		for (int index:choicesList.getSelectedIndices()) {
			result.add(choicesList.getModel().getElementAt(index));
		}
		return result;
	}
	private List getSelectedChoices() {
		List result = new ArrayList();
		for (int index:selectedList.getSelectedIndices()) {
			result.add(selectedList.getModel().getElementAt(index));
		}
		return result;
	}
	public Vector getSelected() {
		Vector result = new Vector();
		ListModel list = selectedList.getModel();
		for (int i = 0;i < list.getSize();i++) {
			result.add(list.getElementAt(i));
		}
		return result;
	}
	private void addSelection() {
		List theChoices = getChoices();
		if (exclusive) {
			((SortedListModel) choicesList.getModel()).removeAll(theChoices);
		}
		((SortedListModel) selectedList.getModel()).addAll(theChoices);
	}
	private void removeSelection() {
		List theChoices = getSelectedChoices();
		((SortedListModel) selectedList.getModel()).removeAll(theChoices);
		if (exclusive) {
			((SortedListModel) choicesList.getModel()).addAll(theChoices);
		}
	}
	private void ok() {
		if (dialog != null) {
			dialog.setVisible(false);
			returnValue = JOptionPane.OK_OPTION;
		}
	}
	private void cancel() {
		if (dialog != null) {
			dialog.setVisible(false);
			returnValue = JOptionPane.CANCEL_OPTION;
		}
	}
}



