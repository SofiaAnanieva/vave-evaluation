package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionRemoveElement;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementGeneralizationListModel;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementSpecializationListModel;


public class PropPanelStereotype extends PropPanelModelElement {
	private static final long serialVersionUID = 8038077991746618130l;
	private List<String>metaClasses;
	private static UMLGeneralizableElementSpecializationListModel specializationListModel = new UMLGeneralizableElementSpecializationListModel();
	private static UMLGeneralizableElementGeneralizationListModel generalizationListModel = new UMLGeneralizableElementGeneralizationListModel();
	private static UMLStereotypeTagDefinitionListModel tagDefinitionListModel = new UMLStereotypeTagDefinitionListModel();
	private static UMLExtendedElementsListModel extendedElementsListModel = new UMLExtendedElementsListModel();
	private JScrollPane generalizationScroll;
	private JScrollPane specializationScroll;
	private JScrollPane tagDefinitionScroll;
	private JScrollPane extendedElementsScroll;
	public PropPanelStereotype() {
		super("label.stereotype-title",lookupIcon("Stereotype"));
	}
	protected JScrollPane getGeneralizationScroll() {
		return null;
	}
	protected JScrollPane getSpecializationScroll() {
		return null;
	}
	protected JScrollPane getTagDefinitionScroll() {
		return null;
	}
	protected JScrollPane getExtendedElementsScroll() {
		return null;
	}
	void initMetaClasses() {
	}
	class UMLStereotypeBaseClassListModel extends UMLModelElementListModel2 {
		UMLStereotypeBaseClassListModel() {
				super("baseClass");
			}
		@Override protected void buildModelList() {
		}
		@Override protected boolean isValidElement(Object element) {
			return false;
		}
	}
	class ActionAddStereotypeBaseClass extends AbstractActionAddModelElement2 {
		@Override protected List<String>getChoices() {
			return null;
		}
		@Override protected String getDialogTitle() {
			return null;
		}
		@Override protected List<String>getSelected() {
			return null;
		}
		@Override protected void doIt(Collection selected) {
		}
	}
	class ActionDeleteStereotypeBaseClass extends AbstractActionRemoveElement {
		public ActionDeleteStereotypeBaseClass() {
			super(Translator.localize("menu.popup.remove"));
		}
		@Override public void actionPerformed(ActionEvent e) {
		}
	}
}



