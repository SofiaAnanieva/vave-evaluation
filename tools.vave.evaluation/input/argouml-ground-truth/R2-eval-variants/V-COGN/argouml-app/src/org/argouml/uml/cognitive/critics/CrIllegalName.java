package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import javax.swing.Icon;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrIllegalName extends CrUML {
	public CrIllegalName() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.NAMING);
		addTrigger("name");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAModelElement(dm))) {
			return NO_PROBLEM;
		}
		Object me = dm;
		String meName = Model.getFacade().getName(me);
		if (meName == null||meName.equals("")) {
			return NO_PROBLEM;
		}
		String nameStr = meName;
		int len = nameStr.length();
		for (int i = 0;i < len;i++) {
			char c = nameStr.charAt(i);
			if (!(Character.isLetterOrDigit(c)||c == '_'||(c == ' '&&Model.getFacade().isAStateVertex(me)))) {
				return PROBLEM_FOUND;
			}
		}
		return NO_PROBLEM;
	}
	public Icon getClarifier() {
		return ClClassName.getTheInstance();
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getUMLClass());
		ret.add(Model.getMetaTypes().getInterface());
		ret.add(Model.getMetaTypes().getAssociationClass());
		ret.add(Model.getMetaTypes().getOperation());
		ret.add(Model.getMetaTypes().getParameter());
		ret.add(Model.getMetaTypes().getState());
		return ret;
	}
}



