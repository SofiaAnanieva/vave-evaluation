package org.argouml.uml.ui;

import org.argouml.model.Model;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelActionState;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelActivityGraph;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelCallState;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelClassifierInState;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelObjectFlowState;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelPartition;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelSubactivityState;
import org.argouml.uml.ui.behavior.collaborations.PropPanelAssociationEndRole;
import org.argouml.uml.ui.behavior.collaborations.PropPanelAssociationRole;
import org.argouml.uml.ui.behavior.collaborations.PropPanelClassifierRole;
import org.argouml.uml.ui.behavior.collaborations.PropPanelCollaboration;
import org.argouml.uml.ui.behavior.collaborations.PropPanelInteraction;
import org.argouml.uml.ui.behavior.collaborations.PropPanelMessage;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelActionSequence;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelArgument;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelCallAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelComponentInstance;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelCreateAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelDestroyAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelException;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelLink;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelLinkEnd;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelNodeInstance;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelObject;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelReception;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelReturnAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelSendAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelSignal;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelStimulus;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelTerminateAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelUninterpretedAction;
import org.argouml.uml.ui.behavior.state_machines.PropPanelCallEvent;
import org.argouml.uml.ui.behavior.state_machines.PropPanelChangeEvent;
import org.argouml.uml.ui.behavior.state_machines.PropPanelCompositeState;
import org.argouml.uml.ui.behavior.state_machines.PropPanelFinalState;
import org.argouml.uml.ui.behavior.state_machines.PropPanelGuard;
import org.argouml.uml.ui.behavior.state_machines.PropPanelPseudostate;
import org.argouml.uml.ui.behavior.state_machines.PropPanelSignalEvent;
import org.argouml.uml.ui.behavior.state_machines.PropPanelSimpleState;
import org.argouml.uml.ui.behavior.state_machines.PropPanelStateMachine;
import org.argouml.uml.ui.behavior.state_machines.PropPanelStateVertex;
import org.argouml.uml.ui.behavior.state_machines.PropPanelSubmachineState;
import org.argouml.uml.ui.behavior.state_machines.PropPanelTimeEvent;
import org.argouml.uml.ui.behavior.state_machines.PropPanelTransition;
import org.argouml.uml.ui.foundation.core.PropPanelAbstraction;
import org.argouml.uml.ui.foundation.core.PropPanelAssociation;
import org.argouml.uml.ui.foundation.core.PropPanelAssociationClass;
import org.argouml.uml.ui.foundation.core.PropPanelAssociationEnd;
import org.argouml.uml.ui.foundation.core.PropPanelAttribute;
import org.argouml.uml.ui.foundation.core.PropPanelClass;
import org.argouml.uml.ui.foundation.core.PropPanelClassifier;
import org.argouml.uml.ui.foundation.core.PropPanelComment;
import org.argouml.uml.ui.foundation.core.PropPanelComponent;
import org.argouml.uml.ui.foundation.core.PropPanelConstraint;
import org.argouml.uml.ui.foundation.core.PropPanelDataType;
import org.argouml.uml.ui.foundation.core.PropPanelDependency;
import org.argouml.uml.ui.foundation.core.PropPanelEnumeration;
import org.argouml.uml.ui.foundation.core.PropPanelEnumerationLiteral;
import org.argouml.uml.ui.foundation.core.PropPanelFlow;
import org.argouml.uml.ui.foundation.core.PropPanelGeneralization;
import org.argouml.uml.ui.foundation.core.PropPanelInterface;
import org.argouml.uml.ui.foundation.core.PropPanelMethod;
import org.argouml.uml.ui.foundation.core.PropPanelNode;
import org.argouml.uml.ui.foundation.core.PropPanelOperation;
import org.argouml.uml.ui.foundation.core.PropPanelParameter;
import org.argouml.uml.ui.foundation.core.PropPanelPermission;
import org.argouml.uml.ui.foundation.core.PropPanelRelationship;
import org.argouml.uml.ui.foundation.core.PropPanelUsage;
import org.argouml.uml.ui.foundation.extension_mechanisms.PropPanelStereotype;
import org.argouml.uml.ui.foundation.extension_mechanisms.PropPanelTagDefinition;
import org.argouml.uml.ui.foundation.extension_mechanisms.PropPanelTaggedValue;
import org.argouml.uml.ui.model_management.PropPanelModel;
import org.argouml.uml.ui.model_management.PropPanelPackage;
import org.argouml.uml.ui.model_management.PropPanelSubsystem;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.PropPanelFactory;


class ElementPropPanelFactory implements PropPanelFactory {
	public PropPanel createPropPanel(Object element) {
		if (Model.getFacade().isAElement(element)) {
			if (Model.getFacade().isASubsystem(element)) {
				return new PropPanelSubsystem();
			}else if (Model.getFacade().isAClassifier(element)) {
				return getClassifierPropPanel(element);
			}else if (Model.getFacade().isARelationship(element)) {
				return getRelationshipPropPanel(element);
			}else if (Model.getFacade().isAStateVertex(element)) {
				return getStateVertexPropPanel(element);
			}else if (Model.getFacade().isAActionSequence(element)) {
				return new PropPanelActionSequence();
			}else if (Model.getFacade().isAAction(element)) {
				return getActionPropPanel(element);
			}else if (Model.getFacade().isAActivityGraph(element)) {
				return new PropPanelActivityGraph();
			}else if (Model.getFacade().isAArgument(element)) {
				return new PropPanelArgument();
			}else if (Model.getFacade().isAAssociationEndRole(element)) {
				return new PropPanelAssociationEndRole();
			}else if (Model.getFacade().isAAssociationEnd(element)) {
				return new PropPanelAssociationEnd();
			}else if (Model.getFacade().isAAttribute(element)) {
				return new PropPanelAttribute();
			}else if (Model.getFacade().isACollaboration(element)) {
				return new PropPanelCollaboration();
			}else if (Model.getFacade().isAComment(element)) {
				return new PropPanelComment();
			}else if (Model.getFacade().isAComponentInstance(element)) {
				return new PropPanelComponentInstance();
			}else if (Model.getFacade().isAConstraint(element)) {
				return new PropPanelConstraint();
			}else if (Model.getFacade().isAEnumerationLiteral(element)) {
				return new PropPanelEnumerationLiteral();
			}else if (Model.getFacade().isAGuard(element)) {
				return new PropPanelGuard();
			}else if (Model.getFacade().isAInteraction(element)) {
				return new PropPanelInteraction();
			}else if (Model.getFacade().isALink(element)) {
				return new PropPanelLink();
			}else if (Model.getFacade().isALinkEnd(element)) {
				return new PropPanelLinkEnd();
			}else if (Model.getFacade().isAMessage(element)) {
				return new PropPanelMessage();
			}else if (Model.getFacade().isAMethod(element)) {
				return new PropPanelMethod();
			}else if (Model.getFacade().isAModel(element)) {
				return new PropPanelModel();
			}else if (Model.getFacade().isANodeInstance(element)) {
				return new PropPanelNodeInstance();
			}else if (Model.getFacade().isAObject(element)) {
				return new PropPanelObject();
			}else if (Model.getFacade().isAOperation(element)) {
				return new PropPanelOperation();
			}else if (Model.getFacade().isAPackage(element)) {
				return new PropPanelPackage();
			}else if (Model.getFacade().isAParameter(element)) {
				return new PropPanelParameter();
			}else if (Model.getFacade().isAPartition(element)) {
				return new PropPanelPartition();
			}else if (Model.getFacade().isAReception(element)) {
				return new PropPanelReception();
			}else if (Model.getFacade().isAStateMachine(element)) {
				return new PropPanelStateMachine();
			}else if (Model.getFacade().isAStereotype(element)) {
				return new PropPanelStereotype();
			}else if (Model.getFacade().isAStimulus(element)) {
				return new PropPanelStimulus();
			}else if (Model.getFacade().isATaggedValue(element)) {
				return new PropPanelTaggedValue();
			}else if (Model.getFacade().isATagDefinition(element)) {
				return new PropPanelTagDefinition();
			}else if (Model.getFacade().isATransition(element)) {
				return new PropPanelTransition();
			}else if (Model.getFacade().isACallEvent(element)) {
				return new PropPanelCallEvent();
			}else if (Model.getFacade().isAChangeEvent(element)) {
				return new PropPanelChangeEvent();
			}else if (Model.getFacade().isASignalEvent(element)) {
				return new PropPanelSignalEvent();
			}else if (Model.getFacade().isATimeEvent(element)) {
				return new PropPanelTimeEvent();
			}else if (Model.getFacade().isADependency(element)) {
				return new PropPanelDependency();
			}
			throw new IllegalArgumentException("Unsupported Element type");
		}
		return null;
	}
	private PropPanelClassifier getClassifierPropPanel(Object element) {
		if (Model.getFacade().isAAssociationClass(element)) {
			return new PropPanelAssociationClass();
		}else if (Model.getFacade().isAClass(element)) {
			return new PropPanelClass();
		}else if (Model.getFacade().isAClassifierInState(element)) {
			return new PropPanelClassifierInState();
		}else if (Model.getFacade().isAClassifierRole(element)) {
			return new PropPanelClassifierRole();
		}else if (Model.getFacade().isAComponent(element)) {
			return new PropPanelComponent();
		}else if (Model.getFacade().isADataType(element)) {
			if (Model.getFacade().isAEnumeration(element)) {
				return new PropPanelEnumeration();
			}else {
				return new PropPanelDataType();
			}
		}else if (Model.getFacade().isAInterface(element)) {
			return new PropPanelInterface();
		}else if (Model.getFacade().isANode(element)) {
			return new PropPanelNode();
		}else if (Model.getFacade().isASignal(element)) {
			if (Model.getFacade().isAException(element)) {
				return new PropPanelException();
			}else {
				return new PropPanelSignal();
			}
		}
		throw new IllegalArgumentException("Unsupported Element type");
	}
	private PropPanelRelationship getRelationshipPropPanel(Object element) {
		if (Model.getFacade().isAAssociation(element)) {
			if (Model.getFacade().isAAssociationRole(element)) {
				return new PropPanelAssociationRole();
			}else {
				return new PropPanelAssociation();
			}
		}else if (Model.getFacade().isADependency(element)) {
			if (Model.getFacade().isAAbstraction(element)) {
				return new PropPanelAbstraction();
			}else if (Model.getFacade().isAPackageImport(element)) {
				return new PropPanelPermission();
			}else if (Model.getFacade().isAUsage(element)) {
				return new PropPanelUsage();
			}else {
				return new PropPanelDependency();
			}
		}else if (Model.getFacade().isAFlow(element)) {
			return new PropPanelFlow();
		}else if (Model.getFacade().isAGeneralization(element)) {
			return new PropPanelGeneralization();
		}
		throw new IllegalArgumentException("Unsupported Relationship type");
	}
	private PropPanelAction getActionPropPanel(Object action) {
		if (Model.getFacade().isACallAction(action)) {
			return new PropPanelCallAction();
		}else if (Model.getFacade().isACreateAction(action)) {
			return new PropPanelCreateAction();
		}else if (Model.getFacade().isADestroyAction(action)) {
			return new PropPanelDestroyAction();
		}else if (Model.getFacade().isAReturnAction(action)) {
			return new PropPanelReturnAction();
		}else if (Model.getFacade().isASendAction(action)) {
			return new PropPanelSendAction();
		}else if (Model.getFacade().isATerminateAction(action)) {
			return new PropPanelTerminateAction();
		}else if (Model.getFacade().isAUninterpretedAction(action)) {
			return new PropPanelUninterpretedAction();
		}
		throw new IllegalArgumentException("Unsupported Action type");
	}
	private PropPanelStateVertex getStateVertexPropPanel(Object element) {
		if (Model.getFacade().isAState(element)) {
			if (Model.getFacade().isACallState(element)) {
				return new PropPanelCallState();
			}else if (Model.getFacade().isAActionState(element)) {
				return new PropPanelActionState();
			}else if (Model.getFacade().isACompositeState(element)) {
				if (Model.getFacade().isASubmachineState(element)) {
					if (Model.getFacade().isASubactivityState(element)) {
						return new PropPanelSubactivityState();
					}else {
						return new PropPanelSubmachineState();
					}
				}else {
					return new PropPanelCompositeState();
				}
			}else if (Model.getFacade().isAFinalState(element)) {
				return new PropPanelFinalState();
			}else if (Model.getFacade().isAObjectFlowState(element)) {
				return new PropPanelObjectFlowState();
			}else if (Model.getFacade().isASimpleState(element)) {
				return new PropPanelSimpleState();
			}
		}else if (Model.getFacade().isAPseudostate(element)) {
			return new PropPanelPseudostate();
		}
		throw new IllegalArgumentException("Unsupported State type");
	}
}



