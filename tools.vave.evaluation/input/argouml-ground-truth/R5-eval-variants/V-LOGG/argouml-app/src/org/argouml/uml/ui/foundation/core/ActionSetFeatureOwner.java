package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetFeatureOwner extends UndoableAction {
	private static final ActionSetFeatureOwner SINGLETON = new ActionSetFeatureOwner();
	protected ActionSetFeatureOwner() {
		super(Translator.localize("Set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object source = e.getSource();
		Object oldClassifier = null;
		Object newClassifier = null;
		Object feature = null;
		if (source instanceof UMLComboBox2) {
			UMLComboBox2 box = (UMLComboBox2) source;
			Object o = box.getTarget();
			if (Model.getFacade().isAFeature(o)) {
				feature = o;
				oldClassifier = Model.getFacade().getOwner(feature);
			}
			o = box.getSelectedItem();
			if (Model.getFacade().isAClassifier(o)) {
				newClassifier = o;
			}
		}
		if (newClassifier != oldClassifier&&feature != null&&newClassifier != null) {
			Model.getCoreHelper().setOwner(feature,newClassifier);
		}
	}
	public static ActionSetFeatureOwner getInstance() {
		return SINGLETON;
	}
}



