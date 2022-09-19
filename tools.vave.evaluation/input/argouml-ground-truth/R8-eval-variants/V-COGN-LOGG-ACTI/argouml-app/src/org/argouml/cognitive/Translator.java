package org.argouml.cognitive;


public class Translator {
	private static AbstractCognitiveTranslator translator;
	public static void setTranslator(AbstractCognitiveTranslator trans) {
		translator = trans;
	}
	public static String localize(String key) {
		return(translator != null)?translator.i18nlocalize(key):key;
	}
	public static String messageFormat(String key,Object[]args) {
		return(translator != null)?translator.i18nmessageFormat(key,args):key;
	}
}



