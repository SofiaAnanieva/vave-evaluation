package org.argouml.uml.diagram.layout;

import java.awt.*;


public interface Layouter {
	void add(LayoutedObject obj);
	void remove(LayoutedObject obj);
	LayoutedObject[]getObjects();
	LayoutedObject getObject(int index);
	void layout();
	Dimension getMinimumDiagramSize();
}



