package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.Collection;
import javax.swing.Action;
import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.ui.FigClassAssociationClass;
import org.argouml.uml.diagram.ui.FigEdgeAssociationClass;
import org.argouml.uml.diagram.ui.FigNodeAssociation;
import org.argouml.uml.diagram.ui.ModeCreateDependency;
import org.argouml.uml.diagram.ui.ModeCreatePermission;
import org.argouml.uml.diagram.ui.ModeCreateUsage;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.foundation.core.ActionAddAttribute;
import org.argouml.uml.ui.foundation.core.ActionAddOperation;
import org.argouml.util.ToolBarUtility;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.presentation.FigNode;


public class UMLClassDiagram extends UMLDiagram {
	private static final long serialVersionUID = -9192325790126361563l;
	private static final Logger LOG = Logger.getLogger(UMLClassDiagram.class);
	private Action actionAssociationClass;
	private Action actionClass;
	private Action actionInterface;
	private Action actionDependency;
	private Action actionPermission;
	private Action actionUsage;
	private Action actionLink;
	private Action actionGeneralization;
	private Action actionRealization;
	private Action actionPackage;
	private Action actionModel;
	private Action actionSubsystem;
	private Action actionAssociation;
	private Action actionAssociationEnd;
	private Action actionAggregation;
	private Action actionComposition;
	private Action actionUniAssociation;
	private Action actionUniAggregation;
	private Action actionUniComposition;
	private Action actionDataType;
	private Action actionEnumeration;
	private Action actionStereotype;
	private Action actionSignal;
	private Action actionException;
	@Deprecated public UMLClassDiagram() {
		super(new ClassDiagramGraphModel());
	}
	public UMLClassDiagram(String name,Object namespace) {
		super(name,namespace,new ClassDiagramGraphModel());
	}
	public UMLClassDiagram(Object m) {
		super("",m,new ClassDiagramGraphModel());
		String name = getNewDiagramName();
		try {
			setName(name);
		}catch (PropertyVetoException pve) {
			LOG.warn("Generated diagram name \'" + name + "\' was vetoed by setName");
		}
	}
	public void setNamespace(Object ns) {
		if (!Model.getFacade().isANamespace(ns)) {
			LOG.error("Illegal argument. " + "Object " + ns + " is not a namespace");
			throw new IllegalArgumentException("Illegal argument. " + "Object " + ns + " is not a namespace");
		}
		boolean init = (null == getNamespace());
		super.setNamespace(ns);
		ClassDiagramGraphModel gm = (ClassDiagramGraphModel) getGraphModel();
		gm.setHomeModel(ns);
		if (init) {
			LayerPerspective lay = new LayerPerspectiveMutable(Model.getFacade().getName(ns),gm);
			ClassDiagramRenderer rend = new ClassDiagramRenderer();
			lay.setGraphNodeRenderer(rend);
			lay.setGraphEdgeRenderer(rend);
			setLayer(lay);
		}
	}
	protected Object[]getUmlActions() {
		Object[]actions =  {getPackageActions(),getActionClass(),null,getAssociationActions(),getAggregationActions(),getCompositionActions(),getActionAssociationEnd(),getActionGeneralization(),null,getActionInterface(),getActionRealization(),null,getDependencyActions(),null,ActionAddAttribute.getTargetFollower(),ActionAddOperation.getTargetFollower(),getActionAssociationClass(),null,getDataTypeActions()};
		return actions;
	}
	private Object[]getDataTypeActions() {
		Object[]actions =  {getActionDataType(),getActionEnumeration(),getActionStereotype(),getActionSignal(),getActionException()};
		ToolBarUtility.manageDefault(actions,"diagram.class.datatype");
		return actions;
	}
	private Object getPackageActions() {
		if (false) {
			Object[]actions =  {getActionPackage(),getActionModel(),getActionSubsystem()};
			ToolBarUtility.manageDefault(actions,"diagram.class.package");
			return actions;
		}else {
			return getActionPackage();
		}
	}
	private Object[]getDependencyActions() {
		Object[]actions =  {getActionDependency(),getActionPermission(),getActionUsage()};
		ToolBarUtility.manageDefault(actions,"diagram.class.dependency");
		return actions;
	}
	private Object[]getAssociationActions() {
		Object[]actions =  {getActionAssociation(),getActionUniAssociation()};
		ToolBarUtility.manageDefault(actions,"diagram.class.association");
		return actions;
	}
	private Object[]getAggregationActions() {
		Object[]actions =  {getActionAggregation(),getActionUniAggregation()};
		ToolBarUtility.manageDefault(actions,"diagram.class.aggregation");
		return actions;
	}
	private Object[]getCompositionActions() {
		Object[]actions =  {getActionComposition(),getActionUniComposition()};
		ToolBarUtility.manageDefault(actions,"diagram.class.composition");
		return actions;
	}
	public String getLabelName() {
		return Translator.localize("label.class-diagram");
	}
	protected Action getActionAggregation() {
		if (actionAggregation == null) {
			actionAggregation = makeCreateAssociationAction(Model.getAggregationKind().getAggregate(),false,"button.new-aggregation");
		}
		return actionAggregation;
	}
	protected Action getActionAssociation() {
		if (actionAssociation == null) {
			actionAssociation = makeCreateAssociationAction(Model.getAggregationKind().getNone(),false,"button.new-association");
		}
		return actionAssociation;
	}
	protected Action getActionAssociationEnd() {
		if (actionAssociationEnd == null) {
			actionAssociationEnd = makeCreateAssociationEndAction("button.new-association-end");
		}
		return actionAssociationEnd;
	}
	protected Action getActionClass() {
		if (actionClass == null) {
			actionClass = makeCreateNodeAction(Model.getMetaTypes().getUMLClass(),"button.new-class");
		}
		return actionClass;
	}
	protected Action getActionAssociationClass() {
		if (actionAssociationClass == null) {
			actionAssociationClass = makeCreateAssociationClassAction("button.new-associationclass");
		}
		return actionAssociationClass;
	}
	protected Action getActionComposition() {
		if (actionComposition == null) {
			actionComposition = makeCreateAssociationAction(Model.getAggregationKind().getComposite(),false,"button.new-composition");
		}
		return actionComposition;
	}
	protected Action getActionDependency() {
		if (actionDependency == null) {
			actionDependency = makeCreateDependencyAction(ModeCreateDependency.class,Model.getMetaTypes().getDependency(),"button.new-dependency");
		}
		return actionDependency;
	}
	protected Action getActionGeneralization() {
		if (actionGeneralization == null) {
			actionGeneralization = makeCreateGeneralizationAction();
		}
		return actionGeneralization;
	}
	protected Action getActionInterface() {
		if (actionInterface == null) {
			actionInterface = makeCreateNodeAction(Model.getMetaTypes().getInterface(),"button.new-interface");
		}
		return actionInterface;
	}
	protected Action getActionLink() {
		if (actionLink == null) {
			actionLink = makeCreateEdgeAction(Model.getMetaTypes().getLink(),"Link");
		}
		return actionLink;
	}
	protected Action getActionModel() {
		if (actionModel == null) {
			actionModel = makeCreateNodeAction(Model.getMetaTypes().getModel(),"Model");
		}
		return actionModel;
	}
	protected Action getActionPackage() {
		if (actionPackage == null) {
			actionPackage = makeCreateNodeAction(Model.getMetaTypes().getPackage(),"button.new-package");
		}
		return actionPackage;
	}
	protected Action getActionPermission() {
		if (actionPermission == null) {
			actionPermission = makeCreateDependencyAction(ModeCreatePermission.class,Model.getMetaTypes().getPackageImport(),"button.new-permission");
		}
		return actionPermission;
	}
	protected Action getActionRealization() {
		if (actionRealization == null) {
			actionRealization = makeCreateEdgeAction(Model.getMetaTypes().getAbstraction(),"button.new-realization");
		}
		return actionRealization;
	}
	protected Action getActionSubsystem() {
		if (actionSubsystem == null) {
			actionSubsystem = makeCreateNodeAction(Model.getMetaTypes().getSubsystem(),"Subsystem");
		}
		return actionSubsystem;
	}
	protected Action getActionUniAggregation() {
		if (actionUniAggregation == null) {
			actionUniAggregation = makeCreateAssociationAction(Model.getAggregationKind().getAggregate(),true,"button.new-uniaggregation");
		}
		return actionUniAggregation;
	}
	protected Action getActionUniAssociation() {
		if (actionUniAssociation == null) {
			actionUniAssociation = makeCreateAssociationAction(Model.getAggregationKind().getNone(),true,"button.new-uniassociation");
		}
		return actionUniAssociation;
	}
	protected Action getActionUniComposition() {
		if (actionUniComposition == null) {
			actionUniComposition = makeCreateAssociationAction(Model.getAggregationKind().getComposite(),true,"button.new-unicomposition");
		}
		return actionUniComposition;
	}
	protected Action getActionUsage() {
		if (actionUsage == null) {
			actionUsage = makeCreateDependencyAction(ModeCreateUsage.class,Model.getMetaTypes().getUsage(),"button.new-usage");
		}
		return actionUsage;
	}
	private Action getActionDataType() {
		if (actionDataType == null) {
			actionDataType = makeCreateNodeAction(Model.getMetaTypes().getDataType(),"button.new-datatype");
		}
		return actionDataType;
	}
	private Action getActionEnumeration() {
		if (actionEnumeration == null) {
			actionEnumeration = makeCreateNodeAction(Model.getMetaTypes().getEnumeration(),"button.new-enumeration");
		}
		return actionEnumeration;
	}
	private Action getActionStereotype() {
		if (actionStereotype == null) {
			actionStereotype = makeCreateNodeAction(Model.getMetaTypes().getStereotype(),"button.new-stereotype");
		}
		return actionStereotype;
	}
	private Action getActionSignal() {
		if (actionSignal == null) {
			actionSignal = makeCreateNodeAction(Model.getMetaTypes().getSignal(),"button.new-signal");
		}
		return actionSignal;
	}
	private Action getActionException() {
		if (actionException == null) {
			actionException = makeCreateNodeAction(Model.getMetaTypes().getException(),"button.new-exception");
		}
		return actionException;
	}
	public boolean isRelocationAllowed(Object base) {
		return Model.getFacade().isANamespace(base);
	}
	public Collection getRelocationCandidates(Object root) {
		return Model.getModelManagementHelper().getAllModelElementsOfKindWithModel(root,Model.getMetaTypes().getNamespace());
	}
	public boolean relocate(Object base) {
		setNamespace(base);
		damage();
		return true;
	}
	public void encloserChanged(FigNode enclosed,FigNode oldEncloser,FigNode newEncloser) {
	}
	@Override public boolean doesAccept(Object objectToAccept) {
		if (Model.getFacade().isAClass(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isAInterface(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isAModel(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isASubsystem(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isAPackage(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isAComment(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isAAssociation(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isAEnumeration(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isADataType(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isAStereotype(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isAException(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isASignal(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isAObject(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isANodeInstance(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isAComponentInstance(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isANode(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isAComponent(objectToAccept)) {
			return true;
		}
		return false;
	}
	@Override public FigNode drop(Object droppedObject,Point location) {
		FigNode figNode = null;
		Rectangle bounds = null;
		if (location != null) {
			bounds = new Rectangle(location.x,location.y,0,0);
		}
		DiagramSettings settings = getDiagramSettings();
		if (Model.getFacade().isAAssociation(droppedObject)) {
			figNode = createNaryAssociationNode(droppedObject,bounds,settings);
		}else if (Model.getFacade().isAClass(droppedObject)) {
			figNode = null;
		}else if (Model.getFacade().isAInterface(droppedObject)) {
			figNode = new FigInterface(droppedObject,bounds,settings);
		}else if (Model.getFacade().isAModel(droppedObject)) {
			figNode = new FigModel(droppedObject,bounds,settings);
		}else if (Model.getFacade().isASubsystem(droppedObject)) {
			figNode = new FigSubsystem(droppedObject,bounds,settings);
		}else if (Model.getFacade().isAPackage(droppedObject)) {
			figNode = new FigPackage(droppedObject,bounds,settings);
		}else if (Model.getFacade().isAComment(droppedObject)) {
			figNode = new FigComment(droppedObject,bounds,settings);
		}else if (Model.getFacade().isAEnumeration(droppedObject)) {
			figNode = new FigEnumeration(droppedObject,bounds,settings);
		}else if (Model.getFacade().isADataType(droppedObject)) {
			figNode = new FigDataType(droppedObject,bounds,settings);
		}else if (Model.getFacade().isAStereotype(droppedObject)) {
			figNode = new FigStereotypeDeclaration(droppedObject,bounds,settings);
		}else if (Model.getFacade().isAException(droppedObject)) {
			figNode = new FigException(droppedObject,bounds,settings);
		}else if (Model.getFacade().isASignal(droppedObject)) {
			figNode = new FigSignal(droppedObject,bounds,settings);
		}
		if (figNode != null) {
			if (location != null) {
				figNode.setLocation(location.x,location.y);
			}
			LOG.debug("Dropped object " + droppedObject + " converted to " + figNode);
		}else {
			LOG.debug("Dropped object NOT added " + droppedObject);
		}
		return figNode;
	}
}



