package org.argouml.uml.ui.foundation.core;

import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.ActionNavigateUpNextDown;
import org.argouml.uml.ui.ActionNavigateUpPreviousDown;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;


public class PropPanelParameter extends PropPanelModelElement {
	private static final long serialVersionUID = -1207518946939283220l;
	private JPanel behFeatureScroll;
	private static UMLParameterBehavioralFeatListModel behFeatureModel;
	public PropPanelParameter() {
		super("label.parameter",lookupIcon("Parameter"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.owner"),getBehavioralFeatureScroll());
		addSeparator();
		addField(Translator.localize("label.type"),new UMLComboBox2(new UMLParameterTypeComboBoxModel(),ActionSetParameterType.getInstance()));
		UMLExpressionModel2 defaultModel = new UMLDefaultValueExpressionModel(this,"defaultValue");
		JPanel defaultPanel = createBorderPanel(Translator.localize("label.parameter.default-value"));
		defaultPanel.add(new JScrollPane(new UMLExpressionBodyField(defaultModel,true)));
		defaultPanel.add(new UMLExpressionLanguageField(defaultModel,false));
		add(defaultPanel);
		add(new UMLParameterDirectionKindRadioButtonPanel(Translator.localize("label.parameter.kind"),true));
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionNavigateUpPreviousDown() {
			public List getFamily(Object parent) {
				return Model.getFacade().getParametersList(parent);
			}
			public Object getParent(Object child) {
				return Model.getFacade().getModelElementContainer(child);
			}
			public boolean isEnabled() {
				return(Model.getFacade().isABehavioralFeature(getTarget())||Model.getFacade().isAEvent(getTarget()))&&super.isEnabled();
			}
		});
		addAction(new ActionNavigateUpNextDown() {
			public List getFamily(Object parent) {
				return Model.getFacade().getParametersList(parent);
			}
			public Object getParent(Object child) {
				return Model.getFacade().getModelElementContainer(child);
			}
			public boolean isEnabled() {
				return(Model.getFacade().isABehavioralFeature(getTarget())||Model.getFacade().isAEvent(getTarget()))&&super.isEnabled();
			}
		});
		addAction(new ActionNewParameter());
		addAction(new ActionAddDataType());
		addAction(new ActionAddEnumeration());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
	public JPanel getBehavioralFeatureScroll() {
		if (behFeatureScroll == null) {
			if (behFeatureModel == null) {
				behFeatureModel = new UMLParameterBehavioralFeatListModel();
			}
			behFeatureScroll = getSingleRowScroll(behFeatureModel);
		}
		return behFeatureScroll;
	}
}

class UMLDefaultValueExpressionModel extends UMLExpressionModel2 {
	public UMLDefaultValueExpressionModel(UMLUserInterfaceContainer container,String propertyName) {
		super(container,propertyName);
	}
	public Object getExpression() {
		Object target = TargetManager.getInstance().getTarget();
		if (target == null) {
			return null;
		}
		return Model.getFacade().getDefaultValue(target);
	}
	public void setExpression(Object expression) {
		Object target = TargetManager.getInstance().getTarget();
		if (target == null) {
			throw new IllegalStateException("There is no target for " + getContainer());
		}
		Model.getCoreHelper().setDefaultValue(target,expression);
	}
	public Object newExpression() {
		return Model.getDataTypesFactory().createExpression("","");
	}
}



