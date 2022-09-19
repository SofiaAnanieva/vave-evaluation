package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JRadioButton;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLRadioButtonPanel;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetAssociationEndAggregation extends UndoableAction {
	private static final ActionSetAssociationEndAggregation SINGLETON = new ActionSetAssociationEndAggregation();
	public static final String AGGREGATE_COMMAND = "aggregate";
	public static final String COMPOSITE_COMMAND = "composite";
	public static final String NONE_COMMAND = "none";
	protected ActionSetAssociationEndAggregation() {
		super(Translator.localize("action.set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof JRadioButton) {
			JRadioButton source = (JRadioButton) e.getSource();
			String actionCommand = source.getActionCommand();
			Object target = ((UMLRadioButtonPanel) source.getParent()).getTarget();
			if (Model.getFacade().isAAssociationEnd(target)) {
				Object m = target;
				Object kind = null;
				if (actionCommand.equals(AGGREGATE_COMMAND)) {
					kind = Model.getAggregationKind().getAggregate();
				}else if (actionCommand.equals(COMPOSITE_COMMAND)) {
					kind = Model.getAggregationKind().getComposite();
				}else {
					kind = Model.getAggregationKind().getNone();
				}
				Model.getCoreHelper().setAggregation(m,kind);
			}
		}
	}
	public static ActionSetAssociationEndAggregation getInstance() {
		return SINGLETON;
	}
}



