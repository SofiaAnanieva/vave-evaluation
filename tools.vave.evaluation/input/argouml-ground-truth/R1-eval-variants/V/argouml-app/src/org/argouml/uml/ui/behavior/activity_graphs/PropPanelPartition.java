package org.argouml.uml.ui.behavior.activity_graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelPartition extends PropPanelModelElement {
	private JScrollPane contentsScroll;
	private JPanel activityGraphScroll;
	private static UMLPartitionContentListModel contentListModel = new UMLPartitionContentListModel("contents");
	public PropPanelPartition() {
		super("label.partition-title",lookupIcon("Partition"));
		addField(Translator.localize("label.name"),getNameTextField());
		activityGraphScroll = getSingleRowScroll(new UMLPartitionActivityGraphListModel());
		addField(Translator.localize("label.activity-graph"),getActivityGraphField());
		addSeparator();
		addField(Translator.localize("label.contents"),getContentsField());
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
	protected JComponent getContentsField() {
		if (contentsScroll == null) {
			JList contentList = new UMLMutableLinkedList(contentListModel,new ActionAddPartitionContent(),null);
			contentsScroll = new JScrollPane(contentList);
		}
		return contentsScroll;
	}
	protected JPanel getActivityGraphField() {
		return activityGraphScroll;
	}
	class ActionAddPartitionContent extends AbstractActionAddModelElement2 {
		public ActionAddPartitionContent() {
			super();
			setMultiSelect(true);
		}
		@Override protected void doIt(Collection selected) {
		}
		protected List getChoices() {
			List ret = new ArrayList();
			if (Model.getFacade().isAPartition(getTarget())) {
				Object partition = getTarget();
				Object ag = Model.getFacade().getActivityGraph(partition);
				if (ag != null) {
					Object top = Model.getFacade().getTop(ag);
					ret.addAll(Model.getFacade().getSubvertices(top));
				}
			}
			return ret;
		}
		protected String getDialogTitle() {
			return Translator.localize("dialog.title.add-contents");
		}
		protected List getSelected() {
			List ret = new ArrayList();
			ret.addAll(Model.getFacade().getContents(getTarget()));
			return ret;
		}
	}
}

class UMLPartitionContentListModel extends UMLModelElementListModel2 {
	public UMLPartitionContentListModel(String name) {
		super(name);
	}
	protected void buildModelList() {
		Object partition = getTarget();
		setAllElements(Model.getFacade().getContents(partition));
	}
	protected boolean isValidElement(Object element) {
		if (!Model.getFacade().isAModelElement(element)) {
			return false;
		}
		Object partition = getTarget();
		return Model.getFacade().getContents(partition).contains(element);
	}
}

class UMLPartitionActivityGraphListModel extends UMLModelElementListModel2 {
	public UMLPartitionActivityGraphListModel() {
		super("activityGraph");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getActivityGraph(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getActivityGraph(getTarget()) == element;
	}
}



