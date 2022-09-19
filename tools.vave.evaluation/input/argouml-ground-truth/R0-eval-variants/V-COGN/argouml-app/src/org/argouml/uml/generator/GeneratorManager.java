package org.argouml.uml.generator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoGeneratorEvent;
import org.argouml.model.Model;
import org.argouml.uml.reveng.ImportInterface;


public final class GeneratorManager {
	private static final GeneratorManager INSTANCE = new GeneratorManager();
	public static GeneratorManager getInstance() {
		return INSTANCE;
	}
	private Map<Language,CodeGenerator>generators = new HashMap<Language,CodeGenerator>();
	private Language currLanguage = null;
	private GeneratorManager() {
	}
	public void addGenerator(Language lang,CodeGenerator gen) {
		if (currLanguage == null) {
			currLanguage = lang;
		}
		generators.put(lang,gen);
		ArgoEventPump.fireEvent(new ArgoGeneratorEvent(ArgoEventTypes.GENERATOR_ADDED,gen));
	}
	public CodeGenerator removeGenerator(Language lang) {
		CodeGenerator old = generators.remove(lang);
		if (lang.equals(currLanguage)) {
			Iterator it = generators.keySet().iterator();
			if (it.hasNext()) {
				currLanguage = (Language) it.next();
			}else {
				currLanguage = null;
			}
		}
		if (old != null) {
			ArgoEventPump.fireEvent(new ArgoGeneratorEvent(ArgoEventTypes.GENERATOR_REMOVED,old));
		}
		return old;
	}
	public CodeGenerator removeGenerator(String name) {
		Language lang = findLanguage(name);
		if (lang != null) {
			return removeGenerator(lang);
		}
		return null;
	}
	public CodeGenerator getGenerator(Language lang) {
		if (lang == null) {
			return null;
		}
		return generators.get(lang);
	}
	public CodeGenerator getGenerator(String name) {
		Language lang = findLanguage(name);
		return getGenerator(lang);
	}
	public Language getCurrLanguage() {
		return currLanguage;
	}
	public CodeGenerator getCurrGenerator() {
		return currLanguage == null?null:getGenerator(currLanguage);
	}
	public Map<Language,CodeGenerator>getGenerators() {
		Object clone = ((HashMap<Language,CodeGenerator>) generators).clone();
		return(Map<Language,CodeGenerator>) clone;
	}
	public Set<Language>getLanguages() {
		return generators.keySet();
	}
	public Language findLanguage(String name) {
		for (Language lang:getLanguages()) {
			if (lang.getName().equals(name)) {
				return lang;
			}
		}
		return null;
	}
	public static String getCodePath(Object me) {
		if (me == null) {
			return null;
		}
		Object taggedValue = Model.getFacade().getTaggedValue(me,ImportInterface.SOURCE_PATH_TAG);
		String s;
		if (taggedValue == null) {
			return null;
		}
		s = Model.getFacade().getValueOfTag(taggedValue);
		if (s != null) {
			return s.trim();
		}
		return null;
	}
}



