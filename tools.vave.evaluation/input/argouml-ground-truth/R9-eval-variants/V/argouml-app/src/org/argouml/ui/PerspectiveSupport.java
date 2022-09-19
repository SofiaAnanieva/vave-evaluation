package org.argouml.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.TreeModel;
import org.argouml.i18n.Translator;


public class PerspectiveSupport {
	private List<TreeModel>goRules;
	private String name;
	private static List<TreeModel>rules = new ArrayList<TreeModel>();
	private PerspectiveSupport() {
	}
	public PerspectiveSupport(String n) {
		name = Translator.localize(n);
		goRules = new ArrayList<TreeModel>();
	}
	public PerspectiveSupport(String n,List<TreeModel>subs) {
		this(n);
		goRules = subs;
	}
	public void addSubTreeModel(TreeModel tm) {
		if (goRules.contains(tm)) {
			return;
		}
		goRules.add(tm);
	}
	public void removeSubTreeModel(TreeModel tm) {
		goRules.remove(tm);
	}
	public List<TreeModel>getSubTreeModelList() {
		return goRules;
	}
	public String getName() {
		return name;
	}
	public void setName(String s) {
		name = s;
	}
	@Override public String toString() {
		if (getName() != null) {
			return getName();
		}else {
			return super.toString();
		}
	}
	public static void registerRule(TreeModel rule) {
		rules.add(rule);
	}
	protected List<TreeModel>getGoRuleList() {
		return goRules;
	}
}



