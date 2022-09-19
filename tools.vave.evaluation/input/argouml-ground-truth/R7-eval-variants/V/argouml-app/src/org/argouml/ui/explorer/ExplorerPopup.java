package org.argouml.ui.explorer;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.Model;
import org.argouml.profile.Profile;
import org.argouml.ui.ActionCreateContainedModelElement;
import org.argouml.ui.ActionCreateEdgeModelElement;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.ActionAddAllClassesFromModel;
import org.argouml.uml.diagram.ui.ActionAddExistingEdge;
import org.argouml.uml.diagram.ui.ActionAddExistingNode;
import org.argouml.uml.diagram.ui.ActionAddExistingNodes;
import org.argouml.uml.diagram.ui.ActionSaveDiagramToClipboard;
import org.argouml.uml.diagram.ui.ModeAddToDiagram;
import org.argouml.uml.ui.ActionClassDiagram;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.argouml.uml.ui.ActionSetSourcePath;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.undo.UndoableAction;


public class ExplorerPopup extends JPopupMenu {
	private JMenu createDiagrams = new JMenu(menuLocalize("menu.popup.create-diagram"));
	private static final Object[]MODEL_ELEMENT_MENUITEMS = new Object[] {Model.getMetaTypes().getPackage(),"button.new-package",Model.getMetaTypes().getActor(),"button.new-actor",Model.getMetaTypes().getExtensionPoint(),"button.new-extension-point",Model.getMetaTypes().getUMLClass(),"button.new-class",Model.getMetaTypes().getInterface(),"button.new-interface",Model.getMetaTypes().getAttribute(),"button.new-attribute",Model.getMetaTypes().getOperation(),"button.new-operation",Model.getMetaTypes().getDataType(),"button.new-datatype",Model.getMetaTypes().getEnumeration(),"button.new-enumeration",Model.getMetaTypes().getEnumerationLiteral(),"button.new-enumeration-literal",Model.getMetaTypes().getSignal(),"button.new-signal",Model.getMetaTypes().getException(),"button.new-exception",Model.getMetaTypes().getComponent(),"button.new-component",Model.getMetaTypes().getComponentInstance(),"button.new-componentinstance",Model.getMetaTypes().getNode(),"button.new-node",Model.getMetaTypes().getNodeInstance(),"button.new-nodeinstance",Model.getMetaTypes().getReception(),"button.new-reception",Model.getMetaTypes().getStereotype(),"button.new-stereotype"};
	public ExplorerPopup(Object selectedItem,MouseEvent me) {
		super("Explorer popup menu");
		boolean multiSelect = TargetManager.getInstance().getTargets().size() > 1;
		boolean mutableModelElementsOnly = true;
		for (Object element:TargetManager.getInstance().getTargets()) {
			if (!Model.getFacade().isAUMLElement(element)||Model.getModelManagementHelper().isReadOnly(element)) {
				mutableModelElementsOnly = false;
				break;
			}
		}
		final ArgoDiagram activeDiagram = DiagramUtils.getActiveDiagram();
		if (!multiSelect&&mutableModelElementsOnly) {
			initMenuCreateDiagrams();
			this.add(createDiagrams);
		}
		try {
			if (!multiSelect&&selectedItem instanceof Profile&&!((Profile) selectedItem).getProfilePackages().isEmpty()) {
				this.add(new ActionExportProfileXMI((Profile) selectedItem));
			}
		}catch (Exception e) {
		}
		if (!multiSelect&&selectedItem instanceof ProfileConfiguration) {
			this.add(new ActionManageProfiles());
		}
		if (mutableModelElementsOnly) {
			initMenuCreateModelElements();
		}
		final boolean modelElementSelected = Model.getFacade().isAUMLElement(selectedItem);
		if (modelElementSelected) {
			final boolean nAryAssociationSelected = Model.getFacade().isANaryAssociation(selectedItem);
			final boolean classifierSelected = Model.getFacade().isAClassifier(selectedItem);
			final boolean packageSelected = Model.getFacade().isAPackage(selectedItem);
			final boolean commentSelected = Model.getFacade().isAComment(selectedItem);
			final boolean stateVertexSelected = Model.getFacade().isAStateVertex(selectedItem);
			final boolean instanceSelected = Model.getFacade().isAInstance(selectedItem);
			final boolean dataValueSelected = Model.getFacade().isADataValue(selectedItem);
			final boolean relationshipSelected = Model.getFacade().isARelationship(selectedItem);
			final boolean flowSelected = Model.getFacade().isAFlow(selectedItem);
			final boolean linkSelected = Model.getFacade().isALink(selectedItem);
			final boolean transitionSelected = Model.getFacade().isATransition(selectedItem);
			Collection projectModels = ProjectManager.getManager().getCurrentProject().getModels();
			if (!multiSelect) {
				if ((classifierSelected&&!relationshipSelected)||(packageSelected&&!projectModels.contains(selectedItem))||(stateVertexSelected)||(instanceSelected&&!dataValueSelected)||nAryAssociationSelected||commentSelected) {
					Action action = new ActionAddExistingNode(menuLocalize("menu.popup.add-to-diagram"),selectedItem);
					this.add(action);
				}
				if ((relationshipSelected&&!flowSelected&&!nAryAssociationSelected)||(linkSelected)||transitionSelected) {
					Action action = new ActionAddExistingEdge(menuLocalize("menu.popup.add-to-diagram"),selectedItem);
					this.add(action);
					addMenuItemForBothEndsOf(selectedItem);
				}
				if (classifierSelected||packageSelected) {
					this.add(new ActionSetSourcePath());
				}
			}
			if (mutableModelElementsOnly&&!(projectModels.size() == 1&&projectModels.contains(selectedItem))) {
				this.add(new ActionDeleteModelElements());
			}
		}
		if (!multiSelect) {
			if (selectedItem instanceof UMLClassDiagram) {
				Action action = new ActionAddAllClassesFromModel(menuLocalize("menu.popup.add-all-classes-to-diagram"),selectedItem);
				this.add(action);
			}
		}
		if (multiSelect) {
			List<Object>classifiers = new ArrayList<Object>();
			for (Object o:TargetManager.getInstance().getTargets()) {
				if (Model.getFacade().isAClassifier(o)&&!Model.getFacade().isARelationship(o)) {
					classifiers.add(o);
				}
			}
			if (!classifiers.isEmpty()) {
				Action action = new ActionAddExistingNodes(menuLocalize("menu.popup.add-to-diagram"),classifiers);
				this.add(action);
			}
		}else if (selectedItem instanceof Diagram) {
			this.add(new ActionSaveDiagramToClipboard());
			ActionDeleteModelElements ad = new ActionDeleteModelElements();
			ad.setEnabled(ad.shouldBeEnabled());
			this.add(ad);
		}
	}
	private void initMenuCreateDiagrams() {
		createDiagrams.add(new ActionClassDiagram());
	}
	private void initMenuCreateModelElements() {
		List targets = TargetManager.getInstance().getTargets();
		Set<JMenuItem>menuItems = new TreeSet<JMenuItem>();
		if (targets.size() >= 2) {
			boolean classifierRoleFound = false;
			boolean classifierRolesOnly = true;
			for (Iterator it = targets.iterator();it.hasNext()&&classifierRolesOnly;) {
				if (Model.getFacade().isAClassifierRole(it.next())) {
					classifierRoleFound = true;
				}else {
					classifierRolesOnly = false;
				}
			}
			if (classifierRolesOnly) {
				menuItems.add(new OrderedMenuItem(new ActionCreateAssociationRole(Model.getMetaTypes().getAssociationRole(),targets)));
			}else if (!classifierRoleFound) {
				boolean classifiersOnly = true;
				for (Iterator it = targets.iterator();it.hasNext()&&classifiersOnly;) {
					if (!Model.getFacade().isAClassifier(it.next())) {
						classifiersOnly = false;
					}
				}
				if (classifiersOnly) {
					menuItems.add(new OrderedMenuItem(new ActionCreateAssociation(Model.getMetaTypes().getAssociation(),targets)));
				}
			}
		}
		if (targets.size() == 2) {
			addCreateModelElementAction(menuItems,Model.getMetaTypes().getDependency()," " + menuLocalize("menu.popup.depends-on") + " ");
			addCreateModelElementAction(menuItems,Model.getMetaTypes().getGeneralization()," " + menuLocalize("menu.popup.generalizes") + " ");
			addCreateModelElementAction(menuItems,Model.getMetaTypes().getInclude()," " + menuLocalize("menu.popup.includes") + " ");
			addCreateModelElementAction(menuItems,Model.getMetaTypes().getExtend()," " + menuLocalize("menu.popup.extends") + " ");
			addCreateModelElementAction(menuItems,Model.getMetaTypes().getPackageImport()," " + menuLocalize("menu.popup.has-permission-on") + " ");
			addCreateModelElementAction(menuItems,Model.getMetaTypes().getUsage()," " + menuLocalize("menu.popup.uses") + " ");
			addCreateModelElementAction(menuItems,Model.getMetaTypes().getAbstraction()," " + menuLocalize("menu.popup.realizes") + " ");
		}else if (targets.size() == 1) {
			Object target = targets.get(0);
			for (int iter = 0;iter < MODEL_ELEMENT_MENUITEMS.;iter += 2) {
				if (Model.getUmlFactory().isContainmentValid(MODEL_ELEMENT_MENUITEMS[iter],target)) {
					menuItems.add(new OrderedMenuItem(new ActionCreateContainedModelElement(MODEL_ELEMENT_MENUITEMS[iter],target,(String) MODEL_ELEMENT_MENUITEMS[iter + 1])));
				}
			}
		}
		if (menuItems.size() == 1) {
			add(menuItems.iterator().next());
		}else if (menuItems.size() > 1) {
			JMenu menu = new JMenu(menuLocalize("menu.popup.create-model-element"));
			add(menu);
			for (JMenuItem item:menuItems) {
				menu.add(item);
			}
		}
	}
	private void addCreateModelElementAction(Set<JMenuItem>menuItems,Object metaType,String relationshipDescr) {
		List targets = TargetManager.getInstance().getTargets();
		Object source = targets.get(0);
		Object dest = targets.get(1);
		JMenu subMenu = new OrderedMenu(menuLocalize("menu.popup.create") + " " + Model.getMetaTypes().getName(metaType));
		buildDirectionalCreateMenuItem(metaType,dest,source,relationshipDescr,subMenu);
		buildDirectionalCreateMenuItem(metaType,source,dest,relationshipDescr,subMenu);
		if (subMenu.getMenuComponents(). > 0) {
			menuItems.add(subMenu);
		}
	}
	private void buildDirectionalCreateMenuItem(Object metaType,Object source,Object dest,String relationshipDescr,JMenu menu) {
		if (Model.getUmlFactory().isConnectionValid(metaType,source,dest,true)) {
			JMenuItem menuItem = new JMenuItem(new ActionCreateEdgeModelElement(metaType,source,dest,relationshipDescr));
			if (menuItem != null) {
				menu.add(menuItem);
			}
		}
	}
	private String menuLocalize(String key) {
		return Translator.localize(key);
	}
	private void addMenuItemForBothEndsOf(Object edge) {
		Collection coll = null;
		if (Model.getFacade().isAAssociation(edge)||Model.getFacade().isALink(edge)) {
			coll = Model.getFacade().getConnections(edge);
		}else if (Model.getFacade().isAAbstraction(edge)||Model.getFacade().isADependency(edge)) {
			coll = new ArrayList();
			coll.addAll(Model.getFacade().getClients(edge));
			coll.addAll(Model.getFacade().getSuppliers(edge));
		}else if (Model.getFacade().isAGeneralization(edge)) {
			coll = new ArrayList();
			Object parent = Model.getFacade().getGeneral(edge);
			coll.add(parent);
			coll.addAll(Model.getFacade().getChildren(parent));
		}
		if (coll == null) {
			return;
		}
		Iterator iter = coll.iterator();
		while (iter.hasNext()) {
			Object me = iter.next();
			if (me != null) {
				if (Model.getFacade().isAAssociationEnd(me)) {
					me = Model.getFacade().getType(me);
				}
				if (me != null) {
					String name = Model.getFacade().getName(me);
					if (name == null||name.length() == 0) {
						name = "(anon element)";
					}
					Action action = new ActionAddExistingRelatedNode(menuLocalize("menu.popup.add-to-diagram") + ": " + name,me);
					this.add(action);
				}
			}
		}
	}
	private static final long serialVersionUID = -5663884871599931780l;
	private class ActionAddExistingRelatedNode extends UndoableAction {
	private Object object;
	public ActionAddExistingRelatedNode(String name,Object o) {
		super(name);
		object = o;
	}
	public boolean isEnabled() {
		ArgoDiagram dia = DiagramUtils.getActiveDiagram();
		if (dia == null) {
			return false;
		}
		MutableGraphModel gm = (MutableGraphModel) dia.getGraphModel();
		return gm.canAddNode(object);
	}
	public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		Editor ce = Globals.curEditor();
		GraphModel gm = ce.getGraphModel();
		if (!(gm instanceof MutableGraphModel)) {
			return;
		}
		String instructions = null;
		if (object != null) {
			instructions = Translator.localize("misc.message.click-on-diagram-to-add",new Object[] {Model.getFacade().toString(object)});
			Globals.showStatus(instructions);
		}
		ArrayList<Object>elementsToAdd = new ArrayList<Object>(1);
		elementsToAdd.add(object);
		final ModeAddToDiagram placeMode = new ModeAddToDiagram(elementsToAdd,instructions);
		Globals.mode(placeMode,false);
	}
}
	private class OrderedMenuItem extends JMenuItem implements Comparable {
	public OrderedMenuItem(Action action) {
		super(action);
	}
	public OrderedMenuItem(String name) {
		super(name);
		setName(name);
	}
	public int compareTo(Object o) {
		JMenuItem other = (JMenuItem) o;
		return toString().compareTo(other.toString());
	}
}
	private class OrderedMenu extends JMenu implements Comparable {
	public OrderedMenu(String name) {
		super(name);
	}
	public int compareTo(Object o) {
		JMenuItem other = (JMenuItem) o;
		return toString().compareTo(other.toString());
	}
}
	private class ActionCreateAssociation extends AbstractAction {
	private Object metaType;
	private List classifiers;
	public ActionCreateAssociation(Object theMetaType,List classifiersList) {
		super(menuLocalize("menu.popup.create") + " " + Model.getMetaTypes().getName(theMetaType));
		this.metaType = theMetaType;
		this.classifiers = classifiersList;
	}
	public void actionPerformed(ActionEvent e) {
		try {
			Object newElement = Model.getUmlFactory().buildConnection(metaType,classifiers.get(0),null,classifiers.get(1),null,null,null);
			for (int i = 2;i < classifiers.size();++i) {
				Model.getUmlFactory().buildConnection(Model.getMetaTypes().getAssociationEnd(),newElement,null,classifiers.get(i),null,null,null);
			}
		}catch (IllegalModelElementConnectionException e1) {
		}
	}
}
	private class ActionCreateAssociationRole extends AbstractAction {
	private Object metaType;
	private List classifierRoles;
	public ActionCreateAssociationRole(Object theMetaType,List classifierRolesList) {
		super(menuLocalize("menu.popup.create") + " " + Model.getMetaTypes().getName(theMetaType));
		this.metaType = theMetaType;
		this.classifierRoles = classifierRolesList;
	}
	public void actionPerformed(ActionEvent e) {
		try {
			Object newElement = Model.getUmlFactory().buildConnection(metaType,classifierRoles.get(0),null,classifierRoles.get(1),null,null,null);
			for (int i = 2;i < classifierRoles.size();++i) {
				Model.getUmlFactory().buildConnection(Model.getMetaTypes().getAssociationEndRole(),newElement,null,classifierRoles.get(i),null,null,null);
			}
		}catch (IllegalModelElementConnectionException e1) {
		}
	}
}
}



