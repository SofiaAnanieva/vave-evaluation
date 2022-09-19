package org.argouml.uml.ui.foundation.core;

import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLPlainTextDocument;
import org.argouml.uml.ui.UMLTextArea2;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;


public class PropPanelConstraint extends PropPanelModelElement {
	private static final long serialVersionUID = -7621484706045787046l;
	public PropPanelConstraint() {
		super("label.constraint",lookupIcon("Constraint"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.constrained-elements"),new JScrollPane(new UMLLinkedList(new UMLConstraintConstrainedElementListModel())));
		addSeparator();
		UMLTextArea2 text = new UMLTextArea2(new UMLConstraintBodyDocument());
		text.setEditable(false);
		text.setLineWrap(false);
		text.setRows(5);
		JScrollPane pane = new JScrollPane(text);
		addField(Translator.localize("label.constraint.body"),pane);
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}

class UMLConstraintBodyDocument extends UMLPlainTextDocument {
	public UMLConstraintBodyDocument() {
		super("body");
	}
	protected void setProperty(String text) {
	}
	protected String getProperty() {
		return(String) Model.getFacade().getBody(Model.getFacade().getBody(getTarget()));
	}
}



