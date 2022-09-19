package org.argouml.uml.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPopupMenu;
import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLMutableLinkedList extends UMLLinkedList implements MouseListener {
	private static final Logger LOG = Logger.getLogger(UMLMutableLinkedList.class);
	private boolean deletePossible = true;
	private boolean addPossible = false;
	private boolean newPossible = false;
	private JPopupMenu popupMenu;
	private AbstractActionAddModelElement2 addAction = null;
	private AbstractActionNewModelElement newAction = null;
	private AbstractActionRemoveElement deleteAction = null;
	private class PopupMenu extends JPopupMenu {
	public PopupMenu() {
		super();
		if (isAdd()) {
			addAction.setTarget(getTarget());
			add(addAction);
			if (isNew()||isDelete()) {
				addSeparator();
			}
		}
		if (isNew()) {
			newAction.setTarget(getTarget());
			add(newAction);
			if (isDelete()) {
				addSeparator();
			}
		}
		if (isDelete()) {
			deleteAction.setObjectToRemove(getSelectedValue());
			deleteAction.setTarget(getTarget());
			add(deleteAction);
		}
	}
}
	public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,AbstractActionAddModelElement2 theAddAction,AbstractActionNewModelElement theNewAction,AbstractActionRemoveElement theDeleteAction,boolean showIcon) {
		super(dataModel,showIcon);
		setAddAction(theAddAction);
		setNewAction(theNewAction);
		if (theDeleteAction != null)setDeleteAction(theDeleteAction);
		addMouseListener(this);
	}
	public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,AbstractActionAddModelElement2 theAddAction,AbstractActionNewModelElement theNewAction) {
		this(dataModel,theAddAction,theNewAction,null,true);
	}
	public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,AbstractActionAddModelElement2 theAddAction) {
		this(dataModel,theAddAction,null,null,true);
	}
	public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,AbstractActionNewModelElement theNewAction) {
		this(dataModel,null,theNewAction,null,true);
	}
	protected UMLMutableLinkedList(UMLModelElementListModel2 dataModel) {
		this(dataModel,null,null,null,true);
		setDelete(false);
		setDeleteAction(null);
	}
	public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,JPopupMenu popup,boolean showIcon) {
		super(dataModel,showIcon);
		setPopupMenu(popup);
	}
	public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,JPopupMenu popup) {
		this(dataModel,popup,false);
	}
	public boolean isAdd() {
		return addAction != null&&addPossible;
	}
	public boolean isDelete() {
		return deleteAction != null&deletePossible;
	}
	public boolean isNew() {
		return newAction != null&&newPossible;
	}
	public void setDelete(boolean delete) {
		deletePossible = delete;
	}
	public AbstractActionAddModelElement2 getAddAction() {
		return addAction;
	}
	public AbstractActionNewModelElement getNewAction() {
		return newAction;
	}
	public void setAddAction(AbstractActionAddModelElement2 action) {
		if (action != null)addPossible = true;
		addAction = action;
	}
	public void setNewAction(AbstractActionNewModelElement action) {
		if (action != null)newPossible = true;
		newAction = action;
	}
	protected void initActions() {
		if (isAdd()) {
			addAction.setTarget(getTarget());
		}
		if (isNew()) {
			newAction.setTarget(getTarget());
		}
		if (isDelete()) {
			deleteAction.setObjectToRemove(getSelectedValue());
			deleteAction.setTarget(getTarget());
		}
	}
	@Override public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()&&!Model.getModelManagementHelper().isReadOnly(getTarget())) {
			Point point = e.getPoint();
			int index = locationToIndex(point);
			JPopupMenu popup = getPopupMenu();
			Object model = getModel();
			if (model instanceof UMLModelElementListModel2) {
				((UMLModelElementListModel2) model).buildPopup(popup,index);
			}
			if (popup.getComponentCount() > 0) {
				initActions();
				LOG.info("Showing popup at " + e.getX() + "," + e.getY());
				popup.show(this,e.getX(),e.getY());
			}
			e.consume();
		}
	}
	@Override public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()&&!Model.getModelManagementHelper().isReadOnly(getTarget())) {
			JPopupMenu popup = getPopupMenu();
			if (popup.getComponentCount() > 0) {
				initActions();
				LOG.debug("Showing popup at " + e.getX() + "," + e.getY());
				getPopupMenu().show(this,e.getX(),e.getY());
			}
			e.consume();
		}
	}
	public JPopupMenu getPopupMenu() {
		if (popupMenu == null) {
			popupMenu = new PopupMenu();
		}
		return popupMenu;
	}
	public void setPopupMenu(JPopupMenu menu) {
		popupMenu = menu;
	}
	public AbstractActionRemoveElement getDeleteAction() {
		return deleteAction;
	}
	public void setDeleteAction(AbstractActionRemoveElement action) {
		deleteAction = action;
	}
	@Override public void mouseClicked(MouseEvent e) {
		if (e.isPopupTrigger()&&!Model.getModelManagementHelper().isReadOnly(getTarget())) {
			JPopupMenu popup = getPopupMenu();
			if (popup.getComponentCount() > 0) {
				initActions();
				LOG.info("Showing popup at " + e.getX() + "," + e.getY());
				getPopupMenu().show(this,e.getX(),e.getY());
			}
			e.consume();
		}
	}
	@Override public void mouseEntered(MouseEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}
	public void mouseExited(MouseEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}



