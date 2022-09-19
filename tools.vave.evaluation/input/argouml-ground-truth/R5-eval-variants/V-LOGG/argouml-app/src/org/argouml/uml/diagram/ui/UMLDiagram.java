package org.argouml.uml.diagram.ui;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.JToolBar;
import org.apache.log4j.Logger;
import org.argouml.gefext.ArgoModeCreateFigCircle;
import org.argouml.gefext.ArgoModeCreateFigInk;
import org.argouml.gefext.ArgoModeCreateFigLine;
import org.argouml.gefext.ArgoModeCreateFigPoly;
import org.argouml.gefext.ArgoModeCreateFigRRect;
import org.argouml.gefext.ArgoModeCreateFigRect;
import org.argouml.gefext.ArgoModeCreateFigSpline;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.UUIDHelper;
import org.argouml.uml.diagram.ArgoDiagramImpl;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.Relocatable;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.util.ToolBarUtility;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.ModeBroom;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.base.ModePlace;
import org.tigris.gef.base.ModeSelect;
import org.tigris.gef.graph.GraphFactory;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigNode;
import org.tigris.toolbar.ToolBarFactory;
import org.tigris.toolbar.ToolBarManager;
import org.tigris.toolbar.toolbutton.ToolButton;
import org.argouml.uml.diagram.ui.ActionAddNote;
import org.argouml.uml.diagram.ui.FigNodeAssociation;
import org.argouml.uml.diagram.ui.FigClassAssociationClass;
import org.argouml.uml.diagram.ui.FigEdgeAssociationClass;


public abstract class UMLDiagram extends ArgoDiagramImpl implements Relocatable {
	private static final Logger LOG = Logger.getLogger(UMLDiagram.class);
	private static Action actionComment = new RadioAction(new ActionAddNote());
	private static Action actionCommentLink = new RadioAction(new ActionSetAddCommentLinkMode());
	private static Action actionSelect = new ActionSetMode(ModeSelect.class,"button.select");
	private static Action actionBroom = new ActionSetMode(ModeBroom.class,"button.broom");
	private static Action actionRectangle = new RadioAction(new ActionSetMode(ArgoModeCreateFigRect.class,"Rectangle","misc.primitive.rectangle"));
	private static Action actionRRectangle = new RadioAction(new ActionSetMode(ArgoModeCreateFigRRect.class,"RRect","misc.primitive.rounded-rectangle"));
	private static Action actionCircle = new RadioAction(new ActionSetMode(ArgoModeCreateFigCircle.class,"Circle","misc.primitive.circle"));
	private static Action actionLine = new RadioAction(new ActionSetMode(ArgoModeCreateFigLine.class,"Line","misc.primitive.line"));
	private static Action actionText = new RadioAction(new ActionSetMode(ArgoModeCreateFigText.class,"Text","misc.primitive.text"));
	private static Action actionPoly = new RadioAction(new ActionSetMode(ArgoModeCreateFigPoly.class,"Polygon","misc.primitive.polygon"));
	private static Action actionSpline = new RadioAction(new ActionSetMode(ArgoModeCreateFigSpline.class,"Spline","misc.primitive.spline"));
	private static Action actionInk = new RadioAction(new ActionSetMode(ArgoModeCreateFigInk.class,"Ink","misc.primitive.ink"));
	private JToolBar toolBar;
	private Action selectedAction;
	@SuppressWarnings("deprecation")@Deprecated public UMLDiagram() {
		super();
	}
	@Deprecated public UMLDiagram(Object ns) {
		this();
		if (!Model.getFacade().isANamespace(ns)) {
			throw new IllegalArgumentException();
		}
		setNamespace(ns);
	}
	@Deprecated public UMLDiagram(String name,Object ns) {
		this(ns);
		try {
			setName(name);
		}catch (PropertyVetoException pve) {
			LOG.fatal("Name not allowed in construction of diagram");
		}
	}
	public UMLDiagram(String name,Object ns,GraphModel graphModel) {
		super(name,graphModel,new LayerPerspective(name,graphModel));
		setNamespace(ns);
	}
	public UMLDiagram(GraphModel graphModel) {
		super("",graphModel,new LayerPerspective("",graphModel));
	}
	@Override public void initialize(Object owner) {
		super.initialize(owner);
		if (Model.getFacade().isANamespace(owner)) {
			setNamespace(owner);
		}
	}
	public String getClassAndModelID() {
		String s = super.getClassAndModelID();
		if (getOwner() == null) {
			return s;
		}
		String id = UUIDHelper.getUUID(getOwner());
		return s + "|" + id;
	}
	public JToolBar getJToolBar() {
		if (toolBar == null) {
			initToolBar();
			toolBar.setName("misc.toolbar.diagram");
		}
		return toolBar;
	}
	public void initToolBar() {
		ToolBarFactory factory = new ToolBarFactory(getActions());
		factory.setRollover(true);
		factory.setFloatable(false);
		toolBar = factory.createToolBar();
		toolBar.putClientProperty("ToolBar.toolTipSelectTool",Translator.localize("action.select"));
	}
	public Object[]getActions() {
		Object[]manipulateActions = getManipulateActions();
		Object[]umlActions = getUmlActions();
		Object[]commentActions = getCommentActions();
		Object[]shapeActions = getShapeActions();
		Object[]actions = new Object[manipulateActions. + umlActions. + commentActions. + shapeActions.];
		int posn = 0;
		System.arraycopy(manipulateActions,0,actions,posn,manipulateActions.);
		posn += manipulateActions.;
		System.arraycopy(umlActions,0,actions,posn,umlActions.);
		posn += umlActions.;
		System.arraycopy(commentActions,0,actions,posn,commentActions.);
		posn += commentActions.;
		System.arraycopy(shapeActions,0,actions,posn,shapeActions.);
		return actions;
	}
	protected abstract Object[]getUmlActions();
	private Object[]getManipulateActions() {
		Object[]actions =  {new RadioAction(actionSelect),new RadioAction(actionBroom),null};
		return actions;
	}
	private Object[]getCommentActions() {
		Object[]actions =  {null,actionComment,actionCommentLink};
		return actions;
	}
	private Object[]getShapeActions() {
		Object[]actions =  {null,getShapePopupActions()};
		return actions;
	}
	private Object[]getShapePopupActions() {
		Object[][]actions =  { {actionRectangle,actionRRectangle}, {actionCircle,actionLine}, {actionText,actionPoly}, {actionSpline,actionInk}};
		ToolBarUtility.manageDefault(actions,"diagram.shape");
		return actions;
	}
	public void setSelectedAction(Action theAction) {
		selectedAction = theAction;
		int toolCount = toolBar.getComponentCount();
		for (int i = 0;i < toolCount;++i) {
			Component c = toolBar.getComponent(i);
			if (c instanceof ToolButton) {
				ToolButton tb = (ToolButton) c;
				Action action = tb.getRealAction();
				if (action instanceof RadioAction) {
					action = ((RadioAction) action).getAction();
				}
				Action otherAction = theAction;
				if (theAction instanceof RadioAction) {
					otherAction = ((RadioAction) theAction).getAction();
				}
				if (action != null&&!action.equals(otherAction)) {
					tb.setSelected(false);
					ButtonModel bm = tb.getModel();
					bm.setRollover(false);
					bm.setSelected(false);
					bm.setArmed(false);
					bm.setPressed(false);
					if (!ToolBarManager.alwaysUseStandardRollover()) {
						tb.setBorderPainted(false);
					}
				}else {
					tb.setSelected(true);
					ButtonModel bm = tb.getModel();
					bm.setRollover(true);
					if (!ToolBarManager.alwaysUseStandardRollover()) {
						tb.setBorderPainted(true);
					}
				}
			}
		}
	}
	public Action getSelectedAction() {
		return selectedAction;
	}
	public void deselectAllTools() {
		setSelectedAction(actionSelect);
		actionSelect.actionPerformed(null);
	}
	protected Action makeCreateNodeAction(Object modelElement,String descr) {
		return new RadioAction(new CmdCreateNode(modelElement,descr));
	}
	protected Action makeCreateEdgeAction(Object modelElement,String descr) {
		return new RadioAction(new ActionSetMode(ModeCreatePolyEdge.class,"edgeClass",modelElement,descr));
	}
	protected Action makeCreateDependencyAction(Class modeClass,Object metaType,String descr) {
		return new RadioAction(new ActionSetMode(modeClass,"edgeClass",metaType,descr));
	}
	protected Action makeCreateGeneralizationAction() {
		return new RadioAction(new ActionSetMode(ModeCreateGeneralization.class,"edgeClass",Model.getMetaTypes().getGeneralization(),"button.new-generalization"));
	}
	protected Action makeCreateAssociationAction(Object aggregationKind,boolean unidirectional,String descr) {
		return new RadioAction(new ActionSetAddAssociationMode(aggregationKind,unidirectional,descr));
	}
	protected Action makeCreateAssociationEndAction(String descr) {
		return new RadioAction(new ActionSetAddAssociationEndMode(descr));
	}
	protected Action makeCreateAssociationClassAction(String descr) {
		return new RadioAction(new ActionSetAddAssociationClassMode(descr));
	}
	@Deprecated public void resetDiagramSerial() {
	}
	@Deprecated protected int getNextDiagramSerial() {
		return 1;
	}
	public abstract String getLabelName();
	public abstract boolean isRelocationAllowed(Object base);
	public abstract boolean relocate(Object base);
	@Override public final void setProject(Project p) {
		super.setProject(p);
		UMLMutableGraphSupport gm = (UMLMutableGraphSupport) getGraphModel();
		gm.setProject(p);
	}
	protected String getNewDiagramName() {
		return getLabelName();
	}
	public boolean doesAccept(@SuppressWarnings("unused")Object objectToAccept) {
		return false;
	}
	@SuppressWarnings("unused")public FigNode drop(Object droppedObject,Point location) {
		return null;
	}
	public String getInstructions(Object droppedObject) {
		return Translator.localize("misc.message.click-on-diagram-to-add",new Object[] {Model.getFacade().toString(droppedObject)});
	}
	public ModePlace getModePlace(GraphFactory gf,String instructions) {
		return new ModePlace(gf,instructions);
	}
	protected FigNode createNaryAssociationNode(final Object modelElement,final Rectangle bounds,final DiagramSettings settings) {
		final FigNodeAssociation diamondFig = new FigNodeAssociation(modelElement,bounds,settings);
		if (Model.getFacade().isAAssociationClass(modelElement)&&bounds != null) {
			final FigClassAssociationClass classBoxFig = new FigClassAssociationClass(modelElement,bounds,settings);
			final FigEdgeAssociationClass dashEdgeFig = new FigEdgeAssociationClass(classBoxFig,diamondFig,settings);
			classBoxFig.renderingChanged();
			Point location = bounds.getLocation();
			location.y = (location.y - diamondFig.getHeight()) - 32;
			if (location.y < 16) {
				location.y = 16;
			}
			classBoxFig.setLocation(location);
			this.add(diamondFig);
			this.add(classBoxFig);
			this.add(dashEdgeFig);
		}
		return diamondFig;
	}
}



