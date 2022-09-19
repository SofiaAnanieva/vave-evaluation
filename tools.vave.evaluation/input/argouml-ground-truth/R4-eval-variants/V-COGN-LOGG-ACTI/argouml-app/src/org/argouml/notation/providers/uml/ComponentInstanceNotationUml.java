package org.argouml.notation.providers.uml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.ComponentInstanceNotation;


public class ComponentInstanceNotationUml extends ComponentInstanceNotation {
	public ComponentInstanceNotationUml(Object componentInstance) {
		super(componentInstance);
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
		StringTokenizer tokenizer = null;
		if (s.indexOf(":",0) > -1) {
			name = s.substring(0,s.indexOf(":")).trim();
			bases = s.substring(s.indexOf(":") + 1).trim();
		}else {
			name = s;
		}
		tokenizer = new StringTokenizer(bases,",");
		List<Object>classifiers = new ArrayList<Object>();
		Object ns = Model.getFacade().getNamespace(modelElement);
		if (ns != null) {
			while (tokenizer.hasMoreElements()) {
				String newBase = tokenizer.nextToken();
				Object cls = Model.getFacade().lookupIn(ns,newBase.trim());
				if (cls != null) {
					classifiers.add(cls);
				}
			}
		}
		Model.getCommonBehaviorHelper().setClassifiers(modelElement,classifiers);
		Model.getCoreHelper().setName(modelElement,name);
	}
	public String getParsingHelp() {
		return"parsing.help.fig-componentinstance";
	}
	@SuppressWarnings("deprecation")@Deprecated public String toString(Object modelElement,Map args) {
		return toString(modelElement);
	}
	private String toString(Object modelElement) {
		String nameStr = "";
		if (Model.getFacade().getName(modelElement) != null) {
			nameStr = Model.getFacade().getName(modelElement).trim();
		}
		StringBuilder baseStr = formatNameList(Model.getFacade().getClassifiers(modelElement));
		if ((nameStr.length() == 0)&&(baseStr.length() == 0)) {
			return"";
		}
		baseStr = new StringBuilder(baseStr.toString().trim());
		if (baseStr.length() < 1) {
			return nameStr.trim();
		}
		return nameStr.trim() + " : " + baseStr.toString();
	}
	@Override public String toString(Object modelElement,NotationSettings settings) {
		return toString(modelElement);
	}
}



