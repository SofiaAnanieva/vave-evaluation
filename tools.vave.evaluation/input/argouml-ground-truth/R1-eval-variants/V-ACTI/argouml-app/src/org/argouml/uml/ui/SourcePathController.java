package org.argouml.uml.ui;

import java.io.File;
import java.util.Collection;


public interface SourcePathController {
	File getSourcePath(final Object modelElement);
	SourcePathTableModel getSourcePathSettings();
	void setSourcePath(Object modelElement,File sourcePath);
	void setSourcePath(SourcePathTableModel srcPaths);
	void deleteSourcePath(Object modelElement);
	Collection getAllModelElementsWithSourcePath();
}



