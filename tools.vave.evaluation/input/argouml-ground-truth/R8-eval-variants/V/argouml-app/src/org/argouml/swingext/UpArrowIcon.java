package org.argouml.swingext;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.Icon;


public class UpArrowIcon implements Icon {
	public void paintIcon(Component c,Graphics g,int x,int y) {
		int w = getIconWidth(),h = getIconHeight();
		g.setColor(Color.black);
		Polygon p = new Polygon();
		p.addPoint(x,y + h);
		p.addPoint(x + w / 2 + 1,y);
		p.addPoint(x + w,y + h);
		g.fillPolygon(p);
	}
	public int getIconWidth() {
		return 9;
	}
	public int getIconHeight() {
		return 9;
	}
}



