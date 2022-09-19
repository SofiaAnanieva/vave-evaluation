package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetTagDefinitionType extends UndoableAction {
	private static final ActionSetTagDefinitionType SINGLETON = new ActionSetTagDefinitionType();
	private static final Logger LOG = Logger.getLogger(ActionSetTagDefinitionType.class);
	protected ActionSetTagDefinitionType() {
		super(Translator.localize("Set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object source = e.getSource();
		LOG.debug("Receiving " + e + "/" + e.getID() + "/" + e.getActionCommand());
		String oldType = null;
		String newType = null;
		Object tagDef = null;
		if (source instanceof UMLComboBox2) {
			UMLComboBox2 box = (UMLComboBox2) source;
			Object t = box.getTarget();
			if (Model.getFacade().isATagDefinition(t)) {
				tagDef = t;
				oldType = (String) Model.getFacade().getType(tagDef);
			}
			newType = (String) box.getSelectedItem();
			LOG.debug("Selected item is " + newType);
		}
		if (newType != null&&!newType.equals(oldType)&&tagDef != null) {
			LOG.debug("New type is " + newType);
			Model.getExtensionMechanismsHelper().setTagType(tagDef,newType);
		}
	}
	public static ActionSetTagDefinitionType getInstance() {
		return SINGLETON;
	}
}



