package org.argouml.uml.ui;

import java.util.Collection;
import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;


public class ActionNavigateOppositeAssocEnd extends AbstractActionNavigate {
	public ActionNavigateOppositeAssocEnd() {
		super("button.go-opposite",true);
		putValue(Action.SMALL_ICON,ResourceLoaderWrapper.lookupIconResource("AssociationEnd"));
	}
	protected Object navigateTo(Object source) {
		return Model.getFacade().getNextEnd(source);
	}
	public boolean isEnabled() {
		Object o = TargetManager.getInstance().getTarget();
		if (o != null&&Model.getFacade().isAAssociationEnd(o)) {
			Collection ascEnds = Model.getFacade().getConnections(Model.getFacade().getAssociation(o));
			return!(ascEnds.size() > 2);
		}
		return false;
	}
	private static final long serialVersionUID = 7054600929513339932l;
}



