package org.argouml.moduleloader;


public interface ModuleInterface {
	boolean enable();
	boolean disable();
	String getName();
	String getInfo(int type);
	int DESCRIPTION = 0;
	int AUTHOR = 1;
	int VERSION = 2;
	int DOWNLOADSITE = 3;
}



