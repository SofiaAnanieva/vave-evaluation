package org.argouml.uml.ui.foundation.core;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLRadioButtonPanel;


public class UMLModelElementVisibilityRadioButtonPanel extends UMLRadioButtonPanel {
	private static final long serialVersionUID = -1705561978481456281l;
	private static List<String[]>labelTextsAndActionCommands = new ArrayList<String[]>();
	static {
	labelTextsAndActionCommands.add(new String[] {Translator.localize("label.visibility-public"),ActionSetModelElementVisibility.PUBLIC_COMMAND});
	labelTextsAndActionCommands.add(new String[] {Translator.localize("label.visibility-package"),ActionSetModelElementVisibility.PACKAGE_COMMAND});
	labelTextsAndActionCommands.add(new String[] {Translator.localize("label.visibility-protected"),ActionSetModelElementVisibility.PROTECTED_COMMAND});
	labelTextsAndActionCommands.add(new String[] {Translator.localize("label.visibility-private"),ActionSetModelElementVisibility.PRIVATE_COMMAND});
}
	public UMLModelElementVisibilityRadioButtonPanel(String title,boolean horizontal) {
		super(title,labelTextsAndActionCommands,"visibility",ActionSetModelElementVisibility.getInstance(),horizontal);
	}
	public void buildModel() {
		if (getTarget() != null) {
			Object target = getTarget();
			Object kind = Model.getFacade().getVisibility(target);
			if (kind == null) {
				setSelected(null);
			}else if (kind.equals(Model.getVisibilityKind().getPublic())) {
				setSelected(ActionSetModelElementVisibility.PUBLIC_COMMAND);
			}else if (kind.equals(Model.getVisibilityKind().getPackage())) {
				setSelected(ActionSetModelElementVisibility.PACKAGE_COMMAND);
			}else if (kind.equals(Model.getVisibilityKind().getProtected())) {
				setSelected(ActionSetModelElementVisibility.PROTECTED_COMMAND);
			}else if (kind.equals(Model.getVisibilityKind().getPrivate())) {
				setSelected(ActionSetModelElementVisibility.PRIVATE_COMMAND);
			}else {
				setSelected(ActionSetModelElementVisibility.PUBLIC_COMMAND);
			}
		}
	}
	public void setEnabled(boolean enabled) {
		for (final Component component:getComponents()) {
			component.setEnabled(enabled);
		}
	}
}



