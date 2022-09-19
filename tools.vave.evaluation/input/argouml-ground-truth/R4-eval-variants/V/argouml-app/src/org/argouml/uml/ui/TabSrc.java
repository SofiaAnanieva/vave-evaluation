package org.argouml.uml.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JComboBox;
import org.argouml.application.api.Predicate;
import org.argouml.language.ui.LanguageComboBox;
import org.argouml.model.Model;
import org.argouml.ui.TabText;
import org.argouml.uml.generator.GeneratorHelper;
import org.argouml.uml.generator.Language;
import org.argouml.uml.generator.SourceUnit;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;


public class TabSrc extends TabText implements ItemListener {
	private static final long serialVersionUID = -4958164807996827484l;
	private Language langName = null;
	private String fileName = null;
	private SourceUnit[]files = null;
	private LanguageComboBox cbLang = new LanguageComboBox();
	private JComboBox cbFiles = new JComboBox();
	private static List<Predicate>predicates;
	public TabSrc() {
		super("tab.source",true);
		if (predicates == null) {
			predicates = new ArrayList<Predicate>();
			predicates.add(new DefaultPredicate());
		}
		setEditable(false);
		langName = (Language) cbLang.getSelectedItem();
		fileName = null;
		getToolbar().add(cbLang);
		getToolbar().addSeparator();
		cbLang.addItemListener(this);
		getToolbar().add(cbFiles);
		getToolbar().addSeparator();
		cbFiles.addItemListener(this);
	}
	@Override protected void finalize() {
		cbLang.removeItemListener(this);
	}
	private void generateSource(Object elem) {
		Collection code = GeneratorHelper.generate(langName,elem,false);
		cbFiles.removeAllItems();
		if (!code.isEmpty()) {
			files = new SourceUnit[code.size()];
			files = (SourceUnit[]) code.toArray(files);
			for (int i = 0;i < files.length;i++) {
				StringBuilder title = new StringBuilder(files[i].getName());
				if (files[i].getBasePath().length() > 0) {
					title.append(" ( " + files[i].getFullName() + ")");
				}
				cbFiles.addItem(title.toString());
			}
		}
	}
	@Override protected String genText(Object modelObject) {
		if (files == null) {
			generateSource(modelObject);
		}
		if (files != null&&files.length > cbFiles.getSelectedIndex())return files[cbFiles.getSelectedIndex()].getContent();
		return null;
	}
	@Override protected void parseText(String s) {
		Object modelObject = getTarget();
		if (getTarget()instanceof FigNode)modelObject = ((FigNode) getTarget()).getOwner();
		if (getTarget()instanceof FigEdge)modelObject = ((FigEdge) getTarget()).getOwner();
		if (modelObject == null)return;
	}
	@Override public void setTarget(Object t) {
		Object modelTarget = (t instanceof Fig)?((Fig) t).getOwner():t;
		setShouldBeEnabled(Model.getFacade().isAClassifier(modelTarget));
		cbFiles.removeAllItems();
		files = null;
		super.setTarget(t);
	}
	@Override public boolean shouldBeEnabled(Object target) {
		target = (target instanceof Fig)?((Fig) target).getOwner():target;
		setShouldBeEnabled(false);
		for (Predicate p:predicates) {
			if (p.evaluate(target)) {
				setShouldBeEnabled(true);
			}
		}
		return shouldBeEnabled();
	}
	public void itemStateChanged(ItemEvent event) {
		if (event.getSource() == cbLang) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				Language newLang = (Language) cbLang.getSelectedItem();
				if (!newLang.equals(langName)) {
					langName = newLang;
					refresh();
				}
			}
		}else if (event.getSource() == cbFiles) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				String newFile = (String) cbFiles.getSelectedItem();
				if (!newFile.equals(fileName)) {
					fileName = newFile;
					super.setTarget(getTarget());
				}
			}
		}
	}
	@Override public void refresh() {
		setTarget(getTarget());
	}
	public static void addPredicate(Predicate predicate) {
		predicates.add(predicate);
	}
	class DefaultPredicate implements Predicate {
		public boolean evaluate(Object object) {
			return(Model.getFacade().isAClassifier(object));
		}
	}
}



