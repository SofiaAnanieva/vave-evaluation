package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBoxModel2;


public class UMLMetaClassComboBoxModel extends UMLComboBoxModel2 {
	private List<String>metaClasses;
	public UMLMetaClassComboBoxModel() {
		super("tagType",true);
		Collection<String>tmpMetaClasses = Model.getCoreHelper().getAllMetatypeNames();
		if (tmpMetaClasses instanceof List) {
			metaClasses = (List<String>) tmpMetaClasses;
		}else {
			metaClasses = new LinkedList<String>(tmpMetaClasses);
		}
		tmpMetaClasses.addAll(Model.getCoreHelper().getAllMetaDatatypeNames());
		try {
			Collections.sort(metaClasses);
		}catch (UnsupportedOperationException e) {
			metaClasses = new LinkedList<String>(tmpMetaClasses);
			Collections.sort(metaClasses);
		}
	}
	@Override protected Object getSelectedModelElement() {
		if (getTarget() != null) {
			return Model.getFacade().getType(getTarget());
		}
		return null;
	}
	protected void buildModelList() {
		setElements(metaClasses);
	}
	protected boolean isValidElement(Object element) {
		return metaClasses.contains(element);
	}
}



