package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.profile.Profile;


public class ProfileGoodPractices extends Profile {
	private Set<Critic>critics = new HashSet<Critic>();
	private CrMissingClassName crMissingClassName = new CrMissingClassName();
	public ProfileGoodPractices() {
		critics.add(new CrEmptyPackage());
		critics.add(new CrZeroLengthEdge());
		critics.add(new CrCircularComposition());
		critics.add(new CrMissingAttrName());
		critics.add(crMissingClassName);
		critics.add(new CrMissingStateName());
		critics.add(new CrMissingOperName());
		critics.add(new CrNonAggDataType());
		critics.add(new CrSubclassReference());
		critics.add(new CrTooManyAssoc());
		critics.add(new CrTooManyAttr());
		critics.add(new CrTooManyOper());
		critics.add(new CrTooManyTransitions());
		critics.add(new CrTooManyStates());
		critics.add(new CrTooManyClasses());
		critics.add(new CrUtilityViolated());
		this.setCritics(critics);
	}
	@Override public String getDisplayName() {
		return"Critics for Good Practices";
	}
	public String getProfileIdentifier() {
		return"GoodPractices";
	}
	public Critic getCrMissingClassName() {
		return crMissingClassName;
	}
}



