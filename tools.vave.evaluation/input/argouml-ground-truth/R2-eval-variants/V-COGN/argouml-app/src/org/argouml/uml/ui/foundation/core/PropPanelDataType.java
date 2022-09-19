package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelDataType extends PropPanelClassifier {
	private JScrollPane operationScroll;
	private static UMLClassOperationListModel operationListModel = new UMLClassOperationListModel();
	public PropPanelDataType(String title,ImageIcon icon) {
		super(title,icon);
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		add(getModifiersPanel());
		add(getVisibilityPanel());
		addSeparator();
		addField(Translator.localize("label.client-dependencies"),getClientDependencyScroll());
		addField(Translator.localize("label.supplier-dependencies"),getSupplierDependencyScroll());
		addField(Translator.localize("label.generalizations"),getGeneralizationScroll());
		addField(Translator.localize("label.specializations"),getSpecializationScroll());
		addSeparator();
		addField(Translator.localize("label.operations"),getOperationScroll());
		addAction(new ActionNavigateContainerElement());
		addAction(new ActionAddDataType());
		addEnumerationButtons();
		addAction(new ActionAddQueryOperation());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
	protected void addEnumerationButtons() {
		addAction(new ActionAddEnumeration());
	}
	public PropPanelDataType() {
		this("label.data-type",lookupIcon("DataType"));
	}
	private static class ActionAddQueryOperation extends AbstractActionNewModelElement {
	public ActionAddQueryOperation() {
		super("button.new-operation");
		putValue(Action.NAME,Translator.localize("button.new-operation"));
	}
	@Override public void actionPerformed(ActionEvent e) {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isAClassifier(target)) {
			Object returnType = ProjectManager.getManager().getCurrentProject().getDefaultReturnType();
			Object newOper = Model.getCoreFactory().buildOperation(target,returnType);
			Model.getCoreHelper().setQuery(newOper,true);
			TargetManager.getInstance().setTarget(newOper);
			super.actionPerformed(e);
		}
	}
	private static final long serialVersionUID = -3393730108010236394l;
}
	@Override public JScrollPane getOperationScroll() {
		if (operationScroll == null) {
			JList list = new UMLLinkedList(operationListModel);
			operationScroll = new JScrollPane(list);
		}
		return operationScroll;
	}
	private static final long serialVersionUID = -8752986130386737802l;
}



