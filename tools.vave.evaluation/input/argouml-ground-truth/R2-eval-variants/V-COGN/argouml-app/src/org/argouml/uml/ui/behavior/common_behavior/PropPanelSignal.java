package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionRemoveElement;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelClassifier;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelSignal extends PropPanelClassifier {
	private static final long serialVersionUID = -4496838172438164508l;
	public PropPanelSignal() {
		this("label.signal-title","SignalSending");
	}
	public PropPanelSignal(String title,String iconName) {
		super(title,lookupIcon(iconName));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		add(getModifiersPanel());
		add(getVisibilityPanel());
		addSeparator();
		addField(Translator.localize("label.generalizations"),getGeneralizationScroll());
		addField(Translator.localize("label.specializations"),getSpecializationScroll());
		addSeparator();
		AbstractActionAddModelElement2 actionAddContext = new ActionAddContextSignal();
		AbstractActionRemoveElement actionRemoveContext = new ActionRemoveContextSignal();
		JScrollPane operationScroll = new JScrollPane(new UMLMutableLinkedList(new UMLSignalContextListModel(),actionAddContext,null,actionRemoveContext,true));
		addField(Translator.localize("label.contexts"),operationScroll);
		AbstractActionAddModelElement2 actionAddReception = new ActionAddReceptionSignal();
		AbstractActionRemoveElement actionRemoveReception = new ActionRemoveReceptionSignal();
		JScrollPane receptionScroll = new JScrollPane(new UMLMutableLinkedList(new UMLSignalReceptionListModel(),actionAddReception,null,actionRemoveReception,true));
		addField(Translator.localize("label.receptions"),receptionScroll);
		addAction(new ActionNavigateNamespace());
		addAction(new ActionNewSignal());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}

class UMLSignalReceptionListModel extends UMLModelElementListModel2 {
	private static final long serialVersionUID = 3273212639257377015l;
	public UMLSignalReceptionListModel() {
		super("reception");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getReceptions(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isAReception(element)&&Model.getFacade().getReceptions(getTarget()).contains(element);
	}
}

class ActionAddReceptionSignal extends AbstractActionAddModelElement2 {
	private static final long serialVersionUID = -2854099588590429237l;
	public ActionAddReceptionSignal() {
		super();
	}
	protected List getChoices() {
		List ret = new ArrayList();
		Object model = ProjectManager.getManager().getCurrentProject().getModel();
		if (getTarget() != null) {
			ret.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(model,Model.getMetaTypes().getReception()));
		}
		return ret;
	}
	protected List getSelected() {
		List ret = new ArrayList();
		ret.addAll(Model.getFacade().getReceptions(getTarget()));
		return ret;
	}
	protected String getDialogTitle() {
		return Translator.localize("dialog.title.add-receptions");
	}
	@Override protected void doIt(Collection selected) {
		Model.getCommonBehaviorHelper().setReception(getTarget(),selected);
	}
}

class ActionRemoveContextSignal extends AbstractActionRemoveElement {
	private static final long serialVersionUID = -3345844954130000669l;
	public ActionRemoveContextSignal() {
		super(Translator.localize("menu.popup.remove"));
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object context = getObjectToRemove();
		if (context != null) {
			Object signal = getTarget();
			if (Model.getFacade().isASignal(signal)) {
				Model.getCommonBehaviorHelper().removeContext(signal,context);
			}
		}
	}
}

class ActionRemoveReceptionSignal extends AbstractActionRemoveElement {
	private static final long serialVersionUID = -2630315087703962883l;
	public ActionRemoveReceptionSignal() {
		super(Translator.localize("menu.popup.remove"));
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object reception = getObjectToRemove();
		if (reception != null) {
			Object signal = getTarget();
			if (Model.getFacade().isASignal(signal)) {
				Model.getCommonBehaviorHelper().removeReception(signal,reception);
			}
		}
	}
}



