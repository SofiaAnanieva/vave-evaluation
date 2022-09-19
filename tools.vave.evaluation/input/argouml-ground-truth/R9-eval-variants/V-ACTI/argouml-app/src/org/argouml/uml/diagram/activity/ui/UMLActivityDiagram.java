package org.argouml.uml.diagram.activity.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.ActivityGraphsHelper;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.Model;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.activity.ActivityDiagramGraphModel;
import org.argouml.uml.diagram.state.ui.FigFinalState;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.ui.ActionSetMode;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewActionSequence;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewCallAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewCreateAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewDestroyAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewReturnAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewSendAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewTerminateAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewUninterpretedAction;
import org.argouml.uml.ui.behavior.state_machines.ButtonActionNewGuard;
import org.argouml.util.ToolBarUtility;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;


public class UMLActivityDiagram extends UMLDiagram {
	private static final long serialVersionUID = 6223128918989919230l;
	private Object theActivityGraph;
	private Action actionState;
	private Action actionStartPseudoState;
	private Action actionFinalPseudoState;
	private Action actionJunctionPseudoState;
	private Action actionForkPseudoState;
	private Action actionJoinPseudoState;
	private Action actionTransition;
	private Action actionObjectFlowState;
	private Action actionSwimlane;
	private Action actionCallState;
	private Action actionSubactivityState;
	private Action actionCallEvent;
	private Action actionChangeEvent;
	private Action actionSignalEvent;
	private Action actionTimeEvent;
	private Action actionGuard;
	private Action actionCallAction;
	private Action actionCreateAction;
	private Action actionDestroyAction;
	private Action actionReturnAction;
	private Action actionSendAction;
	private Action actionTerminateAction;
	private Action actionUninterpretedAction;
	private Action actionActionSequence;
	@Deprecated public UMLActivityDiagram() {
		super();
		try {
			setName(getNewDiagramName());
		}catch (PropertyVetoException pve) {
		}
		setGraphModel(createGraphModel());
	}
	@Deprecated public UMLActivityDiagram(Object namespace,Object agraph) {
		this();
		if (namespace == null) {
			namespace = Model.getFacade().getNamespace(agraph);
		}
		if (!Model.getFacade().isANamespace(namespace)||!Model.getFacade().isAActivityGraph(agraph)) {
			throw new IllegalArgumentException();
		}
		if (Model.getFacade().getName(namespace) != null) {
			if (!Model.getFacade().getName(namespace).trim().equals("")) {
				String name = Model.getFacade().getName(namespace) + " activity " + (Model.getFacade().getBehaviors(namespace).size());
				try {
					setName(name);
				}catch (PropertyVetoException pve) {
				}
			}
		}
		setup(namespace,agraph);
	}
	public void initialize(Object o) {
		if (!(Model.getFacade().isAActivityGraph(o))) {
			return;
		}
		Object context = Model.getFacade().getContext(o);
		if (context != null) {
			if (Model.getFacade().isABehavioralFeature(context)) {
				setup(Model.getFacade().getNamespace(Model.getFacade().getOwner(context)),o);
			}else {
				setup(context,o);
			}
		}else {
			Object namespace4Diagram = Model.getFacade().getNamespace(o);
			if (namespace4Diagram != null) {
				setup(namespace4Diagram,o);
			}else {
				throw new IllegalStateException("Cannot find context " + "nor namespace while initializing activity diagram");
			}
		}
	}
	public void setup(Object namespace,Object agraph) {
		if (!Model.getFacade().isANamespace(namespace)||!Model.getFacade().isAActivityGraph(agraph)) {
			throw new IllegalArgumentException();
		}
		setNamespace(namespace);
		theActivityGraph = agraph;
		ActivityDiagramGraphModel gm = createGraphModel();
		gm.setHomeModel(namespace);
		if (theActivityGraph != null) {
			gm.setMachine(theActivityGraph);
		}
		ActivityDiagramRenderer rend = new ActivityDiagramRenderer();
		LayerPerspective lay = new LayerPerspectiveMutable(Model.getFacade().getName(namespace),gm);
		lay.setGraphNodeRenderer(rend);
		lay.setGraphEdgeRenderer(rend);
		setLayer(lay);
		Model.getPump().addModelEventListener(this,theActivityGraph,new String[] {"remove","namespace"});
	}
	private ActivityDiagramGraphModel createGraphModel() {
		if ((getGraphModel()instanceof ActivityDiagramGraphModel)) {
			return(ActivityDiagramGraphModel) getGraphModel();
		}else {
			return new ActivityDiagramGraphModel();
		}
	}
	public void propertyChange(PropertyChangeEvent evt) {
		if ((evt.getSource() == theActivityGraph)&&(evt instanceof DeleteInstanceEvent)&&"remove".equals(evt.getPropertyName())) {
			Model.getPump().removeModelEventListener(this,theActivityGraph,new String[] {"remove","namespace"});
			getProject().moveToTrash(this);
		}
		if (evt.getSource() == getStateMachine()) {
			Object newNamespace = Model.getFacade().getNamespace(getStateMachine());
			if (getNamespace() != newNamespace) {
				setNamespace(newNamespace);
				((UMLMutableGraphSupport) getGraphModel()).setHomeModel(newNamespace);
			}
		}
	}
	public Object getOwner() {
		if (!(getGraphModel()instanceof ActivityDiagramGraphModel)) {
			throw new IllegalStateException("Incorrect graph model of " + getGraphModel().getClass().getName());
		}
		ActivityDiagramGraphModel gm = (ActivityDiagramGraphModel) getGraphModel();
		return gm.getMachine();
	}
	public Object getStateMachine() {
		return null;
	}
	public void setStateMachine(Object sm) {
		if (!Model.getFacade().isAStateMachine(sm)) {
			throw new IllegalArgumentException();
		}
		((ActivityDiagramGraphModel) getGraphModel()).setMachine(sm);
	}
	protected Object[]getUmlActions() {
		Object[]actions =  {getActionState(),getActionTransition(),null,getActionFinalPseudoState(),getActionSwimlane(),null,getActionCallState(),getActionObjectFlowState(),null,getActionGuard(),getEffectActions()};
		return actions;
	}
	protected Object[]getEffectActions() {
		Object[]actions =  {getActionCallAction(),getActionCreateAction(),getActionDestroyAction(),getActionReturnAction(),getActionSendAction(),getActionTerminateAction(),getActionUninterpretedAction(),getActionActionSequence()};
		ToolBarUtility.manageDefault(actions,"diagram.activity.effect");
		return actions;
	}
	public String getLabelName() {
		return Translator.localize("label.activity-diagram");
	}
	protected Action getActionCallState() {
		if (actionCallState == null) {
			actionCallState = new RadioAction(new CmdCreateNode(Model.getMetaTypes().getCallState(),"button.new-callstate"));
		}
		return actionCallState;
	}
	protected Action getActionFinalPseudoState() {
		if (actionFinalPseudoState == null) {
			actionFinalPseudoState = new RadioAction(new CmdCreateNode(Model.getMetaTypes().getFinalState(),"button.new-finalstate"));
		}
		return actionFinalPseudoState;
	}
	protected Action getActionSwimlane() {
		if (actionSwimlane == null) {
			actionSwimlane = new ActionCreatePartition(getStateMachine());
		}
		return actionSwimlane;
	}
	protected Action getActionObjectFlowState() {
		if (actionObjectFlowState == null) {
			actionObjectFlowState = new RadioAction(new CmdCreateNode(Model.getMetaTypes().getObjectFlowState(),"button.new-objectflowstate"));
		}
		return actionObjectFlowState;
	}
	protected Action getActionState() {
		if (actionState == null) {
			actionState = new RadioAction(new CmdCreateNode(Model.getMetaTypes().getActionState(),"button.new-actionstate"));
		}
		return actionState;
	}
	protected Action getActionSubactivityState() {
		if (actionSubactivityState == null) {
			actionSubactivityState = new RadioAction(new CmdCreateNode(Model.getMetaTypes().getSubactivityState(),"button.new-subactivitystate"));
		}
		return actionSubactivityState;
	}
	protected Action getActionTransition() {
		if (actionTransition == null) {
			actionTransition = new RadioAction(new ActionSetMode(ModeCreatePolyEdge.class,"edgeClass",Model.getMetaTypes().getTransition(),"button.new-transition"));
		}
		return actionTransition;
	}
	protected Action getActionGuard() {
		if (actionGuard == null) {
			actionGuard = new ButtonActionNewGuard();
		}
		return actionGuard;
	}
	protected Action getActionCallAction() {
		if (actionCallAction == null) {
			actionCallAction = ActionNewCallAction.getButtonInstance();
		}
		return actionCallAction;
	}
	protected Action getActionCreateAction() {
		if (actionCreateAction == null) {
			actionCreateAction = ActionNewCreateAction.getButtonInstance();
		}
		return actionCreateAction;
	}
	protected Action getActionDestroyAction() {
		if (actionDestroyAction == null) {
			actionDestroyAction = ActionNewDestroyAction.getButtonInstance();
		}
		return actionDestroyAction;
	}
	protected Action getActionReturnAction() {
		if (actionReturnAction == null) {
			actionReturnAction = ActionNewReturnAction.getButtonInstance();
		}
		return actionReturnAction;
	}
	protected Action getActionSendAction() {
		if (actionSendAction == null) {
			actionSendAction = ActionNewSendAction.getButtonInstance();
		}
		return actionSendAction;
	}
	protected Action getActionTerminateAction() {
		if (actionTerminateAction == null) {
			actionTerminateAction = ActionNewTerminateAction.getButtonInstance();
		}
		return actionTerminateAction;
	}
	protected Action getActionUninterpretedAction() {
		if (actionUninterpretedAction == null) {
			actionUninterpretedAction = ActionNewUninterpretedAction.getButtonInstance();
		}
		return actionUninterpretedAction;
	}
	protected Action getActionActionSequence() {
		if (actionActionSequence == null) {
			actionActionSequence = ActionNewActionSequence.getButtonInstance();
		}
		return actionActionSequence;
	}
	public Object getDependentElement() {
		return getStateMachine();
	}
	public boolean isRelocationAllowed(Object base) {
		return false;
	}
	@SuppressWarnings("unchecked")public Collection getRelocationCandidates(Object root) {
		Collection c = new HashSet();
		c.add(getOwner());
		return c;
	}
	public boolean relocate(Object base) {
		return false;
	}
	@Override public void postLoad() {
		FigPartition previous = null;
		HashMap map = new HashMap();
		Iterator it = new ArrayList(getLayer().getContents()).iterator();
		while (it.hasNext()) {
			Fig f = (Fig) it.next();
			if (f instanceof FigPartition) {
				map.put(Integer.valueOf(f.getX()),f);
			}
		}
		List xList = new ArrayList(map.keySet());
		Collections.sort(xList);
		it = xList.iterator();
		while (it.hasNext()) {
			Fig f = (Fig) map.get(it.next());
			if (f instanceof FigPartition) {
				FigPartition fp = (FigPartition) f;
				if (previous != null) {
					previous.setNextPartition(fp);
				}
				fp.setPreviousPartition(previous);
				fp.setNextPartition(null);
				previous = fp;
			}
		}
	}
	public void encloserChanged(FigNode enclosed,FigNode oldEncloser,FigNode newEncloser) {
		if (oldEncloser == null&&newEncloser == null) {
			return;
		}
		if (enclosed instanceof FigObjectFlowState) {
			changePartition(enclosed);
		}
	}
	private void changePartition(FigNode enclosed) {
		assert enclosed != null;
		Object state = enclosed.getOwner();
		ActivityGraphsHelper activityGraph = Model.getActivityGraphsHelper();
		for (Object f:getLayer().getContentsNoEdges()) {
			if (f instanceof FigPartition) {
				FigPartition fig = (FigPartition) f;
				Object partition = fig.getOwner();
				if (fig.getBounds().intersects(enclosed.getBounds())) {
					activityGraph.addContent(partition,state);
				}else if (isStateInPartition(state,partition)) {
					activityGraph.removeContent(partition,state);
				}
			}
		}
	}
	private boolean isStateInPartition(Object state,Object partition) {
		return Model.getFacade().getContents(partition).contains(state);
	}
	@Override public boolean doesAccept(Object objectToAccept) {
		if (Model.getFacade().isAPartition(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isAState(objectToAccept)) {
			return true;
		}else if (Model.getFacade().isAPseudostate(objectToAccept)) {
			Object kind = Model.getFacade().getKind(objectToAccept);
			if (kind == null) {
				return false;
			}
			if (kind.equals(Model.getPseudostateKind().getShallowHistory())) {
				return false;
			}else if (kind.equals(Model.getPseudostateKind().getDeepHistory())) {
				return false;
			}
			return true;
		}else if (Model.getFacade().isAComment(objectToAccept)) {
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
		if (Model.getFacade().isAPartition(droppedObject)) {
			figNode = new FigPartition(droppedObject,bounds,settings);
		}else if (Model.getFacade().isAActionState(droppedObject)) {
			figNode = new FigActionState(droppedObject,bounds,settings);
		}else if (Model.getFacade().isACallState(droppedObject)) {
			figNode = new FigCallState(droppedObject,bounds,settings);
		}else if (Model.getFacade().isAObjectFlowState(droppedObject)) {
			figNode = new FigObjectFlowState(droppedObject,bounds,settings);
		}else if (Model.getFacade().isASubactivityState(droppedObject)) {
			figNode = new FigSubactivityState(droppedObject,bounds,settings);
		}else if (Model.getFacade().isAFinalState(droppedObject)) {
			figNode = new FigFinalState(droppedObject,bounds,settings);
		}else if (Model.getFacade().isAPseudostate(droppedObject)) {
			Object kind = Model.getFacade().getKind(droppedObject);
			if (kind == null) {
				return null;
			}
		}else if (Model.getFacade().isAComment(droppedObject)) {
			figNode = new FigComment(droppedObject,bounds,settings);
		}
		if (figNode != null) {
			if (location != null) {
				figNode.setLocation(location.x,location.y);
			}
		}
		return figNode;
	}
}



