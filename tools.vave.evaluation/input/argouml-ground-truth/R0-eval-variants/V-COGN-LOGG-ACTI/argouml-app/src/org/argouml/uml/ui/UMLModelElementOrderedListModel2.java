package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.argouml.i18n.Translator;
import org.tigris.gef.undo.UndoableAction;
import org.argouml.uml.ui.UMLModelElementListModel2;


public abstract class UMLModelElementOrderedListModel2 extends UMLModelElementListModel2 {
	public UMLModelElementOrderedListModel2(String name) {
		super(name);
	}
	protected abstract void buildModelList();
	protected abstract boolean isValidElement(Object element);
	protected abstract void moveDown(int index);
	protected abstract void moveToTop(int index);
	protected abstract void moveToBottom(int index);
	public boolean buildPopup(JPopupMenu popup,int index) {
		JMenuItem moveToTop = new JMenuItem(new MoveToTopAction(this,index));
		JMenuItem moveUp = new JMenuItem(new MoveUpAction(this,index));
		JMenuItem moveDown = new JMenuItem(new MoveDownAction(this,index));
		JMenuItem moveToBottom = new JMenuItem(new MoveToBottomAction(this,index));
		popup.add(moveToTop);
		popup.add(moveUp);
		popup.add(moveDown);
		popup.add(moveToBottom);
		return true;
	}
}

class MoveUpAction extends UndoableAction {
	private UMLModelElementOrderedListModel2 model;
	private int index;
	public MoveUpAction(UMLModelElementOrderedListModel2 theModel,int theIndex) {
		super(Translator.localize("menu.popup.moveup"));
		model = theModel;
		index = theIndex;
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		model.moveDown(index - 1);
	}
	@Override public boolean isEnabled() {
		return index > 0;
	}
}

class MoveDownAction extends UndoableAction {
	private UMLModelElementOrderedListModel2 model;
	private int index;
	public MoveDownAction(UMLModelElementOrderedListModel2 theModel,int theIndex) {
		super(Translator.localize("menu.popup.movedown"));
		model = theModel;
		index = theIndex;
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		model.moveDown(index);
	}
	@Override public boolean isEnabled() {
		return model.getSize() > index + 1;
	}
}

class MoveToTopAction extends UndoableAction {
	private UMLModelElementOrderedListModel2 model;
	private int index;
	public MoveToTopAction(UMLModelElementOrderedListModel2 theModel,int theIndex) {
		super(Translator.localize("menu.popup.movetotop"));
		model = theModel;
		index = theIndex;
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		model.moveToTop(index);
	}
	@Override public boolean isEnabled() {
		return model.getSize() > 1&&index > 0;
	}
}

class MoveToBottomAction extends UndoableAction {
	private UMLModelElementOrderedListModel2 model;
	private int index;
	public MoveToBottomAction(UMLModelElementOrderedListModel2 theModel,int theIndex) {
		super(Translator.localize("menu.popup.movetobottom"));
		model = theModel;
		index = theIndex;
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		model.moveToBottom(index);
	}
	@Override public boolean isEnabled() {
		return model.getSize() > 1&&index < model.getSize() - 1;
	}
}



