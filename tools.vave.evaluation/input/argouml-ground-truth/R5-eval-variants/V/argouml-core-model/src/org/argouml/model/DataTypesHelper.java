package org.argouml.model;


public interface DataTypesHelper {
	boolean equalsINITIALKind(Object kind);
	boolean equalsDeepHistoryKind(Object kind);
	boolean equalsShallowHistoryKind(Object kind);
	boolean equalsFORKKind(Object kind);
	boolean equalsJOINKind(Object kind);
	boolean equalsCHOICEKind(Object kind);
	boolean equalsJUNCTIONKind(Object kind);
	String multiplicityToString(Object multiplicity);
	Object setBody(Object handle,String body);
	String getBody(Object handle);
	Object setLanguage(Object handle,String language);
	String getLanguage(Object handle);
}



