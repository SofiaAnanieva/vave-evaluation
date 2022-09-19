package org.argouml.notation;

import javax.swing.Icon;


public interface NotationName {
	String getName();
	String getVersion();
	String getTitle();
	Icon getIcon();
	String getConfigurationValue();
	String toString();
	boolean sameNotationAs(NotationName notationName);
}



