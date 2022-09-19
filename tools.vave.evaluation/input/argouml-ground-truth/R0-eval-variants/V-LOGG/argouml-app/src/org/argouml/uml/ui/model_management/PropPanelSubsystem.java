package org.argouml.uml.ui.model_management;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.UMLClassifierFeatureListModel;


public class PropPanelSubsystem extends PropPanelPackage {
	private JScrollPane featureScroll;
	private static UMLClassifierFeatureListModel featureListModel = new UMLClassifierFeatureListModel();
	public PropPanelSubsystem() {
		super("label.subsystem",lookupIcon("Subsystem"));
		addField(Translator.localize("label.available-features"),getFeatureScroll());
		addAction(new ActionNewOperation());
	}
	private static class ActionNewOperation extends AbstractActionNewModelElement {
	private static final String ACTION_KEY = "button.new-operation";
	public ActionNewOperation() {
		super(ACTION_KEY);
		putValue(Action.NAME,Translator.localize(ACTION_KEY));
	}
	@Override public void actionPerformed(ActionEvent e) {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isAClassifier(target)) {
			Project p = ProjectManager.getManager().getCurrentProject();
			Object returnType = p.getDefaultReturnType();
			Object newOper = Model.getCoreFactory().buildOperation(target,returnType);
			TargetManager.getInstance().setTarget(newOper);
			super.actionPerformed(e);
		}
	}
	private static final long serialVersionUID = -5149342278246959597l;
}
	public JScrollPane getFeatureScroll() {
		if (featureScroll == null) {
			JList list = new UMLLinkedList(featureListModel);
			featureScroll = new JScrollPane(list);
		}
		return featureScroll;
	}
	private static final long serialVersionUID = -8616239241648089917l;
}



