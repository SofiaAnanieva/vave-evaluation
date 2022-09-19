package org.argouml.cognitive.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.Decision;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Goal;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLTreeCellRenderer;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.Fig;


public class ToDoTreeRenderer extends DefaultTreeCellRenderer {
	private final ImageIcon postIt0 = lookupIconResource("PostIt0");
	private final ImageIcon postIt25 = lookupIconResource("PostIt25");
	private final ImageIcon postIt50 = lookupIconResource("PostIt50");
	private final ImageIcon postIt75 = lookupIconResource("PostIt75");
	private final ImageIcon postIt99 = lookupIconResource("PostIt99");
	private final ImageIcon postIt100 = lookupIconResource("PostIt100");
	private final ImageIcon postItD0 = lookupIconResource("PostItD0");
	private final ImageIcon postItD25 = lookupIconResource("PostItD25");
	private final ImageIcon postItD50 = lookupIconResource("PostItD50");
	private final ImageIcon postItD75 = lookupIconResource("PostItD75");
	private final ImageIcon postItD99 = lookupIconResource("PostItD99");
	private final ImageIcon postItD100 = lookupIconResource("PostItD100");
	private UMLTreeCellRenderer treeCellRenderer = new UMLTreeCellRenderer();
	private static ImageIcon lookupIconResource(String name) {
		return ResourceLoaderWrapper.lookupIconResource(name);
	}
	public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel,boolean expanded,boolean leaf,int row,boolean hasTheFocus) {
		Component r = super.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasTheFocus);
		if (r instanceof JLabel) {
			JLabel lab = (JLabel) r;
			if (value instanceof ToDoItem) {
				ToDoItem item = (ToDoItem) value;
				Poster post = item.getPoster();
				if (post instanceof Designer) {
					if (item.getProgress() == 0)lab.setIcon(postItD0);else if (item.getProgress() <= 25)lab.setIcon(postItD25);else if (item.getProgress() <= 50)lab.setIcon(postItD50);else if (item.getProgress() <= 75)lab.setIcon(postItD75);else if (item.getProgress() <= 100)lab.setIcon(postItD99);else lab.setIcon(postItD100);
				}else {
					if (item.getProgress() == 0)lab.setIcon(postIt0);else if (item.getProgress() <= 25)lab.setIcon(postIt25);else if (item.getProgress() <= 50)lab.setIcon(postIt50);else if (item.getProgress() <= 75)lab.setIcon(postIt75);else if (item.getProgress() <= 100)lab.setIcon(postIt99);else lab.setIcon(postIt100);
				}
			}else if (value instanceof Decision) {
				lab.setIcon(MetalIconFactory.getTreeFolderIcon());
			}else if (value instanceof Goal) {
				lab.setIcon(MetalIconFactory.getTreeFolderIcon());
			}else if (value instanceof Poster) {
				lab.setIcon(MetalIconFactory.getTreeFolderIcon());
			}else if (value instanceof PriorityNode) {
				lab.setIcon(MetalIconFactory.getTreeFolderIcon());
			}else if (value instanceof KnowledgeTypeNode) {
				lab.setIcon(MetalIconFactory.getTreeFolderIcon());
			}else if (value instanceof Diagram) {
				return treeCellRenderer.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasTheFocus);
			}else {
				Object newValue = value;
				if (newValue instanceof Fig) {
					newValue = ((Fig) value).getOwner();
				}
				if (Model.getFacade().isAUMLElement(newValue)) {
					return treeCellRenderer.getTreeCellRendererComponent(tree,newValue,sel,expanded,leaf,row,hasTheFocus);
				}
			}
			String tip = lab.getText() + " ";
			lab.setToolTipText(tip);
			tree.setToolTipText(tip);
			if (!sel) {
				lab.setBackground(getBackgroundNonSelectionColor());
			}else {
				Color high = Globals.getPrefs().getHighlightColor();
				high = high.brighter().brighter();
				lab.setBackground(high);
			}
			lab.setOpaque(sel);
		}
		return r;
	}
}



