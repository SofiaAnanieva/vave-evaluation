package org.argouml.uml.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLPlainTextDocument;


public class UMLModelElementCommentDocument extends UMLPlainTextDocument {
	private boolean useBody;
	public UMLModelElementCommentDocument(boolean useBody) {
		super("comment");
		this.useBody = useBody;
	}
	protected void setProperty(String text) {
	}
	protected String getProperty() {
		StringBuffer sb = new StringBuffer();
		Collection comments = Collections.EMPTY_LIST;
		if (Model.getFacade().isAModelElement(getTarget())) {
			comments = Model.getFacade().getComments(getTarget());
		}
		for (Iterator i = comments.iterator();i.hasNext();) {
			Object c = i.next();
			String s;
			if (useBody) {
				s = (String) Model.getFacade().getBody(c);
			}else {
				s = Model.getFacade().getName(c);
			}
			if (s == null) {
				s = "";
			}
			sb.append(s);
			sb.append(" // ");
		}
		if (sb.length() > 4) {
			return(sb.substring(0,sb.length() - 4)).toString();
		}else {
			return"";
		}
	}
}



