package org.argouml.notation.providers.uml;

import java.util.Collections;
import java.util.Map;
import java.util.StringTokenizer;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.ObjectNotation;


public class ObjectNotationUml extends ObjectNotation {
	public ObjectNotationUml(Object theObject) {
		super(theObject);
	}
	public String getParsingHelp() {
		return"parsing.help.fig-object";
	}
	public void parse(Object modelElement,String text) {
		String s = text.trim();
		if (s.length() == 0) {
			return;
		}
		if (s.charAt(s.length() - 1) == ';') {
			s = s.substring(0,s.length() - 2);
		}
		String name = "";
		String bases = "";
		StringTokenizer baseTokens = null;
		if (s.indexOf(":",0) > -1) {
			name = s.substring(0,s.indexOf(":",0)).trim();
			bases = s.substring(s.indexOf(":",0) + 1).trim();
			baseTokens = new StringTokenizer(bases,",");
		}else {
			name = s;
		}
		Model.getCommonBehaviorHelper().setClassifiers(modelElement,Collections.emptyList());
		if (baseTokens != null) {
			while (baseTokens.hasMoreElements()) {
				String typeString = baseTokens.nextToken();
				Object type = ProjectManager.getManager().getCurrentProject().findType(typeString);
				Model.getCommonBehaviorHelper().addClassifier(modelElement,type);
			}
		}
		Model.getCoreHelper().setName(modelElement,name);
	}
	@SuppressWarnings("deprecation")@Deprecated public String toString(Object modelElement,Map args) {
		return toString(modelElement);
	}
	private String toString(Object modelElement) {
		String nameStr = "";
		if (Model.getFacade().getName(modelElement) != null) {
			nameStr = Model.getFacade().getName(modelElement).trim();
		}
		StringBuilder baseString = formatNameList(Model.getFacade().getClassifiers(modelElement));
		if ((nameStr.length() == 0)&&(baseString.length() == 0)) {
			return"";
		}
		String base = baseString.toString().trim();
		if (base.length() < 1) {
			return nameStr.trim();
		}
		return nameStr.trim() + " : " + base;
	}
	@Override public String toString(Object modelElement,NotationSettings settings) {
		return toString(modelElement);
	}
}



