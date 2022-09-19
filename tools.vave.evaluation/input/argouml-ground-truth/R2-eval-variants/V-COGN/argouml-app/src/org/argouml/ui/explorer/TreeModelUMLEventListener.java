package org.argouml.ui.explorer;


public interface TreeModelUMLEventListener {
	void modelElementChanged(Object element);
	void modelElementAdded(Object element);
	void modelElementRemoved(Object element);
	void structureChanged();
}



