package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JRadioButton;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLRadioButtonPanel;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetModelElementVisibility extends UndoableAction {
	private static final ActionSetModelElementVisibility SINGLETON = new ActionSetModelElementVisibility();
	public static final String PUBLIC_COMMAND = "public";
	public static final String PROTECTED_COMMAND = "protected";
	public static final String PRIVATE_COMMAND = "private";
	public static final String PACKAGE_COMMAND = "package";
	protected ActionSetModelElementVisibility() {
		super(Translator.localize("Set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof JRadioButton) {
			JRadioButton source = (JRadioButton) e.getSource();
			String actionCommand = source.getActionCommand();
			Object target = ((UMLRadioButtonPanel) source.getParent()).getTarget();
			if (Model.getFacade().isAModelElement(target)||Model.getFacade().isAElementResidence(target)||Model.getFacade().isAElementImport(target)) {
				Object kind = null;
				if (actionCommand.equals(PUBLIC_COMMAND)) {
					kind = Model.getVisibilityKind().getPublic();
				}else if (actionCommand.equals(PROTECTED_COMMAND)) {
					kind = Model.getVisibilityKind().getProtected();
				}else if (actionCommand.equals(PACKAGE_COMMAND)) {
					kind = Model.getVisibilityKind().getPackage();
				}else {
					kind = Model.getVisibilityKind().getPrivate();
				}
				Model.getCoreHelper().setVisibility(target,kind);
			}
		}
	}
	public static ActionSetModelElementVisibility getInstance() {
		return SINGLETON;
	}
}



