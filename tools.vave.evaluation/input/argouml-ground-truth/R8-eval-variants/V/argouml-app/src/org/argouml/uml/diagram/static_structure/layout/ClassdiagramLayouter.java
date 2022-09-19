package org.argouml.uml.diagram.static_structure.layout;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.layout.LayoutedObject;
import org.argouml.uml.diagram.layout.Layouter;
import org.tigris.gef.presentation.Fig;
import org.argouml.uml.diagram.static_structure.layout.ClassdiagramModelElementFactory;


public class ClassdiagramLayouter implements Layouter {
	private class NodeRow implements Iterable<ClassdiagramNode> {
	private List<ClassdiagramNode>nodes = new ArrayList<ClassdiagramNode>();
	private int rowNumber;
	public NodeRow(int aRowNumber) {
		rowNumber = aRowNumber;
	}
	public void addNode(ClassdiagramNode node) {
		node.setRank(rowNumber);
		node.setColumn(nodes.size());
		nodes.add(node);
	}
	public NodeRow doSplit(int maxWidth,int gap) {
		TreeSet<ClassdiagramNode>ts = new TreeSet<ClassdiagramNode>(nodes);
		if (ts.size() < 2) {
			return null;
		}
		ClassdiagramNode firstNode = ts.first();
		if (!firstNode.isStandalone()) {
			return null;
		}
		ClassdiagramNode lastNode = ts.last();
		if (firstNode.isStandalone()&&lastNode.isStandalone()&&(firstNode.isPackage() == lastNode.isPackage())&&getWidth(gap) <= maxWidth) {
			return null;
		}
		boolean hasPackage = firstNode.isPackage();
		NodeRow newRow = new NodeRow(rowNumber + 1);
		ClassdiagramNode split = null;
		int width = 0;
		int count = 0;
		for (Iterator<ClassdiagramNode>iter = ts.iterator();iter.hasNext()&&(width < maxWidth||count < 2);) {
			ClassdiagramNode node = iter.next();
			split = (split == null||(hasPackage&&split.isPackage() == hasPackage)||split.isStandalone())?node:split;
			width += node.getSize().width + gap;
			count++;
		}
		nodes = new ArrayList<ClassdiagramNode>(ts.headSet(split));
		for (ClassdiagramNode n:ts.tailSet(split)) {
			newRow.addNode(n);
		}
		return newRow;
	}
	public List<ClassdiagramNode>getNodeList() {
		return nodes;
	}
	public int getRowNumber() {
		return rowNumber;
	}
	public int getWidth(int gap) {
		int result = 0;
		for (ClassdiagramNode node:nodes) {
			result += node.getSize().width + gap;
		}
		return result;
	}
	public void setRowNumber(int rowNum) {
		this.rowNumber = rowNum;
		adjustRowNodes();
	}
	private void adjustRowNodes() {
		int col = 0;
		int numNodesWithDownlinks = 0;
		List<ClassdiagramNode>list = new ArrayList<ClassdiagramNode>();
		for (ClassdiagramNode node:this) {
			node.setRank(rowNumber);
			node.setColumn(col++);
			if (!node.getDownNodes().isEmpty()) {
				numNodesWithDownlinks++;
				list.add(node);
			}
		}
		int offset = -numNodesWithDownlinks * E_GAP / 2;
		for (ClassdiagramNode node:list) {
			node.setEdgeOffset(offset);
			offset += E_GAP;
		}
	}
	public Iterator<ClassdiagramNode>iterator() {
		return(new TreeSet<ClassdiagramNode>(nodes)).iterator();
	}
}
	private static final int E_GAP = 5;
	private static final int H_GAP = 80;
	private static final int MAX_ROW_WIDTH = 1200;
	private static final int V_GAP = 80;
	private ArgoDiagram diagram;
	private HashMap<Fig,ClassdiagramNode>figNodes = new HashMap<Fig,ClassdiagramNode>();
	private List<ClassdiagramNode>layoutedClassNodes = new ArrayList<ClassdiagramNode>();
	private List<ClassdiagramEdge>layoutedEdges = new ArrayList<ClassdiagramEdge>();
	private List<LayoutedObject>layoutedObjects = new ArrayList<LayoutedObject>();
	private List<NodeRow>nodeRows = new ArrayList<NodeRow>();
	private int xPos;
	private int yPos;
	public ClassdiagramLayouter(ArgoDiagram theDiagram) {
		diagram = theDiagram;
		for (Fig fig:diagram.getLayer().getContents()) {
			if (fig.getEnclosingFig() == null) {
				add(ClassdiagramModelElementFactory.SINGLETON.getInstance(fig));
			}
		}
	}
	public void add(LayoutedObject obj) {
		layoutedObjects.add(obj);
		if (obj instanceof ClassdiagramNode) {
			layoutedClassNodes.add((ClassdiagramNode) obj);
		}else if (obj instanceof ClassdiagramEdge) {
			layoutedEdges.add((ClassdiagramEdge) obj);
		}
	}
	private int getHGap() {
		return H_GAP;
	}
	public Dimension getMinimumDiagramSize() {
		int width = 0,height = 0;
		int hGap2 = getHGap() / 2;
		int vGap2 = getVGap() / 2;
		for (ClassdiagramNode node:layoutedClassNodes) {
			width = Math.max(width,node.getLocation().x + (int) node.getSize().getWidth() + hGap2);
			height = Math.max(height,node.getLocation().y + (int) node.getSize().getHeight() + vGap2);
		}
		return new Dimension(width,height);
	}
	public LayoutedObject getObject(int index) {
		return layoutedObjects.get(index);
	}
	public LayoutedObject[]getObjects() {
		return(LayoutedObject[]) layoutedObjects.toArray();
	}
	private int getVGap() {
		return V_GAP;
	}
	public void layout() {
		long s = System.currentTimeMillis();
		setupLinks();
		rankAndWeightNodes();
		placeNodes();
		placeEdges();
	}
	private void placeEdges() {
		ClassdiagramEdge.setVGap(getVGap());
		ClassdiagramEdge.setHGap(getHGap());
		for (ClassdiagramEdge edge:layoutedEdges) {
			if (edge instanceof ClassdiagramInheritanceEdge) {
				ClassdiagramNode parent = figNodes.get(edge.getDestFigNode());
				((ClassdiagramInheritanceEdge) edge).setOffset(parent.getEdgeOffset());
			}
			edge.layout();
		}
	}
	private void placeNode(ClassdiagramNode node) {
		List<ClassdiagramNode>uplinks = node.getUpNodes();
		List<ClassdiagramNode>downlinks = node.getDownNodes();
		int width = node.getSize().width;
		double xOffset = width + getHGap();
		int bumpX = getHGap() / 2;
		int xPosNew = Math.max(xPos + bumpX,uplinks.size() == 1?node.getPlacementHint():-1);
		node.setLocation(new Point(xPosNew,yPos));
		if (downlinks.size() == 1) {
			ClassdiagramNode downNode = downlinks.get(0);
			if (downNode.getUpNodes().get(0).equals(node)) {
				downNode.setPlacementHint(xPosNew);
			}
		}
		xPos = (int) Math.max(node.getPlacementHint() + width,xPos + xOffset);
	}
	private void placeNodes() {
		int xInit = 0;
		yPos = getVGap() / 2;
		for (NodeRow row:nodeRows) {
			xPos = xInit;
			int rowHeight = 0;
			for (ClassdiagramNode node:row) {
				placeNode(node);
				rowHeight = Math.max(rowHeight,node.getSize().height);
			}
			yPos += rowHeight + getVGap();
		}
		centerParents();
	}
	private void centerParents() {
		for (int i = nodeRows.size() - 1;i >= 0;i--) {
			for (ClassdiagramNode node:nodeRows.get(i)) {
				List<ClassdiagramNode>children = node.getDownNodes();
				if (children.size() > 0) {
					node.setLocation(new Point(xCenter(children) - node.getSize().width / 2,node.getLocation().y));
				}
			}
		}
	}
	private int xCenter(List<ClassdiagramNode>nodes) {
		int left = 9999999;
		int right = 0;
		for (ClassdiagramNode node:nodes) {
			int x = node.getLocation().x;
			left = Math.min(left,x);
			right = Math.max(right,x + node.getSize().width);
		}
		return(right + left) / 2;
	}
	private void rankAndWeightNodes() {
		List<ClassdiagramNode>comments = new ArrayList<ClassdiagramNode>();
		nodeRows.clear();
		TreeSet<ClassdiagramNode>nodeTree = new TreeSet<ClassdiagramNode>(layoutedClassNodes);
		for (ClassdiagramNode node:nodeTree) {
			if (node.isComment()) {
				comments.add(node);
			}else {
				int rowNum = node.getRank();
				for (int i = nodeRows.size();i <= rowNum;i++) {
					nodeRows.add(new NodeRow(rowNum));
				}
				NodeRow nr = nodeRows.get(rowNum);
				nr.addNode(node);
			}
		}
		for (ClassdiagramNode node:comments) {
			ClassdiagramNode cdn = (node.getUpNodes().get(0));
			int rowInd = node.getUpNodes().isEmpty()?0:(cdn.getRank());
			NodeRow nr = nodeRows.get(rowInd);
			nr.addNode(node);
		}
		for (int row = 0;row < nodeRows.size();) {
			NodeRow diaRow = nodeRows.get(row);
			diaRow.setRowNumber(row++);
			diaRow = diaRow.doSplit(MAX_ROW_WIDTH,H_GAP);
			if (diaRow != null) {
				nodeRows.add(row,diaRow);
			}
		}
	}
	public void remove(LayoutedObject obj) {
		layoutedObjects.remove(obj);
	}
	private void setupLinks() {
		figNodes.clear();
		HashMap<Fig,List<ClassdiagramInheritanceEdge>>figParentEdges = new HashMap<Fig,List<ClassdiagramInheritanceEdge>>();
		for (ClassdiagramNode node:layoutedClassNodes) {
			node.getUpNodes().clear();
			node.getDownNodes().clear();
			figNodes.put(node.getFigure(),node);
		}
		for (ClassdiagramEdge edge:layoutedEdges) {
			Fig parentFig = edge.getDestFigNode();
			ClassdiagramNode child = figNodes.get(edge.getSourceFigNode());
			ClassdiagramNode parent = figNodes.get(parentFig);
			if (edge instanceof ClassdiagramInheritanceEdge) {
				if (parent != null&&child != null) {
					parent.addDownlink(child);
					child.addUplink(parent);
					List<ClassdiagramInheritanceEdge>edgeList = figParentEdges.get(parentFig);
					if (edgeList == null) {
						edgeList = new ArrayList<ClassdiagramInheritanceEdge>();
						figParentEdges.put(parentFig,edgeList);
					}
					edgeList.add((ClassdiagramInheritanceEdge) edge);
				}
			}else if (edge instanceof ClassdiagramNoteEdge) {
				if (parent.isComment()) {
					parent.addUplink(child);
				}else if (child.isComment()) {
					child.addUplink(parent);
				}
			}else if (edge instanceof ClassdiagramAssociationEdge) {
			}
		}
	}
}



