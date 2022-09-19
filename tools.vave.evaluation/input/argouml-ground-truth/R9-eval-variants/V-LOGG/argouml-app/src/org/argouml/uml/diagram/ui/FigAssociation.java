package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.ArrowHead;
import org.tigris.gef.presentation.ArrowHeadComposite;
import org.tigris.gef.presentation.ArrowHeadDiamond;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.ArrowHeadNone;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigText;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.PathItemPlacement;
import org.argouml.uml.diagram.ui.FigSingleLineTextWithNotation;
import org.argouml.uml.diagram.ui.FigSingleLineText;


public class FigAssociation extends FigEdgeModelElement {
	private static final Logger LOG = Logger.getLogger(FigAssociation.class);
	private FigAssociationEndAnnotation srcGroup;
	private FigAssociationEndAnnotation destGroup;
	private FigTextGroup middleGroup;
	private FigMultiplicity srcMult;
	private FigMultiplicity destMult;
	@SuppressWarnings("deprecation")@Deprecated public FigAssociation() {
		super();
		middleGroup = new FigTextGroup();
		if (getNameFig() != null) {
			middleGroup.addFig(getNameFig());
		}
		middleGroup.addFig(getStereotypeFig());
		addPathItem(middleGroup,new PathItemPlacement(this,middleGroup,50,25));
		ArgoFigUtil.markPosition(this,50,0,90,25,Color.yellow);
		srcMult = new FigMultiplicity();
		addPathItem(srcMult,new PathItemPlacement(this,srcMult,0,5,135,5));
		ArgoFigUtil.markPosition(this,0,5,135,5,Color.green);
		srcGroup = new FigAssociationEndAnnotation(this);
		addPathItem(srcGroup,new PathItemPlacement(this,srcGroup,0,5,-135,5));
		ArgoFigUtil.markPosition(this,0,5,-135,5,Color.blue);
		destMult = new FigMultiplicity();
		addPathItem(destMult,new PathItemPlacement(this,destMult,100,-5,45,5));
		ArgoFigUtil.markPosition(this,100,-5,45,5,Color.red);
		destGroup = new FigAssociationEndAnnotation(this);
		addPathItem(destGroup,new PathItemPlacement(this,destGroup,100,-5,-45,5));
		ArgoFigUtil.markPosition(this,100,-5,-45,5,Color.orange);
		setBetweenNearestPoints(true);
	}
	public FigAssociation(Object owner,DiagramSettings settings) {
		super(owner,settings);
		createNameLabel(owner,settings);
		Object[]ends = Model.getFacade().getConnections(owner).toArray();
		srcMult = new FigMultiplicity(ends[0],settings);
		addPathItem(srcMult,new PathItemPlacement(this,srcMult,0,5,135,5));
		ArgoFigUtil.markPosition(this,0,5,135,5,Color.green);
		srcGroup = new FigAssociationEndAnnotation(this,ends[0],settings);
		addPathItem(srcGroup,new PathItemPlacement(this,srcGroup,0,5,-135,5));
		ArgoFigUtil.markPosition(this,0,5,-135,5,Color.blue);
		destMult = new FigMultiplicity(ends[1],settings);
		addPathItem(destMult,new PathItemPlacement(this,destMult,100,-5,45,5));
		ArgoFigUtil.markPosition(this,100,-5,45,5,Color.red);
		destGroup = new FigAssociationEndAnnotation(this,ends[1],settings);
		addPathItem(destGroup,new PathItemPlacement(this,destGroup,100,-5,-45,5));
		ArgoFigUtil.markPosition(this,100,-5,-45,5,Color.orange);
		setBetweenNearestPoints(true);
		initializeNotationProvidersInternal(owner);
	}
	@Deprecated public FigAssociation(Object edge,Layer lay) {
		this();
		setOwner(edge);
		setLayer(lay);
	}
	protected void createNameLabel(Object owner,DiagramSettings settings) {
		middleGroup = new FigTextGroup(owner,settings);
		if (getNameFig() != null) {
			middleGroup.addFig(getNameFig());
		}
		middleGroup.addFig(getStereotypeFig());
		addPathItem(middleGroup,new PathItemPlacement(this,middleGroup,50,25));
		ArgoFigUtil.markPosition(this,50,0,90,25,Color.yellow);
	}
	@SuppressWarnings("deprecation")@Deprecated@Override public void setOwner(Object owner) {
		super.setOwner(owner);
		Object[]ends = Model.getFacade().getConnections(owner).toArray();
		Object source = ends[0];
		Object dest = ends[1];
		srcGroup.setOwner(source);
		srcMult.setOwner(source);
		destGroup.setOwner(dest);
		destMult.setOwner(dest);
	}
	@Override public void renderingChanged() {
		super.renderingChanged();
		srcMult.renderingChanged();
		destMult.renderingChanged();
		srcGroup.renderingChanged();
		destGroup.renderingChanged();
		middleGroup.renderingChanged();
	}
	@Override protected void initNotationProviders(Object own) {
		initializeNotationProvidersInternal(own);
	}
	private void initializeNotationProvidersInternal(Object own) {
		super.initNotationProviders(own);
		srcMult.initNotationProviders();
		destMult.initNotationProviders();
	}
	@Override public void updateListeners(Object oldOwner,Object newOwner) {
		Set<Object[]>listeners = new HashSet<Object[]>();
		if (newOwner != null) {
			listeners.add(new Object[] {newOwner,new String[] {"isAbstract","remove"}});
		}
		updateElementListeners(listeners);
	}
	@Override protected int getNotationProviderType() {
		return NotationProviderFactory2.TYPE_ASSOCIATION_NAME;
	}
	@Override protected void textEdited(FigText ft) {
		if (getOwner() == null) {
			return;
		}
		super.textEdited(ft);
		Collection conn = Model.getFacade().getConnections(getOwner());
		if (conn == null||conn.size() == 0) {
			return;
		}
		if (ft == srcGroup.getRole()) {
			srcGroup.getRole().textEdited();
		}else if (ft == destGroup.getRole()) {
			destGroup.getRole().textEdited();
		}else if (ft == srcMult) {
			srcMult.textEdited();
		}else if (ft == destMult) {
			destMult.textEdited();
		}
	}
	@Override protected void textEditStarted(FigText ft) {
		if (ft == srcGroup.getRole()) {
			srcGroup.getRole().textEditStarted();
		}else if (ft == destGroup.getRole()) {
			destGroup.getRole().textEditStarted();
		}else if (ft == srcMult) {
			srcMult.textEditStarted();
		}else if (ft == destMult) {
			destMult.textEditStarted();
		}else {
			super.textEditStarted(ft);
		}
	}
	protected void applyArrowHeads() {
		if (srcGroup == null||destGroup == null) {
			return;
		}
		int sourceArrowType = srcGroup.getArrowType();
		int destArrowType = destGroup.getArrowType();
		if (!getSettings().isShowBidirectionalArrows()&&sourceArrowType > 2&&destArrowType > 2) {
			sourceArrowType -= 3;
			destArrowType -= 3;
		}
		setSourceArrowHead(FigAssociationEndAnnotation.ARROW_HEADS[sourceArrowType]);
		setDestArrowHead(FigAssociationEndAnnotation.ARROW_HEADS[destArrowType]);
	}
	@Override public Vector getPopUpActions(MouseEvent me) {
		Vector popUpActions = super.getPopUpActions(me);
		boolean ms = TargetManager.getInstance().getTargets().size() > 1;
		if (ms) {
			return popUpActions;
		}
		Point firstPoint = this.getFirstPoint();
		Point lastPoint = this.getLastPoint();
		int length = getPerimeterLength();
		int rSquared = (int) (0.3 * length);
		if (rSquared > 100) {
			rSquared = 10000;
		}else {
			rSquared *= rSquared;
		}
		int srcDeterminingFactor = getSquaredDistance(me.getPoint(),firstPoint);
		int destDeterminingFactor = getSquaredDistance(me.getPoint(),lastPoint);
		if (srcDeterminingFactor < rSquared&&srcDeterminingFactor < destDeterminingFactor) {
			ArgoJMenu multMenu = new ArgoJMenu("menu.popup.multiplicity");
			multMenu.add(ActionMultiplicity.getSrcMultOne());
			multMenu.add(ActionMultiplicity.getSrcMultZeroToOne());
			multMenu.add(ActionMultiplicity.getSrcMultOneToMany());
			multMenu.add(ActionMultiplicity.getSrcMultZeroToMany());
			popUpActions.add(popUpActions.size() - getPopupAddOffset(),multMenu);
			ArgoJMenu aggMenu = new ArgoJMenu("menu.popup.aggregation");
			aggMenu.add(ActionAggregation.getSrcAggNone());
			aggMenu.add(ActionAggregation.getSrcAgg());
			aggMenu.add(ActionAggregation.getSrcAggComposite());
			popUpActions.add(popUpActions.size() - getPopupAddOffset(),aggMenu);
		}else if (destDeterminingFactor < rSquared) {
			ArgoJMenu multMenu = new ArgoJMenu("menu.popup.multiplicity");
			multMenu.add(ActionMultiplicity.getDestMultOne());
			multMenu.add(ActionMultiplicity.getDestMultZeroToOne());
			multMenu.add(ActionMultiplicity.getDestMultOneToMany());
			multMenu.add(ActionMultiplicity.getDestMultZeroToMany());
			popUpActions.add(popUpActions.size() - getPopupAddOffset(),multMenu);
			ArgoJMenu aggMenu = new ArgoJMenu("menu.popup.aggregation");
			aggMenu.add(ActionAggregation.getDestAggNone());
			aggMenu.add(ActionAggregation.getDestAgg());
			aggMenu.add(ActionAggregation.getDestAggComposite());
			popUpActions.add(popUpActions.size() - getPopupAddOffset(),aggMenu);
		}
		Object association = getOwner();
		if (association != null) {
			Collection ascEnds = Model.getFacade().getConnections(association);
			Iterator iter = ascEnds.iterator();
			Object ascStart = iter.next();
			Object ascEnd = iter.next();
			if (Model.getFacade().isAClassifier(Model.getFacade().getType(ascStart))&&Model.getFacade().isAClassifier(Model.getFacade().getType(ascEnd))) {
				ArgoJMenu navMenu = new ArgoJMenu("menu.popup.navigability");
				navMenu.add(ActionNavigability.newActionNavigability(ascStart,ascEnd,ActionNavigability.BIDIRECTIONAL));
				navMenu.add(ActionNavigability.newActionNavigability(ascStart,ascEnd,ActionNavigability.STARTTOEND));
				navMenu.add(ActionNavigability.newActionNavigability(ascStart,ascEnd,ActionNavigability.ENDTOSTART));
				popUpActions.add(popUpActions.size() - getPopupAddOffset(),navMenu);
			}
		}
		return popUpActions;
	}
	protected void updateMultiplicity() {
		if (getOwner() != null&&srcMult.getOwner() != null&&destMult.getOwner() != null) {
			srcMult.setText();
			destMult.setText();
		}
	}
	@Override public void paint(Graphics g) {
		if (getOwner() == null) {
			LOG.error("Trying to paint a FigAssociation without an owner. ");
		}else {
			applyArrowHeads();
		}
		if (getSourceArrowHead() != null&&getDestArrowHead() != null) {
			getSourceArrowHead().setLineColor(getLineColor());
			getDestArrowHead().setLineColor(getLineColor());
		}
		super.paint(g);
	}
	@Override public void paintClarifiers(Graphics g) {
		indicateBounds(getNameFig(),g);
		indicateBounds(srcMult,g);
		indicateBounds(srcGroup.getRole(),g);
		indicateBounds(destMult,g);
		indicateBounds(destGroup.getRole(),g);
		super.paintClarifiers(g);
	}
	protected FigTextGroup getMiddleGroup() {
		return middleGroup;
	}
	@Override protected void layoutEdge() {
		FigNode sourceFigNode = getSourceFigNode();
		Point[]points = getPoints();
		if (points. < 3&&sourceFigNode != null&&getDestFigNode() == sourceFigNode) {
			Rectangle rect = new Rectangle(sourceFigNode.getX() + sourceFigNode.getWidth() - 20,sourceFigNode.getY() + sourceFigNode.getHeight() - 20,40,40);
			points = new Point[5];
			points[0] = new Point(rect.x,rect.y + rect.height / 2);
			points[1] = new Point(rect.x,rect.y + rect.height);
			points[2] = new Point(rect.x + rect.width,rect.y + rect.height);
			points[3] = new Point(rect.x + rect.width,rect.y);
			points[4] = new Point(rect.x + rect.width / 2,rect.y);
			setPoints(points);
		}else {
			super.layoutEdge();
		}
	}
	protected void updateNameText() {
		super.updateNameText();
		if (middleGroup != null) {
			middleGroup.calcBounds();
		}
	}
}

class FigMultiplicity extends FigSingleLineTextWithNotation {
	@SuppressWarnings("deprecation")@Deprecated FigMultiplicity() {
		super(X0,Y0,90,20,false,new String[] {"multiplicity"});
		setTextFilled(false);
		setJustification(FigText.JUSTIFY_CENTER);
	}
	FigMultiplicity(Object owner,DiagramSettings settings) {
			super(owner,new Rectangle(X0,Y0,90,20),settings,false,"multiplicity");
			setTextFilled(false);
			setJustification(FigText.JUSTIFY_CENTER);
		}
	@Override protected int getNotationProviderType() {
		return NotationProviderFactory2.TYPE_MULTIPLICITY;
	}
}

class FigOrdering extends FigSingleLineText {
	private static final long serialVersionUID = 5385230942216677015l;
	@SuppressWarnings("deprecation")@Deprecated FigOrdering() {
		super(X0,Y0,90,20,false,"ordering");
		setTextFilled(false);
		setJustification(FigText.JUSTIFY_CENTER);
		setEditable(false);
	}
	FigOrdering(Object owner,DiagramSettings settings) {
			super(owner,new Rectangle(X0,Y0,90,20),settings,false,"ordering");
			setTextFilled(false);
			setJustification(FigText.JUSTIFY_CENTER);
			setEditable(false);
		}
	@Override protected void setText() {
		assert getOwner() != null;
		if (getSettings().getNotationSettings().isShowProperties()) {
			setText(getOrderingName(Model.getFacade().getOrdering(getOwner())));
		}else {
			setText("");
		}
		damage();
	}
	private String getOrderingName(Object orderingKind) {
		if (orderingKind == null) {
			return"";
		}
		if (Model.getFacade().getName(orderingKind) == null) {
			return"";
		}
		if ("".equals(Model.getFacade().getName(orderingKind))) {
			return"";
		}
		if ("unordered".equals(Model.getFacade().getName(orderingKind))) {
			return"";
		}
		return"{" + Model.getFacade().getName(orderingKind) + "}";
	}
}

class FigRole extends FigSingleLineTextWithNotation {
	@SuppressWarnings("deprecation")@Deprecated FigRole() {
		super(X0,Y0,90,20,false,(String[]) null);
		setTextFilled(false);
		setJustification(FigText.JUSTIFY_CENTER);
	}
	FigRole(Object owner,DiagramSettings settings) {
			super(owner,new Rectangle(X0,Y0,90,20),settings,false,(String[]) null);
			setTextFilled(false);
			setJustification(FigText.JUSTIFY_CENTER);
			setText();
		}
	protected int getNotationProviderType() {
		return NotationProviderFactory2.TYPE_ASSOCIATION_END_NAME;
	}
	@Override public void propertyChange(PropertyChangeEvent pce) {
		super.propertyChange(pce);
		this.getGroup().calcBounds();
	}
}

class FigAssociationEndAnnotation extends FigTextGroup {
	private static final long serialVersionUID = 1871796732318164649l;
	private static final ArrowHead NAV_AGGR = new ArrowHeadComposite(ArrowHeadDiamond.WhiteDiamond,new ArrowHeadGreater());
	private static final ArrowHead NAV_COMP = new ArrowHeadComposite(ArrowHeadDiamond.BlackDiamond,new ArrowHeadGreater());
	private static final int NONE = 0;
	private static final int AGGREGATE = 1;
	private static final int COMPOSITE = 2;
	private static final int NAV_NONE = 3;
	private static final int NAV_AGGREGATE = 4;
	private static final int NAV_COMPOSITE = 5;
	public static final ArrowHead[]ARROW_HEADS = new ArrowHead[6];
	static {
	ARROW_HEADS[NONE] = ArrowHeadNone.TheInstance;
	ARROW_HEADS[AGGREGATE] = ArrowHeadDiamond.WhiteDiamond;
	ARROW_HEADS[COMPOSITE] = ArrowHeadDiamond.BlackDiamond;
	ARROW_HEADS[NAV_NONE] = new ArrowHeadGreater();
	ARROW_HEADS[NAV_AGGREGATE] = NAV_AGGR;
	ARROW_HEADS[NAV_COMPOSITE] = NAV_COMP;
}
	private FigRole role;
	private FigOrdering ordering;
	private int arrowType = 0;
	private FigEdgeModelElement figEdge;
	@SuppressWarnings("deprecation")@Deprecated FigAssociationEndAnnotation(FigEdgeModelElement edge) {
		figEdge = edge;
		role = new FigRole();
		addFig(role);
		ordering = new FigOrdering();
		addFig(ordering);
	}
	FigAssociationEndAnnotation(FigEdgeModelElement edge,Object owner,DiagramSettings settings) {
			super(owner,settings);
			figEdge = edge;
			role = new FigRole(owner,settings);
			addFig(role);
			ordering = new FigOrdering(owner,settings);
			addFig(ordering);
			determineArrowHead();
			Model.getPump().addModelEventListener(this,owner,new String[] {"isNavigable","aggregation","participant"});
		}
	@SuppressWarnings("deprecation")@Deprecated@Override public void setOwner(Object owner) {
		if (owner != null) {
			if (!Model.getFacade().isAAssociationEnd(owner)) {
				throw new IllegalArgumentException("An AssociationEnd was expected");
			}
			super.setOwner(owner);
			ordering.setOwner(owner);
			role.setOwner(owner);
			role.setText();
			determineArrowHead();
			Model.getPump().addModelEventListener(this,owner,new String[] {"isNavigable","aggregation","participant"});
		}
	}
	@Override public void removeFromDiagram() {
		Model.getPump().removeModelEventListener(this,getOwner(),new String[] {"isNavigable","aggregation","participant"});
		super.removeFromDiagram();
	}
	@Override public void propertyChange(PropertyChangeEvent pce) {
		if (pce instanceof AttributeChangeEvent&&(pce.getPropertyName().equals("isNavigable")||pce.getPropertyName().equals("aggregation"))) {
			determineArrowHead();
			((FigAssociation) figEdge).applyArrowHeads();
			damage();
		}
		if (pce instanceof AddAssociationEvent&&pce.getPropertyName().equals("participant")) {
			figEdge.determineFigNodes();
		}
		String pName = pce.getPropertyName();
		if (pName.equals("editing")&&Boolean.FALSE.equals(pce.getNewValue())) {
			role.textEdited();
			calcBounds();
			endTrans();
		}else if (pName.equals("editing")&&Boolean.TRUE.equals(pce.getNewValue())) {
			role.textEditStarted();
		}else {
			super.propertyChange(pce);
		}
	}
	private void determineArrowHead() {
		assert getOwner() != null;
		Object ak = Model.getFacade().getAggregation(getOwner());
		boolean nav = Model.getFacade().isNavigable(getOwner());
		if (nav) {
			if (Model.getAggregationKind().getNone().equals(ak)||(ak == null)) {
				arrowType = NAV_NONE;
			}else if (Model.getAggregationKind().getAggregate().equals(ak)) {
				arrowType = NAV_AGGREGATE;
			}else if (Model.getAggregationKind().getComposite().equals(ak)) {
				arrowType = NAV_COMPOSITE;
			}
		}else {
			if (Model.getAggregationKind().getNone().equals(ak)||(ak == null)) {
				arrowType = NONE;
			}else if (Model.getAggregationKind().getAggregate().equals(ak)) {
				arrowType = AGGREGATE;
			}else if (Model.getAggregationKind().getComposite().equals(ak)) {
				arrowType = COMPOSITE;
			}
		}
	}
	public int getArrowType() {
		return arrowType;
	}
	FigRole getRole() {
		return role;
	}
}



