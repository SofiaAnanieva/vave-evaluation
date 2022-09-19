package org.argouml.uml.ui.foundation.core;

import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.UMLLinkedList;


public class PropPanelEnumeration extends PropPanelDataType {
	private JScrollPane literalsScroll;
	private static UMLEnumerationLiteralsListModel literalsListModel = new UMLEnumerationLiteralsListModel();
	public PropPanelEnumeration() {
		super("label.enumeration-title",lookupIcon("Enumeration"));
		addField(Translator.localize("label.literals"),getLiteralsScroll());
	}
	@Override protected void addEnumerationButtons() {
		super.addEnumerationButtons();
		addAction(new ActionAddLiteral());
	}
	public JScrollPane getLiteralsScroll() {
		if (literalsScroll == null) {
			JList list = new UMLLinkedList(literalsListModel);
			literalsScroll = new JScrollPane(list);
		}
		return literalsScroll;
	}
}



