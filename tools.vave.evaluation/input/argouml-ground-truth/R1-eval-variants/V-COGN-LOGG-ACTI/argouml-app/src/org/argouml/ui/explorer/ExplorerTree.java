package org.argouml.ui.explorer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import org.argouml.ui.DisplayTextTree;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;


public class ExplorerTree extends DisplayTextTree {
	private boolean updatingSelection;
	private boolean updatingSelectionViaTreeSelection;
	public ExplorerTree() {
		super();
	}
	class ExplorerMouseListener extends MouseAdapter {
		private JTree mLTree;
		public ExplorerMouseListener(JTree newtree) {
			super();
		}
		@Override public void mousePressed(MouseEvent me) {
		}
		@Override public void mouseReleased(MouseEvent me) {
		}
		@Override public void mouseClicked(MouseEvent me) {
		}
		private void myDoubleClick() {
		}
		public void showPopupMenu(MouseEvent me) {
		}
	}
	class ExplorerTreeWillExpandListener implements TreeWillExpandListener {
		public void treeWillCollapse(TreeExpansionEvent tee) {
		}
		public void treeWillExpand(TreeExpansionEvent tee) {
		}
	}
	class ExplorerTreeExpansionListener implements TreeExpansionListener {
		public void treeCollapsed(TreeExpansionEvent event) {
		}
		public void treeExpanded(TreeExpansionEvent event) {
		}
	}
	public void refreshSelection() {
	}
	private void setSelection(Object[]targets) {
	}
	private void addTargetsInternal(Object[]addedTargets) {
	}
	private void selectVisible(Object target) {
	}
	private void selectAll(Set targets) {
	}
	private void selectChildren(ExplorerTreeModel model,ExplorerTreeNode node,Set targets) {
	}
	class ExplorerTreeSelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
		}
	}
	class ExplorerTargetListener implements TargetListener {
		private void setTargets(Object[]targets) {
		}
		public void targetAdded(TargetEvent e) {
		}
		public void targetRemoved(TargetEvent e) {
		}
		public void targetSet(TargetEvent e) {
		}
	}
	private static final long serialVersionUID = 992867483644759920l;
}



