package org.argouml.profile.internal;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.profile.CoreProfileReference;
import org.argouml.profile.DefaultTypeStrategy;
import org.argouml.profile.FormatingStrategy;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileModelLoader;
import org.argouml.profile.ProfileReference;
import org.argouml.profile.ResourceModelLoader;
import org.argouml.profile.internal.ocl.CrOCL;
import org.argouml.profile.internal.ocl.InvalidOclException;
import org.argouml.uml.cognitive.critics.CrAssocNameConflict;
import org.argouml.uml.cognitive.critics.CrAttrNameConflict;
import org.argouml.uml.cognitive.critics.CrCircularAssocClass;
import org.argouml.uml.cognitive.critics.CrCircularInheritance;
import org.argouml.uml.cognitive.critics.CrClassMustBeAbstract;
import org.argouml.uml.cognitive.critics.CrCrossNamespaceAssoc;
import org.argouml.uml.cognitive.critics.CrDupParamName;
import org.argouml.uml.cognitive.critics.CrDupRoleNames;
import org.argouml.uml.cognitive.critics.CrFinalSubclassed;
import org.argouml.uml.cognitive.critics.CrForkOutgoingTransition;
import org.argouml.uml.cognitive.critics.CrIllegalGeneralization;
import org.argouml.uml.cognitive.critics.CrInterfaceAllPublic;
import org.argouml.uml.cognitive.critics.CrInterfaceOperOnly;
import org.argouml.uml.cognitive.critics.CrInvalidBranch;
import org.argouml.uml.cognitive.critics.CrInvalidFork;
import org.argouml.uml.cognitive.critics.CrInvalidHistory;
import org.argouml.uml.cognitive.critics.CrInvalidInitial;
import org.argouml.uml.cognitive.critics.CrInvalidJoin;
import org.argouml.uml.cognitive.critics.CrInvalidJoinTriggerOrGuard;
import org.argouml.uml.cognitive.critics.CrInvalidPseudoStateTrigger;
import org.argouml.uml.cognitive.critics.CrInvalidSynch;
import org.argouml.uml.cognitive.critics.CrJoinIncomingTransition;
import org.argouml.uml.cognitive.critics.CrMultiComposite;
import org.argouml.uml.cognitive.critics.CrMultipleAgg;
import org.argouml.uml.cognitive.critics.CrMultipleDeepHistoryStates;
import org.argouml.uml.cognitive.critics.CrMultipleShallowHistoryStates;
import org.argouml.uml.cognitive.critics.CrNWayAgg;
import org.argouml.uml.cognitive.critics.CrNameConflict;
import org.argouml.uml.cognitive.critics.CrNameConflictAC;
import org.argouml.uml.cognitive.critics.CrNameConfusion;
import org.argouml.uml.cognitive.critics.CrOppEndConflict;
import org.argouml.uml.cognitive.critics.CrOppEndVsAttr;


public class ProfileUML extends Profile {
	private static final String PROFILE_FILE = "default-uml14.xmi";
	static final String NAME = "UML 1.4";
	private FormatingStrategy formatingStrategy;
	private ProfileModelLoader profileModelLoader;
	private Collection model;
	@SuppressWarnings("unchecked")ProfileUML()throws ProfileException {
		formatingStrategy = new JavaFormatingStrategy();
		profileModelLoader = new ResourceModelLoader();
		ProfileReference profileReference = null;
		try {
			profileReference = new CoreProfileReference(PROFILE_FILE);
		}catch (MalformedURLException e) {
			throw new ProfileException("Exception while creating profile reference.",e);
		}
		model = profileModelLoader.loadModel(profileReference);
		if (model == null) {
			model = new ArrayList();
			model.add(Model.getModelManagementFactory().createModel());
		}
		loadWellFormednessRules();
	}
	private void loadWellFormednessRules() {
		Set<Critic>critics = new HashSet<Critic>();
		critics.add(new CrAssocNameConflict());
		critics.add(new CrAttrNameConflict());
		critics.add(new CrCircularAssocClass());
		critics.add(new CrCircularInheritance());
		critics.add(new CrClassMustBeAbstract());
		critics.add(new CrCrossNamespaceAssoc());
		critics.add(new CrDupParamName());
		critics.add(new CrDupRoleNames());
		critics.add(new CrNameConfusion());
		critics.add(new CrInvalidHistory());
		critics.add(new CrInvalidSynch());
		critics.add(new CrInvalidJoinTriggerOrGuard());
		critics.add(new CrInvalidPseudoStateTrigger());
		critics.add(new CrInvalidInitial());
		critics.add(new CrInvalidJoin());
		critics.add(new CrInvalidFork());
		critics.add(new CrInvalidBranch());
		critics.add(new CrMultipleDeepHistoryStates());
		critics.add(new CrMultipleShallowHistoryStates());
		critics.add(new CrForkOutgoingTransition());
		critics.add(new CrJoinIncomingTransition());
		critics.add(new CrFinalSubclassed());
		critics.add(new CrIllegalGeneralization());
		critics.add(new CrInterfaceAllPublic());
		critics.add(new CrInterfaceOperOnly());
		critics.add(new CrMultipleAgg());
		critics.add(new CrNWayAgg());
		critics.add(new CrNameConflictAC());
		critics.add(new CrOppEndConflict());
		critics.add(new CrMultiComposite());
		critics.add(new CrNameConflict());
		critics.add(new CrOppEndVsAttr());
		try {
			critics.add(new CrOCL("context AssociationClass inv:" + "self.allConnections->" + "forAll( ar | self.allFeatures->" + "forAll( f | f.oclIsKindOf(StructuralFeature) " + "implies ar.name <> f.name ))","The names of the AssociationEnds and " + "the StructuralFeatures do not overlap.",null,ToDoItem.HIGH_PRIORITY,null,null,"http://www.uml.org/"));
		}catch (InvalidOclException e) {
			e.printStackTrace();
		}
		try {
			critics.add(new CrOCL("context AssociationClass inv:" + "self.allConnections->" + "forAll(ar | ar.participant <> self)","An AssociationClass cannot be defined " + "between itself and something else.",null,ToDoItem.HIGH_PRIORITY,null,null,"http://www.uml.org/"));
		}catch (InvalidOclException e) {
			e.printStackTrace();
		}
		try {
			critics.add(new CrOCL("context Classifier inv:" + "self.oppositeAssociationEnds->" + "forAll( o | not self.allAttributes->" + "union (self.allContents)->" + "collect ( q | q.name )->includes (o.name) )","The name of an opposite AssociationEnd may not be the same " + "as the name of an Attribute or a ModelElement contained " + "in the Classifier.",null,ToDoItem.HIGH_PRIORITY,null,null,"http://www.uml.org/"));
		}catch (InvalidOclException e) {
			e.printStackTrace();
		}
		try {
			critics.add(new CrOCL("context DataType inv:" + "self.allFeatures->forAll(f | f.oclIsKindOf(Operation)" + " and f.oclAsType(Operation).isQuery)","A DataType can only contain Operations, " + "which all must be queries.",null,ToDoItem.HIGH_PRIORITY,null,null,"http://www.uml.org/"));
		}catch (InvalidOclException e) {
			e.printStackTrace();
		}
		try {
			critics.add(new CrOCL("context GeneralizableElement inv:" + "self.isRoot implies self.generalization->isEmpty","A root cannot have any Generalizations.",null,ToDoItem.HIGH_PRIORITY,null,null,"http://www.uml.org/"));
		}catch (InvalidOclException e) {
			e.printStackTrace();
		}
		try {
			critics.add(new CrOCL("context GeneralizableElement inv:" + "self.generalization->" + "forAll(g |self.namespace.allContents->" + "includes(g.parent) )","The parent must be included in the Namespace of" + " the GeneralizableElement.",null,ToDoItem.HIGH_PRIORITY,null,null,"http://www.uml.org/"));
		}catch (InvalidOclException e) {
			e.printStackTrace();
		}
		try {
			critics.add(new CrOCL("context Namespace inv:" + "self.allContents -> select(x|x.oclIsKindOf(Association))->" + "forAll(a1, a2 |a1.name = a2.name and " + "a1.connection.participant = a2.connection.participant" + " implies a1 = a2)","All Associations must have a unique combination of name " + "and associated Classifiers in the Namespace.",null,ToDoItem.HIGH_PRIORITY,null,null,"http://www.uml.org/"));
		}catch (InvalidOclException e) {
			e.printStackTrace();
		}
		setCritics(critics);
	}
	@Override public FormatingStrategy getFormatingStrategy() {
		return formatingStrategy;
	}
	@Override public String getDisplayName() {
		return NAME;
	}
	@Override public Collection getProfilePackages() {
		return model;
	}
	@Override public DefaultTypeStrategy getDefaultTypeStrategy() {
		return new DefaultTypeStrategy() {
			public Object getDefaultAttributeType() {
				return ModelUtils.findTypeInModel("Integer",model.iterator().next());
			}
			public Object getDefaultParameterType() {
				return ModelUtils.findTypeInModel("Integer",model.iterator().next());
			}
			public Object getDefaultReturnType() {
				return null;
			}
		};
	}
}



