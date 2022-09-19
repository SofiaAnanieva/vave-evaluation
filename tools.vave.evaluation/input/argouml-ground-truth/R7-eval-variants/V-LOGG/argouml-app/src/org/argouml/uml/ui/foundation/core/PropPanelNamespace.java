package org.argouml.uml.ui.foundation.core;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ScrollList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;


public abstract class PropPanelNamespace extends PropPanelModelElement {
	private JScrollPane ownedElementsScroll;
	private static UMLNamespaceOwnedElementListModel ownedElementListModel = new UMLNamespaceOwnedElementListModel();
	public PropPanelNamespace(String panelName,ImageIcon icon) {
		super(panelName,icon);
	}
	public void addClass() {
		Object target = getTarget();
		if (Model.getFacade().isANamespace(target)) {
			Object ns = target;
			Object ownedElem = Model.getCoreFactory().buildClass();
			Model.getCoreHelper().addOwnedElement(ns,ownedElem);
			TargetManager.getInstance().setTarget(ownedElem);
		}
	}
	public void addInterface() {
		Object target = getTarget();
		if (Model.getFacade().isANamespace(target)) {
			Object ns = target;
			Object ownedElem = Model.getCoreFactory().createInterface();
			Model.getCoreHelper().addOwnedElement(ns,ownedElem);
			TargetManager.getInstance().setTarget(ownedElem);
		}
	}
	public void addPackage() {
		Object target = getTarget();
		if (Model.getFacade().isANamespace(target)) {
			Object ns = target;
			Object ownedElem = Model.getModelManagementFactory().createPackage();
			Model.getCoreHelper().addOwnedElement(ns,ownedElem);
			TargetManager.getInstance().setTarget(ownedElem);
		}
	}
	public JScrollPane getOwnedElementsScroll() {
		if (ownedElementsScroll == null) {
			ownedElementsScroll = new ScrollList(ownedElementListModel,true,false);
		}
		return ownedElementsScroll;
	}
}



