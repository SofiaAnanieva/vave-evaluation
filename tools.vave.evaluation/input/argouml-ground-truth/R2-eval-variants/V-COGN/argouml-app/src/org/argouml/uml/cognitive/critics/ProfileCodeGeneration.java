package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.CompoundCritic;
import org.argouml.cognitive.Critic;
import org.argouml.profile.Profile;


public class ProfileCodeGeneration extends Profile {
	private Set<Critic>critics = new HashSet<Critic>();
	private static Critic crMissingClassName;
	private static Critic crDisambigClassName = new CrDisambigClassName();
	private static Critic crNoTransitions = new CrNoTransitions();
	private static Critic crNoIncomingTransitions = new CrNoIncomingTransitions();
	private static Critic crNoOutgoingTransitions = new CrNoOutgoingTransitions();
	private static CompoundCritic crCompoundConstructorNeeded;
	private static CompoundCritic clsNaming;
	private static CompoundCritic noTrans1 = new CompoundCritic(crNoTransitions,crNoIncomingTransitions);
	private static CompoundCritic noTrans2 = new CompoundCritic(crNoTransitions,crNoOutgoingTransitions);
	public ProfileCodeGeneration(ProfileGoodPractices profileGoodPractices) {
		crMissingClassName = profileGoodPractices.getCrMissingClassName();
		crCompoundConstructorNeeded = new CompoundCritic(crMissingClassName,new CrConstructorNeeded());
		clsNaming = new CompoundCritic(crMissingClassName,crDisambigClassName);
		critics.add(crCompoundConstructorNeeded);
		critics.add(clsNaming);
		critics.add(new CrDisambigStateName());
		critics.add(crDisambigClassName);
		critics.add(new CrIllegalName());
		critics.add(new CrReservedName());
		critics.add(new CrNoInitialState());
		critics.add(new CrNoTriggerOrGuard());
		critics.add(new CrNoGuard());
		critics.add(new CrOperNameConflict());
		critics.add(new CrNoInstanceVariables());
		critics.add(new CrNoAssociations());
		critics.add(new CrNoOperations());
		critics.add(new CrUselessAbstract());
		critics.add(new CrUselessInterface());
		critics.add(new CrNavFromInterface());
		critics.add(new CrUnnavigableAssoc());
		critics.add(new CrAlreadyRealizes());
		critics.add(new CrMultipleInitialStates());
		critics.add(new CrUnconventionalOperName());
		critics.add(new CrUnconventionalAttrName());
		critics.add(new CrUnconventionalClassName());
		critics.add(new CrUnconventionalPackName());
		critics.add(noTrans1);
		critics.add(noTrans2);
		this.setCritics(critics);
		addProfileDependency("GoodPractices");
	}
	@Override public String getDisplayName() {
		return"Critics for Code Generation";
	}
	public String getProfileIdentifier() {
		return"CodeGeneration";
	}
}



