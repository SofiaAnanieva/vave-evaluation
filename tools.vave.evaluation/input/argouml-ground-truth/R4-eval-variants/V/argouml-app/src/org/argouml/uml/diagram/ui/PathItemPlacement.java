package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.PathConv;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;


public class PathItemPlacement extends PathConv {
	private boolean useCollisionCheck = true;
	private boolean useAngle = true;
	private double angle = 90;
	private Fig itemFig;
	private int percent;
	private int pathOffset;
	private int vectorOffset;
	private Point offset;
	private final boolean swap = true;
	public PathItemPlacement(FigEdge pathFig,Fig theItemFig,int pathPercent,int displacement) {
		this(pathFig,theItemFig,pathPercent,0,90,displacement);
	}
	public PathItemPlacement(FigEdge pathFig,Fig theItemFig,int pathPercent,int pathDelta,int displacementAngle,int displacementDistance) {
		super(pathFig);
		itemFig = theItemFig;
		setAnchor(pathPercent,pathDelta);
		setDisplacementVector(displacementAngle + 180,displacementDistance);
	}
	public PathItemPlacement(FigEdge pathFig,Fig theItemFig,int pathPercent,int pathDelta,Point absoluteOffset) {
		super(pathFig);
		itemFig = theItemFig;
		setAnchor(pathPercent,pathDelta);
		setAbsoluteOffset(absoluteOffset);
	}
	public Fig getItemFig() {
		return itemFig;
	}
	public void stuffPoint(Point result) {
		result = getPosition(result);
	}
	public Point getPosition() {
		return getPosition(new Point());
	}
	@Override public Point getPoint() {
		return getPosition();
	}
	public Point getAnchorPosition() {
		int pathDistance = getPathDistance();
		Point anchor = new Point();
		_pathFigure.stuffPointAlongPerimeter(pathDistance,anchor);
		return anchor;
	}
	private int getPathDistance() {
		int length = _pathFigure.getPerimeterLength();
		int distance = Math.max(0,(length * percent) / 100 + pathOffset);
		if (distance >= length) {
			distance = length - 1;
		}
		return distance;
	}
	private Point getPosition(Point result) {
		Point anchor = getAnchorPosition();
		result.setLocation(anchor);
		if (!useAngle) {
			result.translate(offset.x,offset.y);
			return result;
		}
		double slope = getSlope();
		result.setLocation(applyOffset(slope,vectorOffset,anchor));
		if (useCollisionCheck) {
			int increment = 2;
			Dimension size = new Dimension(itemFig.getWidth(),itemFig.getHeight());
			FigEdge fp = (FigEdge) _pathFigure;
			Point[]points = fp.getPoints();
			if (intersects(points,result,size)) {
				int scaledOffset = vectorOffset + increment;
				int limit = 20;
				int count = 0;
				while (intersects(points,result,size)&&count++ < limit) {
					result.setLocation(applyOffset(slope,scaledOffset,anchor));
					scaledOffset += increment;
				}
				if (false) {
					result.setLocation(anchor);
					result.setLocation(applyOffset(slope,-vectorOffset,anchor));
					count = 0;
					scaledOffset = -scaledOffset;
					while (intersects(points,result,size)&&count++ < limit) {
						result.setLocation(applyOffset(slope,scaledOffset,anchor));
						scaledOffset += increment;
					}
				}
			}
		}
		return result;
	}
	private boolean intersects(Point[]points,Point center,Dimension size) {
		Rectangle r = new Rectangle(center.x - (size.width / 2),center.y - (size.height / 2),size.width,size.height);
		Line2D line = new Line2D.Double();
		for (int i = 0;i < points. - 1;i++) {
			line.setLine(points[i],points[i + 1]);
			if (r.intersectsLine(line)) {
				return true;
			}
		}
		return false;
	}
	public void setAnchor(int newPercent,int newOffset) {
		setAnchorPercent(newPercent);
		setAnchorOffset(newOffset);
	}
	public void setAnchorPercent(int newPercent) {
		percent = newPercent;
	}
	public void setAnchorOffset(int newOffset) {
		pathOffset = newOffset;
	}
	public void setAbsoluteOffset(Point newOffset) {
		offset = newOffset;
		useAngle = false;
	}
	public void setPoint(Point newPoint) {
		int vect[] = computeVector(newPoint);
		setDisplacementAngle(vect[0]);
		setDisplacementDistance(vect[1]);
	}
	public int[]computeVector(Point point) {
		Point anchor = getAnchorPosition();
		int distance = (int) anchor.distance(point);
		int angl = 0;
		double pathSlope = getSlope();
		double offsetSlope = getSlope(anchor,point);
		if (swap&&pathSlope > Math.PI / 2&&pathSlope < Math.PI * 3 / 2) {
			angl = -(int) ((offsetSlope - pathSlope) / Math.PI * 180);
		}else {
			angl = (int) ((offsetSlope - pathSlope) / Math.PI * 180);
		}
		int[]result = new int[] {angl,distance};
		return result;
	}
	public void setDisplacementVector(int vectorAngle,int vectorDistance) {
		setDisplacementAngle(vectorAngle);
		setDisplacementDistance(vectorDistance);
	}
	public void setDisplacementVector(double vectorAngle,int vectorDistance) {
		setDisplacementAngle(vectorAngle);
		setDisplacementDistance(vectorDistance);
	}
	public void setDisplacementAngle(int offsetAngle) {
		angle = offsetAngle * Math.PI / 180.0;
		useAngle = true;
	}
	public void setDisplacementAngle(double offsetAngle) {
		angle = offsetAngle * Math.PI / 180.0;
		useAngle = true;
	}
	public void setDisplacementDistance(int newDistance) {
		vectorOffset = newDistance;
		useAngle = true;
	}
	public void setClosestPoint(Point newPoint) {
		throw new UnsupportedOperationException();
	}
	private double getSlope() {
		final int slopeSegLen = 40;
		int pathLength = _pathFigure.getPerimeterLength();
		int pathDistance = getPathDistance();
		int d1 = Math.max(0,pathDistance - slopeSegLen / 2);
		int d2 = Math.min(pathLength - 1,d1 + slopeSegLen);
		if (d1 == d2) {
			return 0;
		}
		Point p1 = _pathFigure.pointAlongPerimeter(d1);
		Point p2 = _pathFigure.pointAlongPerimeter(d2);
		double theta = getSlope(p1,p2);
		return theta;
	}
	private static double getSlope(Point p1,Point p2) {
		int opposite = p2.y - p1.y;
		int adjacent = p2.x - p1.x;
		double theta;
		if (adjacent == 0) {
			if (opposite == 0) {
				return 0;
			}
			if (opposite < 0) {
				theta = Math.PI * 3 / 2;
			}else {
				theta = Math.PI / 2;
			}
		}else {
			theta = Math.atan((double) opposite / (double) adjacent);
			if (adjacent < 0) {
				theta += Math.PI;
			}
			if (theta < 0) {
				theta += Math.PI * 2;
			}
		}
		return theta;
	}
	private Point applyOffset(double theta,int theOffset,Point anchor) {
		Point result = new Point(anchor);
		final boolean aboveAndRight = false;
		if (swap&&theta > Math.PI / 2&&theta < Math.PI * 3 / 2) {
			theta = theta - angle;
		}else {
			theta = theta + angle;
		}
		if (theta > Math.PI * 2) {
			theta -= Math.PI * 2;
		}
		if (theta < 0) {
			theta += Math.PI * 2;
		}
		int dx = (int) (theOffset * Math.cos(theta));
		int dy = (int) (theOffset * Math.sin(theta));
		if (aboveAndRight) {
			dx = Math.abs(dx);
			dy = -Math.abs(dy);
		}
		result.x += dx;
		result.y += dy;
		return result;
	}
	public void paint(Graphics g) {
		final Point p1 = getAnchorPosition();
		Point p2 = getPoint();
		Rectangle r = itemFig.getBounds();
		Color c = Globals.getPrefs().handleColorFor(itemFig);
		c = new Color(c.getRed(),c.getGreen(),c.getBlue(),100);
		g.setColor(c);
		r.grow(2,2);
		g.fillRoundRect(r.x,r.y,r.width,r.height,8,8);
		if (r.contains(p2)) {
			p2 = getRectLineIntersection(r,p1,p2);
		}
		g.drawLine(p1.x,p1.y,p2.x,p2.y);
	}
	private Point getRectLineIntersection(Rectangle r,Point pOut,Point pIn) {
		Line2D.
				Double m,n;
		m = new Line2D.Double(pOut,pIn);
		n = new Line2D.Double(r.x,r.y,r.x + r.width,r.y);
		if (m.intersectsLine(n)) {
			return intersection(m,n);
		}
		n = new Line2D.Double(r.x + r.width,r.y,r.x + r.width,r.y + r.height);
		if (m.intersectsLine(n)) {
			return intersection(m,n);
		}
		n = new Line2D.Double(r.x,r.y + r.height,r.x + r.width,r.y + r.height);
		if (m.intersectsLine(n)) {
			return intersection(m,n);
		}
		n = new Line2D.Double(r.x,r.y,r.x,r.y + r.width);
		if (m.intersectsLine(n)) {
			return intersection(m,n);
		}
		return pIn;
	}
	private Point intersection(Line2D m,Line2D n) {
		double d = (n.getY2() - n.getY1()) * (m.getX2() - m.getX1()) - (n.getX2() - n.getX1()) * (m.getY2() - m.getY1());
		double a = (n.getX2() - n.getX1()) * (m.getY1() - n.getY1()) - (n.getY2() - n.getY1()) * (m.getX1() - n.getX1());
		double as = a / d;
		double x = m.getX1() + as * (m.getX2() - m.getX1());
		double y = m.getY1() + as * (m.getY2() - m.getY1());
		return new Point((int) x,(int) y);
	}
	public int getPercent() {
		return percent;
	}
	public double getAngle() {
		return angle * 180 / Math.PI;
	}
	public int getVectorOffset() {
		return vectorOffset;
	}
}



