package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetFlowSource extends UndoableAction {
	private static final ActionSetFlowSource SINGLETON = new ActionSetFlowSource();
	protected ActionSetFlowSource() {
		super(Translator.localize("Set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof UMLComboBox2) {
			UMLComboBox2 source = (UMLComboBox2) e.getSource();
			Object target = source.getTarget();
			if (Model.getFacade().isAFlow(target)) {
				Object flow = target;
				Object old = null;
				if (!Model.getFacade().getSources(flow).isEmpty()) {
					old = Model.getFacade().getSources(flow).toArray()[0];
				}
				if (old != source.getSelectedItem()) {
					if (source.getSelectedItem() != null) {
						Collection sources = new ArrayList();
						sources.add(source.getSelectedItem());
						Model.getCoreHelper().setSources(flow,sources);
					}
				}
			}
		}
	}
	public static ActionSetFlowSource getInstance() {
		return SINGLETON;
	}
}



