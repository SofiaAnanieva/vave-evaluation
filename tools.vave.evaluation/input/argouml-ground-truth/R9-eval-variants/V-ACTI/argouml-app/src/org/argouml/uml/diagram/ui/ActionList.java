package org.argouml.uml.diagram.ui;

import java.util.List;
import java.util.Vector;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.argouml.kernel.UmlModelMutator;


class ActionList<E>extends Vector<E> {
	private final boolean readonly;
	ActionList(List<?extends E>initialList,boolean readOnly) {
			super(initialList);
			this.readonly = readOnly;
		}
	@Override public boolean add(E o) {
		if (readonly) {
			if (isUmlMutator(o)) {
				return false;
			}else if (o instanceof JMenu) {
				o = (E) trimMenu((JMenu) o);
			}
		}
		if (o != null) {
			return super.add(o);
		}else {
			return false;
		}
	}
	@Override public void addElement(E o) {
		if (readonly) {
			if (isUmlMutator(o)) {
				return;
			}else if (o instanceof JMenu) {
				o = (E) trimMenu((JMenu) o);
			}
		}
		if (o != null) {
			super.addElement(o);
		}
	}
	@Override public void add(int index,E o) {
		if (readonly) {
			if (isUmlMutator(o)) {
				return;
			}else if (o instanceof JMenu) {
				o = (E) trimMenu((JMenu) o);
			}
		}
		if (o != null) {
			super.add(index,o);
		}
	}
	@Override public void insertElementAt(E o,int index) {
		if (readonly) {
			if (isUmlMutator(o)) {
				return;
			}else if (o instanceof JMenu) {
				o = (E) trimMenu((JMenu) o);
			}
		}
		if (o != null) {
			super.insertElementAt(o,index);
		}
	}
	private JMenu trimMenu(JMenu menu) {
		for (int i = menu.getItemCount() - 1;i >= 0;--i) {
			JMenuItem menuItem = menu.getItem(i);
			Action action = menuItem.getAction();
			if (action == null&&menuItem.getActionListeners(). > 0&&menuItem.getActionListeners()[0]instanceof Action) {
				action = (Action) menuItem.getActionListeners()[0];
			}
			if (isUmlMutator(action)) {
				menu.remove(i);
			}
		}
		if (menu.getItemCount() == 0) {
			return null;
		}
		return menu;
	}
	private boolean isUmlMutator(Object a) {
		return a instanceof UmlModelMutator||a.getClass().isAnnotationPresent(UmlModelMutator.class);
	}
}



