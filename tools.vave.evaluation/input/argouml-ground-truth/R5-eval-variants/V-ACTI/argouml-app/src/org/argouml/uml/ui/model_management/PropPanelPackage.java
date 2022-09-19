package org.argouml.uml.ui.model_management;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLAddDialog;
import org.argouml.uml.ui.UMLDerivedCheckBox;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.ActionAddDataType;
import org.argouml.uml.ui.foundation.core.ActionAddEnumeration;
import org.argouml.uml.ui.foundation.core.PropPanelNamespace;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementAbstractCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementGeneralizationListModel;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementLeafCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementRootCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementSpecializationListModel;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewTagDefinition;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.undo.UndoableAction;


public class PropPanelPackage extends PropPanelNamespace {
	private static final long serialVersionUID = -699491324617952412l;
	private JPanel modifiersPanel;
	private JScrollPane generalizationScroll;
	private JScrollPane specializationScroll;
	private static UMLGeneralizableElementGeneralizationListModel generalizationListModel = new UMLGeneralizableElementGeneralizationListModel();
	private static UMLGeneralizableElementSpecializationListModel specializationListModel = new UMLGeneralizableElementSpecializationListModel();
	public PropPanelPackage() {
		this("label.package",lookupIcon("Package"));
	}
	public PropPanelPackage(String title,ImageIcon icon) {
		super(title,icon);
		placeElements();
	}
	protected void placeElements() {
		addField("label.name",getNameTextField());
		addField("label.namespace",getNamespaceSelector());
		add(getVisibilityPanel());
		add(getModifiersPanel());
		addSeparator();
		addField("label.generalizations",getGeneralizationScroll());
		addField("label.specializations",getSpecializationScroll());
		addSeparator();
		addField("label.owned-elements",getOwnedElementsScroll());
		JList importList = new UMLMutableLinkedList(new UMLClassifierPackageImportsListModel(),new ActionAddPackageImport(),null,new ActionRemovePackageImport(),true);
		addField("label.imported-elements",new JScrollPane(importList));
		addAction(new ActionNavigateNamespace());
		addAction(new ActionAddPackage());
		addAction(new ActionAddDataType());
		addAction(new ActionAddEnumeration());
		addAction(new ActionDialogElementImport());
		addAction(new ActionNewStereotype());
		addAction(new ActionNewTagDefinition());
		addAction(getDeleteAction());
	}
	public JPanel getModifiersPanel() {
		if (modifiersPanel == null) {
			modifiersPanel = createBorderPanel(Translator.localize("label.modifiers"));
			modifiersPanel.add(new UMLGeneralizableElementAbstractCheckBox());
			modifiersPanel.add(new UMLGeneralizableElementLeafCheckBox());
			modifiersPanel.add(new UMLGeneralizableElementRootCheckBox());
			modifiersPanel.add(new UMLDerivedCheckBox());
		}
		return modifiersPanel;
	}
	public JScrollPane getGeneralizationScroll() {
		if (generalizationScroll == null) {
			JList list = new UMLLinkedList(generalizationListModel);
			generalizationScroll = new JScrollPane(list);
		}
		return generalizationScroll;
	}
	public JScrollPane getSpecializationScroll() {
		if (specializationScroll == null) {
			JList list = new UMLLinkedList(specializationListModel);
			specializationScroll = new JScrollPane(list);
		}
		return specializationScroll;
	}
}

@UmlModelMutator class ActionDialogElementImport extends UndoableAction {
	public ActionDialogElementImport() {
		super();
		putValue(Action.SMALL_ICON,ResourceLoaderWrapper.lookupIcon("ElementImport"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("button.add-element-import"));
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object target = TargetManager.getInstance().getSingleModelTarget();
		if (target != null) {
			UMLAddDialog dialog = new UMLAddDialog(getChoices(target),getSelected(target),getDialogTitle(),isMultiSelect(),isExclusive());
			int result = dialog.showDialog(ArgoFrame.getInstance());
			if (result == JOptionPane.OK_OPTION) {
				doIt(target,dialog.getSelected());
			}
		}
	}
	protected List getChoices(Object target) {
		List result = new ArrayList();
		result.addAll(Model.getModelManagementHelper().getAllPossibleImports(target));
		return result;
	}
	protected List getSelected(Object target) {
		List result = new ArrayList();
		result.addAll(Model.getFacade().getImportedElements(target));
		return result;
	}
	protected String getDialogTitle() {
		return Translator.localize("dialog.title.add-imported-elements");
	}
	public boolean isMultiSelect() {
		return true;
	}
	public boolean isExclusive() {
		return true;
	}
	protected void doIt(Object target,List selected) {
		Model.getModelManagementHelper().setImportedElements(target,selected);
	}
}



