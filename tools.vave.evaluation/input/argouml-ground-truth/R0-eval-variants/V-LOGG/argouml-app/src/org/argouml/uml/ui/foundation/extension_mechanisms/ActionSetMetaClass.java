package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetMetaClass extends UndoableAction {
	public static final ActionSetMetaClass SINGLETON = new ActionSetMetaClass();
	public ActionSetMetaClass() {
		super(Translator.localize("Set"),ResourceLoaderWrapper.lookupIcon("Set"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object source = e.getSource();
		Object newBase = null;
		Object stereo = null;
		if (source instanceof UMLComboBox2) {
			UMLComboBox2 combo = (UMLComboBox2) source;
			stereo = combo.getTarget();
			if (Model.getFacade().isAStereotype(stereo)) {
				Collection oldBases = Model.getFacade().getBaseClasses(stereo);
				newBase = combo.getSelectedItem();
				if (newBase != null) {
					if (!oldBases.contains(newBase)) {
						Model.getExtensionMechanismsHelper().addBaseClass(stereo,newBase);
					}else {
						if (newBase != null&&newBase.equals("")) {
							Model.getExtensionMechanismsHelper().addBaseClass(stereo,"ModelElement");
						}
					}
				}
			}
		}
	}
}



