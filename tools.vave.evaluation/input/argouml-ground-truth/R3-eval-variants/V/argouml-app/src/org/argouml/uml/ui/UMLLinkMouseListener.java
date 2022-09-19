package org.argouml.uml.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;


public class UMLLinkMouseListener implements MouseListener {
	private JList owner = null;
	private int numberOfMouseClicks;
	public UMLLinkMouseListener(JList theOwner) {
		this(theOwner,2);
	}
	private UMLLinkMouseListener(JList theOwner,int numberOfmouseClicks) {
		owner = theOwner;
		numberOfMouseClicks = numberOfmouseClicks;
	}
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() >= numberOfMouseClicks&&SwingUtilities.isLeftMouseButton(e)) {
			Object o = owner.getSelectedValue();
			if (Model.getFacade().isAModelElement(o)) {
				TargetManager.getInstance().setTarget(o);
			}
			e.consume();
		}
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
}



