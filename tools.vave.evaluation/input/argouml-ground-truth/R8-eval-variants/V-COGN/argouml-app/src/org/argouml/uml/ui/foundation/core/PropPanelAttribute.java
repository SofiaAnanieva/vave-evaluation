package org.argouml.uml.ui.foundation.core;

import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.ActionNavigateUpNextDown;
import org.argouml.uml.ui.ActionNavigateUpPreviousDown;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.uml.ui.foundation.core.PropPanelStructuralFeature;
import org.argouml.uml.ui.UMLRadioButtonPanel;
import org.argouml.uml.ui.foundation.core.UMLFeatureOwnerScopeCheckBox;


public class PropPanelAttribute extends PropPanelStructuralFeature {
	private static final long serialVersionUID = -5596689167193050170l;
	public PropPanelAttribute() {
		super("label.attribute",lookupIcon("Attribute"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.type"),new UMLComboBoxNavigator(Translator.localize("label.class.navigate.tooltip"),getTypeComboBox()));
		addField(Translator.localize("label.multiplicity"),getMultiplicityComboBox());
		addField(Translator.localize("label.owner"),getOwnerScroll());
		add(getVisibilityPanel());
		addSeparator();
		add(getChangeabilityRadioButtonPanel());
		JPanel modifiersPanel = createBorderPanel(Translator.localize("label.modifiers"));
		modifiersPanel.add(getOwnerScopeCheckbox());
		add(modifiersPanel);
		UMLExpressionModel2 initialModel = new UMLInitialValueExpressionModel(this,"initialValue");
		JPanel initialPanel = createBorderPanel(Translator.localize("label.initial-value"));
		JScrollPane jsp = new JScrollPane(new UMLExpressionBodyField(initialModel,true));
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		initialPanel.add(jsp);
		initialPanel.add(new UMLExpressionLanguageField(initialModel,false));
		add(initialPanel);
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionNavigateUpPreviousDown() {
			public List getFamily(Object parent) {
				if (Model.getFacade().isAAssociationEnd(parent)) {
					return Model.getFacade().getQualifiers(parent);
				}
				return Model.getFacade().getAttributes(parent);
			}
			public Object getParent(Object child) {
				return Model.getFacade().getModelElementContainer(child);
			}
		});
		addAction(new ActionNavigateUpNextDown() {
			public List getFamily(Object parent) {
				if (Model.getFacade().isAAssociationEnd(parent)) {
					return Model.getFacade().getQualifiers(parent);
				}
				return Model.getFacade().getAttributes(parent);
			}
			public Object getParent(Object child) {
				return Model.getFacade().getModelElementContainer(child);
			}
		});
		addAction(new ActionAddAttribute());
		addAction(new ActionAddDataType());
		addAction(new ActionAddEnumeration());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
	private class UMLInitialValueExpressionModel extends UMLExpressionModel2 {
	public UMLInitialValueExpressionModel(UMLUserInterfaceContainer container,String propertyName) {
		super(container,propertyName);
	}
	public Object getExpression() {
		Object target = getTarget();
		if (target == null) {
			return null;
		}
		return Model.getFacade().getInitialValue(target);
	}
	public void setExpression(Object expression) {
		Object target = getTarget();
		if (target == null) {
			throw new IllegalStateException("There is no target for " + getContainer());
		}
		Model.getCoreHelper().setInitialValue(target,expression);
	}
	public Object newExpression() {
		return Model.getDataTypesFactory().createExpression("","");
	}
}
}



