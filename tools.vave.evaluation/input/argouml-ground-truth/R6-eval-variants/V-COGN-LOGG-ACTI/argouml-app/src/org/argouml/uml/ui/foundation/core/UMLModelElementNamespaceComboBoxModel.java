package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.util.PathComparator;


public class UMLModelElementNamespaceComboBoxModel extends UMLComboBoxModel2 {
	private static final Logger LOG = Logger.getLogger(UMLModelElementNamespaceComboBoxModel.class);
	private static final long serialVersionUID = -775116993155949065l;
	public UMLModelElementNamespaceComboBoxModel() {
		super("namespace",true);
		Model.getPump().addClassModelEventListener(this,Model.getMetaTypes().getNamespace(),"ownedElement");
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isANamespace(o)&&Model.getCoreHelper().isValidNamespace(getTarget(),o);
	}
	@Override protected void buildMinimalModelList() {
		Object target = getTarget();
		Collection c = new ArrayList(1);
		if (target != null) {
			Object namespace = Model.getFacade().getNamespace(target);
			if (namespace != null&&!c.contains(namespace)) {
				c.add(namespace);
			}
		}
		setElements(c);
		setModelInvalid();
	}
	protected void buildModelList() {
		Set<Object>elements = new TreeSet<Object>(new PathComparator());
		Object model = ProjectManager.getManager().getCurrentProject().getRoot();
		Object target = getTarget();
		elements.addAll(Model.getCoreHelper().getAllPossibleNamespaces(target,model));
		if (target != null) {
			Object namespace = Model.getFacade().getNamespace(target);
			if (namespace != null&&!elements.contains(namespace)) {
				elements.add(namespace);
				LOG.warn("The current namespace is not a valid one!");
			}
		}
		removeAllElements();
		addAll(elements);
	}
	protected Object getSelectedModelElement() {
		if (getTarget() != null) {
			return Model.getFacade().getNamespace(getTarget());
		}
		return null;
	}
	@Override public void modelChanged(UmlChangeEvent evt) {
		Object t = getTarget();
		if (t != null&&evt.getSource() == t&&evt.getNewValue() != null) {
			buildMinimalModelList();
			setSelectedItem(getSelectedModelElement());
		}
	}
	@Override protected boolean isLazy() {
		return true;
	}
}



