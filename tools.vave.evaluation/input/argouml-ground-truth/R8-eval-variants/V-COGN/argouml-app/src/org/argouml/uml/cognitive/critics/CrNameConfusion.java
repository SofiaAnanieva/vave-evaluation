package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.Icon;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.UMLToDoItem;


public class CrNameConfusion extends CrUML {
	private static final long serialVersionUID = -6659510145586121263l;
	public CrNameConfusion() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.NAMING);
		setKnowledgeTypes(Critic.KT_PRESENTATION);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("name");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAModelElement(dm))||Model.getFacade().isAAssociation(dm)) {
			return NO_PROBLEM;
		}
		Object me = dm;
		ListSet offs = computeOffenders(me);
		if (offs.size() > 1) {
			return PROBLEM_FOUND;
		}
		return NO_PROBLEM;
	}
	public ListSet computeOffenders(Object dm) {
		Object ns = Model.getFacade().getNamespace(dm);
		ListSet res = new ListSet(dm);
		String n = Model.getFacade().getName(dm);
		if (n == null||n.equals("")) {
			return res;
		}
		String dmNameStr = n;
		if (dmNameStr == null||dmNameStr.length() == 0) {
			return res;
		}
		String stripped2 = strip(dmNameStr);
		if (ns == null) {
			return res;
		}
		Collection oes = Model.getFacade().getOwnedElements(ns);
		if (oes == null) {
			return res;
		}
		Iterator elems = oes.iterator();
		while (elems.hasNext()) {
			Object me2 = elems.next();
			if (me2 == dm||Model.getFacade().isAAssociation(me2)) {
				continue;
			}
			String meName = Model.getFacade().getName(me2);
			if (meName == null||meName.equals("")) {
				continue;
			}
			String compareName = meName;
			if (confusable(stripped2,strip(compareName))&&!dmNameStr.equals(compareName)) {
				res.add(me2);
			}
		}
		return res;
	}
	@Override public ToDoItem toDoItem(Object dm,Designer dsgr) {
		Object me = dm;
		ListSet offs = computeOffenders(me);
		return new UMLToDoItem(this,offs,dsgr);
	}
	@Override public boolean stillValid(ToDoItem i,Designer dsgr) {
		if (!isActive()) {
			return false;
		}
		ListSet offs = i.getOffenders();
		Object dm = offs.get(0);
		if (!predicate(dm,dsgr)) {
			return false;
		}
		ListSet newOffs = computeOffenders(dm);
		boolean res = offs.equals(newOffs);
		return res;
	}
	public boolean confusable(String stripped1,String stripped2) {
		int countDiffs = countDiffs(stripped1,stripped2);
		return countDiffs <= 1;
	}
	public int countDiffs(String s1,String s2) {
		int len = Math.min(s1.length(),s2.length());
		int count = Math.abs(s1.length() - s2.length());
		if (count > 2) {
			return count;
		}
		for (int i = 0;i < len;i++) {
			if (s1.charAt(i) != s2.charAt(i)) {
				count++;
			}
		}
		return count;
	}
	public String strip(String s) {
		StringBuffer res = new StringBuffer(s.length());
		int len = s.length();
		for (int i = 0;i < len;i++) {
			char c = s.charAt(i);
			if (Character.isLetterOrDigit(c)) {
				res.append(Character.toLowerCase(c));
			}else if (c == ']'&&i > 1&&s.charAt(i - 1) == '[') {
				res.append("[]");
			}
		}
		return res.toString();
	}
	@Override public Icon getClarifier() {
		return ClClassName.getTheInstance();
	}
	@Override public void initWizard(Wizard w) {
		if (w instanceof WizManyNames) {
			ToDoItem item = (ToDoItem) w.getToDoItem();
			((WizManyNames) w).setModelElements(item.getOffenders());
		}
	}
	public Class getWizardClass(ToDoItem item) {
		return WizManyNames.class;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getClassifier());
		ret.add(Model.getMetaTypes().getState());
		return ret;
	}
}



