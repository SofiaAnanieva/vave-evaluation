package org.argouml.uml.diagram.layout;

import java.awt.*;


public interface LayoutedContainer {
	void add(LayoutedObject obj);
	void remove(LayoutedObject obj);
	LayoutedObject[]getContent();
	void resize(Dimension newSize);
}



