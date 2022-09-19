package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.tigris.gef.presentation.Fig;


public class ActionNewStereotype extends AbstractActionNewModelElement {
	public ActionNewStereotype() {
		super("button.new-stereotype");
		putValue(Action.NAME,Translator.localize("button.new-stereotype"));
	}
	public void actionPerformed(ActionEvent e) {
		Object t = TargetManager.getInstance().getTarget();
		if (t instanceof Fig)t = ((Fig) t).getOwner();
		Project p = ProjectManager.getManager().getCurrentProject();
		Object model = p.getModel();
		Collection models = p.getModels();
		Object newStereo = Model.getExtensionMechanismsFactory().buildStereotype(Model.getFacade().isAModelElement(t)?t:null,(String) null,model,models);
		if (Model.getFacade().isAModelElement(t)) {
			Object ns = Model.getFacade().getNamespace(t);
			if (Model.getFacade().isANamespace(ns)) {
				Model.getCoreHelper().setNamespace(newStereo,ns);
			}
		}
		TargetManager.getInstance().setTarget(newStereo);
		super.actionPerformed(e);
	}
}



