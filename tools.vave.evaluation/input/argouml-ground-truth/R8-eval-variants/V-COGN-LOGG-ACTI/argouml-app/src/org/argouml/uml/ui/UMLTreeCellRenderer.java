package org.argouml.uml.ui;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.UMLDiagram;


public class UMLTreeCellRenderer extends DefaultTreeCellRenderer {
	private static String name = Translator.localize("label.name");
	private static String typeName = Translator.localize("label.type");
	@Override public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel,boolean expanded,boolean leaf,int row,boolean hasFocusParam) {
		if (value instanceof DefaultMutableTreeNode) {
			value = ((DefaultMutableTreeNode) value).getUserObject();
		}
		Component r = super.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasFocusParam);
		if (value != null&&r instanceof JLabel) {
			JLabel lab = (JLabel) r;
			Icon icon = ResourceLoaderWrapper.getInstance().lookupIcon(value);
			if (icon != null) {
				lab.setIcon(icon);
			}
			String type = null;
			if (Model.getFacade().isAModelElement(value)) {
				type = Model.getFacade().getUMLClassName(value);
			}else if (value instanceof UMLDiagram) {
				type = ((UMLDiagram) value).getLabelName();
			}
			if (type != null) {
				StringBuffer buf = new StringBuffer();
				buf.append("<html>");
				buf.append(name);
				buf.append(' ');
				buf.append(lab.getText());
				buf.append("<br>");
				buf.append(typeName);
				buf.append(' ');
				buf.append(type);
				lab.setToolTipText(buf.toString());
			}else {
				lab.setToolTipText(lab.getText());
			}
		}
		return r;
	}
}



