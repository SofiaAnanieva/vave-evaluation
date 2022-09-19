package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.List;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLRadioButtonPanel;


public class UMLOperationConcurrencyRadioButtonPanel extends UMLRadioButtonPanel {
	private static List<String[]>labelTextsAndActionCommands;
	private static List<String[]>getCommands() {
		if (labelTextsAndActionCommands == null) {
			labelTextsAndActionCommands = new ArrayList<String[]>();
			labelTextsAndActionCommands.add(new String[] {Translator.localize("label.concurrency-sequential"),ActionSetOperationConcurrencyKind.SEQUENTIAL_COMMAND});
			labelTextsAndActionCommands.add(new String[] {Translator.localize("label.concurrency-guarded"),ActionSetOperationConcurrencyKind.GUARDED_COMMAND});
			labelTextsAndActionCommands.add(new String[] {Translator.localize("label.concurrency-concurrent"),ActionSetOperationConcurrencyKind.CONCURRENT_COMMAND});
		}
		return labelTextsAndActionCommands;
	}
	public UMLOperationConcurrencyRadioButtonPanel(String title,boolean horizontal) {
		super(title,getCommands(),"concurrency",ActionSetOperationConcurrencyKind.getInstance(),horizontal);
	}
	public void buildModel() {
		if (getTarget() != null) {
			Object target = getTarget();
			Object kind = Model.getFacade().getConcurrency(target);
			if (kind == null) {
				setSelected(null);
			}else if (kind.equals(Model.getConcurrencyKind().getSequential())) {
				setSelected(ActionSetOperationConcurrencyKind.SEQUENTIAL_COMMAND);
			}else if (kind.equals(Model.getConcurrencyKind().getGuarded())) {
				setSelected(ActionSetOperationConcurrencyKind.GUARDED_COMMAND);
			}else if (kind.equals(Model.getConcurrencyKind().getConcurrent())) {
				setSelected(ActionSetOperationConcurrencyKind.CONCURRENT_COMMAND);
			}else {
				setSelected(ActionSetOperationConcurrencyKind.SEQUENTIAL_COMMAND);
			}
		}
	}
}



