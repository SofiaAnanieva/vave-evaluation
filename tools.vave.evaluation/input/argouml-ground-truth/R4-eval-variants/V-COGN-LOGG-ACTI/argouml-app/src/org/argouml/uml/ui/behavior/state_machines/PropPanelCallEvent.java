package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLSearchableComboBox;
import org.argouml.uml.ui.foundation.core.ActionNewParameter;


public class PropPanelCallEvent extends PropPanelEvent {
	public PropPanelCallEvent() {
		super("label.call-event",lookupIcon("CallEvent"));
	}
	@Override public void initialize() {
		super.initialize();
		UMLSearchableComboBox operationComboBox = new UMLCallEventOperationComboBox2(new UMLCallEventOperationComboBoxModel());
		addField("label.operations",new UMLComboBoxNavigator(Translator.localize("label.operation.navigate.tooltip"),operationComboBox));
		addAction(new ActionNewParameter());
		addAction(getDeleteAction());
	}
}

class UMLCallEventOperationComboBox2 extends UMLSearchableComboBox {
	public UMLCallEventOperationComboBox2(UMLComboBoxModel2 arg0) {
		super(arg0,null);
		setEditable(false);
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object source = e.getSource();
		if (source instanceof UMLComboBox2) {
			Object selected = ((UMLComboBox2) source).getSelectedItem();
			Object target = ((UMLComboBox2) source).getTarget();
			if (Model.getFacade().isACallEvent(target)&&Model.getFacade().isAOperation(selected)) {
				if (Model.getFacade().getOperation(target) != selected) {
					Model.getCommonBehaviorHelper().setOperation(target,selected);
				}
			}
		}
	}
}

class UMLCallEventOperationComboBoxModel extends UMLComboBoxModel2 {
	public UMLCallEventOperationComboBoxModel() {
		super("operation",true);
	}
	protected void buildModelList() {
		Object target = TargetManager.getInstance().getModelTarget();
		Collection ops = new ArrayList();
		if (Model.getFacade().isACallEvent(target)) {
			Object ns = Model.getFacade().getNamespace(target);
			if (Model.getFacade().isANamespace(ns)) {
				Collection classifiers = Model.getModelManagementHelper().getAllModelElementsOfKind(ns,Model.getMetaTypes().getClassifier());
				for (Object classifier:classifiers) {
					ops.addAll(Model.getFacade().getOperations(classifier));
				}
				for (Object importedElem:Model.getModelManagementHelper().getAllImportedElements(ns)) {
					if (Model.getFacade().isAClassifier(importedElem)) {
						ops.addAll(Model.getFacade().getOperations(importedElem));
					}
				}
			}
		}
		setElements(ops);
	}
	protected Object getSelectedModelElement() {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isACallEvent(target)) {
			return Model.getFacade().getOperation(target);
		}
		return null;
	}
	protected boolean isValidElement(Object element) {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isACallEvent(target)) {
			return element == Model.getFacade().getOperation(target);
		}
		return false;
	}
}



