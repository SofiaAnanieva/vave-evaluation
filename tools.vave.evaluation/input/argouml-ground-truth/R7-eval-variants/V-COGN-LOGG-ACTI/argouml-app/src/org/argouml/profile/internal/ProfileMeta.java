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
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileModelLoader;
import org.argouml.profile.ProfileReference;
import org.argouml.profile.ResourceModelLoader;
import org.argouml.profile.internal.ocl.CrOCL;
import org.argouml.profile.internal.ocl.InvalidOclException;


public class ProfileMeta extends Profile {
	private static final String PROFILE_FILE = "metaprofile.xmi";
	private Collection model;
	@SuppressWarnings("unchecked")public ProfileMeta()throws ProfileException {
		ProfileModelLoader profileModelLoader = new ResourceModelLoader();
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
		try {
			critics.add(new CrOCL("context ModelElement inv: " + "self.taggedValue->" + "exists(x|x.type.name=\'Dependency\') implies " + "self.stereotype->exists(x|x.name = \'Profile\')","The \'Dependency\' tag definition should be applied" + " only to profiles.",null,ToDoItem.MED_PRIORITY,null,null,"http://argouml.tigris.org/"));
		}catch (InvalidOclException e) {
			e.printStackTrace();
		}
		try {
			critics.add(new CrOCL("context ModelElement inv: " + "self.taggedValue->" + "exists(x|x.type.name=\'Figure\') or " + "exists(x|x.type.name=\'Description\') or " + "exists(x|x.type.name=\'i18n\') or " + "exists(x|x.type.name=\'KnowledgeType\') or " + "exists(x|x.type.name=\'MoreInfoURL\') or " + "exists(x|x.type.name=\'Priority\') or " + "exists(x|x.type.name=\'Description\') or " + "exists(x|x.type.name=\'SupportedDecision\') or " + "exists(x|x.type.name=\'Headline\') " + "implies " + "self.stereotype->exists(x|x.name = \'Critic\')","Misuse of Metaprofile TaggedValues","The \'Figure\', \'i18n\', \'KnowledgeType\', \'MoreInfoURL\', " + "\'Priority\', \'SupportedDecision\', \'Description\' " + "and \'Headline\' tag definitions " + "should be applied only to OCL critics.",ToDoItem.MED_PRIORITY,null,null,"http://argouml.tigris.org/"));
		}catch (InvalidOclException e) {
			e.printStackTrace();
		}
		try {
			critics.add(new CrOCL("context Stereotype inv: " + "self.namespace.stereotype->exists(x|x.name = \'Profile\')","Stereotypes should be declared inside a Profile. ","Please add the <<Profile>> stereotype to " + "the containing Namespace",ToDoItem.MED_PRIORITY,null,null,"http://argouml.tigris.org/"));
		}catch (InvalidOclException e) {
			e.printStackTrace();
		}
		setCritics(critics);
	}
	@Override public String getDisplayName() {
		return"MetaProfile";
	}
	@Override public Collection getProfilePackages()throws ProfileException {
		return model;
	}
}



