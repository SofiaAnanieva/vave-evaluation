package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectSettings;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;
import org.argouml.notation.providers.uml.NotationUtilityUml;
import org.tigris.gef.undo.UndoableAction;


@Deprecated@UmlModelMutator class ActionAddStereotype extends UndoableAction {
	private Object modelElement;
	private Object stereotype;
	public ActionAddStereotype(Object me,Object st) {
		super(Translator.localize(buildString(st)),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize(buildString(st)));
		modelElement = me;
		stereotype = st;
	}
	private static String buildString(Object st) {
		Project p = ProjectManager.getManager().getCurrentProject();
		ProjectSettings ps = p.getProjectSettings();
		return NotationUtilityUml.generateStereotype(st,ps.getNotationSettings().isUseGuillemets());
	}
	@Override public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		if (Model.getFacade().getStereotypes(modelElement).contains(stereotype)) {
			Model.getCoreHelper().removeStereotype(modelElement,stereotype);
		}else {
			Model.getCoreHelper().addStereotype(modelElement,stereotype);
		}
	}
	@Override public Object getValue(String key) {
		if ("SELECTED".equals(key)) {
			if (Model.getFacade().getStereotypes(modelElement).contains(stereotype)) {
				return Boolean.TRUE;
			}else {
				return Boolean.FALSE;
			}
		}
		return super.getValue(key);
	}
}



