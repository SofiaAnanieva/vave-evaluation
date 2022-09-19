package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.util.PathComparator;


public class UMLTagDefinitionComboBoxModel extends UMLComboBoxModel2 {
	private static final Logger LOG = Logger.getLogger(UMLTagDefinitionComboBoxModel.class);
	public UMLTagDefinitionComboBoxModel() {
		super("stereotype",false);
	}
	@Override protected void addOtherModelEventListeners(Object target) {
		Model.getPump().addClassModelEventListener(this,Model.getMetaTypes().getTagDefinition(),(String[]) null);
	}
	@Override protected void removeOtherModelEventListeners(Object target) {
		Model.getPump().addClassModelEventListener(this,Model.getMetaTypes().getTagDefinition(),(String[]) null);
	}
	@Override public void modelChanged(UmlChangeEvent evt) {
		if (Model.getFacade().isATagDefinition(evt.getSource())) {
			LOG.debug("Got TagDefinition event " + evt.toString());
			setModelInvalid();
		}else if ("stereotype".equals(evt.getPropertyName())) {
			LOG.debug("Got stereotype event " + evt.toString());
			setModelInvalid();
		}else {
			LOG.debug("Got other event " + evt.toString());
		}
	}
	@Override public boolean isLazy() {
		return true;
	}
	protected boolean isValidElement(Object element) {
		Object owner = Model.getFacade().getOwner(element);
		return(Model.getFacade().isATagDefinition(element)&&(owner == null||Model.getFacade().getStereotypes(getTarget()).contains(owner)));
	}
	@Override public void setSelectedItem(Object o) {
		setFireListEvents(false);
		super.setSelectedItem(o);
		setFireListEvents(true);
	}
	protected void buildModelList() {
		removeAllElements();
		Object target = getTarget();
		addAll(getApplicableTagDefinitions(target));
	}
	protected Object getSelectedModelElement() {
		return getSelectedItem();
	}
	private Collection getApplicableTagDefinitions(Object element) {
		Set<List<String>>paths = new HashSet<List<String>>();
		Set<Object>availableTagDefs = new TreeSet<Object>(new PathComparator());
		Collection stereotypes = Model.getFacade().getStereotypes(element);
		Project project = ProjectManager.getManager().getCurrentProject();
		for (Object model:project.getModels()) {
			addAllUniqueModelElementsFrom(availableTagDefs,paths,Model.getModelManagementHelper().getAllModelElementsOfKind(model,Model.getMetaTypes().getTagDefinition()));
		}
		addAllUniqueModelElementsFrom(availableTagDefs,paths,project.getProfileConfiguration().findByMetaType(Model.getMetaTypes().getTagDefinition()));
		List notValids = new ArrayList();
		for (Object tagDef:availableTagDefs) {
			Object owner = Model.getFacade().getOwner(tagDef);
			if (owner != null&&!stereotypes.contains(owner)) {
				notValids.add(tagDef);
			}
		}
		int size = availableTagDefs.size();
		availableTagDefs.removeAll(notValids);
		int delta = size - availableTagDefs.size();
		return availableTagDefs;
	}
	private static void addAllUniqueModelElementsFrom(Set elements,Set<List<String>>paths,Collection sources) {
		for (Object source:sources) {
			List<String>path = Model.getModelManagementHelper().getPathList(source);
			if (!paths.contains(path)) {
				paths.add(path);
				elements.add(source);
			}
		}
	}
	private static final long serialVersionUID = -4194727034416788372l;
}



