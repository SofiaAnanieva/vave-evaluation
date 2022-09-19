package org.argouml.uml.ui;

import java.util.Iterator;
import org.argouml.kernel.ProfileConfiguration;


public interface UMLUserInterfaceContainer {
	public Object getTarget();
	public Object getModelElement();
	public ProfileConfiguration getProfile();
	public String formatElement(Object element);
	public String formatCollection(Iterator iter);
	public String formatNamespace(Object ns);
}



