package org.argouml.uml.diagram.layout;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Polygon;


public class LayoutHelper {
	public static final int NORTH = 0;
	public static final int NORTHEAST = 1;
	public static final int EAST = 2;
	public static final int SOUTHEAST = 4;
	public static final int SOUTH = 8;
	public static final int SOUTHWEST = 16;
	public static final int WEST = 32;
	public static final int NORTHWEST = 64;
	public static Point getPointOnPerimeter(Rectangle rect,int direction) {
		return getPointOnPerimeter(rect,direction,0,0);
	}
	public static Point getPointOnPerimeter(Rectangle rect,int direction,double xOff,double yOff) {
		double x = 0;
		double y = 0;
		if (direction == NORTH||direction == NORTHEAST||direction == NORTHWEST) {
			y = rect.getY();
		}
		if (direction == SOUTH||direction == SOUTHWEST||direction == SOUTHEAST) {
			y = rect.getY() + rect.getHeight();
		}
		if (direction == EAST||direction == WEST) {
			y = rect.getY() + rect.getHeight() / 2.0;
		}
		if (direction == NORTHWEST||direction == WEST||direction == SOUTHWEST) {
			x = rect.getX();
		}
		if (direction == NORTHEAST||direction == EAST||direction == SOUTHEAST) {
			x = rect.getX() + rect.getWidth();
		}
		if (direction == NORTH||direction == SOUTH) {
			x = rect.getX() + rect.getWidth() / 2.0;
		}
		x += xOff;
		y += yOff;
		return new Point((int) x,(int) y);
	}
	public static Polygon getRoutingPolygonStraightLine(Point start,Point end) {
		return getRoutingPolygonStraightLineWithOffset(start,end,0);
	}
	public static Polygon getRoutingPolygonStraightLineWithOffset(Point start,Point end,int offset) {
		Polygon newPoly = new Polygon();
		newPoly.addPoint((int) start.getX(),(int) start.getY());
		if (offset != 0) {
			double newY = 0.0;
			if (offset < 0) {
				newY = Math.min(start.getY() + offset,end.getY() + offset);
			}
			if (offset > 0) {
				newY = Math.max(start.getY() + offset,end.getY() + offset);
			}
			newPoly.addPoint((int) start.getX(),(int) newY);
			newPoly.addPoint((int) end.getX(),(int) newY);
		}
		newPoly.addPoint((int) end.getX(),(int) end.getY());
		return newPoly;
	}
}



