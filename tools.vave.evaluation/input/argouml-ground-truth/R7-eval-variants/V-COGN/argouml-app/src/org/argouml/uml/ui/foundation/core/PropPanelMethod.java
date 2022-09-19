package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionNavigateOwner;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import org.tigris.gef.undo.UndoableAction;
import org.argouml.uml.ui.foundation.core.PropPanelFeature;
import org.argouml.uml.ui.foundation.core.UMLBehavioralFeatureQueryCheckBox;
import org.argouml.uml.ui.foundation.core.UMLFeatureOwnerScopeCheckBox;


public class PropPanelMethod extends PropPanelFeature {
	private UMLComboBox2 specificationComboBox;
	private static UMLMethodSpecificationComboBoxModel specificationComboBoxModel;
	public PropPanelMethod() {
		super("label.method",lookupIcon("Method"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.owner"),getOwnerScroll());
		addField(Translator.localize("label.specification"),new UMLComboBoxNavigator(Translator.localize("label.specification.navigate.tooltip"),getSpecificationComboBox()));
		add(getVisibilityPanel());
		JPanel modifiersPanel = createBorderPanel(Translator.localize("label.modifiers"));
		modifiersPanel.add(new UMLBehavioralFeatureQueryCheckBox());
		modifiersPanel.add(new UMLFeatureOwnerScopeCheckBox());
		add(modifiersPanel);
		addSeparator();
		UMLExpressionModel2 procedureModel = new UMLMethodProcedureExpressionModel(this,"");
		addField(Translator.localize("label.language"),new UMLExpressionLanguageField(procedureModel,false));
		JScrollPane bodyPane = new JScrollPane(new UMLExpressionBodyField(procedureModel,true));
		addField(Translator.localize("label.body"),bodyPane);
		addAction(new ActionNavigateOwner());
		addAction(getDeleteAction());
	}
	public UMLComboBox2 getSpecificationComboBox() {
		if (specificationComboBox == null) {
			if (specificationComboBoxModel == null) {
				specificationComboBoxModel = new UMLMethodSpecificationComboBoxModel();
			}
			specificationComboBox = new UMLComboBox2(specificationComboBoxModel,new ActionSetMethodSpecification());
		}
		return specificationComboBox;
	}
	private static class UMLMethodSpecificationComboBoxModel extends UMLComboBoxModel2 {
	public UMLMethodSpecificationComboBoxModel() {
		super("specification",false);
		Model.getPump().addClassModelEventListener(this,Model.getMetaTypes().getOperation(),"method");
	}
	protected boolean isValidElement(Object element) {
		Object specification = Model.getCoreHelper().getSpecification(getTarget());
		return specification == element;
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			removeAllElements();
			Object classifier = Model.getFacade().getOwner(getTarget());
			addAll(Model.getFacade().getOperations(classifier));
		}
	}
	protected Object getSelectedModelElement() {
		return Model.getCoreHelper().getSpecification(getTarget());
	}
	public void modelChanged(UmlChangeEvent evt) {
		if (evt instanceof AttributeChangeEvent) {
			if (evt.getPropertyName().equals("specification")) {
				if (evt.getSource() == getTarget()&&(getChangedElement(evt) != null)) {
					Object elem = getChangedElement(evt);
					setSelectedItem(elem);
				}
			}
		}
	}
}
	private static class ActionSetMethodSpecification extends UndoableAction {
	protected ActionSetMethodSpecification() {
		super(Translator.localize("Set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object source = e.getSource();
		Object oldOperation = null;
		Object newOperation = null;
		Object method = null;
		if (source instanceof UMLComboBox2) {
			UMLComboBox2 box = (UMLComboBox2) source;
			Object o = box.getTarget();
			if (Model.getFacade().isAMethod(o)) {
				method = o;
				oldOperation = Model.getCoreHelper().getSpecification(method);
			}
			o = box.getSelectedItem();
			if (Model.getFacade().isAOperation(o)) {
				newOperation = o;
			}
		}
		if (newOperation != oldOperation&&method != null) {
			Model.getCoreHelper().setSpecification(method,newOperation);
		}
	}
}
}

class UMLMethodProcedureExpressionModel extends UMLExpressionModel2 {
	public UMLMethodProcedureExpressionModel(UMLUserInterfaceContainer container,String propertyName) {
		super(container,propertyName);
	}
	public Object getExpression() {
		return Model.getFacade().getBody(TargetManager.getInstance().getTarget());
	}
	public void setExpression(Object expression) {
		Object target = TargetManager.getInstance().getTarget();
		if (target == null) {
			throw new IllegalStateException("There is no target for " + getContainer());
		}
		Model.getCoreHelper().setBody(target,expression);
	}
	public Object newExpression() {
		return Model.getDataTypesFactory().createProcedureExpression("","");
	}
}



