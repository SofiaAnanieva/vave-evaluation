package org.argouml.ui.explorer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.argouml.model.Model;
import org.argouml.ui.TransferableModelElements;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.Relocatable;
import org.argouml.uml.diagram.ui.ActionSaveDiagramToClipboard;


public class DnDExplorerTree extends ExplorerTree implements DragGestureListener,DragSourceListener,Autoscroll {
	private static final String DIAGRAM_TO_CLIPBOARD_ACTION = "export Diagram as GIF";
	private Point clickOffset = new Point();
	private TreePath sourcePath;
	private BufferedImage ghostImage;
	private TreePath selectedTreePath;
	private DragSource dragSource;
	public DnDExplorerTree() {
		super();
		this.addTreeSelectionListener(new DnDTreeSelectionListener());
		dragSource = DragSource.getDefaultDragSource();
		DragGestureRecognizer dgr = dragSource.createDefaultDragGestureRecognizer(this,DnDConstants.ACTION_COPY_OR_MOVE,this);
		dgr.setSourceActions(dgr.getSourceActions()&~InputEvent.BUTTON3_MASK);
		new DropTarget(this,new ArgoDropTargetListener());
		KeyStroke ctrlC = KeyStroke.getKeyStroke("control C");
		this.getInputMap().put(ctrlC,DIAGRAM_TO_CLIPBOARD_ACTION);
		this.getActionMap().put(DIAGRAM_TO_CLIPBOARD_ACTION,new ActionSaveDiagramToClipboard());
	}
	public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
		Collection targets = TargetManager.getInstance().getModelTargets();
		if (targets.size() < 1) {
			return;
		}
		TransferableModelElements tf = new TransferableModelElements(targets);
		Point ptDragOrigin = dragGestureEvent.getDragOrigin();
		TreePath path = getPathForLocation(ptDragOrigin.x,ptDragOrigin.y);
		if (path == null) {
			return;
		}
		Rectangle raPath = getPathBounds(path);
		clickOffset.setLocation(ptDragOrigin.x - raPath.x,ptDragOrigin.y - raPath.y);
		JLabel lbl = (JLabel) getCellRenderer().getTreeCellRendererComponent(this,path.getLastPathComponent(),false,isExpanded(path),getModel().isLeaf(path.getLastPathComponent()),0,false);
		lbl.setSize((int) raPath.getWidth(),(int) raPath.getHeight());
		ghostImage = new BufferedImage((int) raPath.getWidth(),(int) raPath.getHeight(),BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g2 = ghostImage.createGraphics();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC,0.5f));
		lbl.paint(g2);
		Icon icon = lbl.getIcon();
		int nStartOfText = (icon == null)?0:icon.getIconWidth() + lbl.getIconTextGap();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER,0.5f));
		g2.setPaint(new GradientPaint(nStartOfText,0,SystemColor.controlShadow,getWidth(),0,new Color(255,255,255,0)));
		g2.fillRect(nStartOfText,0,getWidth(),ghostImage.getHeight());
		g2.dispose();
		sourcePath = path;
		dragGestureEvent.startDrag(null,ghostImage,new Point(5,5),tf,this);
	}
	private boolean isValidDrag(TreePath destinationPath,Transferable tf) {
		if (destinationPath == null) {
			return false;
		}
		if (selectedTreePath.isDescendant(destinationPath)) {
			return false;
		}
		if (!tf.isDataFlavorSupported(TransferableModelElements.UML_COLLECTION_FLAVOR)) {
			return false;
		}
		Object dest = ((DefaultMutableTreeNode) destinationPath.getLastPathComponent()).getUserObject();
		if (!Model.getFacade().isANamespace(dest)) {
			return false;
		}
		if (Model.getModelManagementHelper().isReadOnly(dest)) {
			return false;
		}
		if (Model.getFacade().isADataType(dest)) {
			return false;
		}
		try {
			Collection transfers = (Collection) tf.getTransferData(TransferableModelElements.UML_COLLECTION_FLAVOR);
			for (Object element:transfers) {
				if (Model.getFacade().isAUMLElement(element)) {
					if (!Model.getModelManagementHelper().isReadOnly(element)) {
						if (Model.getFacade().isAModelElement(dest)&&Model.getFacade().isANamespace(element)&&Model.getCoreHelper().isValidNamespace(element,dest)) {
							return true;
						}
						if (Model.getFacade().isAFeature(element)&&Model.getFacade().isAClassifier(dest)) {
							return true;
						}
					}
				}
				if (element instanceof Relocatable) {
					Relocatable d = (Relocatable) element;
					if (d.isRelocationAllowed(dest)) {
						return true;
					}
				}
			}
		}catch (UnsupportedFlavorException e) {
		}catch (IOException e) {
		}
		return false;
	}
	public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
		sourcePath = null;
		ghostImage = null;
	}
	public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
	}
	public void dragExit(DragSourceEvent dragSourceEvent) {
	}
	public void dragOver(DragSourceDragEvent dragSourceDragEvent) {
	}
	public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {
	}
	class DnDTreeSelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
			selectedTreePath = treeSelectionEvent.getNewLeadSelectionPath();
		}
	}
	private static final int AUTOSCROLL_MARGIN = 12;
	public void autoscroll(Point pt) {
		int nRow = getRowForLocation(pt.x,pt.y);
		if (nRow < 0) {
			return;
		}
		Rectangle raOuter = getBounds();
		nRow = (pt.y + raOuter.y <= AUTOSCROLL_MARGIN)?(nRow <= 0?0:nRow - 1):(nRow < getRowCount() - 1?nRow + 1:nRow);
		scrollRowToVisible(nRow);
	}
	public Insets getAutoscrollInsets() {
		Rectangle raOuter = getBounds();
		Rectangle raInner = getParent().getBounds();
		return new Insets(raInner.y - raOuter.y + AUTOSCROLL_MARGIN,raInner.x - raOuter.x + AUTOSCROLL_MARGIN,raOuter.height - raInner.height - raInner.y + raOuter.y + AUTOSCROLL_MARGIN,raOuter.width - raInner.width - raInner.x + raOuter.x + AUTOSCROLL_MARGIN);
	}
	class ArgoDropTargetListener implements DropTargetListener {
		private TreePath lastPath;
		private Rectangle2D cueLine = new Rectangle2D.Float();
		private Rectangle2D ghostRectangle = new Rectangle2D.Float();
		private Color cueLineColor;
		private Point lastMouseLocation = new Point();
		private Timer hoverTimer;
		public ArgoDropTargetListener() {
			cueLineColor = new Color(SystemColor.controlShadow.getRed(),SystemColor.controlShadow.getGreen(),SystemColor.controlShadow.getBlue(),64);
			hoverTimer = new Timer(1000,new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (getPathForRow(0).equals(lastPath)) {
				return;
			}
			if (isExpanded(lastPath)) {
				collapsePath(lastPath);
			}else {
				expandPath(lastPath);
			}
		}
	});
			hoverTimer.setRepeats(false);
		}
		public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
			if (!isDragAcceptable(dropTargetDragEvent)) {
				dropTargetDragEvent.rejectDrag();
			}else {
				dropTargetDragEvent.acceptDrag(dropTargetDragEvent.getDropAction());
			}
		}
		public void dragExit(DropTargetEvent dropTargetEvent) {
			if (!DragSource.isDragImageSupported()) {
				repaint(ghostRectangle.getBounds());
			}
		}
		public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
			Point pt = dropTargetDragEvent.getLocation();
			if (pt.equals(lastMouseLocation)) {
				return;
			}
			lastMouseLocation = pt;
			Graphics2D g2 = (Graphics2D) getGraphics();
			if (ghostImage != null) {
				if (!DragSource.isDragImageSupported()) {
					paintImmediately(ghostRectangle.getBounds());
					ghostRectangle.setRect(pt.x - clickOffset.x,pt.y - clickOffset.y,ghostImage.getWidth(),ghostImage.getHeight());
					g2.drawImage(ghostImage,AffineTransform.getTranslateInstance(ghostRectangle.getX(),ghostRectangle.getY()),null);
				}else {
					paintImmediately(cueLine.getBounds());
				}
			}
			TreePath path = getPathForLocation(pt.x,pt.y);
			if (!(path == lastPath)) {
				lastPath = path;
				hoverTimer.restart();
			}
			Rectangle raPath = getPathBounds(path);
			if (raPath != null) {
				cueLine.setRect(0,raPath.y + (int) raPath.getHeight(),getWidth(),2);
			}
			g2.setColor(cueLineColor);
			g2.fill(cueLine);
			ghostRectangle = ghostRectangle.createUnion(cueLine);
			try {
				if (!dropTargetDragEvent.isDataFlavorSupported(TransferableModelElements.UML_COLLECTION_FLAVOR)) {
					dropTargetDragEvent.rejectDrag();
					return;
				}
			}catch (NullPointerException e) {
				dropTargetDragEvent.rejectDrag();
				return;
			}
			if (path == null) {
				dropTargetDragEvent.rejectDrag();
				return;
			}
			if (path.equals(sourcePath)) {
				dropTargetDragEvent.rejectDrag();
				return;
			}
			if (selectedTreePath.isDescendant(path)) {
				dropTargetDragEvent.rejectDrag();
				return;
			}
			Object dest = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
			if (!Model.getFacade().isANamespace(dest)) {
				dropTargetDragEvent.rejectDrag();
				return;
			}
			if (Model.getModelManagementHelper().isReadOnly(dest)) {
				return;
			}
			if (Model.getFacade().isADataType(dest)) {
				dropTargetDragEvent.rejectDrag();
				return;
			}
			dropTargetDragEvent.acceptDrag(dropTargetDragEvent.getDropAction());
		}
		public void drop(DropTargetDropEvent dropTargetDropEvent) {
			hoverTimer.stop();
			repaint(ghostRectangle.getBounds());
			if (!isDropAcceptable(dropTargetDropEvent)) {
				dropTargetDropEvent.rejectDrop();
				return;
			}
			try {
				Transferable tr = dropTargetDropEvent.getTransferable();
				Point loc = dropTargetDropEvent.getLocation();
				TreePath destinationPath = getPathForLocation(loc.x,loc.y);
				if (!isValidDrag(destinationPath,tr)) {
					dropTargetDropEvent.rejectDrop();
					return;
				}
				Collection modelElements = (Collection) tr.getTransferData(TransferableModelElements.UML_COLLECTION_FLAVOR);
				Object dest = ((DefaultMutableTreeNode) destinationPath.getLastPathComponent()).getUserObject();
				Object src = ((DefaultMutableTreeNode) sourcePath.getLastPathComponent()).getUserObject();
				int action = dropTargetDropEvent.getDropAction();
				boolean copyAction = (action == DnDConstants.ACTION_COPY);
				boolean moveAction = (action == DnDConstants.ACTION_MOVE);
				if (!(moveAction||copyAction)) {
					dropTargetDropEvent.rejectDrop();
					return;
				}
				if (Model.getFacade().isAUMLElement(dest)) {
					if (Model.getModelManagementHelper().isReadOnly(dest)) {
						dropTargetDropEvent.rejectDrop();
						return;
					}
				}
				if (Model.getFacade().isAUMLElement(src)) {
					if (Model.getModelManagementHelper().isReadOnly(src)) {
						dropTargetDropEvent.rejectDrop();
						return;
					}
				}
				Collection<Object>newTargets = new ArrayList<Object>();
				try {
					dropTargetDropEvent.acceptDrop(action);
					for (Object me:modelElements) {
						if (Model.getFacade().isAUMLElement(me)) {
							if (Model.getModelManagementHelper().isReadOnly(me)) {
								continue;
							}
						}
						if (Model.getCoreHelper().isValidNamespace(me,dest)) {
							if (moveAction) {
								Model.getCoreHelper().setNamespace(me,dest);
								newTargets.add(me);
							}
							if (copyAction) {
								try {
									newTargets.add(Model.getCopyHelper().copy(me,dest));
								}catch (RuntimeException e) {
								}
							}
						}
						if (me instanceof Relocatable) {
							Relocatable d = (Relocatable) me;
							if (d.isRelocationAllowed(dest)) {
								if (d.relocate(dest)) {
									ExplorerEventAdaptor.getInstance().modelElementChanged(src);
									ExplorerEventAdaptor.getInstance().modelElementChanged(dest);
									makeVisible(destinationPath);
									expandPath(destinationPath);
									newTargets.add(me);
								}
							}
						}
						if (Model.getFacade().isAFeature(me)&&Model.getFacade().isAClassifier(dest)) {
							if (moveAction) {
								Model.getCoreHelper().removeFeature(Model.getFacade().getOwner(me),me);
								Model.getCoreHelper().addFeature(dest,me);
								newTargets.add(me);
							}
							if (copyAction) {
								newTargets.add(Model.getCopyHelper().copy(me,dest));
							}
						}
					}
					dropTargetDropEvent.getDropTargetContext().dropComplete(true);
					TargetManager.getInstance().setTargets(newTargets);
				}catch (java.lang.IllegalStateException ils) {
					dropTargetDropEvent.rejectDrop();
				}
				dropTargetDropEvent.getDropTargetContext().dropComplete(true);
			}catch (IOException io) {
				dropTargetDropEvent.rejectDrop();
			}catch (UnsupportedFlavorException ufe) {
				dropTargetDropEvent.rejectDrop();
			}
		}
		public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
			if (!isDragAcceptable(dropTargetDragEvent)) {
				dropTargetDragEvent.rejectDrag();
			}else {
				dropTargetDragEvent.acceptDrag(dropTargetDragEvent.getDropAction());
			}
		}
		public boolean isDragAcceptable(DropTargetDragEvent dropTargetEvent) {
			if ((dropTargetEvent.getDropAction()&DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
				return false;
			}
			Point pt = dropTargetEvent.getLocation();
			TreePath path = getPathForLocation(pt.x,pt.y);
			if (path == null) {
				return false;
			}
			if (path.equals(sourcePath)) {
				return false;
			}
			return true;
		}
		public boolean isDropAcceptable(DropTargetDropEvent dropTargetDropEvent) {
			if ((dropTargetDropEvent.getDropAction()&DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
				return false;
			}
			Point pt = dropTargetDropEvent.getLocation();
			TreePath path = getPathForLocation(pt.x,pt.y);
			if (path == null) {
				return false;
			}
			if (path.equals(sourcePath)) {
				return false;
			}
			return true;
		}
	}
	private static final long serialVersionUID = 6207230394860016617l;
}



