package org.argouml.uml.generator.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.uml.generator.CodeGenerator;
import org.argouml.uml.generator.GeneratorManager;
import org.argouml.uml.generator.Language;
import org.argouml.util.ArgoDialog;
import org.tigris.swidgets.Dialog;


public class ClassGenerationDialog extends ArgoDialog implements ActionListener {
	private static final String SOURCE_LANGUAGE_TAG = "src_lang";
	private TableModelClassChecks classTableModel;
	private boolean isPathInModel;
	private List<Language>languages;
	private JTable classTable;
	private JComboBox outputDirectoryComboBox;
	private int languageHistory;
	public ClassGenerationDialog(List<Object>nodes) {
		this(nodes,false);
	}
	public ClassGenerationDialog(List<Object>nodes,boolean inModel) {
		super(Translator.localize("dialog.title.generate-classes"),Dialog.OK_CANCEL_OPTION,true);
		isPathInModel = inModel;
		buildLanguages();
		JPanel contentPanel = new JPanel(new BorderLayout(10,10));
		classTableModel = new TableModelClassChecks();
		classTableModel.setTarget(nodes);
		classTable = new JTable(classTableModel);
		classTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		classTable.setShowVerticalLines(false);
		if (languages.size() <= 1) {
			classTable.setTableHeader(null);
		}
		setClassTableColumnWidths();
		classTable.setPreferredScrollableViewportSize(new Dimension(300,300));
		JButton selectAllButton = new JButton();
		nameButton(selectAllButton,"button.select-all");
		selectAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				classTableModel.setAllChecks(true);
				classTable.repaint();
			}
		});
		JButton selectNoneButton = new JButton();
		nameButton(selectNoneButton,"button.select-none");
		selectNoneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				classTableModel.setAllChecks(false);
				classTable.repaint();
			}
		});
		JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
		selectPanel.setBorder(BorderFactory.createEmptyBorder(8,0,0,0));
		JPanel selectButtons = new JPanel(new BorderLayout(5,0));
		selectButtons.add(selectAllButton,BorderLayout.CENTER);
		selectButtons.add(selectNoneButton,BorderLayout.EAST);
		selectPanel.add(selectButtons);
		JPanel centerPanel = new JPanel(new BorderLayout(0,2));
		centerPanel.add(new JLabel(Translator.localize("label.available-classes")),BorderLayout.NORTH);
		centerPanel.add(new JScrollPane(classTable),BorderLayout.CENTER);
		centerPanel.add(selectPanel,BorderLayout.SOUTH);
		contentPanel.add(centerPanel,BorderLayout.CENTER);
		outputDirectoryComboBox = new JComboBox(getClasspathEntries().toArray());
		JButton browseButton = new JButton();
		nameButton(browseButton,"button.browse");
		browseButton.setText(browseButton.getText() + "...");
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doBrowse();
			}
		});
		JPanel southPanel = new JPanel(new BorderLayout(0,2));
		if (!inModel) {
			outputDirectoryComboBox.setEditable(true);
			JPanel outputPanel = new JPanel(new BorderLayout(5,0));
			outputPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Translator.localize("label.output-directory")),BorderFactory.createEmptyBorder(2,5,5,5)));
			outputPanel.add(outputDirectoryComboBox,BorderLayout.CENTER);
			outputPanel.add(browseButton,BorderLayout.EAST);
			southPanel.add(outputPanel,BorderLayout.NORTH);
		}
		contentPanel.add(southPanel,BorderLayout.SOUTH);
		setContent(contentPanel);
	}
	@Override protected void nameButtons() {
		super.nameButtons();
		nameButton(getOkButton(),"button.generate");
	}
	private void setClassTableColumnWidths() {
		TableColumn column = null;
		Component c = null;
		int width = 0;
		for (int i = 0;i < classTable.getColumnCount() - 1;++i) {
			column = classTable.getColumnModel().getColumn(i);
			width = 30;
			JTableHeader header = classTable.getTableHeader();
			if (header != null) {
				c = header.getDefaultRenderer().getTableCellRendererComponent(classTable,column.getHeaderValue(),false,false,0,0);
				width = Math.max(c.getPreferredSize().width + 8,width);
			}
			column.setPreferredWidth(width);
			column.setWidth(width);
			column.setMinWidth(width);
			column.setMaxWidth(width);
		}
	}
	private void buildLanguages() {
		languages = new ArrayList<Language>(GeneratorManager.getInstance().getLanguages());
	}
	private static Collection<String>getClasspathEntries() {
		String classpath = System.getProperty("java.class.path");
		Collection<String>entries = new TreeSet<String>();
		final String pathSep = System.getProperty("path.separator");
		StringTokenizer allEntries = new StringTokenizer(classpath,pathSep);
		while (allEntries.hasMoreElements()) {
			String entry = allEntries.nextToken();
			if (!entry.toLowerCase().endsWith(".jar")&&!entry.toLowerCase().endsWith(".zip")) {
				entries.add(entry);
			}
		}
		return entries;
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource() == getOkButton()) {
			String path = null;
			List<String>[]fileNames = new List[languages.size()];
			for (int i = 0;i < languages.size();i++) {
				fileNames[i] = new ArrayList<String>();
				Language language = languages.get(i);
				GeneratorManager genMan = GeneratorManager.getInstance();
				CodeGenerator generator = genMan.getGenerator(language);
				Set nodes = classTableModel.getChecked(language);
				if (!isPathInModel) {
					path = ((String) outputDirectoryComboBox.getModel().getSelectedItem());
					if (path != null) {
						path = path.trim();
						if (path.length() > 0) {
							Collection<String>files = generator.generateFiles(nodes,path,false);
							for (String filename:files) {
								fileNames[i].add(path + CodeGenerator.FILE_SEPARATOR + filename);
							}
						}
					}
				}else {
					Map<String,Set<Object>>nodesPerPath = new HashMap<String,Set<Object>>();
					for (Object node:nodes) {
						if (!Model.getFacade().isAClassifier(node)) {
							continue;
						}
						path = GeneratorManager.getCodePath(node);
						if (path == null) {
							Object parent = Model.getFacade().getNamespace(node);
							while (parent != null) {
								path = GeneratorManager.getCodePath(parent);
								if (path != null) {
									break;
								}
								parent = Model.getFacade().getNamespace(parent);
							}
						}
						if (path != null) {
							final String fileSep = CodeGenerator.FILE_SEPARATOR;
							if (path.endsWith(fileSep)) {
								path = path.substring(0,path.length() - fileSep.length());
							}
							Set<Object>np = nodesPerPath.get(path);
							if (np == null) {
								np = new HashSet<Object>();
								nodesPerPath.put(path,np);
							}
							np.add(node);
							saveLanguage(node,language);
						}
					}
					for (Map.Entry entry:nodesPerPath.entrySet()) {
						String basepath = (String) entry.getKey();
						Set nodeColl = (Set) entry.getValue();
						Collection<String>files = generator.generateFiles(nodeColl,basepath,false);
						for (String filename:files) {
							fileNames[i].add(basepath + CodeGenerator.FILE_SEPARATOR + filename);
						}
					}
				}
			}
		}
	}
	private void saveLanguage(Object node,Language language) {
		Object taggedValue = Model.getFacade().getTaggedValue(node,SOURCE_LANGUAGE_TAG);
		if (taggedValue != null) {
			String savedLang = Model.getFacade().getValueOfTag(taggedValue);
			if (!language.getName().equals(savedLang)) {
				Model.getExtensionMechanismsHelper().setValueOfTag(taggedValue,language.getName());
			}
		}else {
			taggedValue = Model.getExtensionMechanismsFactory().buildTaggedValue(SOURCE_LANGUAGE_TAG,language.getName());
			Model.getExtensionMechanismsHelper().addTaggedValue(node,taggedValue);
		}
	}
	private void doBrowse() {
		try {
			JFileChooser chooser = new JFileChooser((String) outputDirectoryComboBox.getModel().getSelectedItem());
			if (chooser == null) {
				chooser = new JFileChooser();
			}
			chooser.setFileHidingEnabled(true);
			chooser.setMultiSelectionEnabled(false);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setDialogTitle(Translator.localize("dialog.generation.chooser.choose-output-dir"));
			chooser.showDialog(this,Translator.localize("dialog.generation.chooser.approve-button-text"));
			if (!"".equals(chooser.getSelectedFile().getPath())) {
				String path = chooser.getSelectedFile().getPath();
				outputDirectoryComboBox.addItem(path);
				outputDirectoryComboBox.getModel().setSelectedItem(path);
			}
		}catch (Exception userPressedCancel) {
		}
	}
	class TableModelClassChecks extends AbstractTableModel {
		private List<Object>classes;
		private Set<Object>[]checked;
		public TableModelClassChecks() {
		}
		public void setTarget(List<Object>nodes) {
			classes = nodes;
			checked = new Set[getLanguagesCount()];
			for (int j = 0;j < getLanguagesCount();j++) {
				checked[j] = new HashSet<Object>();
			}
			for (Object cls:classes) {
				for (int j = 0;j < getLanguagesCount();j++) {
					Language templanguage = (languages.get(j));
					String tempstring = templanguage.getName();
					if (true) {
						checked[j].add(cls);
					}else if (tempstring.equals(Notation.getConfiguredNotation().getConfigurationValue())) {
						checked[j].add(cls);
					}
				}
			}
			fireTableStructureChanged();
			getOkButton().setEnabled(classes.size() > 0&&getChecked().size() > 0);
		}
		private boolean isSupposedToBeGeneratedAsLanguage(Language lang,Object cls) {
			if (lang == null||cls == null) {
				return false;
			}
			Object taggedValue = Model.getFacade().getTaggedValue(cls,SOURCE_LANGUAGE_TAG);
			if (taggedValue == null) {
				return false;
			}
			String savedLang = Model.getFacade().getValueOfTag(taggedValue);
			return(lang.getName().equals(savedLang));
		}
		private int getLanguagesCount() {
			if (languages == null) {
				return 0;
			}
			return languages.size();
		}
		public Set<Object>getChecked(Language lang) {
			int index = languages.indexOf(lang);
			if (index == -1) {
				return Collections.emptySet();
			}
			return checked[index];
		}
		public Set<Object>getChecked() {
			Set<Object>union = new HashSet<Object>();
			for (int i = 0;i < getLanguagesCount();i++) {
				union.addAll(checked[i]);
			}
			return union;
		}
		public int getColumnCount() {
			return 1 + getLanguagesCount();
		}
		@Override public String getColumnName(int c) {
			if (c >= 0&&c < getLanguagesCount()) {
				Language templanguage = languages.get(c);
				return templanguage.getName();
			}else if (c == getLanguagesCount()) {
				return"Class Name";
			}
			return"XXX";
		}
		public Class getColumnClass(int c) {
			if (c >= 0&&c < getLanguagesCount()) {
				return Boolean.class;
			}else if (c == getLanguagesCount()) {
				return String.class;
			}
			return String.class;
		}
		@Override public boolean isCellEditable(int row,int col) {
			Object cls = classes.get(row);
			if (col == getLanguagesCount()) {
				return false;
			}
			if (!(Model.getFacade().getName(cls).length() > 0)) {
				return false;
			}
			if (col >= 0&&col < getLanguagesCount()) {
				return true;
			}
			return false;
		}
		public int getRowCount() {
			if (classes == null) {
				return 0;
			}
			return classes.size();
		}
		public Object getValueAt(int row,int col) {
			Object cls = classes.get(row);
			if (col == getLanguagesCount()) {
				String name = Model.getFacade().getName(cls);
				if (name.length() > 0) {
					return name;
				}
				return"(anon)";
			}else if (col >= 0&&col < getLanguagesCount()) {
				if (checked[col].contains(cls)) {
					return Boolean.TRUE;
				}
				return Boolean.FALSE;
			}else {
				return"CC-r:" + row + " c:" + col;
			}
		}
		@Override public void setValueAt(Object aValue,int rowIndex,int columnIndex) {
			if (columnIndex == getLanguagesCount()) {
				return;
			}
			if (columnIndex >= getColumnCount()) {
				return;
			}
			if (!(aValue instanceof Boolean)) {
				return;
			}
			boolean val = ((Boolean) aValue).booleanValue();
			Object cls = classes.get(rowIndex);
			if (columnIndex >= 0&&columnIndex < getLanguagesCount()) {
				if (val) {
					checked[columnIndex].add(cls);
				}else {
					checked[columnIndex].remove(cls);
				}
			}
			if (val&&!getOkButton().isEnabled()) {
				getOkButton().setEnabled(true);
			}else if (!val&&getOkButton().isEnabled()&&getChecked().size() == 0) {
				getOkButton().setEnabled(false);
			}
		}
		public void setAllChecks(boolean value) {
			int rows = getRowCount();
			int checks = getLanguagesCount();
			if (rows == 0) {
				return;
			}
			for (int i = 0;i < rows;++i) {
				Object cls = classes.get(i);
				for (int j = 0;j < checks;++j) {
					if (value&&(j == languageHistory)) {
						checked[j].add(cls);
					}else {
						checked[j].remove(cls);
					}
				}
			}
			if (value) {
				if (++languageHistory >= checks) {
					languageHistory = 0;
				}
			}
			getOkButton().setEnabled(value);
		}
		private static final long serialVersionUID = 6108214254680694765l;
	}
	private static final long serialVersionUID = -8897965616334156746l;
}



