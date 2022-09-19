package org.argouml.uml.ui.foundation.core;

import javax.swing.ImageIcon;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;


public class PropPanelRelationship extends PropPanelModelElement {
	public PropPanelRelationship(String name,ImageIcon icon) {
		super(name,icon);
	}
	public PropPanelRelationship() {
		super("label.relationship",lookupIcon("Relationship"));
	}
	private static final long serialVersionUID = -1610200799419501588l;
}



