package org.argouml.uml.ui.model_management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;


class ActionAddPackageImport extends AbstractActionAddModelElement2 {
	ActionAddPackageImport() {
			super();
		}
	protected List getChoices() {
		List vec = new ArrayList();
		vec.addAll(Model.getModelManagementHelper().getAllPossibleImports(getTarget()));
		return vec;
	}
	protected List getSelected() {
		List vec = new ArrayList();
		vec.addAll(Model.getFacade().getImportedElements(getTarget()));
		return vec;
	}
	protected String getDialogTitle() {
		return Translator.localize("dialog.title.add-imported-elements");
	}
	@Override protected void doIt(Collection selected) {
		Object pack = getTarget();
		Model.getModelManagementHelper().setImportedElements(pack,selected);
	}
}



