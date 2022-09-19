package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.argouml.i18n.Translator;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLSearchableComboBox;
import org.tigris.gef.undo.UndoableAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelAction;


public class PropPanelCallAction extends PropPanelAction {
	public PropPanelCallAction() {
		super("label.call-action",lookupIcon("CallAction"));
	}
	@Override public void initialize() {
		super.initialize();
		UMLSearchableComboBox operationComboBox = new UMLCallActionOperationComboBox2(new UMLCallActionOperationComboBoxModel());
		addFieldBefore(Translator.localize("label.operation"),new UMLComboBoxNavigator(Translator.localize("label.operation.navigate.tooltip"),operationComboBox),argumentsScroll);
	}
	private static class UMLCallActionOperationComboBox2 extends UMLSearchableComboBox {
	public UMLCallActionOperationComboBox2(UMLComboBoxModel2 arg0) {
		super(arg0,new SetActionOperationAction());
		setEditable(false);
	}
	private static final long serialVersionUID = 1453984990567492914l;
}
	private static class SetActionOperationAction extends UndoableAction {
	public SetActionOperationAction() {
		super("");
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object source = e.getSource();
		if (source instanceof UMLComboBox2) {
			Object selected = ((UMLComboBox2) source).getSelectedItem();
			Object target = ((UMLComboBox2) source).getTarget();
			if (Model.getFacade().isACallAction(target)&&Model.getFacade().isAOperation(selected)) {
				if (Model.getFacade().getOperation(target) != selected) {
					Model.getCommonBehaviorHelper().setOperation(target,selected);
				}
			}
		}
	}
	private static final long serialVersionUID = -3574312020866131632l;
}
	private static class UMLCallActionOperationComboBoxModel extends UMLComboBoxModel2 {
	public UMLCallActionOperationComboBoxModel() {
		super("operation",true);
	}
	protected void buildModelList() {
		Object target = TargetManager.getInstance().getModelTarget();
		Collection ops = new ArrayList();
		if (Model.getFacade().isACallAction(target)) {
			Object ns = Model.getFacade().getModelElementContainer(target);
			while (!Model.getFacade().isAPackage(ns)) {
				ns = Model.getFacade().getModelElementContainer(ns);
				if (ns == null) {
					break;
				}
			}
			if (Model.getFacade().isANamespace(ns)) {
				Collection c = Model.getModelManagementHelper().getAllModelElementsOfKind(ns,Model.getMetaTypes().getClassifier());
				Iterator i = c.iterator();
				while (i.hasNext()) {
					ops.addAll(Model.getFacade().getOperations(i.next()));
				}
			}
			Object current = Model.getFacade().getOperation(target);
			if (Model.getFacade().isAOperation(current)) {
				if (!ops.contains(current)) {
					ops.add(current);
				}
			}
		}
		setElements(ops);
	}
	protected Object getSelectedModelElement() {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isACallAction(target)) {
			return Model.getFacade().getOperation(target);
		}
		return null;
	}
	protected boolean isValidElement(Object element) {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isACallAction(target)) {
			return element == Model.getFacade().getOperation(target);
		}
		return false;
	}
	@Override public void modelChanged(UmlChangeEvent evt) {
		if (evt instanceof AttributeChangeEvent) {
			if (evt.getPropertyName().equals("operation")) {
				if (evt.getSource() == getTarget()&&(getChangedElement(evt) != null)) {
					Object elem = getChangedElement(evt);
					setSelectedItem(elem);
				}
			}
		}
	}
	private static final long serialVersionUID = 7752478921939209157l;
}
	private static final long serialVersionUID = 6998109319912301992l;
}



