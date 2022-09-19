package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewPseudoState extends AbstractActionNewModelElement {
	private Object kind;
	public ActionNewPseudoState() {
		super();
		putValue(Action.NAME,Translator.localize("button.new-pseudostate"));
	}
	public ActionNewPseudoState(Object k,String n) {
		super();
		kind = k;
		putValue(Action.NAME,Translator.localize(n));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object target = TargetManager.getInstance().getModelTarget();
		Object ps = Model.getStateMachinesFactory().buildPseudoState(target);
		if (kind != null) {
			Model.getCoreHelper().setKind(ps,kind);
		}
	}
}



