package org.argouml.uml.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import org.argouml.model.Model;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargettableModelView;
import org.argouml.uml.ui.UMLModelElementListModel2;


public abstract class UMLList2 extends JList implements TargettableModelView,MouseListener {
	protected UMLList2(ListModel dataModel,ListCellRenderer renderer) {
		super(dataModel);
		setDoubleBuffered(true);
		if (renderer != null) {
			setCellRenderer(renderer);
		}
		setFont(LookAndFeelMgr.getInstance().getStandardFont());
		addMouseListener(this);
	}
	public Object getTarget() {
		return((UMLModelElementListModel2) getModel()).getTarget();
	}
	public TargetListener getTargettableModel() {
		return(TargetListener) getModel();
	}
	public void mouseClicked(MouseEvent e) {
		showPopup(e);
	}
	public void mouseEntered(MouseEvent e) {
		if (hasPopup()) {
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
	}
	public void mouseExited(MouseEvent e) {
		if (hasPopup()) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	public void mousePressed(MouseEvent e) {
		showPopup(e);
	}
	public void mouseReleased(MouseEvent e) {
		showPopup(e);
	}
	private final void showPopup(MouseEvent event) {
		if (event.isPopupTrigger()&&!Model.getModelManagementHelper().isReadOnly(getTarget())) {
			Point point = event.getPoint();
			int index = locationToIndex(point);
			JPopupMenu popup = new JPopupMenu();
			ListModel lm = getModel();
			if (lm instanceof UMLModelElementListModel2) {
				if (((UMLModelElementListModel2) lm).buildPopup(popup,index)) {
					popup.show(this,point.x,point.y);
				}
			}
		}
	}
	protected boolean hasPopup() {
		if (getModel()instanceof UMLModelElementListModel2) {
			return((UMLModelElementListModel2) getModel()).hasPopup();
		}
		return false;
	}
}



