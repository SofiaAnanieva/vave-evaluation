package org.argouml.cognitive.critics.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.Document;
import org.apache.log4j.Logger;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.Translator;
import org.argouml.util.ArgoDialog;
import org.argouml.util.osdep.StartBrowser;
import org.tigris.swidgets.BorderSplitPane;


public class CriticBrowserDialog extends ArgoDialog implements ActionListener,ListSelectionListener,ItemListener,DocumentListener,TableModelListener,Observer {
	private static final Logger LOG = Logger.getLogger(CriticBrowserDialog.class);
	private static int numCriticBrowser = 0;
	private static final int NUM_COLUMNS = 25;
	private static final String HIGH = Translator.localize("misc.level.high");
	private static final String MEDIUM = Translator.localize("misc.level.medium");
	private static final String LOW = Translator.localize("misc.level.low");
	private static final String[]PRIORITIES =  {HIGH,MEDIUM,LOW};
	private static final String ALWAYS = Translator.localize("dialog.browse.use-clarifier.always");
	private static final String IF_ONLY_ONE = Translator.localize("dialog.browse.use-clarifier.if-only-one");
	private static final String NEVER = Translator.localize("dialog.browse.use-clarifier.never");
	private static final String[]USE_CLAR =  {ALWAYS,IF_ONLY_ONE,NEVER};
	private static final int INSET_PX = 3;
	private JLabel criticsLabel = new JLabel(Translator.localize("dialog.browse.label.critics"));
	private JLabel clsNameLabel = new JLabel(Translator.localize("dialog.browse.label.critic-class"));
	private JLabel headlineLabel = new JLabel(Translator.localize("dialog.browse.label.headline"));
	private JLabel priorityLabel = new JLabel(Translator.localize("dialog.browse.label.priority"));
	private JLabel moreInfoLabel = new JLabel(Translator.localize("dialog.browse.label.more-info"));
	private JLabel descLabel = new JLabel(Translator.localize("dialog.browse.label.description"));
	private JLabel clarifierLabel = new JLabel(Translator.localize("dialog.browse.label.use-clarifier"));
	private TableCritics table;
	private JTextField className = new JTextField("",NUM_COLUMNS);
	private JTextField headline = new JTextField("",NUM_COLUMNS);
	private JComboBox priority = new JComboBox(PRIORITIES);
	private JTextField moreInfo = new JTextField("",NUM_COLUMNS - 4);
	private JTextArea desc = new JTextArea("",6,NUM_COLUMNS);
	private JComboBox useClar = new JComboBox(USE_CLAR);
	private JButton wakeButton = new JButton(Translator.localize("dialog.browse.button.wake"));
	private JButton configButton = new JButton(Translator.localize("dialog.browse.button.configure"));
	private JButton networkButton = new JButton(Translator.localize("dialog.browse.button.edit-network"));
	private JButton goButton = new JButton(Translator.localize("dialog.browse.button.go"));
	private JButton advancedButton = new JButton(Translator.localize("dialog.browse.button.advanced"));
	private Critic target;
	public CriticBrowserDialog() {
		super(Translator.localize("dialog.browse.label.critics"),false);
		JPanel mainContent = new JPanel();
		mainContent.setLayout(new BorderLayout(10,10));
		BorderSplitPane bsp = new BorderSplitPane();
		JPanel tablePanel = new JPanel(new BorderLayout(5,5));
		table = new TableCritics(new TableModelCritics(false),this,this);
		criticsLabel.setText(criticsLabel.getText() + " (" + table.getModel().getRowCount() + ")");
		tablePanel.add(criticsLabel,BorderLayout.NORTH);
		JScrollPane tableSP = new JScrollPane(table);
		tablePanel.add(tableSP,BorderLayout.CENTER);
		tableSP.setPreferredSize(table.getInitialSize());
		bsp.add(tablePanel,BorderSplitPane.CENTER);
		JPanel detailsPanel = new JPanel(new GridBagLayout());
		detailsPanel.setBorder(BorderFactory.createTitledBorder(Translator.localize("dialog.browse.titled-border.critic-details")));
		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.anchor = GridBagConstraints.EAST;
		labelConstraints.fill = GridBagConstraints.BOTH;
		labelConstraints.gridy = 0;
		labelConstraints.gridx = 0;
		labelConstraints.gridwidth = 1;
		labelConstraints.gridheight = 1;
		labelConstraints.insets = new Insets(0,10,5,4);
		GridBagConstraints fieldConstraints = new GridBagConstraints();
		fieldConstraints.anchor = GridBagConstraints.WEST;
		fieldConstraints.fill = GridBagConstraints.BOTH;
		fieldConstraints.gridy = 0;
		fieldConstraints.gridx = 1;
		fieldConstraints.gridwidth = 3;
		fieldConstraints.gridheight = 1;
		fieldConstraints.weightx = 1.0;
		fieldConstraints.insets = new Insets(0,4,5,10);
		className.setBorder(null);
		labelConstraints.gridy = 0;
		fieldConstraints.gridy = 0;
		detailsPanel.add(clsNameLabel,labelConstraints);
		detailsPanel.add(className,fieldConstraints);
		labelConstraints.gridy = 1;
		fieldConstraints.gridy = 1;
		detailsPanel.add(headlineLabel,labelConstraints);
		detailsPanel.add(headline,fieldConstraints);
		labelConstraints.gridy = 2;
		fieldConstraints.gridy = 2;
		detailsPanel.add(priorityLabel,labelConstraints);
		detailsPanel.add(priority,fieldConstraints);
		labelConstraints.gridy = 3;
		fieldConstraints.gridy = 3;
		detailsPanel.add(moreInfoLabel,labelConstraints);
		JPanel moreInfoPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gridConstraints = new GridBagConstraints();
		gridConstraints.anchor = GridBagConstraints.WEST;
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 0;
		gridConstraints.weightx = 100;
		gridConstraints.fill = GridBagConstraints.BOTH;
		gridConstraints.insets = new Insets(0,0,5,0);
		moreInfoPanel.add(moreInfo,gridConstraints);
		gridConstraints.anchor = GridBagConstraints.EAST;
		gridConstraints.gridx = 1;
		gridConstraints.fill = GridBagConstraints.NONE;
		gridConstraints.insets = new Insets(0,10,5,0);
		gridConstraints.weightx = 0;
		moreInfoPanel.add(goButton,gridConstraints);
		moreInfoPanel.setMinimumSize(new Dimension(priority.getWidth(),priority.getHeight()));
		detailsPanel.add(moreInfoPanel,fieldConstraints);
		labelConstraints.gridy = 4;
		fieldConstraints.gridy = 4;
		fieldConstraints.weighty = 3.0;
		labelConstraints.anchor = GridBagConstraints.NORTHEAST;
		detailsPanel.add(descLabel,labelConstraints);
		detailsPanel.add(new JScrollPane(desc),fieldConstraints);
		desc.setLineWrap(true);
		desc.setWrapStyleWord(true);
		desc.setMargin(new Insets(INSET_PX,INSET_PX,INSET_PX,INSET_PX));
		labelConstraints.anchor = GridBagConstraints.EAST;
		labelConstraints.gridy = 5;
		fieldConstraints.gridy = 5;
		fieldConstraints.weighty = 0;
		detailsPanel.add(clarifierLabel,labelConstraints);
		detailsPanel.add(useClar,fieldConstraints);
		labelConstraints.gridy = 6;
		fieldConstraints.gridy = 6;
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(wakeButton);
		buttonPanel.add(advancedButton);
		detailsPanel.add(new JLabel(""),labelConstraints);
		detailsPanel.add(buttonPanel,fieldConstraints);
		bsp.add(detailsPanel,BorderSplitPane.EAST);
		this.addListeners();
		this.enableFieldsAndButtons();
		mainContent.add(bsp);
		setResizable(true);
		setContent(mainContent);
		numCriticBrowser++;
	}
	private void addListeners() {
		goButton.addActionListener(this);
		networkButton.addActionListener(this);
		wakeButton.addActionListener(this);
		advancedButton.addActionListener(this);
		configButton.addActionListener(this);
		headline.getDocument().addDocumentListener(this);
		moreInfo.getDocument().addDocumentListener(this);
		desc.getDocument().addDocumentListener(this);
		priority.addItemListener(this);
		useClar.addItemListener(this);
	}
	private void enableFieldsAndButtons() {
		className.setEditable(false);
		headline.setEditable(false);
		priority.setEnabled(false);
		desc.setEditable(false);
		moreInfo.setEditable(false);
		goButton.setEnabled(false);
		wakeButton.setEnabled(false);
		advancedButton.setEnabled(true);
		networkButton.setEnabled(false);
		configButton.setEnabled(false);
		useClar.setSelectedItem(null);
		useClar.repaint();
	}
	private void setTarget(Critic cr) {
		if (cr == null) {
			enableFieldsAndButtons();
			className.setText("");
			headline.setText("");
			priority.setSelectedItem(null);
			priority.repaint();
			moreInfo.setText("");
			desc.setText("");
			return;
		}
		updateButtonsEnabled();
		className.setText(cr.getClass().getName());
		headline.setText(cr.getHeadline());
		int p = cr.getPriority();
		if (p == ToDoItem.HIGH_PRIORITY) {
			priority.setSelectedItem(HIGH);
		}else if (p == ToDoItem.MED_PRIORITY) {
			priority.setSelectedItem(MEDIUM);
		}else {
			priority.setSelectedItem(LOW);
		}
		priority.repaint();
		moreInfo.setText(cr.getMoreInfoURL());
		desc.setText(cr.getDescriptionTemplate());
		desc.setCaretPosition(0);
		useClar.setSelectedItem(ALWAYS);
		useClar.repaint();
	}
	protected void updateButtonsEnabled() {
		this.configButton.setEnabled(false);
		this.goButton.setEnabled(this.target != null&&this.target.getMoreInfoURL() != null&&this.target.getMoreInfoURL().length() > 0);
		this.networkButton.setEnabled(false);
		this.wakeButton.setEnabled(this.target != null&&(this.target.isSnoozed()||!this.target.isEnabled()));
	}
	private void setTargetHeadline() {
		if (target == null)return;
		String h = headline.getText();
		target.setHeadline(h);
	}
	private void setTargetPriority() {
		if (target == null)return;
		String p = (String) priority.getSelectedItem();
		if (p == null)return;
		if (p.equals(PRIORITIES[0]))target.setPriority(ToDoItem.HIGH_PRIORITY);
		if (p.equals(PRIORITIES[1]))target.setPriority(ToDoItem.MED_PRIORITY);
		if (p.equals(PRIORITIES[2]))target.setPriority(ToDoItem.LOW_PRIORITY);
	}
	private void setTargetMoreInfo() {
		if (target == null)return;
		String mi = moreInfo.getText();
		target.setMoreInfoURL(mi);
	}
	private void setTargetDesc() {
		if (target == null)return;
		String d = desc.getText();
		target.setDescription(d);
	}
	private void setTargetUseClarifiers() {
		LOG.debug("setting clarifier usage rule");
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource() == goButton) {
			StartBrowser.openUrl(moreInfo.getText());
			return;
		}
		if (e.getSource() == networkButton) {
			LOG.debug("TODO: network!");
			return;
		}
		if (e.getSource() == configButton) {
			LOG.debug("TODO: config!");
			return;
		}
		if (e.getSource() == wakeButton) {
			target.unsnooze();
			target.setEnabled(true);
			table.repaint();
			return;
		}
		if (e.getSource() == advancedButton) {
			table.setAdvanced(true);
			advancedButton.setEnabled(false);
		}
		LOG.debug("unknown src in CriticBrowserDialog: " + e.getSource());
	}
	public void valueChanged(ListSelectionEvent lse) {
		if (lse.getValueIsAdjusting())return;
		Object src = lse.getSource();
		if (src != table.getSelectionModel()) {
			LOG.debug("src = " + src);
			return;
		}
		LOG.debug("got valueChanged from " + src);
		int row = table.getSelectedRow();
		if (this.target != null) {
			this.target.deleteObserver(this);
		}
		setTarget((row == -1)?null:table.getCriticAtRow(row));
		if (this.target != null) {
			this.target.addObserver(this);
		}
	}
	public void tableChanged(TableModelEvent e) {
		updateButtonsEnabled();
		table.repaint();
	}
	public void insertUpdate(DocumentEvent e) {
		LOG.debug(getClass().getName() + " insert");
		Document hDoc = headline.getDocument();
		Document miDoc = moreInfo.getDocument();
		Document dDoc = desc.getDocument();
		if (e.getDocument() == hDoc)setTargetHeadline();
		if (e.getDocument() == miDoc)setTargetMoreInfo();
		if (e.getDocument() == dDoc)setTargetDesc();
	}
	public void removeUpdate(DocumentEvent e) {
		insertUpdate(e);
	}
	public void changedUpdate(DocumentEvent e) {
		LOG.debug(getClass().getName() + " changed");
	}
	public void itemStateChanged(ItemEvent e) {
		Object src = e.getSource();
		if (src == priority) {
			setTargetPriority();
		}else if (src == useClar) {
			setTargetUseClarifiers();
		}else {
			LOG.debug("unknown itemStateChanged src: " + src);
		}
	}
	public void update(Observable o,Object arg) {
		table.repaint();
	}
}



