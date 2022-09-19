package org.argouml.uml.ui.model_management;

import java.beans.PropertyChangeEvent;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


class UMLClassifierPackageImportsListModel extends UMLModelElementListModel2 {
	public UMLClassifierPackageImportsListModel() {
		super("elementImport");
	}
	protected void buildModelList() {
		setAllElements(Model.getFacade().getImportedElements(getTarget()));
	}
	protected boolean isValidElement(Object elem) {
		if (!Model.getFacade().isAElementImport(elem)) {
			return false;
		}
		return Model.getFacade().getPackage(elem) == getTarget();
	}
	public void propertyChange(PropertyChangeEvent e) {
		if (isValidEvent(e)) {
			removeAllElements();
			setBuildingModel(true);
			buildModelList();
			setBuildingModel(false);
			if (getSize() > 0) {
				fireIntervalAdded(this,0,getSize() - 1);
			}
		}
	}
}



