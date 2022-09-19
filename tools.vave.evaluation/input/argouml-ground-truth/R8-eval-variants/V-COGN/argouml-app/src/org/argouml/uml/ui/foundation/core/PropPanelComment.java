package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionRemoveElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.UMLPlainTextDocument;
import org.argouml.uml.ui.UMLTextArea2;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;


public class PropPanelComment extends PropPanelModelElement {
	private static final long serialVersionUID = -8781239511498017147l;
	public PropPanelComment() {
		super("label.comment",lookupIcon("Comment"));
		addField(Translator.localize("label.name"),getNameTextField());
		UMLMutableLinkedList umll = new UMLMutableLinkedList(new UMLCommentAnnotatedElementListModel(),null,null);
		umll.setDeleteAction(new ActionDeleteAnnotatedElement());
		addField(Translator.localize("label.annotated-elements"),new JScrollPane(umll));
		addSeparator();
		UMLTextArea2 text = new UMLTextArea2(new UMLCommentBodyDocument());
		text.setLineWrap(true);
		text.setRows(5);
		JScrollPane pane = new JScrollPane(text);
		addField(Translator.localize("label.comment.body"),pane);
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}

@UmlModelMutator class UMLCommentBodyDocument extends UMLPlainTextDocument {
	public UMLCommentBodyDocument() {
		super("body");
		putProperty("filterNewlines",Boolean.FALSE);
	}
	protected void setProperty(String text) {
		Model.getCoreHelper().setBody(getTarget(),text);
	}
	protected String getProperty() {
		return(String) Model.getFacade().getBody(getTarget());
	}
}

@UmlModelMutator class ActionDeleteAnnotatedElement extends AbstractActionRemoveElement {
	public ActionDeleteAnnotatedElement() {
		super(Translator.localize("menu.popup.remove"));
	}
	@Override public void actionPerformed(ActionEvent arg0) {
		super.actionPerformed(arg0);
		Model.getCoreHelper().removeAnnotatedElement(getTarget(),getObjectToRemove());
	}
}



