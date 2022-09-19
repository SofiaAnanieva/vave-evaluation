package org.argouml.uml.ui;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import org.argouml.uml.ui.UMLLinkedList;


public class ScrollList extends JScrollPane implements KeyListener {
	private static final long serialVersionUID = 6711776013279497682l;
	private UMLLinkedList list;
	public ScrollList(ListModel listModel) {
		this(listModel,true,true);
	}
	public ScrollList(ListModel listModel,int visibleRowCount) {
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		list = new UMLLinkedList(listModel,true,true);
		list.setVisibleRowCount(visibleRowCount);
		setViewportView(list);
	}
	public ScrollList(ListModel listModel,boolean showIcon,boolean showPath) {
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		list = new UMLLinkedList(listModel,showIcon,showPath);
		setViewportView(list);
	}
	@Deprecated public ScrollList(JList alist) {
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.list = (UMLLinkedList) alist;
		setViewportView(list);
	}
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			final Point posn = getViewport().getViewPosition();
			if (posn.x > 0) {
				getViewport().setViewPosition(new Point(posn.x - 1,posn.y));
			}
		}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			final Point posn = getViewport().getViewPosition();
			if (list.getWidth() - posn.x > getViewport().getWidth()) {
				getViewport().setViewPosition(new Point(posn.x + 1,posn.y));
			}
		}
	}
	public void keyReleased(KeyEvent arg0) {
	}
	public void keyTyped(KeyEvent arg0) {
	}
	public void addNotify() {
		super.addNotify();
		list.addKeyListener(this);
	}
	public void removeNotify() {
		super.removeNotify();
		list.removeKeyListener(this);
	}
}



