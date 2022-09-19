package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionNavigateOwner;
import org.argouml.uml.ui.ActionNavigateUpNextDown;
import org.argouml.uml.ui.ActionNavigateUpPreviousDown;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLTextArea2;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.uml.ui.foundation.core.PropPanelFeature;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementAbstractCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementLeafCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementRootCheckBox;
import org.argouml.uml.ui.foundation.core.UMLBehavioralFeatureQueryCheckBox;
import org.argouml.uml.ui.foundation.core.UMLFeatureOwnerScopeCheckBox;


public class PropPanelOperation extends PropPanelFeature {
	private static final long serialVersionUID = -8231585002039922761l;
	public PropPanelOperation() {
		super("label.operation",lookupIcon("Operation"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.owner"),getOwnerScroll());
		addField(Translator.localize("label.parameters"),new JScrollPane(new UMLLinkedList(new UMLClassifierParameterListModel(),true,false)));
		addSeparator();
		add(getVisibilityPanel());
		JPanel modifiersPanel = createBorderPanel(Translator.localize("label.modifiers"));
		modifiersPanel.add(new UMLGeneralizableElementAbstractCheckBox());
		modifiersPanel.add(new UMLGeneralizableElementLeafCheckBox());
		modifiersPanel.add(new UMLGeneralizableElementRootCheckBox());
		modifiersPanel.add(new UMLBehavioralFeatureQueryCheckBox());
		modifiersPanel.add(new UMLFeatureOwnerScopeCheckBox());
		add(modifiersPanel);
		add(new UMLOperationConcurrencyRadioButtonPanel(Translator.localize("label.concurrency"),true));
		addSeparator();
		addField(Translator.localize("label.raisedsignals"),new JScrollPane(new UMLLinkedList(new UMLOperationRaisedSignalsListModel())));
		addField(Translator.localize("label.methods"),new JScrollPane(new UMLLinkedList(new UMLOperationMethodsListModel())));
		UMLTextArea2 osta = new UMLTextArea2(new UMLOperationSpecificationDocument());
		osta.setRows(3);
		addField(Translator.localize("label.specification"),new JScrollPane(osta));
		addAction(new ActionNavigateOwner());
		addAction(new ActionNavigateUpPreviousDown() {
			public List getFamily(Object parent) {
				return Model.getFacade().getOperations(parent);
			}
			public Object getParent(Object child) {
				return Model.getFacade().getOwner(child);
			}
		});
		addAction(new ActionNavigateUpNextDown() {
			public List getFamily(Object parent) {
				return Model.getFacade().getOperations(parent);
			}
			public Object getParent(Object child) {
				return Model.getFacade().getOwner(child);
			}
		});
		addAction(new ActionAddOperation());
		addAction(new ActionNewParameter());
		addAction(new ActionNewRaisedSignal());
		addAction(new ActionNewMethod());
		addAction(new ActionAddDataType());
		addAction(new ActionAddEnumeration());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
	public void addRaisedSignal() {
		Object target = getTarget();
		if (Model.getFacade().isAOperation(target)) {
			Object oper = target;
			Object newSignal = Model.getCommonBehaviorFactory().createSignal();
			Model.getCoreHelper().addOwnedElement(Model.getFacade().getNamespace(Model.getFacade().getOwner(oper)),newSignal);
			Model.getCoreHelper().addRaisedSignal(oper,newSignal);
			TargetManager.getInstance().setTarget(newSignal);
		}
	}
	public void addMethod() {
		Object target = getTarget();
		if (Model.getFacade().isAOperation(target)) {
			Object oper = target;
			String name = Model.getFacade().getName(oper);
			Object newMethod = Model.getCoreFactory().buildMethod(name);
			Model.getCoreHelper().addMethod(oper,newMethod);
			Model.getCoreHelper().addFeature(Model.getFacade().getOwner(oper),newMethod);
			TargetManager.getInstance().setTarget(newMethod);
		}
	}
	private class ActionNewRaisedSignal extends AbstractActionNewModelElement {
	private static final long serialVersionUID = -2380798799656866520l;
	public ActionNewRaisedSignal() {
		super("button.new-raised-signal");
		putValue(Action.NAME,Translator.localize("button.new-raised-signal"));
		Icon icon = ResourceLoaderWrapper.lookupIcon("SignalSending");
		putValue(Action.SMALL_ICON,icon);
	}
	@Override public void actionPerformed(ActionEvent e) {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isAOperation(target)) {
			addRaisedSignal();
			super.actionPerformed(e);
		}
	}
}
	private class ActionNewMethod extends AbstractActionNewModelElement {
	private static final long serialVersionUID = 1605755146025527381l;
	public ActionNewMethod() {
		super("button.new-method");
		putValue(Action.NAME,Translator.localize("button.new-method"));
		Icon icon = ResourceLoaderWrapper.lookupIcon("Method");
		putValue(Action.SMALL_ICON,icon);
	}
	@Override public boolean isEnabled() {
		Object target = TargetManager.getInstance().getModelTarget();
		boolean result = true;
		if (Model.getFacade().isAOperation(target)) {
			Object owner = Model.getFacade().getOwner(target);
			if (owner == null||Model.getFacade().isAInterface(owner)) {
				result = false;
			}
		}
		return super.isEnabled()&&result;
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isAOperation(target)) {
			addMethod();
		}
	}
}
	@Override protected Object getDisplayNamespace() {
		Object namespace = null;
		Object target = getTarget();
		if (Model.getFacade().isAAttribute(target)) {
			if (Model.getFacade().getOwner(target) != null) {
				namespace = Model.getFacade().getNamespace(Model.getFacade().getOwner(target));
			}
		}
		return namespace;
	}
}



