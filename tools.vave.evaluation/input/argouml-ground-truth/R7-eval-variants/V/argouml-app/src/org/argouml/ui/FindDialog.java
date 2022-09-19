package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.swingext.SpacerPanel;
import org.argouml.uml.PredicateSearch;
import org.argouml.util.ArgoDialog;
import org.argouml.util.Predicate;
import org.argouml.util.PredicateStringMatch;
import org.argouml.util.PredicateType;


public class FindDialog extends ArgoDialog implements ActionListener,MouseListener {
	private static FindDialog instance;
	private static int nextResultNum = 1;
	private static int numFinds;
	private static final int INSET_PX = 3;
	private JButton search = new JButton(Translator.localize("dialog.find.button.find"));
	private JButton clearTabs = new JButton(Translator.localize("dialog.find.button.clear-tabs"));
	private JPanel nameLocTab = new JPanel();
	private JComboBox elementName = new JComboBox();
	private JComboBox diagramName = new JComboBox();
	private JComboBox location = new JComboBox();
	private JComboBox type = new JComboBox();
	private JPanel typeDetails = new JPanel();
	private JTabbedPane results = new JTabbedPane();
	private JPanel help = new JPanel();
	private List<TabResults>resultTabs = new ArrayList<TabResults>();
	public static FindDialog getInstance() {
		if (instance == null) {
			instance = new FindDialog();
		}
		return instance;
	}
	public FindDialog() {
		super(Translator.localize("dialog.find.title"),ArgoDialog.OK_CANCEL_OPTION,false);
		JPanel mainPanel = new JPanel(new BorderLayout());
		initNameLocTab();
		mainPanel.add(nameLocTab,BorderLayout.NORTH);
		initHelpTab();
		results.addTab(Translator.localize("dialog.find.tab.help"),help);
		mainPanel.add(results,BorderLayout.CENTER);
		search.addActionListener(this);
		results.addMouseListener(this);
		clearTabs.addActionListener(this);
		clearTabs.setEnabled(false);
		setContent(mainPanel);
		getOkButton().setEnabled(false);
	}
	private void initNameLocTab() {
		elementName.setEditable(true);
		elementName.getEditor().getEditorComponent().setBackground(Color.white);
		diagramName.setEditable(true);
		diagramName.getEditor().getEditorComponent().setBackground(Color.white);
		elementName.addItem("*");
		diagramName.addItem("*");
		GridBagLayout gb = new GridBagLayout();
		nameLocTab.setLayout(gb);
		JLabel elementNameLabel = new JLabel(Translator.localize("dialog.find.label.element-name"));
		JLabel diagramNameLabel = new JLabel(Translator.localize("dialog.find.label.in-diagram"));
		JLabel typeLabel = new JLabel(Translator.localize("dialog.find.label.element-type"));
		JLabel locLabel = new JLabel(Translator.localize("dialog.find.label.find-in"));
		location.addItem(Translator.localize("dialog.find.comboboxitem.entire-project"));
		initTypes();
		typeDetails.setMinimumSize(new Dimension(200,100));
		typeDetails.setPreferredSize(new Dimension(200,100));
		typeDetails.setSize(new Dimension(200,100));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 3;
		c.ipady = 3;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.0;
		gb.setConstraints(elementNameLabel,c);
		nameLocTab.add(elementNameLabel);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.0;
		gb.setConstraints(elementName,c);
		nameLocTab.add(elementName);
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.0;
		gb.setConstraints(diagramNameLabel,c);
		nameLocTab.add(diagramNameLabel);
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1.0;
		gb.setConstraints(diagramName,c);
		nameLocTab.add(diagramName);
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0.0;
		gb.setConstraints(locLabel,c);
		nameLocTab.add(locLabel);
		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 1.0;
		gb.setConstraints(location,c);
		nameLocTab.add(location);
		SpacerPanel spacer = new SpacerPanel();
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0.0;
		gb.setConstraints(spacer,c);
		nameLocTab.add(spacer);
		c.gridx = 3;
		c.gridy = 0;
		c.weightx = 0.0;
		gb.setConstraints(typeLabel,c);
		nameLocTab.add(typeLabel);
		c.gridx = 4;
		c.gridy = 0;
		c.weightx = 1.0;
		gb.setConstraints(type,c);
		nameLocTab.add(type);
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 5;
		gb.setConstraints(typeDetails,c);
		nameLocTab.add(typeDetails);
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new GridLayout(1,2,5,5));
		searchPanel.add(clearTabs);
		searchPanel.add(search);
		searchPanel.setBorder(new EmptyBorder(10,10,10,10));
		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridwidth = 2;
		c.gridheight = 1;
		gb.setConstraints(searchPanel,c);
		nameLocTab.add(searchPanel);
	}
	private void initHelpTab() {
		help.setLayout(new BorderLayout());
		JTextArea helpText = new JTextArea();
		helpText.setText(Translator.localize("dialog.find.helptext"));
		helpText.setEditable(false);
		helpText.setMargin(new Insets(INSET_PX,INSET_PX,INSET_PX,INSET_PX));
		help.add(new JScrollPane(helpText),BorderLayout.CENTER);
	}
	private void initTypes() {
		type.addItem(PredicateMType.create());
		type.addItem(PredicateMType.create(Model.getMetaTypes().getUMLClass()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getInterface()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getAction()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getActor()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getAssociation()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getAssociationClass()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getAssociationEndRole()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getAssociationRole()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getArtifact()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getAttribute()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getClassifier()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getClassifierRole()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getCollaboration()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getComment()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getComponent()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getCompositeState()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getConstraint()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getDataType()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getDependency()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getElementImport()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getEnumeration()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getEnumerationLiteral()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getException()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getExtend()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getExtensionPoint()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getGuard()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getGeneralization()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getInclude()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getInstance()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getInteraction()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getInterface()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getLink()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getMessage()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getModel()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getNode()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getPackage()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getParameter()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getPartition()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getPseudostate()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getOperation()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getSimpleState()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getSignal()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getState()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getStateMachine()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getStateVertex()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getStereotype()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getTagDefinition()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getTransition()));
		type.addItem(PredicateMType.create(Model.getMetaTypes().getUseCase()));
	}
	@Override protected void nameButtons() {
		super.nameButtons();
		nameButton(getOkButton(),"button.go-to-selection");
		nameButton(getCancelButton(),"button.close");
	}
	@Override public void actionPerformed(ActionEvent e) {
		if (e.getSource() == search) {
			doSearch();
		}else if (e.getSource() == clearTabs) {
			doClearTabs();
		}else if (e.getSource() == getOkButton()) {
			doGoToSelection();
		}else {
			super.actionPerformed(e);
		}
	}
	private void doSearch() {
		numFinds++;
		String eName = "";
		if (elementName.getSelectedItem() != null) {
			eName += elementName.getSelectedItem();
			elementName.removeItem(eName);
			elementName.insertItemAt(eName,0);
			elementName.setSelectedItem(eName);
		}
		String dName = "";
		if (diagramName.getSelectedItem() != null) {
			dName += diagramName.getSelectedItem();
			diagramName.removeItem(dName);
			diagramName.insertItemAt(dName,0);
			diagramName.setSelectedItem(dName);
		}
		String name = eName;
		if (dName.length() > 0) {
			Object[]msgArgs =  {name,dName};
			name = Translator.messageFormat("dialog.find.comboboxitem.element-in-diagram",msgArgs);
		}
		String typeName = type.getSelectedItem().toString();
		if (!typeName.equals("Any Type")) {
			name += " " + typeName;
		}
		if (name.length() == 0) {
			name = Translator.localize("dialog.find.tabname") + (nextResultNum++);
		}
		if (name.length() > 15) {
			name = name.substring(0,12) + "...";
		}
		String pName = "";
		Predicate eNamePred = PredicateStringMatch.create(eName);
		Predicate pNamePred = PredicateStringMatch.create(pName);
		Predicate dNamePred = PredicateStringMatch.create(dName);
		Predicate typePred = (Predicate) type.getSelectedItem();
		PredicateSearch pred = new PredicateSearch(eNamePred,pNamePred,dNamePred,typePred);
		ChildGenSearch gen = new ChildGenSearch();
		Object root = ProjectManager.getManager().getCurrentProject();
		TabResults newResults = new TabResults();
		newResults.setTitle(name);
		newResults.setPredicate(pred);
		newResults.setRoot(root);
		newResults.setGenerator(gen);
		resultTabs.add(newResults);
		results.addTab(name,newResults);
		clearTabs.setEnabled(true);
		getOkButton().setEnabled(true);
		results.setSelectedComponent(newResults);
		Object[]msgArgs =  {name};
		location.addItem(Translator.messageFormat("dialog.find.comboboxitem.in-tab",msgArgs));
		invalidate();
		results.invalidate();
		validate();
		newResults.run();
		newResults.requestFocus();
		newResults.selectResult(0);
	}
	public void reset() {
		doClearTabs();
		doResetFields(true);
	}
	private void doClearTabs() {
		int numTabs = resultTabs.size();
		for (int i = 0;i < numTabs;i++) {
		}
		resultTabs.clear();
		clearTabs.setEnabled(false);
		getOkButton().setEnabled(false);
		doResetFields(false);
	}
	private void doResetFields(boolean complete) {
		if (complete) {
			elementName.removeAllItems();
			diagramName.removeAllItems();
			elementName.addItem("*");
			diagramName.addItem("*");
		}
		location.removeAllItems();
		location.addItem(Translator.localize("dialog.find.comboboxitem.entire-project"));
	}
	private void doGoToSelection() {
		if (results.getSelectedComponent()instanceof TabResults) {
			((TabResults) results.getSelectedComponent()).doDoubleClick();
		}
	}
	public void mousePressed(MouseEvent me) {
	}
	public void mouseReleased(MouseEvent me) {
	}
	public void mouseEntered(MouseEvent me) {
	}
	public void mouseExited(MouseEvent me) {
	}
	public void mouseClicked(MouseEvent me) {
		int tab = results.getSelectedIndex();
		if (tab != -1) {
			Rectangle tabBounds = results.getBoundsAt(tab);
			if (!tabBounds.contains(me.getX(),me.getY())) {
				return;
			}
			if (tab >= 1&&me.getClickCount() >= 2) {
				myDoubleClick(tab - 1);
			}
		}
	}
	private void myDoubleClick(int tab) {
		JPanel t = resultTabs.get(tab);
		if (t instanceof AbstractArgoJPanel) {
			if (((AbstractArgoJPanel) t).spawn() != null) {
				resultTabs.remove(tab);
				location.removeItem("In Tab: " + ((AbstractArgoJPanel) t).getTitle());
			}
		}
	}
	private static final long serialVersionUID = 9209251878896557216l;
}

class PredicateMType extends PredicateType {
	protected PredicateMType(Class[]pats) {
		super(pats,pats.);
	}
	protected PredicateMType(Class[]pats,int numPats) {
		super(pats,numPats);
	}
	public static PredicateType create() {
		return new PredicateMType(null,0);
	}
	public static PredicateType create(Object c0) {
		Class[]classes = new Class[1];
		classes[0] = (Class) c0;
		return new PredicateMType(classes);
	}
	public static PredicateType create(Object c0,Object c1) {
		Class[]classes = new Class[2];
		classes[0] = (Class) c0;
		classes[1] = (Class) c1;
		return new PredicateMType(classes);
	}
	public static PredicateType create(Object c0,Object c1,Object c2) {
		Class[]classes = new Class[3];
		classes[0] = (Class) c0;
		classes[1] = (Class) c1;
		classes[2] = (Class) c2;
		return new PredicateMType(classes);
	}
	@Override public String toString() {
		String result = super.toString();
		if (result.startsWith("Uml")) {
			result = result.substring(3);
		}
		return result;
	}
	private static final long serialVersionUID = 901828109709882796l;
}



