package org.argouml.ui.explorer;

import java.util.List;
import java.util.ArrayList;
import org.argouml.ui.explorer.rules.PerspectiveRule;
import org.argouml.i18n.Translator;


public class ExplorerPerspective {
	private List<PerspectiveRule>rules;
	private String name;
	public ExplorerPerspective(String newName) {
		name = Translator.localize(newName);
		rules = new ArrayList<PerspectiveRule>();
	}
	public void addRule(PerspectiveRule rule) {
		rules.add(rule);
	}
	public void removeRule(PerspectiveRule rule) {
		rules.remove(rule);
	}
	public Object[]getRulesArray() {
		return rules.toArray();
	}
	public List<PerspectiveRule>getList() {
		return rules;
	}
	@Override public String toString() {
		return name;
	}
	public ExplorerPerspective makeNamedClone(String newName) {
		ExplorerPerspective ep = new ExplorerPerspective(newName);
		ep.rules.addAll(rules);
		return ep;
	}
	protected void setName(String theNewName) {
		this.name = theNewName;
	}
}



