package org.argouml.uml.diagram.layout;

import java.awt.*;


public interface LayoutedNode extends LayoutedObject {
	Dimension getSize();
	Point getLocation();
	void setLocation(Point newLocation);
}



