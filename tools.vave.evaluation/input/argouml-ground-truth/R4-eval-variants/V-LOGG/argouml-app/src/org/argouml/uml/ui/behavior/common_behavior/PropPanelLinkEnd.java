package org.argouml.uml.ui.behavior.common_behavior;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionNavigate;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;


public class PropPanelLinkEnd extends PropPanelModelElement {
	private static final long serialVersionUID = 666929091194719951l;
	public PropPanelLinkEnd() {
		super("label.association-link-end",lookupIcon("AssociationEnd"));
		addField("label.name",getNameTextField());
		addSeparator();
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionNavigateOppositeLinkEnd());
		addAction(getDeleteAction());
	}
}

class ActionNavigateOppositeLinkEnd extends AbstractActionNavigate {
	public ActionNavigateOppositeLinkEnd() {
		super("button.go-opposite",true);
		putValue(Action.SMALL_ICON,ResourceLoaderWrapper.lookupIconResource("LinkEnd"));
	}
	protected Object navigateTo(Object source) {
		Object link = Model.getFacade().getLink(source);
		List ends = new ArrayList(Model.getFacade().getConnections(link));
		int index = ends.indexOf(source);
		if (ends.size() > index + 1) {
			return ends.get(index + 1);
		}
		return ends.get(0);
	}
}



