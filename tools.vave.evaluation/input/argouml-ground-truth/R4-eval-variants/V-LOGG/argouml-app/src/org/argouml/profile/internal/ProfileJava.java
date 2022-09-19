package org.argouml.profile.internal;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import org.argouml.model.Model;
import org.argouml.profile.CoreProfileReference;
import org.argouml.profile.DefaultTypeStrategy;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.ProfileModelLoader;
import org.argouml.profile.ProfileReference;
import org.argouml.profile.ResourceModelLoader;


public class ProfileJava extends Profile {
	private static final String PROFILE_FILE = "default-java.xmi";
	static final String NAME = "Java";
	private ProfileModelLoader profileModelLoader;
	private Collection model;
	@SuppressWarnings("unchecked")ProfileJava(Profile uml)throws ProfileException {
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
		addProfileDependency(uml);
		addProfileDependency("CodeGeneration");
	}
	ProfileJava()throws ProfileException {
			this(ProfileFacade.getManager().getProfileForClass(ProfileUML.class.getName()));
		}
	public String getDisplayName() {
		return NAME;
	}
	@Override public Collection getProfilePackages() {
		return model;
	}
	@Override public DefaultTypeStrategy getDefaultTypeStrategy() {
		return new DefaultTypeStrategy() {
			public Object getDefaultAttributeType() {
				return ModelUtils.findTypeInModel("int",model.iterator().next());
			}
			public Object getDefaultParameterType() {
				return ModelUtils.findTypeInModel("int",model.iterator().next());
			}
			public Object getDefaultReturnType() {
				return ModelUtils.findTypeInModel("void",model.iterator().next());
			}
		};
	}
}



