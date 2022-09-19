package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLRecurrenceExpressionModel;
import org.argouml.uml.ui.UMLScriptExpressionModel;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public abstract class PropPanelAction extends PropPanelModelElement {
	protected JScrollPane argumentsScroll;
	public PropPanelAction() {
		this("label.action",null);
	}
	public PropPanelAction(String name,ImageIcon icon) {
		super(name,icon);
		initialize();
	}
	public void initialize() {
		addField(Translator.localize("label.name"),getNameTextField());
		add(new UMLActionAsynchronousCheckBox());
		UMLExpressionModel2 scriptModel = new UMLScriptExpressionModel(this,"script");
		JPanel scriptPanel = createBorderPanel(Translator.localize("label.script"));
		scriptPanel.add(new JScrollPane(new UMLExpressionBodyField(scriptModel,true)));
		scriptPanel.add(new UMLExpressionLanguageField(scriptModel,false));
		add(scriptPanel);
		UMLExpressionModel2 recurrenceModel = new UMLRecurrenceExpressionModel(this,"recurrence");
		JPanel recurrencePanel = createBorderPanel(Translator.localize("label.recurrence"));
		recurrencePanel.add(new JScrollPane(new UMLExpressionBodyField(recurrenceModel,true)));
		recurrencePanel.add(new UMLExpressionLanguageField(recurrenceModel,false));
		add(recurrencePanel);
		addSeparator();
		JList argumentsList = new UMLLinkedList(new UMLActionArgumentListModel());
		argumentsList.setVisibleRowCount(5);
		argumentsScroll = new JScrollPane(argumentsList);
		addField(Translator.localize("label.arguments"),argumentsScroll);
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionCreateArgument());
		addAction(new ActionNewStereotype());
		addExtraActions();
		addAction(getDeleteAction());
	}
	protected void addExtraActions() {
	}
}

class ActionCreateArgument extends AbstractActionNewModelElement {
	public ActionCreateArgument() {
		super("button.new-argument");
		putValue(Action.NAME,Translator.localize("button.new-argument"));
		putValue(Action.SMALL_ICON,ResourceLoaderWrapper.lookupIcon("NewParameter"));
	}
	@Override public void actionPerformed(ActionEvent e) {
		Object t = TargetManager.getInstance().getTarget();
		if (Model.getFacade().isAAction(t)) {
			Object argument = Model.getCommonBehaviorFactory().createArgument();
			Model.getCommonBehaviorHelper().addActualArgument(t,argument);
			TargetManager.getInstance().setTarget(argument);
		}
	}
	private static final long serialVersionUID = -3455108052199995234l;
}



