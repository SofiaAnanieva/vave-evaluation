package org.argouml.uml.ui.behavior.collaborations;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.UMLSingleRowSelector;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelMessage extends PropPanelModelElement {
	public PropPanelMessage() {
		super("label.message",lookupIcon("Message"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.interaction"),new UMLSingleRowSelector(new UMLMessageInteractionListModel()));
		addField(Translator.localize("label.sender"),new UMLSingleRowSelector(new UMLMessageSenderListModel()));
		addField(Translator.localize("label.receiver"),new UMLSingleRowSelector(new UMLMessageReceiverListModel()));
		addSeparator();
		addField(Translator.localize("label.activator"),new UMLMessageActivatorComboBox(this,new UMLMessageActivatorComboBoxModel()));
		addField(Translator.localize("label.action"),new UMLSingleRowSelector(new UMLMessageActionListModel()));
		JScrollPane predecessorScroll = new JScrollPane(new UMLMutableLinkedList(new UMLMessagePredecessorListModel(),ActionAddMessagePredecessor.getInstance(),null));
		addField(Translator.localize("label.predecessor"),predecessorScroll);
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionToolNewAction());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
	private static class ActionToolNewAction extends AbstractActionNewModelElement {
	public ActionToolNewAction() {
		super("button.new-action");
		putValue(Action.NAME,Translator.localize("button.new-action"));
		Icon icon = ResourceLoaderWrapper.lookupIcon("CallAction");
		putValue(Action.SMALL_ICON,icon);
	}
	@Override public void actionPerformed(ActionEvent e) {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isAMessage(target)) {
			Model.getCommonBehaviorFactory().buildAction(target);
			super.actionPerformed(e);
		}
	}
	private static final long serialVersionUID = -6588197204256288453l;
}
	private static final long serialVersionUID = -8433911715875762175l;
}



