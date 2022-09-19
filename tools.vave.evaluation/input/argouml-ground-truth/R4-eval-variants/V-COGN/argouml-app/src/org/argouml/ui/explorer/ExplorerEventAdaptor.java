package org.argouml.ui.explorer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingUtilities;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoProfileEvent;
import org.argouml.application.events.ArgoProfileEventListener;
import org.argouml.configuration.Configuration;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.argouml.notation.Notation;


public final class ExplorerEventAdaptor implements PropertyChangeListener {
	private static ExplorerEventAdaptor instance;
	private TreeModelUMLEventListener treeModel;
	public static ExplorerEventAdaptor getInstance() {
		if (instance == null) {
			instance = new ExplorerEventAdaptor();
		}
		return instance;
	}
	private ExplorerEventAdaptor() {
		Configuration.addListener(Notation.KEY_USE_GUILLEMOTS,this);
		Configuration.addListener(Notation.KEY_SHOW_STEREOTYPES,this);
		ProjectManager.getManager().addPropertyChangeListener(this);
		Model.getPump().addClassModelEventListener(this,Model.getMetaTypes().getModelElement(),(String[]) null);
		ArgoEventPump.addListener(ArgoEventTypes.ANY_PROFILE_EVENT,new ProfileChangeListener());
	}
	@Deprecated public void structureChanged() {
		if (treeModel == null) {
			return;
		}
		treeModel.structureChanged();
	}
	public void modelElementAdded(Object element) {
		if (treeModel == null) {
			return;
		}
		treeModel.modelElementAdded(element);
	}
	public void modelElementChanged(Object element) {
		if (treeModel == null) {
			return;
		}
		treeModel.modelElementChanged(element);
	}
	public void setTreeModelUMLEventListener(TreeModelUMLEventListener newTreeModel) {
		treeModel = newTreeModel;
	}
	public void propertyChange(final PropertyChangeEvent pce) {
		if (treeModel == null) {
			return;
		}
		if (pce instanceof UmlChangeEvent) {
			Runnable doWorkRunnable = new Runnable() {
			public void run() {
				try {
					modelChanged((UmlChangeEvent) pce);
				}catch (InvalidElementException e) {
				}
			}
		};
			SwingUtilities.invokeLater(doWorkRunnable);
		}else if (pce.getPropertyName().equals(ProjectManager.CURRENT_PROJECT_PROPERTY_NAME)||pce.getPropertyName().equals(ProjectManager.OPEN_PROJECTS_PROPERTY)) {
			if (pce.getNewValue() != null) {
				treeModel.structureChanged();
			}
			return;
		}else if (Notation.KEY_USE_GUILLEMOTS.isChangedProperty(pce)||Notation.KEY_SHOW_STEREOTYPES.isChangedProperty(pce)) {
			treeModel.structureChanged();
		}else if (pce.getSource()instanceof ProjectManager) {
			if ("remove".equals(pce.getPropertyName())) {
				treeModel.modelElementRemoved(pce.getOldValue());
			}
		}
	}
	private void modelChanged(UmlChangeEvent event) {
		if (event instanceof AttributeChangeEvent) {
			treeModel.modelElementChanged(event.getSource());
		}else if (event instanceof RemoveAssociationEvent) {
			if (!("namespace".equals(event.getPropertyName()))) {
				treeModel.modelElementChanged(((RemoveAssociationEvent) event).getChangedValue());
			}
		}else if (event instanceof AddAssociationEvent) {
			if (!("namespace".equals(event.getPropertyName()))) {
				treeModel.modelElementAdded(((AddAssociationEvent) event).getSource());
			}
		}else if (event instanceof DeleteInstanceEvent) {
			treeModel.modelElementRemoved(((DeleteInstanceEvent) event).getSource());
		}
	}
	class ProfileChangeListener implements ArgoProfileEventListener {
		public void profileAdded(ArgoProfileEvent e) {
			structureChanged();
		}
		public void profileRemoved(ArgoProfileEvent e) {
			structureChanged();
		}
	}
}



