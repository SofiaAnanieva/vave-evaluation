package org.argouml.uml.ui.foundation.core;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import org.argouml.model.Model;
import org.argouml.uml.StereotypeUtility;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLModelElementStereotypeListModel extends UMLModelElementListModel2 {
	public UMLModelElementStereotypeListModel() {
		super("stereotype");
	}
	protected void buildModelList() {
		removeAllElements();
		if (Model.getFacade().isAModelElement(getTarget())) {
			addAll(Model.getFacade().getStereotypes(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isAStereotype(element);
	}
	public boolean buildPopup(JPopupMenu popup,int index) {
		Action[]stereoActions = StereotypeUtility.getApplyStereotypeActions(getTarget());
		if (stereoActions != null) {
			for (int i = 0;i < stereoActions.;++i) {
				popup.add(getCheckItem(stereoActions[i]));
			}
		}
		return true;
	}
	private static JCheckBoxMenuItem getCheckItem(Action a) {
		String name = (String) a.getValue(Action.NAME);
		Icon icon = (Icon) a.getValue(Action.SMALL_ICON);
		Boolean selected = (Boolean) a.getValue("SELECTED");
		JCheckBoxMenuItem mi = new JCheckBoxMenuItem(name,icon,(selected == null||selected.booleanValue()));
		mi.setHorizontalTextPosition(SwingConstants.RIGHT);
		mi.setVerticalTextPosition(SwingConstants.CENTER);
		mi.setEnabled(a.isEnabled());
		mi.addActionListener(a);
		return mi;
	}
}



