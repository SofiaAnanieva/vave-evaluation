package org.argouml.uml.generator;

import java.util.Collection;


public interface CodeGenerator {
	String FILE_SEPARATOR = System.getProperty("file.separator");
	Collection<SourceUnit>generate(Collection elements,boolean deps);
	Collection<String>generateFiles(Collection elements,String path,boolean deps);
	Collection<String>generateFileList(Collection elements,boolean deps);
}



