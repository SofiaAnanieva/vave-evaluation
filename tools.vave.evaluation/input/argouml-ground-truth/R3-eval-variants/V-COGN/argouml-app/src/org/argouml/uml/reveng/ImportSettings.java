package org.argouml.uml.reveng;


public interface ImportSettings {
	public static final int DETAIL_CLASSIFIER = 0;
	public static final int DETAIL_CLASSIFIER_FEATURE = 1;
	public static final int DETAIL_FULL = 2;
	public int getImportLevel();
	public String getInputSourceEncoding();
}



