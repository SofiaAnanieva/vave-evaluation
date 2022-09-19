package org.argouml.uml.ui;

import java.awt.Color;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import org.argouml.uml.ui.UMLList2;


public class UMLLinkedList extends UMLList2 {
	public UMLLinkedList(ListModel dataModel,boolean showIcon,boolean showPath) {
		super(dataModel,new UMLLinkedListCellRenderer(showIcon,showPath));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setForeground(Color.blue);
		setSelectionForeground(Color.blue.darker());
		UMLLinkMouseListener mouseListener = new UMLLinkMouseListener(this);
		addMouseListener(mouseListener);
	}
	public UMLLinkedList(ListModel dataModel) {
		this(dataModel,true);
	}
	public UMLLinkedList(ListModel dataModel,boolean showIcon) {
		this(dataModel,showIcon,true);
	}
}



