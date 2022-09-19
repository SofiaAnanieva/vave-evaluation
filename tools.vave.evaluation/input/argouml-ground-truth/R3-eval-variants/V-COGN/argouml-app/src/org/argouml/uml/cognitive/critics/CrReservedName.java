package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrReservedName extends CrUML {
	private static List<String>names;
	private static List<String>umlReserved = new ArrayList<String>();
	static {
	umlReserved.add("none");
	umlReserved.add("interface");
	umlReserved.add("sequential");
	umlReserved.add("guarded");
	umlReserved.add("concurrent");
	umlReserved.add("frozen");
	umlReserved.add("aggregate");
	umlReserved.add("composite");
}
	static {
	umlReserved.add("becomes");
	umlReserved.add("call");
	umlReserved.add("component");
	umlReserved.add("deletion");
	umlReserved.add("derived");
	umlReserved.add("enumeration");
	umlReserved.add("extends");
}
	static {
	umlReserved.add("facade");
	umlReserved.add("framework");
	umlReserved.add("friend");
	umlReserved.add("import");
	umlReserved.add("inherits");
	umlReserved.add("instance");
	umlReserved.add("invariant");
	umlReserved.add("library");
	umlReserved.add("metaclass");
	umlReserved.add("powertype");
	umlReserved.add("private");
	umlReserved.add("process");
	umlReserved.add("requirement");
	umlReserved.add("stereotype");
	umlReserved.add("stub");
	umlReserved.add("subclass");
	umlReserved.add("subtype");
	umlReserved.add("system");
	umlReserved.add("table");
	umlReserved.add("thread");
	umlReserved.add("type");
}
	static {
	umlReserved.add("useCaseModel");
	umlReserved.add("uses");
	umlReserved.add("utility");
	umlReserved.add("implementationClass");
	umlReserved.add("postcondition");
	umlReserved.add("precondition");
	umlReserved.add("topLevelPackage");
	umlReserved.add("subtraction");
}
	public CrReservedName() {
		this(umlReserved);
	}
	public CrReservedName(List<String>reservedNames) {
		setupHeadAndDesc();
		setPriority(ToDoItem.HIGH_PRIORITY);
		addSupportedDecision(UMLDecision.NAMING);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("name");
		addTrigger("feature_name");
		names = reservedNames;
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isPrimaryObject(dm))) {
			return NO_PROBLEM;
		}
		if (!(Model.getFacade().isAModelElement(dm))) {
			return NO_PROBLEM;
		}
		String meName = Model.getFacade().getName(dm);
		if (meName == null||meName.equals("")) {
			return NO_PROBLEM;
		}
		String nameStr = meName;
		if (nameStr == null||nameStr.length() == 0) {
			return NO_PROBLEM;
		}
		if (isBuiltin(nameStr)) {
			return NO_PROBLEM;
		}
		for (String name:names) {
			if (name.equalsIgnoreCase(nameStr)) {
				return PROBLEM_FOUND;
			}
		}
		return NO_PROBLEM;
	}
	protected boolean isBuiltin(String name) {
		Project p = ProjectManager.getManager().getCurrentProject();
		Object type = p.findTypeInDefaultModel(name);
		return type != null;
	}
	@Override public Icon getClarifier() {
		return ClClassName.getTheInstance();
	}
	@Override public void initWizard(Wizard w) {
		if (w instanceof WizMEName) {
			ToDoItem item = (ToDoItem) w.getToDoItem();
			String sug = Model.getFacade().getName(item.getOffenders().get(0));
			String ins = super.getInstructions();
			((WizMEName) w).setInstructions(ins);
			((WizMEName) w).setSuggestion(sug);
			((WizMEName) w).setMustEdit(true);
		}
	}
	@Override public Class getWizardClass(ToDoItem item) {
		return WizMEName.class;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getClassifier());
		ret.add(Model.getMetaTypes().getOperation());
		ret.add(Model.getMetaTypes().getState());
		ret.add(Model.getMetaTypes().getAssociation());
		return ret;
	}
	private static final long serialVersionUID = -5839267391209851505l;
}



