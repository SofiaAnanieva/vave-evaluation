package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Set;


public interface PerspectiveRule {
	String getRuleName();
	Collection getChildren(Object parent);
	Set getDependencies(Object parent);
}



