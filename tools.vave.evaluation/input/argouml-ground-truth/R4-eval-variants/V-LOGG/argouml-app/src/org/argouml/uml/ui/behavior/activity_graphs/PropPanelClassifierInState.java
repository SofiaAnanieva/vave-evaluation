package org.argouml.uml.ui.behavior.activity_graphs;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionRemoveElement;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.UMLSearchableComboBox;
import org.argouml.uml.ui.foundation.core.PropPanelClassifier;
import org.tigris.gef.undo.UndoableAction;


public class PropPanelClassifierInState extends PropPanelClassifier {
	private static final long serialVersionUID = 609338855898756817l;
	private JComboBox typeComboBox;
	private JScrollPane statesScroll;
	private UMLClassifierInStateTypeComboBoxModel typeComboBoxModel = new UMLClassifierInStateTypeComboBoxModel();
	public PropPanelClassifierInState() {
		super("label.classifier-in-state",lookupIcon("ClassifierInState"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		addSeparator();
		addField(Translator.localize("label.type"),new UMLComboBoxNavigator(Translator.localize("label.class.navigate.tooltip"),getClassifierInStateTypeSelector()));
		AbstractActionAddModelElement2 actionAdd = new ActionAddCISState();
		AbstractActionRemoveElement actionRemove = new ActionRemoveCISState();
		UMLMutableLinkedList list = new UMLMutableLinkedList(new UMLCISStateListModel(),actionAdd,null,actionRemove,true);
		statesScroll = new JScrollPane(list);
		addField(Translator.localize("label.instate"),statesScroll);
		addAction(new ActionNavigateNamespace());
		addAction(getDeleteAction());
	}
	protected JComboBox getClassifierInStateTypeSelector() {
		if (typeComboBox == null) {
			typeComboBox = new UMLSearchableComboBox(typeComboBoxModel,new ActionSetClassifierInStateType(),true);
		}
		return typeComboBox;
	}
}

class ActionSetClassifierInStateType extends UndoableAction {
	private static final long serialVersionUID = -7537482435346517599l;
	ActionSetClassifierInStateType() {
			super();
		}
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		Object oldClassifier = null;
		Object newClassifier = null;
		Object cis = null;
		if (source instanceof UMLComboBox2) {
			UMLComboBox2 box = (UMLComboBox2) source;
			Object obj = box.getTarget();
			if (Model.getFacade().isAClassifierInState(obj)) {
				try {
					oldClassifier = Model.getFacade().getType(obj);
				}catch (InvalidElementException e1) {
					return;
				}
				cis = obj;
			}
			Object cl = box.getSelectedItem();
			if (Model.getFacade().isAClassifier(cl)) {
				newClassifier = cl;
			}
		}
		if (newClassifier != oldClassifier&&cis != null&&newClassifier != null) {
			Model.getCoreHelper().setType(cis,newClassifier);
			super.actionPerformed(e);
		}
	}
}

class UMLClassifierInStateTypeComboBoxModel extends UMLComboBoxModel2 {
	private static final long serialVersionUID = 1705685511742198305l;
	public UMLClassifierInStateTypeComboBoxModel() {
		super("type",false);
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isAClassifier(o)&&!Model.getFacade().isAClassifierInState(o);
	}
	protected void buildModelList() {
		Object model = ProjectManager.getManager().getCurrentProject().getModel();
		Collection classifiers = new ArrayList(Model.getCoreHelper().getAllClassifiers(model));
		Collection newList = new ArrayList();
		for (Object classifier:classifiers) {
			if (!Model.getFacade().isAClassifierInState(classifier)) {
				newList.add(classifier);
			}
		}
		if (getTarget() != null) {
			Object type = Model.getFacade().getType(getTarget());
			if (Model.getFacade().isAClassifierInState(type)) {
				type = Model.getFacade().getType(type);
			}
			if (type != null)if (!newList.contains(type))newList.add(type);
		}
		setElements(newList);
	}
	protected Object getSelectedModelElement() {
		if (getTarget() != null) {
			Object type = Model.getFacade().getType(getTarget());
			return type;
		}
		return null;
	}
	public void modelChanged(PropertyChangeEvent evt) {
		if (evt instanceof AttributeChangeEvent) {
			if (evt.getPropertyName().equals("type")) {
				if (evt.getSource() == getTarget()&&(getChangedElement(evt) != null)) {
					Object elem = getChangedElement(evt);
					setSelectedItem(elem);
				}
			}
		}
	}
}

class ActionAddCISState extends AbstractActionAddModelElement2 {
	private static final long serialVersionUID = -3892619042821099432l;
	private Object choiceClass = Model.getMetaTypes().getState();
	public ActionAddCISState() {
		super();
		setMultiSelect(true);
	}
	protected void doIt(Collection selected) {
	}
	protected List getChoices() {
		List ret = new ArrayList();
		Object cis = getTarget();
		Object classifier = Model.getFacade().getType(cis);
		if (Model.getFacade().isAClassifier(classifier)) {
			ret.addAll(Model.getModelManagementHelper().getAllModelElementsOfKindWithModel(classifier,choiceClass));
		}
		return ret;
	}
	protected String getDialogTitle() {
		return Translator.localize("dialog.title.add-state");
	}
	protected List getSelected() {
		Object cis = getTarget();
		if (Model.getFacade().isAClassifierInState(cis)) {
			return new ArrayList(Model.getFacade().getInStates(cis));
		}
		return Collections.EMPTY_LIST;
	}
}

class ActionRemoveCISState extends AbstractActionRemoveElement {
	private static final long serialVersionUID = -1431919084967610562l;
	public ActionRemoveCISState() {
		super(Translator.localize("menu.popup.remove"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object state = getObjectToRemove();
		if (state != null) {
			Object cis = getTarget();
			if (Model.getFacade().isAClassifierInState(cis)) {
				Collection states = new ArrayList(Model.getFacade().getInStates(cis));
				states.remove(state);
			}
		}
	}
}

class UMLCISStateListModel extends UMLModelElementListModel2 {
	private static final long serialVersionUID = -8786823179344335113l;
	public UMLCISStateListModel() {
		super("inState");
	}
	protected void buildModelList() {
		Object cis = getTarget();
		if (Model.getFacade().isAClassifierInState(cis)) {
			Collection c = Model.getFacade().getInStates(cis);
			setAllElements(c);
		}
	}
	protected boolean isValidElement(Object elem) {
		Object cis = getTarget();
		if (Model.getFacade().isAClassifierInState(cis)) {
			Collection c = Model.getFacade().getInStates(cis);
			if (c.contains(elem))return true;
		}
		return false;
	}
}



