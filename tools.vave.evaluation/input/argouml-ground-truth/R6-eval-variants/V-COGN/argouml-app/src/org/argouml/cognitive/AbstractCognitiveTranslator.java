package org.argouml.cognitive;


public abstract class AbstractCognitiveTranslator {
	public abstract String i18nlocalize(String key);
	public abstract String i18nmessageFormat(String key,Object[]args);
}



